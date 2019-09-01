/*
 * @file ModRsGauges.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Main mod class.
 */
package wile.rsgauges;

import net.minecraft.util.EnumActionResult;
import wile.rsgauges.items.*;
import wile.rsgauges.detail.*;
import wile.rsgauges.blocks.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import org.apache.logging.log4j.Logger;
import javax.annotation.Nonnull;

@Mod(
  modid = ModRsGauges.MODID,
  name = ModRsGauges.MODNAME,
  version = ModRsGauges.MODVERSION,
  dependencies = "required-after:forge@[14.23.5.2768,)",
  useMetadata = true,
  updateJSON = "https://raw.githubusercontent.com/stfwi/rsgauges/develop/meta/update.json",
  certificateFingerprint = ((ModRsGauges.MODFINGERPRINT==("@"+"MOD_SIGNSHA1"+"@")) ? "" : ModRsGauges.MODFINGERPRINT)
)
@SuppressWarnings({"unused", "ConstantConditions"})
public class ModRsGauges
{
  public static final String MODID = "rsgauges";
  public static final String MODNAME = "Redstone Gauges and Switches";
  public static final String MODVERSION = "@MOD_VERSION@";
  public static final String MODMCVERSION = "@MOD_MCVERSION@";
  public static final String MODFINGERPRINT = "@MOD_SIGNSHA1@";
  public static final String MODBUILDID = "@MOD_BUILDID@";
  public static Logger logger;

  @Mod.Instance
  public static ModRsGauges instance;

  @SidedProxy(clientSide = "wile.rsgauges.detail.ClientProxy", serverSide = "wile.rsgauges.detail.ServerProxy")
  public static IProxy proxy;

  public interface IProxy
  {
    public void preInit(FMLPreInitializationEvent e);
    public void init(FMLInitializationEvent e);
    public void postInit(FMLPostInitializationEvent e);
  }

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event)
  {
    logger = event.getModLog();
    logger.info(MODNAME + ": Version " + MODMCVERSION + "-" + MODVERSION + ( (MODBUILDID.equals("@"+"MOD_BUILDID"+"@")) ? "" : (" "+MODBUILDID) ) + ".");
    if(MODFINGERPRINT.equals("@"+"MOD_SIGNSHA1"+"@")) {
      logger.warn(MODNAME + ": Mod is NOT signed.");
    } else {
      logger.info(MODNAME + ": Found fingerprint " + MODFINGERPRINT + ".");
    }
    proxy.preInit(event);
  }

  @Mod.EventHandler
  public void init(FMLInitializationEvent event)
  {
    DataFixing.registerDataFixers();
    proxy.init(event);
  }

  @Mod.EventHandler
  public void postInit(FMLPostInitializationEvent event)
  {
    ModConfig.onPostInit(event);
    proxy.postInit(event);
    ModAuxiliaries.BlockCategories.compose();
  }

  @Mod.EventBusSubscriber
  public static final class RegistrationSubscriptions
  {
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
      ModBlocks.registerBlocks(event);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
      ModBlocks.registerItemBlocks(event);
      ModItems.registerItems(event);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event)
    {
      ModBlocks.initModels();
      ModItems.initModels();
    }

    @SubscribeEvent
    public void playerInteract(PlayerInteractEvent event)
    {
      final boolean is_rclick = (event instanceof LeftClickBlock);
      final boolean is_lclick = (event instanceof RightClickBlock);
      if((!is_rclick) && (!is_lclick)) return;
      final World world = event.getWorld();
      final BlockPos fromPos = event.getPos();
      for(EnumFacing facing: EnumFacing.values()) {
        final BlockPos pos = fromPos.offset(facing);
        final IBlockState state = event.getWorld().getBlockState(pos);
        if(!(state.getBlock() instanceof IRsNeighbourInteractionSensitive)) continue;
        if(((IRsNeighbourInteractionSensitive)state.getBlock()).onNeighborBlockPlayerInteraction(world, pos, state, fromPos, event.getEntityLiving(), event.getHand(), is_lclick)) {
          event.setCancellationResult(EnumActionResult.SUCCESS);
        }
      }
    }
  }

  public static final CreativeTabs CREATIVE_TAB_RSGAUGES = (new CreativeTabs("tabrsgauges") {
    @Override
    @SideOnly(Side.CLIENT)
    public @Nonnull ItemStack createIcon()
    { return new ItemStack((ModBlocks.flatgauge1Block != null) ? (ModBlocks.flatgauge1Block) : (Blocks.LEVER)); }
  });

}
