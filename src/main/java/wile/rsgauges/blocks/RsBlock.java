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

import wile.rsgauges.ModRsGauges;
import wile.rsgauges.detail.ModAuxiliaries;
import wile.rsgauges.detail.JitModelBakery;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.SoundType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import java.util.List;


public abstract class RsBlock extends Block
{
  public static final PropertyDirection FACING = PropertyDirection.create("facing");
  protected final AxisAlignedBB unrotatedBB;

  public RsBlock(String registryName, @Nullable AxisAlignedBB unrotatedBoundingBox, @Nullable Material material)
  {
    super((material!=null) ? (material) : (ModAuxiliaries.RsMaterials.MATERIAL_METALLIC));
    unrotatedBB = (unrotatedBoundingBox!=null) ? (unrotatedBoundingBox) : (new AxisAlignedBB(0,0,0,1,1,1));
    setCreativeTab(ModRsGauges.CREATIVE_TAB_RSGAUGES);
    setRegistryName(ModRsGauges.MODID, registryName);
    setTranslationKey(ModRsGauges.MODID + "." + registryName);
    setLightOpacity(0);
    setLightLevel(0);
    setResistance(2.0f);
    setTickRandomly(false);
    if(material != ModAuxiliaries.RsMaterials.MATERIAL_PLANT) {
      setHardness(0.3f);
    } else {
      setHardness(0.1f);
      setSoundType(SoundType.PLANT);
    }
  }

  public RsBlock(String registryName)
  { this(registryName, new AxisAlignedBB((0d/16),(1d/16),(0d/16), (8d/16),(12d/16),(2d/16)), null); }

  @SideOnly(Side.CLIENT)
  public void initModel()
  {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    JitModelBakery.JitBakedModel jitbakedmodel = getJitBakedModel();
    if(jitbakedmodel != null) JitModelBakery.initModelRegistrations(this, jitbakedmodel);
  }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isOpaqueCube(IBlockState state)
  { return false; }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isFullCube(IBlockState state)
  { return isCube(); }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isFullBlock(IBlockState state)
  { return false; }

  @Override
  @SuppressWarnings("deprecation")
  public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face)
  { return isCube() ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED; }

  @Override
  @Nullable
  @SuppressWarnings("deprecation")
  public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
  { return (!isCube()) ? (NULL_AABB) : (getBoundingBox(state, world, pos)); }

  @Override
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getRenderLayer()
  { return BlockRenderLayer.CUTOUT; }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, net.minecraft.client.particle.ParticleManager manager)
  { return true; } // no hit particles

  @Override
  @SideOnly(Side.CLIENT)
  public boolean addDestroyEffects(World world, BlockPos pos, net.minecraft.client.particle.ParticleManager manager)
  { return true; } // no destroy effects

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
  { ModAuxiliaries.Tooltip.addInformation(stack, world, tooltip, flag, true); }

  @Override
  public boolean canSpawnInBlock()
  { return false; }

  @Override
  @SuppressWarnings("deprecation")
  public EnumPushReaction getPushReaction(IBlockState state)
  { return isCube() ? EnumPushReaction.NORMAL : EnumPushReaction.DESTROY; }

  @Override
  public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
  { return (!isCube()); }

  @Override
  public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
  { return false; }

  @Override
  @SuppressWarnings("deprecation")
  public IBlockState getStateFromMeta(int meta)
  { return getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta & 0x7)); }

  @Override
  public int getMetaFromState(IBlockState state)
  { return (state.getValue(FACING).getIndex() & 0x07); }

  @Override
  protected BlockStateContainer createBlockState()
  { return new BlockStateContainer(this, FACING); }

  @Override
  @SuppressWarnings("deprecation")
  public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
  { return state; }

  @Override
  public boolean hasTileEntity(IBlockState state)
  { return false; }

  @Override
  @SuppressWarnings("deprecation")
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighbourPos)
  { neighborChangedCheck(state, world, pos, neighborBlock, neighbourPos); }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
  { onBlockPlacedByCheck(world, pos, state, placer, stack); }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
  { return (!isCube()); }

  @Override
  public void onBlockClicked(World world, BlockPos pos, EntityPlayer player)
  {}

  @Override
  public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side)
  { return canPlaceBlockOnSide(world, pos, side, (block)->(!isExceptBlockForAttachWithPiston(block)), null); }

  public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side, @Nullable Predicate<Block> blockWhiteListFilter, @Nullable Predicate<Block> blockBlackListFilter)
  {
    if(isCube()) {
      return true;
    } else if(isLateral() && (!isWallMount())) {
      if(side != EnumFacing.UP) return false; // must be supported from the bottom.
      final IBlockState state = world.getBlockState(pos.down());
      if((state==null) || (state.getBlockFaceShape(world, pos, EnumFacing.UP) != BlockFaceShape.SOLID)) return false;
      if(material!=ModAuxiliaries.RsMaterials.MATERIAL_PLANT) return true;
      return true;
    } else if(isWallMount()) {
      if(isLateral() && ((side==EnumFacing.UP)||(side==EnumFacing.DOWN))) return false; // lateral blocks only on walls.
      final BlockPos blockpos = pos.offset(side.getOpposite());
      final IBlockState state = world.getBlockState(blockpos);
      if((blockBlackListFilter!=null) && (blockBlackListFilter.apply(state.getBlock()))) return false;
      if(side == EnumFacing.UP) return (state.getBlockFaceShape(world, pos, EnumFacing.UP) == BlockFaceShape.SOLID);
      if((blockWhiteListFilter!=null) && (blockWhiteListFilter.apply(state.getBlock()))) return true;
      return (!isExceptionBlockForAttaching(state.getBlock())) && (state.getBlockFaceShape(world, blockpos, side) == BlockFaceShape.SOLID);
    } else {
      return (super.canPlaceBlockOnSide(world, pos, side));
    }
  }

  @Override
  public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
  {
    final IBlockState state = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
    if(isWallMount() && (!isLateral())) {
      return state.withProperty(FACING, facing); // e.g. pulse/bistable switches. Placed on the wall with the ui facing to the player.
    } else if(isWallMount() && isLateral()) {
      return state.withProperty(FACING, facing.getOpposite()); // e.g. trap door switch. Placed on the wall the player clicked, reverse orientation.
    } else if((!isWallMount()) && isLateral()) {
      return state.withProperty(FACING, placer.getHorizontalFacing()); // e.g. contact mats or full blocks, placed in the direction the player is looking.
    } else {
      // other switches, placed so that the UI side is facing the player. That is: the front is looking towards the player.
      // (south if the player is looking north when placing).
      final Vec3d lv = placer.getLookVec();
      return state.withProperty(FACING, EnumFacing.getFacingFromVector((float)lv.x, (float)lv.y, (float)lv.z));
    }
  }

  @Override
  public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
  {
    onRsBlockDestroyed(state, world, pos, false);
    return super.removedByPlayer(state, world, pos, player, willHarvest);
  }

  /**
   * Calculates the actual bounding box form the north-normalised block bounding box and
   * the facing defined in the block state.
   */
  @Override
  @SuppressWarnings("deprecation")
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
  {
    final AxisAlignedBB bb = getUnrotatedBB(state);
    if(!isLateral()) {
      // Wall attached blocks where the UI is facing to the player.
      switch(state.getValue(FACING).getIndex()) {
        case 0: return new AxisAlignedBB(1-bb.maxX, 1-bb.maxZ, 1-bb.maxY, 1-bb.minX, 1-bb.minZ, 1-bb.minY); // D
        case 1: return new AxisAlignedBB(1-bb.maxX,   bb.minZ,   bb.minY, 1-bb.minX,   bb.maxZ,   bb.maxY); // U
        case 2: return new AxisAlignedBB(1-bb.maxX,   bb.minY, 1-bb.maxZ, 1-bb.minX,   bb.maxY, 1-bb.minZ); // N
        case 3: return new AxisAlignedBB(  bb.minX,   bb.minY,   bb.minZ,   bb.maxX,   bb.maxY,   bb.maxZ); // S --> bb
        case 4: return new AxisAlignedBB(1-bb.maxZ,   bb.minY,   bb.minX, 1-bb.minZ,   bb.maxY,   bb.maxX); // W
        case 5: return new AxisAlignedBB(  bb.minZ,   bb.minY, 1-bb.maxX,   bb.maxZ,   bb.maxY, 1-bb.minX); // E
      }
    } else {
      // Wall or floor attached blocks where the UI and actuated facing is on the top.
      switch(state.getValue(FACING).getIndex()) {
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

  /**
   * Overridden to indicate that the block is attached to a wall. getBoundingBox() and
   * getStateForPlacement() will return differently accordingly.
   */
  public boolean isWallMount()
  { return false; }

  /**
   * Overridden to indicate that the block is attached to the floor. getBoundingBox() and
   * getStateForPlacement() will return differently accordingly.
   */
  public boolean isLateral()
  { return false; }

  /**
   * Overridden to indicate that the block is a standard cube, cannot be washed off, and is
   * not explicitly attached to another block, so that it would pop off when that block,
   * is destroyed. Also implies Block class overrides for cubes.
   */
  public boolean isCube()
  { return false; }

  /**
   * Returns the bounding box facing north. For other facing direction
   * block states, the corresponding facing will be calculated from this
   * data.
   */
  public AxisAlignedBB getUnrotatedBB()
  { return unrotatedBB; }

  public AxisAlignedBB getUnrotatedBB(IBlockState state)
  { return getUnrotatedBB(); }

  /**
   * Lookup table for fast transformation from placed block facing to the absolute world facing.
   */
  private static final EnumFacing[][] fast_transform_lut = {
    { EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.UP, EnumFacing.DOWN, EnumFacing.WEST, EnumFacing.EAST }, // DOWN
    { EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.DOWN, EnumFacing.UP, EnumFacing.WEST, EnumFacing.EAST }, // UP
    { EnumFacing.DOWN, EnumFacing.UP, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.WEST, EnumFacing.EAST }, // NORTH
    { EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.EAST, EnumFacing.WEST }, // SOUTH
    { EnumFacing.DOWN, EnumFacing.UP, EnumFacing.EAST, EnumFacing.WEST, EnumFacing.SOUTH, EnumFacing.NORTH }, // WEST
    { EnumFacing.DOWN, EnumFacing.UP, EnumFacing.WEST, EnumFacing.EAST, EnumFacing.NORTH, EnumFacing.SOUTH }  // EAST
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
  protected EnumFacing getAbsoluteFacing(IBlockState state, EnumFacing relativeSide)
  { return ((state==null) || (relativeSide==null)) ? EnumFacing.NORTH : fast_transform_lut[state.getValue(FACING).getIndex()][relativeSide.getIndex()]; }

  /**
   * Returns the JIT model bakery instance for custom rendering, or null
   * if no custom rendering applies.
   */
  public JitModelBakery.JitBakedModel getJitBakedModel()
  { return null; }

  /**
   * RsBlock handler before a block gets dropped as item in the world.
   * Allows actions in the tile entity to happen before the forge/MC
   * block dropping actions are invoked.
   */
  protected void onRsBlockDestroyed(IBlockState state, World world, BlockPos pos, boolean isUpdateEvent)
  {}

  /**
   * Checks if the changed neighbour is the block where the gauge/switch/device
   * is attached to. If this block cannot hold the device (air, water...), then
   * the device pop off and dropped as item.
   * Returns true if the neighbour block change may affect the state of the device.
   */
  protected boolean neighborChangedCheck(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos)
  {
    if(isCube()) {
      return true;
    } else if(!isWallMount()) {
      if(!(pos.down().equals(neighborPos))) return false;
    } else if(!isLateral()) {
      if(!pos.offset(state.getValue(FACING).getOpposite()).equals(neighborPos)) return false;
    } else {
      if(!pos.offset(state.getValue(FACING)).equals(neighborPos)) return false;
    }
    final IBlockState neighborState = world.getBlockState(neighborPos);
    if(neighborState == null) return false;
    if((world.isAirBlock(neighborPos)) || (neighborState.getMaterial().isLiquid())) {
      if(!world.isRemote) {
        onRsBlockDestroyed(state, world, pos, true);
        world.setBlockToAir(pos);
        dropBlockAsItem(world, pos, state, 0);
      }
      return false;
    }
    return true;
  }

  /**
   * Checks if these blocks can be placed at a given position with a given facing. The client does not send a
   * placing request if not, the server will drop the item if it was placed somehow.
   */
  protected boolean onBlockPlacedByCheck(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
  {
    if(isCube() || canPlaceBlockOnSide(world, pos, isWallMount() ? state.getValue(FACING) : EnumFacing.UP)) return true;
    if(world.isRemote) return false;
    onRsBlockDestroyed(state, world, pos, false);
    dropBlockAsItem(world, pos, state, 0);
    world.setBlockToAir(pos);
    return false;
  }

  /**
   * Main RsBlock derivate tile entity base
   */
  public static class RsTileEntity extends TileEntity
  {
    private static final int NBT_ENTITY_TYPE = 1; // forge-doc: use 1, does not matter, only used ba vanilla.

    public void writeNbt(NBTTagCompound nbt, boolean updatePacket) {} // overridden if NBT is needed
    public void readNbt(NBTTagCompound nbt, boolean updatePacket)  {} // overridden if NBT is needed

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState os, IBlockState ns)
    { return (os.getBlock() != ns.getBlock()) || (!(os.getBlock() instanceof RsBlock)) || (!(ns.getBlock() instanceof RsBlock)); } // Tile entity re-creation condition.

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    { super.writeToNBT(nbt); writeNbt(nbt, false); return nbt; }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    { super.readFromNBT(nbt); readNbt(nbt, false); }

    @Override // on client
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    { super.readFromNBT(pkt.getNbtCompound()); readNbt(pkt.getNbtCompound(), true); super.onDataPacket(net, pkt); }

    @Override // on server
    public NBTTagCompound getUpdateTag()
    { NBTTagCompound nbt = new NBTTagCompound(); super.writeToNBT(nbt); writeNbt(nbt, true); return nbt; }

    @Override // on server
    public SPacketUpdateTileEntity getUpdatePacket()
    { return new SPacketUpdateTileEntity(pos, NBT_ENTITY_TYPE, getUpdateTag()); }

    @Override // on client
    public void handleUpdateTag(NBTTagCompound tag)
    { readFromNBT(tag); }

    @SideOnly(Side.CLIENT)
    @Override
    public double getMaxRenderDistanceSquared()
    { return 48 * 48; } // TESR

    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getRenderBoundingBox()
    { return new AxisAlignedBB(getPos(), getPos().add(1, 1, 1)); } // TESR
  }

}
