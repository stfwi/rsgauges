/**
 * @file ModConfig.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Main class for module settings. Handles reading and
 * saving the config file.
**/
package wile.rsgauges;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;

@Config(modid = ModRsGauges.MODID)
@Config.LangKey("rsgauges.config.title")
public class ModConfig
{
  @Config.Comment("Sample interval of the gauges in ticks. Lower values decrease the display "
                + "latency for indirect weak power measurements. The value is mainly related "
                + "to the server side logic. Minor performance impact for values >= 5.")
  @Config.Name("Gauge sample interval")
  @Config.RangeInt(min=2, max=100)
  public static int gauge_update_interval = 8;

  @Config.Comment("Sample interval of volume sensing automatic switches in ticks (e.g infrared motion detector). "
      + "Lower values make the switches reacting faster, but also have an impact on the server performance due "
      + "to ray tracing.")
  @Config.Name("Volumetric sensor switch sample interval")
  @Config.RangeInt(min=5, max=50)
  public static int autoswitch_volumetric_update_interval = 10;

  @Config.Comment("Sample interval of the linear switches in ticks (like laser pointer based sensors). "
      + "Lower values make the switches reacting faster, but also have an impact on the server performance "
      + "due to ray tracing. Has much less impact as the volumetric autoswitch interval.")
  @Config.Name("Linear sensor switch sample interval")
  @Config.RangeInt(min=1, max=50)
  public static int autoswitch_linear_update_interval = 4;

  @Config.Comment("Completely disable all (power metering) gauges. Requires restart.")
  @Config.Name("Without gauges")
  @Config.RequiresMcRestart
  public static boolean without_gauges = false;

  @Config.Comment("Testing: Gauges shall not frequently lookup weak power provided to the block they"
                + "are attached to.")
  @Config.Name("Without gauge weak power measurements")
  public static boolean without_gauge_weak_power_measurement = false;

  @Config.Comment("Completely disable all (blinking and steady) indicator lamps/LEDs. Requires restart.")
  @Config.Name("Without indicators")
  @Config.RequiresMcRestart
  public static boolean without_indicators = false;

  @Config.Comment("Completely disable all blinking indicator lamps/LEDs. Requires restart.")
  @Config.Name("Without blinking indicators")
  @Config.RequiresMcRestart
  public static boolean without_blinking_indicators = false;

  @Config.Comment("Completely disable all sound emmitting indicators. Requires restart.")
  @Config.Name("Without blinking indicators")
  @Config.RequiresMcRestart
  public static boolean without_sound_indicators = false;

  @Config.Comment("Completely disable all (button like) pulse switches. Requires restart.")
  @Config.Name("Without pulse switches")
  @Config.RequiresMcRestart
  public static boolean without_pulse_switches = false;

  @Config.Comment("Completely disable all (lever like) bistable switches. Requires restart.")
  @Config.Name("Without bistable switches")
  @Config.RequiresMcRestart
  public static boolean without_bistable_switches = false;

  @Config.Comment("Completely disable all contact switches. Requires restart.")
  @Config.Name("Without contact switches")
  @Config.RequiresMcRestart
  public static boolean without_contact_switches = false;

  @Config.Comment("Completely disable all automatic switches. Requires restart.")
  @Config.Name("Without automatic switches")
  @Config.RequiresMcRestart
  public static boolean without_automatic_switches = false;

  @Config.Comment("Completely disable all decorative blocks. Requires restart.")
  @Config.Name("Without decorative blocks")
  @Config.RequiresMcRestart
  public static boolean without_decorative = false;

  @Config.Comment("Disable pulse time configuration of switches using redstone dust stack clicking.")
  @Config.Name("Without pulse time configuration")
  public static boolean without_pulsetime_config = false;

  @Config.Comment("Disable color tinting for switches.")
  @Config.Name("Without switch color tinting")
  @Config.RequiresMcRestart
  public static boolean without_switch_colortinting = false;

  @Config.Comment("Disable the 'no output' config option for switches.")
  @Config.Name("Without switch 'no output' option")
  public static boolean without_switch_nooutput = true;

  @Config.Comment("Disable the status overlay for switches and use chat messages instead.")
  @Config.Name("Testing: Without switch status overlay")
  @Config.RequiresMcRestart
  public static boolean z_without_switch_status_overlay = false;

  @Config.Comment("Vertial position of the switch status overlay message.")
  @Config.Name("Testing: Switch status overlay y-position")
  @Config.RangeDouble(min=0.1, max=0.8)
  public static double z_switch_status_overlay_y = 0.75f;

  @Config.Comment("Disable tile entity update() for detector switches (for performance testing only, don't do this at home).")
  @Config.Name("Testing: Without detector switch update")
  public static boolean z_without_detector_switch_update = false;

  @Config.Comment("Disable tile entity update() for environmental sensor switches (for performance testing only, don't do this at home).")
  @Config.Name("Testing: Without environmental switch update")
  public static boolean z_without_environmental_switch_update = false;

  @Config.Comment("Disable tile entity update() for time based switches (for performance testing only, don't do this at home).")
  @Config.Name("Testing: Without timer switch update")
  public static boolean z_without_timer_switch_update = false;

  @Config.Comment("Defines a constant light level for sensitive glass on the server, " +
                  "no matter if the block is powered or not. Prevents performance issues " +
                  "if many sensitive glass blocks are frequently switched on and off. "
                  )
  @Config.Name("Constant sensitive glass light level on server")
  public static boolean sensitive_glass_constant_server_light_level = false;

  @Config.Comment("Comma sepatated list of items names that can be used alter configurable blocks of this mod." +
                  "This applies when the display side of the block is right click (activated) with the item in the " +
                  "main hand. Empty hand is 'air'.")
  @Config.Name("Accepted wrenches")
  public static String accepted_wrenches = "air";

  @Config.Comment("Timeout in milliseconds defining the timeout for left clicking switches or devices in order to " +
                  "configure them. If the device can be opened, it will be opened on 'double-left-click' and closed " +
                  "again on 'single-left-click'. The item in the hand must be a valid wrench (see 'Accepted wrenches'). " +
                  "For switches/devices that cannot be opened, multi-clicking cycles through the configuration options. " +
                  "The block has to be at least clicked two times withing the timeout to differ configuration from block " +
                  "breaking, and prevent misconfiguration on unintended left-clicking.")
  @Config.Name("Config left multi-click timeout")
  public static int config_left_click_timeout = 700;


  @Mod.EventBusSubscriber(modid=ModRsGauges.MODID)
  private static final class EventHandler
  {
    @SubscribeEvent
    public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
      if(!event.getModID().equals(ModRsGauges.MODID)) return;
      ConfigManager.sync(ModRsGauges.MODID, Config.Type.INSTANCE);
      update();
    }
  }

  public static final void onPostInit(FMLPostInitializationEvent event)
  { update(); }

  private static final void update()
  {}

}
