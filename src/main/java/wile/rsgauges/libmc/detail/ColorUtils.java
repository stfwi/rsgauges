/*
 * @file ColorSupport.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2019 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Encapsulates colour handling for blocks and their item representations.
 */
package wile.rsgauges.libmc.detail;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public final class ColorUtils
{
  /**
   * Vanilla interface wrapper allowing to filter the tintable blocks during color handler registration.
   */
  public interface IBlockColorTintSupport extends BlockColor
  {
    default boolean hasColorTint() { return false; }
    default int getColor(BlockState state, @Nullable BlockAndTintGetter world, @Nullable BlockPos pos, int tintIndex) { return 0xffffffff; }
  }

  public interface IItemColorTintSupport extends ItemColor
  {
    default boolean hasColorTint() { return false; }
    default int getColor(ItemStack stack, int tintIndex) { return 0xffffffff; }
  }

  private static Supplier<List<Block>> blocks_supplier_ = Collections::emptyList;
  private static Supplier<List<Item>>  items_supplier_  = Collections::emptyList;

  public static void init(Supplier<List<Block>> registeredBlocksSuplier, Supplier<List<Item>> registeredItemsSuplier)
  { blocks_supplier_ = registeredBlocksSuplier; items_supplier_  = registeredItemsSuplier; }

  @OnlyIn(Dist.CLIENT)
  public static void registerBlockColourHandlers(final ColorHandlerEvent.Block event)
  {
    if(!blocks_supplier_.get().isEmpty()) {
      event.getBlockColors().register(
        (state, world, pos, tintIndex) -> (((IBlockColorTintSupport)state.getBlock()).getColor(state, world, pos, tintIndex)), // handler
        (blocks_supplier_.get().stream()
          .filter(b -> ((b instanceof IBlockColorTintSupport) && (((IBlockColorTintSupport)b).hasColorTint())))
          .collect(Collectors.toList())).toArray(new Block[]{}) // supporting blocks
      );
    }
  }

  @OnlyIn(Dist.CLIENT)
  public static void registerItemColourHandlers(final ColorHandlerEvent.Item event)
  {
    if(!items_supplier_.get().isEmpty()) {
      event.getItemColors().register(
        (ItemStack stack, int tintIndex) -> (((IItemColorTintSupport)(stack.getItem())).getColor(stack, tintIndex)),
        items_supplier_.get().stream()
          .filter(e -> ((e instanceof IItemColorTintSupport) && (((IItemColorTintSupport)e).hasColorTint())))
          .collect(Collectors.toList()).toArray(new ItemLike[]{})
      );
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

  public static boolean isDye(ItemStack stack)
  { return getColorFromDyeItem(stack).isPresent(); }

  public static Optional<DyeColor> getColorFromDyeItem(ItemStack stack)
  {
    final Item item = stack.getItem();
    if(item instanceof DyeItem) return Optional.of(((DyeItem)item).getDyeColor());
    // There must be a standard for that somewhere ...
    if(stack.is(Tags.Items.DYES_BLACK)) return Optional.of(DyeColor.BLACK);
    if(stack.is(Tags.Items.DYES_RED)) return Optional.of(DyeColor.RED);
    if(stack.is(Tags.Items.DYES_GREEN)) return Optional.of(DyeColor.GREEN);
    if(stack.is(Tags.Items.DYES_BROWN)) return Optional.of(DyeColor.BROWN);
    if(stack.is(Tags.Items.DYES_BLUE)) return Optional.of(DyeColor.BLUE);
    if(stack.is(Tags.Items.DYES_PURPLE)) return Optional.of(DyeColor.PURPLE);
    if(stack.is(Tags.Items.DYES_CYAN)) return Optional.of(DyeColor.CYAN);
    if(stack.is(Tags.Items.DYES_LIGHT_GRAY)) return Optional.of(DyeColor.LIGHT_GRAY);
    if(stack.is(Tags.Items.DYES_GRAY)) return Optional.of(DyeColor.GRAY);
    if(stack.is(Tags.Items.DYES_PINK)) return Optional.of(DyeColor.PINK);
    if(stack.is(Tags.Items.DYES_LIME)) return Optional.of(DyeColor.LIME);
    if(stack.is(Tags.Items.DYES_YELLOW)) return Optional.of(DyeColor.YELLOW);
    if(stack.is(Tags.Items.DYES_LIGHT_BLUE)) return Optional.of(DyeColor.LIGHT_BLUE);
    if(stack.is(Tags.Items.DYES_MAGENTA)) return Optional.of(DyeColor.MAGENTA);
    if(stack.is(Tags.Items.DYES_ORANGE)) return Optional.of(DyeColor.ORANGE);
    if(stack.is(Tags.Items.DYES_WHITE)) return Optional.of(DyeColor.WHITE);
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
