/*
 * @file BlockDoorSensorSwitch.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Player sensitive switch that is mounted on walls above doors.
 */
package wile.rsgauges.blocks;

import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.block.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import wile.rsgauges.ModContent;
import wile.rsgauges.detail.ModResources;
import javax.annotation.Nullable;
import java.util.List;


public class DoorSensorSwitchBlock extends SwitchBlock
{
  public DoorSensorSwitchBlock(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound); }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  public DoorSensorSwitchTileEntity getTe(IWorldReader world, BlockPos pos)
  {
    TileEntity te = world.getTileEntity(pos);
    if((!(te instanceof DoorSensorSwitchTileEntity))) return null;
    return (DoorSensorSwitchTileEntity)te;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world)
  { return new DoorSensorSwitchTileEntity(ModContent.TET_DOORSENSOR_SWITCH); }

  // -------------------------------------------------------------------------------------------------------------------
  // Tile entity
  // -------------------------------------------------------------------------------------------------------------------

  public static class DoorSensorSwitchTileEntity extends SwitchTileEntity implements ITickableTileEntity
  {
    private int update_timer_ = 0;

    public DoorSensorSwitchTileEntity(TileEntityType<?> te_type)
    { super(te_type); }

    public DoorSensorSwitchTileEntity()
    { super(ModContent.TET_DOORSENSOR_SWITCH); }

    @Override
    public void tick()
    {
      if(world.isRemote || (--update_timer_ > 0)) return;
      update_timer_ = 4;
      final BlockState state = world.getBlockState(getPos());
      if((state==null) || (!(state.getBlock() instanceof DoorSensorSwitchBlock))) return;
      final DoorSensorSwitchBlock block = (DoorSensorSwitchBlock) state.getBlock();
      final Vector3d door_vec = Vector3d.copy(state.get(FACING).getDirectionVec());
      final AxisAlignedBB volume = (new AxisAlignedBB(getPos().down())).grow(0.4).offset(door_vec.scale(-0.5));
      boolean active = false;
      @SuppressWarnings("unchecked")
      List<Entity> hits = world.getEntitiesWithinAABB(PlayerEntity.class, volume);
      if(hits.size() > 0) {
        final Vector3d door_pos = Vector3d.copy(getPos().offset(state.get(FACING)).down()).add(0.5,0.5,0.5);
        for(Entity e:hits) {
          if(Math.abs(e.getLookVec().getY()) > 0.7) continue; // early skip, looks too much up/down
          final Vector3d look_vec = new Vector3d(e.getLookVec().getX(), 0, e.getLookVec().getZ());
          final double cos_a1 = look_vec.dotProduct(door_vec);
          if(cos_a1 < 0.5) continue; // look vector already too far off
          final Vector3d rel_position = new Vector3d(e.getPositionVec().getX()-door_pos.getX(), 0, e.getPositionVec().getZ()-door_pos.getZ()).normalize();
          final double cos_a2 = look_vec.dotProduct(rel_position);
          if(cos_a2 > -0.8) continue; // position relative look vector not close enough
          active = true;
          break;
        }
      }
      if(active) {
        if(!state.get(POWERED)) {
          block.onSwitchActivated(world, pos, state, null, null);
          on_timer_reset(12);
        } else {
          on_timer_reset(12);
          reschedule_block_tick();
        }
      }
    }
  }

}
