/**
 * @file AutoSwitchBlock.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Basic class for blocks representing redstone signal sources, like
 * the vanilla lever or button.
**/
package wile.rsgauges.blocks;

import wile.rsgauges.ModConfig;
import wile.rsgauges.ModAuxiliaries;
import wile.rsgauges.ModResources;
import wile.rsgauges.blocks.RsBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.monster.*;

public class AutoSwitchBlock extends SwitchBlock {

  public AutoSwitchBlock(String registryName, AxisAlignedBB unrotatedBB, int config, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound) {
    super(registryName, unrotatedBB, null, config, powerOnSound, powerOffSound);
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    if(world.isRemote) return true;
    AutoSwitchBlock.AutoSwitchTileEntity te = getTe(world, pos);
    if(te == null) return true;
    te.click_config(null);
    if((config & SWITCH_CONFIG_TOUCH_CONFIGURABLE)==0) return true;
    RsBlock.WrenchActivationCheck wac = RsBlock.WrenchActivationCheck.onBlockActivatedCheck(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    if((wac.accepted) && (wac.wrenched) && (state.getBlock() instanceof AutoSwitchBlock)) {
      te.activation_config((AutoSwitchBlock)state.getBlock(), player, wac.x, wac.y);
    }
    return true;
  }

  @Override
  public int tickRate(World world) { return 100; } // no block based scheduling needed

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {} // in tile entity update (which is anyway needed)

  @Override
  public TileEntity createTileEntity(World world, IBlockState state) { return new AutoSwitchBlock.AutoSwitchTileEntity(); }

  @Override
  public AutoSwitchBlock.AutoSwitchTileEntity getTe(World world, BlockPos pos) {
    TileEntity te = world.getTileEntity(pos);
    if((!(te instanceof AutoSwitchBlock.AutoSwitchTileEntity))) return null;
    return (AutoSwitchBlock.AutoSwitchTileEntity)te;
  }

  /**
   * Tile entity
   */
  public static final class AutoSwitchTileEntity extends SwitchBlock.SwitchTileEntity implements ITickable {
    public static final Class filter_classes[] = { EntityLivingBase.class, EntityPlayer.class, EntityMob.class, EntityAnimal.class, EntityVillager.class, Entity.class };
    public static final String filter_class_names[] = { "any creature", "players", "mobs", "animals", "villagers", "everything" };
    private static final int max_sensor_range_ = 16;
    private int hold_time_ = 10;
    private int sensor_entity_count_threshold_ = 1;
    private int sensor_range_ = 5;
    private int filter_ = 0;
    private AxisAlignedBB area_ = null;
    private int update_interval_ = 10;
    private int update_timer_ = 0;

    public int filter() { return filter_; }
    public void filter(int sel) { filter_ = (sel<0) ? 0 : (sel >= filter_classes.length) ? (filter_classes.length-1) : sel; }
    public Class filter_class() { return (filter_<=0) ? (filter_classes[0]) : ((filter_ >= filter_classes.length) ? (filter_classes[filter_classes.length-1]) : filter_classes[filter_]); }
    public void sensor_entity_threshold(int count) { sensor_entity_count_threshold_ = (count < 1) ? 1 : count; }
    public int sensor_entity_threshold() { return sensor_entity_count_threshold_; }
    public void sensor_range(int r) { sensor_range_ = (r<1) ? (1) : ((r>max_sensor_range_) ? max_sensor_range_ : r); }
    public int sensor_range() { return sensor_range_; }

    @Override
    public void writeNbt(NBTTagCompound nbt, boolean updatePacket) {
      super.writeNbt(nbt, updatePacket);
      nbt.setInteger("range", sensor_range_);
      nbt.setInteger("entitythreshold", sensor_entity_count_threshold_);
      nbt.setInteger("filter", filter_);
    }

    @Override
    public void readNbt(NBTTagCompound nbt, boolean updatePacket)  {
      super.readNbt(nbt, updatePacket);
      this.sensor_range(nbt.getInteger("range"));
      this.sensor_entity_threshold(nbt.getInteger("entitythreshold"));
      this.filter(nbt.getInteger("filter"));
    }

    @Override
    public void reset() { super.reset(); update_timer_=0; area_=null; sensor_range_=5; filter_=0; }

    @Override
    public boolean activation_config(@Nullable SwitchBlock block, @Nullable EntityPlayer player, double x, double y) {
      if(block == null) return false;
      int direction=0, field=0;
      //System.out.println("xy:" + Double.toString(x) + "," + Double.toString(y));
      if((block.config & (SWITCH_CONFIG_SENSOR_VOLUME|SWITCH_CONFIG_SENSOR_LINEAR)) != 0) {
        direction = ((y >= 11) && (y <= 14)) ? (1) : (((y >= 1) && (y <= 5)) ? (-1) : (0));
        field = ((x>=2) && (x<=4)) ? (1) : (
                ((x>=5) && (x<=7)) ? (2) : (
                ((x>=8) && (x<=10)) ? (3) : (
                ((x>=11) && (x<=13)) ? (4) : (0)
                )));
      }
      if((direction==0) || (field==0)) return false;
      switch(field) {
        case 1: {
          this.sensor_range(this.sensor_range()+direction);
          area_ = null;
          ModAuxiliaries.playerMessage(player, "switch sensor range: " + Integer.toString(sensor_range_));
          break;
        }
        case 2: {
          this.sensor_entity_threshold(sensor_entity_threshold() + direction);
          ModAuxiliaries.playerMessage(player, "switch entity threshold: " + Integer.toString(sensor_entity_count_threshold_));
          break;
        }
        case 3: {
          this.filter(this.filter() + direction);
          ModAuxiliaries.playerMessage(player, "switch entity type: " + filter_class_names[filter_]);
          break;
        }
        case 4: {
          this.on_power(this.on_power() + direction);
          if(this.on_power() < 1) this.on_power(1);
          ModAuxiliaries.playerMessage(player, "switch power: " + Integer.toString(this.on_power()));
          break;
        }
      }
      this.markDirty();
      return true;
    }

    @Override
    public void update() {
      if((!hasWorld()) || (getWorld().isRemote) || (--update_timer_ > 0)) return;
      update_timer_ = update_interval_;
      IBlockState state = getWorld().getBlockState(getPos());
      if((state==null) || (!(state.getBlock() instanceof AutoSwitchBlock))) return;
      AutoSwitchBlock block = (AutoSwitchBlock)(state.getBlock());
      // initialisations
      if(update_interval_==0) {
        if((block.config & SWITCH_CONFIG_SENSOR_LINEAR) != 0) {
          update_interval_ = ModConfig.autoswitch_linear_update_interval;
        } else {
          update_interval_ = ModConfig.autoswitch_volumetric_update_interval;
        }
      }
      if(area_ == null) {
        int size = sensor_range();
        AxisAlignedBB range_bb;
        if((block.config & SWITCH_CONFIG_SENSOR_VOLUME) != 0) {
          final int yy = ((size/2)<2) ? 2 : (size/2);
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
      if(this.off_timer() > update_interval_) {
        active = true; // no need to ray trace, it's anyway on at the next update.
      } else {
        List<Entity> hits = world.getEntitiesWithinAABB(filter_class(), area_);
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
      if(active) {
        this.off_timer_reset(1+(hold_time_+(update_interval_/2))/update_interval_);
        if(!state.getValue(POWERED)) {
          world.setBlockState(pos, state.withProperty(POWERED, true), 1|2);
          block.power_on_sound.play(world, pos);
          world.notifyNeighborsOfStateChange(pos, block, false);
          world.notifyNeighborsOfStateChange(pos.offset(state.getValue(FACING).getOpposite()), block, false);
        }
      } else if(state.getValue(POWERED)) {
        if(this.off_timer_tick() <= 0) {
          world.setBlockState(pos, state.withProperty(POWERED, false));
          block.power_off_sound.play(world, pos);
          world.notifyNeighborsOfStateChange(pos, block, false);
          world.notifyNeighborsOfStateChange(pos.offset(state.getValue(FACING).getOpposite()), block, false);
        }
      }
    }
  }
}
