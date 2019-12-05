/*
 * @file ColorSupport.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2019 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Encapsulates colour handling for blocks and their item representations.
 */
package wile.rsgauges.detail;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import wile.rsgauges.ModContent;
import wile.rsgauges.ModRsGauges;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.BlockItem;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraft.util.math.BlockPos;
import javax.annotation.Nullable;


public final class ModColors
{
  public interface ColorTintSupport
  {
    /**
     * Return true if the specific block shall be registered for tinting.
     */
    default boolean hasColorMultiplierRGBA() { return false; }

    /**
     * Unified forwarded for items and blocks to retrieve the color filter .
     */
    default int getColorMultiplierRGBA(@Nullable BlockState state, @Nullable IEnviromentBlockReader world, @Nullable BlockPos pos) { return 0xffffffff; }
  }

  public static final void registerBlockColourHandlers(final ColorHandlerEvent.Block event)
  {
    final IBlockColor blockSpecifiedColorHandler =
      (state, world, pos, tintIndex) -> (((ColorTintSupport)state.getBlock()).getColorMultiplierRGBA(state, world, pos));
    final BlockColors bc = event.getBlockColors();
    int n = 0;
    for(Block e: ModContent.getRegisteredBlocks()) {
      if((e instanceof ModColors.ColorTintSupport) && (((ModColors.ColorTintSupport)e).hasColorMultiplierRGBA())) {
        bc.register(blockSpecifiedColorHandler, e);
        ++n;
      }
    }
    ModRsGauges.logger().info("Registered " + Integer.toString(n) + " block color handlers.");
  }

  public static final void registerItemColourHandlers(final ColorHandlerEvent.Item event)
  {
    final ItemColors ic = event.getItemColors();
    final IItemColor constantBlockColorHandler = (stack, tintIndex) -> (((ColorTintSupport)((BlockItem)stack.getItem()).getBlock()).getColorMultiplierRGBA(null, null, null));
    for(Block e:ModContent.getRegisteredBlocks()) {
      if((e instanceof ModColors.ColorTintSupport) && (((ModColors.ColorTintSupport)e).hasColorMultiplierRGBA())) {
        ic.register(constantBlockColorHandler, e);
      }
    }
  }
}
