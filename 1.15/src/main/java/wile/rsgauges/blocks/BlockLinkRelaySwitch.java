/*
 * @file BlockLinkRelaySwitch.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Switch-Link relays.
 */
package wile.rsgauges.blocks;

import wile.rsgauges.detail.ModResources;
import net.minecraft.world.IWorldReader;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.util.math.*;
import javax.annotation.Nullable;

public class BlockLinkRelaySwitch extends BlockSwitch
{
  public BlockLinkRelaySwitch(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound); }

  public BlockLinkRelaySwitch(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, null, null); }

  @Override
  protected void neighborUpdated(BlockState state, World world, BlockPos pos, BlockPos facingPos)
  {
    if(!isAffectedByNeigbour(state, world, pos, facingPos)) return;
    final TileEntitySwitch te = getTe(world, pos);
    if(te==null) return;
    boolean powered;
    if(isCube()) {
      powered = world.isBlockPowered(pos);
    } else {
      Direction facing = state.get(FACING).getOpposite();
      final BlockPos neighbour_pos = pos.offset(facing);
      final BlockState neighbour_state = world.getBlockState(neighbour_pos);
      if(!state.canProvidePower()) {
        powered = world.isBlockPowered(neighbour_pos);
      } else {
        powered = (neighbour_state.getWeakPower(world, neighbour_pos, facing) > 0)
               || (neighbour_state.getStrongPower(world, neighbour_pos, facing) > 0);
      }
    }
    if(((config & SWITCH_CONFIG_INVERTABLE)!=0) && (te.inverted())) powered = !powered; // inverted==redstone input state inverted
    if((te.on_power()==0) == (powered)) return; // power state not changed
    te.on_power(powered ? 0 : 15);
    if(((config & SWITCH_CONFIG_BISTABLE)!=0) && (state.get(POWERED)==powered)) return; // no state change, don't flip state
    if(((config & SWITCH_CONFIG_PULSE)!=0) && ((!powered) || (state.get(POWERED)))) return; // no extension etc.
    onSwitchActivated((World)world, pos, state, null, null);
  }

  @Override
  public boolean getWeakChanges(BlockState state, IWorldReader world, BlockPos pos)
  { return true; }

}
