/*
 * @file SensitiveGlassBlock.java
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

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.IBlockReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.DyeColor;
import net.minecraft.block.BlockState;
import net.minecraft.block.Block;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.item.ItemStack;
import wile.rsgauges.libmc.detail.ColorUtils;
import wile.rsgauges.libmc.detail.Auxiliaries;

import java.util.Optional;
import java.util.Random;

public class SensitiveGlassBlock extends RsBlock
{
  public static final BooleanProperty POWERED = BooleanProperty.create("powered");
  public static final ColorUtils.DyeColorProperty COLOR = ColorUtils.DyeColorProperty.create("color");

  // -------------------------------------------------------------------------------------------------------------------

  public SensitiveGlassBlock(Block.Properties properties)
  {
    super(RSBLOCK_CONFIG_TRANSLUCENT, properties, Auxiliaries.getPixeledAABB(0, 0, 0, 16, 16,16 ));
    setDefaultState(super.getDefaultState().with(POWERED, false).with(COLOR, DyeColor.WHITE));
  }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  // Light reduction
  @OnlyIn(Dist.CLIENT)
  public float func_220080_a(BlockState state, IBlockReader world, BlockPos pos)
  { return 0.95f; }

  @Override
  public RenderTypeHint getRenderTypeHint()
  { return RenderTypeHint.TRANSLUCENT; }

  @Override
  @OnlyIn(Dist.CLIENT)
  @SuppressWarnings("deprecation")
  public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side)
  {
    if((!(adjacentBlockState.getBlock() instanceof SensitiveGlassBlock))) return false;
    return (adjacentBlockState.get(POWERED) == state.get(POWERED));
  }

  @Override
  public boolean canSpawnInBlock()
  { return false; }

  @Override
  public boolean shouldCheckWeakPower(BlockState state, IWorldReader world, BlockPos pos, Direction side)
  { return true; }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
  { super.fillStateContainer(builder); builder.add(POWERED, COLOR); }

  @Override
  public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random)
  {
    if(world.isRemote()) return;
    if(state.get(POWERED) && (!(world.isBlockPowered(pos)))) {
      world.setBlockState(pos, state.with(POWERED, false), 1|2|8|16);
    }
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos)
  { return true; }

  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context)
  {
    final BlockState state = super.getStateForPlacement(context);
    return (state==null) ? (null) : (state.with(POWERED, context.getWorld().isBlockPowered(context.getPos())));
  }

  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
  {
    final ItemStack stack = player.getHeldItem(hand);
    Optional<DyeColor> dye = ColorUtils.getColorFromDyeItem(stack);
    if(!dye.isPresent()) return ActionResultType.PASS;
    world.setBlockState(pos, state.with(COLOR, dye.get()), 1|2);
    return ActionResultType.SUCCESS;
  }

  @Override
  public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
  {
    if(world.isRemote()) return;
    final boolean was_powered = state.get(POWERED);
    final boolean powered = world.isBlockPowered(pos);
    if(was_powered == powered) return;
    if(powered) {
      world.setBlockState(pos, state.with(POWERED, powered), 1|2);
    } else {
      world.getPendingBlockTicks().scheduleTick(pos, this, 4);
    }
  }

}
