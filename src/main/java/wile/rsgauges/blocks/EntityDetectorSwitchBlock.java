/*
 * @file BlockEntityDetectorSwitch.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Auto switch specialised for item and entity detection.
 */
package wile.rsgauges.blocks;

import net.minecraft.util.text.*;
import net.minecraft.world.IWorldReader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.tileentity.TileEntity;
import wile.rsgauges.ModContent;
import wile.rsgauges.ModConfig;
import wile.rsgauges.libmc.detail.Auxiliaries;
import wile.rsgauges.libmc.detail.Overlay;
import wile.rsgauges.detail.ModResources;

import javax.annotation.Nullable;
import java.util.List;

public class EntityDetectorSwitchBlock extends AutoSwitchBlock
{
  public EntityDetectorSwitchBlock(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound); }

  public EntityDetectorSwitchBlock(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, null, null); }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world)
  { return new DetectorSwitchTileEntity(ModContent.TET_DETECTOR_SWITCH); }

  // -------------------------------------------------------------------------------------------------------------------
  // Tile entity
  // -------------------------------------------------------------------------------------------------------------------

  /**
   * Tile entity for entity detection based auto switches
   */
  public static class DetectorSwitchTileEntity extends TileEntityAutoSwitch implements ITickableTileEntity
  {
    public static final Class<?> filter_classes[] = { LivingEntity.class, PlayerEntity.class, MonsterEntity.class, AnimalEntity.class, VillagerEntity.class, ItemEntity.class, Entity.class };
    public static final String filter_class_names[] = { "creatures", "players", "mobs", "animals", "villagers", "objects", "everything" };
    private static final int max_sensor_range_ = 16;
    private int sensor_entity_count_threshold_ = 1;
    private int sensor_range_ = 5;
    private int filter_ = 0;
    private AxisAlignedBB area_ = null;
    private int update_interval_ = 8;
    private int update_timer_ = 0;

    public DetectorSwitchTileEntity(TileEntityType<?> te_type)
    { super(te_type); }

    public DetectorSwitchTileEntity()
    { super(ModContent.TET_DETECTOR_SWITCH); }

    public int filter()
    { return filter_; }

    public void filter(int sel)
    { filter_ = (sel<0) ? 0 : (sel >= filter_classes.length) ? (filter_classes.length-1) : sel; }

    public Class<?> filter_class()
    { return (filter_<=0) ? (filter_classes[0]) : ((filter_ >= filter_classes.length) ? (filter_classes[filter_classes.length-1]) : filter_classes[filter_]); }

    public void sensor_entity_threshold(int count)
    { sensor_entity_count_threshold_ = (count < 1) ? 1 : count; }

    public int sensor_entity_threshold()
    { return sensor_entity_count_threshold_; }

    public void sensor_range(int r)
    { sensor_range_ = (r<1) ? (1) : ((r>max_sensor_range_) ? max_sensor_range_ : r); }

    public int sensor_range()
    { return sensor_range_; }

    @Override
    public void write(CompoundNBT nbt, boolean updatePacket)
    {
      super.write(nbt, updatePacket);
      nbt.putInt("range", sensor_range_);
      nbt.putInt("entitythreshold", sensor_entity_count_threshold_);
      nbt.putInt("filter", filter_);
    }

    @Override
    public void read(CompoundNBT nbt, boolean updatePacket)
    {
      super.read(nbt, updatePacket);
      sensor_range(nbt.getInt("range"));
      sensor_entity_threshold(nbt.getInt("entitythreshold"));
      filter(nbt.getInt("filter"));
    }

    @Override
    public void reset(@Nullable IWorldReader world)
    { super.reset(world); update_timer_=0; area_=null; sensor_range_=5; filter_=0; }

    @Override
    public boolean activation_config(BlockState state, @Nullable PlayerEntity player, double x, double y, boolean show_only)
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
        switch(field) {
          case 1: {
            sensor_range(sensor_range()+direction);
            area_ = null;
            break;
          }
          case 2: {
            sensor_entity_threshold(sensor_entity_threshold() + direction);
            break;
          }
          case 3: {
            filter(filter() + direction);
            break;
          }
          case 4: {
            on_power(on_power() + direction);
            if(on_power() < 1) on_power(1);
            break;
          }
        }
        markDirty();
      }
      {
        Overlay.show(player,
          (new StringTextComponent(""))
            .append(Auxiliaries.localizable("switchconfig.detector.sensor_range", TextFormatting.BLUE, new Object[]{sensor_range()}))
            .appendString(" | ")
            .append(Auxiliaries.localizable("switchconfig.detector.entity_threshold", TextFormatting.YELLOW, new Object[]{sensor_entity_threshold()}))
            .appendString(" | ")
            .append(Auxiliaries.localizable("switchconfig.detector.entity_filter", TextFormatting.DARK_GREEN, new Object[]{new TranslationTextComponent("rsgauges.switchconfig.detector.entity_filter."+filter_class_names[filter()])}))
            .appendString(" | ")
            .append(Auxiliaries.localizable("switchconfig.detector.output_power", TextFormatting.RED, new Object[]{on_power()}))
        );
      }
      return true;
    }

    @Override
    public void tick()
    {
      if((world.isRemote()) || (--update_timer_ > 0)) return;
      update_timer_ = update_interval_;
      BlockState state = world.getBlockState(getPos());
      if((state==null) || (!(state.getBlock() instanceof AutoSwitchBlock))) return;
      AutoSwitchBlock block = (AutoSwitchBlock)(state.getBlock());
      // initialisations
      if(update_interval_==0) {
        if((block.config & SWITCH_CONFIG_SENSOR_LINEAR) != 0) {
          update_interval_ = ModConfig.autoswitch_linear_update_interval;
        } else {
          update_interval_ = ModConfig.autoswitch_volumetric_update_interval;
        }
      }
      if(area_ == null) {
        int size = sensor_range();
        AxisAlignedBB range_bb;
        if((block.config & SWITCH_CONFIG_SENSOR_VOLUME) != 0) {
          range_bb = new AxisAlignedBB(0,-2,-size, size,2,size);
        } else if((block.config & SWITCH_CONFIG_SENSOR_LINEAR) != 0) {
          range_bb = new AxisAlignedBB(-0.5,-0.5,-0.5, size,0.5,0.5);
        } else {
          range_bb = new AxisAlignedBB(0,0,0, 1,0,0);
        }
        Direction facing = state.get(FACING);
        AxisAlignedBB bb = Auxiliaries.transform_forward(range_bb, facing).offset(getPos()).expand(1,1,1);
        area_ = new AxisAlignedBB(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
      }

      // measurement
      boolean active = false;
      @SuppressWarnings("unchecked")
      List<Entity> hits = world.getEntitiesWithinAABB((Class<Entity>)filter_class(), area_);
      if(hits.size() >= sensor_entity_count_threshold_) {
        int num_seen = 0;
        final Vector3d switch_position = new Vector3d((double)getPos().getX()+.5, (double)getPos().getY()+.5, (double)getPos().getZ()+.5);
        for(Entity e:hits) {
          if(
            (
              world.rayTraceBlocks(
                new RayTraceContext(
                  new Vector3d(e.getPosition().getX()-0.2, e.getPosition().getY()+e.getEyeHeight(), e.getPosition().getZ()-0.2),
                  switch_position,
                  RayTraceContext.BlockMode.OUTLINE,
                  RayTraceContext.FluidMode.NONE,
                  e
                )
              ).getType() != RayTraceResult.Type.BLOCK
            )
            ||
            (
              world.rayTraceBlocks(
                new RayTraceContext(
                  new Vector3d(e.getPosition().getX()+0.2, e.getPosition().getY()+e.getEyeHeight(), e.getPosition().getZ()+0.2),
                  switch_position,
                  RayTraceContext.BlockMode.OUTLINE,
                  RayTraceContext.FluidMode.NONE,
                  e
                )
              ).getType() != RayTraceResult.Type.BLOCK
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
