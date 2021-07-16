/*
 * @file IndicatorBlock.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Block class for redstone power "on/off" indication. Identical
 * to BlockGauge, except that the block state POWER is stripped
 * to two state values instead of 16 "analog" values.
 */
package wile.rsgauges.blocks;

import net.minecraft.block.AbstractBlock;
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

  public IndicatorBlock(long config, AbstractBlock.Properties properties, AxisAlignedBB unrotatedBB, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  {
    super(config, properties, unrotatedBB, powerOnSound, powerOffSound);
    registerDefaultState(super.defaultBlockState().setValue(POWERED, false).setValue(FACING, Direction.DOWN));
  }

  public IndicatorBlock(long config, AbstractBlock.Properties properties, AxisAlignedBB unrotatedBB)
  { this(config, properties, unrotatedBB, null, null); }

  @Override
  @Nullable
  public BlockState getStateForPlacement(BlockItemUseContext context)
  {
    final BlockState state = super.getStateForPlacement(context);
    return (state==null) ? (null) : (state.setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos())));
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder)
  { super.createBlockStateDefinition(builder); builder.add(POWERED); }
}
