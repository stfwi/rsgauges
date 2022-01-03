/*
 * @file EnvironmentalSensorSwitchBlock.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Autoswitch specialised for environmental (also normally slower sampling rate)
 * measurements.
 */
package wile.rsgauges.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import wile.rsgauges.ModContent;
import wile.rsgauges.detail.ModResources;
import wile.rsgauges.libmc.detail.Auxiliaries;
import wile.rsgauges.libmc.detail.Overlay;

import javax.annotation.Nullable;
import java.util.ArrayList;


public class EnvironmentalSensorSwitchBlock extends AutoSwitchBlock
{
  public EnvironmentalSensorSwitchBlock(long config, BlockBehaviour.Properties properties, AABB unrotatedBBUnpowered, @Nullable AABB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound); }

  public EnvironmentalSensorSwitchBlock(long config, BlockBehaviour.Properties properties, AABB unrotatedBBUnpowered, @Nullable AABB unrotatedBBPowered)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, null, null); }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  @Nullable
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
  { return new EnvironmentalSensorSwitchTileEntity(pos, state); }

  // -------------------------------------------------------------------------------------------------------------------
  // Tile entity
  // -------------------------------------------------------------------------------------------------------------------

  /**
   * Tile entity for environmental and time sensor based switches
   */
  public static class EnvironmentalSensorSwitchTileEntity extends AutoSwitchTileEntity
  {
    protected static final int debounce_max = 10;
    protected int update_interval_ = 10;
    protected double threshold0_on_  = 0;
    protected double threshold0_off_ = 0;
    protected int debounce_ = 0;
    protected int update_timer_ = 0;
    protected int debounce_counter_ = 0;

    public EnvironmentalSensorSwitchTileEntity(BlockEntityType<?> te_type, BlockPos pos, BlockState state)
    { super(te_type, pos, state); }

    public EnvironmentalSensorSwitchTileEntity(BlockPos pos, BlockState state)
    { super(ModContent.TET_ENVSENSOR_SWITCH, pos, state); }

    public double threshold0_on()
    { return threshold0_on_; }

    public double threshold0_off()
    { return threshold0_off_; }

    public int debounce()
    { return debounce_; }

    public void threshold0_on(double v)
    { threshold0_on_ = (v<0) ? (0) : (Math.min(v, 15.0)); }

    public void threshold0_off(double v)
    { threshold0_off_ = (v<0) ? (0) : (Math.min(v, 15.0)); }

    public void debounce(int v)
    { debounce_ = (v<0) ? (0) : (Math.min(v, debounce_max)); }

    @Override
    public void write(CompoundTag nbt, boolean updatePacket)
    {
      super.write(nbt, updatePacket);
      nbt.putDouble("threshold0_on", threshold0_on());
      nbt.putDouble("threshold0_off", threshold0_off());
      nbt.putInt("debounce", debounce());
    }

    @Override
    public void read(CompoundTag nbt, boolean updatePacket)
    {
      super.read(nbt, updatePacket);
      threshold0_on(nbt.getDouble("threshold0_on"));
      threshold0_off(nbt.getDouble("threshold0_off"));
      debounce(nbt.getInt("debounce"));
    }

    @Override
    public boolean activation_config(BlockState state, @Nullable Player player, double x, double y, boolean show_only)
    {
      if(state == null) return false;
      final SwitchBlock block = (SwitchBlock)state.getBlock();
      final int direction = (y >= 13) ? (1) : ((y <= 2) ? (-1) : (0));
      final int field = ((x>=2) && (x<=3.95)) ? (1) : ( ((x>=4.25) && (x<=7)) ? (2) : ( ((x>=8) && (x<=10)) ? (3) : ( ((x>=11) && (x<=13)) ? (4) : (0) )));
      if((direction==0) || (field==0)) return false;
      if((block.config & SWITCH_CONFIG_SENSOR_LIGHT)!=0) {
        if(!show_only) {
          switch (field) {
            case 1 -> {
              threshold0_on(threshold0_on() + direction);
              if (threshold0_off() > threshold0_on()) threshold0_off(threshold0_on());
              break;
            }
            case 2 -> {
              threshold0_off(threshold0_off() + direction);
              if (threshold0_on() < threshold0_off()) threshold0_on(threshold0_off());
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
          }
          if(threshold0_on() < 1) threshold0_on(1);
          if(setpower() < 1) setpower(1);
          setChanged();
        }
        {
          ArrayList<Object> tr = new ArrayList<>();
          final TranslatableComponent trunit = Auxiliaries.localizable("switchconfig.lightsensor.lightunit");
          TextComponent separator = (new TextComponent(" | ")); separator.withStyle(ChatFormatting.GRAY);
          tr.add(Auxiliaries.localizable("switchconfig.lightsensor.threshold_on", ChatFormatting.BLUE, new Object[]{(int)threshold0_on(), trunit}));
          tr.add(separator.copy().append(Auxiliaries.localizable("switchconfig.lightsensor.threshold_off", ChatFormatting.YELLOW, new Object[]{(int)threshold0_off(), trunit})));
          tr.add(separator.copy().append(Auxiliaries.localizable("switchconfig.lightsensor.output_power", ChatFormatting.RED, new Object[]{setpower()})));
          if(debounce()>0) {
            tr.add(separator.copy().append(Auxiliaries.localizable("switchconfig.lightsensor.debounce", ChatFormatting.DARK_GREEN, new Object[]{debounce()})));
          } else {
            tr.add(new TextComponent(""));
          }
          Overlay.show(player, Auxiliaries.localizable("switchconfig.lightsensor", ChatFormatting.RESET, tr.toArray()));
        }
      } else if((block.config & (SWITCH_CONFIG_SENSOR_RAIN|SWITCH_CONFIG_SENSOR_LIGHTNING))!=0) {
        if(!show_only) {
          switch (field) {
            case 4 -> {
              setpower(setpower() + direction);
              break;
            }
          }
          if(setpower() < 1) setpower(1);
          setChanged();
        }
        if((block.config & SWITCH_CONFIG_SENSOR_RAIN)!=0) {
          Overlay.show(player, Auxiliaries.localizable("switchconfig.rainsensor.output_power", ChatFormatting.RED, new Object[]{setpower()}));
        } else {
          Overlay.show(player, Auxiliaries.localizable("switchconfig.thundersensor.output_power", ChatFormatting.RED, new Object[]{setpower()}));
        }
      }
      return true;
    }

    @Override
    public void tick()
    {
      if((!hasLevel()) || (getLevel().isClientSide) || (--update_timer_ > 0)) return;
      if(update_interval_ < 10) update_interval_ = 10;
      update_timer_ = update_interval_ + (int)(Math.random()*5); // sensor timing noise using rnd
      BlockState state = getLevel().getBlockState(getBlockPos());
      if((state==null) || (!(state.getBlock() instanceof AutoSwitchBlock))) return;
      AutoSwitchBlock block = (AutoSwitchBlock)(state.getBlock());
      boolean active = state.getValue(POWERED);
      if((block.config & SWITCH_CONFIG_SENSOR_LIGHT) != 0) {
        if((threshold0_on()==0) && (threshold0_off()==0) ) {
          threshold0_on(7);
          threshold0_off(6);
        } else {
          // measurement
          double value = getLevel().getMaxLocalRawBrightness(worldPosition);
          // switch value evaluation
          int measurement = 0;
          if(threshold0_off() >= threshold0_on()) {
            // Use exact light value match
            measurement += (value==threshold0_on()) ? 1 : -1;
          } else {
            // Standard balanced threshold switching.
            if(value >= threshold0_on()) measurement = 1;
            if(value <= threshold0_off()) measurement = -1; // priority off
          }
          if(debounce() <= 0) {
            if(measurement!=0) active = (measurement>0);
            debounce_counter_ = 0;
          } else {
            debounce_counter_ = debounce_counter_ + measurement;
            if(debounce_counter_ <= 0) {
              active = false; debounce_counter_ = 0;
            } else if(debounce_counter_ >= debounce_) {
              active = true; debounce_counter_ = debounce_;
            }
          }
        }
      } else if((block.config & SWITCH_CONFIG_SENSOR_RAIN)!=0) {
        if((state.getValue(FACING)!= Direction.UP) && (state.getValue(FACING)!=Direction.DOWN)) {
          debounce_counter_ += getLevel().isRainingAt(getBlockPos().relative(Direction.UP, 1)) ? 1 : -1;
          if(debounce_counter_ <= 0) {
            debounce_counter_ = 0;
            active = false;
          } else if(debounce_counter_ >= 4) {
            debounce_counter_ = 4;
            active = true;
          }
        }
      } else if((block.config & SWITCH_CONFIG_SENSOR_LIGHTNING)!=0) {
        debounce_counter_ += (getLevel().isThundering() && (getLevel().isRainingAt(getBlockPos()) || getLevel().isRainingAt(getBlockPos().relative(Direction.UP, 20)) )) ? 1 : -1;
        if(debounce_counter_ <= 0) {
          debounce_counter_ = 0;
          active = false;
        } else if(debounce_counter_ >= 4) {
          debounce_counter_ = 4;
          active = true;
        }
      }
      // state setting
      updateSwitchState(state, block, active, configured_on_time());
    }
  }

}
