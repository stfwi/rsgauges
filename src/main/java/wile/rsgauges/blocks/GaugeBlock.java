/**
 * @file GaugeBlock.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Basic class for blocks representing a redstone signal
 * display, measuring the redstone signal of the block
 * it is attached to (only the back not the sides or front).
 * The block has a "tickable" tile entity to ensure that
 * the gauge display is updated even if a block update event
 * was lost. Depending on the model/type additional constants
 * like power-to-light scaling are implemented, e.g. for LED
 * indicators.
 *
**/
package wile.rsgauges.blocks;

import wile.rsgauges.Config;
import wile.rsgauges.ModBlocks;
import wile.rsgauges.blocks.RsBlock;
import wile.rsgauges.client.JitModelBakery;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityComparator;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.BlockRedstoneComparator;


public class GaugeBlock extends RsBlock {

  public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);

  // If the block emits light proportional to the power (e.g. lamp) this scaler is
  // nonzero. The light is only rendering light, not anti-spawn light.
  private final int lightValueScaling;
  // Specifies if the gauge shall look at the maximum redstone power, including
  // weak power, of the target block it is attached to.
  private final boolean measureTargetWeakPower = true;
  private final int blinkInterval;

  public GaugeBlock(String registryName, AxisAlignedBB unrotatedBB, int powerToLightValueScaling0To15, int blinkInterval) {
    super(registryName, unrotatedBB);
    this.lightValueScaling = (powerToLightValueScaling0To15 < 0) ? (0) : ((powerToLightValueScaling0To15 > 15) ? 15 : powerToLightValueScaling0To15);
    this.blinkInterval = (blinkInterval < 500) ? (500) : ((blinkInterval > 2500) ? 2500 : blinkInterval);
    setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWER, 0));
  }

  public GaugeBlock(String registryName, AxisAlignedBB boundingBox) {
    this(registryName, boundingBox, 0, 0);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void initModel() { super.initModel(); }

  public int getIndirectlyMeasuredPower(World world, BlockPos pos) {
    int p = 0;
    if(!measureTargetWeakPower) {
      p = world.getStrongPower(pos);
    } else {
      for(EnumFacing nbf:EnumFacing.values()) {
        if(p >= 15) break;
        // Check if the block next to the neighbour is actually a redstone source block or just a strong powered normal block.
        BlockPos nbp = pos.offset(nbf);
        IBlockState nbs = world.getBlockState(nbp);
        if((nbs == null) || (!nbs.canProvidePower()) || (nbs.getBlock() instanceof GaugeBlock)) continue;
        int pa = Math.max(nbs.getStrongPower(world, nbp, nbf), nbs.getWeakPower(world, nbp, nbf));
        if(p < pa) p = pa;
      }
    }
    return (p >= 15) ? (15) : (p);
  }

  public void updateBlock(IBlockState state, World world, BlockPos pos, BlockPos neighborPos) {
    updateBlock(state, world, pos, neighborPos, true);
  }

  public void updateBlock(IBlockState state, World world, BlockPos pos, BlockPos neighborPos, boolean forcesync) {
    if(!(world instanceof World)) return;
    GaugeBlock.GaugeTileEntity te = this.getTe(state, world, pos, true);
    if(te == null) return;
    if(!world.isRemote) {
      IBlockState neighborState = world.getBlockState(neighborPos);
      if(neighborState == null) return;
      int p = 0;
      if(neighborState.canProvidePower()) {
        p = Math.max(
            neighborState.getWeakPower(world, neighborPos, state.getValue(FACING).getOpposite()),
            neighborState.getStrongPower(world, neighborPos, state.getValue(FACING).getOpposite())
        );
      } else {
        p = getIndirectlyMeasuredPower(world, neighborPos);
      }
      if(te.power() != p) forcesync = true;
      te.power(p);
      {
        // Test for server based blinking state change
        if(this.blinkInterval > 500) {
          int T = this.blinkInterval * 50/1000;
          T = ((T+6)/10)*10;
          if((world.getTotalWorldTime() % T) > (T/2))
          te.power(0);
          forcesync = true;
        }
      }
      if(forcesync) {
        te.markDirty();
        world.markAndNotifyBlock(pos, null, state, state.withProperty(POWER, te.power()), 2);
      }
    }
    world.setBlockState(pos, state.withProperty(POWER, te.power()), 2);
    if(world.isRemote) {
      world.markBlockRangeForRenderUpdate(pos, pos);
    }
  }

  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
    state = super.getActualState(state, world, pos);
    if(!(state.getBlock() instanceof GaugeBlock)) return state; // no idea if that can happen
    GaugeBlock.GaugeTileEntity te = ((GaugeBlock)state.getBlock()).getTe(state, world, pos, true);
    if(te == null) return state;
    return state.withProperty(POWER, te.power());
  }

  @Override
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos) {
    if(this.neighborChangedCheck(state, world, pos, neighborBlock, neighborPos)) updateBlock(state, world, pos, neighborPos);
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    if(this.onBlockPlacedByCheck(world, pos, state, placer, stack, true, false)) world.scheduleBlockUpdate(pos, state.getBlock(), 1, 1);
  }

  @Override
  public int getLightValue(IBlockState state) { int v = (int)((this.lightValueScaling * state.getValue(POWER)) / 15); return (v < 0) ? (0) : ((v > 15) ? 15 : v); }

  @Override
  public boolean getWeakChanges(IBlockAccess world, BlockPos pos) { return true; }

  @Override
  protected BlockStateContainer createBlockState() { return new BlockStateContainer(this, FACING,POWER); }

  @Override
  public boolean hasTileEntity(IBlockState state) { return true; }

  @Override
  public TileEntity createTileEntity(World world, IBlockState state) { return new GaugeBlock.GaugeTileEntity(); }

  public GaugeBlock.GaugeTileEntity getTe(IBlockState state, IBlockAccess world, BlockPos pos) {
    return this.getTe(state, world, pos, true);
  }

  public GaugeBlock.GaugeTileEntity getTe(IBlockState state, IBlockAccess world, BlockPos pos, boolean nocache) {
    if(nocache && (world instanceof ChunkCache)) return null;
    TileEntity te = (world instanceof ChunkCache) ? ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : ((World)world).getTileEntity(pos);
    return (!(te instanceof GaugeBlock.GaugeTileEntity)) ? (null) : ((GaugeBlock.GaugeTileEntity)te);
  }

  /**
   * Tile entity to update the gauge block frequently.
   */
  public static final class GaugeTileEntity extends RsTileEntity<GaugeBlock> implements ITickable {

    private long trigger_time = 0;
    private int power_ = 0;
    private int ticktrack = 0;

    public int power() { return this.power_; }
    public void power(int p) { this.power_ = p; }

    @Override
    public void update() { update(false); }

    @Override
    public void writeNbt(NBTTagCompound nbt, boolean updatePacket) {
      nbt.setInteger("power", (this.power() > 15) ? (15) : (this.power() < 0 ? 0 : this.power()));
    }

    @Override
    public void readNbt(NBTTagCompound nbt, boolean updatePacket)  {
      int p = nbt.getInteger("power");
      this.power( (p<=0) ? (0) : ((p>15) ? 15 : p));
      if(updatePacket && getWorld().isRemote) this.update(true);
    }

    private void update(boolean force) {
      if(!hasWorld()) return;
      World world = getWorld();
      long t = world.getTotalWorldTime();
      if((!force) && (t < this.trigger_time)) return;
      this.trigger_time = t + 50; // in case of early return
      if(!world.isBlockLoaded(getPos())) return;
      IBlockState state = world.getBlockState(getPos());
      if(state == null) return;
      Block ckblock = state.getBlock();
      if(!(ckblock instanceof GaugeBlock)) return;
      GaugeBlock block = (GaugeBlock) ckblock;
      BlockPos neighbourPos = getPos().offset((EnumFacing)state.getValue(GaugeBlock.FACING), -1);
      if(!world.isBlockLoaded(neighbourPos)) return; // Gauge is placed on a chunk boundary, don't forge loading of neighbour chunk.

      if(block.blinkInterval > 0) {
        // server based indicator state change. Can be removed if client based gauge rendering update approved.
        this.trigger_time = t + 10;
      } else {
        this.trigger_time = t +  (world.isRemote ? 10 : Config.getGaugeUpdateInterval());
      }

      block.updateBlock(state, world, getPos(), neighbourPos, ((++ticktrack < 5) || force));
    }
  }

  /**
   * Dynamic gauge models
   */
  @Override
  public JitModelBakery.JitBakedModel getJitBakedModel() {
      return null;
      //
      // Preparation for client based indicator blinking.
      //
      //  /**
      //   * Blinking block JIT model
      //   */
      //  if(this.blinkInterval > 500) {
      //    return new JitModelBakery.JitBakedModel() {
      //      @Override
      //      public List<BakedQuad> getQuads(@Nullable IBlockState blockstate, @Nullable EnumFacing side, long rand) {
      //        if((blockstate!=null)) {
      //          int i = ((GaugeBlock)blockstate.getBlock()).blinkInterval;
      //          if((i > 500) && (System.currentTimeMillis() % i) > (i/2)) {
      //            blockstate = blockstate.withProperty(POWER, 0);
      //          };
      //        }
      //        return baked.getOrDefault(this.getStateVariantHash(blockstate), firstbaked).getQuads(blockstate, side, rand);
      //      }
      //    };
      //  } else {
      //    // no JIT model.
      //    return null;
      //  }
  }

}
