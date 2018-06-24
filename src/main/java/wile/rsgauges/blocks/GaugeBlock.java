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
import wile.rsgauges.blocks.RsBlock;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class GaugeBlock extends RsBlock {

  public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);

  // If the block emits light proportional to the power (e.g. lamp) this scaler is
  // nonzero. The light is only rendering light, not anti-spawn light.
  private int lightValueScaling = 0;
  // If the block has an alternation when powered, such as blinking lights, this
  // value is nonzero and a value in ticks.
  private int alternationTimeBase = 0;
  private int alternationOnTime = 0;
  private int alternationOffTime = 0;
  // Specifies if the gauge shall look at the maximum redstone power, including
  // weak power, of the target block it is attached to.
  private boolean measureTargetWeakPower = false;

  public int getAlternationTimeBase() {
    return alternationTimeBase;
  }

  public void blockConfig(int powerToLightValueScaling0To15, int alternationTimeBaseTicks, int alternationOnTime, int alternationOffTime, boolean measureTargetWeakPower) {
    this.lightValueScaling = (powerToLightValueScaling0To15 < 0) ? (0) : ((powerToLightValueScaling0To15 > 15) ? 15 : powerToLightValueScaling0To15);
    this.alternationTimeBase = (alternationTimeBaseTicks < 1) ? 1 : alternationTimeBaseTicks;
    this.alternationOnTime = (alternationOnTime < 0) ? 0 : alternationOnTime;
    this.alternationOffTime = (alternationOffTime < 0) ? 0 : alternationOffTime;
    this.measureTargetWeakPower = measureTargetWeakPower;
  }

  public GaugeBlock(String registryName, AxisAlignedBB unrotatedBB, int powerToLightValueScaling0To15, int alternationTimeBaseTicks, int alternationOnTime, int alternationOffTime) {
    super(registryName, unrotatedBB);
    this.blockConfig(powerToLightValueScaling0To15, alternationTimeBaseTicks, alternationOnTime, alternationOffTime, false);
    setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWER, 0));
  }

  public GaugeBlock(String registryName, AxisAlignedBB boundingBox, int powerToLightValueScaling0To15) {
    this(registryName, boundingBox, powerToLightValueScaling0To15, 0, 0, 0);
  }

  public GaugeBlock(String registryName, AxisAlignedBB boundingBox) {
    this(registryName, boundingBox, 0, 0, 0, 0);
  }

  public int getNeighborPower(World world, BlockPos pos) {
    if(!measureTargetWeakPower) {
      int p = world.getStrongPower(pos);
      return p >= 15 ? 15 : p;
    } else {
      int p = 0;
      p = Math.max(p, world.getRedstonePower(pos.down(), EnumFacing.DOWN));   if(p >= 15) return 15;
      p = Math.max(p, world.getRedstonePower(pos.up(), EnumFacing.UP));       if(p >= 15) return 15;
      p = Math.max(p, world.getRedstonePower(pos.north(), EnumFacing.NORTH)); if(p >= 15) return 15;
      p = Math.max(p, world.getRedstonePower(pos.south(), EnumFacing.SOUTH)); if(p >= 15) return 15;
      p = Math.max(p, world.getRedstonePower(pos.west(), EnumFacing.WEST));   if(p >= 15) return 15;
      p = Math.max(p, world.getRedstonePower(pos.east(), EnumFacing.EAST));   if(p >= 15) return 15;
      return p;
    }
  }

  public static void updateBlock(GaugeBlock block, IBlockState state, World world, BlockPos pos, BlockPos neighborPos, int flags) {
    int p = block.getNeighborPower(world, neighborPos);
    if((p > 0) && (block.alternationTimeBase > 4)) {
      final long interval = block.alternationOnTime + block.alternationOffTime;
      if(((world.getTotalWorldTime() / block.alternationTimeBase) % interval) > (block.alternationOnTime)) p = 0;
    }
    world.setBlockState(pos, world.getBlockState(pos).withProperty(POWER, p), flags);
    if(world.isRemote) world.markBlockRangeForRenderUpdate(pos, pos);
  }

  @Override
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos) {
    if(!this.neighborChangedCheck(state, world, pos, neighborBlock, neighborPos)) return;
    updateBlock(this, state, world, pos, neighborPos, 2 | 16);
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    if(!this.onBlockPlacedByCheck(world, pos, state, placer, stack, true, false)) return;
    EnumFacing blockFacing = state.getValue(FACING);
    BlockPos neighborPos = pos.offset(blockFacing.getOpposite());
    updateBlock(this, state, world, pos, neighborPos, 1 | 2);
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
    return new GaugeTileEntity();
  }
}
