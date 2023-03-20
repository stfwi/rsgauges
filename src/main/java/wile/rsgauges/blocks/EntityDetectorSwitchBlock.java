/*
 * @file DetectorSwitchTileEntity.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Auto switch specialised for item and entity detection.
 */
package wile.rsgauges.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import wile.rsgauges.ModConfig;
import wile.rsgauges.ModContent;
import wile.rsgauges.detail.ModResources;
import wile.rsgauges.detail.RsAuxiliaries;
import wile.rsgauges.libmc.detail.Auxiliaries;
import wile.rsgauges.libmc.detail.Overlay;

import javax.annotation.Nullable;
import java.util.List;


public class EntityDetectorSwitchBlock extends AutoSwitchBlock
{
  public EntityDetectorSwitchBlock(long config, BlockBehaviour.Properties properties, AABB unrotatedBBUnpowered, @Nullable AABB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound); }

  public EntityDetectorSwitchBlock(long config, BlockBehaviour.Properties properties, AABB unrotatedBBUnpowered, @Nullable AABB unrotatedBBPowered)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, null, null); }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  @Nullable
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
  { return new DetectorSwitchTileEntity(pos, state); }

  // -------------------------------------------------------------------------------------------------------------------
  // Tile entity
  // -------------------------------------------------------------------------------------------------------------------

  /**
   * Tile entity for entity detection based auto switches
   */
  public static class DetectorSwitchTileEntity extends AutoSwitchTileEntity
  {
    public static final Class<?> filter_classes[] = { LivingEntity.class, Player.class, Monster.class, Animal.class, Villager.class, ItemEntity.class, Entity.class };
    public static final String filter_class_names[] = { "creatures", "players", "mobs", "animals", "villagers", "objects", "everything" };
    private static final int max_sensor_range_ = 16;
    private int sensor_entity_count_threshold_ = 1;
    private int sensor_range_ = 5;
    private int filter_ = 0;
    private AABB area_ = null;
    private int update_interval_ = 8;
    private int update_timer_ = 0;

    public DetectorSwitchTileEntity(BlockEntityType<?> te_type, BlockPos pos, BlockState state)
    { super(te_type, pos, state); }

    public DetectorSwitchTileEntity(BlockPos pos, BlockState state)
    { super(ModContent.TET_DETECTOR_SWITCH, pos, state); }

    public int filter()
    { return filter_; }

    public void filter(int sel)
    { filter_ = (sel<0) ? 0 : (sel >= filter_classes.length) ? (filter_classes.length-1) : sel; }

    public Class<?> filter_class()
    { return (filter_<=0) ? (filter_classes[0]) : ((filter_ >= filter_classes.length) ? (filter_classes[filter_classes.length-1]) : filter_classes[filter_]); }

    public void sensor_entity_threshold(int count)
    { sensor_entity_count_threshold_ = Math.max(count, 1); }

    public int sensor_entity_threshold()
    { return sensor_entity_count_threshold_; }

    public void sensor_range(int r)
    { sensor_range_ = (r<1) ? (1) : (Math.min(r, max_sensor_range_)); }

    public int sensor_range()
    { return sensor_range_; }

    @Override
    public void write(CompoundTag nbt, boolean updatePacket)
    {
      super.write(nbt, updatePacket);
      nbt.putInt("range", sensor_range_);
      nbt.putInt("entitythreshold", sensor_entity_count_threshold_);
      nbt.putInt("filter", filter_);
    }

    @Override
    public void read(CompoundTag nbt, boolean updatePacket)
    {
      super.read(nbt, updatePacket);
      sensor_range(nbt.getInt("range"));
      sensor_entity_threshold(nbt.getInt("entitythreshold"));
      filter(nbt.getInt("filter"));
    }

    @Override
    public void reset(@Nullable LevelReader world)
    { super.reset(world); update_timer_=0; area_=null; sensor_range_=5; filter_=0; }

    @Override
    public boolean activation_config(BlockState state, @Nullable Player player, double x, double y, boolean show_only)
    {
      if(state == null) return false;
      final int direction = (y >= 12) ? (1) : ((y <= 5) ? (-1) : (0));
      final int field = ((x>=2) && (x<=3.95)) ? (1) : (
        ((x>=4.25) && (x<=7)) ? (2) : (
          ((x>=8) && (x<=10)) ? (3) : (
            ((x>=11) && (x<=13)) ? (4) : (0)
          )));
      if((direction==0) || (field==0)) return false;
      if(!show_only) {
        switch (field) {
          case 1 -> {
            sensor_range(sensor_range() + direction);
            area_ = null;
            break;
          }
          case 2 -> {
            sensor_entity_threshold(sensor_entity_threshold() + direction);
            break;
          }
          case 3 -> {
            filter(filter() + direction);
            break;
          }
          case 4 -> {
            setpower(setpower() + direction);
            if (setpower() < 1) setpower(1);
            break;
          }
        }
        setChanged();
      }
      {
        Overlay.show(player,
          (Component.literal(""))
            .append(Auxiliaries.localizable("switchconfig.detector.sensor_range", ChatFormatting.BLUE, new Object[]{sensor_range()}))
            .append(" | ")
            .append(Auxiliaries.localizable("switchconfig.detector.entity_threshold", ChatFormatting.YELLOW, new Object[]{sensor_entity_threshold()}))
            .append(" | ")
            .append(Auxiliaries.localizable("switchconfig.detector.entity_filter", ChatFormatting.DARK_GREEN, new Object[]{Component.translatable("rsgauges.switchconfig.detector.entity_filter."+filter_class_names[filter()])}))
            .append(" | ")
            .append(Auxiliaries.localizable("switchconfig.detector.output_power", ChatFormatting.RED, new Object[]{setpower()}))
        );
      }
      return true;
    }

    @Override
    public void tick()
    {
      if((level.isClientSide()) || (--update_timer_ > 0)) return;
      update_timer_ = update_interval_;
      BlockState state = level.getBlockState(getBlockPos());
      if((state==null) || (!(state.getBlock() instanceof AutoSwitchBlock))) return;
      AutoSwitchBlock block = (AutoSwitchBlock)(state.getBlock());
      // initialisations
      if(update_interval_ == 0) {
        if((block.config & SWITCH_CONFIG_SENSOR_LINEAR) != 0) {
          update_interval_ = ModConfig.autoswitch_linear_update_interval;
        } else {
          update_interval_ = ModConfig.autoswitch_volumetric_update_interval;
        }
      }
      if(area_ == null) {
        int size = sensor_range();
        AABB range_bb;
        if((block.config & SWITCH_CONFIG_SENSOR_VOLUME) != 0) {
          range_bb = new AABB(0,-2,-size, size,2,size);
        } else if((block.config & SWITCH_CONFIG_SENSOR_LINEAR) != 0) {
          range_bb = new AABB(-0.5,-0.5,-0.5, size,0.5,0.5);
        } else {
          range_bb = new AABB(0,0,0, 1,0,0);
        }
        Direction facing = state.getValue(FACING);
        AABB bb = RsAuxiliaries.transform_forward(range_bb, facing).move(getBlockPos()).expandTowards(1,1,1);
        area_ = new AABB(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
      }
      // measurement
      boolean active = false;
      @SuppressWarnings("unchecked")
      List<Entity> hits = level.getEntitiesOfClass((Class<Entity>)filter_class(), area_);
      if(hits.size() >= sensor_entity_count_threshold_) {
        int num_seen = 0;
        final Vec3 switch_position = new Vec3((double)getBlockPos().getX()+.5, (double)getBlockPos().getY()+.5, (double)getBlockPos().getZ()+.5);
        for(Entity e:hits) {
          if(e instanceof HangingEntity) continue;
          if(
            (
              level.clip(
                new ClipContext(
                  new Vec3(e.blockPosition().getX()-0.2, e.blockPosition().getY()+e.getEyeHeight(), e.blockPosition().getZ()-0.2),
                  switch_position,
                  ClipContext.Block.OUTLINE,
                  ClipContext.Fluid.NONE,
                  e
                )
              ).getType() != HitResult.Type.BLOCK
            )
            ||
            (
              level.clip(
                new ClipContext(
                  new Vec3(e.blockPosition().getX()+0.2, e.blockPosition().getY()+e.getEyeHeight(), e.blockPosition().getZ()+0.2),
                  switch_position,
                  ClipContext.Block.OUTLINE,
                  ClipContext.Fluid.NONE,
                  e
                )
              ).getType() != HitResult.Type.BLOCK
            )
          ) {
            if(++num_seen >= sensor_entity_count_threshold_) {
              active = true;
              break;
            }
          }
        }
      }
      // state setting
      updateSwitchState(state, block, active, configured_on_time());
    }
  }

}
