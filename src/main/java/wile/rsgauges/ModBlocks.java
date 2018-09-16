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
import net.minecraft.init.SoundEvents;

public class ModBlocks {

  @GameRegistry.ObjectHolder("rsgauges:flatgauge1")               public static final GaugeBlock flatgauge1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:flatgauge2")               public static final GaugeBlock flatgauge2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:flatgauge3")               public static final GaugeBlock flatgauge3Block = null;
  @GameRegistry.ObjectHolder("rsgauges:flatgauge4")               public static final GaugeBlock flatgauge4Block = null;
  @GameRegistry.ObjectHolder("rsgauges:flatgauge5")               public static final GaugeBlock flatgauge5Block = null;
  @GameRegistry.ObjectHolder("rsgauges:flatgauge6")               public static final GaugeBlock flatgauge6Block = null;
  private static final GaugeBlock gauges[] = {
      new GaugeBlock("flatgauge1", new AxisAlignedBB((2d/16),(2d/16),(0d/16), (14d/16),(14d/16),(1d/16)) ),
      new GaugeBlock("flatgauge2", new AxisAlignedBB((4d/16),(2d/16),(0d/16), (12d/16),(14d/16),(1d/16)) ),
      new GaugeBlock("flatgauge3", new AxisAlignedBB((4d/16),(5d/16),(0d/16), (12d/16),(11d/16),(1d/16)) ),
      new GaugeBlock("flatgauge4", new AxisAlignedBB((7d/16),(3.7d/16),(0d/16), (10d/16),(12d/16),(0.4d/16)) ),
      new GaugeBlock("flatgauge5", new AxisAlignedBB((7d/16),(4d/16),(0d/16), (9d/16),(12d/16),(3d/16)) ),
      new GaugeBlock("flatgauge6", new AxisAlignedBB((2d/16),(4d/16),(0d/16), (14d/16),(12d/16),(1d/16)) )
  };

  @GameRegistry.ObjectHolder("rsgauges:indicator1")               public static final GaugeBlock indicator1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:indicator2")               public static final GaugeBlock indicator2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:indicator3")               public static final GaugeBlock indicator3Block = null;
  private static final GaugeBlock indicators[] = {
      // square LED
      new GaugeBlock("indicator1",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16)),
          5, 0  // light level scaling, blink frequency
      ),
      new GaugeBlock("indicator2",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16)),
          5, 0  // light level scaling, blink frequency
      ),
      new GaugeBlock("indicator3",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16)),
          5, 0  // light level scaling, blink frequency
      )
  };

  @GameRegistry.ObjectHolder("rsgauges:indicator1blink1")         public static final GaugeBlock indicator1Blink1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:indicator2blink1")         public static final GaugeBlock indicator2Blink1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:indicator3blink1")         public static final GaugeBlock indicator3Blink1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:indicator4")               public static final GaugeBlock indicator4Block = null;
  private static final GaugeBlock blinkIndicators[] = {
      // Blinking square LEDs
      new GaugeBlock("indicator1blink1",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16)),
          5, 2000  // light level scaling, blink frequency
      ),
      new GaugeBlock("indicator2blink1",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16)),
          5, 2000  // light level scaling, blink frequency
      ),
      new GaugeBlock("indicator3blink1",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16)),
          5, 2000  // light level scaling, blink frequency
      ),
      // alarm lamp
      new GaugeBlock("indicator4",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(4d/16)),
          8, 1500  // light level scaling, blink frequency
      )
  };

  @GameRegistry.ObjectHolder("rsgauges:soundindicator1")          public static final GaugeBlock soundIndicator1Block = null;
  private static final GaugeBlock soundIndicators[] = {
      // Alarm siren
      new GaugeBlock("soundindicator1",
          new AxisAlignedBB((4d/16),(6.5d/16),(0d/16), (11.5d/16),(9.5d/16),(4d/16)),
          1, 1500, // light level scaling, blink frequency
          new ModResources.BlockSoundEvent(ModResources.alarm_siren_sound),
          null
      )
  };

  @GameRegistry.ObjectHolder("rsgauges:bistableswitch1")          public static final SwitchBlock bistableSwitch1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch2")          public static final SwitchBlock bistableSwitch2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch3")          public static final SwitchBlock bistableSwitch3Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch4")          public static final SwitchBlock bistableSwitch4Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch5")          public static final SwitchBlock bistableSwitch5Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch6")          public static final SwitchBlock bistableSwitch6Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch7")          public static final SwitchBlock bistableSwitch7Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch8")          public static final SwitchBlock bistableSwitch8Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_oldfancy1") public static final SwitchBlock bistableSwitchOldFancy1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_oldfancy2") public static final SwitchBlock bistableSwitchOldFancy2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_oldfancy3") public static final SwitchBlock bistableSwitchOldFancy3Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_oldfancy4") public static final SwitchBlock bistableSwitchOldFancy4Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_oldfancy5") public static final SwitchBlock bistableSwitchOldFancy5Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_oldfancy6") public static final SwitchBlock bistableSwitchOldFancy6Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_oldfancy7") public static final SwitchBlock bistableSwitchOldFancy7Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_rustic1")   public static final SwitchBlock bistableSwitchRustic1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_rustic2")   public static final SwitchBlock bistableSwitchRustic2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_rustic3")   public static final SwitchBlock bistableSwitchRustic3Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_rustic4")   public static final SwitchBlock bistableSwitchRustic4Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_rustic5")   public static final SwitchBlock bistableSwitchRustic5Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_rustic6")   public static final SwitchBlock bistableSwitchRustic6Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_rustic7")   public static final SwitchBlock bistableSwitchRustic7Block = null;
  private static final SwitchBlock bistableSwitches[] = {
      // Rotary machine switch
      new SwitchBlock("bistableswitch1",
          new AxisAlignedBB((4d/16),(4d/16),(0d/16),(12d/16),(12d/16),(1d/16)),
          SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      // Contact lever switch
      new SwitchBlock("bistableswitch2",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(14d/16),(1.5d/16)),
          new AxisAlignedBB((6d/16),(2d/16),(0d/16),(10d/16),(10d/16),(1.5d/16)),
          SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      // two-button machine switch
      new SwitchBlock("bistableswitch3",
          new AxisAlignedBB((4d/16),(4d/16),(0d/16),(12d/16),(12d/16),(1d/16)),
          SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      // Light flip switch
      new SwitchBlock("bistableswitch4",
          new AxisAlignedBB((7d/16),(6d/16),(0d/16),(9d/16),(10d/16),(1.5d/16)),
          SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      // ESTOP button
      new SwitchBlock("bistableswitch5",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1.5d/16)),
          new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(2.5d/16)),
          SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
          SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE_OFF
      ),
      // Hopper blocking switch
      new SwitchBlock("bistableswitch6",
          new AxisAlignedBB((4.8d/16),(10d/16),(0d/16), (11.2d/16),(11d/16),(6d/16)), // bb when off
          new AxisAlignedBB((4.8d/16),(10d/16),(0d/16), (11.2d/16),(11d/16),(3d/16)), // bb when on
          SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
          SwitchBlock.SWITCH_CONFIG_HOPPER_MOUNTBALE|SwitchBlock.SWITCH_DATA_WEAK
      ),
      // mechanical rotary lever
      new SwitchBlock("bistableswitch7",
          new AxisAlignedBB((2.5d/16),(9.5d/16),(0d/16), (12.3d/16),(13d/16),(2.7d/16)), // bb when off
          new AxisAlignedBB((8.7d/16),( 3d/16),(0d/16), (12.3d/16),(13d/16),(2.7d/16)), // bb when on
          SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
          SwitchBlock.SWITCH_CONFIG_HOPPER_MOUNTBALE
      ),
      // mechanical lever
      new SwitchBlock("bistableswitch8",
          new AxisAlignedBB((6.5d/16),(6.5d/16),(0d/16), (9.7d/16),(14.0d/16),(3d/16)), // bb when off
          new AxisAlignedBB((6.5d/16),(5.0d/16),(0d/16), (9.7d/16),(12.5d/16),(3d/16)), // bb when on
          SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      // Old fancy gold decorated lever
      new SwitchBlock("bistableswitch_oldfancy1",
          new AxisAlignedBB((6d/16),(6.5d/16),(0d/16), (10.3d/16),(13.5d/16),(4.5d/16)), // bb when off
          new AxisAlignedBB((6d/16),(3.5d/16),(0d/16), (10.3d/16),(10.0d/16),(4.5d/16)), // bb when on
          SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      // Old fancy angular lever
      new SwitchBlock("bistableswitch_oldfancy2",
          new AxisAlignedBB((2.5d/16),(6.0d/16),(0d/16), (9.7d/16),(10.0d/16),(4.5d/16)), // bb when off
          new AxisAlignedBB((4.5d/16),(3.5d/16),(0d/16), (9.2d/16),(10.0d/16),(4.5d/16)), // bb when on
          SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
          SwitchBlock.SWITCH_CONFIG_HOPPER_MOUNTBALE
      ),
      // Old fancy bent lever
      new SwitchBlock("bistableswitch_oldfancy3",
          new AxisAlignedBB((4.5d/16),(5.0d/16),(0d/16), (12.0d/16),(11.0d/16),(4.5d/16)), // bb when off
          new AxisAlignedBB((4.5d/16),(5.0d/16),(0d/16), (12.0d/16),(11.0d/16),(4.5d/16)), // bb when on
          SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      // Old fancy gold decorated manual slide sphere connector
      new SwitchBlock("bistableswitch_oldfancy4",
          new AxisAlignedBB((5.0d/16),(6.0d/16),(0d/16), (13.0d/16),(13.0d/16),(4.5d/16)),
          SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      // Old fancy gold decorated rotary sphere connector
      new SwitchBlock("bistableswitch_oldfancy5",
          new AxisAlignedBB((5.0d/16),(5.0d/16),(0d/16), (13.0d/16),(13.0d/16),(4.5d/16)),
          new AxisAlignedBB((5.0d/16),(6.0d/16),(0d/16), (13.0d/16),(13.0d/16),(4.5d/16)),
          SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      // Old fancy gold decorated rotary sphere connector 2
      new SwitchBlock("bistableswitch_oldfancy6",
          new AxisAlignedBB((5.0d/16),(5.0d/16),(0d/16), (13.0d/16),(13.0d/16),(4.5d/16)),
          SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      // Old fancy gold decorated manual contactor
      new SwitchBlock("bistableswitch_oldfancy7",
          new AxisAlignedBB((2.0d/16),(6.0d/16),(0d/16), (11.7d/16),(14.0d/16),(4.5d/16)), // bb when off
          new AxisAlignedBB((3.5d/16),(3.5d/16),(0d/16), (11.2d/16),(14.0d/16),(4.5d/16)), // bb when on
          SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      // Rustic lever 1
      new SwitchBlock("bistableswitch_rustic1",
          new AxisAlignedBB((6d/16),(5.0d/16),(0d/16), (10.3d/16),(15.0d/16),(4.5d/16)), // bb when off
          new AxisAlignedBB((6d/16),(2.0d/16),(0d/16), (10.3d/16),(11.0d/16),(4.5d/16)), // bb when on
          SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      // Rustic lever 2 (bolted)
      new SwitchBlock("bistableswitch_rustic2",
          new AxisAlignedBB((4d/16),(6.0d/16),(0d/16), (13.0d/16),(13.0d/16),(4.5d/16)), // bb when off
          new AxisAlignedBB((4d/16),(4.0d/16),(0d/16), (13.0d/16),(10.0d/16),(4.5d/16)), // bb when on
          SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      // Rustic lever 3 (big angular)
      new SwitchBlock("bistableswitch_rustic3",
          new AxisAlignedBB((0.2d/16),(10.0d/16),(0d/16), (13.5d/16),(15.0d/16),(4.5d/16)), // bb when off
          new AxisAlignedBB((5.5d/16),( 2.0d/16),(0d/16), (13.5d/16),(15.0d/16),(4.5d/16)), // bb when on
          SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
          SwitchBlock.SWITCH_CONFIG_HOPPER_MOUNTBALE
      ),
      // Rustic lever 4 (counter weighted rotary lever)
      new SwitchBlock("bistableswitch_rustic4",
          new AxisAlignedBB((0.5d/16),(11.0d/16),(0d/16), (14.0d/16),(15.0d/16),(4.5d/16)), // bb when off
          SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
          SwitchBlock.SWITCH_CONFIG_HOPPER_MOUNTBALE
      ),
      // Rustic lever 5
      new SwitchBlock("bistableswitch_rustic5",
          new AxisAlignedBB((3.5d/16),(6.0d/16),(0d/16), (12.5d/16),(11.0d/16),(4.5d/16)),
          SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      // Rustic lever 6 (latch slide)
      new SwitchBlock("bistableswitch_rustic6",
          new AxisAlignedBB((3d/16),(6.0d/16),(0d/16), (13.0d/16),(12.0d/16),(4.5d/16)),
          SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      // Rustic lever 7 (The Nail)
      new SwitchBlock("bistableswitch_rustic7",
          new AxisAlignedBB((6.0d/16),(7.0d/16),(0d/16), (9.0d/16),(10.0d/16),(3.0d/16)),
          SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
          SwitchBlock.SWITCH_CONFIG_HOPPER_MOUNTBALE
      ),
  };

  @GameRegistry.ObjectHolder("rsgauges:pulseswitch1")             public static final SwitchBlock pulseSwitch1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch2")             public static final SwitchBlock pulseSwitch2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch3")             public static final SwitchBlock pulseSwitch3Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch5")             public static final SwitchBlock pulseSwitch5Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch6")             public static final SwitchBlock pulseSwitch6Block = null;
  @GameRegistry.ObjectHolder("rsgauges:arrowtarget")              public static final SwitchBlock pulseSwitchArrowTargetBlock = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch_oldfancy1")    public static final SwitchBlock pulseSwitchOldFancy1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch_oldfancy2")    public static final SwitchBlock pulseSwitchOldFancy2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch_oldfancy3")    public static final SwitchBlock pulseSwitchOldFancy3Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch_oldfancy4")    public static final SwitchBlock pulseSwitchOldFancy4Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch_rustic1")      public static final SwitchBlock pulseSwitchRustic1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch_rustic2")      public static final SwitchBlock pulseSwitchRustic2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch_rustic3")      public static final SwitchBlock pulseSwitchRustic3Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch_rustic4")      public static final SwitchBlock pulseSwitchRustic4Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch_rustic5")      public static final SwitchBlock pulseSwitchRustic5Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch_rustic6")      public static final SwitchBlock pulseSwitchRustic6Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch_rustic7")      public static final SwitchBlock pulseSwitchRustic7Block = null;
  private static final SwitchBlock pulseSwitches[] = {
      // Square machine pulse switch
      new SwitchBlock("pulseswitch1",
          new AxisAlignedBB((5d/16),(5d/16),(0d/16),(11d/16),(11d/16),(1d/16)),
          SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
          SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      // Fenced round machine pulse switch
      new SwitchBlock("pulseswitch2",
          new AxisAlignedBB((5.5d/16),(5.5d/16),(0d/16),(10.5d/16),(10.5d/16),(0.5d/16)),
          SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
          SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      // Mechanical spring reset pull handle
      new SwitchBlock("pulseswitch3",
          new AxisAlignedBB((5.5d/16),(5.5d/16),(0d/16),(10.5d/16),(10.5d/16),(2d/16)),
          SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
          SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      // Arrow target
      new SwitchBlock("arrowtarget",
          new AxisAlignedBB((5d/16),(5d/16),(0d/16),(11d/16),(11d/16),(1d/16)),
          SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
          SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      // Mechanical spring reset push button
      new SwitchBlock("pulseswitch5",
          new AxisAlignedBB((5.5d/16),(0.5d/16),(0d/16),(10.5d/16),(5.0d/16),(4d/16)),
          SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
          SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      // Retro double pole switch
      new SwitchBlock("pulseswitch6",
          new AxisAlignedBB((7d/16),(6d/16),(0d/16),(12d/16),(10d/16),(4d/16)),
          SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
          SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      // Old fancy (golden decorated) button
      new SwitchBlock("pulseswitch_oldfancy1",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1.5d/16)),
          SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
          SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      // Old fancy (golden decorated) chain pulse switch
      new SwitchBlock("pulseswitch_oldfancy2",
          new AxisAlignedBB((6.5d/16),(4.8d/16),(0d/16),(9.5d/16),(13d/16),(4d/16)),
          new AxisAlignedBB((6.5d/16),(3.8d/16),(0d/16),(9.5d/16),(12d/16),(4d/16)),
          SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
          SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      // Old fancy (golden decorated) crank
      new SwitchBlock("pulseswitch_oldfancy3",
          new AxisAlignedBB((6.5d/16),(4.8d/16),(0d/16),(9.5d/16),(13d/16),(4d/16)),
          new AxisAlignedBB((6.5d/16),(3.8d/16),(0d/16),(9.5d/16),(12d/16),(4d/16)),
          SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
          SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      // Old fancy (golden decorated) tiny button
      new SwitchBlock("pulseswitch_oldfancy4",
          new AxisAlignedBB((7d/16),(7d/16),(0d/16),(9d/16),(9d/16),(1.5d/16)),
          SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
          SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      // Rustic button 1
      new SwitchBlock("pulseswitch_rustic1",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(2.5d/16)),
          SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
          SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      // Rustic button 2 (bolted)
      new SwitchBlock("pulseswitch_rustic2",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(2.5d/16)),
          SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
          SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      // Rustic button 3 (pull chain)
      new SwitchBlock("pulseswitch_rustic3",
          new AxisAlignedBB((6d/16),(6.5d/16),(0d/16),(10d/16),(14.5d/16),(2.5d/16)),
          SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
          SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE
      ),
      // Rustic button 4 (weight reset pull chain)
      new SwitchBlock("pulseswitch_rustic4",
          new AxisAlignedBB((3d/16),(5.5d/16),(0d/16),(14d/16),(15d/16),(2.5d/16)),
          SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
          SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE
      ),
      // Rustic button 5 (pull handle)
      new SwitchBlock("pulseswitch_rustic5",
          new AxisAlignedBB((3.5d/16),(6.0d/16),(0d/16), (12.5d/16),(11.0d/16),(4.5d/16)),
          SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
          SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE
      ),
      // Rustic button 6 (handle)
      new SwitchBlock("pulseswitch_rustic6",
          new AxisAlignedBB((6.0d/16),(5.0d/16),(0d/16), (10.0d/16),(12.0d/16),(3.0d/16)),
          SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
          SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE
      ),
      // Rustic button 7 (pull nail)
      new SwitchBlock("pulseswitch_rustic7",
          new AxisAlignedBB((6.0d/16),(7.0d/16),(0d/16), (9.0d/16),(10.0d/16),(3.0d/16)),
          SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
          SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE
      )
  };

  @GameRegistry.ObjectHolder("rsgauges:contactmat1")  public static final ContactSwitchBlock contactSwitch1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:contactmat2")  public static final ContactSwitchBlock contactSwitch2Block = null;
  private static final ContactSwitchBlock contactSwitches[] = {
      // Door contact mat
      new ContactSwitchBlock("contactmat1",
          new AxisAlignedBB((1d/16),(0.0d/16),(0d/16), (15d/16),(0.5d/16),(12d/16)),
          SwitchBlock.SWITCH_CONFIG_FLOOR_MOUNT|SwitchBlock.SWITCH_DATA_WEAK|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
          SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE,
          new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
          new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
      ),
      // Sensitive full size contact mat
      new ContactSwitchBlock("contactmat2",
          new AxisAlignedBB((0d/16),(0.0d/16),(0d/16), (16d/16),(0.5d/16),(16d/16)),
          SwitchBlock.SWITCH_CONFIG_FLOOR_MOUNT|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
          SwitchBlock.SWITCH_CONFIG_INVERTABLE,
          new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
          new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
      )
  };

  @GameRegistry.ObjectHolder("rsgauges:automaticswitch1") public static final AutoSwitchBlock automaticSwitch1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:automaticswitch2") public static final AutoSwitchBlock automaticSwitch2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:automaticswitch3") public static final AutoSwitchBlock automaticSwitch3Block = null;
  @GameRegistry.ObjectHolder("rsgauges:automaticswitch4") public static final AutoSwitchBlock automaticSwitch4Block = null;
  @GameRegistry.ObjectHolder("rsgauges:automaticswitch5") public static final AutoSwitchBlock automaticSwitch5Block = null;
  @GameRegistry.ObjectHolder("rsgauges:automaticswitch6") public static final AutoSwitchBlock automaticSwitch6Block = null;
  @GameRegistry.ObjectHolder("rsgauges:automaticswitch7") public static final AutoSwitchBlock automaticSwitch7Block = null;
  private static final AutoSwitchBlock automaticSwitches[] = {
      // Infrared motion_sensor
      new AutoSwitchBlock("automaticswitch1",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
          SwitchBlock.SWITCH_CONFIG_AUTOMATIC|SwitchBlock.SWITCH_CONFIG_SENSOR_VOLUME|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
          SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE,
          new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
          new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
      ),
      // Linear laser motion sensor
      new AutoSwitchBlock("automaticswitch2",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
          SwitchBlock.SWITCH_CONFIG_AUTOMATIC|SwitchBlock.SWITCH_CONFIG_SENSOR_LINEAR|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
          SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE,
          new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
          new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
      ),
      // Local light sensor
      new AutoSwitchBlock("automaticswitch3",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
          SwitchBlock.SWITCH_CONFIG_AUTOMATIC|SwitchBlock.SWITCH_CONFIG_SENSOR_LIGHT|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
          SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE,
          new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
          new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
      ),
      // Day time switch
      new AutoSwitchBlock("automaticswitch4",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
          SwitchBlock.SWITCH_CONFIG_AUTOMATIC|SwitchBlock.SWITCH_CONFIG_TIMER_DAYTIME|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
          SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE,
          new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
          new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
      ),
      // Rain sensor switch
      new AutoSwitchBlock("automaticswitch5",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
          SwitchBlock.SWITCH_CONFIG_AUTOMATIC|SwitchBlock.SWITCH_CONFIG_SENSOR_RAIN|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
          SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE,
          new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
          new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
      ),
      // Lightning sensor switch
      new AutoSwitchBlock("automaticswitch6",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
          SwitchBlock.SWITCH_CONFIG_AUTOMATIC|SwitchBlock.SWITCH_CONFIG_SENSOR_LIGHTNING|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
          SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE,
          new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
          new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
      ),
      // Interval signal timer
      new AutoSwitchBlock("automaticswitch7",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
          SwitchBlock.SWITCH_CONFIG_AUTOMATIC|SwitchBlock.SWITCH_CONFIG_TIMER_INTERVAL|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
          SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE,
          new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
          new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
      )

  };

  @GameRegistry.ObjectHolder("rsgauges:sensitiveglass")           public static final SensitiveGlassBlock sensitiveGlassBlock = null;
  @GameRegistry.ObjectHolder("rsgauges:sensitiveglass_blue")      public static final SensitiveGlassBlock blueSensitiveGlassBlock = null;
  private static final SensitiveGlassBlock sensitiveGlassBlocks[] = {
      // Black/white stained sensitive glass
      new SensitiveGlassBlock("sensitiveglass", 0x000f|0x0010), // light value if on 0xf | off 0x1
      // Blue stained sensitive glass
      new SensitiveGlassBlock("sensitiveglass_blue", 0x000f|0x0010)
  };

  // Invoked from CommonProxy.registerBlocks()
  public static final void registerBlocks(RegistryEvent.Register<Block> event) {
    if((!ModConfig.without_gauges) || (!ModConfig.without_indicators) || (!ModConfig.without_blinking_indicators) || (!ModConfig.without_sound_indicators)) {
      GameRegistry.registerTileEntity(GaugeBlock.GaugeTileEntity.class, ModRsGauges.MODID + "_gauge_entity");
    }
    if(!ModConfig.without_gauges) { for(GaugeBlock e:gauges) event.getRegistry().register(e); }
    if(!ModConfig.without_indicators) { for(GaugeBlock e:indicators) event.getRegistry().register(e); }
    if(!ModConfig.without_blinking_indicators) { for(GaugeBlock e:blinkIndicators) event.getRegistry().register(e); }
    if(!ModConfig.without_sound_indicators) { for(GaugeBlock e:soundIndicators) event.getRegistry().register(e); }
    if(!ModConfig.without_bistable_switches) { for(SwitchBlock e:bistableSwitches) event.getRegistry().register(e); }
    if(!ModConfig.without_decorative) { for(SensitiveGlassBlock e:sensitiveGlassBlocks) event.getRegistry().register(e); }
    if(!ModConfig.without_pulse_switches) {
      GameRegistry.registerTileEntity(SwitchBlock.SwitchTileEntity.class, ModRsGauges.MODID + "_pulseswitch_entity");
      for(SwitchBlock e:pulseSwitches) event.getRegistry().register(e);
    }
    if(!ModConfig.without_contact_switches) {
      GameRegistry.registerTileEntity(ContactSwitchBlock.ContactSwitchTileEntity.class, ModRsGauges.MODID + "_contactswitch_entity");
      for(SwitchBlock e:contactSwitches) event.getRegistry().register(e);
    }
    if(!ModConfig.without_automatic_switches) {
      GameRegistry.registerTileEntity(AutoSwitchBlock.AutoSwitchTileEntity.class, ModRsGauges.MODID + "_autoswitch_entity");
      GameRegistry.registerTileEntity(AutoSwitchBlock.DetectorSwitchTileEntity.class, ModRsGauges.MODID + "_detectorswitch_entity");
      GameRegistry.registerTileEntity(AutoSwitchBlock.EnvironmentalSensorSwitchTileEntity.class, ModRsGauges.MODID + "_sensorswitch_entity");
      GameRegistry.registerTileEntity(AutoSwitchBlock.IntervalTimerSwitchTileEntity.class, ModRsGauges.MODID + "_timerswitch_entity");
      for(SwitchBlock e:automaticSwitches) event.getRegistry().register(e);
    }
  }

  // Invoked from ClientProxy.registerModels()
  @SideOnly(Side.CLIENT)
  public static final void initModels() {
    if(!ModConfig.without_gauges) { for(GaugeBlock e:gauges) e.initModel(); }
    if(!ModConfig.without_indicators) { for(GaugeBlock e:indicators) e.initModel(); }
    if(!ModConfig.without_blinking_indicators) { for(GaugeBlock e:blinkIndicators) e.initModel(); }
    if(!ModConfig.without_sound_indicators) { for(GaugeBlock e:soundIndicators) e.initModel(); }
    if(!ModConfig.without_bistable_switches) { for(SwitchBlock e:bistableSwitches) e.initModel(); }
    if(!ModConfig.without_pulse_switches) { for(SwitchBlock e:pulseSwitches) e.initModel(); }
    if(!ModConfig.without_contact_switches) { for(ContactSwitchBlock e:contactSwitches) e.initModel(); }
    if(!ModConfig.without_automatic_switches) { for(AutoSwitchBlock e:automaticSwitches) e.initModel(); }
    if(!ModConfig.without_decorative) { for(SensitiveGlassBlock e:sensitiveGlassBlocks) e.initModel(); }
  }

  // Invoked from CommonProxy.registerItems()
  public static final void registerItemBlocks(RegistryEvent.Register<Item> event) {
    if(!ModConfig.without_gauges) { for(GaugeBlock e:gauges) event.getRegistry().register(new ItemBlock(e).setRegistryName(e.getRegistryName())); }
    if(!ModConfig.without_indicators) { for(GaugeBlock e:indicators) event.getRegistry().register(new ItemBlock(e).setRegistryName(e.getRegistryName())); }
    if(!ModConfig.without_blinking_indicators) { for(GaugeBlock e:blinkIndicators) event.getRegistry().register(new ItemBlock(e).setRegistryName(e.getRegistryName())); }
    if(!ModConfig.without_sound_indicators) { for(GaugeBlock e:soundIndicators) event.getRegistry().register(new ItemBlock(e).setRegistryName(e.getRegistryName())); }
    if(!ModConfig.without_bistable_switches) { for(SwitchBlock e:bistableSwitches) event.getRegistry().register(new ItemBlock(e).setRegistryName(e.getRegistryName())); }
    if(!ModConfig.without_pulse_switches) { for(SwitchBlock e:pulseSwitches) event.getRegistry().register(new ItemBlock(e).setRegistryName(e.getRegistryName())); }
    if(!ModConfig.without_contact_switches) { for(ContactSwitchBlock e:contactSwitches) event.getRegistry().register(new ItemBlock(e).setRegistryName(e.getRegistryName())); }
    if(!ModConfig.without_automatic_switches) { for(AutoSwitchBlock e:automaticSwitches) event.getRegistry().register(new ItemBlock(e).setRegistryName(e.getRegistryName())); }
    if(!ModConfig.without_decorative) { for(SensitiveGlassBlock e:sensitiveGlassBlocks) event.getRegistry().register(new ItemBlock(e).setRegistryName(e.getRegistryName())); }
  }
}
