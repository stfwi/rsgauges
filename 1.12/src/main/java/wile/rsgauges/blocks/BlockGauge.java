/*
 * @file BlockGauge.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Basic class for blocks representing a redstone signal
 * display, measuring the redstone signal of the block
 * it is attached to (only the back not the sides or front).
 * The block has a "tickable" tile entity to ensure that
 * the gauge display is updated even if a block update event
 * was lost. Depending on the model/type additional constants
 * like power-to-light scaling are implemented, e.g. for LED
 * indicators.
 *
 */
package wile.rsgauges.blocks;

import wile.rsgauges.ModContent;
import wile.rsgauges.ModRsGauges;
import wile.rsgauges.detail.ModConfig;
import wile.rsgauges.detail.ModResources;
import wile.rsgauges.detail.ModAuxiliaries;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.DyeUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import javax.annotation.Nullable;


public class BlockGauge extends RsBlock implements ModContent.Colors.ColorTintSupport
{
  public static final long GAUGE_DATA_POWER_MASK            = 0x000000000000000fl;
  public static final int  GAUGE_DATA_POWER_SHIFT           = 0;
  public static final long GAUGE_DATA_LIGHT_MASK            = 0x00000000000000f0l;
  public static final int  GAUGE_DATA_LIGHT_SHIFT           = 4;
  public static final long GAUGE_DATA_BLINK_MASK            = 0x0000000000000f00l;
  public static final int  GAUGE_DATA_BLINK_SHIFT           = 8;
  public static final long GAUGE_DATA_COLOR_MASK            = 0x000000000000f000l;
  public static final int  GAUGE_DATA_COLOR_SHIFT           = 12;
  //
  public static final long GAUGE_CONFIG_COLOR_TINT_SUPPORT  = 0x0000000000010000l;
  public static final long GAUGE_CONFIG_TRANSLUCENT         = 0x0000000000020000l;

  public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
  public final long config;
  @Nullable public final ModResources.BlockSoundEvent power_on_sound;
  @Nullable public final ModResources.BlockSoundEvent power_off_sound;

  public BlockGauge(String registryName, AxisAlignedBB unrotatedBB, long config, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  {
    super(registryName, unrotatedBB, null);
    this.config = config;
    power_on_sound = powerOnSound;
    power_off_sound = powerOffSound;
    setDefaultState(getBlockStateWithPower(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH), 0));
  }

  public BlockGauge(String registryName, AxisAlignedBB unrotatedBB, long config)
  { this(registryName, unrotatedBB, config, null, null); }

  public BlockGauge(String registryName, AxisAlignedBB boundingBox)
  { this(registryName, boundingBox, 0, null, null); }

  // methods here until TE needed
  public int light_value()
  { return (int)((config & GAUGE_DATA_LIGHT_MASK) >> GAUGE_DATA_LIGHT_SHIFT); }

  public int blink_interval()
  { return (int)((config & GAUGE_DATA_BLINK_MASK) >> GAUGE_DATA_BLINK_SHIFT) * 200; }

  @Override
  public boolean isWallMount()
  { return true; }

  /**
   * Enables overriding to allow stripped value ranges for individual POWER block properties.
   * Returns the blockstate, where the corresponding POWER value property is set to the closest
   * value to `power`.
   */
  public IBlockState getBlockStateWithPower(IBlockState orig, int power)
  { return orig.withProperty(POWER, power); }

  /**
   * Enables overriding to allow stripped value ranges for individual POWER block properties.
   * Returns the int value of the block specific POWER property.
   */
  public int getBlockStatePower(IBlockState state)
  { return state.getValue(POWER); }

  /**
   * Enables overriding to allow stripped value ranges for individual POWER block properties.
   * Returns true if state power and passed power are identical.
   */
  public boolean cmpBlockStatePower(IBlockState state, int power)
  { return state.getValue(POWER) == power; }

  @Override
  @SideOnly(Side.CLIENT)
  public void initModel()
  { super.initModel(); }

  @Override
  public boolean hasColorMultiplierRGBA()
  { return (!ModConfig.optouts.without_color_tinting) && ((config & GAUGE_CONFIG_COLOR_TINT_SUPPORT)!=0); }

  @Override
  public int getColorMultiplierRGBA(@Nullable IBlockState state, @Nullable IBlockAccess world, @Nullable BlockPos pos)
  {
    if((pos==null) || (world==null)) return 0xffffffff;
    final TileEntityGauge te = getTe(world, pos);
    if(te==null) return 0xffffffff;
    return ModAuxiliaries.DyeColorFilters.lightTintByIndex(te.color_tint());
  }

  ///
  /// Forge / Minecraft overloads
  ///

  @Override
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getRenderLayer()
  { return (((config & GAUGE_CONFIG_TRANSLUCENT)!=0) || (light_value() > 0)) ? (BlockRenderLayer.TRANSLUCENT) : (BlockRenderLayer.CUTOUT); }

  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
  {
    final TileEntityGauge te = getTe(world, pos);
    return getBlockStateWithPower(super.getActualState(state, world, pos), (te==null) ? 0 : te.power());
  }

  @Override
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos)
  {
    if(!neighborChangedCheck(state, world, pos, neighborBlock, neighborPos)) return;
    final TileEntityGauge te = getTe(world, pos);
    if(te != null) te.reset_timer();
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
  { if(onBlockPlacedByCheck(world, pos, state, placer, stack)) world.scheduleBlockUpdate(pos, state.getBlock(), 0, 1); }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
  { return false; }

  @Override
  public void onBlockClicked(World world, BlockPos pos, EntityPlayer player)
  {
    if(world.isRemote) return;
    final IBlockState state = world.getBlockState(pos);
    final TileEntityGauge te = getTe(world, pos);
    final ItemStack item = player.getHeldItemMainhand();
    if((te==null) || (state==null) || (item==null)) return;
    if((DyeUtils.isDye(item)) && ((config & GAUGE_CONFIG_COLOR_TINT_SUPPORT) != 0) && (!ModConfig.optouts.without_color_tinting)) {
      int dye = DyeUtils.rawMetaFromStack(item);
      te.color_tint((dye<0)?(0):((dye>15)?15:dye));
      te.markDirty();
      world.markAndNotifyBlock(pos, null, state, state, 1|2|4|16);
      ModAuxiliaries.playerStatusMessage(player, ModAuxiliaries.localizable("switchconfig.tinting", null, new Object[]{ModAuxiliaries.localizable("switchconfig.tinting." + ModAuxiliaries.DyeColorFilters.nameByIndex[dye & 0xf], null)}));
    }
  }

  @Override
  @SuppressWarnings("deprecation")
  public int getLightValue(IBlockState state)
  { return (light_value() < 1) ? 0 : ((!ModAuxiliaries.isClientSide()) || (getBlockStatePower(state)>0)) ? (light_value()) : (0); }

  @Override
  public boolean getWeakChanges(IBlockAccess world, BlockPos pos)
  { return true; }

  @Override
  protected BlockStateContainer createBlockState()
  { return new BlockStateContainer(this, FACING, POWER); }

  @Override
  public boolean hasTileEntity(IBlockState state)
  { return true; }

  @Override
  public TileEntity createTileEntity(World world, IBlockState state)
  { return new TileEntityGauge(); }

  public TileEntityGauge getTe(IBlockAccess world, BlockPos pos)
  {
    final TileEntity te = world.getTileEntity(pos);
    return (te instanceof TileEntityGauge) ? ((TileEntityGauge)te) : (null);
  }

  /**
   * Tile entity to update the gauge block frequently.
   */
  public static class TileEntityGauge extends RsBlock.RsTileEntity implements ITickable
  {
    private long trigger_timer_ = 0;
    private long scd_ = 0;
    private boolean force_off_ = false;
    private IBlockState last_state_ = null;

    public int power()
    { return (int)((scd_ & GAUGE_DATA_POWER_MASK) >> GAUGE_DATA_POWER_SHIFT); }

    public int color_tint()
    { return (int)((scd_ & GAUGE_DATA_COLOR_MASK) >> GAUGE_DATA_COLOR_SHIFT); }

    public void power(int p)
    { scd_ =  (scd_ & (~GAUGE_DATA_POWER_MASK)) | ((((p<=0)?(0):((p>15)?15:p)) << GAUGE_DATA_POWER_SHIFT) & GAUGE_DATA_POWER_MASK); }

    public void color_tint(int p)
    { scd_ =  (scd_ & (~GAUGE_DATA_COLOR_MASK)) | ((((p<=0)?(0):((p>15)?15:p)) << GAUGE_DATA_COLOR_SHIFT) & GAUGE_DATA_COLOR_MASK); }

    public boolean force_off()
    { return force_off_; }

    public void reset_timer()
    { trigger_timer_= 0; }

    public void reset()
    { reset(getWorld()); }

    public void reset(IBlockAccess world)
    {
      trigger_timer_= 0;
      if(world == null) {
        scd_ = 0;
      } else {
        try {
          final long current_scd = scd_;
          scd_ = (int) ((((BlockGauge) (world.getBlockState(getPos()).getBlock())).config));
          if(current_scd != scd_) markDirty();
        } catch(Exception e) {
          scd_ = 0; // ok, the default then
        }
      }
    }

    @Override
    public void writeNbt(NBTTagCompound nbt, boolean updatePacket)
    { nbt.setLong("scd", scd_); }

    @Override
    public void readNbt(NBTTagCompound nbt, boolean updatePacket)
    {
      scd_= nbt.getLong("scd");
      if(updatePacket && (world!=null) && (world.isRemote)) trigger_timer_ = 0; // Client re-render on NBT power change
    }

    @Override
    protected void setWorldCreate(World world)
    { reset(world); }

    @Override
    public void update()
    {
      if(--trigger_timer_ > 0) return;
      if(world.isRemote) {
        trigger_timer_ = ModConfig.tweaks.gauge_update_interval * 2;
        IBlockState state = world.getBlockState(pos);
        state = ((BlockGauge) state.getBlock()).getBlockStateWithPower(state, power());
        if(last_state_ != state) {
          last_state_ = state;
          world.markBlockRangeForRenderUpdate(pos, pos);
        }
      } else {
        trigger_timer_ = ModConfig.tweaks.gauge_update_interval;
        try {
          IBlockState state = world.getBlockState(pos);
          final BlockGauge block = (BlockGauge) state.getBlock();
          final BlockPos pos = getPos();
          force_off_ = (block.blink_interval() > 0) && ((System.currentTimeMillis() % block.blink_interval()) > (block.blink_interval() / 2));
          if(block.blink_interval() > 0) trigger_timer_ = 5;
          {
            final BlockPos neighbourPos = pos.offset((EnumFacing) state.getValue(BlockGauge.FACING), -1);
            final IBlockState neighborState = world.getBlockState(neighbourPos);
            int p = 0;
            if((block instanceof BlockIndicator) && (world.isBlockPowered(getPos()))) {
              p = 15; // fast path for directly powered inicators
            } else if(neighborState != null) {
              if(neighborState.canProvidePower()) {
                p = Math.max(
                  neighborState.getWeakPower(world, neighbourPos, state.getValue(FACING).getOpposite()),
                  neighborState.getStrongPower(world, neighbourPos, state.getValue(FACING).getOpposite())
                );
              } else if(ModConfig.optouts.without_gauge_weak_power_measurement) {
                p = world.getStrongPower(neighbourPos);
              } else {
                for(EnumFacing nbf : EnumFacing.values()) {
                  if(p >= 15) break;
                  // Check if the block next to the neighbour is actually a redstone source block or just a strong powered normal block.
                  final BlockPos nbp = neighbourPos.offset(nbf);
                  final IBlockState nbs = world.getBlockState(nbp);
                  if((nbs == null) || (!nbs.canProvidePower()) || (nbs.getBlock() instanceof BlockGauge)) continue;
                  int pa = Math.max(nbs.getStrongPower(world, nbp, nbf), nbs.getWeakPower(world, nbp, nbf));
                  if(p < pa) p = pa;
                }
              }
            }
            if((block.blink_interval() > 0) && force_off()) p = 0;
            final boolean sync = (power() != p);
            if((block.power_on_sound != null) && (power() == 0) && (p > 0)) {
              block.power_on_sound.play(world, pos);
            } else if((block.power_off_sound != null) && (power() > 0) && (p == 0)) {
              block.power_off_sound.play(world, pos);
            }
            power(p);
            if(sync) {
              world.markChunkDirty(pos, this);
              world.notifyBlockUpdate(pos, state, block.getBlockStateWithPower(state, p), 2|4|16); // without observer and comparator stuff
            } else if(!block.cmpBlockStatePower(state, p)) {
              world.setBlockState(pos, block.getBlockStateWithPower(state, p), 4|16);
            }
          }
        } catch(Throwable e) {
          trigger_timer_ = 72000;
          ModRsGauges.logger.error("TE update() failed: " + e);
        }
      }
    }
  }
}
