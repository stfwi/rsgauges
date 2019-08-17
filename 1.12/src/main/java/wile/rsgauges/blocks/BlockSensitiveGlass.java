/*
 * @file BlockSensitiveGlass.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Class representing full, transparent blocks with different
 * look depending on the redstone power they receive.
 */
package wile.rsgauges.blocks;

import wile.rsgauges.ModRsGauges;
import wile.rsgauges.detail.ModAuxiliaries;
import wile.rsgauges.detail.ModConfig;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import javax.annotation.Nullable;
import java.util.Random;
import java.util.List;

public class BlockSensitiveGlass extends Block implements ModBlocks.Colors.ColorTintSupport
{
  public static final PropertyBool POWERED = PropertyBool.create("powered");

  public static final int CONFIG_LIGHT_MASK_POWERED              = 0x0000000f;
  public static final int CONFIG_LIGHT_MASK_UNPOWERED            = 0x000000f0;
  public static final int CONFIG_SIDES_ALWAYS_RENDERED_POWERED   = 0x00000100;
  public static final int CONFIG_SIDES_ALWAYS_RENDERED_UNPOWERED = 0x00000200;
  protected final int config;
  protected final int color_multiplier_value;

  public BlockSensitiveGlass(String registryName, int config, int colorMultiplierValue)
  {
    super(Material.REDSTONE_LIGHT);
    setCreativeTab(ModRsGauges.CREATIVE_TAB_RSGAUGES);
    setRegistryName(ModRsGauges.MODID, registryName);
    setTranslationKey(ModRsGauges.MODID + "." + registryName);
    setLightOpacity(0);
    setLightLevel(0);
    setHardness(0.3f);
    setResistance(2.0f);
    setTickRandomly(false);
    this.config = config;
    color_multiplier_value = colorMultiplierValue;
  }

  public BlockSensitiveGlass(String registryName, int config)
  { this(registryName, config, (int)0xffffffff); }

  @SideOnly(Side.CLIENT)
  public void initModel()
  { ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory")); }

  @Override
  @SideOnly(Side.CLIENT)
  @SuppressWarnings("deprecation")
  public float getAmbientOcclusionLightValue(IBlockState state)
  { return 1.0F; }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isOpaqueCube(IBlockState state)
  { return false; }

  @Override
  @SuppressWarnings("deprecation")
  public boolean isFullCube(IBlockState state)
  { return true; }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, net.minecraft.client.particle.ParticleManager manager)
  { return true; } // no hit particles

  @Override
  @SideOnly(Side.CLIENT)
  public boolean addDestroyEffects(World world, BlockPos pos, net.minecraft.client.particle.ParticleManager manager)
  { return true; }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag)
  { ModAuxiliaries.Tooltip.addInformation(stack, world, tooltip, flag, true); }

  @SideOnly(Side.CLIENT)
  @Override
  public BlockRenderLayer getRenderLayer()
  { return BlockRenderLayer.TRANSLUCENT; }

  @SideOnly(Side.CLIENT)
  @Override
  @SuppressWarnings("deprecation")
  public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
  {
    if((config & (CONFIG_SIDES_ALWAYS_RENDERED_POWERED|CONFIG_SIDES_ALWAYS_RENDERED_UNPOWERED))==(CONFIG_SIDES_ALWAYS_RENDERED_POWERED|CONFIG_SIDES_ALWAYS_RENDERED_UNPOWERED)) {
      return true; // fast return branch
    } else {
      final IBlockState neighbourState = world.getBlockState(pos.offset(side));
      if((neighbourState==null) || (!(neighbourState.getBlock() instanceof BlockSensitiveGlass))) return true;
      final boolean powered = state.getValue(POWERED);
      if(((config & CONFIG_SIDES_ALWAYS_RENDERED_POWERED)!=0) && powered) return true;
      if(((config & CONFIG_SIDES_ALWAYS_RENDERED_UNPOWERED)!=0) && (!powered)) return true;
      return (neighbourState.getValue(POWERED) != powered);
    }
  }

  @Override
  @SuppressWarnings("deprecation")
  public int getLightValue(IBlockState state)
  {
    if((!ModConfig.zmisc.sensitive_glass_constant_server_light_level) || (ModAuxiliaries.isClientSide())) {
      return state.getValue(POWERED) ? ((config & CONFIG_LIGHT_MASK_POWERED)>>0) : ((config & CONFIG_LIGHT_MASK_UNPOWERED)>>4);
    } else {
      return (config & CONFIG_LIGHT_MASK_POWERED); // server constant light level
    }
  }

  @Override
  @SuppressWarnings("deprecation")
  public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
  { return state; }

  @Override
  @SuppressWarnings("deprecation")
  public IBlockState getStateFromMeta(int meta)
  { return getDefaultState().withProperty(POWERED, (meta & 0x1)!=0); }

  @Override
  public int getMetaFromState(IBlockState state)
  { return (state.getValue(POWERED) ? 0x1 : 0x0); }

  @Override
  protected BlockStateContainer createBlockState()
  { return new BlockStateContainer(this, POWERED); }

  @Override
  public int tickRate(World world)
  { return 4; }

  @Override
  public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
  {
    if(world.isRemote) return;
    if(state.getValue(POWERED) && (!(world.isBlockPowered(pos)))) world.setBlockState(pos, state.withProperty(POWERED, false), 2);
  }

  @Override
  public boolean canSpawnInBlock()
  { return false; }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
  {
    if(world.isRemote) return;
    if(world.isBlockPowered(pos)) world.setBlockState(pos, state.withProperty(POWERED, true), 2);
  }

  @Override
  @SuppressWarnings("deprecation")
  public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos neighbourPos)
  {
    if(world.isRemote) return;
    if(state.getValue(POWERED)) {
      // Delay to prevent recalculations on short peaks, especially for light emitting variants.
      if(!world.isBlockPowered(pos)) world.scheduleUpdate(pos, this, (((config & CONFIG_LIGHT_MASK_POWERED)>>0)==((config & CONFIG_LIGHT_MASK_UNPOWERED)>>4)) ? 1 : 4);
    } else {
      if(world.isBlockPowered(pos)) world.setBlockState(pos, state.withProperty(POWERED, true), 2);
    }
  }

  @Override
  public boolean hasColorMultiplierRGBA()
  { return true; }

  @Override
  public int getColorMultiplierRGBA(@Nullable IBlockState state, @Nullable IBlockAccess world, @Nullable BlockPos pos)
  { return color_multiplier_value; }

}
