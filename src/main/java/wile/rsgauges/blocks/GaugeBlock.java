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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityComparator;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
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
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.lwjgl.opengl.GL11;


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
    this.blinkInterval = (blinkInterval <= 0) ? (0) : ((blinkInterval < 500) ? (500) : ((blinkInterval > 3000) ? 3000 : blinkInterval));
    setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWER, 0));
  }

  public GaugeBlock(String registryName, AxisAlignedBB boundingBox) { this(registryName, boundingBox, 0, 0); }

  @Override
  @SideOnly(Side.CLIENT)
  public void initModel() { super.initModel(); }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, net.minecraft.client.particle.ParticleManager manager) { return true; }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean addDestroyEffects(World world, BlockPos pos, net.minecraft.client.particle.ParticleManager manager) { return true; }

  public void update(IBlockState state, World world, BlockPos pos) {
    boolean sync = false;
    GaugeBlock.GaugeTileEntity te = getTe(state, world, pos, true);
    if(te == null) return;
    if(state == null) state = world.getBlockState(pos);
    if(world.isRemote) {
      if(sync || (state.getValue(POWER) != te.power())) {
        world.setBlockState(pos, state.withProperty(POWER, te.power()), 1|2|16);
        world.markBlockRangeForRenderUpdate(pos, pos);
      }
    } else {
      BlockPos neighbourPos = pos.offset((EnumFacing)state.getValue(GaugeBlock.FACING), -1);
      if(!world.isBlockLoaded(neighbourPos)) return; // Gauge is placed on a chunk boundary, don't forge loading of neighbour chunk.
      IBlockState neighborState = world.getBlockState(neighbourPos);
      int p = 0;
      if(neighborState != null) {
        if(neighborState.canProvidePower()) {
          p = Math.max(
              neighborState.getWeakPower(world, neighbourPos, state.getValue(FACING).getOpposite()),
              neighborState.getStrongPower(world, neighbourPos, state.getValue(FACING).getOpposite())
          );
        } else if(!measureTargetWeakPower) {
          p = world.getStrongPower(neighbourPos);
        } else {
          for(EnumFacing nbf:EnumFacing.values()) {
            if(p >= 15) break;
            // Check if the block next to the neighbour is actually a redstone source block or just a strong powered normal block.
            BlockPos nbp = neighbourPos.offset(nbf);
            IBlockState nbs = world.getBlockState(nbp);
            if((nbs == null) || (!nbs.canProvidePower()) || (nbs.getBlock() instanceof GaugeBlock)) continue;
            int pa = Math.max(nbs.getStrongPower(world, nbp, nbf), nbs.getWeakPower(world, nbp, nbf));
            if(p < pa) p = pa;
          }
        }
      }
      if((this.blinkInterval > 0) && te.force_off()) p = 0;
      if(te.power() != p) sync = true;
      te.power(p);
      IBlockState newState = state.withProperty(POWER, p);
      if(sync) {
        te.markDirty();
        world.setBlockState(pos, newState, 1|2|16);
        world.markAndNotifyBlock(pos, null, state, newState, 1|2|16);
      } else {
        world.setBlockState(pos, newState, 1|16);
      }
    }
  }

  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
    GaugeBlock.GaugeTileEntity te = getTe(state, world, pos);
    return super.getActualState(state, world, pos).withProperty(POWER, (te==null) ? 0 : te.power());
  }

  @Override
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos) {
    if(!this.neighborChangedCheck(state, world, pos, neighborBlock, neighborPos)) return;
    GaugeBlock.GaugeTileEntity te = getTe(state, world, pos);
    if(te != null) te.reset_timer();
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    if(this.onBlockPlacedByCheck(world, pos, state, placer, stack, true, false)) world.scheduleBlockUpdate(pos, state.getBlock(), 0, 1);
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) { update(state, world, pos); return true; }

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
    TileEntity te = world.getTileEntity(pos);
    return (!(te instanceof GaugeBlock.GaugeTileEntity)) ? (null) : ((GaugeBlock.GaugeTileEntity)te);
  }

  public GaugeBlock.GaugeTileEntity getTe(IBlockState state, IBlockAccess world, BlockPos pos, boolean nocache) {
    TileEntity te;
    if(nocache) {
      te = ((World)world).getTileEntity(pos);
    } else if((world instanceof ChunkCache)) {
      return null;
    } else {
      te = (world instanceof ChunkCache) ? ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : ((World)world).getTileEntity(pos);
    }
    return (!(te instanceof GaugeBlock.GaugeTileEntity)) ? (null) : ((GaugeBlock.GaugeTileEntity)te);
  }

  /**
   * Tile entity to update the gauge block frequently.
   */
  public static final class GaugeTileEntity extends RsTileEntity<GaugeBlock> implements ITickable {
    private long trigger_timer_ = 0;
    private int power_ = 0;
    private boolean force_off_ = false;
    private IBlockState lastState = null;

    public int power() { return this.power_; }
    public void power(int p) { this.power_ = (p<=0) ? (0) : ((p>15) ? 15 : p); }
    public boolean force_off() { return this.force_off_; }
    public void reset_timer() { trigger_timer_= 0; }

    @Override
    public void writeNbt(NBTTagCompound nbt, boolean updatePacket) { nbt.setInteger("power", power()); }

    @Override
    public void readNbt(NBTTagCompound nbt, boolean updatePacket)  { power(nbt.getInteger("power")); }

    @Override
    public void update() {
      if(--trigger_timer_ > 0) return;
      trigger_timer_ = Config.getGaugeUpdateInterval();
      if(world.isRemote) {
        IBlockState state = world.getBlockState(pos).withProperty(POWER, power());
        if(lastState != state) {
          lastState = state;
          world.setBlockState(pos, state, 1|16);
          world.markBlockRangeForRenderUpdate(pos, pos);
        }
      } else {
        try {
          IBlockState state = world.getBlockState(pos);
          GaugeBlock block = (GaugeBlock) state.getBlock();
          if(block.blinkInterval > 0) {
            // server based indicator state change. Can be removed if client based gauge rendering update approved.
            trigger_timer_ = 5;
            force_off_ = (System.currentTimeMillis() % block.blinkInterval) > (block.blinkInterval/2);
          } else {
            force_off_ = false;
          }
          block.update(state, world, pos);
        } catch(Exception e) {
          trigger_timer_ = 50;
        }
      }
    }
  }
}
