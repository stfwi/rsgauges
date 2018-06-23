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
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wile.rsgauges.blocks.*;

public class ModBlocks {

  @GameRegistry.ObjectHolder("rsgauges:flatgauge1")
  public static final GaugeBlock flatgauge1Block = new GaugeBlock("flatgauge1",
      new AxisAlignedBB((2d/16),(2d/16),(0d/16), (14d/16),(14d/16),(1d/16))
  );

  @GameRegistry.ObjectHolder("rsgauges:flatgauge2")
  public static final GaugeBlock flatgauge2Block = new GaugeBlock("flatgauge2",
      new AxisAlignedBB((4d/16),(2d/16),(0d/16), (12d/16),(14d/16),(1d/16))
  );

  @GameRegistry.ObjectHolder("rsgauges:flatgauge3")
  public static final GaugeBlock flatgauge3Block = new GaugeBlock("flatgauge3",
      new AxisAlignedBB((4d/16),(5d/16),(0d/16), (12d/16),(11d/16),(1d/16))
  );

  @GameRegistry.ObjectHolder("rsgauges:flatgauge4")
  public static final GaugeBlock flatgauge4Block = new GaugeBlock("flatgauge4",
      new AxisAlignedBB((7d/16),(3.7d/16),(0d/16), (10d/16),(12d/16),(0.4d/16))
  );

  @GameRegistry.ObjectHolder("rsgauges:flatgauge5")
  public static final GaugeBlock flatgauge5Block = new GaugeBlock("flatgauge5",
      new AxisAlignedBB((7d/16),(4d/16),(0d/16), (9d/16),(12d/16),(3d/16))
  );

  @GameRegistry.ObjectHolder("rsgauges:flatgauge6")
  public static final GaugeBlock flatgauge6Block = new GaugeBlock("flatgauge6",
      new AxisAlignedBB((2d/16),(4d/16),(0d/16), (14d/16),(12d/16),(1d/16))
  );

  @GameRegistry.ObjectHolder("rsgauges:indicator1")
  public static final GaugeBlock indicator1Block = new GaugeBlock("indicator1",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16)),
      5
  );

  @GameRegistry.ObjectHolder("rsgauges:indicator2")
  public static final GaugeBlock indicator2Block = new GaugeBlock("indicator2",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16)),
      5 // light level scaling
  );

  @GameRegistry.ObjectHolder("rsgauges:indicator3")
  public static final GaugeBlock indicator3Block = new GaugeBlock("indicator3",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16)),
      5 // light level scaling
  );

  @GameRegistry.ObjectHolder("rsgauges:indicator1blink1")
  public static final GaugeBlock indicator1Blink1Block = new GaugeBlock("indicator1blink1",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16)),
      5,     // light level scaling
      10,2,2 // alternation time base, on time, off time
  );

  @GameRegistry.ObjectHolder("rsgauges:indicator2blink1")
  public static final GaugeBlock indicator2Blink1Block = new GaugeBlock("indicator2blink1",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16)),
      5,     // light level scaling
      10,2,2 // alternation time base, on time, off time
  );

  @GameRegistry.ObjectHolder("rsgauges:indicator3blink1")
  public static final GaugeBlock indicator3Blink1Block = new GaugeBlock("indicator3blink1",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16)),
      5,     // light level scaling
      10,2,2 // alternation time base, on time, off time
  );

  @GameRegistry.ObjectHolder("rsgauges:indicator4")
  public static final GaugeBlock indicator4Block = new GaugeBlock("indicator4",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(4d/16)),
      8,     // light level scaling
      5,2,6  // alternation time base, on time, off time
  );

  @GameRegistry.ObjectHolder("rsgauges:bistableswitch1")
  public static final BistableInputBlock bistableSwitch1Block = new BistableInputBlock("bistableswitch1",
      new AxisAlignedBB((4d/16),(4d/16),(0d/16),(12d/16),(12d/16),(1d/16))
  );

  @GameRegistry.ObjectHolder("rsgauges:bistableswitch2")
  public static final BistableInputBlock bistableSwitch2Block = new BistableInputBlock("bistableswitch2",
      new AxisAlignedBB((5d/16),(2d/16),(0d/16),(11d/16),(14d/16),(1.5d/16))
  );

  @GameRegistry.ObjectHolder("rsgauges:bistableswitch3")
  public static final BistableInputBlock bistableSwitch3Block = new BistableInputBlock("bistableswitch3",
      new AxisAlignedBB((4d/16),(4d/16),(0d/16),(12d/16),(12d/16),(1d/16))
  );

  @GameRegistry.ObjectHolder("rsgauges:bistableswitch4")
  public static final BistableInputBlock bistableSwitch4Block = new BistableInputBlock("bistableswitch4",
      new AxisAlignedBB((7d/16),(6d/16),(0d/16),(9d/16),(10d/16),(1.5d/16))
  );

  @GameRegistry.ObjectHolder("rsgauges:bistableswitch5")
  public static final BistableInputBlock bistableSwitch5Block = new BistableInputBlock("bistableswitch5",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(3d/16))
  );

  @GameRegistry.ObjectHolder("rsgauges:pulseswitch1")
  public static final PulseInputBlock pulseSwitch1Block = new PulseInputBlock("pulseswitch1",
      new AxisAlignedBB((5d/16),(5d/16),(0d/16),(11d/16),(11d/16),(1d/16))
  );



  // If list based initialisation / registration starts making sense ...
  private static ResourceLocation makeRegistryName(String unlocalized) {
    return new ResourceLocation(unlocalized.substring(unlocalized.indexOf(ModRsGauges.MODID)).replaceFirst("\\.", ":"));
  }

  // Invoked from ClientProxy.registerModels()
  @SideOnly(Side.CLIENT)
  public static final void initModels() {
    flatgauge1Block.initModel();
    flatgauge2Block.initModel();
    flatgauge3Block.initModel();
    flatgauge4Block.initModel();
    flatgauge5Block.initModel();
    flatgauge6Block.initModel();
    indicator1Block.initModel();
    indicator2Block.initModel();
    indicator3Block.initModel();
    indicator1Blink1Block.initModel();
    indicator2Blink1Block.initModel();
    indicator3Blink1Block.initModel();
    indicator4Block.initModel();
    bistableSwitch1Block.initModel();
    bistableSwitch2Block.initModel();
    bistableSwitch3Block.initModel();
    bistableSwitch4Block.initModel();
    bistableSwitch5Block.initModel();
    pulseSwitch1Block.initModel();
  }

  // Invoked from CommonProxy.registerBlocks()
  public static final void registerBlocks(RegistryEvent.Register<Block> event) {
    GameRegistry.registerTileEntity(GaugeTileEntity.class, ModRsGauges.MODID + "_gauge_entity");
    event.getRegistry().register(flatgauge1Block);
    event.getRegistry().register(flatgauge2Block);
    event.getRegistry().register(flatgauge3Block);
    event.getRegistry().register(flatgauge4Block);
    event.getRegistry().register(flatgauge5Block);
    event.getRegistry().register(flatgauge6Block);
    event.getRegistry().register(indicator1Block);
    event.getRegistry().register(indicator2Block);
    event.getRegistry().register(indicator3Block);
    event.getRegistry().register(indicator1Blink1Block);
    event.getRegistry().register(indicator2Blink1Block);
    event.getRegistry().register(indicator3Blink1Block);
    event.getRegistry().register(indicator4Block);
    event.getRegistry().register(bistableSwitch1Block);
    event.getRegistry().register(bistableSwitch2Block);
    event.getRegistry().register(bistableSwitch3Block);
    event.getRegistry().register(bistableSwitch4Block);
    event.getRegistry().register(bistableSwitch5Block);
    event.getRegistry().register(pulseSwitch1Block);
  }

  // Invoked from CommonProxy.registerItems()
  public static final void registerItemBlocks(RegistryEvent.Register<Item> event) {
    event.getRegistry().register(new ItemBlock(flatgauge1Block).setRegistryName(flatgauge1Block.getRegistryName()));
    event.getRegistry().register(new ItemBlock(flatgauge2Block).setRegistryName(flatgauge2Block.getRegistryName()));
    event.getRegistry().register(new ItemBlock(flatgauge3Block).setRegistryName(flatgauge3Block.getRegistryName()));
    event.getRegistry().register(new ItemBlock(flatgauge4Block).setRegistryName(flatgauge4Block.getRegistryName()));
    event.getRegistry().register(new ItemBlock(flatgauge5Block).setRegistryName(flatgauge5Block.getRegistryName()));
    event.getRegistry().register(new ItemBlock(flatgauge6Block).setRegistryName(flatgauge6Block.getRegistryName()));
    event.getRegistry().register(new ItemBlock(indicator1Block).setRegistryName(indicator1Block.getRegistryName()));
    event.getRegistry().register(new ItemBlock(indicator2Block).setRegistryName(indicator2Block.getRegistryName()));
    event.getRegistry().register(new ItemBlock(indicator3Block).setRegistryName(indicator3Block.getRegistryName()));
    event.getRegistry().register(new ItemBlock(indicator1Blink1Block).setRegistryName(indicator1Blink1Block.getRegistryName()));
    event.getRegistry().register(new ItemBlock(indicator2Blink1Block).setRegistryName(indicator2Blink1Block.getRegistryName()));
    event.getRegistry().register(new ItemBlock(indicator3Blink1Block).setRegistryName(indicator3Blink1Block.getRegistryName()));
    event.getRegistry().register(new ItemBlock(indicator4Block).setRegistryName(indicator4Block.getRegistryName()));
    event.getRegistry().register(new ItemBlock(bistableSwitch1Block).setRegistryName(bistableSwitch1Block.getRegistryName()));
    event.getRegistry().register(new ItemBlock(bistableSwitch2Block).setRegistryName(bistableSwitch2Block.getRegistryName()));
    event.getRegistry().register(new ItemBlock(bistableSwitch3Block).setRegistryName(bistableSwitch3Block.getRegistryName()));
    event.getRegistry().register(new ItemBlock(bistableSwitch4Block).setRegistryName(bistableSwitch4Block.getRegistryName()));
    event.getRegistry().register(new ItemBlock(bistableSwitch5Block).setRegistryName(bistableSwitch5Block.getRegistryName()));
    event.getRegistry().register(new ItemBlock(pulseSwitch1Block).setRegistryName(pulseSwitch1Block.getRegistryName()));
  }
}
