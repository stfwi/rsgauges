/*
 * @file IntervalTimerSwitchBlock.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Autoswitch, specialised for fast sampling rate interval timing.
 */
package wile.rsgauges.blocks;


import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import wile.rsgauges.ModContent;
import wile.rsgauges.detail.ModResources;
import wile.rsgauges.detail.RsAuxiliaries;
import wile.rsgauges.libmc.detail.Auxiliaries;
import wile.rsgauges.libmc.detail.Overlay;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Optional;


public class IntervalTimerSwitchBlock extends AutoSwitchBlock
{
  public IntervalTimerSwitchBlock(long config, BlockBehaviour.Properties properties, AABB unrotatedBBUnpowered, @Nullable AABB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound); }

  public IntervalTimerSwitchBlock(long config, BlockBehaviour.Properties properties, AABB unrotatedBBUnpowered, @Nullable AABB unrotatedBBPowered)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, null, null); }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  @Nullable
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
  { return new IntervalTimerSwitchTileEntity(pos, state); }

  @Override
  public Optional<Integer> switchLinkOutputPower(Level world, BlockPos pos)
  {
    BlockState state = world.getBlockState(pos);
    if(!(state.getBlock() instanceof IntervalTimerSwitchBlock)) return Optional.empty();
    return Optional.of(state.getValue(POWERED) ? 15 : 0);
  }

  // -------------------------------------------------------------------------------------------------------------------
  // Tile entity
  // -------------------------------------------------------------------------------------------------------------------

  /**
   * Tile entity for timer interval based switches
   */
  public static class IntervalTimerSwitchTileEntity extends AutoSwitchTileEntity
  {
    private static final int ramp_max = 5;
    private static final int t_max = 20 * 60 * 10; // 10min @20clk/s
    private static final int t_min =  5;           // 0.25s @20clk/s

    public IntervalTimerSwitchTileEntity(BlockEntityType<?> te_type, BlockPos pos, BlockState state)
    { super(te_type, pos, state); }

    public IntervalTimerSwitchTileEntity(BlockPos pos, BlockState state)
    { super(ModContent.TET_TIMER_SWITCH, pos, state); }

    private int p_set_  = 15;
    private int t_on_  = 20;
    private int t_off_ = 20;
    private int ramp_ = 0;
    private int update_timer_ = 0;
    private int p_ = 0;
    private boolean s_ = false;

    public void restart()
    { update_timer_=0; p_=0; s_=false; }

    public int p_set()
    { return p_set_; }

    public int t_on()
    { return t_on_; }

    public int t_off()
    { return t_off_; }

    public int ramp()
    { return ramp_; }

    public void p_set(int v)
    { p_set_ = (v<1) ? (1) : (Math.min(v, 15)); }

    public void t_on(int v)
    { t_on_ = (v<0) ? (0) : (Math.min(v, t_max)); }

    public void t_off(int v)
    { t_off_ = (v<0) ? (0) : (Math.min(v, t_max)); }

    public void ramp(int v)
    { ramp_ = (v<0) ? (0) : (Math.min(v, ramp_max)); }

    @Override
    public void write(CompoundTag nbt, boolean updatePacket)
    {
      super.write(nbt, updatePacket);
      nbt.putInt("pset", p_set());
      nbt.putInt("toff", t_off());
      nbt.putInt("ton", t_on());
      nbt.putInt("ramp", ramp());
    }

    @Override
    public void read(CompoundTag nbt, boolean updatePacket)
    {
      super.read(nbt, updatePacket);
      p_set(nbt.getInt("pset"));
      t_off(nbt.getInt("toff"));
      t_on(nbt.getInt("ton"));
      ramp(nbt.getInt("ramp"));
    }

    private int next_higher_interval_setting(int ticks)
    {
      if     (ticks <  100) ticks +=   5; //  5s   ->  0.25s steps
      else if(ticks <  200) ticks +=  10; // 10s   ->  0.5s steps
      else if(ticks <  400) ticks +=  20; // 20s   ->  1.0s steps
      else if(ticks <  600) ticks +=  40; // 30s   ->  2.0s steps
      else if(ticks <  800) ticks += 100; // 40s   ->  5.0s steps
      else if(ticks < 2400) ticks += 200; //  2min -> 10.0s steps
      else                  ticks += 600; //  5min -> 30.0s steps
      return Math.min(ticks, t_max);
    }

    private int next_lower_interval_setting(int ticks)
    {
      if     (ticks <  100) ticks -=   5; //  5s   ->  0.25s steps
      else if(ticks <  200) ticks -=  10; // 10s   ->  0.5s steps
      else if(ticks <  400) ticks -=  20; // 20s   ->  1.0s steps
      else if(ticks <  600) ticks -=  40; // 30s   ->  2.0s steps
      else if(ticks <  800) ticks -= 100; // 40s   ->  5.0s steps
      else if(ticks < 2400) ticks -= 200; //  2min -> 10.0s steps
      else                  ticks -= 600; //  5min -> 30.0s steps
      return Math.max(ticks, t_min);
    }

    @Override
    public boolean activation_config(BlockState state, @Nullable Player player, double x, double y, boolean show_only)
    {
      if((state == null) || (!(state.getBlock() instanceof SwitchBlock))) return false;
      final int direction = (y >= 13) ? (1) : ((y <= 2) ? (-1) : (0));
      final int field = ((x>=2) && (x<=3.95)) ? (1) : (
        ((x>=4.25) && (x<=7)) ? (2) : (
          ((x>=8) && (x<=10)) ? (3) : (
            ((x>=11) && (x<=13)) ? (4) : (0)
          )));
      final boolean selected = ((direction!=0) && (field!=0));
      if(selected && (!show_only)) {
        switch (field) {
          case 1 -> t_on((direction > 0) ? next_higher_interval_setting(t_on()) : next_lower_interval_setting(t_on()));
          case 2 -> t_off((direction > 0) ? next_higher_interval_setting(t_off()) : next_lower_interval_setting(t_off()));
          case 3 -> ramp(ramp() + direction);
          case 4 -> p_set(((p_set() <= 0) ? 15 : p_set()) + direction);
        }
        setChanged();
      }
      {
        boolean switch_state = state.getValue(POWERED);
        if(!selected) switch_state = !switch_state; // will be switched in turn.
        updateSwitchState(state, (AutoSwitchBlock) state.getBlock(), switch_state, 0);
        {
          MutableComponent separator = (Component.literal(" | ")); separator.withStyle(ChatFormatting.GRAY);
          ArrayList<Object> tr = new ArrayList<>();
          tr.add(Auxiliaries.localizable("switchconfig.intervaltimer.t_on", ChatFormatting.BLUE, new Object[]{RsAuxiliaries.ticksToSecondsString(t_on())}));
          tr.add(separator.copy().append(Auxiliaries.localizable("switchconfig.intervaltimer.t_off", ChatFormatting.YELLOW, new Object[]{RsAuxiliaries.ticksToSecondsString(t_off())})));
          tr.add(separator.copy().append(Auxiliaries.localizable("switchconfig.intervaltimer.output_power", ChatFormatting.RED, new Object[]{p_set()})));
          if(ramp()>0) tr.add(separator.copy().append(Auxiliaries.localizable("switchconfig.intervaltimer.ramp", ChatFormatting.DARK_GREEN, new Object[]{ramp()})));
          if(!switch_state) tr.add(separator.copy().append(Auxiliaries.localizable("switchconfig.intervaltimer.standby", ChatFormatting.AQUA)));
          while(tr.size() < 5) tr.add(Component.literal("")); // const lang file formatting arg count.
          Overlay.show(player, Auxiliaries.localizable("switchconfig.intervaltimer", ChatFormatting.RESET, tr.toArray()));
        }
      }
      return selected; // false: Switches output on/off (blockstate) in caller
    }

    @Override
    public int power(BlockState state, boolean strong)
    { return (nooutput() || (!state.getValue(POWERED)) || ((strong && weak())) ? (0) : setpower()); }

    @Override
    public void tick()
    {
      if((!hasLevel()) || (getLevel().isClientSide()) || (--update_timer_ > 0)) return;
      int p = p_;
      if((t_on()<=0) || (t_off()<=0) || (p_set() <= 0)) {
        p_ = 0;
        update_timer_ = 20;
      } else if(!s_) {
        // switching on
        update_timer_ = t_on();
        if((ramp() <= 0) || ((p_+=ramp()) >= p_set())) {
          p_ = p_set();
          s_ = true;
        } else {
          update_timer_ = 5; // ramping @ 0.25s
        }
      } else {
        // switching off
        update_timer_ = t_off();
        if((ramp() <= 0) || ((p_-=ramp()) <= 0)) {
          p_ = 0;
          s_ = false;
        } else {
          update_timer_ = 5; // ramping @ 0.25s
        }
      }
      if(p != p_) {
        setpower((inverted() ? (15-p_) : (p_)));
        BlockState state = getLevel().getBlockState(getBlockPos());
        if((state==null) || (!(state.getBlock() instanceof AutoSwitchBlock)) || (!state.getValue(POWERED))) {
          update_timer_ = 200 + ((int)(Math.random() * 10));
          setpower(inverted() ? (15) : (0));
        }
        level.updateNeighborsAt(worldPosition, state.getBlock());
        level.updateNeighborsAt(worldPosition.relative(state.getValue(FACING).getOpposite()), state.getBlock());
      }
    }
  }

}
