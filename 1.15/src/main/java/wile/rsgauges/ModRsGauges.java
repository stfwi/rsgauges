/*
 * @file ModRsGauges.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Main mod class.
 */
package wile.rsgauges;

import net.minecraft.util.Hand;
import wile.rsgauges.blocks.IRsNeighbourInteractionSensitive;
import wile.rsgauges.detail.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;
import net.minecraft.block.BlockState;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import wile.rsgauges.detail.OptionalRecipeCondition.Serializer;

import javax.annotation.Nullable;


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
    ModAuxiliaries.logGitVersion(MODNAME);
    MinecraftForge.EVENT_BUS.register(this);
    MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST,false, PlayerInteractEvent.class, ForgeEvents::onPlayerInteract);
    ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, ModConfig.COMMON_CONFIG_SPEC);
    //ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.SERVER, ModConfig.SERVER_CONFIG_SPEC);
    //ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.CLIENT, ModConfig.CLIENT_CONFIG_SPEC);
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
    { ModContent.registerItems(event); }

    @SubscribeEvent
    public static final void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event)
    { ModContent.registerTileEntities(event); }

    @SubscribeEvent
    public static final void onRegisterEntityTypes(final RegistryEvent.Register<EntityType<?>> event)
    {}

    @SubscribeEvent
    public static final void onRegisterContainerTypes(final RegistryEvent.Register<ContainerType<?>> event)
    {}

    @SubscribeEvent
    public static final void onRegisterSounds(final RegistryEvent.Register<SoundEvent> event)
    {
      ModResources.registerSoundEvents(event);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static final void registerBlockColourHandlers(final ColorHandlerEvent.Block event)
    { ModColors.registerBlockColourHandlers(event); }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static final void registerItemColourHandlers(final ColorHandlerEvent.Item event)
    { ModColors.registerItemColourHandlers(event); }

    @SubscribeEvent
    public static void onCommonSetup(final FMLCommonSetupEvent event)
    {
      ModConfig.apply();
      LOGGER.info("Registering recipe condition processor ...");
      CraftingHelper.register(Serializer.INSTANCE);
      LOGGER.info("Init networking, overlay handling, content processors ...");
      Networking.init();
      ModContent.processRegisteredContent();
      BlockCategories.update();
    }

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event)
    {
      OverlayEventHandler.register();
      ModContent.processContentClientSide(event);
    }

    @SubscribeEvent
    public static void onSendImc(final InterModEnqueueEvent event)
    {}

    @SubscribeEvent
    public static void onRecvImc(final InterModProcessEvent event)
    {}

    @SubscribeEvent
    public static void onServerStarting(FMLServerStartingEvent event)
    {}

    @SubscribeEvent
    public static void onConfigLoad(net.minecraftforge.fml.config.ModConfig.Loading event)
    { ModConfig.onLoad(event.getConfig()); }

    @SubscribeEvent
    public static void onConfigChanged(net.minecraftforge.fml.config.ModConfig.ConfigReloading event)
    { ModConfig.onFileChange(event.getConfig()); }

    // Listener added in c'tor
    public static void onPlayerInteract(PlayerInteractEvent event)
    {
      final World world = event.getWorld();
      if(world.isRemote) return;
      final boolean is_rclick = (event instanceof RightClickBlock) && (event.getHand()==Hand.MAIN_HAND);
      final boolean is_lclick = (event instanceof LeftClickBlock) && (event.getHand()==Hand.MAIN_HAND) && (event.getFace()!=Direction.DOWN); // last one temporary workaround for double trigger on mouse release
      if((!is_rclick) && (!is_lclick)) return;
      final BlockPos fromPos = event.getPos();
      for(Direction facing: Direction.values()) {
        if(event.getFace() == facing) continue;
        final BlockPos pos = fromPos.offset(facing);
        final BlockState state = event.getWorld().getBlockState(pos);
        if(!((state.getBlock()) instanceof IRsNeighbourInteractionSensitive)) continue;
        if(((IRsNeighbourInteractionSensitive)state.getBlock()).onNeighborBlockPlayerInteraction(world, pos, state, fromPos, event.getEntityLiving(), event.getHand(), is_lclick)) {
          event.setCancellationResult(ActionResultType.SUCCESS);
        }
      }
    }

  }

  // -------------------------------------------------------------------------------------------------------------------
  // Sided proxy functionality
  // -------------------------------------------------------------------------------------------------------------------

  public static final ISidedProxy proxy = DistExecutor.runForDist(()->ClientProxy::new, ()->ServerProxy::new);
  public interface ISidedProxy
  {
    default @Nullable PlayerEntity getPlayerClientSide() { return null; }
    default @Nullable World getWorldClientSide() { return null; }
    default @Nullable Minecraft mc() { return null; }
  }
  public static final class ClientProxy implements ISidedProxy
  {
    public @Nullable PlayerEntity getPlayerClientSide() { return Minecraft.getInstance().player; }
    public @Nullable World getWorldClientSide() { return Minecraft.getInstance().world; }
    public @Nullable Minecraft mc() { return Minecraft.getInstance(); }
  }
  public static final class ServerProxy implements ISidedProxy
  {
    public @Nullable PlayerEntity getPlayerClientSide() { return null; }
    public @Nullable World getWorldClientSide() { return null; }
    public @Nullable Minecraft mc() { return null; }
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
