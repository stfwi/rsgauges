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

import wile.rsgauges.ModRsGauges;
import wile.rsgauges.client.JitModelBakery;

import javax.annotation.Nullable;

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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.world.chunk.*;
import net.minecraft.world.ChunkCache;

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
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) { this.onBlockPlacedByCheck(world, pos, state, placer, stack, true, true); }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) { return true; }

  @Override
  public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {}

  @Override
  public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
    BlockPos blockpos = pos.offset(side.getOpposite());
    IBlockState state = world.getBlockState(blockpos);
    if(side == EnumFacing.UP) return (state.getBlockFaceShape(world, pos, EnumFacing.UP) == BlockFaceShape.SOLID);
    return !isExceptBlockForAttachWithPiston(state.getBlock()) && (state.getBlockFaceShape(world, blockpos, side) == BlockFaceShape.SOLID);
  }

  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    AxisAlignedBB bb = getUnrotatedBB();
    switch(state.getValue(FACING).getIndex()) {
      case 0: return new AxisAlignedBB(1 - bb.maxX, 1 - bb.maxZ, 1 - bb.maxY, 1 - bb.minX, 1 - bb.minZ, 1 - bb.minY); // D
      case 1: return new AxisAlignedBB(1 - bb.maxX, bb.minZ, bb.minY, 1 - bb.minX, bb.maxZ, bb.maxY); // U
      case 2: return new AxisAlignedBB(1 - bb.maxX, bb.minY, 1 - bb.maxZ, 1 - bb.minX, bb.maxY, 1 - bb.minZ); // N
      case 3: return new AxisAlignedBB(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ); // S --> bb
      case 4: return new AxisAlignedBB(1 - bb.maxZ, bb.minY, bb.minX, 1 - bb.minZ, bb.maxY, bb.maxX); // W
      case 5: return new AxisAlignedBB(bb.minZ, bb.minY, 1 - bb.maxX, bb.maxZ, bb.maxY, 1 - bb.minX); // E
    }
    return bb;
  }

  public AxisAlignedBB getUnrotatedBB() { return unrotatedBB; }

  public JitModelBakery.JitBakedModel getJitBakedModel() { return null; }

  protected boolean neighborChangedCheck(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos) {
    if(!pos.offset(state.getValue(FACING).getOpposite()).equals(neighborPos)) return false;
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

  protected boolean onBlockPlacedByCheck(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack, boolean allowUpDown, boolean applyFlags) {
    EnumFacing facing = EnumFacing.NORTH;
    Vec2f py = placer.getPitchYaw();
    if(allowUpDown && (py.x >= 80)) {
      facing = EnumFacing.UP;
    } else if(allowUpDown && (py.x <= -80)) {
      facing = EnumFacing.DOWN;
    } else {
      switch((int)Math.round((((placer.getPitchYaw().y + 360) / 90.0)) % 4) * 90) {
        case 270: facing = EnumFacing.WEST; break;
        case 180: facing = EnumFacing.SOUTH; break;
        case 90:  facing = EnumFacing.EAST; break;
        case 0: facing = EnumFacing.NORTH;
      }
    }
    if(!canPlaceBlockOnSide(world, pos, facing)) {
      // try to find a corrected facing
      boolean found = false;
      for(EnumFacing side : EnumFacing.values()) {
        if((!allowUpDown) && ((side == EnumFacing.UP) | (side == EnumFacing.DOWN)))
          continue;
        if(canPlaceBlockOnSide(world, pos, side)) {
          facing = side;
          found = true;
        }
      }
      if(!found) {
        this.dropBlockAsItem(world, pos, state, 0);
        world.setBlockToAir(pos);
        return false;
      }
    }
    world.setBlockState(pos, state.withProperty(FACING, facing), applyFlags ? (1|2|16) : 0);
    return true;
  }

}
