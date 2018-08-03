/**
 * @file BistableInputBlock.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Basic class for blocks representing bistable redstone
 * signal sources, like the vanilla lever. The blocks do
 * not have tile entities, the switching state is stored
 * as bit in the block meta data. They provide strong
 * redstone power.
**/
package wile.rsgauges.blocks;

import wile.rsgauges.blocks.RsBlock;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BistableInputBlock extends RsBlock {

  public static final PropertyBool POWERED = PropertyBool.create("powered");

  public BistableInputBlock(String registryName, AxisAlignedBB unrotatedBB) {
    super(registryName, unrotatedBB);
    setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, false));
  }

  @Override
  public IBlockState getStateFromMeta(int meta) { return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 0x7)).withProperty(POWERED, ((meta & 0x8) != 0)); }

  @Override
  public int getMetaFromState(IBlockState state) { return (state.getValue(FACING).getIndex() & 0x07) | ((state.getValue(POWERED)) ? 0x8 : 0x0); }

  @Override
  protected BlockStateContainer createBlockState() { return new BlockStateContainer(this, FACING, POWERED); }

  @Override
  public boolean canProvidePower(IBlockState state) { return true; }

  @Override
  public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) { return getStrongPower(state, world, pos, side); }

  @Override
  public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) { return (!(state.getValue(POWERED)) || (state.getValue(FACING) != side)) ? 0 : 15; }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    if(!super.onBlockPlacedByCheck(world, pos, state, placer, stack, true, false)) return;
    world.setBlockState(pos, world.getBlockState(pos).withProperty(POWERED, false), 1|2);
    world.notifyNeighborsOfStateChange(pos, this, false);
    world.notifyNeighborsOfStateChange(pos.offset(state.getValue(FACING).getOpposite()), this, false);
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    if(world.isRemote) return true;
    state = state.cycleProperty(POWERED);
    world.setBlockState(pos, state, 1|2);
    world.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, ((!state.getValue(POWERED)) ? 0.82f : 0.92f));
    world.notifyNeighborsOfStateChange(pos, this, false);
    world.notifyNeighborsOfStateChange(pos.offset(state.getValue(FACING).getOpposite()), this, false);
    return true;
  }

  @Override
  public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
    world.setBlockState(pos, world.getBlockState(pos).withProperty(POWERED, false), 1|2);
    world.notifyNeighborsOfStateChange(pos, this, false);
    world.notifyNeighborsOfStateChange(pos.offset(state.getValue(FACING).getOpposite()), this, false);
    return super.removedByPlayer(state, world, pos, player, willHarvest);
  }
}
