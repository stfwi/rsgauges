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

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Logger;
import wile.rsgauges.libmc.detail.Auxiliaries;
import wile.rsgauges.libmc.detail.OptionalRecipeCondition;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;


public class ModConfig
{
  private static final Logger LOGGER = ModRsGauges.logger();
  private static final String MODID = ModRsGauges.MODID;
  public static final CommonConfig COMMON;
  public static final ServerConfig SERVER;
  public static final ForgeConfigSpec COMMON_CONFIG_SPEC;
  public static final ForgeConfigSpec SERVER_CONFIG_SPEC;

  static {
    final Pair<CommonConfig, ForgeConfigSpec> common_ = (new ForgeConfigSpec.Builder()).configure(CommonConfig::new);
    COMMON_CONFIG_SPEC = common_.getRight();
    COMMON = common_.getLeft();
    final Pair<ServerConfig, ForgeConfigSpec> server_ = (new ForgeConfigSpec.Builder()).configure(ServerConfig::new);
    SERVER_CONFIG_SPEC = server_.getRight();
    SERVER = server_.getLeft();
  }

  //--------------------------------------------------------------------------------------------------------------------

  public static class CommonConfig
  {
    // Optout
    public final ForgeConfigSpec.ConfigValue<String> pattern_excludes;
    public final ForgeConfigSpec.ConfigValue<String> pattern_includes;
    public final ForgeConfigSpec.BooleanValue without_switch_linking;
    // Misc
    public final ForgeConfigSpec.BooleanValue with_experimental;
    public final ForgeConfigSpec.IntValue max_switch_linking_distance;
    public final ForgeConfigSpec.ConfigValue<String> accepted_wrenches;
    public final ForgeConfigSpec.BooleanValue with_config_logging;
    // Tweaks
    public final ForgeConfigSpec.BooleanValue without_gauge_weak_power_measurement;
    public final ForgeConfigSpec.IntValue gauge_update_interval;
    public final ForgeConfigSpec.IntValue autoswitch_volumetric_update_interval;
    public final ForgeConfigSpec.IntValue autoswitch_linear_update_interval;

    CommonConfig(ForgeConfigSpec.Builder builder)
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
        without_switch_linking = builder
          .translation(ModRsGauges.MODID + ".config.without_switch_linking")
          .comment("Disables switch remote linking.")
          .define("without_switch_linking", false);
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
        max_switch_linking_distance = builder
          .translation(ModRsGauges.MODID + ".config.max_switch_linking_distance")
          .comment("Defines how far you or a link source switch can be away from " +
            "the target to activate it. The value 0 means 'no limitation', " +
            " as long as the target chunk is loaded.")
          .defineInRange("max_switch_linking_distance", 48, 0, 64);
        accepted_wrenches = builder
          .translation(ModRsGauges.MODID + ".config.accepted_wrenches")
          .comment("Comma separated list of items names that can be used alter configurable " +
            "blocks of this mod. This applies when the display side of the block is " +
            "right click (activated) with the item in the main hand.")
          .define("accepted_wrenches", "minecraft:redstone_torch,immersiveengineering:screwdriver,immersiveengineering:hammer");
        with_config_logging = builder
          .translation(MODID + ".config.with_config_logging")
          .comment("Enable detailed logging of the config values and resulting calculations in each mod feature config.")
          .define("with_config_logging", false);

        builder.pop();
      }
      // --- TWEAKS -------------------------------------------------------------
      {
        builder.comment("Settings to tweak the performance, or use cases normally no change should be required here.")
          .push("tweaks");
        without_gauge_weak_power_measurement = builder
          .translation(ModRsGauges.MODID + ".config.without_gauge_weak_power_measurement")
          .comment("Gauges shall not frequently lookup weak power provided to the block they are attached to.")
          .define("without_gauge_weak_power_measurement", false);
        gauge_update_interval = builder
          .translation(ModRsGauges.MODID + ".config.gauge_update_interval")
          .comment("Sample interval of the gauges in ticks. Lower values decrease the display latency " +
            "for indirect weak power measurements. Minor performance impact for values >= 5.")
          .defineInRange("gauge_update_interval", 8, 2, 100);
        autoswitch_volumetric_update_interval = builder
          .translation(ModRsGauges.MODID + ".config.autoswitch_volumetric_update_interval")
          .comment("Sample interval of volume sensing automatic switches in ticks (e.g infrared " +
            "motion detector). Lower values make the switches reacting faster, but also " +
            "have an impact on the server performance due to ray tracing.")
          .defineInRange("autoswitch_volumetric_update_interval", 10, 5, 50);
        autoswitch_linear_update_interval = builder
          .translation(ModRsGauges.MODID + ".config.autoswitch_linear_update_interval")
          .comment("Sample interval of the linear switches in ticks (like laser pointer based " +
              "sensors). Lower values make the switches reacting faster, but also have an " +
              "impact on the server performance due to ray tracing. Has much less impact",
            "as the volumetric autoswitch interval.")
          .defineInRange("autoswitch_linear_update_interval", 4, 1, 50);
        builder.pop();
      }
    }
  }

  //--------------------------------------------------------------------------------------------------------------------

  public static class ServerConfig
  {
    ServerConfig(ForgeConfigSpec.Builder builder)
    {
      builder.comment("Settings affecting the logical server side, but are also configurable in single player.")
        .push("server");
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
  { return with_experimental_features_; }

  public static boolean withoutRecipes()
  { return false; }

  //--------------------------------------------------------------------------------------------------------------------
  // Cache
  //--------------------------------------------------------------------------------------------------------------------

  private static final CompoundTag server_config_ = new CompoundTag();
  private static HashSet<String> optouts_ = new HashSet<>();
  private static boolean with_experimental_features_ = false;
  private static boolean with_config_logging_ = false;
  public static boolean status_overlay_disabled = false;
  public static boolean without_switch_linking = false;
  public static boolean without_gauge_weak_power_measurement = false;
  public static boolean without_pulsetime_config = false;
  public static boolean without_switch_nooutput = false;
  public static boolean without_rightclick_item_switchconfig = false;
  public static int max_switch_linking_distance = 16;
  public static int autoswitch_linear_update_interval = 2;
  public static int autoswitch_volumetric_update_interval = 2;
  public static int gauge_update_interval = 2;
  public static int config_left_click_timeout = 600;
  public static double switch_status_overlay_y = 0.75;
  public static final Set<ResourceLocation> accepted_wrenches = new HashSet<>(Arrays.asList(new ResourceLocation("minecraft","redsdtone_torch")));

  public static final CompoundTag getServerConfig()
  { return server_config_; }

  private static final void updateOptouts()
  {
    final ArrayList<String> includes = new ArrayList<>();
    final ArrayList<String> excludes = new ArrayList<>();
    {
      String inc = COMMON.pattern_includes.get().toLowerCase().replaceAll(MODID+":", "").replaceAll("[^*_,a-z0-9]", "");
      if(COMMON.pattern_includes.get() != inc) COMMON.pattern_includes.set(inc);
      String[] incl = inc.split(",");
      for(int i=0; i< incl.length; ++i) {
        incl[i] = incl[i].replaceAll("[*]", ".*?");
        if(!incl[i].isEmpty()) includes.add(incl[i]);
      }
    }
    {
      String exc = COMMON.pattern_excludes.get().toLowerCase().replaceAll(MODID+":", "").replaceAll("[^*_,a-z0-9]", "");
      String[] excl = exc.split(",");
      for(int i=0; i< excl.length; ++i) {
        excl[i] = excl[i].replaceAll("[*]", ".*?");
        if(!excl[i].isEmpty()) excludes.add(excl[i]);
      }
    }
    if(!excludes.isEmpty()) log("Config pattern excludes: '" + String.join(",", excludes) + "'");
    if(!includes.isEmpty()) log("Config pattern includes: '" + String.join(",", includes) + "'");
    {
      HashSet<String> optouts = new HashSet<>();
      ModContent.getRegisteredItems().stream().filter(Objects::nonNull).forEach(
        e -> optouts.add(e.getRegistryName().getPath())
      );
      ModContent.getRegisteredBlocks().stream().filter((Block block) -> {
        if(block==null) return true;
        try {
          if(!with_experimental_features_) {
            if(block instanceof Auxiliaries.IExperimentalFeature) return true;
            if(ModContent.isExperimentalBlock(block)) return true;
          }
          // Force-include/exclude pattern matching
          final String rn = block.getRegistryName().getPath();
          try {
            for(String e : includes) {
              if(rn.matches(e)) {
                return false;
              }
            }
            for(String e : excludes) {
              if(rn.matches(e)) {
                return true;
              }
            }
          } catch(Throwable ex) {
            LOGGER.error("optout include pattern failed, disabling.");
            includes.clear();
            excludes.clear();
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
    OptionalRecipeCondition.on_config(withExperimental(), withoutRecipes(), ModConfig::isOptedOut, ModConfig::isOptedOut);
  }

  public static final void apply()
  {
    if((COMMON == null) || (!COMMON_CONFIG_SPEC.isLoaded())) return;
    with_config_logging_ = COMMON.with_config_logging.get();
    with_experimental_features_ = COMMON.with_experimental.get();
    if(with_experimental_features_) LOGGER.info("Config: EXPERIMENTAL FEATURES ENABLED.");
    updateOptouts();
    without_switch_linking = COMMON.without_switch_linking.get();
    max_switch_linking_distance = COMMON.max_switch_linking_distance.get();
    autoswitch_linear_update_interval = COMMON.autoswitch_linear_update_interval.get();
    autoswitch_volumetric_update_interval = COMMON.autoswitch_volumetric_update_interval.get();
    gauge_update_interval = COMMON.gauge_update_interval.get();
    without_gauge_weak_power_measurement = COMMON.without_gauge_weak_power_measurement.get();
    // Wrenches
    {
      String cfg_wrenches = COMMON.accepted_wrenches.get().toLowerCase().replaceAll("[\\s,]"," ").trim();
      List<ResourceLocation> wrenches = Arrays.stream(cfg_wrenches.split(" "))
        .filter(e->!e.trim().isEmpty())
        .map(ResourceLocation::tryParse)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
      wrenches.add(new ResourceLocation("minecraft", "redstone_torch"));
      wrenches.remove(new ResourceLocation("minecraft", "air"));
      accepted_wrenches.clear();
      accepted_wrenches.addAll(wrenches);
    }
    log("Accepted wrenches: " + accepted_wrenches.stream().map(ResourceLocation::toString).collect(Collectors.joining(",")));
  }

  public static final void log(String config_message)
  {
    if(!with_config_logging_) return;
    LOGGER.info(config_message);
  }

  public static final boolean isWrench(final ItemStack stack)
  { return accepted_wrenches.contains(stack.getItem().getRegistryName()); }

}
