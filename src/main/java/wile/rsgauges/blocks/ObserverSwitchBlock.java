/*
 * @file ObserverSwitchBlock.java
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

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import wile.rsgauges.ModContent;
import wile.rsgauges.detail.BlockCategories;
import wile.rsgauges.detail.ModResources;
import wile.rsgauges.libmc.detail.Auxiliaries;
import wile.rsgauges.libmc.detail.Overlay;

import javax.annotation.Nullable;
import java.util.ArrayList;


public class ObserverSwitchBlock extends SwitchBlock
{
  public ObserverSwitchBlock(long config, BlockBehaviour.Properties properties, AABB unrotatedBBUnpowered, @Nullable AABB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound); }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos pos, BlockPos facingPos)
  {
    if(!isAffectedByNeigbour(state, world, pos, facingPos)) return state;
    final ObserverSwitchTileEntity te = getTe(world, pos);
    if(te != null) te.observe();
    return state;
  }

  @Override
  public ObserverSwitchTileEntity getTe(LevelReader world, BlockPos pos)
  {
    BlockEntity te = world.getBlockEntity(pos);
    if((!(te instanceof ObserverSwitchTileEntity))) return null;
    return (ObserverSwitchTileEntity)te;
  }

  @Override
  @Nullable
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
  { return new ObserverSwitchTileEntity(pos, state); }

  // -------------------------------------------------------------------------------------------------------------------
  // Tile entity
  // -------------------------------------------------------------------------------------------------------------------

  public static class ObserverSwitchTileEntity extends SwitchTileEntity
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

    public ObserverSwitchTileEntity(BlockEntityType<?> te_type, BlockPos pos, BlockState state)
    { super(te_type, pos, state); }

    public ObserverSwitchTileEntity(BlockPos pos, BlockState state)
    { super(ModContent.TET_OBSERVER_SWITCH, pos, state); }

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
    { debounce_ = (i<=0) ? 0 : (Math.min(i, debounce_max)); }

    void range(int i)
    { range_ = (i<=0) ? 0 : (Math.min(i, range_max)); }

    void threshold(int i)
    { threshold_ = (i<=0) ? 0 : (Math.min(i, threshold_max)); }

    void filter(int f)
    { filter_index_ = (f>=BlockCategories.getMatcherNames().size()) ? (BlockCategories.getMatcherNames().size()-1) : (Math.max(f, 0)); }

    @Override
    public void write(CompoundTag nbt, boolean updatePacket)
    {
      super.write(nbt, updatePacket);
      nbt.putInt("range", range_);
      nbt.putInt("threshold", threshold_);
      nbt.putString("filter", filter_name());
      nbt.putInt("debounce", debounce_);
    }

    @Override
    public void read(CompoundTag nbt, boolean updatePacket)
    {
      super.read(nbt, updatePacket);
      range(nbt.getInt("range"));
      threshold(nbt.getInt("threshold"));
      filter_name(nbt.getString("filter"));
      debounce(nbt.getInt("debounce"));
    }

    @Override
    public void reset(@Nullable LevelReader world)
    { super.reset(world); filter_index_=0; range_=0; threshold_=1; debounce_=0; update_timer_=0; debounce_counter_=0; }

    @Override
    public boolean activation_config(BlockState state, @Nullable Player player, double x, double y, boolean show_only)
    {
      if(state == null) return false;
      final int direction = ((y >= 12) && (y <= 13)) ? (1) : (((y >= 9) && (y <= 10)) ? (-1) : (0));
      final int field = ((x>=1) && (x<=2)) ? (1) : ( ((x>=4) && (x<=5)) ? (2) : ( ((x>=7) && (x<=8)) ? (3) : ( ((x>=10) && (x<=11)) ? (4) : ( ((x>=13) && (x<=14)) ? (5) : (0) ))));
      if((direction==0) || (field==0)) return false;
      if(!show_only) {
        switch (field) {
          case 1 -> {
            range(range() + direction);
            if (threshold() > range()) {
              threshold(range());
            }
            break;
          }
          case 2 -> {
            threshold(threshold() + direction);
            break;
          }
          case 3 -> {
            debounce(debounce() + direction);
            break;
          }
          case 4 -> {
            setpower(setpower() + direction);
            break;
          }
          case 5 -> {
            filter(filter() + direction);
            break;
          }
        }
        if(threshold() < 1) threshold(1);
        if(setpower() < 1) setpower(1);
        update_timer_ = 0;
        setChanged();
      }
      {
        ArrayList<Object> tr = new ArrayList<>();
        MutableComponent separator = (Component.literal(" | ")); separator.withStyle(ChatFormatting.GRAY);
        tr.add(Auxiliaries.localizable("switchconfig.blocksensor.range", ChatFormatting.BLUE, new Object[]{range()}));
        tr.add(separator.copy().append(Auxiliaries.localizable("switchconfig.blocksensor.threshold", ChatFormatting.YELLOW, new Object[]{threshold()})));
        if(debounce()>0) {
          tr.add(separator.copy().append(Auxiliaries.localizable("switchconfig.lightsensor.debounce", ChatFormatting.AQUA, new Object[]{debounce()})));
        } else {
          tr.add(Component.literal(""));
        }
        tr.add(separator.copy().append(Auxiliaries.localizable("switchconfig.blocksensor.output_power", ChatFormatting.RED, new Object[]{setpower()})));
        tr.add(separator.copy().append(Auxiliaries.localizable("switchconfig.blocksensor.filter",
          ChatFormatting.DARK_GREEN,
          new Object[]{Component.translatable("rsgauges.switchconfig.blocksensor.filter."+filter_name())})
        ));
        Overlay.show(player, Auxiliaries.localizable("switchconfig.blocksensor", ChatFormatting.RESET, tr.toArray()));
      }
      return (!show_only);
    }

    public void observe()
    { if(update_timer_ > 2) update_timer_ = 0; } // cooldown time to prevent state jitters

    @Override
    @SuppressWarnings("deprecation")
    public void tick()
    {
      if(level.isClientSide() || (--update_timer_ > 0)) return;
      update_timer_ = ((range_ <= 1) ? 20 : 10) + ((int)(Math.random()*3)); // Neighbours are fast updated using neighbourChanged notifications.
      final BlockState state = level.getBlockState(worldPosition);
      if((state==null) || (!(state.getBlock() instanceof final ObserverSwitchBlock block))) return;
      final Direction obervationDirection = state.getValue(FACING);
      int n_matched = 0;
      final int rng =  (range_ < 2) ? 1 : range_;
      final int tr = Math.min(threshold_, rng);
      String fname = filter_name();
      final BlockCategories.Matcher matcher = BlockCategories.getMatcher(fname);
      for(int n=1; n<=rng; ++n) {
        final BlockPos p = worldPosition.relative(obervationDirection, n);
        if(!level.isAreaLoaded(p,1)) continue;
        if(!matcher.match(level, p)) continue;
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
      if(state.getValue(POWERED) != active) block.onSwitchActivated(level, worldPosition, state, null, null);
    }
  }

}
