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

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.AABB;
import wile.rsgauges.detail.ModResources;

import javax.annotation.Nullable;


public class IndicatorBlock extends AbstractGaugeBlock
{
  public static final BooleanProperty POWERED = BooleanProperty.create("power");

  public IndicatorBlock(long config, BlockBehaviour.Properties properties, AABB unrotatedBB, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  {
    super(config, properties, unrotatedBB, powerOnSound, powerOffSound);
    registerDefaultState(super.defaultBlockState().setValue(POWERED, false).setValue(FACING, Direction.DOWN));
  }

  public IndicatorBlock(long config, BlockBehaviour.Properties properties, AABB unrotatedBB)
  { this(config, properties, unrotatedBB, null, null); }

  @Override
  @Nullable
  public BlockState getStateForPlacement(BlockPlaceContext context)
  {
    final BlockState state = super.getStateForPlacement(context);
    return (state==null) ? (null) : (state.setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos())));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
  { super.createBlockStateDefinition(builder); builder.add(POWERED); }
}
