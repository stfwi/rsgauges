/*
 * @file BlockSensitiveGlass.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Class representing full, transparent blocks with different
 * look depending on the redstone power they receive.
 *
 * Light handling see Mojang Redstone Lamp.
 */
package wile.rsgauges.blocks;

import wile.rsgauges.detail.ModAuxiliaries;
import net.minecraft.world.IBlockReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import java.util.Random;

public class BlockSensitiveGlass extends RsBlock
{
  public static final BooleanProperty POWERED = BooleanProperty.create("powered");

  // -------------------------------------------------------------------------------------------------------------------

  public BlockSensitiveGlass(Block.Properties properties)
  {
    super(RSBLOCK_CONFIG_TRANSLUCENT, properties, ModAuxiliaries.getPixeledAABB(0, 0, 0, 16, 16,16 ));
    setDefaultState(getDefaultState().with(POWERED, false));
  }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  // Light reduction
  @OnlyIn(Dist.CLIENT)
  public float func_220080_a(BlockState state, IBlockReader world, BlockPos pos)
  { return 0.95f; }

  @Override
  @OnlyIn(Dist.CLIENT)
  public BlockRenderLayer getRenderLayer()
  { return BlockRenderLayer.TRANSLUCENT; }

  @Override
  @OnlyIn(Dist.CLIENT)
  @SuppressWarnings("deprecation")
  public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side)
  {
    if((!(adjacentBlockState.getBlock() instanceof BlockSensitiveGlass))) return false; // or better == this?
    return (adjacentBlockState.get(POWERED) == state.get(POWERED));
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isSolid(BlockState state)
  { return false; } // hmm. making this true simplifies some redstone reactions, but the side rendering is incorrect then.

  @Override
  public boolean shouldCheckWeakPower(BlockState state, IWorldReader world, BlockPos pos, Direction side)
  { return true; }

  @Override
  public int getLightValue(BlockState state)
  { return state.get(POWERED) ? super.getLightValue(state) : 0; }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
  { super.fillStateContainer(builder); builder.add(POWERED); }

  @Override
  public int tickRate(IWorldReader world)
  { return 4; }

  @Override
  public void tick(BlockState state, World world, BlockPos pos, Random random)
  {
    if(world.isRemote) return;
    if(state.get(POWERED) && (!(world.isBlockPowered(pos)))) {
      world.setBlockState(pos, state.with(POWERED, false), 2);
    }
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos)
  { return true; }

  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context)
  { return super.getStateForPlacement(context).with(POWERED, context.getWorld().isBlockPowered(context.getPos())); }

  @Override
  public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
  { return false; }

  @Override
  protected void neighborUpdated(BlockState state, World world, BlockPos pos, BlockPos facingPos)
  {
    if(world.isRemote) return;
    final boolean was_powered = state.get(POWERED);
    final boolean powered = world.isBlockPowered(pos);
    if(was_powered == powered) return;
    if(powered) {
      // Switching on is immediate, off -> no changing light value, direct block state change.
      world.setBlockState(pos, state.with(POWERED, powered), 2);
    } else {
      // Delay "switch-off" to prevent recalculations on short peaks for light emitting variants.
      world.getPendingBlockTicks().scheduleTick(pos, this, 4);
    }
  }
}
