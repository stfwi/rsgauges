/*
 * @file BlockDoorSensorSwitch.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Player sensitive switch that is mounted on walls above doors.
 */
package wile.rsgauges.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ITickable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import wile.rsgauges.detail.ModResources;
import javax.annotation.Nullable;
import java.util.List;


public class BlockDoorSensorSwitch extends BlockSwitch
{

  public BlockDoorSensorSwitch(String registryName, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered, long config, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound, @Nullable Material material)
  { super(registryName, unrotatedBBUnpowered, unrotatedBBPowered, config, powerOnSound, powerOffSound, material); }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  public TileEntityDoorSensorSwitch getTe(IBlockAccess world, BlockPos pos)
  {
    TileEntity te = world.getTileEntity(pos);
    if((!(te instanceof TileEntityDoorSensorSwitch))) return null;
    return (TileEntityDoorSensorSwitch)te;
  }

  @Override
  public TileEntity createTileEntity(World world, IBlockState state)
  { return new TileEntityDoorSensorSwitch(); }

  // -------------------------------------------------------------------------------------------------------------------
  // Tile entity
  // -------------------------------------------------------------------------------------------------------------------

  public static class TileEntityDoorSensorSwitch extends TileEntitySwitch implements ITickable
  {
    private int update_timer_ = 0;

    @Override
    public void update()
    {
      if(world.isRemote || (--update_timer_ > 0)) return;
      update_timer_ = 4;
      final IBlockState state = world.getBlockState(getPos());
      if((state==null) || (!(state.getBlock() instanceof BlockDoorSensorSwitch))) return;
      final BlockDoorSensorSwitch block = (BlockDoorSensorSwitch) state.getBlock();
      final Vec3d door_vec = new Vec3d(state.getValue(FACING).getDirectionVec());
      final AxisAlignedBB volume = (new AxisAlignedBB(getPos().down())).grow(0.4).offset(door_vec.scale(-0.5));
      boolean active = false;
      List<Entity> hits = world.getEntitiesWithinAABB(EntityPlayer.class, volume);
      if(hits.size() > 0) {
        final Vec3d door_pos = new Vec3d(getPos().offset(state.getValue(FACING)).down()).add(0.5,0.5,0.5);
        for(Entity e:hits) {
          if(Math.abs(e.getLookVec().y) > 0.7) continue; // early skip, looks too much up/down
          final Vec3d look_vec = new Vec3d(e.getLookVec().x, 0, e.getLookVec().z);
          final double cos_a1 = look_vec.dotProduct(door_vec);
          if(cos_a1 < 0.5) continue; // look vector already too far off
          final Vec3d rel_position = new Vec3d(e.posX-door_pos.x, 0, e.posZ-door_pos.z).normalize();
          final double cos_a2 = look_vec.dotProduct(rel_position);
          if(cos_a2 > -0.8) continue; // position relative look vector not close enough
          active = true;
          break;
        }
      }
      if(active) {
        if(!state.getValue(POWERED)) {
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
