/*
 * @file TrapdoorSwitchBlock.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Basic class for blocks representing switches which swing
 * open on activation, allowing entities to fall throuh.
 * Different activation triggers possible: fall uppon, walk,
 * etc.
 */
package wile.rsgauges.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.pathfinding.PathType;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import wile.rsgauges.detail.ModResources;
import wile.rsgauges.detail.SwitchLink;
import javax.annotation.Nullable;

public class TrapdoorSwitchBlock extends ContactSwitchBlock
{
  public TrapdoorSwitchBlock(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound); }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  @SuppressWarnings("deprecation")
  public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type)
  {
    /// -> was public boolean isPassable(IWorldReader world, BlockPos pos)
    switch(type) {
      case LAND:  return (!state.get(POWERED));
      case AIR:   return (!state.get(POWERED));
      default:    return true;
    }
  }

  @Override
  public void onFallenUpon(World world, BlockPos pos, Entity entity, float distance)
  {
    if(((config & SWITCH_CONFIG_SHOCK_SENSITIVE)!=0)) {
      if(world.isRemote()) return;
      onEntityCollided(world, pos, world.getBlockState(pos));
      final BlockPos[] neighbors = { pos.add(1,0,0), pos.add(-1,0,0), pos.add(0,0,1), pos.add(0,0,-1), pos.add(1,0,1), pos.add(-1,0,-1), pos.add(-1,0,1), pos.add(1,0,-1), };
      for(BlockPos p: neighbors) {
        final BlockState st = world.getBlockState(p);
        if((st!=null) && (st.getBlock()==this)) onEntityCollided(world, p, st);
      }
    }
    super.onFallenUpon(world, pos, entity, distance);
  }

  @Override
  public void onEntityWalk(World world, BlockPos pos, Entity entity)
  {
    if(((config & SWITCH_CONFIG_HIGH_SENSITIVE)==0) || (world.isRemote()) || (entity.isSneaking())) return;
    onEntityCollided(world, pos, world.getBlockState(pos));
    final BlockPos[] neighbors = { pos.add(1,0,0), pos.add(-1,0,0), pos.add(0,0,1), pos.add(0,0,-1), pos.add(1,0,1), pos.add(-1,0,-1), pos.add(-1,0,1), pos.add(1,0,-1), };
    for(BlockPos p: neighbors) {
      final BlockState st = world.getBlockState(p);
      if((st!=null) && (st.getBlock()==this)) onEntityCollided(world, p, st);
    }
  }

  @Override
  public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity)
  {
    if(((config & SWITCH_CONFIG_SHOCK_SENSITIVE)!=0) && (entity.getHeight() < 0.9)) return; // close on items
    onEntityCollided(world, pos, state);
  }

  // -------------------------------------------------------------------------------------------------------------------

  @Override
  protected AxisAlignedBB detectionVolume(BlockPos pos)
  {
    if((config & (SWITCH_CONFIG_SHOCK_SENSITIVE|SWITCH_CONFIG_HIGH_SENSITIVE))==0) {
      return new AxisAlignedBB(Vector3d.copy(pos).add(0,0,0), Vector3d.copy(pos).add(1,1,1));
    } else {
      return new AxisAlignedBB(Vector3d.copy(pos).add(-0.2,0,-0.2), Vector3d.copy(pos).add(1.2,2,1.2));
    }
  }

  @Override
  public SwitchLink.RequestResult switchLinkTrigger(SwitchLink link)
  {
    final World world = link.world;
    final BlockPos pos = link.target_position;
    if((world==null) || ((config & (SWITCH_CONFIG_LINK_TARGET_SUPPORT))==0) || (world.isRemote())) return SwitchLink.RequestResult.REJECTED;
    BlockState state = world.getBlockState(pos);
    if((state == null) || (!(state.getBlock() instanceof TrapdoorSwitchBlock))) return SwitchLink.RequestResult.REJECTED;
    if(state.get(POWERED)) return SwitchLink.RequestResult.OK; // already active
    ContactSwitchTileEntity te = getTe(world, pos);
    if((te==null) || (!te.verifySwitchLinkTarget(link))) return SwitchLink.RequestResult.REJECTED;
    world.setBlockState(pos, state.with(POWERED, true), 1|2|8|16);
    power_on_sound.play(world, pos);
    notifyNeighbours(world, pos, state, te, false);
    te.on_timer_reset( (te.configured_on_time()==0) ? (default_pulse_on_time) : ( (te.configured_on_time() < 2) ? 2 : te.configured_on_time()) );
    te.reschedule_block_tick();
    return SwitchLink.RequestResult.OK;
  }

}
