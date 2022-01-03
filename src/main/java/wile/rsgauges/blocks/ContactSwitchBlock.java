/*
 * @file ContactSwitchBlock.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Basic class for blocks representing redstone signal sources, like
 * the vanilla lever or button.
 */
package wile.rsgauges.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import wile.rsgauges.ModContent;
import wile.rsgauges.detail.ModResources;
import wile.rsgauges.libmc.detail.Auxiliaries;
import wile.rsgauges.libmc.detail.Overlay;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ContactSwitchBlock extends SwitchBlock
{
  public ContactSwitchBlock(long config, BlockBehaviour.Properties properties, AABB unrotatedBBUnpowered, @Nullable AABB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config|SwitchBlock.SWITCH_CONFIG_CONTACT, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound); }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction side)
  { return (side==null) || ((side==(Direction.UP)) && (!isWallMount())) || (side==(state.getValue(FACING).getOpposite())); }

  @Override
  public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float distance)
  {
    if(((config & SWITCH_CONFIG_SHOCK_SENSITIVE)!=0)) onEntityCollided(world, pos, world.getBlockState(pos));
    super.fallOn(world, state, pos, entity, distance);
  }

  @Override
  public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity)
  {
    if(world.isClientSide()) return;
    if(((config & (SWITCH_CONFIG_SHOCK_SENSITIVE|SWITCH_CONFIG_HIGH_SENSITIVE))==(SWITCH_CONFIG_SHOCK_SENSITIVE|SWITCH_CONFIG_HIGH_SENSITIVE)) && (!entity.isShiftKeyDown())) {
      onEntityCollided(world, pos, world.getBlockState(pos));
    }
  }

  @Override
  public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity)
  {
    if(world.isClientSide()) return;
    if(((config & SWITCH_CONFIG_SHOCK_SENSITIVE)!=0) && (entity.fallDistance < 0.2)) return;
    onEntityCollided(world, pos, state);
  }

  @Override
  public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random)
  { if(((config & SWITCH_CONFIG_SHOCK_SENSITIVE)!=0) || (!onEntityCollided(world, pos, state))) super.tick(state, world, pos, random); }

  protected boolean onEntityCollided(Level world, BlockPos pos, BlockState state)
  {
    if(world.isClientSide()) return false;
    ContactSwitchTileEntity te = getTe(world, pos);
    if(te == null) return false;
    boolean active = false;
    final boolean powered = state.getValue(POWERED);
    @SuppressWarnings("unchecked")
    List<Entity> hits = world.getEntitiesOfClass((Class<Entity>)te.filter_class(), detectionVolume(pos));
    if(hits.size() >= te.entity_count_threshold()) {
      if(te.high_sensitivity()) {
        active = true;
      } else {
        for(Entity e:hits) {
          if(!e.isIgnoringBlockTriggers()) {
            active = true;
            break;
          }
        }
      }
    }
    if(active) {
      int t = te.configured_on_time();
      te.on_timer_reset( (t<=0) ? (default_pulse_on_time) : (Math.max(t, 4)));
    }
    if(active && (!powered)) {
      state = state.setValue(POWERED, true);
      world.setBlock(pos, state, 1|2|8|16);
      power_on_sound.play(world, pos);
      notifyNeighbours(world, pos, state, te, false);
      if((config & SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT)!=0) {
        if(!te.activateSwitchLinks(te.on_power(), 15, true)) {
          ModResources.BlockSoundEvents.SWITCHLINK_LINK_PEAL_USE_FAILED.play(world, pos);
        }
      }
    }
    te.reschedule_block_tick();
    return active;
  }

  @Override
  @Nullable
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
  { return new ContactSwitchTileEntity(pos, state); }

  // -------------------------------------------------------------------------------------------------------------------

  protected AABB detectionVolume(BlockPos pos)
  { return new AABB(Vec3.atLowerCornerOf(pos), Vec3.atLowerCornerOf(pos).add(1,2,1)); }

  @Override
  public ContactSwitchTileEntity getTe(LevelReader world, BlockPos pos)
  {
    BlockEntity te = world.getBlockEntity(pos);
    if((!(te instanceof ContactSwitchTileEntity))) return null;
    return (ContactSwitchTileEntity)te;
  }

  // -------------------------------------------------------------------------------------------------------------------
  // Tile entity
  // -------------------------------------------------------------------------------------------------------------------

  /**
   * Tile entity
   */
  public static class ContactSwitchTileEntity extends SwitchTileEntity
  {
    public static final Class<?> filter_classes[] = { Entity.class, LivingEntity.class, Player.class, Monster.class, Animal.class, Villager.class, ItemEntity.class };
    public static final String filter_class_names[] = { "everything", "creatures", "players", "mobs", "animals", "villagers", "objects" };
    private static final int max_entity_count = 64;
    private boolean high_sensitivity_ = false;
    private int count_threshold_ = 1;
    private int filter_ = 0;

    public ContactSwitchTileEntity(BlockEntityType<?> te_type, BlockPos pos, BlockState state)
    { super(te_type, pos, state); }

    public ContactSwitchTileEntity(BlockPos pos, BlockState state)
    { super(ModContent.TET_CONTACT_SWITCH, pos, state); }

    public int filter()
    { return filter_; }

    public void filter(int sel)
    { filter_ = (sel<0) ? 0 : (sel >= filter_classes.length) ? (filter_classes.length-1) : sel; }

    public Class<?> filter_class()
    { return (filter_<=0) ? (filter_classes[0]) : ((filter_ >= filter_classes.length) ? (filter_classes[filter_classes.length-1]) : filter_classes[filter_]); }

    public boolean high_sensitivity()
    { return high_sensitivity_; }

    public void high_sensitivity(boolean sel)
    { high_sensitivity_ = sel; }

    public int entity_count_threshold()
    { return count_threshold_; }

    public void entity_count_threshold(int sel)
    { count_threshold_ = ((sel<1) ? 1 : Math.min(sel, max_entity_count)); }

    @Override
    public void write(CompoundTag nbt, boolean updatePacket)
    {
      super.write(nbt, updatePacket);
      nbt.putInt("filter", filter());
      nbt.putBoolean("highsensitive", high_sensitivity());
      nbt.putInt("entitythreshold", entity_count_threshold());
    }

    @Override
    public void read(CompoundTag nbt, boolean updatePacket)
    {
      super.read(nbt, updatePacket);
      filter(nbt.getInt("filter"));
      high_sensitivity(nbt.getBoolean("highsensitive"));
      entity_count_threshold(nbt.getInt("entitythreshold"));
    }

    @Override
    public void reset(@Nullable LevelReader world)
    {
      super.reset(world);
      filter_=0;
      count_threshold_=1;
      high_sensitivity_=false;
      configured_on_time(20);
    }

    @Override
    public boolean activation_config(BlockState state, @Nullable Player player, double x, double y, boolean show_only)
    {
      if(state == null) return false;
      final SwitchBlock block = (SwitchBlock)state.getBlock();
      int direction=0, field=0;
      if(block.isLateral()) {
        direction = ((y>=13) && (y<=15)) ? (1) : (((y>=10) && (y<=12)) ? (-1) : (0));
        field = ((x>=9.5) && (x<=10.1)) ? (1) : (
                ((x>=10.9) && (x<=11.7)) ? (2) : (
                ((x>=12.2) && (x<=13.0)) ? (3) : (
                ((x>=13.5) && (x<=14.2)) ? (4) : (0)
                )));
      }
      if((direction==0) || (field==0)) return false;
      if(!show_only) {
        switch (field) {
          case 1 -> high_sensitivity(direction > 0);
          case 2 -> entity_count_threshold(entity_count_threshold() + direction);
          case 3 -> filter(filter() + direction);
          case 4 -> on_power(Mth.clamp(on_power() + direction, 1, 15));
        }
        setChanged();
      }
      {
        Overlay.show(player,
          (new TextComponent(""))
            .append(Auxiliaries.localizable("switchconfig.touchcontactmat.sensitivity", ChatFormatting.BLUE, new Object[]{
                Auxiliaries.localizable("switchconfig.touchcontactmat.sensitivity." + (high_sensitivity() ? "high":"normal"))
              }))
            .append(" | ")
            .append(Auxiliaries.localizable("switchconfig.touchcontactmat.entity_threshold", ChatFormatting.YELLOW, new Object[]{entity_count_threshold()}))
            .append(" | ")
            .append(Auxiliaries.localizable("switchconfig.touchcontactmat.entity_filter", ChatFormatting.DARK_GREEN, new Object[]{new TranslatableComponent("rsgauges.switchconfig.touchcontactmat.entity_filter."+filter_class_names[filter_])}))
            .append(" | ")
            .append(Auxiliaries.localizable("switchconfig.touchcontactmat.output_power", ChatFormatting.RED, new Object[]{on_power()}))
        );
      }
      return true;
    }
  }
}
