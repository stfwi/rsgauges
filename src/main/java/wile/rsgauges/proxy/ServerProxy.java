/**
 * @file ServerProxy.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Module initialisation event handling, server side only.
**/
package wile.rsgauges.proxy;

import wile.rsgauges.network.*;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ServerProxy extends CommonProxy
{
  @Override
  public void preInit(FMLPreInitializationEvent e) {
    super.preInit(e);
    Networking.preInitServer();
  }
}
