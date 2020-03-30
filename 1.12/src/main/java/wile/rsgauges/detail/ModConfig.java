/*
 * @file ModConfig.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Main class for module settings. Handles reading and
 * saving the config file.
 */
package wile.rsgauges.detail;

import net.minecraft.item.ItemBlock;
import wile.rsgauges.ModRsGauges;
import wile.rsgauges.blocks.*;
import wile.rsgauges.items.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;

import java.util.ArrayList;

@Config(modid = ModRsGauges.MODID)
@Config.LangKey("rsgauges.config.title")
public class ModConfig
{
  @Config.Comment({
    "Settings to completly omit defined categories."
  })
  @Config.Name("Feature opt-outs")
  public static final SettingsAFeatureOptout optouts = new SettingsAFeatureOptout();
  public static final class SettingsAFeatureOptout
  {
    @Config.Comment({
      "Completely disable all (power metering) gauges.",
      "Affects server and client side. Requires restart."
    })
    @Config.Name("Without gauges")
    @Config.RequiresMcRestart
    public boolean without_gauges = false;

    @Config.Comment({
      "Gauges shall not frequently lookup weak power provided to the block they " +
      "are attached to.",
      "Affects server side. Can be changed while the game is running."
    })
    @Config.Name("Without gauge weak power measurements")
    public boolean without_gauge_weak_power_measurement = false;

    @Config.Comment({
      "Completely disable all (blinking and steady) indicator lamps/LEDs.",
      "Affects server and client side. Requires restart."
    })
    @Config.Name("Without indicators")
    @Config.RequiresMcRestart
    public boolean without_indicators = false;

    @Config.Comment({
      "Completely disable all blinking indicator lamps/LEDs.",
      "Affects server and client side. Requires restart."
    })
    @Config.Name("Without blinking indicators")
    @Config.RequiresMcRestart
    public boolean without_blinking_indicators = false;

    @Config.Comment({
      "Completely disable all sound emmitting indicators.",
      "Affects server and client side. Requires restart."
    })
    @Config.Name("Without blinking indicators")
    @Config.RequiresMcRestart
    public boolean without_sound_indicators = false;

    @Config.Comment({
      "Completely disable all (button like) pulse switches.",
      "Affects server and client side. Requires restart."
    })
    @Config.Name("Without pulse switches")
    @Config.RequiresMcRestart
    public boolean without_pulse_switches = false;

    @Config.Comment({
      "Completely disable all (lever like) bistable switches.",
      "Affects server and client side. Requires restart."
    })
    @Config.Name("Without bistable switches")
    @Config.RequiresMcRestart
    public boolean without_bistable_switches = false;

    @Config.Comment({
      "Completely disable all contact switches.",
      "Affects server and client side. Requires restart."
    })
    @Config.Name("Without contact switches")
    @Config.RequiresMcRestart
    public boolean without_contact_switches = false;

    @Config.Comment({
      "Completely disable all automatic switches.",
      "Affects server and client side. Requires restart."
    })
    @Config.Name("Without automatic switches")
    @Config.RequiresMcRestart
    public boolean without_automatic_switches = false;

    @Config.Comment({
      "Completely disable all link relay switches.",
      "Affects server and client side. Requires restart."
    })
    @Config.Name("Without link relay switches")
    @Config.RequiresMcRestart
    public boolean without_linkrelay_switches = false;

    @Config.Comment({
      "Completely disable all decorative blocks.",
      "Affects server and client side. Requires restart."
    })
    @Config.Name("Without decorative blocks")
    @Config.RequiresMcRestart
    public boolean without_decorative = false;

    @Config.Comment({
      "Disable pulse time configuration of switches using redstone dust " +
      "stack clicking",
      "Affects server side only. Can be changed while the game is running."
    })
    @Config.Name("Without pulse time configuration")
    public boolean without_pulsetime_config = false;

    @Config.Comment({
      "Disable color tinting for switches and gauges.",
      "Affects client and server side. Can be changed during server operation," +
      "requires client restart.",
    })
    @Config.Name("Without color tinting")
    @Config.RequiresMcRestart
    public boolean without_color_tinting = false;

    @Config.Comment({
      "Disable the 'no output' config option for switches.",
      "Affects server side only. Can be changed while the game is running."
    })
    @Config.Name("Without switch 'no output' option")
    public boolean without_switch_nooutput = false;

    @Config.Comment({
      "Disables switch remote linking.",
      "Affects server side only. Can be changed during operation."
    })
    @Config.Name("Without switch linking")
    public boolean without_switch_linking = false;

    @Config.Comment({
      "Disables the possibility to right click switches with Redstone Dust, " +
      "Ender Pearls or Switch Link pearls for configuration or linking. " +
      "Can be useful if it is important to the players that no unforseen " +
      "switch configuration happens when activating a switch was intended. " +
      "Affects server side only. Can be changed during operation."
    })
    @Config.Name("Without rclick item config")
    public boolean without_rightclick_item_switchconfig = false;

    @Config.Comment({
      "Opt-out any block by its registry name ('*' wildcard matching, "
      + "comma separated list, whitespaces ignored. You must match the whole name, "
      + "means maybe add '*' also at the begin and end. Example: '*wood*,*steel*' "
      + "excludes everything that has 'wood' or 'steel' in the registry name. "
      + "The matching result is also traced in the log file. "
    })
    @Config.Name("Pattern exclude")
    public String pattern_excludes = "";

    @Config.Comment({
      "Prevent blocks from being opt'ed by registry name ('*' wildcard matching, "
      + "comma separated list, whitespaces ignored. Evaluated before all other opt-out checks. "
      + "You must match the whole name, means maybe add '*' also at the begin and end. Example: "
      + "'*wood*,*steel*' includes everything that has 'wood' or 'steel' in the registry name."
      + "The matching result is also traced in the log file."
    })
    @Config.Name("Pattern include")
    public String pattern_includes = "";

  }

  @Config.Comment({
    "Settings to tweak the performance, or use cases normally no change should be" +
    "required here."
  })
  @Config.Name("Performance and usability tweaks")
  public static final SettingsBPerformance tweaks = new SettingsBPerformance();
  public static final class SettingsBPerformance
  {
    @Config.Comment({
      "Sample interval of the gauges in ticks. Lower values decrease the display latency " +
      "for indirect weak power measurements. Minor performance impact for values >= 5.",
      "Affects server side only. Can be changed during operation."
    })
    @Config.Name("Gauge sample interval")
    @Config.RangeInt(min=2, max=100)
    public int gauge_update_interval = 8;

    @Config.Comment({
      "Sample interval of volume sensing automatic switches in ticks (e.g infrared " +
      "motion detector). Lower values make the switches reacting faster, but also " +
      "have an impact on the server performance due to ray tracing.",
      "Affects server side only. Can be changed during operation."
    })
    @Config.Name("Volumetric sensor switch sample interval")
    @Config.RangeInt(min=5, max=50)
    public int autoswitch_volumetric_update_interval = 10;

    @Config.Comment({
      "Sample interval of the linear switches in ticks (like laser pointer based " +
      "sensors). Lower values make the switches reacting faster, but also have an " +
      "impact on the server performance due to ray tracing. Has much less impact",
      "as the volumetric autoswitch interval.",
      "Affects server side only. Can be changed during operation."
    })
    @Config.Name("Linear sensor switch sample interval")
    @Config.RangeInt(min=1, max=50)
    public int autoswitch_linear_update_interval = 4;

    @Config.Comment({
      "Timeout in milliseconds defining the timeout for left clicking switches or devices in order to " +
      "configure them. If the device can be opened, it will be opened on 'double-left-click' and closed " +
      "again on 'single-left-click'. The item in the hand must be a valid wrench (see 'Accepted wrenches'). " +
      "For switches/devices that cannot be opened, multi-clicking cycles through the configuration options. " +
      "The block has to be at least clicked two times withing the timeout to differ configuration from block " +
      "breaking, and prevent misconfiguration on unintended left-clicking.",
      "Affects server side only. Can be changed during operation."
    })
    @Config.Name("Switch left-double-click config timeout")
    public int config_left_click_timeout = 700;
  }

  @Config.Comment({
    "Settings for beta testing and trouble shooting. Some of the settings " +
    "may be moved to other categories after testing."
  })
  @Config.Name("Miscellaneous")
  public static final SettingsZTesting zmisc = new SettingsZTesting();
  public static final class SettingsZTesting
  {
    @Config.Comment({
      "Disable the status overlay for switches and use chat messages instead.",
      "Affects server and client side. Requires restart."
    })
    @Config.Name("Without switch status overlay")
    @Config.RequiresMcRestart
    public boolean without_switch_status_overlay = false;

    @Config.Comment({
      "Vertial position of the switch status overlay message.",
      "Affects client only. Can be changed in-game."
    })
    @Config.Name("Switch status overlay y-position")
    @Config.RangeDouble(min=0.1, max=0.8)
    public double switch_status_overlay_y = 0.75f;

    @Config.Comment({
      "Disable tile entity update() for detector switches",
      "(for performance testing only, don't do this at home).",
      "Affects server side only. Can be changed during operation."
    })
    @Config.Name("Without detector switch update")
    public boolean without_detector_switch_update = false;

    @Config.Comment({
      "Disable tile entity update() for environmental sensor switches.",
      "(for performance testing only, don't do this at home)",
      "Affects server side only. Can be changed during operation."
    })

    @Config.Name("Without environmental switch update")
    public boolean without_environmental_switch_update = false;

    @Config.Comment({
      "Disable tile entity update() for time based switches.",
      "(for performance testing only, don't do this at home)",
      "Affects server side only. Can be changed during operation."
    })
    @Config.Name("Without timer switch update")
    public boolean without_timer_switch_update = false;

    @Config.Comment({
      "Defines how far you or a link source switch can be away from " +
      "the target to activate it. The value 0 means 'no limitation'.",
      "Affects server and client side. Can be changed in-game."
    })
    @Config.Name("Max switch linking distance")
    public int max_switch_linking_distance = 48;

    @Config.Comment({
      "Defines a constant light level for sensitive glass on the server, " +
      "no matter if the block is powered or not. Prevents performance issues " +
      "if many sensitive glass blocks are frequently switched on and off.",
      "Affects server side only. Can be changed during operation."
    })
    @Config.Name("Constant sensitive glass light level on server")
    public boolean sensitive_glass_constant_server_light_level = false;

    @Config.Comment({
      "Comma sepatated list of items names that can be used alter configurable " +
      "blocks of this mod. This applies when the display side of the block is " +
      "right click (activated) with the item in the main hand.",
      "Affects server side only. Can be changed during operation."
    })
    @Config.Name("Accepted wrenches")
    public String accepted_wrenches = "redstone_torch";

    @Config.Comment({
      "Blocks and items opt'ed out in this config will not disabled in the " +
      "recipe system, but not registered at all. Note this can cause trouble " +
      "if server and client have different settings here.",
      "Affects server and client side. Requires restart."
    })
    @Config.Name("Omit registrations of opt-out features")
    public boolean without_optout_registrations = false;

    @Config.Comment({
      "Enables experimental features, blocks or items.",
      "Affects server and client side. Use at own risk."
    })
    @Config.Name("With experimental")
    public boolean with_experimental = false;
  }

  @SuppressWarnings("unused")
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

  @SuppressWarnings("unused")
  public static final void onPostInit(FMLPostInitializationEvent event)
  { update(); }

  private static final ArrayList<String> includes_ = new ArrayList<String>();
  private static final ArrayList<String> excludes_ = new ArrayList<String>();

  private static final void update()
  {
    zmisc.accepted_wrenches = zmisc.accepted_wrenches.toLowerCase().replaceAll("[\\s]","").replaceAll(",,",",");
    zmisc.accepted_wrenches = ("," + zmisc.accepted_wrenches + ",").replaceAll(",air,",",redstone_torch,"); // @todo added for version transition, normally .replaceAll(",air,",",")
    zmisc.accepted_wrenches = zmisc.accepted_wrenches.replaceAll("[,]+$", "").replaceAll("^[,]+", "");
    // patterns
    {
      String inc = optouts.pattern_includes.toLowerCase().replaceAll(ModRsGauges.MODID+":", "").replaceAll("[^*_,a-z0-9]", "");
      if(optouts.pattern_includes != inc) optouts.pattern_includes = inc;
      if(!inc.isEmpty()) ModRsGauges.logger.info("Pattern includes: '" + inc + "'");
      String[] incl = inc.split(",");
      includes_.clear();
      for(int i=0; i< incl.length; ++i) {
        incl[i] = incl[i].replaceAll("[*]", ".*?");
        if(!incl[i].isEmpty()) includes_.add(incl[i]);
      }
    }
    {
      String exc = optouts.pattern_excludes.toLowerCase().replaceAll(ModRsGauges.MODID+":", "").replaceAll("[^*_,a-z0-9]", "");
      if(!exc.isEmpty()) ModRsGauges.logger.info("Pattern excludes: '" + exc + "'");
      String[] excl = exc.split(",");
      excludes_.clear();
      for(int i=0; i< excl.length; ++i) {
        excl[i] = excl[i].replaceAll("[*]", ".*?");
        if(!excl[i].isEmpty()) excludes_.add(excl[i]);
      }
    }
  }

  // Code unification
  public static boolean isOptedOut(Block block)
  { return !enabled(block); }

  public static boolean isOptedOut(Item item)
  { return (!enabled(item)) || ((item instanceof ItemBlock) && (!enabled(Block.getBlockFromItem(item)))); }

  public static boolean isWithoutRecipes()
  { return false; }

  private static boolean enabled(Block block)
  {
    try {
      final String rn = block.getRegistryName().getPath();
      for(String e : includes_) {
        if(rn.matches(e)) {
          return true;
        }
      }
      for(String e : excludes_) {
        if(rn.matches(e)) {
          return false;
        }
      }
    } catch(Throwable e) {
      ModRsGauges.logger.error("Exception while parsing exclude/include pattern: " + e.getMessage());
    }
    if(block instanceof BlockIndicator) {
      BlockIndicator bl = ((BlockIndicator)block);
      if((bl.config & RsBlock.RSBLOCK_CONFIG_OBSOLETE) != 0) return false;
      if(ModConfig.optouts.without_indicators) return false;
      if((ModConfig.optouts.without_blinking_indicators) && (bl.blink_interval() > 0)) return false;
      if((ModConfig.optouts.without_sound_indicators) && ((bl.power_on_sound != null) || (bl.power_off_sound != null))) return false;
    } if(block instanceof BlockGauge) {
      BlockGauge bl = ((BlockGauge)block);
      if((bl.config & RsBlock.RSBLOCK_CONFIG_OBSOLETE) != 0) return false;
      if(ModConfig.optouts.without_gauges) return false;
    } else if(block instanceof BlockSwitch) {
      BlockSwitch bl = ((BlockSwitch)block);
      if((bl.config & RsBlock.RSBLOCK_CONFIG_OBSOLETE) != 0) return false;
      if((ModConfig.optouts.without_bistable_switches) && ((bl.config & BlockSwitch.SWITCH_CONFIG_BISTABLE)!=0)) return false;
      if((ModConfig.optouts.without_pulse_switches) && ((bl.config & BlockSwitch.SWITCH_CONFIG_PULSE)!=0)) return false;
      if((ModConfig.optouts.without_contact_switches) && ((bl.config & BlockSwitch.SWITCH_CONFIG_CONTACT)!=0)) return false;
      if((ModConfig.optouts.without_automatic_switches) && ((bl.config & BlockSwitch.SWITCH_CONFIG_AUTOMATIC)!=0)) return false;
      if((ModConfig.optouts.without_linkrelay_switches)  && ((bl.config & BlockSwitch.SWITCH_CONFIG_LINK_RELAY)!=0)) return false;
    } else if(block instanceof BlockSensitiveGlass) {
      if(ModConfig.optouts.without_decorative) return false;
    }
    return true;
  }

  private static boolean enabled(Item item)
  {
    if((ModConfig.optouts.without_switch_linking) && (item instanceof ItemSwitchLinkPearl)) return false;
    return true;
  }

}
