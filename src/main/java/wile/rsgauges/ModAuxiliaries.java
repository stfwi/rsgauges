/*
 * @file ModAuxiliaries.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * General commonly used functionality.
 */
package wile.rsgauges;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import wile.rsgauges.network.Networking;
import javax.annotation.Nullable;

public class ModAuxiliaries
{
  /**
   * Text localisation wrapper, implicitly prepends `ModRsGauges.MODID` to the
   * translation keys.
   */
  public static TextComponentTranslation localizable(String modtrkey, @Nullable TextFormatting color, Object... args)
  {
    TextComponentTranslation tr = new TextComponentTranslation(ModRsGauges.MODID+"."+modtrkey, args);
    if(color!=null) tr.getStyle().setColor(color);
    return tr;
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
    public static final int[] byIndex = { WHITE,ORANGE,MAGENTA,LIGHTBLUE,YELLOW,LIME,PINK,GRAY,SILVER,CYAN,PURPLE,BLUE,BROWN,GREEN,RED,BLACK };
    public static final String[] nameByIndex = { "white","orange","magenta","lightblue","yellow","lime","pink","gray","silver","cyan","purple","blue","brown","green","red","black" };
  }

  public static class RsMaterials
  {
    public static final Material MATERIAL_TRAPDOORSWITCH = new MetalMaterial();
    public static final Material MATERIAL_PLANT = new PlantMaterial();

    public static class MetalMaterial extends Material {
      public MetalMaterial() { super(MapColor.IRON); }
      @Override public boolean isLiquid() { return false; }
      @Override public boolean blocksLight() { return false; }
      @Override public boolean blocksMovement() { return true; }
      @Override public boolean isToolNotRequired()
      {
        return true;
      }
    }

    public static class PlantMaterial extends Material {
      public PlantMaterial() { super(MapColor.GRASS); }
      @Override public boolean isLiquid() { return false; }
      @Override public boolean blocksLight() { return false; }
      @Override public boolean blocksMovement() { return false; }
      @Override public boolean isToolNotRequired()
      {
        return true;
      }
    }

  }

}
