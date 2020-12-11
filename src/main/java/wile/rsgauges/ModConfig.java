/*
 * @file ModConfig.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Main class for module settings. Handles reading and
 * saving the config file.
 */
package wile.rsgauges;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Logger;
import wile.rsgauges.blocks.*;
import wile.rsgauges.items.SwitchLinkPearlItem;
import wile.rsgauges.libmc.detail.Auxiliaries;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;


public class ModConfig
{
  private static final Logger LOGGER = ModRsGauges.logger();
  private static final String MODID = ModRsGauges.MODID;
  public static final CommonConfig COMMON;
  public static final ServerConfig SERVER;
  public static final ClientConfig CLIENT;
  public static final ForgeConfigSpec COMMON_CONFIG_SPEC;
  public static final ForgeConfigSpec SERVER_CONFIG_SPEC;
  public static final ForgeConfigSpec CLIENT_CONFIG_SPEC;

  static {
    final Pair<CommonConfig, ForgeConfigSpec> common_ = (new ForgeConfigSpec.Builder()).configure(CommonConfig::new);
    COMMON_CONFIG_SPEC = common_.getRight();
    COMMON = common_.getLeft();
    final Pair<ServerConfig, ForgeConfigSpec> server_ = (new ForgeConfigSpec.Builder()).configure(ServerConfig::new);
    SERVER_CONFIG_SPEC = server_.getRight();
    SERVER = server_.getLeft();
    final Pair<ClientConfig, ForgeConfigSpec> client_ = (new ForgeConfigSpec.Builder()).configure(ClientConfig::new);
    CLIENT_CONFIG_SPEC = client_.getRight();
    CLIENT = client_.getLeft();
  }

  //--------------------------------------------------------------------------------------------------------------------

  public static class ClientConfig
  {
    ClientConfig(ForgeConfigSpec.Builder builder)
    {
      builder.comment("Settings not loaded on servers.")
        .push("client");
      // --- OPTOUTS ------------------------------------------------------------
      {
      }
      builder.pop();
    }
  }

  //--------------------------------------------------------------------------------------------------------------------

  public static class CommonConfig
  {
    CommonConfig(ForgeConfigSpec.Builder builder)
    {
      builder.comment("!Server side config had to be moved to the 'serverconfig' directory of the game!")
        .push("server");
      builder.pop();
    }
  }

  //--------------------------------------------------------------------------------------------------------------------

  public static class ServerConfig
  {
    // Optout
    public final ForgeConfigSpec.ConfigValue<String> pattern_excludes;
    public final ForgeConfigSpec.ConfigValue<String> pattern_includes;
    public final ForgeConfigSpec.BooleanValue without_gauges;
    public final ForgeConfigSpec.BooleanValue without_indicators;
    public final ForgeConfigSpec.BooleanValue without_blinking_indicators;
    public final ForgeConfigSpec.BooleanValue without_sound_indicators;
    public final ForgeConfigSpec.BooleanValue without_pulse_switches;
    public final ForgeConfigSpec.BooleanValue without_bistable_switches;
    public final ForgeConfigSpec.BooleanValue without_contact_switches;
    public final ForgeConfigSpec.BooleanValue without_automatic_switches;
    public final ForgeConfigSpec.BooleanValue without_linkrelay_switches;
    public final ForgeConfigSpec.BooleanValue without_analog_switches;
    public final ForgeConfigSpec.BooleanValue without_decorative;
    public final ForgeConfigSpec.BooleanValue without_pulsetime_config;
    public final ForgeConfigSpec.BooleanValue without_switch_nooutput;
    public final ForgeConfigSpec.BooleanValue without_switch_linking;
    public final ForgeConfigSpec.BooleanValue without_rightclick_item_switchconfig;
    // this would be normally client config, but buggy, and really annoyed now, so common config then.
    public final ForgeConfigSpec.BooleanValue without_tooltips;
    public final ForgeConfigSpec.DoubleValue switch_status_overlay_y;
    // Misc
    public final ForgeConfigSpec.BooleanValue with_experimental;
    public final ForgeConfigSpec.BooleanValue without_recipes;
    public final ForgeConfigSpec.BooleanValue without_switch_status_overlay;
    public final ForgeConfigSpec.IntValue max_switch_linking_distance;
    public final ForgeConfigSpec.ConfigValue<String> accepted_wrenches;
    // Tweaks
    public final ForgeConfigSpec.BooleanValue without_gauge_weak_power_measurement;
    public final ForgeConfigSpec.IntValue gauge_update_interval;
    public final ForgeConfigSpec.IntValue autoswitch_volumetric_update_interval;
    public final ForgeConfigSpec.IntValue autoswitch_linear_update_interval;
    public final ForgeConfigSpec.IntValue config_left_click_timeout;

    ServerConfig(ForgeConfigSpec.Builder builder)
    {
      builder.comment("Settings affecting the logical server side, but are also configurable in single player.")
        .push("server");
      // --- OPTOUTS ------------------------------------------------------------
      {
        builder.comment("Opt-out settings")
          .push("optout");
        pattern_excludes = builder
          .translation(ModRsGauges.MODID + ".config.pattern_excludes")
          .comment("Opt-out any block by its registry name ('*' wildcard matching, "
            + "comma separated list, whitespaces ignored. You must match the whole name, "
            + "means maybe add '*' also at the begin and end. Example: '*wood*,*steel*' "
            + "excludes everything that has 'wood' or 'steel' in the registry name. "
            + "The matching result is also traced in the log file. ")
          .define("pattern_excludes", "");
        pattern_includes = builder
          .translation(ModRsGauges.MODID + ".config.pattern_includes")
          .comment("Prevent blocks from being opt'ed by registry name ('*' wildcard matching, "
            + "comma separated list, whitespaces ignored. Evaluated before all other opt-out checks. "
            + "You must match the whole name, means maybe add '*' also at the begin and end. Example: "
            + "'*wood*,*steel*' includes everything that has 'wood' or 'steel' in the registry name."
            + "The matching result is also traced in the log file.")
          .define("pattern_includes", "");
        // @Config.Name("Without gauges")
        without_gauges = builder
          .translation(ModRsGauges.MODID + ".config.without_gauges")
          .comment("Completely disable all (power metering) gauges.")
          .define("without_gauges", false);
        // @Config.Name("Without indicators")
        without_indicators = builder
          .translation(ModRsGauges.MODID + ".config.without_indicators")
          .comment("Completely disable all (blinking and steady) indicator lamps/LEDs.")
          .define("without_indicators", false);
        // @Config.Name("Without blinking indicators")
        without_blinking_indicators = builder
          .translation(ModRsGauges.MODID + ".config.without_blinking_indicators")
          .comment("Completely disable all blinking indicator lamps/LEDs.")
          .define("without_blinking_indicators", false);
        // @Config.Name("Without sound indicators")
        without_sound_indicators = builder
          .translation(ModRsGauges.MODID + ".config.without_sound_indicators")
          .comment("Completely disable all sound emitting indicators.")
          .define("without_sound_indicators", false);
        // @Config.Name("Without pulse switches")
        without_pulse_switches = builder
          .translation(ModRsGauges.MODID + ".config.without_pulse_switches")
          .comment("Completely disable all (button like) pulse switches.")
          .define("without_pulse_switches", false);
        // @Config.Name("Without bistable switches")
        without_bistable_switches = builder
          .translation(ModRsGauges.MODID + ".config.without_bistable_switches")
          .comment("Completely disable all (lever like) bistable switches.")
          .define("without_bistable_switches", false);
        // @Config.Name("Without contact switches")
        without_contact_switches = builder
          .translation(ModRsGauges.MODID + ".config.without_contact_switches")
          .comment("Completely disable all contact switches.")
          .define("without_contact_switches", false);
        // @Config.Name("Without automatic switches")
        without_automatic_switches = builder
          .translation(ModRsGauges.MODID + ".config.without_automatic_switches")
          .comment("Completely disable all automatic switches.")
          .define("without_automatic_switches", false);
        // @Config.Name("Without link relay switches")
        without_linkrelay_switches = builder
          .translation(ModRsGauges.MODID + ".config.without_linkrelay_switches")
          .comment("Completely disable all link relay switches.")
          .define("without_linkrelay_switches", false);
        without_analog_switches = builder
          .translation(ModRsGauges.MODID + ".config.without_analog_switches")
          .comment("Completely disable all analog switches, such as dimmers.")
          .define("without_analog_switches", false);
        // @Config.Name("Without decorative blocks")
        without_decorative = builder
          .translation(ModRsGauges.MODID + ".config.without_decorative")
          .comment("Completely disable all decorative blocks.")
          .define("without_decorative", false);
        // @Config.Name("Without pulse time configuration")
        without_pulsetime_config = builder
          .translation(ModRsGauges.MODID + ".config.without_pulsetime_config")
          .comment("Disable pulse time configuration of switches using redstone dust stack clicking.")
          .define("without_pulsetime_config", false);
        //@Config.Name("Without switch 'no output' option")
        without_switch_nooutput = builder
          .translation(ModRsGauges.MODID + ".config.without_switch_nooutput")
          .comment("Disable the 'no output' config option for switches.")
          .define("without_switch_nooutput", false);
        //@Config.Name("Without switch linking")
        without_switch_linking = builder
          .translation(ModRsGauges.MODID + ".config.without_switch_linking")
          .comment("Disables switch remote linking.")
          .define("without_switch_linking", false);
        without_recipes = builder
          .translation(ModRsGauges.MODID + ".config.without_recipes")
          .comment("Disable all internal recipes, allowing to use alternative pack recipes.")
          .define("without_recipes", false);
        without_rightclick_item_switchconfig = builder
          .translation(ModRsGauges.MODID + ".config.without_rightclick_item_switchconfig")
          .comment(
            "Disables the possibility to right click switches with Redstone Dust, " +
            "Ender Pearls or Switch Link pearls for configuration or linking. " +
            "Can be useful if it is important to the players that no unforeseen " +
            "switch configuration happens when activating a switch was intended. " +
            "Affects server side only. Can be changed during operation."
          )
          .define("without_rightclick_item_switchconfig", false);
        builder.pop();
      }
      // --- MISC ---------------------------------------------------------------
      {
        builder.comment("Miscellaneous settings")
          .push("miscellaneous");
        with_experimental = builder
          .translation(ModRsGauges.MODID + ".config.with_experimental")
          .comment("Enables experimental features. Use at own risk.")
          .define("with_experimental", false);
        // @Config.Name("Without switch status overlay")
        without_switch_status_overlay = builder
          .translation(ModRsGauges.MODID + ".config.without_switch_status_overlay")
          .comment("Disable the status overlay for switches and use chat messages instead.")
          .define("without_switch_status_overlay", false);
        // @Config.Name("Max switch linking distance")
        max_switch_linking_distance = builder
          .translation(ModRsGauges.MODID + ".config.max_switch_linking_distance")
          .comment("Defines how far you or a link source switch can be away from " +
                   "the target to activate it. The value 0 means 'no limitation', " +
                   " as long as the target chunk is loaded.")
          .defineInRange("max_switch_linking_distance", 48, 0, 64);
        // @Config.Name("Accepted wrenches")
        accepted_wrenches = builder
          .translation(ModRsGauges.MODID + ".config.accepted_wrenches")
          .comment("Comma separated list of items names that can be used alter configurable " +
                   "blocks of this mod. This applies when the display side of the block is " +
                   "right click (activated) with the item in the main hand.")
          .define("accepted_wrenches", "minecraft:redstone_torch,immersiveengineering:screwdriver,immersiveengineering:hammer");
        builder.pop();
      }
      // --- TWEAKS -------------------------------------------------------------
      {
        // @Config.Name("Performance and usability tweaks")
        builder.comment("Settings to tweak the performance, or use cases normally no change should be required here.")
          .push("tweaks");
        // @Config.Name("Without gauge weak power measurements")
        without_gauge_weak_power_measurement = builder
          .translation(ModRsGauges.MODID + ".config.without_gauge_weak_power_measurement")
          .comment("Gauges shall not frequently lookup weak power provided to the block they are attached to.")
          .define("without_gauge_weak_power_measurement", false);
        // @Config.Name("Gauge sample interval")
        gauge_update_interval = builder
          .translation(ModRsGauges.MODID + ".config.gauge_update_interval")
          .comment("Sample interval of the gauges in ticks. Lower values decrease the display latency " +
                   "for indirect weak power measurements. Minor performance impact for values >= 5.")
          .defineInRange("gauge_update_interval", 8, 2, 100);
        // @Config.Name("Volumetric sensor switch sample interval")
        autoswitch_volumetric_update_interval = builder
          .translation(ModRsGauges.MODID + ".config.autoswitch_volumetric_update_interval")
          .comment("Sample interval of volume sensing automatic switches in ticks (e.g infrared " +
                   "motion detector). Lower values make the switches reacting faster, but also " +
                   "have an impact on the server performance due to ray tracing.")
          .defineInRange("autoswitch_volumetric_update_interval", 10, 5, 50);
        // @Config.Name("Linear sensor switch sample interval")
        autoswitch_linear_update_interval = builder
          .translation(ModRsGauges.MODID + ".config.autoswitch_linear_update_interval")
          .comment("Sample interval of the linear switches in ticks (like laser pointer based " +
                   "sensors). Lower values make the switches reacting faster, but also have an " +
                   "impact on the server performance due to ray tracing. Has much less impact",
                   "as the volumetric autoswitch interval.")
          .defineInRange("autoswitch_linear_update_interval", 4, 1, 50);
        // @Config.Name("Switch left-double-click config timeout")
        config_left_click_timeout = builder
          .translation(ModRsGauges.MODID + ".config.config_left_click_timeout")
          .comment("Timeout in milliseconds defining the timeout for left clicking switches or devices in order to " +
            "configure them. If the device can be opened, it will be opened on 'double-left-click' and closed " +
            "again on 'single-left-click'. The item in the hand must be a valid wrench (see 'Accepted wrenches'). " +
            "For switches/devices that cannot be opened, multi-clicking cycles through the configuration options. " +
            "The block has to be at least clicked two times withing the timeout to differ configuration from block " +
            "breaking, and prevent misconfiguration on unintended left-clicking.")
          .defineInRange("config_left_click_timeout", 700, 500, 1200);

        ////// Client config that was moved to COMMON due to bugs.
        // @Config.Name("Without tooltips")
        without_tooltips = builder
          .translation(ModRsGauges.MODID + ".config.without_tooltips")
          .comment("Disable CTRL-SHIFT item tooltip display.")
          .define("without_tooltips", false);
        // @Config.Name("Switch status overlay y-position")
        switch_status_overlay_y = builder
          .translation(ModRsGauges.MODID + ".config.switch_status_overlay_y")
          .comment("Vertial position of the switch status overlay message.")
          .defineInRange("switch_status_overlay_y", 0.75, 0.1, 0.8);

        builder.pop();
      }
    }
  }

  //--------------------------------------------------------------------------------------------------------------------
  // Optout checks
  //--------------------------------------------------------------------------------------------------------------------

  public static final boolean isOptedOut(final @Nullable Block block)
  { return isOptedOut(block.asItem()); }

  public static final boolean isOptedOut(final @Nullable Item item)
  { return (item!=null) && optouts_.contains(item.getRegistryName().getPath()); }

  public static boolean withExperimental()
  { return with_experimental; }

  //--------------------------------------------------------------------------------------------------------------------
  // Cache
  //--------------------------------------------------------------------------------------------------------------------

  private static final CompoundNBT server_config_ = new CompoundNBT();
  private static HashSet<String> optouts_ = new HashSet<>();

  public static final CompoundNBT getServerConfig() // config that may be synchronized from server to client via net pkg.
  { return server_config_; }

  public static boolean status_overlay_disabled = false;
  public static boolean without_switch_linking = false;
  public static boolean without_gauge_weak_power_measurement = false;
  public static boolean without_pulsetime_config = false;
  public static boolean without_switch_nooutput = false;
  public static boolean without_indicators = false;
  public static boolean without_blinking_indicators = false;
  public static boolean without_sound_indicators = false;
  public static boolean without_gauges = false;
  public static boolean without_bistable_switches = false;
  public static boolean without_pulse_switches = false;
  public static boolean without_contact_switches = false;
  public static boolean without_automatic_switches = false;
  public static boolean without_linkrelay_switches = false;
  public static boolean without_decorative = false;
  public static boolean without_analog_switches = false;
  public static boolean without_rightclick_item_switchconfig = false;
  public static int max_switch_linking_distance = 16;
  public static int autoswitch_linear_update_interval = 4;
  public static int autoswitch_volumetric_update_interval = 4;
  public static int gauge_update_interval = 4;
  public static int config_left_click_timeout = 600;
  public static double switch_status_overlay_y = 0.75;
  public static final Set<ResourceLocation> accepted_wrenches = new HashSet<>(Arrays.asList(new ResourceLocation("minecraft","redsdtone_torch")));
  public static boolean with_experimental = false;

  private static final void updateOptouts()
  {
    final ArrayList<String> includes_ = new ArrayList<String>();
    final ArrayList<String> excludes_ = new ArrayList<String>();
    {
      String inc = SERVER.pattern_includes.get().toLowerCase().replaceAll(MODID+":", "").replaceAll("[^*_,a-z0-9]", "");
      if(SERVER.pattern_includes.get() != inc) SERVER.pattern_includes.set(inc);
      if(!inc.isEmpty()) LOGGER.info("Config pattern includes: '" + inc + "'");
      String[] incl = inc.split(",");
      includes_.clear();
      for(int i=0; i< incl.length; ++i) {
        incl[i] = incl[i].replaceAll("[*]", ".*?");
        if(!incl[i].isEmpty()) includes_.add(incl[i]);
      }
    }
    {
      String exc = SERVER.pattern_excludes.get().toLowerCase().replaceAll(MODID+":", "").replaceAll("[^*_,a-z0-9]", "");
      if(!exc.isEmpty()) LOGGER.info("Config pattern excludes: '" + exc + "'");
      String[] excl = exc.split(",");
      excludes_.clear();
      for(int i=0; i< excl.length; ++i) {
        excl[i] = excl[i].replaceAll("[*]", ".*?");
        if(!excl[i].isEmpty()) excludes_.add(excl[i]);
      }
    }
    {
      boolean with_log_details = false;
      HashSet<String> optouts = new HashSet<>();
      ModContent.getRegisteredItems().stream().filter((Item item) -> {
        if(item == null) return true;
        if(without_switch_linking && (item instanceof SwitchLinkPearlItem)) return true;
        return false;
      }).forEach(
        e -> optouts.add(e.getRegistryName().getPath())
      );
      ModContent.getRegisteredBlocks().stream().filter((Block block) -> {
        if(block==null) return true;
        try {
          if(!SERVER.with_experimental.get()) {
            if(block instanceof Auxiliaries.IExperimentalFeature) return true;
            if(ModContent.isExperimentalBlock(block)) return true;
          }
          final String rn = block.getRegistryName().getPath();
          // Force-include/exclude pattern matching
          try {
            for(String e : includes_) {
              if(rn.matches(e)) {
                if(with_log_details) LOGGER.info("Optout force include: "+rn);
                return false;
              }
            }
            for(String e : excludes_) {
              if(rn.matches(e)) {
                if(with_log_details) LOGGER.info("Optout force exclude: "+rn);
                return true;
              }
            }
          } catch(Throwable ex) {
            LOGGER.error("optout include pattern failed, disabling.");
            includes_.clear();
            excludes_.clear();
          }
          // Early non-opt out type based evaluation
          if(block instanceof IndicatorBlock) {
            IndicatorBlock bl = ((IndicatorBlock)block);
            if(without_indicators) return true;
            if((without_blinking_indicators) && ((bl.config & GaugeBlock.GAUGE_DATA_BLINKING) > 0)) return true;
            if((without_sound_indicators) && ((bl.power_on_sound != null) || (bl.power_off_sound != null))) return true;
          } if(block instanceof GaugeBlock) {
            GaugeBlock bl = ((GaugeBlock)block);
            if(without_gauges) return true;
          } else if(block instanceof SwitchBlock) {
            SwitchBlock bl = ((SwitchBlock)block);
            if((without_bistable_switches) && (((bl.config & SwitchBlock.SWITCH_CONFIG_BISTABLE)!=0) || (bl instanceof BistableSwitchBlock))) return true;
            if((without_pulse_switches) && (((bl.config & SwitchBlock.SWITCH_CONFIG_PULSE)!=0) || (bl instanceof PulseSwitchBlock))) return true;
            if((without_contact_switches) && ((bl.config & SwitchBlock.SWITCH_CONFIG_CONTACT)!=0)) return true;
            if((without_analog_switches) && (bl instanceof DimmerSwitchBlock)) return true;
            if((without_linkrelay_switches) && (((bl.config & SwitchBlock.SWITCH_CONFIG_LINK_SENDER)!=0) || (bl instanceof LinkSenderSwitchBlock))) return true;
            if(without_automatic_switches) {
              if((bl.config & SwitchBlock.SWITCH_CONFIG_AUTOMATIC)!=0) return true;
              if(bl instanceof PulseKnockSwitchBlock) return true;
              if(bl instanceof BistableKnockSwitchBlock) return true;
              if(bl instanceof ComparatorSwitchBlock) return true;
              if(bl instanceof DoorSensorSwitchBlock) return true;
            }
            if(without_switch_linking) {
              if(bl instanceof LinkReceiverSwitchBlock) return true;
              if(bl instanceof LinkSenderSwitchBlock) return true;
            }
            if(without_decorative) {
              if(bl==ModContent.ELEVATOR_BUTTON) return true;
              if(bl==ModContent.ARROW_TARGET_SWITCH) return true;
            }
          } else if(block instanceof SensitiveGlassBlock) {
            if(without_decorative) return true;
          }
        } catch(Exception ex) {
          LOGGER.error("Exception evaluating the optout config: '"+ex.getMessage()+"'");
        }
        return false;
      }).forEach(
        e -> optouts.add(e.getRegistryName().getPath())
      );
      optouts_ = optouts;
    }
  }

  public static final void apply()
  {
    if(SERVER == null) return;
    status_overlay_disabled = SERVER.without_switch_status_overlay.get();
    without_switch_linking = SERVER.without_switch_linking.get();
    max_switch_linking_distance = SERVER.max_switch_linking_distance.get();
    autoswitch_linear_update_interval = SERVER.autoswitch_linear_update_interval.get();
    autoswitch_volumetric_update_interval = SERVER.autoswitch_volumetric_update_interval.get();
    gauge_update_interval = SERVER.gauge_update_interval.get();
    without_gauge_weak_power_measurement = SERVER.without_gauge_weak_power_measurement.get();
    without_pulsetime_config = SERVER.without_pulsetime_config.get();
    config_left_click_timeout= SERVER.config_left_click_timeout.get();
    without_switch_nooutput = SERVER.without_switch_nooutput.get();
    without_indicators = SERVER.without_indicators.get();
    without_blinking_indicators = SERVER.without_blinking_indicators.get();
    without_sound_indicators = SERVER.without_sound_indicators.get();
    without_gauges = SERVER.without_gauges.get();
    without_bistable_switches = SERVER.without_bistable_switches.get();
    without_pulse_switches = SERVER.without_pulse_switches.get();
    without_contact_switches = SERVER.without_contact_switches.get();
    without_automatic_switches = SERVER.without_automatic_switches.get();
    without_linkrelay_switches = SERVER.without_linkrelay_switches.get();
    without_analog_switches = SERVER.without_analog_switches.get();
    without_decorative = SERVER.without_decorative.get();
    switch_status_overlay_y = SERVER.switch_status_overlay_y.get();
    without_rightclick_item_switchconfig = SERVER.without_rightclick_item_switchconfig.get();
    with_experimental = SERVER.with_experimental.get();
    // Wrenches
    {
      String cfg_wrenches = SERVER.accepted_wrenches.get().toLowerCase().replaceAll("[\\s,]"," ").trim();
      List<ResourceLocation> wrenches = Arrays.stream(cfg_wrenches.split(" "))
        .filter(e->!e.trim().isEmpty())
        .map(ResourceLocation::tryCreate)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
      wrenches.add(new ResourceLocation("minecraft", "redstone_torch"));
      wrenches.remove(new ResourceLocation("minecraft", "air"));
      accepted_wrenches.clear();
      accepted_wrenches.addAll(wrenches);
    }
    LOGGER.info("Accepted wrenches: " + accepted_wrenches.stream().map(ResourceLocation::toString).collect(Collectors.joining(",")));
    // Opt-outs
    updateOptouts();
  }

  public static final boolean isWrench(final ItemStack stack)
  { return accepted_wrenches.contains(stack.getItem().getRegistryName()); }

}
