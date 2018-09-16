/**
 * @file SwitchBlock.java
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
import net.minecraft.block.BlockHopper;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;
import javax.annotation.Nullable;

public class SwitchBlock extends RsBlock {

  public static final long SWITCH_DATA_POWERED_POWER_MASK    = 0x000000000fl;
  public static final long SWITCH_DATA_UNPOWERED_POWER_MASK  = 0x00000000f0l;
  public static final long SWITCH_DATA_INVERTED              = 0x0000000100l;
  public static final long SWITCH_DATA_WEAK                  = 0x0000000200l;
  public static final long SWITCH_CONFIG_INVERTABLE          = 0x0000001000l;
  public static final long SWITCH_CONFIG_WEAKABLE            = 0x0000002000l;
  public static final long SWITCH_CONFIG_POWER_SETTABLE      = 0x0000004000l;
  public static final long SWITCH_CONFIG_BISTABLE            = 0x0000008000l;
  public static final long SWITCH_CONFIG_PULSE               = 0x0000010000l;
  public static final long SWITCH_CONFIG_PULSE_EXTENDABLE    = 0x0000020000l;
  public static final long SWITCH_CONFIG_LCLICK_RESETTABLE   = 0x0000040000l;
  public static final long SWITCH_CONFIG_TOUCH_CONFIGURABLE  = 0x0000080000l;
  public static final long SWITCH_CONFIG_AUTOMATIC           = 0x0000100000l;
  public static final long SWITCH_CONFIG_SENSOR_VOLUME       = 0x0000200000l;
  public static final long SWITCH_CONFIG_SENSOR_LINEAR       = 0x0000400000l;
  public static final long SWITCH_CONFIG_FLOOR_MOUNT         = 0x0000800000l;
  public static final long SWITCH_CONFIG_PROJECTILE_SENSE_ON = 0x0001000000l;
  public static final long SWITCH_CONFIG_PROJECTILE_SENSE_OFF= 0x0002000000l;
  public static final long SWITCH_CONFIG_PROJECTILE_SENSE    = SWITCH_CONFIG_PROJECTILE_SENSE_ON|SWITCH_CONFIG_PROJECTILE_SENSE_OFF;
  public static final long SWITCH_CONFIG_HOPPER_MOUNTBALE    = 0x0004000000l;
  public static final long SWITCH_CONFIG_SENSOR_LIGHT        = 0x0008000000l;
  public static final long SWITCH_CONFIG_TIMER_DAYTIME       = 0x0010000000l;
  public static final long SWITCH_CONFIG_SENSOR_RAIN         = 0x0020000000l;
  public static final long SWITCH_CONFIG_SENSOR_LIGHTNING    = 0x0040000000l;
  public static final long SWITCH_CONFIG_SENSOR_ENVIRONMENTAL= SWITCH_CONFIG_SENSOR_LIGHT|
                           SWITCH_CONFIG_TIMER_DAYTIME|SWITCH_CONFIG_SENSOR_RAIN|
                           SWITCH_CONFIG_SENSOR_LIGHTNING;
  public static final long SWITCH_CONFIG_TIMER_INTERVAL      = 0x0080000000l;
  public static final long SWITCH_CONFIG_TRANSLUCENT         = 0x0100000000l;

  public final long config;

  @Nullable protected final AxisAlignedBB unrotated_bb_powered;
  @Nullable protected final ModResources.BlockSoundEvent power_on_sound;
  @Nullable protected final ModResources.BlockSoundEvent power_off_sound;

  public static final PropertyBool POWERED = PropertyBool.create("powered");

  public SwitchBlock(String registryName, AxisAlignedBB unrotatedBBUnpowered, AxisAlignedBB unrotatedBBPowered, long config, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound) {
    super(registryName, unrotatedBBUnpowered);
    this.config = config;
    if((powerOnSound==null) && (powerOffSound==null)) {
      powerOnSound = new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.3f, 0.92f);
      powerOffSound = new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.3f, 0.82f);
    }
    unrotated_bb_powered = unrotatedBBPowered;
    power_on_sound = powerOnSound;
    power_off_sound = powerOffSound;
    setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, false));
  }

  public SwitchBlock(String registryName, AxisAlignedBB unrotatedBBUnpowered, AxisAlignedBB unrotatedBBPowered, long config) { this(registryName, unrotatedBBUnpowered, unrotatedBBPowered, config, null, null); }
  public SwitchBlock(String registryName, AxisAlignedBB unrotatedBB, long config) { this(registryName, unrotatedBB, null, config, null, null); }

  @Override
  public AxisAlignedBB getUnrotatedBB(IBlockState state) { return ((unrotated_bb_powered==null) || (state==null) || (state.getBlock()!=this) || (!state.getValue(POWERED))) ? super.getUnrotatedBB() : unrotated_bb_powered; }

  @Override
  public boolean isWallMount() { return ((config & SWITCH_CONFIG_FLOOR_MOUNT) == 0); }

  @Override
  public boolean isFloorMount() { return ((config & SWITCH_CONFIG_FLOOR_MOUNT) != 0); }

  @SideOnly(Side.CLIENT)
  @Override
  public BlockRenderLayer getBlockLayer() { return ((config & SWITCH_CONFIG_TRANSLUCENT) != 0) ? (BlockRenderLayer.TRANSLUCENT) : (BlockRenderLayer.CUTOUT); }

  @Override
  public IBlockState getStateFromMeta(int meta) { return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 0x7)).withProperty(POWERED, ((meta & 0x8) != 0)); }

  @Override
  public int getMetaFromState(IBlockState state) { return (state.getValue(FACING).getIndex() & 0x07) | ((state.getValue(POWERED)) ? 0x8 : 0x0); }

  @Override
  protected BlockStateContainer createBlockState() { return new BlockStateContainer(this, FACING, POWERED); }

  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) { return state; }

  @Override
  public boolean canProvidePower(IBlockState state) { return true; }

  @Override
  public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
    return (side==null) || ((side)==(EnumFacing.UP)) || ((side)==(state.getValue(FACING)));
  }

  @Override
  public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    if((!(world instanceof World)) || (side != ((this.isFloorMount()) ? ((state.getValue(FACING)).getOpposite()) : (state.getValue(FACING))))) return 0;
    SwitchBlock.SwitchTileEntity te = getTe((World)world, pos);
    return (te==null) ? 0 : te.power(state, false);
  }

  @Override
  public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    if((!(world instanceof World)) || (side != ((this.isFloorMount()) ? ((state.getValue(FACING)).getOpposite()) : (state.getValue(FACING))))) return 0;
    SwitchBlock.SwitchTileEntity te = getTe((World)world, pos);
    return (te==null) ? 0 : te.power(state, true);
  }

  protected void notifyNeighbours(World world, BlockPos pos, IBlockState state) {
    world.notifyNeighborsOfStateChange(pos, this, false);
    if(!this.isFloorMount()) {
      world.notifyNeighborsOfStateChange(pos.offset(state.getValue(FACING).getOpposite()), this, false);
    } else {
      world.notifyNeighborsOfStateChange(pos.offset(state.getValue(FACING)), this, false);
      world.notifyNeighborsOfStateChange(pos.down(), this, false);
    }
  }

  @Override
  public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
    if((config & SWITCH_CONFIG_HOPPER_MOUNTBALE)==0) {
      return super.canPlaceBlockOnSide(world, pos, side);
    } else {
      return super.canPlaceBlockOnSide(world, pos, side, (Block block)->{
        return (block instanceof BlockHopper);
      }, null);
    }
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    SwitchBlock.SwitchTileEntity te = getTe(world, pos);
    if(te != null) te.reset();
    if(!super.onBlockPlacedByCheck(world, pos, state, placer, stack)) return;
    world.setBlockState(pos, world.getBlockState(pos).withProperty(POWERED, false), 1|2);
    notifyNeighbours(world, pos, state);
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    return onBlockActivated(world, pos, state, player);
  }

  @Override
  public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
    if(world.isRemote || ((this.config & SWITCH_CONFIG_PROJECTILE_SENSE)==0) || (!(entity instanceof IProjectile))) return;
    if(state.getValue(POWERED)) {
      if((config & SWITCH_CONFIG_PROJECTILE_SENSE_OFF)==0) return;
    } else {
      if((config & SWITCH_CONFIG_PROJECTILE_SENSE_ON)==0) return;
    }
    onBlockActivated(world, pos, state, null);
  }

  protected boolean onBlockActivated(World world, BlockPos pos, IBlockState state, @Nullable EntityPlayer player) {
    if(world.isRemote) return true;
    SwitchBlock.SwitchTileEntity te = getTe(world, pos);
    if(te == null) return true;
    if((player != null) && te.click_config(null)) return true;
    if((config & SWITCH_CONFIG_BISTABLE)!=0) {
      state = state.cycleProperty(POWERED);
      world.setBlockState(pos, state, 1|2);
      if(!state.getValue(POWERED)) power_off_sound.play(world, pos); else power_on_sound.play(world, pos);
    } else {
      world.setBlockState(pos, state.withProperty(POWERED, true), 1|2);
      power_on_sound.play(world, pos);
    }
    notifyNeighbours(world, pos, state);
    if((config & SWITCH_CONFIG_PULSE)!=0) {
      world.scheduleUpdate(pos, this, this.tickRate(world));
      if((config & SWITCH_CONFIG_PULSE_EXTENDABLE)==0) te.off_timer_reset();
      te.off_timer_extend();
    }
    return true;
  }

  @Override
  public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
    if(world.isRemote) return;
    IBlockState state = world.getBlockState(pos);
    if(!(state.getBlock() instanceof SwitchBlock)) return;
    SwitchBlock.SwitchTileEntity te = getTe(world, pos);
    if(te == null) return;
    if(RsBlock.WrenchActivationCheck.wrenched(player) && te.click_config(this)) {
      ModAuxiliaries.playerMessage(player, te.toString());
    } else if((config & (SWITCH_CONFIG_LCLICK_RESETTABLE))!=0) {
      te.off_timer_reset();
      if(!state.getValue(POWERED)) return;
      world.setBlockState(pos, state.withProperty(POWERED, false), 1|2);
      power_off_sound.play(world, pos);
    }
    notifyNeighbours(world, pos, state);
  }

  @Override
  public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
    SwitchBlock.SwitchTileEntity te = getTe(world, pos);
    if(te != null) te.reset();
    world.setBlockState(pos, world.getBlockState(pos).withProperty(POWERED, false), 1|2);
    notifyNeighbours(world, pos, state);
    return super.removedByPlayer(state, world, pos, player, willHarvest);
  }

  @Override
  public int tickRate(World world) { return 5; }

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
    if(((config & SWITCH_CONFIG_BISTABLE)!=0) || (world.isRemote) || (!state.getValue(POWERED))) return;
    SwitchBlock.SwitchTileEntity te = getTe(world, pos);
    if((te!=null) && (te.off_timer_tick() > 0)) { world.scheduleUpdate(pos, this, this.tickRate(world)); return; }
    world.setBlockState(pos, state.withProperty(POWERED, false));
    power_off_sound.play(world, pos);
    notifyNeighbours(world, pos, state);
    world.markBlockRangeForRenderUpdate(pos, pos);
  }

  @Override
  public boolean hasTileEntity(IBlockState state) { return true; }

  @Override
  public TileEntity createTileEntity(World world, IBlockState state) { return new SwitchBlock.SwitchTileEntity(); }

  public SwitchBlock.SwitchTileEntity getTe(World world, BlockPos pos) {
    TileEntity te = world.getTileEntity(pos);
    if((!(te instanceof SwitchBlock.SwitchTileEntity))) return null;
    return (SwitchBlock.SwitchTileEntity)te;
  }

  /**
   * Tile entity
   */
  public static class SwitchTileEntity extends RsTileEntity<SwitchBlock> {
    protected int off_timer_ = 0;
    protected int scd_ = 0; // encoded state data
    protected long click_config_time_lastclicked_ = 0;

    @Override
    public void writeNbt(NBTTagCompound nbt, boolean updatePacket) { nbt.setInteger("scd", scd_); }

    @Override
    public void readNbt(NBTTagCompound nbt, boolean updatePacket)  { scd_ = nbt.getInteger("scd"); if((!updatePacket) && (scd_==0)) reset(); }

    @Override
    protected void setWorldCreate(World world) { this.reset(world); }

    public int scd() { return scd_; }
    public int off_timer() { return off_timer_; }
    public void off_timer_reset() { off_timer_ = 0; }
    public void off_timer_reset(int preset) { off_timer_ = (preset < 0) ? 0 : preset; }
    public int off_timer_tick() { return  ((--off_timer_ <= 0) ? 0 : off_timer_); }

    public void off_timer_extend() {
      if(off_timer_ > 35) off_timer_ = 80;
      else if(off_timer_ > 15) off_timer_ = 40;
      else if(off_timer_ > 8) off_timer_ = 20;
      else if(off_timer_ > 3) off_timer_ = 10;
      else off_timer_ = 5;
    }

    public int on_power()             { return ((scd_ & 0x000f) >> 0); }
    public int off_power()            { return ((scd_ & 0x00f0) >> 8); }
    public boolean inverted()         { return ((scd_ & 0x0100) != 0); }
    public boolean weak()             { return ((scd_ & 0x0200) != 0); }

    public void on_power(int p)       { scd_ = (scd_ & ~((int)0x000f)) | (int)(((p<0)?0:((p>15)?(15):(p)) & 0x000f)<<0); }
    public void off_power(int p)      { scd_ = (scd_ & ~((int)0x00f0)) | (int)(((p<0)?0:((p>15)?(15):(p)) & 0x000f)<<8); }
    public void inverted(boolean val) { if(val) scd_ |= ((int)0x0100); else scd_ &= ~((int)0x0100); }
    public void weak(boolean val)     { if(val) scd_ |= ((int)0x0200); else scd_ &= ~((int)0x0200); }

    public void reset() { reset(getWorld()); }

    public void reset(World world) {
      off_timer_ = 0;
      click_config_time_lastclicked_ = 0;
      try {
        // If the world is not yet available or the block not loaded let it run with the head
        // into the wall and say 0.
        final int current_scd = scd_;
        scd_ = (int)((((SwitchBlock)(world.getBlockState(getPos()).getBlock())).config) & 0x0fff);
        if((scd_ & 0xff)==0) scd_ |= 15; // implicitly set on-power if not set yet
        if(current_scd != scd_) this.markDirty();
      } catch(Exception e) {
        scd_ = 15;
      }
    }

    /**
     * Returns the current power depending on the block settings.
     */
    public int power(IBlockState state, boolean strong) {
      return (strong && weak()) ? (0) : ( (inverted() == state.getValue(POWERED)) ? off_power() : on_power() );
    }

    /**
     * Configuration via block right clicking, x and z are normalised
     * coords of the xy position clicked on the main facing, values
     * 0 to 16. Shall return true if the block has to be updated and
     * re-rendered.
     */
    public boolean activation_config(@Nullable SwitchBlock block, @Nullable EntityPlayer player, double x, double y) { return false; }

    /**
     * Left click configuration
     */
    public boolean click_config(@Nullable SwitchBlock block) {
      if(block==null) { click_config_time_lastclicked_ = 0; return false; }

      // Check multiclick
      {
        long t = System.currentTimeMillis();
        boolean multiclicked = ((t-click_config_time_lastclicked_) > 0) && ((t-click_config_time_lastclicked_) < ModConfig.config_left_click_timeout);
        click_config_time_lastclicked_ = t;
        if(!multiclicked) return false;
      }

      // Not openable: Settings are changed by cycling through available states.
      if((block.config & (SWITCH_CONFIG_INVERTABLE|SWITCH_CONFIG_WEAKABLE))==(SWITCH_CONFIG_INVERTABLE|SWITCH_CONFIG_WEAKABLE)) {
        if((!weak()) && (!inverted())) {
          weak(true);
        } else if((weak()) && (!inverted())) {
          inverted(true);
        } else if((weak()) && (inverted())) {
          weak(false);
        } else {
          weak(false); inverted(false);
        }
      } else if((block.config & SWITCH_CONFIG_WEAKABLE) != 0) {
        weak(!weak());
      } else if((block.config & SWITCH_CONFIG_INVERTABLE) != 0) {
        inverted(!inverted());
      }
      this.markDirty();
      return true;
    }

    @Override
    public String toString() {
      return ModAuxiliaries.localize("switch power") + ":" + Integer.toString(on_power())
      + ((off_power()>0) ? ("/"+ ModAuxiliaries.localize("off") + ":" + Integer.toString(off_power())) : (""))
      + (weak() ? (" " + ModAuxiliaries.localize("weak")) : (" " + ModAuxiliaries.localize("strong")))
      + (inverted() ? (" " + ModAuxiliaries.localize("inverted")) : "");
    }

    public String toString(SwitchBlock block) {
      return this.toString()
          + (((block.config & SWITCH_CONFIG_BISTABLE) != 0) ? " bistable":"")
          + (((block.config & SWITCH_CONFIG_WEAKABLE) != 0) ? " weakable":"")
          + (((block.config & SWITCH_CONFIG_INVERTABLE) != 0) ? " invertable":"")
          + (((block.config & SWITCH_CONFIG_POWER_SETTABLE) != 0) ? " powersettable":"")
          ;
    }
  }
}
