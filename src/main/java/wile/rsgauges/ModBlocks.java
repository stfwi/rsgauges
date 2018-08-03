/**
 * @file ModBlocks.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Definition and initialisation of blocks of this
 * module, along with their tile entities if applicable.
 *
 * Note: Straight forward definition of different blocks/entities
 *       to make recipes, models and texture definitions easier.
**/
package wile.rsgauges;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wile.rsgauges.blocks.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;

public class ModBlocks {

  // Object holder injection fields for own blocks
  @GameRegistry.ObjectHolder("rsgauges:flatgauge1")         public static final GaugeBlock flatgauge1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:flatgauge2")         public static final GaugeBlock flatgauge2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:flatgauge3")         public static final GaugeBlock flatgauge3Block = null;
  @GameRegistry.ObjectHolder("rsgauges:flatgauge4")         public static final GaugeBlock flatgauge4Block = null;
  @GameRegistry.ObjectHolder("rsgauges:flatgauge5")         public static final GaugeBlock flatgauge5Block = null;
  @GameRegistry.ObjectHolder("rsgauges:flatgauge6")         public static final GaugeBlock flatgauge6Block = null;
  @GameRegistry.ObjectHolder("rsgauges:indicator1")         public static final GaugeBlock indicator1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:indicator2")         public static final GaugeBlock indicator2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:indicator3")         public static final GaugeBlock indicator3Block = null;
  @GameRegistry.ObjectHolder("rsgauges:indicator1blink1")   public static final GaugeBlock indicator1Blink1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:indicator2blink1")   public static final GaugeBlock indicator2Blink1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:indicator3blink1")   public static final GaugeBlock indicator3Blink1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:indicator4")         public static final GaugeBlock indicator4Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch1")    public static final BistableInputBlock bistableSwitch1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch2")    public static final BistableInputBlock bistableSwitch2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch3")    public static final BistableInputBlock bistableSwitch3Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch4")    public static final BistableInputBlock bistableSwitch4Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch5")    public static final BistableInputBlock bistableSwitch5Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch1")       public static final PulseInputBlock pulseSwitch1Block = null;


  // For loop based initialisation
  private static final GaugeBlock gauges[] = {
      new GaugeBlock("flatgauge1", new AxisAlignedBB((2d/16),(2d/16),(0d/16), (14d/16),(14d/16),(1d/16)) ),
      new GaugeBlock("flatgauge2", new AxisAlignedBB((4d/16),(2d/16),(0d/16), (12d/16),(14d/16),(1d/16)) ),
      new GaugeBlock("flatgauge3", new AxisAlignedBB((4d/16),(5d/16),(0d/16), (12d/16),(11d/16),(1d/16)) ),
      new GaugeBlock("flatgauge4", new AxisAlignedBB((7d/16),(3.7d/16),(0d/16), (10d/16),(12d/16),(0.4d/16)) ),
      new GaugeBlock("flatgauge5", new AxisAlignedBB((7d/16),(4d/16),(0d/16), (9d/16),(12d/16),(3d/16)) ),
      new GaugeBlock("flatgauge6", new AxisAlignedBB((2d/16),(4d/16),(0d/16), (14d/16),(12d/16),(1d/16)) )
  };

  private static final GaugeBlock indicators[] = {
      // square LED
      new GaugeBlock("indicator1",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16)),
          5,  // light level scaling
          0   // blink frequency
      ),
      new GaugeBlock("indicator2",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16)),
          5, // light level scaling
          0  // blink frequency
      ),
      new GaugeBlock("indicator3",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16)),
          5, // light level scaling
          0  // blink frequency
      )
  };

  private static final GaugeBlock blinkIndicators[] = {
      // Blinking square LEDs
      new GaugeBlock("indicator1blink1",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16)),
          5,    // light level scaling
          2000  // blink frequency
      ),
      new GaugeBlock("indicator2blink1",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16)),
          5,    // light level scaling
          2000  // blink frequency
      ),
      new GaugeBlock("indicator3blink1",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16)),
          5,    // light level scaling
          2000  // blink frequency
      ),
      // alarm lamp
      new GaugeBlock("indicator4",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(4d/16)),
          8, // light level scaling
          1500  // blink frequency
      )
  };

  private static final BistableInputBlock bistableSwitches[] = {
      new BistableInputBlock("bistableswitch1", new AxisAlignedBB((4d/16),(4d/16),(0d/16),(12d/16),(12d/16),(1d/16)) ),
      new BistableInputBlock("bistableswitch2", new AxisAlignedBB((5d/16),(2d/16),(0d/16),(11d/16),(14d/16),(1.5d/16)) ),
      new BistableInputBlock("bistableswitch3", new AxisAlignedBB((4d/16),(4d/16),(0d/16),(12d/16),(12d/16),(1d/16)) ),
      new BistableInputBlock("bistableswitch4", new AxisAlignedBB((7d/16),(6d/16),(0d/16),(9d/16),(10d/16),(1.5d/16)) ),
      new BistableInputBlock("bistableswitch5", new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(3d/16)) )
  };

  private static final PulseInputBlock pulseSwitches[] = {
      new PulseInputBlock("pulseswitch1", new AxisAlignedBB((5d/16),(5d/16),(0d/16),(11d/16),(11d/16),(1d/16)) )
  };

  // Invoked from CommonProxy.registerBlocks()
  public static final void registerBlocks(RegistryEvent.Register<Block> event) {
    if((!ModConfig.without_gauges) || (!ModConfig.without_indicators) || (!ModConfig.without_blinking_indicators)) {
      GameRegistry.registerTileEntity(GaugeBlock.GaugeTileEntity.class, ModRsGauges.MODID + "_gauge_entity");
    }
    if(!ModConfig.without_gauges) { for(GaugeBlock e:gauges) event.getRegistry().register(e); }
    if(!ModConfig.without_indicators) { for(GaugeBlock e:indicators) event.getRegistry().register(e); }
    if(!ModConfig.without_blinking_indicators) { for(GaugeBlock e:blinkIndicators) event.getRegistry().register(e); }
    if(!ModConfig.without_bistable_switches) { for(BistableInputBlock e:bistableSwitches) event.getRegistry().register(e); }
    if(!ModConfig.without_pulse_switches) {
      GameRegistry.registerTileEntity(PulseInputBlock.UpdateTileEntity.class, ModRsGauges.MODID + "_pulseswitch_entity");
      for(PulseInputBlock e:pulseSwitches) event.getRegistry().register(e);
    }
  }

  // Invoked from ClientProxy.registerModels()
  @SideOnly(Side.CLIENT)
  public static final void initModels() {
    if(!ModConfig.without_gauges) { for(GaugeBlock e:gauges) e.initModel(); }
    if(!ModConfig.without_indicators) { for(GaugeBlock e:indicators) e.initModel(); }
    if(!ModConfig.without_blinking_indicators) { for(GaugeBlock e:blinkIndicators) e.initModel(); }
    if(!ModConfig.without_bistable_switches) { for(BistableInputBlock e:bistableSwitches) e.initModel(); }
    if(!ModConfig.without_pulse_switches) { for(PulseInputBlock e:pulseSwitches) e.initModel(); }
  }

  // Invoked from CommonProxy.registerItems()
  public static final void registerItemBlocks(RegistryEvent.Register<Item> event) {
    if(!ModConfig.without_gauges) { for(GaugeBlock e:gauges) event.getRegistry().register(new ItemBlock(e).setRegistryName(e.getRegistryName())); }
    if(!ModConfig.without_indicators) { for(GaugeBlock e:indicators) event.getRegistry().register(new ItemBlock(e).setRegistryName(e.getRegistryName())); }
    if(!ModConfig.without_blinking_indicators) { for(GaugeBlock e:blinkIndicators) event.getRegistry().register(new ItemBlock(e).setRegistryName(e.getRegistryName())); }
    if(!ModConfig.without_bistable_switches) { for(BistableInputBlock e:bistableSwitches) event.getRegistry().register(new ItemBlock(e).setRegistryName(e.getRegistryName())); }
    if(!ModConfig.without_pulse_switches) { for(PulseInputBlock e:pulseSwitches) event.getRegistry().register(new ItemBlock(e).setRegistryName(e.getRegistryName())); }
  }
}
