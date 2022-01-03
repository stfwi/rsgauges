/*
 * @file RsDirectedBlock.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2019 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Smaller (cutout) block with a defined facing.
 */
package wile.rsgauges.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;


public abstract class RsDirectedBlock extends RsBlock
{
  public static final long RSBLOCK_CONFIG_WALLMOUNT         = 0x4000000000000000l;
  public static final long RSBLOCK_CONFIG_LATERAL           = 0x8000000000000000l;
  public static final long RSBLOCK_CONFIG_FULLCUBE          = 0x0000000000000000l;
  public static final long RSBLOCK_CONFIG_OPOSITE_PLACEMENT = 0x0800000000000000l;

  public static final DirectionProperty FACING = DirectionalBlock.FACING;
  protected final VoxelShape[][] shapes_;

  // -------------------------------------------------------------------------------------------------------------------

  public RsDirectedBlock(long config, BlockBehaviour.Properties properties, @Nullable AABB aabb1, @Nullable AABB aabb2)
  {
    super(config | (((config & RSBLOCK_CONFIG_TRANSLUCENT)==0) && ((aabb1.getXsize()<0.99) || (aabb1.getYsize()<0.99) || (aabb1.getXsize()<0.99)) ? RSBLOCK_CONFIG_CUTOUT : 0), properties);
    registerDefaultState(super.defaultBlockState().setValue(FACING, Direction.SOUTH));
    VoxelShape[][] shapes = new VoxelShape[Direction.values().length][2];
    if(aabb1==null) aabb1 = new AABB(0,0,0,1,1,1);
    if(aabb2==null) aabb2 = aabb1;
    for(int i_dir=0; i_dir<Direction.values().length; ++i_dir) {
      for(int i_pow=0; i_pow<2; ++i_pow) {
        AABB bb = (i_pow==0) ? aabb1 : aabb2;
        if((config & RSBLOCK_CONFIG_LATERAL) == 0) {
          // Wall attached blocks where the UI is facing to the player.
          switch (i_dir) {
            case 0 -> bb = new AABB(1 - bb.maxX, 1 - bb.maxZ, 1 - bb.maxY, 1 - bb.minX, 1 - bb.minZ, 1 - bb.minY);
            // D
            case 1 -> bb = new AABB(1 - bb.maxX, bb.minZ, bb.minY, 1 - bb.minX, bb.maxZ, bb.maxY);
            // U
            case 2 -> bb = new AABB(1 - bb.maxX, bb.minY, 1 - bb.maxZ, 1 - bb.minX, bb.maxY, 1 - bb.minZ);
            // N
            case 3 -> bb = new AABB(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
            // S --> bb
            case 4 -> bb = new AABB(1 - bb.maxZ, bb.minY, bb.minX, 1 - bb.minZ, bb.maxY, bb.maxX);
            // W
            case 5 -> bb = new AABB(bb.minZ, bb.minY, 1 - bb.maxX, bb.maxZ, bb.maxY, 1 - bb.minX);
            // E
          }
        } else {
          // Wall or floor attached blocks where the UI and actuated facing is on the top.
          // E
          bb = switch (i_dir) { // U --> bb
            // N --> bb
            case 0, 1, 2 -> new AABB(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ); // D --> bb
            case 3 -> new AABB(1 - bb.maxX, bb.minY, 1 - bb.maxZ, 1 - bb.minX, bb.maxY, 1 - bb.minZ); // S
            case 4 -> new AABB(bb.minZ, bb.minY, 1 - bb.maxX, bb.maxZ, bb.maxY, 1 - bb.minX); // W
            case 5 -> new AABB(1 - bb.maxZ, bb.minY, bb.minX, 1 - bb.minZ, bb.maxY, bb.maxX);
            default -> bb;
          };
        }
        shapes[i_dir][i_pow] = Shapes.create(bb);
      }
    }
    shapes_ = shapes;
  }

  // -------------------------------------------------------------------------------------------------------------------
  // Block overrides
  // -------------------------------------------------------------------------------------------------------------------

  protected VoxelShape getShape(BlockState state)
  { return shapes_[state.getValue(FACING).get3DDataValue()][0]; }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter source, BlockPos pos, CollisionContext selectionContext)
  { return getShape(state); }

  @Override
  public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext selectionContext)
  { return getShape(state, world, pos, selectionContext); }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
  { super.createBlockStateDefinition(builder); builder.add(FACING); }

  @Override
  @Nullable
  public BlockState getStateForPlacement(BlockPlaceContext context)
  {
    if(!isValidPositionOnSide(context.getLevel(), context.getClickedPos(), context.getClickedFace())) return null;
    final BlockState state = super.getStateForPlacement(context);
    if(state==null) return null;
    Direction facing;
    if(isWallMount() && (!isLateral())) {
      facing = context.getClickedFace(); // e.g. pulse/bistable switches. Placed on the wall with the ui facing to the player.
    } else if(isWallMount() && isLateral()) {
      facing = context.getClickedFace().getOpposite(); // e.g. trap door switch. Placed on the wall the player clicked, reverse orientation.
    } else if((!isWallMount()) && isLateral()) {
      facing = context.getHorizontalDirection(); // e.g. contact mats or full blocks, placed in the direction the player is looking.
    } else {
      facing = context.getNearestLookingDirection();
    }
    if(isOpositePlacement()) facing = facing.getOpposite();
    return state.setValue(FACING, facing);
  }

  @SuppressWarnings("unused")
  @Override
  public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos)
  {
    if(isCube() || ((!world.isEmptyBlock(facingPos)) && (!facingState.getMaterial().isLiquid()))) return state;
    Direction blockfacing = state.getValue(FACING);
    if((!isWallMount()) && (isLateral()) && (facing==Direction.DOWN)) return Blocks.AIR.defaultBlockState(); // floor mount, e.g. contact mats
    if(isWallMount() && (!isLateral()) && (facing==state.getValue(FACING).getOpposite())) return Blocks.AIR.defaultBlockState(); // wallmount are placed facing the player
    if(isWallMount() && (isLateral()) && (facing==state.getValue(FACING))) return Blocks.AIR.defaultBlockState();  // trapdoors etc are placed facing the neighbour block
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
   * Checks if the changed neighbour is the block where the gauge/switch/device
   * is attached to. If this block cannot hold the device (air, water...), then
   * the device pop off and dropped as item.
   * Returns true if the neighbour block change may affect the state of the device.
   */
  protected boolean isAffectedByNeigbour(BlockState state, LevelAccessor world, BlockPos pos, BlockPos neighborPos)
  {
    if(isCube()) return true;
    if((!isWallMount()) && (!(pos.below().equals(neighborPos)))) return false;
    if((!isLateral()) && (!pos.relative(state.getValue(FACING).getOpposite()).equals(neighborPos))) return false;
    if(!pos.relative(state.getValue(FACING).getOpposite()).equals(neighborPos)) return false;
    final BlockState neighborState = world.getBlockState(neighborPos);
    if(neighborState == null) return false;
    if((world.isEmptyBlock(neighborPos)) || (neighborState.getMaterial().isLiquid())) return false;
    return true;
  }

  /**
   * Lookup table for fast transformation from placed block facing to the absolute world facing.
   */
  private static final Direction[][] facing_transform_lut = {
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
  { return ((state==null) || (relativeSide==null)) ? Direction.NORTH : facing_transform_lut[state.getValue(FACING).get3DDataValue()][relativeSide.get3DDataValue()]; }

  protected boolean isValidPositionOnSide(LevelReader world, BlockPos pos, Direction side)
  {
    if(isCube()) {
      return true;
    } else if(isLateral() && (!isWallMount())) {
      if(side != Direction.UP) return false; // must be supported from the bottom.
      if(!Block.canSupportRigidBlock(world, pos.below())) return false;
      return true;
    } else if(isWallMount()) {
      if(isLateral() && ((side == Direction.UP) || (side == Direction.DOWN))) return false; // lateral blocks only on walls.
      final BlockPos blockpos = pos.relative(side.getOpposite());
      final BlockState state = world.getBlockState(blockpos);
    }
    return true;
  }
}
