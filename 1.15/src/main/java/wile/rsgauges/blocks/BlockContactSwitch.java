/*
 * @file BlockContactSwitch.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Basic class for blocks representing redstone signal sources, like
 * the vanilla lever or button.
 */
package wile.rsgauges.blocks;

import wile.rsgauges.ModContent;
import wile.rsgauges.detail.ModAuxiliaries;
import wile.rsgauges.detail.ModResources;
import wile.rsgauges.items.ItemSwitchLinkPearl;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.monster.*;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockContactSwitch extends BlockSwitch
{
  public BlockContactSwitch(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config|BlockSwitch.SWITCH_CONFIG_CONTACT, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound); }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side)
  { return (side==null) || ((side==(Direction.UP)) && (!isWallMount())) || (side==(state.get(FACING).getOpposite())); }

  @Override
  public void onFallenUpon(World world, BlockPos pos, Entity entity, float distance)
  {
    if(((config & SWITCH_CONFIG_SHOCK_SENSITIVE)!=0)) onEntityCollided(world, pos, world.getBlockState(pos));
    super.onFallenUpon(world, pos, entity, distance);
  }

  @Override
  public void onEntityWalk(World world, BlockPos pos, Entity entity)
  {
    if(world.isRemote) return;
    if(((config & (SWITCH_CONFIG_SHOCK_SENSITIVE|SWITCH_CONFIG_HIGH_SENSITIVE))==(SWITCH_CONFIG_SHOCK_SENSITIVE|SWITCH_CONFIG_HIGH_SENSITIVE)) && (!entity.func_225608_bj_()/*isSneaking()*/)) {
      onEntityCollided(world, pos, world.getBlockState(pos));
    }
  }

  @Override
  public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity)
  {
    if(world.isRemote) return;
    if(((config & SWITCH_CONFIG_SHOCK_SENSITIVE)!=0) && (entity.fallDistance < 0.2)) return;
    onEntityCollided(world, pos, state);
  }

  @Override
  public void tick(BlockState state, World world, BlockPos pos, Random random)
  { if(((config & SWITCH_CONFIG_SHOCK_SENSITIVE)!=0) || (!onEntityCollided(world, pos, state))) super.tick(state, world, pos, random); }

  protected boolean onEntityCollided(World world, BlockPos pos, BlockState state)
  {
    if(world.isRemote) return false;
    TileEntityContactSwitch te = getTe(world, pos);
    if(te == null) return false;
    boolean active = false;
    final boolean powered = state.get(POWERED);
    if(powered && (te.on_time_remaining() > 3) && ((world.getGameTime() & 0x3)!=0)) {
      active = true; // anyway on at the next update.
    } else {
      @SuppressWarnings("unchecked")
      List<Entity> hits = world.getEntitiesWithinAABB((Class<Entity>)te.filter_class(), detectionVolume(pos));
      if(hits.size() >= te.entity_count_threshold()) {
        if(te.high_sensitivity()) {
          active = true;
        } else {
          for(Entity e:hits) {
            if(!e.doesEntityNotTriggerPressurePlate()) {
              active = true;
              break;
            }
          }
        }
      }
    }
    if(active) {
      int t = te.configured_on_time();
      te.on_timer_reset( (t<=0) ? (default_pulse_on_time) : ((t<4) ? 4 : t));
    }
    if(active && (!powered)) {
      state = state.with(POWERED, true);
      world.setBlockState(pos, state, 1|2);
      power_on_sound.play(world, pos);
      notifyNeighbours(world, pos, state, te, false);
      if((config & BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT)!=0) {
        if(!te.activate_links(ItemSwitchLinkPearl.SwitchLink.SWITCHLINK_RELAY_ACTIVATE)) {
          ModResources.BlockSoundEvents.SWITCHLINK_LINK_PEAL_USE_FAILED.play(world, pos);
        }
      }
    }
    te.reschedule_block_tick();
    return active;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world)
  { return new TileEntityContactSwitch(ModContent.TET_CONTACT_SWITCH); }

  // -------------------------------------------------------------------------------------------------------------------

  protected AxisAlignedBB detectionVolume(BlockPos pos)
  { return new AxisAlignedBB(pos, pos.add(1,2,1)); }

  @Override
  public TileEntityContactSwitch getTe(IWorldReader world, BlockPos pos)
  {
    TileEntity te = world.getTileEntity(pos);
    if((!(te instanceof TileEntityContactSwitch))) return null;
    return (TileEntityContactSwitch)te;
  }

  // -------------------------------------------------------------------------------------------------------------------
  // Tile entity
  // -------------------------------------------------------------------------------------------------------------------

  /**
   * Tile entity
   */
  public static class TileEntityContactSwitch extends TileEntitySwitch
  {
    public static final Class<?> filter_classes[] = { Entity.class, LivingEntity.class, PlayerEntity.class, MonsterEntity.class, AnimalEntity.class, VillagerEntity.class, ItemEntity.class };
    public static final String filter_class_names[] = { "everything", "creatures", "players", "mobs", "animals", "villagers", "objects" };
    private static final int max_entity_count = 64;
    private boolean high_sensitivity_ = false;
    private int count_threshold_ = 1;
    private int filter_ = 0;

    public TileEntityContactSwitch(TileEntityType<?> te_type)
    { super(te_type); }

    public TileEntityContactSwitch()
    { super(ModContent.TET_CONTACT_SWITCH); }

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
    { count_threshold_ = ((sel<1) ? 1 : ((sel>=max_entity_count)) ? max_entity_count : sel); }

    @Override
    public void writeNbt(CompoundNBT nbt, boolean updatePacket)
    {
      super.writeNbt(nbt, updatePacket);
      nbt.putInt("filter", filter());
      nbt.putBoolean("highsensitive", high_sensitivity());
      nbt.putInt("entitythreshold", entity_count_threshold());
    }

    @Override
    public void readNbt(CompoundNBT nbt, boolean updatePacket)
    {
      super.readNbt(nbt, updatePacket);
      filter(nbt.getInt("filter"));
      high_sensitivity(nbt.getBoolean("highsensitive"));
      entity_count_threshold(nbt.getInt("entitythreshold"));
    }

    @Override
    public void reset()
    { super.reset(); filter_=0; count_threshold_=1; high_sensitivity_=false; }

    @Override
    public boolean activation_config(BlockState state, @Nullable PlayerEntity player, double x, double y)
    {
      if(state == null) return false;
      final BlockSwitch block = (BlockSwitch)state.getBlock();
      int direction=0, field=0;
      if((block.config & (SWITCH_CONFIG_LATERAL)) != 0) {
        direction = ((y>=13) && (y<=15)) ? (1) : (((y>=10) && (y<=12)) ? (-1) : (0));
        field = ((x>=9.5) && (x<=10.1)) ? (1) : (
                ((x>=10.9) && (x<=11.7)) ? (2) : (
                ((x>=12.2) && (x<=13.0)) ? (3) : (
                ((x>=13.5) && (x<=14.2)) ? (4) : (0)
                )));
      }
      if((direction==0) || (field==0)) return false;
      switch(field) {
        case 1: {
          high_sensitivity(direction > 0);
          ModAuxiliaries.playerStatusMessage(player,
            ModAuxiliaries.localizable("switchconfig.touchcontactmat.sensitivity", TextFormatting.BLUE, new Object[]{
              ModAuxiliaries.localizable("switchconfig.touchcontactmat.sensitivity." + (high_sensitivity() ? "high":"normal"), null)
            })
          );
          break;
        }
        case 2: {
          entity_count_threshold(entity_count_threshold() + direction);
          ModAuxiliaries.playerStatusMessage(player,
            ModAuxiliaries.localizable("switchconfig.touchcontactmat.entity_threshold", TextFormatting.YELLOW, new Object[]{entity_count_threshold()})
          );
          break;
        }
        case 3: {
          filter(filter() + direction);
          ModAuxiliaries.playerStatusMessage(player,
            ModAuxiliaries.localizable("switchconfig.touchcontactmat.entity_filter", TextFormatting.DARK_GREEN, new Object[]{new TranslationTextComponent("rsgauges.switchconfig.touchcontactmat.entity_filter."+filter_class_names[filter_])})
          );
          break;
        }
        case 4: {
          on_power(on_power() + direction);
          if(on_power() < 1) on_power(1);
          ModAuxiliaries.playerStatusMessage(player,
            ModAuxiliaries.localizable("switchconfig.touchcontactmat.output_power", TextFormatting.RED, new Object[]{on_power()})
          );
          break;
        }
      }
      markDirty();
      return true;
    }
  }
}
