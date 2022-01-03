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

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wile.rsgauges.libmc.detail.Auxiliaries;
import wile.rsgauges.libmc.detail.ColorUtils;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;


public class SensitiveGlassBlock extends RsBlock
{
  public static final BooleanProperty POWERED = BooleanProperty.create("powered");
  public static final ColorUtils.DyeColorProperty COLOR = ColorUtils.DyeColorProperty.create("color");

  // -------------------------------------------------------------------------------------------------------------------

  public SensitiveGlassBlock(BlockBehaviour.Properties properties)
  {
    super(RSBLOCK_CONFIG_TRANSLUCENT, properties, Auxiliaries.getPixeledAABB(0, 0, 0, 16, 16,16 ));
    registerDefaultState(super.defaultBlockState().setValue(POWERED, false).setValue(COLOR, DyeColor.WHITE));
  }

  @Override
  @Nullable
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
  { return null; }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  // Light reduction
  @OnlyIn(Dist.CLIENT)
  @SuppressWarnings("deprecation")
  public float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos)
  { return 0.95f; }

  @Override
  public RenderTypeHint getRenderTypeHint()
  { return RenderTypeHint.TRANSLUCENT; }

  @Override
  @OnlyIn(Dist.CLIENT)
  @SuppressWarnings("deprecation")
  public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side)
  {
    if((!(adjacentBlockState.getBlock() instanceof SensitiveGlassBlock))) return false;
    return (adjacentBlockState.getValue(POWERED) == state.getValue(POWERED));
  }

  @Override
  public boolean isPossibleToRespawnInThis()
  { return false; }

  @Override
  public boolean shouldCheckWeakPower(BlockState state, LevelReader world, BlockPos pos, Direction side)
  { return true; }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
  { super.createBlockStateDefinition(builder); builder.add(POWERED, COLOR); }

  @Override
  public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random)
  {
    if(world.isClientSide()) return;
    if(state.getValue(POWERED) && (!(world.hasNeighborSignal(pos)))) {
      world.setBlock(pos, state.setValue(POWERED, false), 1|2|8|16);
    }
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos)
  { return true; }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context)
  {
    final BlockState state = super.getStateForPlacement(context);
    return (state==null) ? (null) : (state.setValue(POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos())));
  }

  @Override
  public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit)
  {
    final ItemStack stack = player.getItemInHand(hand);
    Optional<DyeColor> dye = ColorUtils.getColorFromDyeItem(stack);
    if(dye.isEmpty()) return InteractionResult.PASS;
    world.setBlock(pos, state.setValue(COLOR, dye.get()), 1|2);
    return world.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
  }

  @Override
  public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
  {
    if(world.isClientSide()) return;
    final boolean was_powered = state.getValue(POWERED);
    final boolean powered = world.hasNeighborSignal(pos);
    if(was_powered == powered) return;
    if(powered) {
      world.setBlock(pos, state.setValue(POWERED, powered), 1|2);
    } else {
      world.scheduleTick(pos, this, 4);
    }
  }

}
