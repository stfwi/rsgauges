/**
 * @file AutoSwitchBlock.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Basic class for blocks representing redstone signal sources, like
 * the vanilla lever or button.
**/
package wile.rsgauges.blocks;

import wile.rsgauges.ModRsGauges;
import wile.rsgauges.ModBlocks;
import wile.rsgauges.client.JitModelBakery;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import java.util.List;
import java.util.Random;

public class SensitiveGlassBlock extends Block implements ModBlocks.Colors.Colored {
  public static final PropertyBool POWERED = PropertyBool.create("powered");

  public static final int CONFIG_LIGHT_MASK_POWERED              = 0x0000000f;
  public static final int CONFIG_LIGHT_MASK_UNPOWERED            = 0x000000f0;
  public static final int CONFIG_SIDES_ALWAYS_RENDERED_POWERED   = 0x00000100;
  public static final int CONFIG_SIDES_ALWAYS_RENDERED_UNPOWERED = 0x00000200;
  protected final int config;
  protected final int colorMultiplierValue;

  public SensitiveGlassBlock(String registryName, int config, int colorMultiplierValue) {
    super(Material.REDSTONE_LIGHT);
    setCreativeTab(ModRsGauges.CREATIVE_TAB_RSGAUGES);
    setRegistryName(registryName);
    setUnlocalizedName(ModRsGauges.MODID + "." + registryName);
    setLightOpacity(0);
    setLightLevel(0);
    setHardness(0.3f);
    setResistance(2.0f);
    setTickRandomly(false);
    this.config = config;
    this.colorMultiplierValue = colorMultiplierValue;
  }

  public SensitiveGlassBlock(String registryName, int config) { this(registryName, config, (int)0xffffffff); }

  @SideOnly(Side.CLIENT)
  public void initModel() { ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory")); }

  @Override
  public boolean isOpaqueCube(IBlockState state) { return false; }

  @Override
  public boolean isFullCube(IBlockState state) { return true; }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, net.minecraft.client.particle.ParticleManager manager) { return true; } // no hit particles

  @Override
  @SideOnly(Side.CLIENT)
  public boolean addDestroyEffects(World world, BlockPos pos, net.minecraft.client.particle.ParticleManager manager) { return true; } // no destroy

  @SideOnly(Side.CLIENT)
  @Override
  public BlockRenderLayer getBlockLayer() { return BlockRenderLayer.TRANSLUCENT; }

  @SideOnly(Side.CLIENT)
  @Override
  public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    if((config & (CONFIG_SIDES_ALWAYS_RENDERED_POWERED|CONFIG_SIDES_ALWAYS_RENDERED_UNPOWERED))==(CONFIG_SIDES_ALWAYS_RENDERED_POWERED|CONFIG_SIDES_ALWAYS_RENDERED_UNPOWERED)) {
      return true; // fast return branch
    } else {
      final IBlockState neighbourState = world.getBlockState(pos.offset(side));
      if((neighbourState==null) || (!(neighbourState.getBlock() instanceof SensitiveGlassBlock))) return true;
      final boolean powered = state.getValue(POWERED);
      if(((config & CONFIG_SIDES_ALWAYS_RENDERED_POWERED)!=0) && powered) return true;
      if(((config & CONFIG_SIDES_ALWAYS_RENDERED_UNPOWERED)!=0) && (!powered)) return true;
      return (neighbourState.getValue(POWERED) != powered);
    }
  }

  @Override
  public int getLightValue(IBlockState state) { return state.getValue(POWERED) ? ((config & CONFIG_LIGHT_MASK_POWERED)>>0) : ((config & CONFIG_LIGHT_MASK_UNPOWERED)>>8); }

  @Override
  public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) { return state; }

  @Override
  public IBlockState getStateFromMeta(int meta) { return getDefaultState().withProperty(POWERED, (meta & 0x1)!=0); }

  @Override
  public int getMetaFromState(IBlockState state) { return (state.getValue(POWERED) ? 0x1 : 0x0); }

  @Override
  protected BlockStateContainer createBlockState() { return new BlockStateContainer(this, POWERED); }

  @Override
  public int tickRate(World world) { return 5; }

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
    if(world.isRemote) return;
    if(state.getValue(POWERED) && (!(world.isBlockPowered(pos)))) world.setBlockState(pos, state.withProperty(POWERED, false), 2);
  }

  @Override
  public boolean canSpawnInBlock() { return false; }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    if(world.isRemote) return;
    if(world.isBlockPowered(pos)) world.setBlockState(pos, state.withProperty(POWERED, true), 2);
  }

  @Override
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos neighbourPos) {
    if(world.isRemote) return;
    if(state.getValue(POWERED)) {
      if(!world.isBlockPowered(pos)) world.scheduleUpdate(pos, this, 2);
    } else {
      if(world.isBlockPowered(pos)) world.setBlockState(pos, state.withProperty(POWERED, true), 2);
    }
  }

  @Override
  public int getColorMultiplierRGBA(@Nullable IBlockState state) { return this.colorMultiplierValue; }
}
