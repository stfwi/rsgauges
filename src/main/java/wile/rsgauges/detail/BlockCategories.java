/*
 * @file java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Block category wrapper matching.
 */
package wile.rsgauges.detail;

import net.minecraft.world.World;
import net.minecraft.block.*;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.IPlantable;
import wile.rsgauges.ModRsGauges;

import java.util.*;


public class BlockCategories
{
  private static final String MODID = ModRsGauges.MODID;

  public final static void update()
  {}

  public final static Matcher getMatcher(String name)
  { return matchers_.getOrDefault(name, filter_none); }

  public final static List<String> getMatcherNames()
  { return matcher_names_; }

  public interface Matcher { boolean match(World world, BlockPos pos); }
  private static final Matcher filter_none = (final World w, final BlockPos p) -> false;
  private static final Map<String, Matcher> matchers_;
  private static final List<String> matcher_names_;

  static
  {
    // --------------------------------------------------------------------------------
    matchers_ = new HashMap<String, Matcher>();

    matchers_.put("any", (final World w, final BlockPos p) -> {
      BlockState st = w.getBlockState(p);
      return !st.getBlock().isAir(st, w, p);
    });

    matchers_.put("solid", (final World w, final BlockPos p) -> {
      return w.getBlockState(p).getMaterial().isSolid();
    });

    matchers_.put("liquid", (final World w, final BlockPos p) -> {
      return (w.getBlockState(p).getMaterial().isLiquid()) || (!w.getFluidState(p).isEmpty());
    });

    matchers_.put("air", (final World w, final BlockPos p) -> {
      BlockState st = w.getBlockState(p);
      return st.getBlock().isAir(st, w, p);
    });

    matchers_.put("plant", (final World w, final BlockPos p) -> {
      Block b=w.getBlockState(p).getBlock();
      return (b instanceof IGrowable) || (b instanceof IPlantable) || b.getTags().contains(new ResourceLocation(MODID, "plants"));
    });

    matchers_.put("material_wood", (final World w, final BlockPos p) -> {
      return w.getBlockState(p).getBlock().getTags().contains(new ResourceLocation(MODID, "wooden"));
    });

    matchers_.put("material_stone", (final World w, final BlockPos p) -> {
      return w.getBlockState(p).getBlock().getTags().contains(new ResourceLocation(MODID, "stone_like"));
    });

    matchers_.put("material_glass", (final World w, final BlockPos p) -> {
      return w.getBlockState(p).getBlock().getTags().contains(new ResourceLocation(MODID, "glass_like"));
    });

    matchers_.put("material_clay", (final World w, final BlockPos p) -> {
      return w.getBlockState(p).getBlock().getTags().contains(new ResourceLocation(MODID, "clay_like"));
    });

    matchers_.put("material_water", (final World w, final BlockPos p) -> {
      BlockState st = w.getBlockState(p);
      Block b = st.getBlock();
      if(b.getTags().contains(new ResourceLocation(MODID, "water_like"))) return true;
      if(st.getFluidState().isEmpty()) return false;
      return (st.getFluidState().getFluid() == Fluids.WATER) || (st.getFluidState().getFluid() == Fluids.FLOWING_WATER);
    });

    matchers_.put("ore", (final World w, final BlockPos p) -> {
      return w.getBlockState(p).getBlock().getTags().contains(new ResourceLocation(MODID, "ores"));
    });

    matchers_.put("woodlog", (final World w, final BlockPos p) -> {
      return w.getBlockState(p).getBlock().getTags().contains(new ResourceLocation(MODID, "logs"));
    });

    matchers_.put("crop", (final World w, final BlockPos p) -> {
      Block b = w.getBlockState(p).getBlock();
      return (b instanceof CropsBlock) || b.getTags().contains(new ResourceLocation(MODID, "crops"));
    });

    matchers_.put("crop_mature", (final World w, final BlockPos p) -> {
      final BlockState s = w.getBlockState(p);
      final Block b = s.getBlock();
      return ((b instanceof CropsBlock) && ((CropsBlock)b).isMaxAge(s)) || (b==Blocks.MELON) || (b==Blocks.PUMPKIN);
    });

    matchers_.put("sapling", (final World w, final BlockPos p) -> {
      return w.getBlockState(p).getBlock().getTags().contains(new ResourceLocation(MODID, "saplings"));
    });

    matchers_.put("soil", (final World w, final BlockPos p) -> {
      return w.getBlockState(p).getBlock().getTags().contains(new ResourceLocation(MODID, "soils"));
    });

    matchers_.put("fertile", (final World w, final BlockPos p) -> {
      return w.getBlockState(p).getBlock().isFertile(w.getBlockState(p), w, p);
    });

    matchers_.put("planks", (final World w, final BlockPos p) -> {
      return w.getBlockState(p).getBlock().getTags().contains(new ResourceLocation(MODID, "planks"));
    });

    matchers_.put("slab", (final World w, final BlockPos p) -> {
      return w.getBlockState(p).getBlock().getTags().contains(new ResourceLocation(MODID, "slabs"));
    });

    // --------------------------------------------------------------------------------

    matcher_names_ = new ArrayList<String>(); // use case sorted list
    matcher_names_.add("any");
    matcher_names_.add("solid");
    matcher_names_.add("liquid");
    matcher_names_.add("air");
    matcher_names_.add("plant");
    matcher_names_.add("material_wood");
    matcher_names_.add("material_stone");
    matcher_names_.add("material_glass");
    matcher_names_.add("material_clay");
    matcher_names_.add("material_water");
    matcher_names_.add("ore");
    matcher_names_.add("woodlog");
    matcher_names_.add("crop");
    matcher_names_.add("crop_mature");
    matcher_names_.add("sapling");
    matcher_names_.add("soil");
    matcher_names_.add("fertile");
    matcher_names_.add("planks");
    matcher_names_.add("slab");
    matchers_.forEach((k,v)->{ if(!matcher_names_.contains(k)) matcher_names_.add(k);});
  }

}
