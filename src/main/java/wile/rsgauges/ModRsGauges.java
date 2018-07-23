/**
 * @file ModRsGauges.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Main mod class.
 *
**/
package wile.rsgauges;

import wile.rsgauges.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(
    modid = ModRsGauges.MODID,
    name = ModRsGauges.MODNAME,
    version = ModRsGauges.MODVERSION,
    dependencies = "required-after:forge@[14.23.2.2611,)",
    useMetadata = true
)
public class ModRsGauges {

  public static final String MODID = "rsgauges";
  public static final String MODNAME = "Redstone gauges";
  public static final String MODVERSION = "1.0.0rc3";
  public static Logger logger;

  @SidedProxy(clientSide = "wile.rsgauges.proxy.ClientProxy", serverSide = "wile.rsgauges.proxy.ServerProxy")
  public static CommonProxy proxy;

  @Mod.Instance
  public static ModRsGauges instance;

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    logger = event.getModLog();
    Config.onPreInit(event);
    proxy.preInit(event);
  }

  @Mod.EventHandler
  public void init(FMLInitializationEvent event) {
    proxy.init(event);
    Config.update();
  }

  @Mod.EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    Config.onPostInit(event);
    proxy.postInit(event);
  }
}
