/*
 * @file ModRsGauges.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Main mod class.
 */
package wile.rsgauges;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
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
    ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, ModConfig.COMMON_CONFIG_SPEC);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(ForgeEvents::onSetup);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(ForgeEvents::onClientSetup);
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
    public static final void onTileEntityRegistry(final RegistryEvent.Register<BlockEntityType<?>> event)
    { ModContent.registerTileEntities(event); }

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

    @SubscribeEvent
    public static void onConfigLoad(final ModConfigEvent.Loading event)
    { ModConfig.apply(); }

    @SubscribeEvent
    public static void onConfigReload(final ModConfigEvent.Reloading event)
    {
      try {
        Auxiliaries.logger().info("Config file changed {}", event.getConfig().getFileName());
        ModConfig.apply();
      } catch(Throwable e) {
        Auxiliaries.logger().error("Failed to load changed config: " + e.getMessage());
      }
    }

  }

  // -------------------------------------------------------------------------------------------------------------------
  // Item group / creative tab
  // -------------------------------------------------------------------------------------------------------------------

  public static final CreativeModeTab ITEMGROUP = (new CreativeModeTab("tab" + MODID) {
    @OnlyIn(Dist.CLIENT)
    public ItemStack makeIcon()
    { return new ItemStack(ModContent.INDUSTRIAL_SMALL_LEVER); }
  });

}
