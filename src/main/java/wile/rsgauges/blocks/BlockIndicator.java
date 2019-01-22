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

import wile.rsgauges.detail.ModResources;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import javax.annotation.Nullable;


public class BlockIndicator extends BlockGauge
{
  public static final PropertyBool POWER = PropertyBool.create("power");

  public BlockIndicator(String registryName, AxisAlignedBB unrotatedBB, int powerToLightValueScaling0To15, int blinkInterval, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(registryName, unrotatedBB, powerToLightValueScaling0To15, blinkInterval, powerOnSound, powerOffSound); }

  public BlockIndicator(String registryName, AxisAlignedBB unrotatedBB, int powerToLightValueScaling0To15, int blinkInterval)
  { super(registryName, unrotatedBB, powerToLightValueScaling0To15, blinkInterval, null, null); }

  public BlockIndicator(String registryName, AxisAlignedBB unrotatedBB)
  { super(registryName, unrotatedBB, 0, 0, null, null); }

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
  protected BlockStateContainer createBlockState()
  { return new BlockStateContainer(this, FACING, POWER); }

}
