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
import wile.rsgauges.detail.ModConfig;
import wile.rsgauges.detail.JitModelBakery;
import wile.rsgauges.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.SoundType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.DyeUtils;
import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import java.util.List;


public abstract class RsBlock extends Block
{
  public static final PropertyDirection FACING = PropertyDirection.create("facing");
  protected final AxisAlignedBB unrotatedBB;

  public RsBlock(String registryName, @Nullable AxisAlignedBB unrotatedBoundingBox, @Nullable Material material)
  {
    super((material!=null) ? (material) : (Material.CIRCUITS));
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
      return state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()); // other switches, placed so that the UI side is facing the player.
    }
  }

  @Override
  public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
  {
    onRsBlockDestroyed(state, world, pos);
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
    if(isWallMount() && (!isLateral())) {
      // Wall attached blocks where the UI is facing to the player.
      switch(state.getValue(FACING).getIndex()) {
        case 0: return new AxisAlignedBB(1-bb.maxX, 1-bb.maxZ, 1-bb.maxY, 1-bb.minX, 1-bb.minZ, 1-bb.minY); // D
        case 1: return new AxisAlignedBB(1-bb.maxX,   bb.minZ,   bb.minY, 1-bb.minX,   bb.maxZ,   bb.maxY); // U
        case 2: return new AxisAlignedBB(1-bb.maxX,   bb.minY, 1-bb.maxZ, 1-bb.minX,   bb.maxY, 1-bb.minZ); // N
        case 3: return new AxisAlignedBB(  bb.minX,   bb.minY,   bb.minZ,   bb.maxX,   bb.maxY,   bb.maxZ); // S --> bb
        case 4: return new AxisAlignedBB(1-bb.maxZ,   bb.minY,   bb.minX, 1-bb.minZ,   bb.maxY,   bb.maxX); // W
        case 5: return new AxisAlignedBB(  bb.minZ,   bb.minY, 1-bb.maxX,   bb.maxZ,   bb.maxY, 1-bb.minX); // E
      }
    } else if(isLateral()) {
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
  protected void onRsBlockDestroyed(IBlockState state, World world, BlockPos pos)
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
    if(((neighborState.getMaterial() == Material.AIR) || neighborState.getMaterial().isLiquid())) {
      if(!world.isRemote) {
        onRsBlockDestroyed(state, world, pos);
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
    onRsBlockDestroyed(state, world, pos);
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

  /**
   * Data class with static factory function to enable unified block activation
   * click coordinates with respect to the device (UI) interface facing, normalised
   * to 0..15. On `wrenched==true` the face was clicked with an valid wrench. If
   * `touch_configured==false`, the block was not clicked on the main display
   * facing side, and remaining instance data are not valid. If clicked with a
   * redstone stack or dye, the corresponding data values will be non-default.
   */
  protected static final class WrenchActivationCheck
  {
    public boolean touch_configured = false;
    public boolean wrenched = false;
    public Item item = Items.AIR;
    public int item_count = 0;
    public int dye = -1;
    public double x = 0;
    public double y = 0;

    @Override
    public String toString()
    {
      return "{x:" + Double.toString(x) + ",y:" + Double.toString(y) + ",touch_configured:" + Boolean.toString(touch_configured)
          + ",wrenched:" + Boolean.toString(wrenched) + ",item_count:" + Integer.toString(item_count) + ",dye:" + Integer.toString(dye) + "}";
    }

    public static WrenchActivationCheck onBlockActivatedCheck(World world, BlockPos pos, @Nullable IBlockState state, EntityPlayer player, @Nullable EnumHand hand, @Nullable EnumFacing facing, float x, float y, float z)
    {
      final WrenchActivationCheck ck = new WrenchActivationCheck();
      if((world==null) || (pos==null)) return ck;
      if(state==null) state = world.getBlockState(pos);
      if((state==null) || (!(state.getBlock() instanceof RsBlock))) return ck;
      final ItemStack item = player.getHeldItemMainhand();
      if(item == null) return ck;
      if(item.getItem()==Items.AIR) {
        ck.wrenched = true;
      } else if(item.getItem() == Items.REDSTONE) {
        ck.item = Items.REDSTONE;
        ck.item_count = item.getCount();
      } else if(item.getItem() == Items.ENDER_PEARL) {
        ck.item = Items.ENDER_PEARL;
        ck.item_count = item.getCount();
      } else if(item.getItem() == ModItems.SWITCH_LINK_PEARL) {
        ck.item = ModItems.SWITCH_LINK_PEARL;
        ck.item_count = item.getCount();
      } else if(DyeUtils.isDye(item)) {
        ck.item = Items.DYE;
        ck.dye = DyeUtils.rawMetaFromStack(item);
        if(ck.dye > 15) ck.dye = 15;
      } else {
        ck.wrenched = ((","+ModConfig.zmisc.accepted_wrenches+",").contains(","+item.getItem().getRegistryName().getPath() + ","));
      }

      // Touch config check
      if((facing==null) || (!ck.wrenched)) return ck;
      final RsBlock block = (RsBlock)(state.getBlock());
      if(block.isLateral() && (facing != EnumFacing.UP)) return ck;
      if(block.isWallMount() && (facing != state.getValue(FACING))) return ck; // wall & floor covered above.
      double xo=0, yo=0;
      if((block.isWallMount()) && (!block.isLateral())) {
        switch(facing.getIndex()) {
          case 0: xo = 1-x; yo = 1-z; break; // DOWN
          case 1: xo = 1-x; yo = z  ; break; // UP
          case 2: xo = 1-x; yo = y  ; break; // NORTH
          case 3: xo = x  ; yo = y  ; break; // SOUTH
          case 4: xo = z  ; yo = y  ; break; // WEST
          case 5: xo = 1-z; yo = y  ; break; // EAST
        }
        final AxisAlignedBB aa = block.getUnrotatedBB();
        xo = Math.round(((xo-aa.minX) * (1.0/(aa.maxX-aa.minX)) * 15.5) - 0.25);
        yo = Math.round(((yo-aa.minY) * (1.0/(aa.maxY-aa.minY)) * 15.5) - 0.25);
      } else if(block.isLateral()) {
        facing = state.getValue(FACING);
        switch(facing.getIndex()) {
          case 0: xo =   x; yo =   z; break; // DOWN
          case 1: xo =   x; yo =   z; break; // UP
          case 2: xo =   x; yo = 1-z; break; // NORTH
          case 3: xo = 1-x; yo =   z; break; // SOUTH
          case 4: xo = 1-z; yo = 1-x; break; // WEST
          case 5: xo =   z; yo =   x; break; // EAST
        }
        final AxisAlignedBB aa = block.getUnrotatedBB();
        xo = 0.1 * Math.round(10.0 * (((xo-aa.minX) * (1.0/(aa.maxX-aa.minX)) * 15.5) - 0.25));
        yo = 0.1 * Math.round(10.0 * (((yo-(1.0-aa.maxZ)) * (1.0/(aa.maxZ-aa.minZ)) * 15.5) - 0.25));
      } else {
        return ck;
      }
      ck.x = ((xo > 15.0) ? (15.0) : ((xo < 0.0) ? 0.0 : xo));
      ck.y = ((yo > 15.0) ? (15.0) : ((yo < 0.0) ? 0.0 : yo));
      ck.touch_configured = true;
      return ck;
    }
  }
}
