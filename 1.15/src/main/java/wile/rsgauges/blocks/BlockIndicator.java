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
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILightReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wile.rsgauges.detail.ModResources;
import net.minecraft.util.math.AxisAlignedBB;
import javax.annotation.Nullable;


public class BlockIndicator extends BlockGauge
{
  public static final BooleanProperty POWERED = BooleanProperty.create("power");

  // -------------------------------------------------------------------------------------------------------------------

  public BlockIndicator(long config, Block.Properties properties, AxisAlignedBB unrotatedBB, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config, properties, unrotatedBB, powerOnSound, powerOffSound); }

  public BlockIndicator(long config, Block.Properties properties, AxisAlignedBB unrotatedBB)
  { super(config, properties, unrotatedBB, null, null); }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
  { builder.add(FACING, POWERED, WATERLOGGED); }


//@todo: IMPLEMENT
//  @Override
//  @OnlyIn(Dist.CLIENT)
//  @SuppressWarnings("deprecation")
//  public int getPackedLightmapCoords(BlockState state, ILightReader world, BlockPos pos)
//  { return world.getCombinedLight(pos, state.get(POWERED) ? 3 : 0); }

  @Override
  public int getLightValue(BlockState state)
  { return 0; }

  // -------------------------------------------------------------------------------------------------------------------

}
