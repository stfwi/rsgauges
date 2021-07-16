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

import net.minecraft.block.AbstractBlock;
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
  public TrapdoorSwitchBlock(long config, AbstractBlock.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound); }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  @SuppressWarnings("deprecation")
  public boolean isPathfindable(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type)
  {
    /// -> was public boolean isPassable(IWorldReader world, BlockPos pos)
    switch(type) {
      case LAND:
      case AIR:  return (!state.getValue(POWERED));
      default:   return true;
    }
  }

  @Override
  public void fallOn(World world, BlockPos pos, Entity entity, float distance)
  {
    if(((config & SWITCH_CONFIG_SHOCK_SENSITIVE)!=0)) {
      if(world.isClientSide()) return;
      onEntityCollided(world, pos, world.getBlockState(pos));
      final BlockPos[] neighbors = { pos.offset(1,0,0), pos.offset(-1,0,0), pos.offset(0,0,1), pos.offset(0,0,-1), pos.offset(1,0,1), pos.offset(-1,0,-1), pos.offset(-1,0,1), pos.offset(1,0,-1), };
      for(BlockPos p: neighbors) {
        final BlockState st = world.getBlockState(p);
        if((st!=null) && (st.getBlock()==this)) onEntityCollided(world, p, st);
      }
    }
    super.fallOn(world, pos, entity, distance);
  }

  @Override
  public void stepOn(World world, BlockPos pos, Entity entity)
  {
    if(((config & SWITCH_CONFIG_HIGH_SENSITIVE)==0) || (world.isClientSide()) || (entity.isShiftKeyDown())) return;
    onEntityCollided(world, pos, world.getBlockState(pos));
    final BlockPos[] neighbors = { pos.offset(1,0,0), pos.offset(-1,0,0), pos.offset(0,0,1), pos.offset(0,0,-1), pos.offset(1,0,1), pos.offset(-1,0,-1), pos.offset(-1,0,1), pos.offset(1,0,-1), };
    for(BlockPos p: neighbors) {
      final BlockState st = world.getBlockState(p);
      if((st!=null) && (st.getBlock()==this)) onEntityCollided(world, p, st);
    }
  }

  @Override
  public void entityInside(BlockState state, World world, BlockPos pos, Entity entity)
  {
    if(((config & SWITCH_CONFIG_SHOCK_SENSITIVE)!=0) && (entity.getBbHeight() < 0.9)) return; // close on items
    onEntityCollided(world, pos, state);
  }

  // -------------------------------------------------------------------------------------------------------------------

  @Override
  protected AxisAlignedBB detectionVolume(BlockPos pos)
  {
    if((config & (SWITCH_CONFIG_SHOCK_SENSITIVE|SWITCH_CONFIG_HIGH_SENSITIVE))==0) {
      return new AxisAlignedBB(Vector3d.atLowerCornerOf(pos).add(0,0,0), Vector3d.atLowerCornerOf(pos).add(1,1,1));
    } else {
      return new AxisAlignedBB(Vector3d.atLowerCornerOf(pos).add(-0.2,0,-0.2), Vector3d.atLowerCornerOf(pos).add(1.2,2,1.2));
    }
  }

  @Override
  public SwitchLink.RequestResult switchLinkTrigger(SwitchLink link)
  {
    final World world = link.world;
    final BlockPos pos = link.target_position;
    if((world==null) || ((config & (SWITCH_CONFIG_LINK_TARGET_SUPPORT))==0) || (world.isClientSide())) return SwitchLink.RequestResult.REJECTED;
    BlockState state = world.getBlockState(pos);
    if((state == null) || (!(state.getBlock() instanceof TrapdoorSwitchBlock))) return SwitchLink.RequestResult.REJECTED;
    if(state.getValue(POWERED)) return SwitchLink.RequestResult.OK; // already active
    ContactSwitchTileEntity te = getTe(world, pos);
    if((te==null) || (!te.verifySwitchLinkTarget(link))) return SwitchLink.RequestResult.REJECTED;
    world.setBlock(pos, state.setValue(POWERED, true), 1|2|8|16);
    power_on_sound.play(world, pos);
    notifyNeighbours(world, pos, state, te, false);
    te.on_timer_reset( (te.configured_on_time()==0) ? (default_pulse_on_time) : (Math.max(te.configured_on_time(), 2)) );
    te.reschedule_block_tick();
    return SwitchLink.RequestResult.OK;
  }

}
