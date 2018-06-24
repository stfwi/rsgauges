/**
 * @file PulseInputBlock.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Represents a simple button like (strong) redstone
 * signal source. The power state is stored in the
 * block metadata, and the timing is realised using
 * scheduled block updates (without tile entity ticks).
 *
 * Pulse switches can be activated multiple times, where
 * the ON-time is increased each time a switch is clicked
 * when it is already active. After the ON time has expired
 * and the switch is off again, the ON-time extension
 * starts at zero again.
**/
package wile.rsgauges.blocks;

import wile.rsgauges.blocks.RsBlock;

import java.util.Random;

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

public class PulseInputBlock extends RsBlock {

  public static final PropertyBool POWERED = PropertyBool.create("powered");
  private int off_timer = 0;

  public PulseInputBlock(String registryName, AxisAlignedBB unrotatedBB) {
    super(registryName, unrotatedBB);
    setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, false));
    this.setTickRandomly(true);
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 0x7)).withProperty(POWERED,
        ((meta & 0x8) != 0));
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return (state.getValue(FACING).getIndex() & 0x07) | ((state.getValue(POWERED)) ? 0x8 : 0x0);
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, FACING, POWERED);
  }

  @Override
  public boolean canProvidePower(IBlockState state) {
    return true;
  }

  @Override
  public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    return getStrongPower(state, world, pos, side);
  }

  @Override
  public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    return (!(state.getValue(POWERED)) || (state.getValue(FACING) != side)) ? 0 : 15;
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    if(super.onBlockPlacedByCheck(world, pos, state, placer, stack, true, false)) {
      world.setBlockState(pos, world.getBlockState(pos).withProperty(POWERED, false), 1 | 2);
      world.notifyNeighborsOfStateChange(pos, this, false);
      world.notifyNeighborsOfStateChange(pos.offset(state.getValue(FACING).getOpposite()), this, false);
      if(!world.isRemote) off_timer = 0;
    }
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    if(world.isRemote) return true;
    world.setBlockState(pos, state.withProperty(POWERED, true), 1 | 2);
    world.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.92f);
    world.notifyNeighborsOfStateChange(pos, this, false);
    world.notifyNeighborsOfStateChange(pos.offset(state.getValue(FACING).getOpposite()), this, false);
    // Simplistic constant non-entity based on-time extension capability.
    if(off_timer > 35) off_timer = 80;
    else if(off_timer > 15) off_timer = 40;
    else if(off_timer > 8) off_timer = 20;
    else if(off_timer > 3) off_timer = 10;
    else off_timer = 5;
    world.scheduleUpdate(pos, this, this.tickRate(world));
    return true;
  }

  @Override
  public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
    world.setBlockState(pos, world.getBlockState(pos).withProperty(POWERED, false), 1 | 2);
    world.notifyNeighborsOfStateChange(pos, this, false);
    world.notifyNeighborsOfStateChange(pos.offset(state.getValue(FACING).getOpposite()), this, false);
    return super.removedByPlayer(state, world, pos, player, willHarvest);
  }

  @Override
  public int tickRate(World world) {
    return 5;
  }

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
    if(world.isRemote) return;
    if(!state.getValue(POWERED)) return;
    if(--off_timer <= 0) {
      off_timer = 0;
      world.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(false)));
      world.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.BLOCKS, 0.3F, 0.82f);
      world.notifyNeighborsOfStateChange(pos, this, false);
      world.notifyNeighborsOfStateChange(pos.offset(state.getValue(FACING).getOpposite()), this, false);
      world.markBlockRangeForRenderUpdate(pos, pos);
    } else {
      world.scheduleUpdate(pos, this, this.tickRate(world));
    }
  }

}
