/*
 * @file BlockIndicator.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Block class for redstone power "on/off" indication. Identical
 * to BlockGauge, except that the block state POWER is stripped
 * to two state values instead of 16 "analog" values.
 */
package wile.rsgauges.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import wile.rsgauges.detail.ModResources;
import net.minecraft.util.math.AxisAlignedBB;
import javax.annotation.Nullable;


public class BlockIndicator extends BlockAbstractGauge
{
  public static final BooleanProperty POWERED = BooleanProperty.create("power");

  // -------------------------------------------------------------------------------------------------------------------

  public BlockIndicator(long config, Block.Properties properties, AxisAlignedBB unrotatedBB, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  {
    super(config, properties, unrotatedBB, powerOnSound, powerOffSound);
    setDefaultState(super.getDefaultState().with(POWERED, false));
  }

  public BlockIndicator(long config, Block.Properties properties, AxisAlignedBB unrotatedBB)
  { super(config, properties, unrotatedBB, null, null); }

  @Override
  @Nullable
  public BlockState getStateForPlacement(BlockItemUseContext context)
  {
    final BlockState state = super.getStateForPlacement(context);
    return (state==null) ? (null) : (state.with(POWERED, context.getWorld().isBlockPowered(context.getPos())));
  }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
  { super.fillStateContainer(builder); builder.add(POWERED); }

  @Override
  public int getLightValue(BlockState state)
  { return 0; }

}
