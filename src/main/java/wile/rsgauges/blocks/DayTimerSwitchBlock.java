/*
 * @file BlockDayTimerSwitch.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Auto switch specialised for day timer clocks.
 */
package wile.rsgauges.blocks;

import wile.rsgauges.ModContent;
import wile.rsgauges.ModConfig;
import wile.rsgauges.blocks.EnvironmentalSensorSwitchBlock.EnvironmentalSensorSwitchTileEntity;
import wile.rsgauges.detail.ModAuxiliaries;
import wile.rsgauges.detail.ModResources;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.*;
import net.minecraft.world.IBlockReader;
import net.minecraft.tileentity.TileEntity;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class DayTimerSwitchBlock extends AutoSwitchBlock
{
  public DayTimerSwitchBlock(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound); }

  public DayTimerSwitchBlock(long config, Block.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, null, null); }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world)
  { return new DayTimerSwitchTileEntity(ModContent.TET_DAYTIMER_SWITCH); }

  // -------------------------------------------------------------------------------------------------------------------
  // Tile entity
  // -------------------------------------------------------------------------------------------------------------------

  public static class DayTimerSwitchTileEntity extends EnvironmentalSensorSwitchTileEntity implements ITickableTileEntity
  {
    public DayTimerSwitchTileEntity(TileEntityType<?> te_type)
    { super(te_type); }

    public DayTimerSwitchTileEntity()
    { super(ModContent.TET_DAYTIMER_SWITCH); }

    @Override
    public boolean activation_config(BlockState state, @Nullable PlayerEntity player, double x, double y)
    {
      if(state == null) return false;
      final SwitchBlock block = (SwitchBlock)state.getBlock();
      final int direction = (y >= 13) ? (1) : ((y <= 2) ? (-1) : (0));
      final int field = ((x>=2) && (x<=3.95)) ? (1) : (
        ((x>=4.25) && (x<=7)) ? (2) : (
          ((x>=8) && (x<=10)) ? (3) : (
            ((x>=11) && (x<=13)) ? (4) : (0)
          )));
      if((direction==0) || (field==0)) return false;
      final double time_scaling = 15.0d * 500.0d / 24000.0d; // 1/2h
      switch(field) {
        case 1: {
          double v = threshold0_on()+(time_scaling*direction);
          if(v < 0) v += 15.0; else if(v > 15) v = 0;
          threshold0_on(v);
          break;
        }
        case 2: {
          double v = threshold0_off()+(time_scaling*direction);
          if(v < 0) v += 15.0; else if(v > 15) v = 0;
          threshold0_off(v);
          break;
        }
        case 3: { debounce(debounce()+direction); break; }
        case 4: { on_power(on_power() + direction); break; }
      }
      if(on_power() < 1) on_power(1);
      {
        // @TODO: day time localisation: how the hack that, transfer long timestamp with tagging and localise on client or system time class?
        StringTextComponent separator = (new StringTextComponent(" | ")); separator.func_240701_a_(TextFormatting.GRAY);
        ArrayList<Object> tr = new ArrayList<>();
        tr.add(ModAuxiliaries.localizable("switchconfig.daytimerclock.daytime_on", TextFormatting.BLUE, new Object[]{ModAuxiliaries.daytimeToString((long)(threshold0_on()*24000.0/15.0))}));
        tr.add(separator.func_230532_e_().func_230529_a_(ModAuxiliaries.localizable("switchconfig.daytimerclock.daytime_off", TextFormatting.YELLOW, new Object[]{ModAuxiliaries.daytimeToString((long)(threshold0_off()*24000.0/15.0))})));
        tr.add(separator.func_230532_e_().func_230529_a_(ModAuxiliaries.localizable("switchconfig.daytimerclock.output_power", TextFormatting.RED, new Object[]{on_power()})));
        if(debounce()>0) {
          tr.add(separator.func_230532_e_().func_230529_a_(ModAuxiliaries.localizable("switchconfig.daytimerclock.random", TextFormatting.DARK_GREEN, new Object[]{debounce()}) ));
        } else {
          tr.add(new StringTextComponent(""));
        }
        tr.add(separator.func_230532_e_().func_230529_a_(ModAuxiliaries.localizable("switchconfig.daytimerclock.output_power", TextFormatting.RED, new Object[]{on_power()})));
        ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchconfig.daytimerclock", TextFormatting.RESET, tr.toArray()));
      }
      markDirty();
      return true;
    }

    @Override
    public void tick()
    {
      if(ModConfig.without_environmental_switch_update) return;
      if((!hasWorld()) || (getWorld().isRemote) || (--update_timer_ > 0)) return;
      if(update_interval_ < 10) update_interval_ = 10;
      update_timer_ = update_interval_ + (int)(Math.random()*5); // sensor timing noise using rnd
      BlockState state = getBlockState();
      if((!(state.getBlock() instanceof DayTimerSwitchBlock))) return;
      boolean active = state.get(POWERED);
      long wt = world.getDayTime() % 24000;
      final double t = 15.0/24000.0 * wt;
      boolean active_setpoint;
      if(threshold0_on() == threshold0_off()) {
        active_setpoint = false;
      } else if(threshold0_on() < threshold0_off()) {
        active_setpoint = (t >= threshold0_on()) && (t <= threshold0_off());
      } else {
        active_setpoint = ((t >= threshold0_on()) && (t <= 15.0)) || (t >= 0.0) && (t <= threshold0_off());
      }
      if(active != active_setpoint) {
        if(debounce() <= 0) {
          active = active_setpoint;
        } else {
          double d1 = (1.0d - ((double)(debounce()))/(debounce_max*0.9)) * 0.7;
          d1 = d1 * d1;
          if(Math.random() <= d1) active = active_setpoint;
        }
      }
      // state setting
      updateSwitchState(state, (DayTimerSwitchBlock)(state.getBlock()), active, 0);
    }
  }
}
