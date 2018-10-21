/**
 * @file ContactSwitchBlock.java
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
import net.minecraft.init.SoundEvents;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import wile.rsgauges.blocks.RsBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.monster.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;


public class ContactSwitchBlock extends SwitchBlock {

  public ContactSwitchBlock(String registryName, AxisAlignedBB unrotatedBB, long config, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound) {
    super(registryName, unrotatedBB, null, config, powerOnSound, powerOffSound);
  }

  public ContactSwitchBlock(String registryName, AxisAlignedBB unrotatedBB, long config) {
    super(registryName, unrotatedBB, config);
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    if(world.isRemote) return true;
    ContactSwitchBlock.ContactSwitchTileEntity te = getTe(world, pos); if(te == null) return true;
    te.click_config(null);
    if((config & SWITCH_CONFIG_TOUCH_CONFIGURABLE)==0) return true;
    RsBlock.WrenchActivationCheck wac = RsBlock.WrenchActivationCheck.onBlockActivatedCheck(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    if((wac.touch_configured) && (wac.wrenched) && (state.getBlock() instanceof ContactSwitchBlock)) {
      te.activation_config((ContactSwitchBlock)state.getBlock(), player, wac.x, wac.y);
    }
    return true;
  }

  @Override
  public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    if((side != (state.getValue(FACING).getOpposite())) && (side != EnumFacing.UP)) return 0;
    ContactSwitchBlock.ContactSwitchTileEntity te = getTe((World)world, pos);
    return (te==null) ? 0 : te.power(state, false);
  }

  @Override
  public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    if((side != (state.getValue(FACING).getOpposite())) && (side != EnumFacing.UP)) return 0;
    SwitchBlock.SwitchTileEntity te = getTe((World)world, pos);
    return (te==null) ? 0 : te.power(state, true);
  }

  @Override
  public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
    return (side==null) || ((side)==(EnumFacing.UP)) || ((side)==(state.getValue(FACING).getOpposite()));
  }

  @Override
  public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
    if(world.isRemote) return;
    ContactSwitchBlock.ContactSwitchTileEntity te = getTe(world, pos); if(te == null) return;
    boolean active = false;
    boolean powered = state.getValue(POWERED);

    if(powered && (te.off_timer() > 2)) {
      active = true; // anyway on at the next update.
    } else {
      List<Entity> hits = world.getEntitiesWithinAABB(te.filter_class(), new AxisAlignedBB(pos, pos.add(1,1,1)));
      if(hits.size() >= te.entity_count_threshold()) {
        if(te.high_sensitivity()) {
          active = true;
        } else {
          for(Entity e:hits) {
            if(!e.doesEntityNotTriggerPressurePlate()) {
              active = true;
              break;
            }
          }
        }
        if(active) te.off_timer_reset( (te.active_time()<=0) ? (20) : ((te.active_time()*base_tick_rate)+1) );
      }
    }
    if(active && (!powered)) {
      world.setBlockState(pos, state.withProperty(POWERED, true), 1|2);
      this.power_on_sound.play(world, pos);
      this.notifyNeighbours(world, pos, state);
    }
    if(!world.isUpdateScheduled(pos, this)) { world.scheduleUpdate(pos, this, 1); }
  }

  @Override
  public TileEntity createTileEntity(World world, IBlockState state) { return new ContactSwitchBlock.ContactSwitchTileEntity(); }

  @Override
  public ContactSwitchBlock.ContactSwitchTileEntity getTe(World world, BlockPos pos) {
    TileEntity te = world.getTileEntity(pos);
    if((!(te instanceof ContactSwitchBlock.ContactSwitchTileEntity))) return null;
    return (ContactSwitchBlock.ContactSwitchTileEntity)te;
  }

  /**
   * Tile entity
   */
  public static final class ContactSwitchTileEntity extends SwitchBlock.SwitchTileEntity {
    public static final Class filter_classes[] = { Entity.class, EntityLivingBase.class, EntityPlayer.class, EntityMob.class, EntityAnimal.class, EntityVillager.class, EntityItem.class };
    public static final String filter_class_names[] = { "everything", "creatures", "players", "mobs", "animals", "villagers", "objects" };
    private static final int max_entity_count = 64;
    private boolean high_sensitivity_ = false;
    private int count_threshold_ = 1;
    private int filter_ = 0;

    public int filter() { return filter_; }
    public void filter(int sel) { filter_ = (sel<0) ? 0 : (sel >= filter_classes.length) ? (filter_classes.length-1) : sel; }
    public Class filter_class() { return (filter_<=0) ? (filter_classes[0]) : ((filter_ >= filter_classes.length) ? (filter_classes[filter_classes.length-1]) : filter_classes[filter_]); }
    public boolean high_sensitivity() { return high_sensitivity_; }
    public void high_sensitivity(boolean sel) { high_sensitivity_ = sel; }
    public int entity_count_threshold() { return count_threshold_; }
    public void entity_count_threshold(int sel) { count_threshold_ = ((sel<1) ? 1 : ((sel>=max_entity_count)) ? max_entity_count : sel); }

    @Override
    public void writeNbt(NBTTagCompound nbt, boolean updatePacket) {
      super.writeNbt(nbt, updatePacket);
      nbt.setInteger("filter", filter());
      nbt.setBoolean("highsensitive", high_sensitivity());
      nbt.setInteger("entitythreshold", entity_count_threshold());
    }

    @Override
    public void readNbt(NBTTagCompound nbt, boolean updatePacket)  {
      super.readNbt(nbt, updatePacket);
      this.filter(nbt.getInteger("filter"));
      this.high_sensitivity(nbt.getBoolean("highsensitive"));
      this.entity_count_threshold(nbt.getInteger("entitythreshold"));
    }

    @Override
    public void reset() { super.reset(); filter_=0; count_threshold_=1; high_sensitivity_=false; }

    @Override
    public boolean activation_config(@Nullable SwitchBlock block, @Nullable EntityPlayer player, double x, double y) {
      if(block == null) return false;
      int direction=0, field=0;
      // System.out.println("xy:" + Double.toString(x) + "," + Double.toString(y));
      if((block.config & (SWITCH_CONFIG_FLOOR_MOUNT)) != 0) {
        direction = ((y>=13) && (y<=15)) ? (1) : (((y>=10) && (y<=12)) ? (-1) : (0));
        field = ((x>=9.5) && (x<=10.1)) ? (1) : (
                ((x>=10.9) && (x<=11.7)) ? (2) : (
                ((x>=12.2) && (x<=13.0)) ? (3) : (
                ((x>=13.5) && (x<=14.2)) ? (4) : (0)
                )));
      }
      if((direction==0) || (field==0)) return false;
      switch(field) {
        case 1: {
          this.high_sensitivity(direction > 0);
          ModAuxiliaries.playerMessage(player, ModAuxiliaries.localize("switch weight threshold") + ": " + ModAuxiliaries.localize(this.high_sensitivity() ? "high sensitivity" : "normal sensitivity") );
          break;
        }
        case 2: {
          this.entity_count_threshold(this.entity_count_threshold() + direction);
          ModAuxiliaries.playerMessage(player, ModAuxiliaries.localize("switch entity threshold") + ": " + Integer.toString(this.entity_count_threshold()));
          break;
        }
        case 3: {
          this.filter(this.filter() + direction);
          ModAuxiliaries.playerMessage(player, ModAuxiliaries.localize("switch entity type") + ": " + ModAuxiliaries.localize(filter_class_names[filter_]));
          break;
        }
        case 4: {
          this.on_power(this.on_power() + direction);
          if(this.on_power() < 1) this.on_power(1);
          ModAuxiliaries.playerMessage(player, ModAuxiliaries.localize("switch power") + ": " + Integer.toString(this.on_power()));
          break;
        }
      }
      this.markDirty();
      return true;
    }
  }
}
