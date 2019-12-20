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

import net.minecraft.world.IWorld;
import wile.rsgauges.ModContent;
import wile.rsgauges.detail.BlockCategories;
import wile.rsgauges.detail.ModAuxiliaries;
import wile.rsgauges.detail.ModResources;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.block.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import javax.annotation.Nullable;
import java.util.ArrayList;


public class BlockObserverSwitch extends BlockSwitch
{
  public BlockObserverSwitch(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound); }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos)
  {
    if(!isAffectedByNeigbour(state, world, pos, facingPos)) return state;
    final TileEntityObserverSwitch te = getTe(world, pos);
    if(te != null) te.observe();
    return state;
  }

  @Override
  public TileEntityObserverSwitch getTe(IWorldReader world, BlockPos pos)
  {
    TileEntity te = world.getTileEntity(pos);
    if((!(te instanceof TileEntityObserverSwitch))) return null;
    return (TileEntityObserverSwitch)te;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world)
  { return new TileEntityObserverSwitch(ModContent.TET_OBSERVER_SWITCH); }

  // -------------------------------------------------------------------------------------------------------------------
  // Tile entity
  // -------------------------------------------------------------------------------------------------------------------

  public static class TileEntityObserverSwitch extends TileEntitySwitch implements ITickableTileEntity
  {
    public final int debounce_max = 10;
    public final int range_max = 8;
    public final int threshold_max = range_max;

    private int debounce_ = 0;
    private int range_ = 0;
    private int threshold_ = 1;
    private int filter_index_ = 0;

    private int update_timer_ = 0;
    private int debounce_counter_ = 0;

    public TileEntityObserverSwitch(TileEntityType<?> te_type)
    { super(te_type); }

    public TileEntityObserverSwitch()
    { super(ModContent.TET_OBSERVER_SWITCH); }

    int debounce()
    { return debounce_; }

    int range()
    { return range_; }

    int threshold()
    { return threshold_; }

    int filter()
    { return filter_index_; }

    String filter_name()
    { return BlockCategories.getMatcherNames().get((filter_index_<0) ? 0 : ((filter_index_>=BlockCategories.getMatcherNames().size()) ? (BlockCategories.getMatcherNames().size()-1) : filter_index_)); }

    void filter_name(String name)
    {
      int i=-1, fi=0; // read javadoc, is there maybe something like an ordered map<string,filter> in java?
      for(String s:BlockCategories.getMatcherNames()) { ++i; if(s.equals(name)) { fi=i; break; } }
      filter_index_ = fi;
    }

    void debounce(int i)
    { debounce_ = (i<=0) ? 0 : ((i>debounce_max) ? debounce_max : i); }

    void range(int i)
    { range_ = (i<=0) ? 0 : ((i>range_max) ? range_max : i); }

    void threshold(int i)
    { threshold_ = (i<=0) ? 0 : ((i>threshold_max) ? threshold_max : i); }

    void filter(int f)
    { filter_index_ = (f>=BlockCategories.getMatcherNames().size()) ? (BlockCategories.getMatcherNames().size()-1) : ((f<0) ? 0 : f); }

    @Override
    public void writeNbt(CompoundNBT nbt, boolean updatePacket)
    {
      super.writeNbt(nbt, updatePacket);
      nbt.putInt("range", range_);
      nbt.putInt("threshold", threshold_);
      nbt.putString("filter", filter_name());
      nbt.putInt("debounce", debounce_);
    }

    @Override
    public void readNbt(CompoundNBT nbt, boolean updatePacket)
    {
      super.readNbt(nbt, updatePacket);
      range(nbt.getInt("range"));
      threshold(nbt.getInt("threshold"));
      filter_name(nbt.getString("filter"));
      debounce(nbt.getInt("debounce"));
    }

    @Override
    public void reset()
    { super.reset(); filter_index_=0; range_=0; threshold_=1; debounce_=0; update_timer_=0; debounce_counter_=0; }

    @Override
    public boolean activation_config(BlockState state, @Nullable PlayerEntity player, double x, double y)
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
        case 2: { threshold(threshold()+direction); break; }
        case 3: { debounce(debounce()+direction); break; }
        case 4: { on_power(on_power() + direction); break; }
        case 5: { filter(filter()+direction); break; }
      }
      if(threshold() < 1) threshold(1);
      if(on_power() < 1) on_power(1);
      {
        ArrayList<Object> tr = new ArrayList<>();
        StringTextComponent separator = (new StringTextComponent(" | ")); separator.getStyle().setColor(TextFormatting.GRAY);
        tr.add(ModAuxiliaries.localizable("switchconfig.blocksensor.range", TextFormatting.BLUE, new Object[]{range()}));
        tr.add(separator.deepCopy().appendSibling(ModAuxiliaries.localizable("switchconfig.blocksensor.threshold", TextFormatting.YELLOW, new Object[]{threshold()})));
        if(debounce()>0) {
          tr.add(separator.deepCopy().appendSibling(ModAuxiliaries.localizable("switchconfig.lightsensor.debounce", TextFormatting.AQUA, new Object[]{debounce()})));
        } else {
          tr.add(new StringTextComponent(""));
        }
        tr.add(separator.deepCopy().appendSibling(ModAuxiliaries.localizable("switchconfig.blocksensor.output_power", TextFormatting.RED, new Object[]{on_power()})));
        tr.add(separator.deepCopy().appendSibling(ModAuxiliaries.localizable("switchconfig.blocksensor.filter",
          TextFormatting.DARK_GREEN,
          new Object[]{new TranslationTextComponent("rsgauges.switchconfig.blocksensor.filter."+filter_name())})
        ));
        ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchconfig.blocksensor", TextFormatting.RESET, tr.toArray()));
      }
      update_timer_ = 0;
      markDirty();
      return true;
    }

    public void observe()
    { if(update_timer_ > 2) update_timer_ = 0; } // cooldown time to prevent state jitters

    @Override
    public void tick()
    {
      if(world.isRemote || (--update_timer_ > 0)) return;
      update_timer_ = ((range_ <= 1) ? 20 : 10) + ((int)(Math.random()*3)); // Neighbours are fast updated using neighbourChanged notifications.
      final BlockState state = world.getBlockState(pos);
      if((state==null) || (!(state.getBlock() instanceof BlockObserverSwitch))) return;
      final BlockObserverSwitch block = (BlockObserverSwitch) state.getBlock();
      final Direction obervationDirection = state.get(FACING);
      int n_matched = 0;
      final int rng =  (range_ < 2) ? 1 : range_;
      final int tr = (threshold_ > rng) ? rng : threshold_;
      String fname = filter_name();
      final BlockCategories.Matcher matcher = BlockCategories.getMatcher(fname);
      for(int n=1; n<=rng; ++n) {
        final BlockPos p = pos.offset(obervationDirection, n);
        if(!world.isAreaLoaded(p,1)) continue;
        if(!matcher.match(world, p)) continue;
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
      if(state.get(POWERED) != active) block.onSwitchActivated(world, pos, state, null, null);
    }
  }

}
