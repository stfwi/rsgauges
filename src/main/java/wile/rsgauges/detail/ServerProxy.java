/*
 * @file ServerProxy.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Module initialisation event handling, server side only.
 */
package wile.rsgauges.detail;

import wile.rsgauges.ModRsGauges;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

public class ServerProxy implements ModRsGauges.IProxy
{
  public void preInit(FMLPreInitializationEvent e)
  {
    Networking.preInitServer();
  }

  public void init(FMLInitializationEvent e)
  {}

  public void postInit(FMLPostInitializationEvent e)
  {}
}
