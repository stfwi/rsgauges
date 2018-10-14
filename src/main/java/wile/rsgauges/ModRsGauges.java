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
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
  public static final String MODVERSION = "mc1.12.2-1.0.3b3";
  public static Logger logger;

  @SidedProxy(clientSide = "wile.rsgauges.proxy.ClientProxy", serverSide = "wile.rsgauges.proxy.ServerProxy")
  public static CommonProxy proxy;

  @Mod.Instance
  public static ModRsGauges instance;

  @Mod.EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    logger = event.getModLog();
    proxy.preInit(event);
  }

  @Mod.EventHandler
  public void init(FMLInitializationEvent event) {
    proxy.init(event);
  }

  @Mod.EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    ModConfig.onPostInit(event);
    proxy.postInit(event);
  }

  public static final CreativeTabs CREATIVE_TAB_RSGAUGES = (new CreativeTabs("tabrsgauges") {
    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getTabIconItem() { return new ItemStack((ModBlocks.flatgauge1Block != null) ? (ModBlocks.flatgauge1Block) : (Blocks.LEVER)); }
  });

}
