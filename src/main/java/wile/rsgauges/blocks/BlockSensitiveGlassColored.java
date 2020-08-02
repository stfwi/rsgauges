/*
 * @file BlockSensitiveGlassColored.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Color changing versions of the sensitive glass.
 */
package wile.rsgauges.blocks;

import wile.rsgauges.detail.ModColors;
import net.minecraft.world.ILightReader;
import net.minecraft.block.BlockState;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import javax.annotation.Nullable;


public class BlockSensitiveGlassColored extends BlockSensitiveGlass implements ModColors.ColorTintSupport
{
  protected final int color_multiplier_value;

  // -------------------------------------------------------------------------------------------------------------------

  public BlockSensitiveGlassColored(Block.Properties properties, int colorMultiplierValue)
  { super(properties); color_multiplier_value = colorMultiplierValue; }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  public int getLightValue(BlockState state)
  { return 0; }

  @Override
  public boolean canSpawnInBlock()
  { return false; }

  // -------------------------------------------------------------------------------------------------------------------

  @Override
  public boolean hasColorMultiplierRGBA()
  { return true; }

  @Override
  public int getColorMultiplierRGBA(@Nullable BlockState state, @Nullable ILightReader world, @Nullable BlockPos pos)
  { return color_multiplier_value; }

}
