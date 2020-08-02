/*
 * @file BlockObserverSwitch.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Basic class for blocks representing switches, which change
 * their output state depending on the presence of blocks (or
 * block categories) on front of them. The output is not a
 * short pulse, as e.g. a vanilla observer emits, but a
 * continuous signal as long as the monitored blocks meet the
 * matching rules of the switch.
 */
package wile.rsgauges.blocks;

import wile.rsgauges.detail.ModAuxiliaries;
import wile.rsgauges.detail.ModResources;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockObserverSwitch extends BlockSwitch
{
  public BlockObserverSwitch(String registryName, AxisAlignedBB unrotatedBBUnpowered, AxisAlignedBB unrotatedBBPowered, long config, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound, @Nullable Material material)
  { super(registryName, unrotatedBBUnpowered, unrotatedBBPowered, config, powerOnSound, powerOffSound, material); }

  public BlockObserverSwitch(String registryName, AxisAlignedBB unrotatedBBUnpowered, AxisAlignedBB unrotatedBBPowered, long config, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(registryName, unrotatedBBUnpowered, unrotatedBBPowered, config, powerOnSound, powerOffSound, null); }

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
  {}

  @Override
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighbourPos) {
    if(!neighborChangedCheck(state, world, pos, neighborBlock, neighbourPos)) return;
    final TileEntityObserverSwitch te = getTe(world, pos);
    if(te != null) te.observe();
  }

  @Override
  public TileEntityObserverSwitch getTe(IBlockAccess world, BlockPos pos)
  {
    TileEntity te = world.getTileEntity(pos);
    if((!(te instanceof TileEntityObserverSwitch))) return null;
    return (TileEntityObserverSwitch)te;
  }

  @Override
  public TileEntity createTileEntity(World world, IBlockState state)
  { return new TileEntityObserverSwitch(); }

  public static class TileEntityObserverSwitch extends TileEntitySwitch implements ITickable
  {
    private static class Filter {
      public final String name;
      public final Predicate<BlockPos> pred;
      Filter(String name, Predicate<BlockPos> pred) { this.name=name; this.pred=pred; }
    }
    private final List<Filter> filters_ = Collections.unmodifiableList(new ArrayList<Filter>() {{
      add(new Filter("any",               (p) -> { IBlockState st=getWorld().getBlockState(p); return !st.getBlock().isAir(st, getWorld(), p); }));
      add(new Filter("solid",             (p) -> getWorld().getBlockState(p).getMaterial().isSolid()));
      add(new Filter("liquid",            (p) -> getWorld().getBlockState(p).getMaterial().isLiquid()));
      add(new Filter("air",               (p) -> { IBlockState st=getWorld().getBlockState(p); return st.getBlock().isAir(st, getWorld(), p); }));
      add(new Filter("plant",             (p) -> { Block b=getWorld().getBlockState(p).getBlock(); return (b instanceof IGrowable) || (b instanceof IPlantable) || ModAuxiliaries.BlockCategories.match_block_categories(b, ModAuxiliaries.BlockCategories.BLOCKCAT_PLANT); }));
      add(new Filter("material_wood",     (p) -> ModAuxiliaries.BlockCategories.match_block_categories(getWorld().getBlockState(p).getBlock(), ModAuxiliaries.BlockCategories.BLOCKCAT_MATERIAL_WOOD|ModAuxiliaries.BlockCategories.BLOCKCAT_SAPLING)));
      add(new Filter("material_stone",    (p) -> ModAuxiliaries.BlockCategories.match_block_categories(getWorld().getBlockState(p).getBlock(), ModAuxiliaries.BlockCategories.BLOCKCAT_MATERIAL_STONE|ModAuxiliaries.BlockCategories.BLOCKCAT_MATERIAL_QUARZ|ModAuxiliaries.BlockCategories.BLOCKCAT_MATERIAL_CLAY|ModAuxiliaries.BlockCategories.BLOCKCAT_MATERIAL_OBSIDIAN)));
      add(new Filter("material_glass",    (p) -> ModAuxiliaries.BlockCategories.match_block_categories(getWorld().getBlockState(p).getBlock(), ModAuxiliaries.BlockCategories.BLOCKCAT_MATERIAL_GLASS|ModAuxiliaries.BlockCategories.BLOCKCAT_MATERIAL_OBSIDIAN)));
      add(new Filter("material_clay",     (p) -> ModAuxiliaries.BlockCategories.match_block_categories(getWorld().getBlockState(p).getBlock(), ModAuxiliaries.BlockCategories.BLOCKCAT_MATERIAL_CLAY)));
      add(new Filter("material_water",    (p) -> { final IBlockState st=getWorld().getBlockState(p); final Block b=st.getBlock(); return (ModAuxiliaries.BlockCategories.match_block_categories(b, ModAuxiliaries.BlockCategories.BLOCKCAT_MATERIAL_WATER)) || ((b instanceof BlockSponge) && st.getValue(BlockSponge.WET)); })); // if you can read this, that's what I call inline ;)
      add(new Filter("ore",               (p) -> ModAuxiliaries.BlockCategories.match_block_categories(getWorld().getBlockState(p).getBlock(), ModAuxiliaries.BlockCategories.BLOCKCAT_ORE)));
      add(new Filter("woodlog",           (p) -> getWorld().getBlockState(p).getBlock().isWood(getWorld(), p)));
      add(new Filter("crop",              (p) -> { Block block = getWorld().getBlockState(p).getBlock(); return (block instanceof BlockCrops);}));
      add(new Filter("crop_mature",       (p) -> { IBlockState s=getWorld().getBlockState(p); Block b=s.getBlock(); return ((b instanceof BlockCrops) && ((BlockCrops)b).isMaxAge(s)) || (b==Blocks.MELON_BLOCK) || (b==Blocks.PUMPKIN); }));
      add(new Filter("sapling",           (p) -> ModAuxiliaries.BlockCategories.match_block_categories(getWorld().getBlockState(p).getBlock(), ModAuxiliaries.BlockCategories.BLOCKCAT_SAPLING)));
      add(new Filter("soil",              (p) -> ModAuxiliaries.BlockCategories.match_block_categories(getWorld().getBlockState(p).getBlock(), ModAuxiliaries.BlockCategories.BLOCKCAT_SOIL)));
      add(new Filter("fertile",           (p) -> getWorld().getBlockState(p).getBlock().isFertile(getWorld(), p)));
      add(new Filter("planks",            (p) -> ModAuxiliaries.BlockCategories.match_block_categories(getWorld().getBlockState(p).getBlock(), ModAuxiliaries.BlockCategories.BLOCKCAT_PLANK)));
      add(new Filter("slab",              (p) -> ModAuxiliaries.BlockCategories.match_block_categories(getWorld().getBlockState(p).getBlock(), ModAuxiliaries.BlockCategories.BLOCKCAT_SLAB)));
      // too vague: add(new Filter("firesource", (p) -> getWorld().getBlockState(p).getBlock().isFireSource(getWorld(), p, EnumFacing.UP)));
    }});

    public final int debounce_max = 10;
    public final int range_max = 8;
    public final int threshold_max = range_max;

    private int debounce_ = 0;
    private int range_ = 0;
    private int threshold_ = 1;
    private int filter_index_ = 0;

    private int update_timer_ = 0;
    private int debounce_counter_ = 0;

    int debounce()
    { return debounce_; }

    int range()
    { return range_; }

    int threshold()
    { return threshold_; }

    int filter()
    { return filter_index_; }

    String filter_name()
    { return filters_.get((filter_index_<0) ? 0 : ((filter_index_>=filters_.size()) ? (filters_.size()-1) : filter_index_)).name; }

    void filter_name(String name)
    {
      int i=-1, fi=0; // read javadoc, is there maybe something like an ordered map<string,filter> in java?
      for(Filter f:filters_) { ++i; if(f.name.equals(name)) { fi=i; break; } }
      filter_index_ = fi;
    }

    void debounce(int i)
    { debounce_ = (i<=0) ? 0 : ((i>debounce_max) ? debounce_max : i); }

    void range(int i)
    { range_ = (i<=0) ? 0 : ((i>range_max) ? range_max : i); }

    void threshold(int i)
    { threshold_ = (i<=0) ? 0 : ((i>threshold_max) ? threshold_max : i); }

    void filter(int f)
    { filter_index_ = (f>=filters_.size()) ? (filters_.size()-1) : ((f<0) ? 0 : f); }

    @Override
    public void writeNbt(NBTTagCompound nbt, boolean updatePacket)
    {
      super.writeNbt(nbt, updatePacket);
      nbt.setInteger("range", range_);
      nbt.setInteger("threshold", threshold_);
      nbt.setString("filter", filter_name());
      nbt.setInteger("debounce", debounce_);
    }

    @Override
    public void readNbt(NBTTagCompound nbt, boolean updatePacket)
    {
      super.readNbt(nbt, updatePacket);
      range(nbt.getInteger("range"));
      threshold(nbt.getInteger("threshold"));
      filter_name(nbt.getString("filter"));
      debounce(nbt.getInteger("debounce"));
    }

    @Override
    public void reset()
    { super.reset(); filter_index_=0; range_=0; threshold_=1; debounce_=0; update_timer_=0; debounce_counter_=0; }

    @Override
    public boolean activation_config(IBlockState state, @Nullable EntityPlayer player, double x, double y)
    {
      if(state == null) return false;
      final int direction = ((y >= 12) && (y <= 13)) ? (1) : (((y >= 9) && (y <= 10)) ? (-1) : (0));
      final int field =
        ((x>=1) && (x<=2)) ? (1) : (
          ((x>=4) && (x<=5)) ? (2) : (
            ((x>=7) && (x<=8)) ? (3) : (
              ((x>=10) && (x<=11)) ? (4) : (
                ((x>=13) && (x<=14)) ? (5) : (0)
          ))));
      if((direction==0) || (field==0)) return false;

      switch(field) {
        case 1: {
          range(range()+direction);
          if(threshold() > range()) threshold(range());
          break;
        }
        case 2: { threshold(threshold()+direction); if(threshold() > range()) threshold(range()); break; }
        case 3: { debounce(debounce()+direction); break; }
        case 4: { on_power(on_power() + direction); break; }
        case 5: { filter(filter()+direction); break; }
      }
      if(threshold() < 1) threshold(1);
      if(on_power() < 1) on_power(1);
      {
        ArrayList<Object> tr = new ArrayList<>();
        TextComponentString separator = (new TextComponentString(" | ")); separator.getStyle().setColor(TextFormatting.GRAY);
        tr.add(ModAuxiliaries.localizable("switchconfig.blocksensor.range", TextFormatting.BLUE, new Object[]{range()}));
        tr.add(separator.createCopy().appendSibling(ModAuxiliaries.localizable("switchconfig.blocksensor.threshold", TextFormatting.YELLOW, new Object[]{threshold()})));
        if(debounce()>0) {
          tr.add(separator.createCopy().appendSibling(ModAuxiliaries.localizable("switchconfig.lightsensor.debounce", TextFormatting.AQUA, new Object[]{debounce()})));
        } else {
          tr.add(new TextComponentString(""));
        }
        tr.add(separator.createCopy().appendSibling(ModAuxiliaries.localizable("switchconfig.blocksensor.output_power", TextFormatting.RED, new Object[]{on_power()})));
        tr.add(separator.createCopy().appendSibling(ModAuxiliaries.localizable("switchconfig.blocksensor.filter",
          TextFormatting.DARK_GREEN,
          new Object[]{new TextComponentTranslation("rsgauges.switchconfig.blocksensor.filter."+filter_name())})
        ));
        ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchconfig.blocksensor", TextFormatting.RESET, tr.toArray()));
      }
      update_timer_ = 0;
      markDirty();
      return true;
    }

    public void observe()
    { if(update_timer_ > 2) update_timer_ = 0; } // cooldown time to prevent state jitters

    public void update()
    {
      if(world.isRemote || (--update_timer_ > 0)) return;
      update_timer_ = ((range_ <= 1) ? 20 : 10) + ((int)(Math.random()*3)); // Neighbours are fast updated using neighbourChanged notifications.
      final IBlockState state = world.getBlockState(pos);
      if((state==null) || (!(state.getBlock() instanceof BlockObserverSwitch))) return;
      final BlockObserverSwitch block = (BlockObserverSwitch) state.getBlock();
      final EnumFacing obervationDirection = state.getValue(FACING);
      int n_matched = 0;
      final int rng =  (range_ < 2) ? 1 : range_;
      final int tr = (threshold_ > rng) ? rng : threshold_;
      final Predicate<BlockPos> pred = filters_.get(filter()).pred;
      for(int n=1; n<=rng; ++n) {
        final BlockPos p = pos.offset(obervationDirection, n);
        if((!world.isBlockLoaded(p)) || (!pred.apply(p))) continue;
        if(++n_matched >= tr) break;
      }
      boolean active = (n_matched >= threshold_);
      if(debounce_ > 0) { // simple inline debouncing
        if(active) {
          if(++debounce_counter_ < debounce_) return;
          debounce_counter_ = debounce_;
          active = true;
        } else {
          if(--debounce_counter_ > 0) return;
          debounce_counter_ = 0;
          active = false;
        }
      }
      if(state.getValue(POWERED) != active) block.onSwitchActivated(world, pos, state, null, null);
    }
  }

}
