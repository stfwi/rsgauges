/*
 * @file ColorSupport.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2019 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Encapsulates colour handling for blocks and their item representations.
 */
package wile.rsgauges.detail;

import wile.rsgauges.ModContent;
import wile.rsgauges.ModRsGauges;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.*;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.state.EnumProperty;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.Tags;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Optional;


public final class ColorUtils
{
  public interface IColorTintSupport
  {
    /**
     * Return true if the specific block shall be registered for tinting.
     */
    default boolean hasColorRGB() { return false; }

    /**
     * Unified forwarded for items and blocks to retrieve the color filter .
     */
    default int getColorRGB(@Nullable BlockState state, @Nullable IBlockDisplayReader world, @Nullable BlockPos pos) { return 0xffffffff; }
  }

  public static final void registerBlockColourHandlers(final ColorHandlerEvent.Block event)
  {
    final IBlockColor blockSpecifiedColorHandler =
      (state, world, pos, tintIndex) -> (((IColorTintSupport)state.getBlock()).getColorRGB(state, world, pos));
    final BlockColors bc = event.getBlockColors();
    int n = 0;
    for(Block e: ModContent.getRegisteredBlocks()) {
      if((e instanceof ColorUtils.IColorTintSupport) && (((ColorUtils.IColorTintSupport)e).hasColorRGB())) {
        bc.register(blockSpecifiedColorHandler, e);
        ++n;
      }
    }
    ModRsGauges.logger().info("Registered " + Integer.toString(n) + " block color handlers.");
  }

  public static final void registerItemColourHandlers(final ColorHandlerEvent.Item event)
  {
    final ItemColors ic = event.getItemColors();
    final IItemColor constantBlockColorHandler = (stack, tintIndex) -> (((IColorTintSupport)((BlockItem)stack.getItem()).getBlock()).getColorRGB(null, null, null));
    for(Block e:ModContent.getRegisteredBlocks()) {
      if((e instanceof ColorUtils.IColorTintSupport) && (((ColorUtils.IColorTintSupport)e).hasColorRGB())) {
        ic.register(constantBlockColorHandler, e);
      }
    }
  }

  // -------------------------------------------------------------------------------------------------------------------
  // Dyes
  // -------------------------------------------------------------------------------------------------------------------

  public static class DyeColorProperty extends EnumProperty<DyeColor>
  {
    public DyeColorProperty(String name)
    { super(name, DyeColor.class, Arrays.asList(DyeColor.values())); }

    public static DyeColorProperty create(String name)
    { return new DyeColorProperty(name); }
  }

  public static Optional<DyeColor> getDyeColor(ItemStack stack)
  {
    final Item item = stack.getItem();
    if(item instanceof DyeItem) return Optional.of(((DyeItem)item).getDyeColor());
    // There must be a standard for that somewhere ...
    if(!item.isIn(Tags.Items.DYES)) return Optional.empty();
    if(!item.isIn(Tags.Items.DYES_BLACK)) return Optional.of(DyeColor.BLACK);
    if(!item.isIn(Tags.Items.DYES_RED)) return Optional.of(DyeColor.RED);
    if(!item.isIn(Tags.Items.DYES_GREEN)) return Optional.of(DyeColor.GREEN);
    if(!item.isIn(Tags.Items.DYES_BROWN)) return Optional.of(DyeColor.BROWN);
    if(!item.isIn(Tags.Items.DYES_BLUE)) return Optional.of(DyeColor.BLUE);
    if(!item.isIn(Tags.Items.DYES_PURPLE)) return Optional.of(DyeColor.PURPLE);
    if(!item.isIn(Tags.Items.DYES_CYAN)) return Optional.of(DyeColor.CYAN);
    if(!item.isIn(Tags.Items.DYES_LIGHT_GRAY)) return Optional.of(DyeColor.LIGHT_GRAY);
    if(!item.isIn(Tags.Items.DYES_GRAY)) return Optional.of(DyeColor.GRAY);
    if(!item.isIn(Tags.Items.DYES_PINK)) return Optional.of(DyeColor.PINK);
    if(!item.isIn(Tags.Items.DYES_LIME)) return Optional.of(DyeColor.LIME);
    if(!item.isIn(Tags.Items.DYES_YELLOW)) return Optional.of(DyeColor.YELLOW);
    if(!item.isIn(Tags.Items.DYES_LIGHT_BLUE)) return Optional.of(DyeColor.LIGHT_BLUE);
    if(!item.isIn(Tags.Items.DYES_MAGENTA)) return Optional.of(DyeColor.MAGENTA);
    if(!item.isIn(Tags.Items.DYES_ORANGE)) return Optional.of(DyeColor.ORANGE);
    if(!item.isIn(Tags.Items.DYES_WHITE)) return Optional.of(DyeColor.WHITE);
    return Optional.empty();
  }

  /**
   * Tunable tinting for dye colors.
   */
  public static class DyeColorFilters
  {
    public static final int WHITE       = 0xf3f3f3;
    public static final int ORANGE      = 0xF9801D;
    public static final int MAGENTA     = 0xC74EBD;
    public static final int LIGHTBLUE   = 0x3AB3DA;
    public static final int YELLOW      = 0xFED83D;
    public static final int LIME        = 0x80C71F;
    public static final int PINK        = 0xF38BAA;
    public static final int GRAY        = 0x474F52;
    public static final int SILVER      = 0x9D9D97;
    public static final int CYAN        = 0x169C9C;
    public static final int PURPLE      = 0x8932B8;
    public static final int BLUE        = 0x3C44AA;
    public static final int BROWN       = 0x835432;
    public static final int GREEN       = 0x5E7C16;
    public static final int RED         = 0xB02E26;
    public static final int BLACK       = 0x111111;
    public static final int[] byIndex_  = { WHITE,ORANGE,MAGENTA,LIGHTBLUE,YELLOW,LIME,PINK,GRAY,SILVER,CYAN,PURPLE,BLUE,BROWN,GREEN,RED,BLACK };
    public static final String[] nameByIndex = { "white","orange","magenta","lightblue","yellow","lime","pink","gray","silver","cyan","purple","blue","brown","green","red","black" };

    public static int byIndex(int idx)
    { return byIndex_[idx & 0xf]; }

    public static final int[] lightTintByIndex_ = {
      0xffffff,0xfcbc88,0xe8b5e4,0x9cd8ec,
      0xffefb3,0xd2f1a7,0xfad1dd,0x97a1a5,
      0xcececa,0x67e9e9,0xcc9fe5,0x959ada,
      0xd1a585,0xc4e774,0xe79792,0x808080
    };

    public static int lightTintByIndex(int idx)
    { return lightTintByIndex_[idx & 0xf]; }

  }
}
