/*
 * @file BlockGauge.java
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

import wile.rsgauges.detail.ModColors;
import wile.rsgauges.detail.ModResources;
import net.minecraft.block.BlockState;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.world.IWorldReader;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import javax.annotation.Nullable;


public class BlockGauge extends BlockAbstractGauge implements ModColors.ColorTintSupport
{
  public static final IntegerProperty POWER = IntegerProperty.create("power", 0, 15);

  // -------------------------------------------------------------------------------------------------------------------

  public BlockGauge(long config, Block.Properties props, final AxisAlignedBB aabb, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  {
    super(config, props, aabb, powerOnSound, powerOffSound);
    setDefaultState(super.getDefaultState().with(POWER, 0));
  }

  public BlockGauge(long config, Block.Properties props, final AxisAlignedBB aabb)
  { this(config, props, aabb, null, null); }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
  { super.fillStateContainer(builder); builder.add(POWER); }

  @Override
  @Nullable
  public BlockState getStateForPlacement(BlockItemUseContext context)
  {
    final BlockState state = super.getStateForPlacement(context);
    if(state==null)
    {
      return null;
    }
    return state.with(BlockGauge.POWER, 0);
  }

  // -------------------------------------------------------------------------------------------------------------------
  // Tile entity
  // -------------------------------------------------------------------------------------------------------------------

  public TileEntityGauge getTe(IWorldReader world, BlockPos pos)
  {
    final TileEntity te = world.getTileEntity(pos);
    return (te instanceof TileEntityGauge) ? ((TileEntityGauge)te) : (null);
  }

}
