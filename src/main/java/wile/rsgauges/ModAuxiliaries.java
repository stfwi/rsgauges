/**
 * @file ModAuxiliaries.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * General commonly used functionality.
**/
package wile.rsgauges;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

public class ModAuxiliaries {

  public static String localize(String txt) { return I18n.translateToLocal(txt); }

  public static void playerMessage(EntityPlayer player, final String message) {
    String s = localize(message.trim()).trim();
    if(!s.isEmpty()) player.sendMessage(new TextComponentTranslation(s));
  }

  /**
   * Rotates an aabb from default facing EASE (direction x+) to another facing.
   */
  public static AxisAlignedBB transform_forward(final AxisAlignedBB bb, final EnumFacing facing) {
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
  public static BlockPos transform_forward(final BlockPos pos, final EnumFacing facing) {
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
   * Returns the localised message for a given fixed English message.
   */
  public static String localized(String message) { return message; } // later.

  /**
   * Returns a time string in 24:00 hour format.
   */
  public static String daytimeToString(long t) {
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
  public static String ticksToSecondsString(long t) {
    return String.format("%.02f", ((double)t)/20.0);
  }

}
