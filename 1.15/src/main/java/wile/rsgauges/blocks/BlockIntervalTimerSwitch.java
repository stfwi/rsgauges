/*
 * @file BlockIntervalTimerSwitch.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Autoswitch, specialised for fast sampling rate interval timing.
 */
package wile.rsgauges.blocks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import wile.rsgauges.ModContent;
import wile.rsgauges.detail.ModAuxiliaries;
import wile.rsgauges.detail.ModConfig;
import wile.rsgauges.detail.ModResources;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.*;
import net.minecraft.world.IBlockReader;
import net.minecraft.tileentity.TileEntity;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class BlockIntervalTimerSwitch extends BlockAutoSwitch
{
  public BlockIntervalTimerSwitch(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound); }

  public BlockIntervalTimerSwitch(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, null, null); }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world)
  { return new TileEntityIntervalTimerSwitch(ModContent.TET_TIMER_SWITCH); }

  // -------------------------------------------------------------------------------------------------------------------
  // Tile entity
  // -------------------------------------------------------------------------------------------------------------------

  /**
   * Tile entity for timer interval based switches
   */
  public static class TileEntityIntervalTimerSwitch extends TileEntityAutoSwitch implements ITickableTileEntity
  {
    private static final int ramp_max = 5;
    private static final int t_max = 20 * 60 * 10; // 10min @20clk/s
    private static final int t_min =  5;           // 0.25s @20clk/s

    public TileEntityIntervalTimerSwitch(TileEntityType<?> te_type)
    { super(te_type); }

    public TileEntityIntervalTimerSwitch()
    { super(ModContent.TET_TIMER_SWITCH); }

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
    { p_set_ = (v<1) ? (1) : ((v>15) ? (15) : (v)); }

    public void t_on(int v)
    { t_on_ = (v<0) ? (0) : ((v>t_max) ? (t_max) : (v)); }

    public void t_off(int v)
    { t_off_ = (v<0) ? (0) : ((v>t_max) ? (t_max) : (v)); }

    public void ramp(int v)
    { ramp_ = (v<0) ? (0) : ((v>ramp_max) ? (ramp_max) : (v)); }

    @Override
    public void writeNbt(CompoundNBT nbt, boolean updatePacket)
    {
      super.writeNbt(nbt, updatePacket);
      nbt.putInt("pset", p_set());
      nbt.putInt("toff", t_off());
      nbt.putInt("ton", t_on());
      nbt.putInt("ramp", ramp());
    }

    @Override
    public void readNbt(CompoundNBT nbt, boolean updatePacket)
    {
      super.readNbt(nbt, updatePacket);
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
      return (ticks > t_max) ? (t_max) : (ticks);
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
      return (ticks < t_min) ? (t_min) : (ticks);
    }

    @Override
    public boolean activation_config(BlockState state, @Nullable PlayerEntity player, double x, double y)
    {
      if((state == null) || (!(state.getBlock() instanceof BlockSwitch))) return false;
      final int direction = (y >= 13) ? (1) : ((y <= 2) ? (-1) : (0));
      final int field = ((x>=2) && (x<=3.95)) ? (1) : (
        ((x>=4.25) && (x<=7)) ? (2) : (
          ((x>=8) && (x<=10)) ? (3) : (
            ((x>=11) && (x<=13)) ? (4) : (0)
          )));
      final boolean selected = ((direction!=0) && (field!=0));
      if(selected) {
        switch(field) {
          case 1: t_on( (direction > 0) ? next_higher_interval_setting(t_on()) : next_lower_interval_setting(t_on()) ); break;
          case 2: t_off( (direction > 0) ? next_higher_interval_setting(t_off()) : next_lower_interval_setting(t_off()) ); break;
          case 3: ramp(ramp()+direction); break;
          case 4: p_set( ((p_set()<=0) ? 15 : p_set()) + direction); break;
        }
        markDirty();
      }
      {
        boolean switch_state = state.get(POWERED);
        if(!selected) switch_state = !switch_state; // will be switched in turn.
        updateSwitchState(state, (BlockAutoSwitch) state.getBlock(), switch_state, 0);
        {
          StringTextComponent separator = (new StringTextComponent(" | ")); separator.getStyle().setColor(TextFormatting.GRAY);
          ArrayList<Object> tr = new ArrayList<>();
          tr.add(ModAuxiliaries.localizable("switchconfig.intervaltimer.t_on", TextFormatting.BLUE, new Object[]{ModAuxiliaries.ticksToSecondsString(t_on())}));
          tr.add(separator.deepCopy().appendSibling(ModAuxiliaries.localizable("switchconfig.intervaltimer.t_off", TextFormatting.YELLOW, new Object[]{ModAuxiliaries.ticksToSecondsString(t_off())})));
          tr.add(separator.deepCopy().appendSibling(ModAuxiliaries.localizable("switchconfig.intervaltimer.output_power", TextFormatting.RED, new Object[]{p_set()})));
          if(ramp()>0) tr.add(separator.deepCopy().appendSibling(ModAuxiliaries.localizable("switchconfig.intervaltimer.ramp", TextFormatting.DARK_GREEN, new Object[]{ramp()})));
          if(!switch_state) tr.add(separator.deepCopy().appendSibling(ModAuxiliaries.localizable("switchconfig.intervaltimer.standby", TextFormatting.AQUA)));
          while(tr.size() < 5) tr.add(new StringTextComponent("")); // const lang file formatting arg count.
          ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchconfig.intervaltimer", TextFormatting.RESET, tr.toArray()));
        }
      }
      return selected; // false: Switches output on/off (blockstate) in caller
    }

    @Override
    public int power(BlockState state, boolean strong)
    { return (nooutput() || (!state.get(POWERED)) || ((strong && weak())) ? (0) : on_power()); }

    @Override
    public void tick()
    {
      if(ModConfig.COMMON.without_timer_switch_update.get()) return;
      if((!hasWorld()) || (getWorld().isRemote) || (--update_timer_ > 0)) return;
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
        on_power((inverted() ? (15-p_) : (p_)));
        BlockState state = getWorld().getBlockState(getPos());
        if((state==null) || (!(state.getBlock() instanceof BlockAutoSwitch)) || (!state.get(POWERED))) {
          update_timer_ = 200 + ((int)(Math.random() * 10));
          on_power(inverted() ? (15) : (0));
        }
        world.notifyNeighborsOfStateChange(pos, state.getBlock());
        world.notifyNeighborsOfStateChange(pos.offset(state.get(FACING).getOpposite()), state.getBlock());
      }
    }
  }

}
