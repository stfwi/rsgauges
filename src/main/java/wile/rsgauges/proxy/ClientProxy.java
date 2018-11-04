/**
 * @file ClientProxy.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Client side only initialisation.
**/
package wile.rsgauges.proxy;

import wile.rsgauges.ModBlocks;
import wile.rsgauges.ModRsGauges;
import wile.rsgauges.network.*;
import wile.rsgauges.client.OverlayEventHandler;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
  @Override
  public void preInit(FMLPreInitializationEvent e) {
    super.preInit(e);
    OBJLoader.INSTANCE.addDomain(ModRsGauges.MODID);
    Networking.preInitClient();
  }

  @Override
  public void init(FMLInitializationEvent e) {
    super.init(e);
  }

  @Override
  public void postInit(FMLPostInitializationEvent e) {
    super.postInit(e);
    OverlayEventHandler.register();
  }

  @SubscribeEvent
  public static void registerModels(ModelRegistryEvent event) {
    ModBlocks.initModels();
  }
}
