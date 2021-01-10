/*
 * @file ModRsGauges.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Main mod class.
 */
package wile.rsgauges;

import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wile.rsgauges.libmc.detail.*;
import wile.rsgauges.detail.*;


@Mod("rsgauges")
public class ModRsGauges
{
  public static final String MODID = "rsgauges";
  public static final String MODNAME = "Gauges and Switches";
  public static final int VERSION_DATAFIXER = 0;
  private static final Logger LOGGER = LogManager.getLogger();

  // -------------------------------------------------------------------------------------------------------------------

  public ModRsGauges()
  {
    Auxiliaries.init(MODID, LOGGER, ModConfig::getServerConfig);
    Auxiliaries.logGitVersion(MODNAME);
    OptionalRecipeCondition.init(MODID, LOGGER);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(ForgeEvents::onSetup);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(ForgeEvents::onClientSetup);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(ForgeEvents::onConfigLoad);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(ForgeEvents::onConfigReload);
    ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, ModConfig.COMMON_CONFIG_SPEC);
    MinecraftForge.EVENT_BUS.register(this);
    PlayerBlockInteraction.init(MODID, LOGGER);
  }

  // -------------------------------------------------------------------------------------------------------------------

  public static final Logger logger() { return LOGGER; }

  // -------------------------------------------------------------------------------------------------------------------
  // Events
  // -------------------------------------------------------------------------------------------------------------------

  @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
  public static final class ForgeEvents
  {
    @SubscribeEvent
    public static final void onBlocksRegistry(final RegistryEvent.Register<Block> event)
    { ModContent.registerBlocks(event); }

    @SubscribeEvent
    public static final void onItemRegistry(final RegistryEvent.Register<Item> event)
    { ModContent.registerItems(event); ModContent.registerBlockItems(event); }

    @SubscribeEvent
    public static final void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event)
    { ModContent.registerTileEntities(event); }

    //@SubscribeEvent
    public static final void onRegisterEntityTypes(final RegistryEvent.Register<EntityType<?>> event)
    {}

    //@SubscribeEvent
    public static final void onRegisterContainerTypes(final RegistryEvent.Register<ContainerType<?>> event)
    {}

    @SubscribeEvent
    public static final void onRegisterSounds(final RegistryEvent.Register<SoundEvent> event)
    { ModResources.registerSoundEvents(event); }

    public static void onSetup(final FMLCommonSetupEvent event)
    {
      LOGGER.info("Registering recipe condition processor ...");
      CraftingHelper.register(OptionalRecipeCondition.Serializer.INSTANCE);
      Networking.init(MODID);
      ModContent.processRegisteredContent();
      BlockCategories.update();
    }

    public static void onClientSetup(final FMLClientSetupEvent event)
    {
      Overlay.register();
      ModContent.processContentClientSide(event);
    }

    public static void onConfigLoad(net.minecraftforge.fml.config.ModConfig.Loading configEvent)
    {
      try {
        ModConfig.apply();
      } catch(Throwable e) {
        logger().error("Failed to load changed config: " + e.getMessage());
      }
    }

    public static void onConfigReload(net.minecraftforge.fml.config.ModConfig.Reloading configEvent)
    {
      try {
        logger().info("Config file changed {}", configEvent.getConfig().getFileName());
        ModConfig.apply();
      } catch(Throwable e) {
        logger().error("Failed to load changed config: " + e.getMessage());
      }
    }

  }

  // -------------------------------------------------------------------------------------------------------------------
  // Item group / creative tab
  // -------------------------------------------------------------------------------------------------------------------

  public static final ItemGroup ITEMGROUP = (new ItemGroup("tab" + MODID) {
    @OnlyIn(Dist.CLIENT)
    public ItemStack createIcon()
    { return new ItemStack(ModContent.INDUSTRIAL_SMALL_LEVER); }
  });

}
