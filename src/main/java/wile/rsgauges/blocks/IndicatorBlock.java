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
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import wile.rsgauges.detail.ModResources;
import javax.annotation.Nullable;


public class IndicatorBlock extends AbstractGaugeBlock
{
  public static final BooleanProperty POWERED = BooleanProperty.create("power");

  public IndicatorBlock(long config, Block.Properties properties, AxisAlignedBB unrotatedBB, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  {
    super(config, properties, unrotatedBB, powerOnSound, powerOffSound);
    setDefaultState(getStateContainer().getBaseState().with(POWERED, false).with(WATERLOGGED, false).with(FACING, Direction.DOWN));
  }

  public IndicatorBlock(long config, Block.Properties properties, AxisAlignedBB unrotatedBB)
  { this(config, properties, unrotatedBB, null, null); }

  @Override
  @Nullable
  public BlockState getStateForPlacement(BlockItemUseContext context)
  {
    final BlockState state = super.getStateForPlacement(context);
    return (state==null) ? (null) : (state.with(POWERED, context.getWorld().isBlockPowered(context.getPos())));
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
  { super.fillStateContainer(builder); builder.add(POWERED); }
}
