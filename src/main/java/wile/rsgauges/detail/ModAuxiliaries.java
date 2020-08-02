/*
 * @file ModAuxiliaries.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * General commonly used functionality.
 */
package wile.rsgauges.detail;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import wile.rsgauges.ModRsGauges;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import org.lwjgl.input.Keyboard;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModAuxiliaries
{
  /**
   * Text localisation wrapper, implicitly prepends `ModRsGauges.MODID` to the
   * translation keys. Forces formatting argument, nullable if no special formatting shall be applied..
   */
  public static TextComponentTranslation localizable(String modtrkey, @Nullable TextFormatting color, Object... args)
  {
    TextComponentTranslation tr = new TextComponentTranslation(ModRsGauges.MODID+"."+modtrkey, args);
    if(color!=null) tr.getStyle().setColor(color);
    return tr;
  }

  @SideOnly(Side.CLIENT)
  public static String localize(String translationKey, Object... args)
  {
    TextComponentTranslation tr = new TextComponentTranslation(translationKey, args);
    tr.getStyle().setColor(TextFormatting.RESET);
    final String ft = tr.getFormattedText();
    if(ft.contains("${")) {
      // Non-recursive, non-argument lang file entry cross referencing.
      Pattern pt = Pattern.compile("\\$\\{([\\w\\.]+)\\}");
      Matcher mt = pt.matcher(ft);
      StringBuffer sb = new StringBuffer();
      while(mt.find()) mt.appendReplacement(sb, (new TextComponentTranslation(mt.group(1))).getFormattedText().trim());
      mt.appendTail(sb);
      return sb.toString();
    } else {
      return ft;
    }
  }

  /**
   * Returns true if a given key is translated for the current language.
   */
  @SideOnly(Side.CLIENT)
  public static boolean hasTranslation(String key)
  { return net.minecraft.client.resources.I18n.hasKey(key); }

  public static final class Tooltip
  {
    @SideOnly(Side.CLIENT)
    public static boolean extendedTipCondition()
    { return (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)||Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)); }

    @SideOnly(Side.CLIENT)
    public static boolean helpCondition()
    { return extendedTipCondition() && ((Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)||Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))); }

    /**
     * Adds an extended tooltip or help tooltip depending on the key states of CTRL and SHIFT.
     * Returns true if the localisable help/tip was added, false if not (either not CTL/SHIFT or
     * no translation found).
     */
    @SideOnly(Side.CLIENT)
    public static boolean addInformation(@Nullable String advancedTooltipTranslationKey, @Nullable String helpTranslationKey, List<String> tooltip, ITooltipFlag flag, boolean addAdvancedTooltipHints)
    {
      // Note: intentionally not using keybinding here, this must be `control` or `shift`. MC uses lwjgl Keyboard,
      //       so using this also here should be ok.
      final boolean help_available = (helpTranslationKey != null) && ModAuxiliaries.hasTranslation(helpTranslationKey + ".help");
      final boolean tip_available = (advancedTooltipTranslationKey != null) && ModAuxiliaries.hasTranslation(helpTranslationKey + ".tip");
      if((!help_available) && (!tip_available)) return false;
      if(helpCondition()) {
        if(!help_available) return false;
        String s = localize(helpTranslationKey + ".help");
        if(s.isEmpty()) return false;
        tooltip.add(s);
        return true;
      } else if(extendedTipCondition()) {
        if(!tip_available) return false;
        String s = localize(advancedTooltipTranslationKey + ".tip");
        if(s.isEmpty()) return false;
        tooltip.add(s);
        return true;
      } else if(addAdvancedTooltipHints) {
        String s = "";
        if(tip_available) s += localize(ModRsGauges.MODID + ".tooltip.hint.extended") + (help_available ? " " : "");
        if(help_available) s += localize(ModRsGauges.MODID + ".tooltip.hint.help");
        tooltip.add(s);
      }
      return false;
    }

    /**
     * Adds an extended tooltip or help tooltip for a given stack depending on the key states of CTRL and SHIFT.
     * Format in the lang file is (e.g. for items): "item.MODID.REGISTRYNAME.tip" and "item.MODID.REGISTRYNAME.help".
     * Return value see method pattern above.
     */
    @SideOnly(Side.CLIENT)
    public static boolean addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag, boolean addAdvancedTooltipHints)
    { return addInformation(stack.getTranslationKey(), stack.getTranslationKey(), tooltip, flag, addAdvancedTooltipHints); }
  }

  /**
   * Send a chat message to the player.
   * Server side usage only.
   */
  @SuppressWarnings("unused")
  public static void playerChatMessage(EntityPlayer player, final String message)
  {
    String s = message.trim();
    if(!s.isEmpty()) player.sendMessage(new TextComponentTranslation(s));
  }

  /**
   * Send RsBlock status message to the player.
   * Server side usage only.
   */
  public static void playerStatusMessage(EntityPlayer player, final TextComponentTranslation message)
  {
    if(ModConfig.zmisc.without_switch_status_overlay) {
      player.sendMessage(message);
    } else {
      Networking.OverlayTextMessage.sendToClient((EntityPlayerMP)player, message);
    }
  }

  public static final AxisAlignedBB getPixeledAABB(double x0, double y0, double z0, double x1, double y1, double z1)
  { return new AxisAlignedBB(x0/16.0, y0/16.0, z0/16.0, x1/16.0, y1/16.0, z1/16.0); }

  /**
   * Rotates an aabb from default facing EASE (direction x+) to another facing.
   */
  public static AxisAlignedBB transform_forward(final AxisAlignedBB bb, final EnumFacing facing)
  {
    switch(facing.getIndex()) {
      case 0: return new AxisAlignedBB(  bb.minY, -bb.minX,  bb.minZ,  bb.maxY, -bb.maxX,  bb.maxZ); // D
      case 1: return new AxisAlignedBB( -bb.minY,  bb.minX,  bb.minZ, -bb.maxY,  bb.maxX,  bb.maxZ); // U
      case 2: return new AxisAlignedBB(  bb.minZ,  bb.minY, -bb.minX,  bb.maxZ,  bb.maxY, -bb.maxX); // N
      case 3: return new AxisAlignedBB( -bb.minZ,  bb.minY,  bb.minX, -bb.maxZ,  bb.maxY,  bb.maxX); // S
      case 4: return new AxisAlignedBB( -bb.minX,  bb.minY, -bb.minZ, -bb.maxX,  bb.maxY, -bb.maxZ); // W
      case 5: return new AxisAlignedBB(  bb.minX,  bb.minY,  bb.minZ,  bb.maxX,  bb.maxY,  bb.maxZ); // E --> bb
      default: return new AxisAlignedBB(0,0,0, 0.1,0.1,0.1);
    }
  }

  /**
   * Transforms a block position, rotated around the world origin from EAST
   * to facing.
   */
  @SuppressWarnings("unused")
  public static BlockPos transform_forward(final BlockPos pos, final EnumFacing facing)
  {
    switch(facing.getIndex()) {
      case 0: return new BlockPos(  pos.getY(), -pos.getX(),  pos.getZ()); // D
      case 1: return new BlockPos( -pos.getY(),  pos.getX(),  pos.getZ()); // U
      case 2: return new BlockPos(  pos.getZ(),  pos.getY(), -pos.getX()); // N
      case 3: return new BlockPos( -pos.getZ(),  pos.getY(),  pos.getX()); // S
      case 4: return new BlockPos( -pos.getX(),  pos.getY(), -pos.getZ()); // W
      case 5: return new BlockPos(  pos.getX(),  pos.getY(),  pos.getZ()); // E --> bb
      default: return pos;
    }
  }

  /**
   * Returns a time string in 24:00 hour format.
   */
  public static String daytimeToString(long t)
  {
    t = (t + 6000) % 24000; // day starts at 06:00 with t==0.
    // @check: java must have string formatting somehow.
    String sh = Long.toString((t/1000));
    String sm = Long.toString((t%1000)*60/1000);
    if(sh.length() < 2) sh = "0" + sh;
    if(sm.length() < 2) sm = "0" + sm;
    return sh + ":" + sm;
  }

  /**
   * Returns a string, where ticks are converted to seconds.
   */
  public static String ticksToSecondsString(long t)
  { return String.format("%.02f", ((double)t)/20.0); }

  /**
   * Prefer world.isRemote, only use this if world is not available.
   */
  public static boolean isClientSide()
  { return (FMLCommonHandler.instance().getSide() == Side.CLIENT); }

  /**
   * Class allowing to have the dye colors also available on
   * server side. (EnumDyeColor not available on dedicated servers).
   * Server and client side usage.
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

  /**
   * Material definitions specific to blocks of this Mod.
   */
  public static class RsMaterials
  {
    public static final Material MATERIAL_TRAPDOORSWITCH = new MetalMaterial();
    public static final Material MATERIAL_METALLIC = new MetalMaterial();
    public static final Material MATERIAL_PLANT = new PlantMaterial();

    public static class MetalMaterial extends Material {
      public MetalMaterial() { super(MapColor.IRON); }
      @Override public boolean isLiquid() { return false; }
      @Override public boolean blocksLight() { return false; }
      @Override public boolean blocksMovement() { return true; }
      @Override public boolean isToolNotRequired()  { return true; }
    }
    public static class PlantMaterial extends Material {
      public PlantMaterial() { super(MapColor.GRASS); }
      @Override public boolean isLiquid() { return false; }
      @Override public boolean blocksLight() { return false; }
      @Override public boolean blocksMovement() { return false; }
      @Override public boolean isToolNotRequired() { return true; }
    }
  }

  public static class BlockCategories
  {
    // doing this using bit mask instead of enum list.
    public static final long BLOCKCAT_NONE                = 0x0000000000000000l;
    public static final long BLOCKCAT_NATURAL             = 0x0000000000000001l;
    public static final long BLOCKCAT_ORE                 = 0x0000000000000002l;
    public static final long BLOCKCAT_STAIR               = 0x0000000000000004l;
    public static final long BLOCKCAT_SLAB                = 0x0000000000000008l;
    public static final long BLOCKCAT_PLANK               = 0x0000000000000010l;
    public static final long BLOCKCAT_SOIL                = 0x0000000000000020l;
    public static final long BLOCKCAT_PLANT               = 0x0000000000000040l;
    public static final long BLOCKCAT_SAPLING             = 0x0000000000000080l;
    public static final long BLOCKCAT_CROP                = 0x0000000000000100l;
    public static final long BLOCKCAT_MATERIAL_WOOD       = 0x0000000001000000l;
    public static final long BLOCKCAT_MATERIAL_STONE      = 0x0000000002000000l;
    public static final long BLOCKCAT_MATERIAL_GLASS      = 0x0000000004000000l;
    public static final long BLOCKCAT_MATERIAL_CLAY       = 0x0000000008000000l;
    public static final long BLOCKCAT_MATERIAL_IRON       = 0x0000000010000000l;
    public static final long BLOCKCAT_MATERIAL_GOLD       = 0x0000000020000000l;
    public static final long BLOCKCAT_MATERIAL_DIAMOND    = 0x0000000040000000l;
    public static final long BLOCKCAT_MATERIAL_EMERALD    = 0x0000000080000000l;
    public static final long BLOCKCAT_MATERIAL_OBSIDIAN   = 0x0000000100000000l;
    public static final long BLOCKCAT_MATERIAL_REDSTONE   = 0x0000000200000000l;
    public static final long BLOCKCAT_MATERIAL_QUARZ      = 0x0000000400000000l;
    public static final long BLOCKCAT_MATERIAL_CONCRETE   = 0x0000000800000000l;
    public static final long BLOCKCAT_MATERIAL_PRISMARINE = 0x0000001000000000l;
    public static final long BLOCKCAT_MATERIAL_WATER      = 0x0000002000000000l;

    private static Map<Block, Long> reverse_lut_ = Collections.synchronizedMap(new HashMap<>());

    /**
     * Should be called in postInit() and when ore dictionary update
     * events are fired (e.g. when unlocking blocks due to age advancement).
     */
    public static void compose()
    {
      Map<Block, Long> map = new HashMap<Block, Long>();
      for(String key: OreDictionary.getOreNames()) {
        List<ItemStack> stacks = OreDictionary.getOres(key);
        if(stacks==null) continue;
        long mask = 0l;
        // material categories
        if(key.matches("(.+)Wood$")) mask |= BLOCKCAT_MATERIAL_WOOD;
        if(key.matches("(.+)Obsidian$|(.+)Stone$|^stone[A-Z](.*)") || key.matches("(.*)(end|sand|cobble)stone$")) mask |= BLOCKCAT_MATERIAL_STONE;
        if(key.matches("(.*)[a-z]Glass(.*)")) mask |= BLOCKCAT_MATERIAL_GLASS;
        // block type categories
        if(key.matches("^ore[A-Z](.*)")) mask |= BLOCKCAT_ORE;
        else if(key.matches("^stair[A-Z](.*)")) mask |= BLOCKCAT_STAIR;
        else if(key.matches("^plank[A-Z](.*)")) mask |= BLOCKCAT_PLANK;
        else if(key.matches("^(grass|sand|dirt|farmland)($|[^a-z](.*))")) mask |= BLOCKCAT_SOIL;
        else if(key.matches("^treeSapling(.*)")) mask |= BLOCKCAT_SAPLING|BLOCKCAT_PLANT;
        for(ItemStack stack: stacks) {
          if(!(stack.getItem() instanceof ItemBlock)) continue;
          Block block = ((ItemBlock)stack.getItem()).getBlock();
          if(block != null) map.put(block, map.getOrDefault(block, BLOCKCAT_NONE) | mask);
        }
        // Corrections what's missing, maybe push this into a config file?
        BiConsumer<Block,Long> push = (blck, msk) -> map.put(blck, map.getOrDefault(blck, BLOCKCAT_NONE) | msk);
        //
        push.accept(Blocks.MYCELIUM, BLOCKCAT_SOIL);
        push.accept(Blocks.SOUL_SAND, BLOCKCAT_SOIL);
        push.accept(Blocks.FARMLAND, BLOCKCAT_SOIL);
        //
        push.accept(Blocks.PUMPKIN_STEM, BLOCKCAT_PLANT);
        push.accept(Blocks.MELON_STEM, BLOCKCAT_PLANT);
        push.accept(Blocks.CACTUS, BLOCKCAT_PLANT);
        push.accept(Blocks.WATERLILY, BLOCKCAT_PLANT);
        push.accept(Blocks.BROWN_MUSHROOM, BLOCKCAT_PLANT);
        push.accept(Blocks.RED_MUSHROOM, BLOCKCAT_PLANT);
        push.accept(Blocks.VINE, BLOCKCAT_PLANT);
        push.accept(Blocks.LEAVES, BLOCKCAT_PLANT);
        push.accept(Blocks.LEAVES2, BLOCKCAT_PLANT);
        push.accept(Blocks.YELLOW_FLOWER, BLOCKCAT_PLANT);
        push.accept(Blocks.CHORUS_FLOWER, BLOCKCAT_PLANT);
        push.accept(Blocks.CHORUS_PLANT, BLOCKCAT_PLANT);
        push.accept(Blocks.RED_FLOWER, BLOCKCAT_PLANT);
        push.accept(Blocks.DOUBLE_PLANT, BLOCKCAT_PLANT);
        push.accept(Blocks.TALLGRASS, BLOCKCAT_PLANT);
        //
        push.accept(Blocks.CARROTS, BLOCKCAT_PLANT|BLOCKCAT_CROP);
        push.accept(Blocks.BEETROOTS, BLOCKCAT_PLANT|BLOCKCAT_CROP);
        push.accept(Blocks.WHEAT, BLOCKCAT_PLANT|BLOCKCAT_CROP);
        push.accept(Blocks.POTATOES, BLOCKCAT_PLANT|BLOCKCAT_CROP);
        push.accept(Blocks.PUMPKIN, BLOCKCAT_PLANT|BLOCKCAT_CROP);
        push.accept(Blocks.MELON_BLOCK, BLOCKCAT_PLANT|BLOCKCAT_CROP);
        //
        push.accept(Blocks.BOOKSHELF, BLOCKCAT_MATERIAL_WOOD);
        push.accept(Blocks.BED, BLOCKCAT_MATERIAL_WOOD);
        //
        push.accept(Blocks.BEDROCK, BLOCKCAT_MATERIAL_STONE);
        push.accept(Blocks.MAGMA, BLOCKCAT_MATERIAL_STONE);
        push.accept(Blocks.MOSSY_COBBLESTONE, BLOCKCAT_MATERIAL_STONE);
        push.accept(Blocks.MOSSY_COBBLESTONE, BLOCKCAT_MATERIAL_STONE);
        push.accept(Blocks.STONE_BRICK_STAIRS, BLOCKCAT_MATERIAL_STONE);
        push.accept(Blocks.STONE_SLAB, BLOCKCAT_MATERIAL_STONE);
        push.accept(Blocks.STONE_SLAB2, BLOCKCAT_MATERIAL_STONE);
        push.accept(Blocks.STONE_STAIRS, BLOCKCAT_MATERIAL_STONE);
        push.accept(Blocks.STONEBRICK, BLOCKCAT_MATERIAL_STONE);
        push.accept(Blocks.BRICK_BLOCK, BLOCKCAT_MATERIAL_STONE);
        push.accept(Blocks.BRICK_STAIRS, BLOCKCAT_MATERIAL_STONE);
        push.accept(Blocks.DOUBLE_STONE_SLAB, BLOCKCAT_MATERIAL_STONE);
        push.accept(Blocks.BRICK_BLOCK, BLOCKCAT_MATERIAL_STONE);
        push.accept(Blocks.END_STONE, BLOCKCAT_MATERIAL_STONE);
        push.accept(Blocks.END_BRICKS, BLOCKCAT_MATERIAL_STONE);
        push.accept(Blocks.SANDSTONE_STAIRS, BLOCKCAT_MATERIAL_STONE);
        push.accept(Blocks.RED_SANDSTONE_STAIRS, BLOCKCAT_MATERIAL_STONE);
        //
        push.accept(Blocks.GLOWSTONE, BLOCKCAT_MATERIAL_GLASS);
        push.accept(Blocks.SEA_LANTERN, BLOCKCAT_MATERIAL_GLASS);
        push.accept(Blocks.REDSTONE_LAMP, BLOCKCAT_MATERIAL_GLASS);
        //
        push.accept(Blocks.OBSIDIAN, BLOCKCAT_MATERIAL_OBSIDIAN);
        push.accept(Blocks.ENCHANTING_TABLE, BLOCKCAT_MATERIAL_OBSIDIAN) ;
        push.accept(Blocks.ENDER_CHEST, BLOCKCAT_MATERIAL_OBSIDIAN);
        //
        push.accept(Blocks.CLAY, BLOCKCAT_MATERIAL_CLAY);
        push.accept(Blocks.HARDENED_CLAY, BLOCKCAT_MATERIAL_CLAY);
        push.accept(Blocks.STAINED_HARDENED_CLAY, BLOCKCAT_MATERIAL_CLAY);
        //
        push.accept(Blocks.QUARTZ_BLOCK, BLOCKCAT_MATERIAL_QUARZ);
        push.accept(Blocks.QUARTZ_STAIRS, BLOCKCAT_MATERIAL_QUARZ);
        //
        push.accept(Blocks.PRISMARINE, BLOCKCAT_MATERIAL_PRISMARINE);
        //
        push.accept(Blocks.ANVIL, BLOCKCAT_MATERIAL_IRON);
        push.accept(Blocks.IRON_BLOCK, BLOCKCAT_MATERIAL_IRON);
        push.accept(Blocks.IRON_BARS, BLOCKCAT_MATERIAL_IRON);
        push.accept(Blocks.IRON_DOOR, BLOCKCAT_MATERIAL_IRON);
        push.accept(Blocks.IRON_TRAPDOOR, BLOCKCAT_MATERIAL_IRON);
        //
        push.accept(Blocks.WATER, BLOCKCAT_MATERIAL_WATER);
        push.accept(Blocks.FLOWING_WATER, BLOCKCAT_MATERIAL_WATER);
        push.accept(Blocks.ICE, BLOCKCAT_MATERIAL_WATER);
        push.accept(Blocks.FROSTED_ICE, BLOCKCAT_MATERIAL_WATER);
        push.accept(Blocks.PACKED_ICE, BLOCKCAT_MATERIAL_WATER);
        push.accept(Blocks.SNOW, BLOCKCAT_MATERIAL_WATER);
        push.accept(Blocks.SNOW_LAYER, BLOCKCAT_MATERIAL_WATER);
      }
      // need synchronized?
      reverse_lut_.clear();
      reverse_lut_.putAll(map);
    }

    public static long lookup_block_categories(Block block)
    { return (block==null) ? BLOCKCAT_NONE : reverse_lut_.getOrDefault(block, BLOCKCAT_NONE); }

    public static boolean match_block_categories(Block block, long category_mask)
    { return (block!=null) && ((reverse_lut_.getOrDefault(block, BLOCKCAT_NONE) & category_mask)!=0); }

  }

}
