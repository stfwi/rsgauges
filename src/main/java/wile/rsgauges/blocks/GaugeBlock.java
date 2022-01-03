/*
 * @file GaugeBlock.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Basic class for blocks representing a redstone signal
 * display, measuring the redstone signal of the block
 * it is attached to (only the back not the sides or front).
 * The block has a "tickable" tile entity to ensure that
 * the gauge display is updated even if a block update event
 * was lost. Depending on the model/type additional constants
 * like power-to-light scaling are implemented, e.g. for LED
 * indicators.
 *
 */
package wile.rsgauges.blocks;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import wile.rsgauges.detail.ModResources;

import javax.annotation.Nullable;


public class GaugeBlock extends AbstractGaugeBlock
{
  public static final IntegerProperty POWER = IntegerProperty.create("power", 0, 15);

  public GaugeBlock(long config, BlockBehaviour.Properties props, final AABB aabb, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config, props, aabb, powerOnSound, powerOffSound); registerDefaultState(super.defaultBlockState().setValue(POWER, 0)); }

  public GaugeBlock(long config, BlockBehaviour.Properties props, final AABB aabb)
  { this(config, props, aabb, null, null); }

  @Override
  @Nullable
  public BlockState getStateForPlacement(BlockPlaceContext context)
  {
    final BlockState state = super.getStateForPlacement(context);
    return (state==null) ? null : state.setValue(POWER, 0);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
  { super.createBlockStateDefinition(builder); builder.add(POWER); }
}
