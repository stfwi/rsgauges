/*
 * @file LinkSenderSwitchBlock.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Switch-Link relays.
 */
package wile.rsgauges.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import wile.rsgauges.detail.ModResources;

import javax.annotation.Nullable;


public class LinkSenderSwitchBlock extends SwitchBlock
{
  private final boolean is_analog;

  public LinkSenderSwitchBlock(long config, BlockBehaviour.Properties properties, AABB unrotatedBBUnpowered, @Nullable AABB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound, boolean analog_device)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound); is_analog = analog_device; }

  @Override
  public boolean switchLinkHasAnalogSupport(Level world, BlockPos pos)
  { return is_analog; }

  @Override
  protected int getPower(BlockState state, BlockGetter world, BlockPos pos, Direction side, boolean strong)
  { return 0; }

  @Override
  public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
  {
    final SwitchTileEntity te = getTe(world, pos);
    if(te != null) te.reset(world);
    world.setBlock(pos, world.getBlockState(pos).setValue(POWERED, false), 1|2|8|16);
    neighborChanged(state, world, pos, state.getBlock(), pos.relative(state.getValue(FACING).getOpposite()), false);
  }

  @Override
  public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
  {
    if(!isAffectedByNeigbour(state, world, pos, fromPos)) return;
    final SwitchTileEntity te = getTe(world, pos);
    if(te==null) return;
    int power;
    if(isCube()) {
      power = world.getBestNeighborSignal(pos);
    } else {
      Direction facing = state.getValue(FACING).getOpposite();
      final BlockPos neighbour_pos = pos.relative(facing);
      final BlockState neighbour_state = world.getBlockState(neighbour_pos);
      if(!state.isSignalSource()) {
        power = world.getBestNeighborSignal(neighbour_pos);
      } else {
        power = Math.max(neighbour_state.getSignal(world, neighbour_pos, facing), neighbour_state.getDirectSignal(world, neighbour_pos, facing));
      }
    }
    if(((config & SWITCH_CONFIG_INVERTABLE)!=0) && (te.inverted())) power = 15-power; // inverted==redstone input state inverted
    if(te.setpower() == power) return; // power state not changed
    te.setpower(power);
    final boolean powered = (power > 0);
    final boolean was_powered = state.getValue(POWERED);
    if(powered != was_powered) {
      if(((config & SWITCH_CONFIG_PULSE)==0) || (powered && !was_powered)) {
        world.setBlock(pos, state.setValue(POWERED, powered), 1|2|8|16);
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
    if(!te.activateSwitchLinks(power, powered?15:0, powered!=was_powered)) {
      ModResources.BlockSoundEvents.SWITCHLINK_LINK_PEAL_USE_FAILED.play(world, pos);
    }
  }

  @Override
  public boolean getWeakChanges(BlockState state, LevelReader world, BlockPos pos)
  { return true; }

}
