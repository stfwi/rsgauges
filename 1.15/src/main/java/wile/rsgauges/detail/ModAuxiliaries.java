/*
 * @file ModAuxiliaries.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * General commonly used functionality.
 */
package wile.rsgauges.detail;

import wile.rsgauges.ModRsGauges;
import wile.rsgauges.ModConfig;
import net.minecraft.util.text.*;
import net.minecraft.world.IBlockReader;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class ModAuxiliaries
{
  public static final String MODID = ModRsGauges.MODID;
  public static final Logger LOGGER = ModRsGauges.logger();
  private static final ModRsGauges.ISidedProxy proxy = ModRsGauges.proxy;

  // -------------------------------------------------------------------------------------------------------------------
  // Sideness, system/environment, tagging interfaces
  // -------------------------------------------------------------------------------------------------------------------

  public interface IExperimentalFeature {}

  public static boolean isClientSide()
  { return proxy.mc() != null; }

  public static final boolean isModLoaded(final String registry_name)
  { return ModList.get().isLoaded(registry_name); }

  // -------------------------------------------------------------------------------------------------------------------
  // Logging
  // -------------------------------------------------------------------------------------------------------------------

  public static final void logInfo(final String msg)
  { LOGGER.info(msg); }

  public static final void logWarn(final String msg)
  { LOGGER.warn(msg); }

  public static final void logError(final String msg)
  { LOGGER.error(msg); }

  // -------------------------------------------------------------------------------------------------------------------
  // Localization, text formatting
  // -------------------------------------------------------------------------------------------------------------------

  /**
   * Text localisation wrapper, implicitly prepends `MODID` to the
   * translation keys. Forces formatting argument, nullable if no special formatting shall be applied..
   */
  public static TranslationTextComponent localizable(String modtrkey, @Nullable TextFormatting color, Object... args)
  {
    TranslationTextComponent tr = new TranslationTextComponent(MODID+"."+modtrkey, args);
    if(color!=null) tr.getStyle().setColor(color);
    return tr;
  }

  @OnlyIn(Dist.CLIENT)
  public static String localize(String translationKey, Object... args)
  {
    TranslationTextComponent tr = new TranslationTextComponent(translationKey, args);
    tr.getStyle().setColor(TextFormatting.RESET);
    final String ft = tr.getFormattedText();
    if(ft.contains("${")) {
      // Non-recursive, non-argument lang file entry cross referencing.
      Pattern pt = Pattern.compile("\\$\\{([\\w\\.]+)\\}");
      Matcher mt = pt.matcher(ft);
      StringBuffer sb = new StringBuffer();
      while(mt.find()) mt.appendReplacement(sb, (new TranslationTextComponent(mt.group(1))).getFormattedText().trim());
      mt.appendTail(sb);
      return sb.toString();
    } else {
      return ft;
    }
  }

  /**
   * Returns true if a given key is translated for the current language.
   */
  @OnlyIn(Dist.CLIENT)
  public static boolean hasTranslation(String key)
  { return net.minecraft.client.resources.I18n.hasKey(key); }

  public static final class Tooltip
  {
    @OnlyIn(Dist.CLIENT)
    public static boolean extendedTipCondition()
    {
      return (
        InputMappings.isKeyDown(proxy.mc().getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) ||
        InputMappings.isKeyDown(proxy.mc().getMainWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_SHIFT)
      );
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean helpCondition()
    {
      return extendedTipCondition() && (
        InputMappings.isKeyDown(proxy.mc().getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_CONTROL) ||
        InputMappings.isKeyDown(proxy.mc().getMainWindow().getHandle(), GLFW.GLFW_KEY_RIGHT_CONTROL)
      );
    }

    /**
     * Adds an extended tooltip or help tooltip depending on the key states of CTRL and SHIFT.
     * Returns true if the localisable help/tip was added, false if not (either not CTL/SHIFT or
     * no translation found).
     */
    @OnlyIn(Dist.CLIENT)
    public static boolean addInformation(@Nullable String advancedTooltipTranslationKey, @Nullable String helpTranslationKey, List<ITextComponent> tooltip, ITooltipFlag flag, boolean addAdvancedTooltipHints)
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
        tooltip.add(new StringTextComponent(s));
        return true;
      } else if(extendedTipCondition()) {
        if(!tip_available) return false;
        String s = localize(advancedTooltipTranslationKey + ".tip");
        if(s.isEmpty()) return false;
        tooltip.add(new StringTextComponent(s));
        return true;
      } else if(addAdvancedTooltipHints) {
        String s = "";
        if(tip_available) s += localize(MODID + ".tooltip.hint.extended") + (help_available ? " " : "");
        if(help_available) s += localize(MODID + ".tooltip.hint.help");
        tooltip.add(new StringTextComponent(s));
      }
      return false;
    }

    /**
     * Adds an extended tooltip or help tooltip for a given stack depending on the key states of CTRL and SHIFT.
     * Format in the lang file is (e.g. for items): "item.MODID.REGISTRYNAME.tip" and "item.MODID.REGISTRYNAME.help".
     * Return value see method pattern above.
     */
    @OnlyIn(Dist.CLIENT)
    public static boolean addInformation(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag, boolean addAdvancedTooltipHints)
    { return addInformation(stack.getTranslationKey(), stack.getTranslationKey(), tooltip, flag, addAdvancedTooltipHints); }

  }

  /**
   * Returns a string, where ticks are converted to seconds.
   */
  public static String ticksToSecondsString(long t)
  { return String.format("%.02f", ((double)t)/20.0); }

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

  // -------------------------------------------------------------------------------------------------------------------
  // Chat messages
  // -------------------------------------------------------------------------------------------------------------------

  /**
   * Send a chat message to the player.
   * Server side usage only.
   */
  @SuppressWarnings("unused")
  public static void playerChatMessage(PlayerEntity player, final String message)
  {
    String s = message.trim();
    if(!s.isEmpty()) player.sendMessage(new TranslationTextComponent(s));
  }

  /**
   * Send RsBlock status message to the player.
   * Server side usage only.
   */
  public static void playerStatusMessage(PlayerEntity player, final ITextComponent message)
  {
    if(ModConfig.status_overlay_disabled) {
      player.sendMessage(message);
    } else {
      Networking.OverlayTextMessage.sendToPlayer(player, message);
    }
  }

  // -------------------------------------------------------------------------------------------------------------------
  // Coorsys/AABB transformations,
  // -------------------------------------------------------------------------------------------------------------------

  /**
   * Rotates an aabb from default facing EASE (direction x+) to another facing.
   */
  @SuppressWarnings("all") // suspicious parameter blabla
  public static AxisAlignedBB transform_forward(final AxisAlignedBB bb, final Direction facing)
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
  public static BlockPos transform_forward(final BlockPos pos, final Direction facing)
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
   * Returns an AABB, where all values are scaled 1/16.
   */
  public static final AxisAlignedBB getPixeledAABB(double x0, double y0, double z0, double x1, double y1, double z1)
  { return new AxisAlignedBB(x0/16.0, y0/16.0, z0/16.0, x1/16.0, y1/16.0, z1/16.0); }

  public static final AxisAlignedBB getRotatedAABB(AxisAlignedBB bb, Direction new_facing, boolean horizontal_rotation)
  {
    if(!horizontal_rotation) {
      switch(new_facing.getIndex()) {
        case 0: return new AxisAlignedBB(1-bb.maxX, 1-bb.maxZ, 1-bb.maxY, 1-bb.minX, 1-bb.minZ, 1-bb.minY); // D
        case 1: return new AxisAlignedBB(1-bb.maxX,   bb.minZ,   bb.minY, 1-bb.minX,   bb.maxZ,   bb.maxY); // U
        case 2: return new AxisAlignedBB(1-bb.maxX,   bb.minY, 1-bb.maxZ, 1-bb.minX,   bb.maxY, 1-bb.minZ); // N
        case 3: return new AxisAlignedBB(  bb.minX,   bb.minY,   bb.minZ,   bb.maxX,   bb.maxY,   bb.maxZ); // S --> bb
        case 4: return new AxisAlignedBB(1-bb.maxZ,   bb.minY,   bb.minX, 1-bb.minZ,   bb.maxY,   bb.maxX); // W
        case 5: return new AxisAlignedBB(  bb.minZ,   bb.minY, 1-bb.maxX,   bb.maxZ,   bb.maxY, 1-bb.minX); // E
      }
    } else {
      switch(new_facing.getIndex()) {
        case 0: return new AxisAlignedBB(  bb.minX, bb.minY,   bb.minZ,   bb.maxX, bb.maxY,   bb.maxZ); // D --> bb
        case 1: return new AxisAlignedBB(  bb.minX, bb.minY,   bb.minZ,   bb.maxX, bb.maxY,   bb.maxZ); // U --> bb
        case 2: return new AxisAlignedBB(  bb.minX, bb.minY,   bb.minZ,   bb.maxX, bb.maxY,   bb.maxZ); // N --> bb
        case 3: return new AxisAlignedBB(1-bb.maxX, bb.minY, 1-bb.maxZ, 1-bb.minX, bb.maxY, 1-bb.minZ); // S
        case 4: return new AxisAlignedBB(  bb.minZ, bb.minY, 1-bb.maxX,   bb.maxZ, bb.maxY, 1-bb.minX); // W
        case 5: return new AxisAlignedBB(1-bb.maxZ, bb.minY,   bb.minX, 1-bb.minZ, bb.maxY,   bb.maxX); // E
      }
    }
    return bb;
  }

  // -------------------------------------------------------------------------------------------------------------------
  // JAR resource related
  // -------------------------------------------------------------------------------------------------------------------

  public static String loadResourceText(String path)
  {
    try {
      InputStream is = ModAuxiliaries.class.getResourceAsStream(path);
      if(is==null) return "";
      BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
      return br.lines().collect(Collectors.joining("\n"));
    } catch(Throwable e) {
      return "";
    }
  }

  public static void logGitVersion(String mod_name)
  {
    try {
      // Done during construction to have an exact version in case of a crash while registering.
      String version = ModAuxiliaries.loadResourceText("/.gitversion-" + MODID).trim();
      logInfo(mod_name+((version.isEmpty())?(" (dev build)"):(" GIT id #"+version)) + ".");
    } catch(Throwable e) {
      // (void)e; well, then not. Priority is not to get unneeded crashes because of version logging.
    }
  }

  // -------------------------------------------------------------------------------------------------------------------
  // Dyes
  // -------------------------------------------------------------------------------------------------------------------

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
