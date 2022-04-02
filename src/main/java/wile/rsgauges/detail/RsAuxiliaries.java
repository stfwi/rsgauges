
/*
 * @file RsAuxiliaries.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2020 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Mod specific utility.
 */
package wile.rsgauges.detail;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;

public class RsAuxiliaries
{
  // -------------------------------------------------------------------------------------------------------------------
  // Coorsys/AABB transformations,
  // -------------------------------------------------------------------------------------------------------------------

  /**
   * Rotates an aabb from default facing EASE (direction x+) to another facing.
   */
  @SuppressWarnings("all") // suspicious parameter
  public static AABB transform_forward(final AABB bb, final Direction facing)
  {
    switch(facing.get3DDataValue()) {
      case 0: return new AABB(  bb.minY, -bb.minX,  bb.minZ,  bb.maxY, -bb.maxX,  bb.maxZ); // D
      case 1: return new AABB( -bb.minY,  bb.minX,  bb.minZ, -bb.maxY,  bb.maxX,  bb.maxZ); // U
      case 2: return new AABB(  bb.minZ,  bb.minY, -bb.minX,  bb.maxZ,  bb.maxY, -bb.maxX); // N
      case 3: return new AABB( -bb.minZ,  bb.minY,  bb.minX, -bb.maxZ,  bb.maxY,  bb.maxX); // S
      case 4: return new AABB( -bb.minX,  bb.minY, -bb.minZ, -bb.maxX,  bb.maxY, -bb.maxZ); // W
      case 5: return new AABB(  bb.minX,  bb.minY,  bb.minZ,  bb.maxX,  bb.maxY,  bb.maxZ); // E --> bb
      default: return new AABB(0,0,0, 0.1,0.1,0.1);
    }
  }

  /**
   * Transforms a block position, rotated around the world origin from EAST
   * to facing.
   */
  @SuppressWarnings("unused")
  public static BlockPos transform_forward(final BlockPos pos, final Direction facing)
  {
    return switch (facing.get3DDataValue()) {
      case 0 -> new BlockPos(pos.getY(), -pos.getX(), pos.getZ()); // D
      case 1 -> new BlockPos(-pos.getY(), pos.getX(), pos.getZ()); // U
      case 2 -> new BlockPos(pos.getZ(), pos.getY(), -pos.getX()); // N
      case 3 -> new BlockPos(-pos.getZ(), pos.getY(), pos.getX()); // S
      case 4 -> new BlockPos(-pos.getX(), pos.getY(), -pos.getZ()); // W
      case 5 -> new BlockPos(pos.getX(), pos.getY(), pos.getZ()); // E --> bb
      default -> pos;
    };
  }

  // -------------------------------------------------------------------------------------------------------------------

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
}