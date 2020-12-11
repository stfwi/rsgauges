/*
 * @file LinkSenderSwitchBlock.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Switch-Link relays.
 */
package wile.rsgauges.blocks;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IWorldReader;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.util.math.*;
import wile.rsgauges.detail.ModResources;

import javax.annotation.Nullable;

public class LinkSenderSwitchBlock extends SwitchBlock
{
  public LinkSenderSwitchBlock(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound); }

  public LinkSenderSwitchBlock(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, null, null); }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
  {
    final SwitchTileEntity te = getTe(world, pos);
    if(te != null) te.reset(world);
    world.setBlockState(pos, world.getBlockState(pos).with(POWERED, false), 1|2|8|16);
    neighborChanged(state, world, pos, state.getBlock(), pos.offset(state.get(FACING).getOpposite()), false);
  }

  @Override
  public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
  {
    if(!isAffectedByNeigbour(state, world, pos, fromPos)) return;
    final SwitchTileEntity te = getTe(world, pos);
    if(te==null) return;
    int power;
    if(isCube()) {
      power = world.getRedstonePowerFromNeighbors(pos);
    } else {
      Direction facing = state.get(FACING).getOpposite();
      final BlockPos neighbour_pos = pos.offset(facing);
      final BlockState neighbour_state = world.getBlockState(neighbour_pos);
      if(!state.canProvidePower()) {
        power = world.getRedstonePowerFromNeighbors(neighbour_pos);
      } else {
        power = Math.max(neighbour_state.getWeakPower(world, neighbour_pos, facing), neighbour_state.getStrongPower(world, neighbour_pos, facing));
      }
    }
    if(((config & SWITCH_CONFIG_INVERTABLE)!=0) && (te.inverted())) power = 15-power; // inverted==redstone input state inverted
    if(te.on_power() == power) return; // power state not changed
    te.on_power(power);
    final boolean powered = (power > 0);
    final boolean was_powered = state.get(POWERED);
    if(powered != was_powered) {
      if(((config & SWITCH_CONFIG_PULSE)==0) || (powered && !was_powered)) {
        world.setBlockState(pos, state.with(POWERED, powered), 1|2|8|16);
        if((config & SWITCH_CONFIG_PULSE)!=0) {
          te.on_timer_reset();
          te.on_timer_extend();
          te.reschedule_block_tick();
        }
        if(powered && (power_on_sound!=null)) {
          power_on_sound.play(world, pos);
        } else if((!powered) && (power_off_sound!=null)) {
          power_off_sound.play(world, pos);
        }
      }
    }
    if(!te.activateSwitchLinks(power, powered!=was_powered)) {
      ModResources.BlockSoundEvents.SWITCHLINK_LINK_PEAL_USE_FAILED.play(world, pos);
    }
  }

  @Override
  public boolean getWeakChanges(BlockState state, IWorldReader world, BlockPos pos)
  { return true; }

}
