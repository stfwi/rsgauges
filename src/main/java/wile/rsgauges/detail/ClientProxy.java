/*
 * @file ClientProxy.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Client side only initialisation.
 */
package wile.rsgauges.detail;

import wile.rsgauges.ModRsGauges;
import wile.rsgauges.detail.Networking;
import wile.rsgauges.detail.OverlayEventHandler;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy implements ModRsGauges.IProxy
{
  public void preInit(FMLPreInitializationEvent e)
  {
    OBJLoader.INSTANCE.addDomain(ModRsGauges.MODID);
    Networking.preInitClient();
  }

  public void init(FMLInitializationEvent e)
  {}

  public void postInit(FMLPostInitializationEvent e)
  {
    OverlayEventHandler.register();
  }
}
