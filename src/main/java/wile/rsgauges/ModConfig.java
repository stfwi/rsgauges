/**
 * @file Config.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Main class for module settings. Handles reading and
 * saving the config file.
**/
package wile.rsgauges;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;
import java.io.File;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = ModRsGauges.MODID)
@Config.LangKey("rsgauges.config.title")
public class ModConfig {

  @Config.Comment("Sample interval of the gauges in ticks. Lower values decrease the display "
                + "latency for indirect weak power measurements. The value is mainly related "
                + "to the server side logic. Minor performance impact for values >= 5.")
  @Config.Name("Gauge sample interval")
  @Config.RangeInt(min=2, max=100)
  public static int gauge_update_interval = 8;

  @Config.Comment("Completely disable all (power metering) gauges. Requires restart.")
  @Config.Name("Without gauges")
  @Config.RequiresMcRestart
  public static boolean without_gauges = false;

  @Config.Comment("Completely disable all (blinking and steady) indicator lamps/LEDs. Requires restart.")
  @Config.Name("Without indicators")
  @Config.RequiresMcRestart
  public static boolean without_indicators = false;

  @Config.Comment("Completely disable all blinking indicator lamps/LEDs. Requires restart.")
  @Config.Name("Without blinking indicators")
  @Config.RequiresMcRestart
  public static boolean without_blinking_indicators = false;

  @Config.Comment("Completely disable all (button like) pulse switches. Requires restart.")
  @Config.Name("Without pulse switches")
  @Config.RequiresMcRestart
  public static boolean without_pulse_switches = false;

  @Config.Comment("Completely disable all (lever like) bistable switches. Requires restart.")
  @Config.Name("Without bistable switches")
  @Config.RequiresMcRestart
  public static boolean without_bistable_switches = false;

  @Mod.EventBusSubscriber(modid=ModRsGauges.MODID)
  private static final class EventHandler {
    @SubscribeEvent
    public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
      if(!event.getModID().equals(ModRsGauges.MODID)) return;
      ConfigManager.sync(ModRsGauges.MODID, Config.Type.INSTANCE);
    }
  }
}
