/*
 * @file RsBlock.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Base class for blocks used in this mod. It sets the
 * defaults for block properties, registration categories,
 * and provides default overloads for further blocks.
 * As rsgauges blocks work directional (placed with a
 * defined facing), the basics for `facing` dependent
 * data are implemented here, too.
 */
package wile.rsgauges.blocks;

import net.minecraft.world.*;
import net.minecraft.loot.LootContext;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.StateContainer;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.PushReaction;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import wile.rsgauges.libmc.detail.Auxiliaries;
import wile.rsgauges.libmc.detail.Networking;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public abstract class RsBlock extends Block implements IWaterLoggable
{
  public static final long RSBLOCK_CONFIG_SOLID              = 0x0000000000000000l;
  public static final long RSBLOCK_CONFIG_CUTOUT             = 0x1000000000000000l;
  public static final long RSBLOCK_CONFIG_CUTOUT_MIPPED      = 0x2000000000000000l;
  public static final long RSBLOCK_CONFIG_TRANSLUCENT        = 0x3000000000000000l;
  public static final long RSBLOCK_NOT_WATERLOGGABLE         = 0x0008000000000000l;
  public enum RenderTypeHint { SOLID,CUTOUT,CUTOUT_MIPPED,TRANSLUCENT }

  private static final RenderTypeHint render_layer_map_[] = { RenderTypeHint.SOLID, RenderTypeHint.CUTOUT, RenderTypeHint.CUTOUT_MIPPED, RenderTypeHint.TRANSLUCENT };
  public final long config;

  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

  // -------------------------------------------------------------------------------------------------------------------

  public RsBlock(long config, Block.Properties properties)
  { this(config, properties, Auxiliaries.getPixeledAABB(0, 0, 0, 16, 16,16 )); }

  public RsBlock(long config, Block.Properties properties, final AxisAlignedBB aabb)
  { this(config, properties, VoxelShapes.create(aabb)); }

  public RsBlock(long config, Block.Properties properties, final VoxelShape vshape)
  { super(properties); this.config = config; setDefaultState(this.getStateContainer().getBaseState().with(WATERLOGGED, false)); }

  public RenderTypeHint getRenderTypeHint()
  { return render_layer_map_[(int)((config>>60)&0x3)]; }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag flag)
  { Auxiliaries.Tooltip.addInformation(stack, world, tooltip, flag, true); }

  @Override
  @OnlyIn(Dist.CLIENT)
  public boolean addHitEffects(BlockState state, World worldObj, RayTraceResult target, net.minecraft.client.particle.ParticleManager manager)
  { return true; }

  @OnlyIn(Dist.CLIENT)
  @SuppressWarnings("deprecation")
  public boolean hasCustomBreakingProgress(BlockState state)
  { return true; }

  @Override
  @SuppressWarnings("deprecation")
  public VoxelShape getShape(BlockState state, IBlockReader source, BlockPos pos, ISelectionContext selectionContext)
  { return VoxelShapes.fullCube(); }

  @Override
  @SuppressWarnings("deprecation")
  public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext selectionContext)
  { return getShape(state, world, pos, selectionContext); }

  @Override
  public boolean canSpawnInBlock()
  { return false; }

  @Override
  public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos, EntitySpawnPlacementRegistry.PlacementType type, @Nullable EntityType<?> entityType)
  { return false; }

  @Override
  @SuppressWarnings("deprecation")
  public PushReaction getPushReaction(BlockState state)
  { return PushReaction.DESTROY; }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
  { super.fillStateContainer(builder); builder.add(WATERLOGGED); }

  @Override
  @Nullable
  public BlockState getStateForPlacement(BlockItemUseContext context)
  {
    BlockState state = super.getStateForPlacement(context);
    if(state==null) return null;
    if((config & RSBLOCK_NOT_WATERLOGGABLE)==0) {
      FluidState fs = context.getWorld().getFluidState(context.getPos());
      state = state.with(WATERLOGGED,fs.getFluid()==Fluids.WATER);
    } else {
      state = state.with(WATERLOGGED, false);
    }
    return state;
  }

  @Override
  @SuppressWarnings("deprecation")
  public FluidState getFluidState(BlockState state)
  { return ((config & RSBLOCK_NOT_WATERLOGGABLE)==0) ? (state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state)) : super.getFluidState(state); }

  @Override
  public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos)
  {
    if(((config & RSBLOCK_NOT_WATERLOGGABLE)==0) && state.get(WATERLOGGED)) return false;
    return super.propagatesSkylightDown(state, reader, pos);
  }

  @Override
  @SuppressWarnings("deprecation")
  public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving)
  {
    super.onReplaced(state, world, pos, newState, isMoving);
    world.updateComparatorOutputLevel(pos, newState.getBlock());
    world.notifyNeighborsOfStateChange(pos, newState.getBlock());
  }

  @Override
  @SuppressWarnings("deprecation")
  public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos)
  {
    if((config & RSBLOCK_NOT_WATERLOGGABLE)==0) {
      if(state.get(WATERLOGGED)) world.getPendingFluidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
    }
    return state;
  }

  @Override
  @SuppressWarnings("deprecation")
  public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving)
  {}

  @Override
  @SuppressWarnings("deprecation")
  public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder)
  { return Collections.singletonList(new ItemStack(state.getBlock().asItem())); }

  @Override
  public boolean hasTileEntity(BlockState state)
  { return false; }

  @Override
  @SuppressWarnings("deprecation")
  public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player)
  {}

  @Override
  @SuppressWarnings("deprecation")
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
  { return ActionResultType.PASS; }

  @Override
  @SuppressWarnings("deprecation")
  public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rnd)
  {}

  // -------------------------------------------------------------------------------------------------------------------
  // Basic tile entity
  // -------------------------------------------------------------------------------------------------------------------

  /**
   * Main RsBlock derivate tile entity base
   */
  public static abstract class RsTileEntity extends TileEntity implements Networking.IPacketTileNotifyReceiver
  {
    private static final int NBT_ENTITY_TYPE = 1; // forge-doc: use 1, does not matter, only used by vanilla.

    public RsTileEntity(TileEntityType<?> te_type)
    { super(te_type); }

    public void write(CompoundNBT nbt, boolean updatePacket)
    {}

    public void read(CompoundNBT nbt, boolean updatePacket)
    {}

    protected final void syncToClients()
    {
      if(world.isRemote()) return;
      CompoundNBT nbt = new CompoundNBT();
      write(nbt, true);
      Networking.PacketTileNotifyServerToClient.sendToAllPlayers((ServerWorld)getWorld(), getPos(), nbt);
    }

    public final void onServerPacketReceived(CompoundNBT nbt)
    { read(nbt, true); }

    // --------------------------------------------------------------------------------------------------------
    // TileEntity
    // --------------------------------------------------------------------------------------------------------

    @Override
    public final CompoundNBT write(CompoundNBT nbt)
    { super.write(nbt); write(nbt, false); return nbt; }

    @Override
    public final void read(BlockState state, CompoundNBT nbt)
    { super.read(state, nbt); read(nbt, false); }
  }

}
