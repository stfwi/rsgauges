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

import wile.rsgauges.Config;
import wile.rsgauges.ModBlocks;
import wile.rsgauges.blocks.RsBlock;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityComparator;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.block.BlockRedstoneComparator;


public class GaugeBlock extends RsBlock {

  public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);

  // If the block emits light proportional to the power (e.g. lamp) this scaler is
  // nonzero. The light is only rendering light, not anti-spawn light.
  private final int lightValueScaling;
  // If the block has an alternation when powered, such as blinking lights, this
  // value is nonzero and a value in ticks.
  private final int alternationTimeBase;
  private final int alternationOnTime;
  private final int alternationOffTime;
  // Specifies if the gauge shall look at the maximum redstone power, including
  // weak power, of the target block it is attached to.
  private final boolean measureTargetWeakPower = true;

  public int getAlternationTimeBase() { return alternationTimeBase; }

  public GaugeBlock(String registryName, AxisAlignedBB unrotatedBB, int powerToLightValueScaling0To15, int alternationTimeBaseTicks, int alternationOnTime, int alternationOffTime) {
    super(registryName, unrotatedBB);
    this.lightValueScaling = (powerToLightValueScaling0To15 < 0) ? (0) : ((powerToLightValueScaling0To15 > 15) ? 15 : powerToLightValueScaling0To15);
    this.alternationTimeBase = (alternationTimeBaseTicks < 1) ? 1 : alternationTimeBaseTicks;
    this.alternationOnTime = (alternationOnTime < 0) ? 0 : alternationOnTime;
    this.alternationOffTime = (alternationOffTime < 0) ? 0 : alternationOffTime;
    setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWER, 0));
  }

  public GaugeBlock(String registryName, AxisAlignedBB boundingBox, int powerToLightValueScaling0To15) {
    this(registryName, boundingBox, powerToLightValueScaling0To15, 0, 0, 0);
  }

  public GaugeBlock(String registryName, AxisAlignedBB boundingBox) {
    this(registryName, boundingBox, 0, 0, 0, 0);
  }

  public int getIndirectlyMeasuredPower(World world, BlockPos pos) {
    int p = 0;
    if(!measureTargetWeakPower) {
      p = world.getStrongPower(pos);
    } else {
      for(EnumFacing nbf:EnumFacing.values()) {
        if(p >= 15) break;
        // Check if the block next to the neighbour is actually a redstone source block or just a strong powered normal block.
        BlockPos nbp = pos.offset(nbf);
        IBlockState nbs = world.getBlockState(nbp);
        if((nbs == null) || (!nbs.canProvidePower()) || (nbs.getBlock() instanceof GaugeBlock)) continue;
        int pa = Math.max(nbs.getStrongPower(world, nbp, nbf), nbs.getWeakPower(world, nbp, nbf));
        if(p < pa) p = pa;
      }
    }
    return (p >= 15) ? (15) : (p);
  }

  public void updateBlock(IBlockState state, World world, BlockPos pos, BlockPos neighborPos) {
    if(!(world instanceof World)) return;
    IBlockState neighborState = world.getBlockState(neighborPos);
    if(neighborState == null) return;
    int p = 0;
    if(neighborState.canProvidePower()) {
      p = Math.max(
          neighborState.getWeakPower(world, neighborPos, state.getValue(FACING).getOpposite()),
          neighborState.getStrongPower(world, neighborPos, state.getValue(FACING).getOpposite())
      );
    } else {
      p = getIndirectlyMeasuredPower(world, neighborPos);
    }
    if(world.isRemote) {
      if(this.alternationTimeBase > 4) {
        long interval = this.alternationOnTime + this.alternationOffTime;
        boolean alternation_power_off = (((((World)world).getTotalWorldTime() / this.alternationTimeBase) % interval) > (this.alternationOnTime));
        if(alternation_power_off) p = 0;
      }
    }
    world.setBlockState(pos, state.withProperty(POWER, p), 1|2|16);
    if(world.isRemote) world.markBlockRangeForRenderUpdate(pos, pos);
  }

  @Override
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos) {
    if(!this.neighborChangedCheck(state, world, pos, neighborBlock, neighborPos)) return;
    updateBlock(state, world, pos, neighborPos);
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    if(!this.onBlockPlacedByCheck(world, pos, state, placer, stack, true, false)) return;
    world.scheduleBlockUpdate(pos, state.getBlock(), 4, 1);
  }

  @Override
  public int getLightValue(IBlockState state) {
    int v = (int)((this.lightValueScaling * state.getValue(POWER)) / 15);
    return (v < 0) ? (0) : ((v > 15) ? 15 : v);
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, FACING, POWER);
  }

  @Override
  public boolean hasTileEntity(IBlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(World world, IBlockState state) {
    return new UpdateTileEntity();
  }

  @Override
  public boolean getWeakChanges(IBlockAccess world, BlockPos pos) {
      return true;
  }

  private GaugeBlock.UpdateTileEntity getTe(IBlockState state, IBlockAccess world, BlockPos pos, boolean nocache)
  {
    if(nocache && (world instanceof ChunkCache)) return null;
    TileEntity te = (world instanceof ChunkCache) ? ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
    return (!(te instanceof GaugeBlock.UpdateTileEntity)) ? (null) : ((GaugeBlock.UpdateTileEntity)te);
  }

  /**
   * Tile entity to update the gauge block frequently.
   */
  public static final class UpdateTileEntity extends RsTileEntity<GaugeBlock> implements ITickable {

    private long trigger_time = 0;

    @Override
    public void update() {
      if(!hasWorld()) return;
      World world = getWorld();
      long t = world.getTotalWorldTime();
      if(t < trigger_time) return;
      trigger_time = t + 100; // in case of early returns on error
      if(!world.isBlockLoaded(getPos())) return;
      IBlockState state = world.getBlockState(getPos());
      if(state == null) return;
      Block ckblock = state.getBlock();
      if(!(ckblock instanceof GaugeBlock)) return;
      GaugeBlock block = (GaugeBlock) ckblock;
      BlockPos neighbourPos = getPos().offset((EnumFacing)state.getValue(GaugeBlock.FACING), -1);
      if(!world.isBlockLoaded(neighbourPos)) return; // Gauge is placed on a chunk boundary, don't forge loading of neighbour chunk.
      if(world.isRemote) {
        int T = block.getAlternationTimeBase();
        if(T <= 2) T = Config.getGaugeUpdateInterval();
        trigger_time  = ((block.alternationTimeBase > 4)) ? ((((t + T - 1) / T) + 1) * T) : (t + Config.getGaugeUpdateInterval());
        block.updateBlock(state, world, getPos(), neighbourPos);
      }
    }
  }
}
