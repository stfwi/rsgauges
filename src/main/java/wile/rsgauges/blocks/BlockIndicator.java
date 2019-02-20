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

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import wile.rsgauges.detail.ModAuxiliaries;
import wile.rsgauges.detail.ModResources;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import javax.annotation.Nullable;


public class BlockIndicator extends BlockGauge
{
  public static final PropertyBool POWER = PropertyBool.create("power");

  public BlockIndicator(String registryName, AxisAlignedBB unrotatedBB, long config, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(registryName, unrotatedBB, config, powerOnSound, powerOffSound); }

  public BlockIndicator(String registryName, AxisAlignedBB unrotatedBB, long config)
  { super(registryName, unrotatedBB, config, null, null); }

  public BlockIndicator(String registryName, AxisAlignedBB unrotatedBB)
  { super(registryName, unrotatedBB, 0,null, null); }

  @Override
  public IBlockState getBlockStateWithPower(IBlockState orig, int power)
  { return orig.withProperty(POWER, power>0); }

  @Override
  public int getBlockStatePower(IBlockState state)
  { return state.getValue(POWER) ? 15 : 0; }

  @Override
  public boolean cmpBlockStatePower(IBlockState state, int power)
  { return state.getValue(POWER) == (power!=0); }

  @Override
  public int getColorMultiplierRGBA(@Nullable IBlockState state, @Nullable IBlockAccess world, @Nullable BlockPos pos)
  {
    if((pos==null) || (world==null)) return 0xffffffff;
    final TileEntityGauge te = getTe(world, pos);
    if(te==null) return 0xffffffff;
    return ModAuxiliaries.DyeColorFilters.byIndex(te.color_tint());
  }

  @Override
  protected BlockStateContainer createBlockState()
  { return new BlockStateContainer(this, FACING, POWER); }

}
