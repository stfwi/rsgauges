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

import net.minecraft.block.BlockState;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.math.AxisAlignedBB;
import wile.rsgauges.detail.ModResources;
import javax.annotation.Nullable;


public class GaugeBlock extends AbstractGaugeBlock
{
  public static final IntegerProperty POWER = IntegerProperty.create("power", 0, 15);

  public GaugeBlock(long config, Block.Properties props, final AxisAlignedBB aabb, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config, props, aabb, powerOnSound, powerOffSound); setDefaultState(super.getDefaultState().with(POWER, 0)); }

  public GaugeBlock(long config, Block.Properties props, final AxisAlignedBB aabb)
  { this(config, props, aabb, null, null); }

  @Override
  @Nullable
  public BlockState getStateForPlacement(BlockItemUseContext context)
  {
    final BlockState state = super.getStateForPlacement(context);
    return (state==null) ? null : state.with(POWER, 0);
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
  { super.fillStateContainer(builder); builder.add(POWER); }
}
