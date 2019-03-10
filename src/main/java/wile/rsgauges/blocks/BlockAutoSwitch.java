/*
 * @file BlockAutoSwitch.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Basic class for blocks representing redstone signal sources, like
 * the vanilla lever or button.
 */
package wile.rsgauges.blocks;

import wile.rsgauges.detail.ModConfig;
import wile.rsgauges.detail.ModAuxiliaries;
import wile.rsgauges.detail.ModResources;
import wile.rsgauges.items.ItemSwitchLinkPearl;
import net.minecraft.world.World;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.monster.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockAutoSwitch extends BlockSwitch
{
  public BlockAutoSwitch(String registryName, AxisAlignedBB unrotatedBB, long config, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound, @Nullable Material material)
  { super(registryName, unrotatedBB, null, config, powerOnSound, powerOffSound, material); }

  public BlockAutoSwitch(String registryName, AxisAlignedBB unrotatedBB, long config, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(registryName, unrotatedBB, null, config, powerOnSound, powerOffSound, null); }

  @Override
  public boolean onLinkRequest(final ItemSwitchLinkPearl.SwitchLink link, long req, final World world, final BlockPos pos, @Nullable final EntityPlayer player)
  {
    if((world==null) || ((config & (SWITCH_CONFIG_LINK_TARGET_SUPPORT))==0) || (world.isRemote)) return false;
    if((config & (SWITCH_CONFIG_TIMER_INTERVAL))==0) return false; // only interval timer can be a link target
    IBlockState state = world.getBlockState(pos);
    if((state == null) || (!(state.getBlock() instanceof BlockAutoSwitch))) return false;
    TileEntityAutoSwitch te = getTe(world, pos);
    if((te==null) || (!te.check_link_request(link))) return false;
    te.updateSwitchState(state, this, !state.getValue(POWERED), 0);
    return true;
  }

  @Override
  public int tickRate(World world)
  { return 200; } // no block based scheduling needed

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
  {}

  @Override
  public TileEntity createTileEntity(World world, IBlockState state)
  {
    if((config & (SWITCH_CONFIG_SENSOR_VOLUME|SWITCH_CONFIG_SENSOR_LINEAR))!=0) {
      return new TileEntityDetectorSwitch();
    } else if((config & (SWITCH_CONFIG_SENSOR_ENVIRONMENTAL))!=0) {
      return new TileEntityEnvironmentalSensorSwitch();
    } else if((config & (SWITCH_CONFIG_TIMER_INTERVAL))!=0) {
      return new TileEntityIntervalTimerSwitch();
    } else {
      return new TileEntityAutoSwitch();
    }
  }

  @Override
  public TileEntityAutoSwitch getTe(IBlockAccess world, BlockPos pos)
  {
    TileEntity te = world.getTileEntity(pos);
    if((!(te instanceof TileEntityAutoSwitch))) return null;
    return (TileEntityAutoSwitch)te;
  }

  /**
   * Tile entity base
   */
  public static class TileEntityAutoSwitch extends TileEntitySwitch
  {
    protected final void updateSwitchState(IBlockState state, BlockAutoSwitch block, boolean active, int hold_time)
    {
      if(active) {
        off_timer_reset(hold_time);
        if(!state.getValue(POWERED)) {
          if(this instanceof TileEntityIntervalTimerSwitch) ((TileEntityIntervalTimerSwitch)this).restart();
          world.setBlockState(pos, (state=state.withProperty(POWERED, true)), 1|2);
          block.power_on_sound.play(world, pos);
          world.notifyNeighborsOfStateChange(pos, block, false);
          world.notifyNeighborsOfStateChange(pos.offset(state.getValue(FACING).getOpposite()), block, false);
          if((block.config & BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT)!=0) {
            if(!activate_links(ItemSwitchLinkPearl.SwitchLink.SWITCHLINK_RELAY_ACTIVATE)) {
              ModResources.BlockSoundEvents.SWITCHLINK_LINK_PEAL_USE_FAILED.play(world, pos);
            }
          }
        }
      } else if(state.getValue(POWERED)) {
        if(off_timer_tick() <= 0) {
          world.setBlockState(pos, (state=state.withProperty(POWERED, false)));
          block.power_off_sound.play(world, pos);
          world.notifyNeighborsOfStateChange(pos, block, false);
          world.notifyNeighborsOfStateChange(pos.offset(state.getValue(FACING).getOpposite()), block, false);
          if((block.config & BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT)!=0) {
            if(!activate_links(ItemSwitchLinkPearl.SwitchLink.SWITCHLINK_RELAY_DEACTIVATE)) {
              ModResources.BlockSoundEvents.SWITCHLINK_LINK_PEAL_USE_FAILED.play(world, pos);
            }
          }
        }
      }
    }
  }

  /**
   * Tile entity for entity detection based auto switches
   */
  public static class TileEntityDetectorSwitch extends TileEntityAutoSwitch implements ITickable
  {
    public static final Class<?> filter_classes[] = { EntityLivingBase.class, EntityPlayer.class, EntityMob.class, EntityAnimal.class, EntityVillager.class, EntityItem.class, Entity.class };
    public static final String filter_class_names[] = { "creatures", "players", "mobs", "animals", "villagers", "objects", "everything" };
    private static final int max_sensor_range_ = 16;
    private int sensor_entity_count_threshold_ = 1;
    private int sensor_range_ = 5;
    private int filter_ = 0;
    private AxisAlignedBB area_ = null;
    private int update_interval_ = 10;
    private int update_timer_ = 0;

    public int filter()
    { return filter_; }

    public void filter(int sel)
    { filter_ = (sel<0) ? 0 : (sel >= filter_classes.length) ? (filter_classes.length-1) : sel; }

    public Class<?> filter_class()
    { return (filter_<=0) ? (filter_classes[0]) : ((filter_ >= filter_classes.length) ? (filter_classes[filter_classes.length-1]) : filter_classes[filter_]); }

    public void sensor_entity_threshold(int count)
    { sensor_entity_count_threshold_ = (count < 1) ? 1 : count; }

    public int sensor_entity_threshold()
    { return sensor_entity_count_threshold_; }

    public void sensor_range(int r)
    { sensor_range_ = (r<1) ? (1) : ((r>max_sensor_range_) ? max_sensor_range_ : r); }

    public int sensor_range()
    { return sensor_range_; }

    @Override
    public void writeNbt(NBTTagCompound nbt, boolean updatePacket)
    {
      super.writeNbt(nbt, updatePacket);
      nbt.setInteger("range", sensor_range_);
      nbt.setInteger("entitythreshold", sensor_entity_count_threshold_);
      nbt.setInteger("filter", filter_);
    }

    @Override
    public void readNbt(NBTTagCompound nbt, boolean updatePacket)
    {
      super.readNbt(nbt, updatePacket);
      sensor_range(nbt.getInteger("range"));
      sensor_entity_threshold(nbt.getInteger("entitythreshold"));
      filter(nbt.getInteger("filter"));
    }

    @Override
    public void reset()
    { super.reset(); update_timer_=0; area_=null; sensor_range_=5; filter_=0; }

    @Override
    public boolean activation_config(IBlockState state, @Nullable EntityPlayer player, double x, double y)
    {
      if(state == null) return false;
      final int direction = ((y >= 11) && (y <= 14)) ? (1) : (((y >= 1) && (y <= 5)) ? (-1) : (0));
      final int field = ((x>=2) && (x<=4)) ? (1) : (
              ((x>=5) && (x<=7)) ? (2) : (
              ((x>=8) && (x<=10)) ? (3) : (
              ((x>=11) && (x<=13)) ? (4) : (0)
              )));
      if((direction==0) || (field==0)) return false;
      switch(field) {
        case 1: {
          sensor_range(sensor_range()+direction);
          area_ = null;
          ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchconfig.detector.sensor_range", TextFormatting.BLUE, new Object[]{sensor_range()}));
          break;
        }
        case 2: {
          sensor_entity_threshold(sensor_entity_threshold() + direction);
          ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchconfig.detector.entity_threshold", TextFormatting.YELLOW, new Object[]{sensor_entity_threshold()}));
          break;
        }
        case 3: {
          filter(filter() + direction);
          ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchconfig.detector.entity_filter", TextFormatting.DARK_GREEN, new Object[]{new TextComponentTranslation("rsgauges.switchconfig.detector.entity_filter."+filter_class_names[filter()])}));
          break;
        }
        case 4: {
          on_power(on_power() + direction);
          if(on_power() < 1) on_power(1);
          ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchconfig.detector.output_power", TextFormatting.RED, new Object[]{on_power()}));
          break;
        }
      }
      markDirty();
      return true;
    }

    @Override
    public void update()
    {
      if(ModConfig.zmisc.without_detector_switch_update) return;
      if((!hasWorld()) || (getWorld().isRemote) || (--update_timer_ > 0)) return;
      update_timer_ = update_interval_;
      IBlockState state = getWorld().getBlockState(getPos());
      if((state==null) || (!(state.getBlock() instanceof BlockAutoSwitch))) return;
      BlockAutoSwitch block = (BlockAutoSwitch)(state.getBlock());
      // initialisations
      if(update_interval_==0) {
        if((block.config & SWITCH_CONFIG_SENSOR_LINEAR) != 0) {
          update_interval_ = ModConfig.tweaks.autoswitch_linear_update_interval;
        } else {
          update_interval_ = ModConfig.tweaks.autoswitch_volumetric_update_interval;
        }
      }
      if(area_ == null) {
        int size = sensor_range();
        AxisAlignedBB range_bb;
        if((block.config & SWITCH_CONFIG_SENSOR_VOLUME) != 0) {
          range_bb = new AxisAlignedBB(0,-2,-size, size,2,size);
        } else if((block.config & SWITCH_CONFIG_SENSOR_LINEAR) != 0) {
          range_bb = new AxisAlignedBB(-0.5,-0.5,-0.5, size,0.5,0.5);
        } else {
          range_bb = new AxisAlignedBB(0,0,0, 1,0,0);
        }
        EnumFacing facing = state.getValue(FACING);
        AxisAlignedBB bb = ModAuxiliaries.transform_forward(range_bb, facing).offset(getPos()).expand(1,1,1);
        area_ = new AxisAlignedBB(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
      }

      // measurement
      boolean active = false;
      if(off_timer() > update_interval_) {
        active = true; // no need to ray trace, it's anyway on at the next update.
      } else {
        @SuppressWarnings("unchecked")
        List<Entity> hits = world.getEntitiesWithinAABB((Class<Entity>)filter_class(), area_);
        if(hits.size() >= sensor_entity_count_threshold_) {
          int num_seen = 0;
          final Vec3d switch_position = new Vec3d((double)getPos().getX()+.5, (double)getPos().getY()+.5, (double)getPos().getZ()+.5);
          for(Entity e:hits) {
            if(
                (world.rayTraceBlocks(new Vec3d(e.posX-0.2,e.posY+e.getEyeHeight(),e.posZ-0.2), switch_position, true, false, false) == null) ||
                (world.rayTraceBlocks(new Vec3d(e.posX+0.2,e.posY+e.getEyeHeight(),e.posZ+0.2), switch_position, true, false, false) == null)
            ) {
              if(++num_seen >= sensor_entity_count_threshold_) {
                active = true;
                break;
              }
            }
          }
        }
      }
      // state setting
      updateSwitchState(state, block, active, 4);
    }
  }

  /**
   * Tile entity for environmental and time sensor based switches
   */
  public static class TileEntityEnvironmentalSensorSwitch extends TileEntityAutoSwitch implements ITickable
  {
    private static final int debounce_max = 10;
    private int update_interval_ = 10;
    private double threshold0_on_  = 0;
    private double threshold0_off_ = 0;
    private int debounce_ = 0;
    private int update_timer_ = 0;
    private int debounce_counter_ = 0;

    public double threshold0_on()
    { return threshold0_on_; }

    public double threshold0_off()
    { return threshold0_off_; }

    public int debounce()
    { return debounce_; }

    public void threshold0_on(double v)
    { threshold0_on_ = (v<0) ? (0) : ((v>15.0) ? (15.0) : (v)); }

    public void threshold0_off(double v)
    { threshold0_off_ = (v<0) ? (0) : ((v>15.0) ? (15.0) : (v)); }

    public void debounce(int v)
    { debounce_ = (v<0) ? (0) : ((v>debounce_max) ? (debounce_max) : (v)); }

    @Override
    public void writeNbt(NBTTagCompound nbt, boolean updatePacket)
    {
      super.writeNbt(nbt, updatePacket);
      nbt.setDouble("threshold0_on", threshold0_on());
      nbt.setDouble("threshold0_off", threshold0_off());
      nbt.setInteger("debounce", debounce());
    }

    @Override
    public void readNbt(NBTTagCompound nbt, boolean updatePacket)
    {
      super.readNbt(nbt, updatePacket);
      threshold0_on(nbt.getDouble("threshold0_on"));
      threshold0_off(nbt.getDouble("threshold0_off"));
      debounce(nbt.getInteger("debounce"));
    }

    @Override
    public boolean activation_config(IBlockState state, @Nullable EntityPlayer player, double x, double y)
    {
      if(state == null) return false;
      final BlockSwitch block = (BlockSwitch)state.getBlock();

      // @TODO: Construction time list or lambla for field assignment.
      final int direction = ((y >= 11) && (y <= 14)) ? (1) : (((y >= 1) && (y <= 5)) ? (-1) : (0));
      final int field = ((x>=2) && (x<=4)) ? (1) : (
              ((x>=5) && (x<=7)) ? (2) : (
              ((x>=8) && (x<=10)) ? (3) : (
              ((x>=11) && (x<=13)) ? (4) : (0)
              )));
      if((direction==0) || (field==0)) return false;

      if((block.config & SWITCH_CONFIG_SENSOR_LIGHT)!=0) {
        switch(field) {
          case 1: {
            threshold0_on(threshold0_on()+direction);
            if(threshold0_off() > threshold0_on()) threshold0_off(threshold0_on());
            break;
          }
          case 2: {
            threshold0_off(threshold0_off()+direction);
            if(threshold0_on() < threshold0_off()) threshold0_on(threshold0_off());
            break;
          }
          case 3: { debounce(debounce()+direction); break; }
          case 4: { on_power(on_power() + direction); break; }
        }
        if(threshold0_on() < 1) threshold0_on(1);
        if(on_power() < 1) on_power(1);
        {
          ArrayList<Object> tr = new ArrayList<>();
          final TextComponentTranslation trunit = ModAuxiliaries.localizable("switchconfig.lightsensor.lightunit", null);
          TextComponentString separator = (new TextComponentString(" | ")); separator.getStyle().setColor(TextFormatting.GRAY);
          tr.add(ModAuxiliaries.localizable("switchconfig.lightsensor.threshold_on", TextFormatting.BLUE, new Object[]{(int)threshold0_on(), trunit}));
          tr.add(separator.createCopy().appendSibling(ModAuxiliaries.localizable("switchconfig.lightsensor.threshold_off", TextFormatting.YELLOW, new Object[]{(int)threshold0_off(), trunit})));
          tr.add(separator.createCopy().appendSibling(ModAuxiliaries.localizable("switchconfig.lightsensor.output_power", TextFormatting.RED, new Object[]{on_power()})));
          if(debounce()>0) {
            tr.add(separator.createCopy().appendSibling(ModAuxiliaries.localizable("switchconfig.lightsensor.debounce", TextFormatting.DARK_GREEN, new Object[]{debounce()})));
          } else {
            tr.add(new TextComponentString(""));
          }
          ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchconfig.lightsensor", TextFormatting.RESET, tr.toArray()));
        }
      } else if((block.config & (SWITCH_CONFIG_TIMER_DAYTIME))!=0) {
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
          TextComponentString separator = (new TextComponentString(" | ")); separator.getStyle().setColor(TextFormatting.GRAY);
          ArrayList<Object> tr = new ArrayList<>();
          tr.add(ModAuxiliaries.localizable("switchconfig.daytimerclock.daytime_on", TextFormatting.BLUE, new Object[]{ModAuxiliaries.daytimeToString((long)(threshold0_on()*24000.0/15.0))}));
          tr.add(separator.createCopy().appendSibling(ModAuxiliaries.localizable("switchconfig.daytimerclock.daytime_off", TextFormatting.YELLOW, new Object[]{ModAuxiliaries.daytimeToString((long)(threshold0_off()*24000.0/15.0))})));
          tr.add(separator.createCopy().appendSibling(ModAuxiliaries.localizable("switchconfig.daytimerclock.output_power", TextFormatting.RED, new Object[]{on_power()})));
          if(debounce()>0) {
            tr.add(separator.createCopy().appendSibling(ModAuxiliaries.localizable("switchconfig.daytimerclock.random", TextFormatting.DARK_GREEN, new Object[]{debounce()}) ));
          } else {
            tr.add(new TextComponentString(""));
          }
          tr.add(separator.createCopy().appendSibling(ModAuxiliaries.localizable("switchconfig.daytimerclock.output_power", TextFormatting.RED, new Object[]{on_power()})));
          ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchconfig.daytimerclock", TextFormatting.RESET, tr.toArray()));
        }
      } else if((block.config & (SWITCH_CONFIG_SENSOR_RAIN|SWITCH_CONFIG_SENSOR_LIGHTNING))!=0) {
        switch(field) {
          case 4: { on_power(on_power() + direction); break; }
        }
        if(on_power() < 1) on_power(1);
        {
          if((block.config & SWITCH_CONFIG_SENSOR_RAIN)!=0) {
            ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchconfig.rainsensor.output_power", TextFormatting.RED, new Object[]{on_power()}));
          } else {
            ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchconfig.thundersensor.output_power", TextFormatting.RED, new Object[]{on_power()}));
          }
        }
      }
      markDirty();
      return true;
    }

    @Override
    public void update()
    {
      if(ModConfig.zmisc.without_environmental_switch_update) return;
      if((!hasWorld()) || (getWorld().isRemote) || (--update_timer_ > 0)) return;
      if(update_interval_ < 10) update_interval_ = 10;
      update_timer_ = update_interval_ + (int)(Math.random()*5); // sensor timing noise using rnd
      IBlockState state = getWorld().getBlockState(getPos());
      if((state==null) || (!(state.getBlock() instanceof BlockAutoSwitch))) return;
      BlockAutoSwitch block = (BlockAutoSwitch)(state.getBlock());
      boolean active = state.getValue(POWERED);
      if((block.config & SWITCH_CONFIG_TIMER_DAYTIME) != 0) {
        long wt = world.getWorldTime() % 24000;
        final double t = 15.0/24000.0 * wt;
        boolean active_setpoint = false;
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
      } else if((block.config & SWITCH_CONFIG_SENSOR_LIGHT) != 0) {
        if((threshold0_on()==0) && (threshold0_off()==0) ) {
          threshold0_on(7);
          threshold0_off(6);
        } else {
          // measurement
          double value = getWorld().getLight(pos, false);
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
        if((state.getValue(FACING)!=EnumFacing.UP) && (state.getValue(FACING)!=EnumFacing.DOWN)) {
          debounce_counter_ += getWorld().isRainingAt(getPos().offset(EnumFacing.UP, 1)) ? 1 : -1;
          if(debounce_counter_ <= 0) {
            debounce_counter_ = 0;
            active = false;
          } else if(debounce_counter_ >= 4) {
            debounce_counter_ = 4;
            active = true;
          }
        }
      } else if((block.config & SWITCH_CONFIG_SENSOR_LIGHTNING)!=0) {
        debounce_counter_ += (getWorld().isThundering() && (getWorld().isRainingAt(getPos()) || getWorld().isRainingAt(getPos().offset(EnumFacing.UP, 20)) )) ? 1 : -1;
        if(debounce_counter_ <= 0) {
          debounce_counter_ = 0;
          active = false;
        } else if(debounce_counter_ >= 4) {
          debounce_counter_ = 4;
          active = true;
        }
      }
      // state setting
      updateSwitchState(state, block, active, 1);
    }
  }

  /**
   * Tile entity for timer interval based switches
   */
  public static class TileEntityIntervalTimerSwitch extends TileEntityAutoSwitch implements ITickable
  {
    private static final int ramp_max = 5;
    private static final int t_max = 20 * 60 * 10; // 10min @20clk/s
    private static final int t_min =  5;           // 0.25s @20clk/s

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
    protected void setWorldCreate(World world)
    { super.setWorldCreate(world); p_set(15); t_on(20); t_off(20); ramp(0); }

    @Override
    public void writeNbt(NBTTagCompound nbt, boolean updatePacket)
    {
      super.writeNbt(nbt, updatePacket);
      nbt.setInteger("pset", p_set());
      nbt.setInteger("toff", t_off());
      nbt.setInteger("ton", t_on());
      nbt.setInteger("ramp", ramp());
    }

    @Override
    public void readNbt(NBTTagCompound nbt, boolean updatePacket)
    {
      super.readNbt(nbt, updatePacket);
      p_set(nbt.getInteger("pset"));
      t_off(nbt.getInteger("toff"));
      t_on(nbt.getInteger("ton"));
      ramp(nbt.getInteger("ramp"));
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
    public boolean activation_config(IBlockState state, @Nullable EntityPlayer player, double x, double y)
    {
      if(state == null) return false;
      final int direction = ((y >= 11) && (y <= 14)) ? (1) : (((y >= 1) && (y <= 5)) ? (-1) : (0));
      final int field = ((x>=2) && (x<=4)) ? (1) : (
              ((x>=5) && (x<=7)) ? (2) : (
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
        boolean switch_state = false;
        try { switch_state = state.getValue(POWERED); } catch(Exception e) {}
        if(!selected) switch_state = !switch_state; // will be switched in turn.
        updateSwitchState(state, (BlockAutoSwitch) state.getBlock(), switch_state, 0);
        {
          TextComponentString separator = (new TextComponentString(" | ")); separator.getStyle().setColor(TextFormatting.GRAY);
          ArrayList<Object> tr = new ArrayList<>();
          tr.add(ModAuxiliaries.localizable("switchconfig.intervaltimer.t_on", TextFormatting.BLUE, new Object[]{ModAuxiliaries.ticksToSecondsString(t_on())}));
          tr.add(separator.createCopy().appendSibling(ModAuxiliaries.localizable("switchconfig.intervaltimer.t_off", TextFormatting.YELLOW, new Object[]{ModAuxiliaries.ticksToSecondsString(t_off())})));
          tr.add(separator.createCopy().appendSibling(ModAuxiliaries.localizable("switchconfig.intervaltimer.output_power", TextFormatting.RED, new Object[]{p_set()})));
          if(ramp()>0) tr.add(separator.createCopy().appendSibling(ModAuxiliaries.localizable("switchconfig.intervaltimer.ramp", TextFormatting.DARK_GREEN, new Object[]{ramp()})));
          if(!switch_state) tr.add(separator.createCopy().appendSibling(ModAuxiliaries.localizable("switchconfig.intervaltimer.standby", TextFormatting.AQUA)));
          while(tr.size() < 5) tr.add(new TextComponentString("")); // const lang file formatting arg count.
          ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchconfig.intervaltimer", TextFormatting.RESET, tr.toArray()));
        }
      }
      return selected; // false: Switches output on/off (blockstate) in caller
    }

    @Override
    public int power(IBlockState state, boolean strong)
    { return (nooutput() || (!state.getValue(POWERED)) || ((strong && weak())) ? (0) : on_power()); }

    @Override
    public void update()
    {
      if(ModConfig.zmisc.without_timer_switch_update) return;
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
        IBlockState state = getWorld().getBlockState(getPos());
        if((state==null) || (!(state.getBlock() instanceof BlockAutoSwitch)) || (!state.getValue(POWERED))) {
          update_timer_ = 200 + ((int)(Math.random() * 10));
          on_power(inverted() ? (15) : (0));
        }
        world.notifyNeighborsOfStateChange(pos, state.getBlock(), false);
        world.notifyNeighborsOfStateChange(pos.offset(state.getValue(FACING).getOpposite()), state.getBlock(), false);
      }
    }
  }
}
