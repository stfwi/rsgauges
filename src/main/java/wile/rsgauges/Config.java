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

public class Config {

  private static Configuration fileConfig = null;
  private static int gaugeUpdateInterval = 20;
  private static boolean withCabinet = false;

  public static int getGaugeUpdateInterval() {
    return gaugeUpdateInterval;
  }

  public static boolean wichCabinetBasedObjects() {
    return withCabinet;
  }

  public static void onPreInit(FMLPreInitializationEvent event) {
    fileConfig = new Configuration(new File(event.getModConfigurationDirectory().getPath(), ModRsGauges.MODID + ".cfg"));
    update();
  }

  public static void onPostInit(FMLPostInitializationEvent event) {
    try {
      if(fileConfig.hasChanged()) fileConfig.save();
    } catch(Exception e2) {
      ModRsGauges.logger.log(Level.ERROR, "Problem saving config file.", e2);
    }
  }

  public static void update() {
    try {
      fileConfig.load();
      fileConfig.addCustomCategoryComment("general", "General configuration");
      gaugeUpdateInterval = fileConfig.getInt("defaultGaugeUpdateInterval",
          "general", gaugeUpdateInterval, 5, 200,
          "The sample interval of the gauges in ticks.");
      withCabinet = fileConfig.getBoolean("withCabinet",
          "general", withCabinet,
          "Enables registration of cabinet block under test.");
    } catch(Exception e1) {
      ModRsGauges.logger.log(Level.ERROR, "Problem loading config file.", e1);
    } finally {
      try {
        if(fileConfig.hasChanged())
          fileConfig.save();
      } catch(Exception e2) {
        ModRsGauges.logger.log(Level.ERROR, "Problem saving config file.", e2);
      }
    }
  }
}
