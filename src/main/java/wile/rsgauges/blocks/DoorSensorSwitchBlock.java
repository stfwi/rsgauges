/*
 * @file DoorSensorSwitchBlock.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Player sensitive switch that is mounted on walls above doors.
 */
package wile.rsgauges.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import wile.rsgauges.ModContent;
import wile.rsgauges.detail.ModResources;

import javax.annotation.Nullable;
import java.util.List;


public class DoorSensorSwitchBlock extends SwitchBlock
{
  public DoorSensorSwitchBlock(long config, BlockBehaviour.Properties properties, AABB unrotatedBBUnpowered, @Nullable AABB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound); }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  public DoorSensorSwitchTileEntity getTe(LevelReader world, BlockPos pos)
  {
    BlockEntity te = world.getBlockEntity(pos);
    if((!(te instanceof DoorSensorSwitchTileEntity))) return null;
    return (DoorSensorSwitchTileEntity)te;
  }

  @Override
  @Nullable
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
  { return new DoorSensorSwitchTileEntity(pos, state); }

  // -------------------------------------------------------------------------------------------------------------------
  // Tile entity
  // -------------------------------------------------------------------------------------------------------------------

  public static class DoorSensorSwitchTileEntity extends SwitchTileEntity
  {
    private int update_timer_ = 0;

    public DoorSensorSwitchTileEntity(BlockEntityType<?> te_type, BlockPos pos, BlockState state)
    { super(te_type, pos, state); }

    public DoorSensorSwitchTileEntity(BlockPos pos, BlockState state)
    { super(ModContent.TET_DOORSENSOR_SWITCH, pos, state); }

    @Override
    public void tick()
    {
      if(level.isClientSide() || (--update_timer_ > 0)) return;
      update_timer_ = 4;
      final BlockState state = level.getBlockState(getBlockPos());
      if((state==null) || (!(state.getBlock() instanceof final DoorSensorSwitchBlock block))) return;
      final Vec3 door_vec = Vec3.atLowerCornerOf(state.getValue(FACING).getNormal());
      final AABB volume = (new AABB(getBlockPos().below())).inflate(0.4).move(door_vec.scale(-0.5));
      boolean active = false;
      List<Player> hits = level.getEntitiesOfClass(Player.class, volume);
      if(hits.size() > 0) {
        final Vec3 door_pos = Vec3.atLowerCornerOf(getBlockPos().relative(state.getValue(FACING)).below()).add(0.5,0.5,0.5);
        for(Entity e:hits) {
          if(Math.abs(e.getLookAngle().y()) > 0.7) continue; // early skip, looks too much up/down
          final Vec3 look_vec = new Vec3(e.getLookAngle().x(), 0, e.getLookAngle().z());
          final double cos_a1 = look_vec.dot(door_vec);
          if(cos_a1 < 0.5) continue; // look vector already too far off
          final Vec3 rel_position = new Vec3(e.position().x()-door_pos.x(), 0, e.position().z()-door_pos.z()).normalize();
          final double cos_a2 = look_vec.dot(rel_position);
          if(cos_a2 > -0.8) continue; // position relative look vector not close enough
          active = true;
          break;
        }
      }
      if(active) {
        if(!state.getValue(POWERED)) {
          block.onSwitchActivated(level, worldPosition, state, null, null);
          on_timer_reset(12);
        } else {
          on_timer_reset(12);
          reschedule_block_tick();
        }
      }
    }
  }

}
