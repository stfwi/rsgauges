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
import wile.rsgauges.ModBlocks;
import wile.rsgauges.ModResources;
import wile.rsgauges.ModRsGauges;
import wile.rsgauges.blocks.RsBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
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

public class SwitchBlock extends RsBlock implements ModBlocks.Colors.ColorTintSupport {

  public static final long SWITCH_DATA_POWERED_POWER_MASK       = 0x000000000000000fl;
  public static final long SWITCH_DATA_UNPOWERED_POWER_MASK     = 0x00000000000000f0l;
  public static final long SWITCH_DATA_INVERTED                 = 0x0000000000000100l;
  public static final long SWITCH_DATA_WEAK                     = 0x0000000000000200l;
  public static final long SWITCH_DATA_NOOUTPUT                 = 0x0000000000000400l;
  public static final long SWITCH_CONFIG_INVERTABLE             = 0x0000000000001000l;
  public static final long SWITCH_CONFIG_WEAKABLE               = 0x0000000000002000l;
  public static final long SWITCH_CONFIG_POWER_SETTABLE         = 0x0000000000004000l;
  public static final long SWITCH_CONFIG_BISTABLE               = 0x0000000000008000l;
  public static final long SWITCH_CONFIG_PULSE                  = 0x0000000000010000l;
  public static final long SWITCH_CONFIG_PULSE_EXTENDABLE       = 0x0000000000020000l;
  public static final long SWITCH_CONFIG_LCLICK_RESETTABLE      = 0x0000000000040000l;
  public static final long SWITCH_CONFIG_TOUCH_CONFIGURABLE     = 0x0000000000080000l;
  public static final long SWITCH_CONFIG_AUTOMATIC              = 0x0000000000100000l;
  public static final long SWITCH_CONFIG_SENSOR_VOLUME          = 0x0000000000200000l;
  public static final long SWITCH_CONFIG_SENSOR_LINEAR          = 0x0000000000400000l;
  public static final long SWITCH_CONFIG_FLOOR_MOUNT            = 0x0000000000800000l;
  public static final long SWITCH_CONFIG_PROJECTILE_SENSE_ON    = 0x0000000001000000l;
  public static final long SWITCH_CONFIG_PROJECTILE_SENSE_OFF   = 0x0000000002000000l;
  public static final long SWITCH_CONFIG_PROJECTILE_SENSE       = SWITCH_CONFIG_PROJECTILE_SENSE_ON|SWITCH_CONFIG_PROJECTILE_SENSE_OFF;
  public static final long SWITCH_CONFIG_HOPPER_MOUNTBALE       = 0x0000000004000000l;
  public static final long SWITCH_CONFIG_SENSOR_LIGHT           = 0x0000000008000000l;
  public static final long SWITCH_CONFIG_TIMER_DAYTIME          = 0x0000000010000000l;
  public static final long SWITCH_CONFIG_SENSOR_RAIN            = 0x0000000020000000l;
  public static final long SWITCH_CONFIG_SENSOR_LIGHTNING       = 0x0000000040000000l;
  public static final long SWITCH_CONFIG_SENSOR_ENVIRONMENTAL   = SWITCH_CONFIG_SENSOR_LIGHT|SWITCH_CONFIG_TIMER_DAYTIME|SWITCH_CONFIG_SENSOR_RAIN|SWITCH_CONFIG_SENSOR_LIGHTNING;
  public static final long SWITCH_CONFIG_TIMER_INTERVAL         = 0x0000000080000000l;
  public static final long SWITCH_CONFIG_TRANSLUCENT            = 0x0000000100000000l;
  public static final long SWITCH_CONFIG_PULSETIME_CONFIGURABLE = 0x0000000200000000l;
  public static final long SWITCH_CONFIG_FAINT_LIGHTSOURCE      = 0x0000000400000000l;
  public static final long SWITCH_CONFIG_COLOR_TINT_SUPPORT     = 0x0000000800000000l;

  public static final int SWITCH_DATA_SVD_ACTIVE_TIME_MASK      = 0x000000ff;
  public static final int SWITCH_DATA_SVD_COLOR_MASK            = 0x00000f00;
  public static final int SWITCH_DATA_SVD_COLOR_SHIFT           = 8;

  public static final int base_tick_rate = 2;
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
  public int getLightValue(IBlockState state) { return (((config & SWITCH_CONFIG_FAINT_LIGHTSOURCE) != 0) && (ModAuxiliaries.isClientSide())) ? 1 : 0; }

  @Override
  public boolean hasColorMultiplierRGBA() { return (!ModConfig.without_switch_colortinting) && ((config & SWITCH_CONFIG_COLOR_TINT_SUPPORT) != 0); }

  @Override
  public int getColorMultiplierRGBA(@Nullable IBlockState state, @Nullable IBlockAccess world, @Nullable BlockPos pos) {
    if((pos==null) || (world==null)) return 0xffffffff;
    TileEntity te = world.getTileEntity(pos);
    return (te instanceof SwitchBlock.SwitchTileEntity)  ? (ModAuxiliaries.DyeColorFilters.byIndex[((SwitchBlock.SwitchTileEntity)te).color_tint() & 0xf]) : (0xffffffff);
  }

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
      world.scheduleUpdate(pos, this, this.base_tick_rate);
      if((config & SWITCH_CONFIG_PULSE_EXTENDABLE)==0) te.off_timer_reset();
      te.off_timer_extend();
    }
    return true;
  }

  @Override
  public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
    if((world.isRemote) || (player == null)) return;
    IBlockState state = world.getBlockState(pos);
    if(!(state.getBlock() instanceof SwitchBlock)) return;
    SwitchBlock.SwitchTileEntity te = getTe(world, pos);
    if(te == null) return;
    RsBlock.WrenchActivationCheck ck = RsBlock.WrenchActivationCheck.onBlockActivatedCheck(world, pos, null, player, null, null, 0, 0, 0);
    if((ck.redstone > 0) && (((SwitchBlock)state.getBlock()).config & SWITCH_CONFIG_PULSETIME_CONFIGURABLE) != 0) {
      if(!ModConfig.without_pulsetime_config) {
        te.active_time(ck.redstone);
        ModAuxiliaries.playerMessage(player, ModAuxiliaries.localize("Pulse time") + ": "
            + Double.toString( (((double)(base_tick_rate)*te.active_time()))/20 )
            + "s (" + Integer.toString(base_tick_rate*te.active_time()) + " ticks)"
        );
        return;
      }
    } else if((ck.dye >= 0) && (((SwitchBlock)state.getBlock()).config & SWITCH_CONFIG_COLOR_TINT_SUPPORT) != 0) {
      if((!ModConfig.without_switch_colortinting) && (ck.dye <= 15)) {
        te.color_tint(ck.dye);
        world.markAndNotifyBlock(pos, null, state, state, 1|2|4|16);
        ModAuxiliaries.playerMessage(player, ModAuxiliaries.localize("Tinted ") + ": "
            + ModAuxiliaries.localize(ModAuxiliaries.DyeColorFilters.nameByIndex[ck.dye & 0xf])
        );
        return;
      }
    }
    if(ck.wrenched && te.click_config(this)) {
      ModAuxiliaries.playerMessage(player, te.toString()); // click-config accepted, nothing to do here.
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
  public int tickRate(World world) { return 1; }

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
    if(((config & SWITCH_CONFIG_BISTABLE)!=0) || (world.isRemote) || (!state.getValue(POWERED))) return;
    SwitchBlock.SwitchTileEntity te = getTe(world, pos);
    if((te!=null) && (te.off_timer_tick() > 0) && (!world.isUpdateScheduled(pos, this)) ) { world.scheduleUpdate(pos, this, 1); return; }
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
    protected int svd_ = 0; // encoded value data
    protected long click_config_time_lastclicked_ = 0;

    @Override
    public void writeNbt(NBTTagCompound nbt, boolean updatePacket) {
      nbt.setInteger("scd", scd_);
      nbt.setInteger("svd", svd_);
    }

    @Override
    public void readNbt(NBTTagCompound nbt, boolean updatePacket)  {
      int previous_svd = svd_;
      scd_ = nbt.getInteger("scd");
      svd_ = nbt.getInteger("svd");
      if(!updatePacket) {
        if(scd_==0) reset();
      } else {
        if((svd_ & SWITCH_DATA_SVD_COLOR_MASK) != (previous_svd & SWITCH_DATA_SVD_COLOR_MASK)) {
          if((getWorld() != null) && (getWorld().isRemote)) {
            world.markBlockRangeForRenderUpdate(getPos(), getPos());
          }
        }
      }
    }

    @Override
    protected void setWorldCreate(World world) { this.reset(world); }

    public int svd()                        { return svd_; }
    public int scd()                        { return scd_; }
    public int off_timer()                  { return off_timer_; }
    public void off_timer_reset()           { off_timer_ = 0; }
    public int off_timer_tick()             { return  ((--off_timer_ <= 0) ? 0 : off_timer_); }
    public int active_time()                { return ((svd_ & SWITCH_DATA_SVD_ACTIVE_TIME_MASK) >> 0); }
    public void active_time(int t)          { svd_ = (svd_ & (~SWITCH_DATA_SVD_ACTIVE_TIME_MASK)) | ((t & SWITCH_DATA_SVD_ACTIVE_TIME_MASK) << 0); }
    public void off_timer_reset(int preset) { if(preset > 0) off_timer_ = preset; else off_timer_ = 0; }

    public void off_timer_extend() {
      if(active_time() > 0) off_timer_ = (active_time()*base_tick_rate)-1;
      else if(off_timer_ > (190/base_tick_rate)) off_timer_  = 400/base_tick_rate;
      else if(off_timer_ > ( 90/base_tick_rate)) off_timer_  = 200/base_tick_rate;
      else if(off_timer_ > ( 30/base_tick_rate)) off_timer_  = 100/base_tick_rate;
      else if(off_timer_ > (  1/base_tick_rate)) off_timer_  =  60/base_tick_rate;
      else off_timer_ = 40/base_tick_rate;
    }

    public int color_tint()           { return ((svd_ & ((int)SWITCH_DATA_SVD_COLOR_MASK)) >> SWITCH_DATA_SVD_COLOR_SHIFT); }
    public int on_power()             { return ((scd_ & ((int)SWITCH_DATA_POWERED_POWER_MASK)) >> 0); }
    public int off_power()            { return ((scd_ & ((int)SWITCH_DATA_UNPOWERED_POWER_MASK)) >> 8); }
    public boolean inverted()         { return ((scd_ & ((int)SWITCH_DATA_INVERTED)) != 0); }
    public boolean weak()             { return ((scd_ & ((int)SWITCH_DATA_WEAK)) != 0); }
    public boolean nooutput()         { return ((scd_ & ((int)SWITCH_DATA_NOOUTPUT)) != 0); }

    public void color_tint(int tint)  { svd_ = (svd_ & ~((int)SWITCH_DATA_SVD_COLOR_MASK)) | ( (tint<<SWITCH_DATA_SVD_COLOR_SHIFT) & SWITCH_DATA_SVD_COLOR_MASK); }
    public void on_power(int p)       { scd_ = (scd_ & ~((int)SWITCH_DATA_POWERED_POWER_MASK)) | (int)(((p<0)?0:((p>15)?(15):(p)) & ((int)SWITCH_DATA_POWERED_POWER_MASK))<<0); }
    public void off_power(int p)      { scd_ = (scd_ & ~((int)SWITCH_DATA_UNPOWERED_POWER_MASK)) | ((int)(((p<0)?0:((p>15)?(15):(p)) & 0x000f)<<8) & ((int)SWITCH_DATA_UNPOWERED_POWER_MASK)); }
    public void inverted(boolean val) { if(val) scd_ |= ((int)SWITCH_DATA_INVERTED); else scd_ &= ~((int)SWITCH_DATA_INVERTED); }
    public void weak(boolean val)     { if(val) scd_ |= ((int)SWITCH_DATA_WEAK); else scd_ &= ~((int)SWITCH_DATA_WEAK); }
    public void nooutput(boolean val) { if(val) scd_ |= ((int)SWITCH_DATA_NOOUTPUT); else scd_ &= ~((int)SWITCH_DATA_NOOUTPUT); }

    public void reset() { reset(getWorld()); }

    public void reset(World world) {
      off_timer_ = 0;
      click_config_time_lastclicked_ = 0;
      svd_ = 0;
      try {
        // If the world is not yet available or the block not loaded let it run with the head
        // into the wall and say 0.
        final int current_scd = scd_;
        scd_ = (int)((((SwitchBlock)(world.getBlockState(getPos()).getBlock())).config) & 0x0fff);
        if((scd_ & 0xff)==0) scd_ |= 15; // implicitly set on-power if not set yet
        if(current_scd != scd_) this.markDirty();
      } catch(Exception e) {
        // set the on-power to default 15, but no other settings.
        scd_ = 15;
      }
    }

    /**
     * Returns the current power depending on the block settings.
     */
    public int power(IBlockState state, boolean strong) {
      return nooutput() ? (0) : ((strong && weak()) ? (0) : ( (inverted() == state.getValue(POWERED)) ? off_power() : on_power() ));
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

      // Check double click
      {
        long t = System.currentTimeMillis();
        boolean multiclicked = ((t-click_config_time_lastclicked_) > 0) && ((t-click_config_time_lastclicked_) < ModConfig.config_left_click_timeout);
        click_config_time_lastclicked_ = multiclicked ? 0 : t;
        if(!multiclicked) return false;
      }

      // Settings are changed by cycling through available states.
      if((block.config & (SWITCH_CONFIG_INVERTABLE|SWITCH_CONFIG_WEAKABLE))==(SWITCH_CONFIG_INVERTABLE|SWITCH_CONFIG_WEAKABLE)) {
        switch(((weak() ? 1:0) | (inverted() ? 2:0)) | (nooutput() ? 4:0)) {
          case  0: weak(true);  inverted(false); nooutput(false); break;
          case  1: weak(false); inverted(true);  nooutput(false); break;
          case  2: weak(true);  inverted(true);  nooutput(false); break;
          case  3: weak(false); inverted(false); nooutput(true);  break;
          default: weak(false); inverted(false); nooutput(false); break;
        }
      } else if((block.config & SWITCH_CONFIG_WEAKABLE) != 0) {
        switch((weak() ? 1:0) | (nooutput() ? 2:0)) {
          case  0: weak(true);  inverted(false); nooutput(false); break;
          case  1: weak(false); inverted(false); nooutput(true);  break;
          default: weak(false); inverted(false); nooutput(false); break;
        }
      } else if((block.config & SWITCH_CONFIG_INVERTABLE) != 0) {
        switch((inverted() ? 1:0) | (nooutput() ? 2:0)) {
          case  0: weak(false); inverted(true);  nooutput(false); break;
          case  1: weak(false); inverted(false); nooutput(true);  break;
          default: weak(false); inverted(false); nooutput(false); break;
        }
      }
      if(ModConfig.without_switch_nooutput) nooutput(false); // override last option, where not weak and not inverted is already set.
      this.markDirty();
      return true;
    }

    @Override
    public String toString() {
      return ModAuxiliaries.localize("switch power") + ":" + Integer.toString(on_power())
      + ((off_power()>0) ? ("/"+ ModAuxiliaries.localize("off") + ":" + Integer.toString(off_power())) : (""))
      + (nooutput() ? (" " + ModAuxiliaries.localize("no output")) : ((weak() ? (" " + ModAuxiliaries.localize("weak")) : (" " + ModAuxiliaries.localize("strong")))) )
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
