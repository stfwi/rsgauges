/**
 * @file GaugeBlock.java
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
**/
package wile.rsgauges.blocks;

import wile.rsgauges.ModConfig;
import wile.rsgauges.ModResources;
import wile.rsgauges.ModRsGauges;
import wile.rsgauges.ModAuxiliaries;
import wile.rsgauges.blocks.RsBlock;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import javax.annotation.Nullable;


public class GaugeBlock extends RsBlock
{
  public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
  private final int lightValue;     // nonzero if the block emits light when powered (e.g. lamp). The light is only rendering light, not anti-spawn light.
  private final int blinkInterval;

  @Nullable protected final ModResources.BlockSoundEvent power_on_sound;
  @Nullable protected final ModResources.BlockSoundEvent power_off_sound;

  public GaugeBlock(String registryName, AxisAlignedBB unrotatedBB, int powerToLightValueScaling0To15, int blinkInterval, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  {
    super(registryName, unrotatedBB, null);
    this.lightValue = (powerToLightValueScaling0To15 < 0) ? (0) : ((powerToLightValueScaling0To15 > 15) ? 15 : powerToLightValueScaling0To15);
    this.blinkInterval = (blinkInterval <= 0) ? (0) : ((blinkInterval < 500) ? (500) : ((blinkInterval > 3000) ? 3000 : blinkInterval));
    this.power_on_sound = powerOnSound;
    this.power_off_sound = powerOffSound;
    setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWER, 0));
  }

  public GaugeBlock(String registryName, AxisAlignedBB unrotatedBB, int powerToLightValueScaling0To15, int blinkInterval)
  { this(registryName, unrotatedBB, powerToLightValueScaling0To15, blinkInterval, null, null); }

  public GaugeBlock(String registryName, AxisAlignedBB boundingBox)
  { this(registryName, boundingBox, 0, 0); }

  @Override
  public boolean isWallMount()
  { return true; }

  @Override
  @SideOnly(Side.CLIENT)
  public void initModel()
  { super.initModel(); }

  @Override
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer()
  { return (lightValue>0) ? (BlockRenderLayer.TRANSLUCENT) : (BlockRenderLayer.CUTOUT); }

  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
  {
    final GaugeBlock.GaugeTileEntity te = getTe(state, world, pos);
    return super.getActualState(state, world, pos).withProperty(POWER, (te==null) ? 0 : te.power());
  }

  @Override
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos)
  {
    if(!this.neighborChangedCheck(state, world, pos, neighborBlock, neighborPos)) return;
    final GaugeBlock.GaugeTileEntity te = getTe(state, world, pos);
    if(te != null) te.reset_timer();
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
  { if(this.onBlockPlacedByCheck(world, pos, state, placer, stack)) world.scheduleBlockUpdate(pos, state.getBlock(), 0, 1); }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
  { return false; }

  @Override
  public int getLightValue(IBlockState state)
  { return (this.lightValue<1) ? 0 : ((!ModAuxiliaries.isClientSide()) || (state.getValue(POWER)>0)) ? (this.lightValue) : (0); }

  @Override
  public boolean getWeakChanges(IBlockAccess world, BlockPos pos)
  { return true; }

  @Override
  protected BlockStateContainer createBlockState()
  { return new BlockStateContainer(this, FACING,POWER); }

  @Override
  public boolean hasTileEntity(IBlockState state)
  { return true; }

  @Override
  public TileEntity createTileEntity(World world, IBlockState state)
  { return new GaugeBlock.GaugeTileEntity(); }

  /**
   * Readwrite TE getter.
   */
  public GaugeBlock.GaugeTileEntity getTe(IBlockState state, IBlockAccess world, BlockPos pos)
  {
    final TileEntity te = world.getTileEntity(pos);
    return (!(te instanceof GaugeBlock.GaugeTileEntity)) ? (null) : ((GaugeBlock.GaugeTileEntity)te);
  }

  /**
   * Tile entity to update the gauge block frequently.
   */
  public static final class GaugeTileEntity extends RsBlock.RsTileEntity<GaugeBlock> implements ITickable
  {
    private long trigger_timer_ = 0;
    private int power_ = 0;
    private boolean force_off_ = false;
    private IBlockState last_state_ = null;

    public int power()
    { return this.power_; }

    public void power(int p)
    { this.power_ = (p<=0) ? (0) : ((p>15) ? 15 : p); }

    public boolean force_off()
    { return this.force_off_; }

    public void reset_timer()
    { trigger_timer_= 0; }

    @Override
    public void writeNbt(NBTTagCompound nbt, boolean updatePacket)
    { nbt.setInteger("power", power()); }

    @Override
    public void readNbt(NBTTagCompound nbt, boolean updatePacket)
    {
      power(nbt.getInteger("power"));
      // Client re-render on NBT power change
      if(updatePacket && (world!=null) && (world.isRemote)) {
        final IBlockState state = world.getBlockState(pos).withProperty(POWER, power());
        if(last_state_ != state) {
          last_state_ = state;
          if(state != null) {
            world.setBlockState(pos, state, 16);
            world.markBlockRangeForRenderUpdate(pos, pos);
          }
        }
      }
    }

    @Override
    public void update()
    {
      if(world.isRemote || (--trigger_timer_ > 0)) return;
      trigger_timer_ = ModConfig.gauge_update_interval;
      try {
        IBlockState state = world.getBlockState(pos);
        final GaugeBlock block = (GaugeBlock) state.getBlock();
        final BlockPos pos = getPos();
        force_off_ = (block.blinkInterval > 0) && ((System.currentTimeMillis() % block.blinkInterval) > (block.blinkInterval/2));
        if(block.blinkInterval > 0) trigger_timer_ = 5;
        {
          final BlockPos neighbourPos = pos.offset((EnumFacing)state.getValue(GaugeBlock.FACING), -1);
          if(!world.isBlockLoaded(neighbourPos)) return; // Gauge is placed on a chunk boundary, don't forge loading of neighbour chunk.
          final IBlockState neighborState = world.getBlockState(neighbourPos);
          int p = 0;
          if(neighborState != null) {
            if(neighborState.canProvidePower()) {
              p = Math.max(
                  neighborState.getWeakPower(world, neighbourPos, state.getValue(FACING).getOpposite()),
                  neighborState.getStrongPower(world, neighbourPos, state.getValue(FACING).getOpposite())
              );
            } else if(ModConfig.without_gauge_weak_power_measurement) {
              p = world.getStrongPower(neighbourPos);
            } else {
              for(EnumFacing nbf:EnumFacing.values()) {
                if(p >= 15) break;
                // Check if the block next to the neighbour is actually a redstone source block or just a strong powered normal block.
                final BlockPos nbp = neighbourPos.offset(nbf);
                final IBlockState nbs = world.getBlockState(nbp);
                if((nbs == null) || (!nbs.canProvidePower()) || (nbs.getBlock() instanceof GaugeBlock)) continue;
                int pa = Math.max(nbs.getStrongPower(world, nbp, nbf), nbs.getWeakPower(world, nbp, nbf));
                if(p < pa) p = pa;
              }
            }
          }
          if((block.blinkInterval > 0) && this.force_off()) p = 0;
          final boolean sync = (this.power() != p);
          if((block.power_on_sound != null) && (this.power() == 0) && (p > 0)) {
            block.power_on_sound.play(world, pos);
          } else if((block.power_off_sound != null) && (this.power() > 0) && (p == 0)) {
            block.power_off_sound.play(world, pos);
          }
          this.power(p);
          if(sync) {
            world.markChunkDirty(pos, this);
            world.notifyBlockUpdate(pos, state, state.withProperty(POWER, p), 2|16); // without observer and comparator stuff
          } else if(state.getValue(POWER) != p) {
            world.setBlockState(pos, state.withProperty(POWER, p), 16);
          }
        }
      } catch(Throwable e) {
        trigger_timer_ = 100;
        ModRsGauges.logger.error("TE update() failed: " + e);
      }
    }
  }
}
