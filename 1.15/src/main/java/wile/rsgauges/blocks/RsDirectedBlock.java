/*
 * @file RsDirectedBlock.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2019 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Smaller (cutout) block with a defined facing.
 */
package wile.rsgauges.blocks;

import net.minecraft.block.Blocks;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.block.Block;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.BlockState;
import net.minecraft.world.IBlockReader;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import javax.annotation.Nullable;

public class RsDirectedBlock extends RsBlock
{
  public static final long RSBLOCK_CONFIG_WALLMOUNT         = 0x4000000000000000l;
  public static final long RSBLOCK_CONFIG_LATERAL           = 0x8000000000000000l;
  public static final long RSBLOCK_CONFIG_FULLCUBE          = 0x0000000000000000l; // !RSBLOCK_CONFIG_WALLMOUNT&&!RSBLOCK_CONFIG_LATERAL
  public static final long RSBLOCK_CONFIG_OPOSITE_PLACEMENT = 0x0800000000000000l;

  public static final DirectionProperty FACING = DirectionalBlock.FACING;
  protected final AxisAlignedBB unrotatedBB;

  // -------------------------------------------------------------------------------------------------------------------

  public RsDirectedBlock(long config, Block.Properties builder, final @Nullable AxisAlignedBB aabb)
  {
    super(
      config | (((config & RSBLOCK_CONFIG_TRANSLUCENT) == RSBLOCK_CONFIG_SOLID) && ((aabb.getXSize()<0.99) || (aabb.getYSize()<0.99) || (aabb.getXSize()<0.99)) ? RSBLOCK_CONFIG_CUTOUT : 0),
      builder
    );
    setDefaultState(stateContainer.getBaseState().with(FACING, Direction.NORTH));
    unrotatedBB = (aabb!=null) ? (aabb) : (new AxisAlignedBB(0,0,0,1,1,1));
  }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader source, BlockPos pos, ISelectionContext selectionContext)
  { return VoxelShapes.create(getBoundingBox(state)); }

  @Override
  public VoxelShape getCollisionShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext selectionContext)
  { return getShape(state, world, pos, selectionContext); }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
  { builder.add(FACING); }

  @Override
  @Nullable
  public BlockState getStateForPlacement(BlockItemUseContext context)
  {
    if(!isValidPositionOnSide(context.getWorld(), context.getPos(), context.getFace())) return null;
    Direction facing;
    if(isWallMount() && (!isLateral())) {
      facing = context.getFace(); // e.g. pulse/bistable switches. Placed on the wall with the ui facing to the player.
    } else if(isWallMount() && isLateral()) {
      facing = context.getFace().getOpposite(); // e.g. trap door switch. Placed on the wall the player clicked, reverse orientation.
    } else if((!isWallMount()) && isLateral()) {
      facing = context.getPlacementHorizontalFacing(); // e.g. contact mats or full blocks, placed in the direction the player is looking.
    } else {
      facing = context.getNearestLookingDirection();
    }
    if(isOpositePlacement()) facing = facing.getOpposite();
    return getDefaultState().with(FACING, facing);
  }

  //  @Override
  //  @SuppressWarnings("deprecation")
  //  public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos)
  //  { return true; } // Nope: that is already called for the AIR before placing a switch. return isValidPositionOnSide(world, pos, world.getBlockState(pos).get(FACING).getOpposite());

  @SuppressWarnings("unused")
  @Override
  public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos)
  {
    if(isCube() || ((!world.isAirBlock(facingPos)) && (!facingState.getMaterial().isLiquid()))) return state;
    boolean iswall = isWallMount();
    boolean islat = isLateral();
    Direction blockfacing = state.get(FACING);
    if((!isWallMount()) && (isLateral()) && (facing==Direction.DOWN)) return Blocks.AIR.getDefaultState(); // floor mount, e.g. contact mats
    if(isWallMount() && (!isLateral()) && (facing==state.get(FACING).getOpposite())) return Blocks.AIR.getDefaultState(); // wallmount are placed facing the player
    if(isWallMount() && (isLateral()) && (facing==state.get(FACING))) return Blocks.AIR.getDefaultState();  // trapdoors etc are placed facing the neighbour block
    return state;
  }

  // -------------------------------------------------------------------------------------------------------------------
  // Mod specific
  // -------------------------------------------------------------------------------------------------------------------

  /**
   * Overridden to indicate that the block is attached to a wall. getBoundingBox() and
   * getStateForPlacement() will return differently accordingly.
   */
  public boolean isWallMount()
  { return (config & RSBLOCK_CONFIG_WALLMOUNT) != 0; }

  /**
   * Overridden to indicate that the block is attached to the floor. getBoundingBox() and
   * getStateForPlacement() will return differently accordingly.
   */
  public boolean isLateral()
  { return (config & RSBLOCK_CONFIG_LATERAL) != 0; }

  /**
   * Overridden to indicate that the block is a standard cube, cannot be washed off, and is
   * not explicitly attached to another block, so that it would pop off when that block,
   * is destroyed. Also implies Block class overrides for cubes.
   */
  public boolean isCube()
  { return (config & (RSBLOCK_CONFIG_LATERAL|RSBLOCK_CONFIG_WALLMOUNT)) == 0; }

  public boolean isOpositePlacement()
  { return (config & (RSBLOCK_CONFIG_OPOSITE_PLACEMENT)) != 0; }

  /**
   * Returns the bounding box facing north. For other facing direction
   * block states, the corresponding facing will be calculated from this
   * data.
   */
  public AxisAlignedBB getUnrotatedBB()
  { return unrotatedBB; }

  public AxisAlignedBB getUnrotatedBB(BlockState state)
  { return getUnrotatedBB(); }

  /**
   * Checks if the changed neighbour is the block where the gauge/switch/device
   * is attached to. If this block cannot hold the device (air, water...), then
   * the device pop off and dropped as item.
   * Returns true if the neighbour block change may affect the state of the device.
   */
  protected boolean isAffectedByNeigbour(BlockState state, IWorld world, BlockPos pos, BlockPos neighborPos)
  {
    if(isCube()) {
      return true;
    } else if(!isWallMount()) {
      if(!(pos.down().equals(neighborPos))) return false;
    } else if(!isLateral()) {
      if(!pos.offset(state.get(FACING).getOpposite()).equals(neighborPos)) return false;
    } else {
      if(!pos.offset(state.get(FACING)).equals(neighborPos)) return false;
    }
    final BlockState neighborState = world.getBlockState(neighborPos);
    if(neighborState == null) return false;
    if((world.isAirBlock(neighborPos)) || (neighborState.getMaterial().isLiquid())) {
//      if((world instanceof World) && (!((World)world).isRemote)) world.destroyBlock(pos, true);
      return false;
    }
    return true;
  }

  /**
   * Lookup table for fast transformation from placed block facing to the absolute world facing.
   */
  private static final Direction[][] fast_transform_lut = {
    { Direction.SOUTH, Direction.NORTH, Direction.UP, Direction.DOWN, Direction.WEST, Direction.EAST }, // DOWN
    { Direction.NORTH, Direction.SOUTH, Direction.DOWN, Direction.UP, Direction.WEST, Direction.EAST }, // UP
    { Direction.DOWN, Direction.UP, Direction.SOUTH, Direction.NORTH, Direction.WEST, Direction.EAST }, // NORTH
    { Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST }, // SOUTH
    { Direction.DOWN, Direction.UP, Direction.EAST, Direction.WEST, Direction.SOUTH, Direction.NORTH }, // WEST
    { Direction.DOWN, Direction.UP, Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH }  // EAST
  };

  /**
   * Transforms a facing from the relative side (front, back, left, right, top, bottom)
   * to an absolute direction (north, south ...). The convention for the relative side
   * is: front=south, hence back=north, right=east, left=west, top=up, bottom=down. This
   * is not standard (like e.g. a furnace), but fatilitates model design (e.g. blockbench:
   * looking north/having xy as known from 2D coord systems means that the front of the
   * device is looking towards you) and player perspective (the front is facing the player
   * when the block is placed, left and right etc is from the player perspective when
   * standing in front of the device).
   */
  protected Direction getAbsoluteFacing(BlockState state, Direction relativeSide)
  { return ((state==null) || (relativeSide==null)) ? Direction.NORTH : fast_transform_lut[state.get(FACING).getIndex()][relativeSide.getIndex()]; }


  /**
   * Checks if these blocks can be placed at a given position with a given facing. The client does not send a
   * placing request if not, the server will drop the item if it was placed somehow.
   */
  protected boolean onBlockPlacedByCheck(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack)
  {
    if(isCube() || isValidPositionOnSide(world, pos, isWallMount() ? state.get(FACING) : Direction.UP)) return true;
    if(world.isRemote) return false;
    onRsBlockDestroyed(state, world, pos, false);
    world.destroyBlock(pos, true);
    return false;
  }

  /**
   * Calculates the actual bounding box form the north-normalised block bounding box and
   * the facing defined in the block state.
   */
  public AxisAlignedBB getBoundingBox(BlockState state)
  {
    final AxisAlignedBB bb = getUnrotatedBB(state);
    if(!isLateral()) {
      // Wall attached blocks where the UI is facing to the player.
      switch(state.get(FACING).getIndex()) {
        case 0: return new AxisAlignedBB(1-bb.maxX, 1-bb.maxZ, 1-bb.maxY, 1-bb.minX, 1-bb.minZ, 1-bb.minY); // D
        case 1: return new AxisAlignedBB(1-bb.maxX,   bb.minZ,   bb.minY, 1-bb.minX,   bb.maxZ,   bb.maxY); // U
        case 2: return new AxisAlignedBB(1-bb.maxX,   bb.minY, 1-bb.maxZ, 1-bb.minX,   bb.maxY, 1-bb.minZ); // N
        case 3: return new AxisAlignedBB(  bb.minX,   bb.minY,   bb.minZ,   bb.maxX,   bb.maxY,   bb.maxZ); // S --> bb
        case 4: return new AxisAlignedBB(1-bb.maxZ,   bb.minY,   bb.minX, 1-bb.minZ,   bb.maxY,   bb.maxX); // W
        case 5: return new AxisAlignedBB(  bb.minZ,   bb.minY, 1-bb.maxX,   bb.maxZ,   bb.maxY, 1-bb.minX); // E
      }
    } else {
      // Wall or floor attached blocks where the UI and actuated facing is on the top.
      switch(state.get(FACING).getIndex()) {
        case 0: return new AxisAlignedBB(  bb.minX, bb.minY,   bb.minZ,   bb.maxX, bb.maxY,   bb.maxZ); // D --> bb
        case 1: return new AxisAlignedBB(  bb.minX, bb.minY,   bb.minZ,   bb.maxX, bb.maxY,   bb.maxZ); // U --> bb
        case 2: return new AxisAlignedBB(  bb.minX, bb.minY,   bb.minZ,   bb.maxX, bb.maxY,   bb.maxZ); // N --> bb
        case 3: return new AxisAlignedBB(1-bb.maxX, bb.minY, 1-bb.maxZ, 1-bb.minX, bb.maxY, 1-bb.minZ); // S
        case 4: return new AxisAlignedBB(  bb.minZ, bb.minY, 1-bb.maxX,   bb.maxZ, bb.maxY, 1-bb.minX); // W
        case 5: return new AxisAlignedBB(1-bb.maxZ, bb.minY,   bb.minX, 1-bb.minZ, bb.maxY,   bb.maxX); // E
      }
    }
    return bb;
  }

  protected boolean isValidPositionOnSide(IWorldReader world, BlockPos pos, Direction side)
  {
    if(isCube()) {
      return true;
    } else if(isLateral() && (!isWallMount())) {
      if(side != Direction.UP) return false; // must be supported from the bottom.
      if(!Block.hasSolidSide(world.getBlockState(pos.down()), world, pos, Direction.UP)) return false;
      // if(material != ModAuxiliaries.RsMaterials.MATERIAL_PLANT) return true;
      return true;
    } else if(isWallMount()) {
      if(isLateral() && ((side == Direction.UP) || (side == Direction.DOWN))) return false; // lateral blocks only on walls.
      final BlockPos blockpos = pos.offset(side.getOpposite());
      final BlockState state = world.getBlockState(blockpos);
      if(side == Direction.UP) return Block.hasSolidSide(world.getBlockState(pos.down()), world, pos, Direction.UP);
    }
    return true;
  }
}
