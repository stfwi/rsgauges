/**
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
**/
package wile.rsgauges.blocks;

import wile.rsgauges.ModConfig;
import wile.rsgauges.ModRsGauges;
import wile.rsgauges.client.JitModelBakery;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.world.chunk.*;
import net.minecraft.world.ChunkCache;
import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;


public abstract class RsBlock extends Block {

  public static final PropertyDirection FACING = PropertyDirection.create("facing");
  protected final AxisAlignedBB unrotatedBB;

  public RsBlock(String registryName, AxisAlignedBB unrotatedBoundingBox) {
    super(Material.CIRCUITS);
    setCreativeTab(CreativeTabs.REDSTONE);
    setRegistryName(registryName);
    setUnlocalizedName(ModRsGauges.MODID + "." + registryName);
    setLightOpacity(0);
    setLightLevel(0);
    setHardness(0.3f);
    setResistance(2.0f);
    setTickRandomly(false);
    this.unrotatedBB = unrotatedBoundingBox;
  }

  public RsBlock(String registryName) { this(registryName, new AxisAlignedBB((0d/16),(1d/16),(0d/16), (8d/16),(12d/16),(2d/16))); }

  @SideOnly(Side.CLIENT)
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    JitModelBakery.JitBakedModel jitbakedmodel = this.getJitBakedModel();
    if(jitbakedmodel != null) JitModelBakery.initModelRegistrations(this, jitbakedmodel);
  }

  @Override
  public boolean isOpaqueCube(IBlockState state) { return false; }

  @Override
  public boolean isFullCube(IBlockState state) { return false; }

  @Override
  public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) { return BlockFaceShape.UNDEFINED; }

  @Override
  @Nullable
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) { return NULL_AABB; }

  @SideOnly(Side.CLIENT)
  @Override
  public BlockRenderLayer getBlockLayer() { return BlockRenderLayer.CUTOUT; }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, net.minecraft.client.particle.ParticleManager manager) { return true; } // no hit particles

  @Override
  @SideOnly(Side.CLIENT)
  public boolean addDestroyEffects(World world, BlockPos pos, net.minecraft.client.particle.ParticleManager manager) { return true; } // no destroy

  @Override
  public boolean canSpawnInBlock() { return false; }

  @Override
  public boolean isPassable(IBlockAccess worldIn, BlockPos pos) { return true; }

  @Override
  public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) { return false; }

  @Override
  public IBlockState getStateFromMeta(int meta) { return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 0x7)); }

  @Override
  public int getMetaFromState(IBlockState state) { return (state.getValue(FACING).getIndex() & 0x07); }

  @Override
  protected BlockStateContainer createBlockState() { return new BlockStateContainer(this, FACING); }

  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) { return state; }

  @Override
  public boolean hasTileEntity(IBlockState state) { return false; }

  @Override
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos) { this.neighborChangedCheck(state, world, pos, neighborBlock, neighborPos); }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) { this.onBlockPlacedByCheck(world, pos, state, placer, stack); }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) { return true; }

  @Override
  public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {}

  @Override
  public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) { return canPlaceBlockOnSide(world, pos, side, null, null); }

  public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side, @Nullable Predicate<Block> blockWhiteListFilter, @Nullable Predicate<Block> blockBlackListFilter) {
    if(isFloorMount()) {
      if(side != EnumFacing.UP) return false;
      return (world.getBlockState(pos.down()).getBlockFaceShape(world, pos, EnumFacing.UP) == BlockFaceShape.SOLID);
    } else {
      BlockPos blockpos = pos.offset(side.getOpposite());
      IBlockState state = world.getBlockState(blockpos);
      if((blockBlackListFilter!=null) && (blockBlackListFilter.apply(state.getBlock()))) return false;
      if(side == EnumFacing.UP) return (state.getBlockFaceShape(world, pos, EnumFacing.UP) == BlockFaceShape.SOLID);
      if((blockWhiteListFilter!=null) && (blockWhiteListFilter.apply(state.getBlock()))) return true;
      return (!isExceptBlockForAttachWithPiston(state.getBlock())) && (state.getBlockFaceShape(world, blockpos, side) == BlockFaceShape.SOLID);
    }
  }

  @Override
  public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
    IBlockState state = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand);
    if(isWallMount()) {
      return state.withProperty(FACING, facing);
    } else {
      return state.withProperty(FACING, placer.getHorizontalFacing()); // floor mount and blocky blocks
    }
  }

  /**
   * Calculates the actual bounding box form the north-normalised block bounding box and
   * the facing defined in the block state.
   */
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    AxisAlignedBB bb = getUnrotatedBB(state);
    if(isWallMount()) {
      switch(state.getValue(FACING).getIndex()) {
        case 0: return new AxisAlignedBB(1-bb.maxX, 1-bb.maxZ, 1-bb.maxY, 1-bb.minX, 1-bb.minZ, 1-bb.minY); // D
        case 1: return new AxisAlignedBB(1-bb.maxX,   bb.minZ,   bb.minY, 1-bb.minX,   bb.maxZ,   bb.maxY); // U
        case 2: return new AxisAlignedBB(1-bb.maxX,   bb.minY, 1-bb.maxZ, 1-bb.minX,   bb.maxY, 1-bb.minZ); // N
        case 3: return new AxisAlignedBB(  bb.minX,   bb.minY,   bb.minZ,   bb.maxX,   bb.maxY,   bb.maxZ); // S --> bb
        case 4: return new AxisAlignedBB(1-bb.maxZ,   bb.minY,   bb.minX, 1-bb.minZ,   bb.maxY,   bb.maxX); // W
        case 5: return new AxisAlignedBB(  bb.minZ,   bb.minY, 1-bb.maxX,   bb.maxZ,   bb.maxY, 1-bb.minX); // E
      }
    } else if(isFloorMount()) {
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
  public boolean isWallMount() { return false; }

  /**
   * Overridden to indicate that the block is attached to the floor. getBoundingBox() and
   * getStateForPlacement() will return differently accordingly.
   */
  public boolean isFloorMount() { return false; }

  /**
   * Returns the bounding box facing north. For other facing direction
   * block states, the corresponding facing will be calculated from this
   * data.
   */
  public AxisAlignedBB getUnrotatedBB() { return unrotatedBB; }
  public AxisAlignedBB getUnrotatedBB(IBlockState state) { return getUnrotatedBB(); }

  /**
   * Returns the JIT model bakery instance for custom rendering, or null
   * if no custom rendering applies.
   */
  public JitModelBakery.JitBakedModel getJitBakedModel() { return null; }

  /**
   * Checks if the changed neighbour is the block where the gauge/switch/device
   * is attached to. If this block cannot hold the device (air, water...), then
   * the device pop off and dropped as item.
   * Returns true if the neighbour block change may affect the state of the device.
   */
  protected boolean neighborChangedCheck(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos) {
    if(isFloorMount()) {
      if(!(pos.down().equals(neighborPos))) return false;
    } else {
      if(!pos.offset(state.getValue(FACING).getOpposite()).equals(neighborPos)) return false;  // wall mount
    }
    IBlockState neighborState = world.getBlockState(neighborPos);
    if(neighborState == null) return false;
    if(((neighborState.getMaterial() == Material.AIR) || (neighborState.getMaterial() == Material.WATER) || (neighborState.getMaterial() == Material.LAVA))) {
      if(!world.isRemote) {
        this.dropBlockAsItem(world, pos, state, 0);
        world.setBlockToAir(pos);
      }
      return false;
    }
    return true;
  }

  protected boolean onBlockPlacedByCheck(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    if(canPlaceBlockOnSide(world, pos, this.isFloorMount() ? EnumFacing.UP : state.getValue(FACING))) return true;
    if(world.isRemote) return false;
    this.dropBlockAsItem(world, pos, state, 0);
    world.setBlockToAir(pos);
    return false;
  }

  /**
   * Main RsBlock derivate tile entity base
   */
  public static class RsTileEntity<BlockType extends RsBlock> extends TileEntity
  {
    private static final int NBT_ENTITY_TYPE = 1; // @todo: no idea yet what effect this value actually has -> double check

    public void writeNbt(NBTTagCompound nbt, boolean updatePacket) {} // overridden if NBT is needed
    public void readNbt(NBTTagCompound nbt, boolean updatePacket)  {} // overridden if NBT is needed

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState os, IBlockState ns) {
      return (os.getBlock() != ns.getBlock()) || (!(os.getBlock() instanceof RsBlock)) || (!(ns.getBlock() instanceof RsBlock));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) { super.writeToNBT(nbt); this.writeNbt(nbt, false); return nbt; }

    @Override
    public void readFromNBT(NBTTagCompound nbt) { super.readFromNBT(nbt); this.readNbt(nbt, false); }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
      super.readFromNBT(pkt.getNbtCompound());
      this.readNbt(pkt.getNbtCompound(), true);
      super.onDataPacket(net, pkt);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
      NBTTagCompound nbt = new NBTTagCompound();
      super.writeToNBT(nbt);
      this.writeNbt(nbt, true);
      return nbt;
    }

    @Override // Only on server.
    public SPacketUpdateTileEntity getUpdatePacket() { return new SPacketUpdateTileEntity(pos, NBT_ENTITY_TYPE, getUpdateTag()); }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) { this.readFromNBT(tag); }

    @SideOnly(Side.CLIENT)
    @Override
    public double getMaxRenderDistanceSquared() { return 48 * 48; } // TESR

    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getRenderBoundingBox() { return new AxisAlignedBB(getPos(), getPos().add(1, 1, 1)); } // TESR
  }

  /**
   * Data class with static factory function to enable unified block activation
   * click coordinates with respect to the device (UI) interface facing, normalised
   * to 0..15. On `wrenched==true` the face was clicked with an valid wrench. If
   * `accepted==false`, the block was not clicked on the main display facing side,
   * and remaining instance data are not valid.
   */
  protected static final class WrenchActivationCheck
  {
    public boolean accepted = false;
    public boolean wrenched = false;
    public int redstone = 0;
    public double x = 0;
    public double y = 0;

    @Override
    public String toString() {
      return "{x:" + Double.toString(x) + ",y:" + Double.toString(y) + ",accepted:" + Boolean.toString(accepted) + ",wrenched:" + Boolean.toString(wrenched) + "}";
    }

    public static boolean wrenched(EntityPlayer player) {
      ItemStack item = player.getHeldItemMainhand();
      return (item != null) && ((","+ModConfig.accepted_wrenches+",").indexOf(","+item.getItem().getRegistryName().getResourcePath() + ",") >= 0);
    }

    public static WrenchActivationCheck onBlockActivatedCheck(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float x, float y, float z) {
      WrenchActivationCheck ck = new WrenchActivationCheck();
      if(!(state.getBlock() instanceof RsBlock)) return ck;
      RsBlock block = (RsBlock)(state.getBlock());
      if(block.isFloorMount() && (facing != EnumFacing.UP)) return ck;
      if(block.isWallMount() && (facing != state.getValue(FACING))) return ck;
      ck.wrenched = wrenched(player);
      if(!ck.wrenched) {
        ItemStack item = player.getHeldItemMainhand();
        if((item != null) && (item.isStackable()) && (item.getItem().getRegistryName().getResourcePath() == "redstone")) {
          ck.redstone = item.getCount();
        }
      }
      double xo=0, yo=0;
      if(block.isWallMount()) {
        switch(facing.getIndex()) {
          case 0: xo = 1-x; yo = 1-z; break; // DOWN
          case 1: xo = 1-x; yo = z  ; break; // UP
          case 2: xo = 1-x; yo = y  ; break; // NORTH
          case 3: xo = x  ; yo = y  ; break; // SOUTH
          case 4: xo = z  ; yo = y  ; break; // WEST
          case 5: xo = 1-z; yo = y  ; break; // EAST
        }
        AxisAlignedBB aa = block.getUnrotatedBB();
        xo = Math.round(((xo-aa.minX) * (1.0/(aa.maxX-aa.minX)) * 15.5) - 0.25);
        yo = Math.round(((yo-aa.minY) * (1.0/(aa.maxY-aa.minY)) * 15.5) - 0.25);
      } else if(block.isFloorMount()) {
        facing = state.getValue(FACING);
        switch(facing.getIndex()) {
          case 0: xo =   x; yo =   z; break; // DOWN
          case 1: xo =   x; yo =   z; break; // UP
          case 2: xo =   x; yo = 1-z; break; // NORTH
          case 3: xo = 1-x; yo =   z; break; // SOUTH
          case 4: xo = 1-z; yo = 1-x; break; // WEST
          case 5: xo =   z; yo =   x; break; // EAST
        }
        AxisAlignedBB aa = block.getUnrotatedBB();
        xo = 0.1 * Math.round(10.0 * (((xo-aa.minX) * (1.0/(aa.maxX-aa.minX)) * 15.5) - 0.25));
        yo = 0.1 * Math.round(10.0 * (((yo-(1.0-aa.maxZ)) * (1.0/(aa.maxZ-aa.minZ)) * 15.5) - 0.25));
      } else {
        return ck;
      }
      ck.x = ((xo > 15.0) ? (15.0) : ((xo < 0.0) ? 0.0 : xo));
      ck.y = ((yo > 15.0) ? (15.0) : ((yo < 0.0) ? 0.0 : yo));
      ck.accepted = true;
      return ck;
    }
  }
}
