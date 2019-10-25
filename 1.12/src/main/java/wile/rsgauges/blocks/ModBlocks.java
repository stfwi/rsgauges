/*
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
 */
package wile.rsgauges.blocks;

import wile.rsgauges.ModRsGauges;
import wile.rsgauges.detail.ModResources;
import wile.rsgauges.detail.ModAuxiliaries;
import wile.rsgauges.detail.ModConfig;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("unused")
public class ModBlocks
{
  // Gauges
  @GameRegistry.ObjectHolder("rsgauges:flatgauge1")                 public static final BlockGauge flatgauge1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:flatgauge2")                 public static final BlockGauge flatgauge2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:flatgauge3")                 public static final BlockGauge flatgauge3Block = null;
  @GameRegistry.ObjectHolder("rsgauges:flatgauge4")                 public static final BlockGauge flatgauge4Block = null;
  @GameRegistry.ObjectHolder("rsgauges:flatgauge5")                 public static final BlockGauge flatgauge5Block = null;
  @GameRegistry.ObjectHolder("rsgauges:flatgauge6")                 public static final BlockGauge flatgauge6Block = null;
  @GameRegistry.ObjectHolder("rsgauges:gauge_rustic2")              public static final BlockGauge gaugeRustic2Block = null;
  // Indicators (steady)
  @GameRegistry.ObjectHolder("rsgauges:indicator1")                 public static final BlockGauge indicator1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:indicator2")                 public static final BlockGauge indicator2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:indicator3")                 public static final BlockGauge indicator3Block = null;
  // Blinking indicators
  @GameRegistry.ObjectHolder("rsgauges:indicator_led_white")        public static final BlockGauge indicatorLedWhiteBlock = null;
  @GameRegistry.ObjectHolder("rsgauges:indicator_led_white_blink")  public static final BlockGauge indicatorLedWhiteBlinkBlock = null;
  @GameRegistry.ObjectHolder("rsgauges:indicator1blink1")           public static final BlockGauge indicator1Blink1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:indicator2blink1")           public static final BlockGauge indicator2Blink1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:indicator3blink1")           public static final BlockGauge indicator3Blink1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:indicator4")                 public static final BlockGauge indicator4Block = null;
  @GameRegistry.ObjectHolder("rsgauges:indicator_rustic_flag")      public static final BlockGauge indicatorRusticFlag = null;
  // Sound indicators
  @GameRegistry.ObjectHolder("rsgauges:soundindicator1")            public static final BlockGauge soundIndicator1Block = null;
  // Bi-stable switches
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch1")            public static final BlockSwitch bistableSwitch1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch2")            public static final BlockSwitch bistableSwitch2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch3")            public static final BlockSwitch bistableSwitch3Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch4")            public static final BlockSwitch bistableSwitch4Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch5")            public static final BlockSwitch bistableSwitch5Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch6")            public static final BlockSwitch bistableSwitch6Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch7")            public static final BlockSwitch bistableSwitch7Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch8")            public static final BlockSwitch bistableSwitch8Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_oldfancy1")   public static final BlockSwitch bistableSwitchOldFancy1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_oldfancy2")   public static final BlockSwitch bistableSwitchOldFancy2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_oldfancy3")   public static final BlockSwitch bistableSwitchOldFancy3Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_oldfancy4")   public static final BlockSwitch bistableSwitchOldFancy4Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_oldfancy5")   public static final BlockSwitch bistableSwitchOldFancy5Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_oldfancy6")   public static final BlockSwitch bistableSwitchOldFancy6Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_oldfancy7")   public static final BlockSwitch bistableSwitchOldFancy7Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_rustic1")     public static final BlockSwitch bistableSwitchRustic1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_rustic2")     public static final BlockSwitch bistableSwitchRustic2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_rustic3")     public static final BlockSwitch bistableSwitchRustic3Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_rustic4")     public static final BlockSwitch bistableSwitchRustic4Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_rustic5")     public static final BlockSwitch bistableSwitchRustic5Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_rustic6")     public static final BlockSwitch bistableSwitchRustic6Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_rustic7")     public static final BlockSwitch bistableSwitchRustic7Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_glass1")      public static final BlockSwitch bistableSwitchGlass1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch_glass2")      public static final BlockSwitch bistableSwitchGlass2Block = null;
  // Pulse switches
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch1")               public static final BlockSwitch pulseSwitch1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch2")               public static final BlockSwitch pulseSwitch2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch3")               public static final BlockSwitch pulseSwitch3Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch5")               public static final BlockSwitch pulseSwitch5Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch6")               public static final BlockSwitch pulseSwitch6Block = null;
  @GameRegistry.ObjectHolder("rsgauges:arrowtarget")                public static final BlockSwitch pulseSwitchArrowTargetBlock = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch_oldfancy1")      public static final BlockSwitch pulseSwitchOldFancy1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch_oldfancy2")      public static final BlockSwitch pulseSwitchOldFancy2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch_oldfancy3")      public static final BlockSwitch pulseSwitchOldFancy3Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch_oldfancy4")      public static final BlockSwitch pulseSwitchOldFancy4Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch_rustic1")        public static final BlockSwitch pulseSwitchRustic1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch_rustic2")        public static final BlockSwitch pulseSwitchRustic2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch_rustic3")        public static final BlockSwitch pulseSwitchRustic3Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch_rustic4")        public static final BlockSwitch pulseSwitchRustic4Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch_rustic5")        public static final BlockSwitch pulseSwitchRustic5Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch_rustic6")        public static final BlockSwitch pulseSwitchRustic6Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch_rustic7")        public static final BlockSwitch pulseSwitchRustic7Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch_glass1")         public static final BlockSwitch pulseSwitchGlass1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch_glass2")         public static final BlockSwitch pulseSwitchGlass2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch_glass3")         public static final BlockSwitch pulseSwitchGlass3Block = null;
  // Contact switches
  @GameRegistry.ObjectHolder("rsgauges:contactmat1")                public static final BlockContactSwitch contactSwitch1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:contactmat2")                public static final BlockContactSwitch contactSwitch2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:contactmat_rustic1")         public static final BlockContactSwitch contactSwitchRustic1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:contactmat_glass1")          public static final BlockContactSwitch contactSwitchGlass1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:contactmat_glass2")          public static final BlockContactSwitch contactSwitchGlass2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:contactmat3")                public static final BlockContactSwitch contactSwitch3Block = null;
  @GameRegistry.ObjectHolder("rsgauges:contactmat_rustic2")         public static final BlockContactSwitch contactSwitchRustic2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:contactmat_rustic3")         public static final BlockContactSwitch contactSwitchRustic3Block = null;
  @GameRegistry.ObjectHolder("rsgauges:trapdoorswitch1")            public static final BlockContactSwitch trapdoorSwitch1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:trapdoorswitch_rustic1")     public static final BlockContactSwitch trapdoorSwitchRustic1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:trapdoorswitch_rustic2")     public static final BlockContactSwitch trapdoorSwitchRustic2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:trapdoorswitch_rustic3")     public static final BlockContactSwitch trapdoorSwitchRustic3Block = null;
  @GameRegistry.ObjectHolder("rsgauges:powerplant_yellow")          public static final BlockContactSwitch powerPlantYellowBlock = null;
  @GameRegistry.ObjectHolder("rsgauges:powerplant_red")             public static final BlockContactSwitch powerPlantRedBlock = null;
  // Auto switches
  @GameRegistry.ObjectHolder("rsgauges:automaticswitch1")           public static final BlockAutoSwitch automaticSwitch1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:automaticswitch2")           public static final BlockAutoSwitch automaticSwitch2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:automaticswitch3")           public static final BlockAutoSwitch automaticSwitch3Block = null;
  @GameRegistry.ObjectHolder("rsgauges:automaticswitch4")           public static final BlockAutoSwitch automaticSwitch4Block = null;
  @GameRegistry.ObjectHolder("rsgauges:automaticswitch5")           public static final BlockAutoSwitch automaticSwitch5Block = null;
  @GameRegistry.ObjectHolder("rsgauges:automaticswitch6")           public static final BlockAutoSwitch automaticSwitch6Block = null;
  @GameRegistry.ObjectHolder("rsgauges:automaticswitch7")           public static final BlockAutoSwitch automaticSwitch7Block = null;
  @GameRegistry.ObjectHolder("rsgauges:detectorswitch_glass1")      public static final BlockAutoSwitch detectorSwitchGlass1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:detectorswitch_glass2")      public static final BlockAutoSwitch detectorSwitchGlass2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:daytimeswitch_glass1")       public static final BlockAutoSwitch daytimeSwitchGlass1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:timerswitch_glass1")         public static final BlockAutoSwitch timerSwitchGlass1Block = null;
  // Link switches
  @GameRegistry.ObjectHolder("rsgauges:relay_pulseswitchrx1")       public static final BlockSwitch relayPulseRxSwitch1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:relay_bistableswitchrx1")    public static final BlockSwitch relayBistableRxSwitch1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:relay_pulseswitchtx1")       public static final BlockSwitch relayPulseRelaySwitch1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:relay_bistableswitchtx1")    public static final BlockSwitch relayBistableRelaySwitch1Block = null;
  // Block inspction / observing switchs
  @GameRegistry.ObjectHolder("rsgauges:observerswitch1")            public static final BlockSwitch observerSwitch1Block = null;
  // Dimmer switches
  @GameRegistry.ObjectHolder("rsgauges:dimmerswitch1")              public static final BlockSwitch dimmerSwitch1Block = null;
  // Sensitive glass
  @GameRegistry.ObjectHolder("rsgauges:sensitiveglass")             public static final BlockSensitiveGlass sensitiveGlassBlock = null;
  @GameRegistry.ObjectHolder("rsgauges:sensitiveglass_white")       public static final BlockSensitiveGlass whiteSensitiveGlassBlock = null;
  @GameRegistry.ObjectHolder("rsgauges:sensitiveglass_red")         public static final BlockSensitiveGlass redSensitiveGlassBlock = null;
  @GameRegistry.ObjectHolder("rsgauges:sensitiveglass_green")       public static final BlockSensitiveGlass greenSensitiveGlassBlock = null;
  @GameRegistry.ObjectHolder("rsgauges:sensitiveglass_blue")        public static final BlockSensitiveGlass blueSensitiveGlassBlock = null;
  @GameRegistry.ObjectHolder("rsgauges:sensitiveglass_yellow")      public static final BlockSensitiveGlass yellowSensitiveGlassBlock = null;
  @GameRegistry.ObjectHolder("rsgauges:sensitiveglass_orange")      public static final BlockSensitiveGlass orangeSensitiveGlassBlock = null;
  @GameRegistry.ObjectHolder("rsgauges:sensitiveglass_magenta")     public static final BlockSensitiveGlass magentaSensitiveGlassBlock = null;
  @GameRegistry.ObjectHolder("rsgauges:sensitiveglass_lightblue")   public static final BlockSensitiveGlass lightblueSensitiveGlassBlock = null;
  @GameRegistry.ObjectHolder("rsgauges:sensitiveglass_lime")        public static final BlockSensitiveGlass limeSensitiveGlassBlock = null;
  @GameRegistry.ObjectHolder("rsgauges:sensitiveglass_pink")        public static final BlockSensitiveGlass pinkSensitiveGlassBlock = null;
  @GameRegistry.ObjectHolder("rsgauges:sensitiveglass_gray")        public static final BlockSensitiveGlass graySensitiveGlassBlock = null;
  @GameRegistry.ObjectHolder("rsgauges:sensitiveglass_lightgray")   public static final BlockSensitiveGlass lightgraySensitiveGlassBlock = null;
  @GameRegistry.ObjectHolder("rsgauges:sensitiveglass_cyan")        public static final BlockSensitiveGlass cyanSensitiveGlassBlock = null;
  @GameRegistry.ObjectHolder("rsgauges:sensitiveglass_purple")      public static final BlockSensitiveGlass purpleSensitiveGlassBlock = null;
  @GameRegistry.ObjectHolder("rsgauges:sensitiveglass_brown")       public static final BlockSensitiveGlass brownSensitiveGlassBlock = null;
  @GameRegistry.ObjectHolder("rsgauges:sensitiveglass_black")       public static final BlockSensitiveGlass blackSensitiveGlassBlock = null;
  @GameRegistry.ObjectHolder("rsgauges:sensitiveglass_inverted")    public static final BlockSensitiveGlass invertedSensitiveGlassBlock = null;

  private static final Block modBlocks[] = {

    // -----------------------------------------------------------------------------------------------------------------
    // -- gauges
    // -----------------------------------------------------------------------------------------------------------------
    new BlockGauge("flatgauge1",
      new AxisAlignedBB((2d/16),(2d/16),(0d/16), (14d/16),(14d/16),(1d/16)),
      BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT
    ),
    new BlockGauge("flatgauge2",
      new AxisAlignedBB((4d/16),(2d/16),(0d/16), (12d/16),(14d/16),(1d/16)),
      BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT
    ),
    new BlockGauge("flatgauge3",
      new AxisAlignedBB((4d/16),(5d/16),(0d/16), (12d/16),(11d/16),(1d/16)),
      BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT
    ),
    new BlockGauge("flatgauge4",
      new AxisAlignedBB((7d/16),(3.7d/16),(0d/16), (10d/16),(12d/16),(0.4d/16)),
      BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT
    ),
    new BlockGauge("flatgauge5",
      new AxisAlignedBB((7d/16),(4d/16),(0d/16), (9d/16),(12d/16),(3d/16)),
      BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT
    ),
    new BlockGauge("flatgauge6",
      new AxisAlignedBB((2d/16),(4d/16),(0d/16), (14d/16),(12d/16),(1d/16)),
      BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT
    ),
    new BlockGauge("gauge_rustic2",
      new AxisAlignedBB((2d/16),(2d/16),(0d/16), (14d/16),(14d/16),(1d/16)),
      0 // no color tint.
    ),

    // -----------------------------------------------------------------------------------------------------------------
    // -- indicators
    // -----------------------------------------------------------------------------------------------------------------

    // square LED
    new BlockIndicator("indicator1",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16)),
      (2<<BlockGauge.GAUGE_DATA_LIGHT_SHIFT)|(13<<BlockGauge.GAUGE_DATA_COLOR_SHIFT) // green
    ),
    new BlockIndicator("indicator2",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16)),
      (2<<BlockGauge.GAUGE_DATA_LIGHT_SHIFT)|(5<<BlockGauge.GAUGE_DATA_COLOR_SHIFT) // yellow
    ),
    new BlockIndicator("indicator3",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16)),
      (2<<BlockGauge.GAUGE_DATA_LIGHT_SHIFT) // red
    ),
    new BlockIndicator("indicator_led_white",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16)),
      (2<<BlockGauge.GAUGE_DATA_LIGHT_SHIFT)|BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT // default white
    ),
    // Blinking square LEDs
    new BlockIndicator("indicator1blink1",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16)),
      (2<<BlockGauge.GAUGE_DATA_LIGHT_SHIFT)
    ),
    new BlockIndicator("indicator2blink1",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16)),
      (2<<BlockGauge.GAUGE_DATA_LIGHT_SHIFT)
    ),
    new BlockIndicator("indicator3blink1",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16)),
      (2<<BlockGauge.GAUGE_DATA_LIGHT_SHIFT)
    ),
    new BlockIndicator("indicator_led_white_blink",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16)),
      (2<<BlockGauge.GAUGE_DATA_LIGHT_SHIFT)|BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT // default white
    ),
    // alarm lamp
    new BlockIndicator("indicator4",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(4d/16)),
      (8<<BlockGauge.GAUGE_DATA_LIGHT_SHIFT)
    ),
    // Alarm siren
    new BlockIndicator("soundindicator1",
      new AxisAlignedBB((4d/16),(6.5d/16),(0d/16), (11.5d/16),(9.5d/16),(4d/16)),
      (1<<BlockGauge.GAUGE_DATA_LIGHT_SHIFT) | (8<<BlockGauge.GAUGE_DATA_BLINK_SHIFT),
      new ModResources.BlockSoundEvent(ModResources.ALARM_SIREN_SOUND),
      null
    ),
    new BlockIndicator("indicator_rustic_flag",
      new AxisAlignedBB((5d/16),(8d/16),(0d/16), (12d/16),(10d/16),(0.5d/16)),
      BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT // default white
    ),

    // -----------------------------------------------------------------------------------------------------------------
    // -- bi-stable switches
    // -----------------------------------------------------------------------------------------------------------------

    // Rotary machine switch
    new BlockSwitch("bistableswitch1",
      new AxisAlignedBB((4.25d/16),(4.25d/16),(0d/16),(11.75d/16),(11.75d/16),(0.80d/16)), null,
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Contact lever switch
    new BlockSwitch("bistableswitch2",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(14d/16),(1.5d/16)),
      new AxisAlignedBB((6d/16),(2d/16),(0d/16),(10d/16),(10d/16),(1.5d/16)),
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // two-button machine switch
    new BlockSwitch("bistableswitch3",
      new AxisAlignedBB((4d/16),(4d/16),(0d/16),(12d/16),(12d/16),(1d/16)), null,
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Light flip switch
    new BlockSwitch("bistableswitch4",
      new AxisAlignedBB((7d/16),(6d/16),(0d/16),(9d/16),(10d/16),(1.5d/16)), null,
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // ESTOP button
    new BlockSwitch("bistableswitch5",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1.5d/16)),
      new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(2.5d/16)),
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
      BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE_OFF,
      null, null
    ),
    // Hopper blocking switch
    new BlockSwitch("bistableswitch6",
      new AxisAlignedBB((4.8d/16),(10d/16),(0d/16), (11.2d/16),(11d/16),(6d/16)),
      new AxisAlignedBB((4.8d/16),(10d/16),(0d/16), (11.2d/16),(11d/16),(3d/16)),
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
      BlockSwitch.SWITCH_CONFIG_HOPPER_MOUNTBALE|BlockSwitch.SWITCH_DATA_WEAK,
      null, null
    ),
    // mechanical rotary lever
    new BlockSwitch("bistableswitch7",
      new AxisAlignedBB((2.5d/16),(9.5d/16),(0d/16), (12.3d/16),(13d/16),(2.7d/16)),
      new AxisAlignedBB((8.7d/16),( 3d/16),(0d/16), (12.3d/16),(13d/16),(2.7d/16)),
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
      BlockSwitch.SWITCH_CONFIG_HOPPER_MOUNTBALE,
      null, null
    ),
    // mechanical lever
    new BlockSwitch("bistableswitch8",
      new AxisAlignedBB((6.5d/16),(6.5d/16),(0d/16), (9.7d/16),(14.0d/16),(3d/16)),
      new AxisAlignedBB((6.5d/16),(5.0d/16),(0d/16), (9.7d/16),(12.5d/16),(3d/16)),
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Old fancy gold decorated lever
    new BlockSwitch("bistableswitch_oldfancy1",
      new AxisAlignedBB((6d/16),(6.5d/16),(0d/16), (10.3d/16),(13.5d/16),(4.5d/16)),
      new AxisAlignedBB((6d/16),(3.5d/16),(0d/16), (10.3d/16),(10.0d/16),(4.5d/16)),
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Old fancy angular lever
    new BlockSwitch("bistableswitch_oldfancy2",
      new AxisAlignedBB((2.5d/16),(6.0d/16),(0d/16), (9.7d/16),(10.0d/16),(4.5d/16)),
      new AxisAlignedBB((4.5d/16),(3.5d/16),(0d/16), (9.2d/16),(10.0d/16),(4.5d/16)),
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
      BlockSwitch.SWITCH_CONFIG_HOPPER_MOUNTBALE,
      null, null
    ),
    // Old fancy bent lever
    new BlockSwitch("bistableswitch_oldfancy3",
      new AxisAlignedBB((4.5d/16),(5.0d/16),(0d/16), (12.0d/16),(11.0d/16),(4.5d/16)),
      new AxisAlignedBB((4.5d/16),(5.0d/16),(0d/16), (12.0d/16),(11.0d/16),(4.5d/16)),
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Old fancy gold decorated manual slide sphere connector
    new BlockSwitch("bistableswitch_oldfancy4",
      new AxisAlignedBB((5.0d/16),(6.0d/16),(0d/16), (13.0d/16),(13.0d/16),(4.5d/16)), null,
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Old fancy gold decorated rotary sphere connector
    new BlockSwitch("bistableswitch_oldfancy5",
      new AxisAlignedBB((5.0d/16),(5.0d/16),(0d/16), (13.0d/16),(13.0d/16),(4.5d/16)),
      new AxisAlignedBB((5.0d/16),(6.0d/16),(0d/16), (13.0d/16),(13.0d/16),(4.5d/16)),
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Old fancy gold decorated rotary sphere connector 2
    new BlockSwitch("bistableswitch_oldfancy6",
      new AxisAlignedBB((5.0d/16),(5.0d/16),(0d/16), (13.0d/16),(13.0d/16),(4.5d/16)), null,
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Old fancy gold decorated manual contactor
    new BlockSwitch("bistableswitch_oldfancy7",
      new AxisAlignedBB((2.0d/16),(6.0d/16),(0d/16), (11.7d/16),(14.0d/16),(4.5d/16)),
      new AxisAlignedBB((3.5d/16),(3.5d/16),(0d/16), (11.2d/16),(14.0d/16),(4.5d/16)),
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Rustic lever 1
    new BlockSwitch("bistableswitch_rustic1",
      new AxisAlignedBB((6d/16),(5.0d/16),(0d/16), (10.3d/16),(15.0d/16),(4.5d/16)),
      new AxisAlignedBB((6d/16),(2.0d/16),(0d/16), (10.3d/16),(11.0d/16),(4.5d/16)),
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Rustic lever 2 (bolted)
    new BlockSwitch("bistableswitch_rustic2",
      new AxisAlignedBB((4d/16),(6.0d/16),(0d/16), (13.0d/16),(13.0d/16),(4.5d/16)),
      new AxisAlignedBB((4d/16),(4.0d/16),(0d/16), (13.0d/16),(10.0d/16),(4.5d/16)),
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Rustic lever 3 (big angular)
    new BlockSwitch("bistableswitch_rustic3",
      new AxisAlignedBB((0.2d/16),(10.0d/16),(0d/16), (13.5d/16),(15.0d/16),(4.5d/16)),
      new AxisAlignedBB((5.5d/16),( 2.0d/16),(0d/16), (13.5d/16),(15.0d/16),(4.5d/16)),
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
      BlockSwitch.SWITCH_CONFIG_HOPPER_MOUNTBALE,
      null, null
    ),
    // Rustic lever 4 (counter weighted rotary lever)
    new BlockSwitch("bistableswitch_rustic4",
      new AxisAlignedBB((0.5d/16),(11.0d/16),(0d/16), (14.0d/16),(15.0d/16),(4.5d/16)), null,
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
      BlockSwitch.SWITCH_CONFIG_HOPPER_MOUNTBALE,
      null, null
    ),
    // Rustic lever 5
    new BlockSwitch("bistableswitch_rustic5",
      new AxisAlignedBB((3.5d/16),(6.0d/16),(0d/16), (12.5d/16),(11.0d/16),(4.5d/16)), null,
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Rustic lever 6 (latch slide)
    new BlockSwitch("bistableswitch_rustic6",
      new AxisAlignedBB((3d/16),(6.0d/16),(0d/16), (13.0d/16),(12.0d/16),(4.5d/16)), null,
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Rustic lever 7 (The Nail)
    new BlockSwitch("bistableswitch_rustic7",
      new AxisAlignedBB((6.0d/16),(7.0d/16),(0d/16), (9.0d/16),(10.0d/16),(3.0d/16)), null,
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
      BlockSwitch.SWITCH_CONFIG_HOPPER_MOUNTBALE,
      null, null
    ),
    // Thin star shaped glass switch
    new BlockSwitch("bistableswitch_glass1",
      new AxisAlignedBB((5.5d/16),(5.5d/16),(0d/16),(10.5d/16),(10.5d/16),(0.5d/16)), null,
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
      BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|BlockSwitch.SWITCH_CONFIG_FAINT_LIGHTSOURCE|
      BlockSwitch.SWITCH_CONFIG_COLOR_TINT_SUPPORT,
      null, null
    ),
    // Bistable glass touch switch
    new BlockSwitch("bistableswitch_glass2",
      new AxisAlignedBB((5.5d/16),(5.5d/16),(0d/16),(10.5d/16),(10.5d/16),(0.5d/16)), null,
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
      BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|BlockSwitch.SWITCH_CONFIG_FAINT_LIGHTSOURCE|
      BlockSwitch.SWITCH_CONFIG_COLOR_TINT_SUPPORT,
      null, null
    ),

    // -----------------------------------------------------------------------------------------------------------------
    // -- pulse switches
    // -----------------------------------------------------------------------------------------------------------------

    // Square machine pulse switch
    new BlockSwitch("pulseswitch1",
      new AxisAlignedBB((5d/16),(5d/16),(0d/16),(11d/16),(11d/16),(1d/16)), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Fenced round machine pulse switch
    new BlockSwitch("pulseswitch2",
      new AxisAlignedBB((5.5d/16),(5.5d/16),(0d/16),(10.5d/16),(10.5d/16),(0.5d/16)), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Mechanical spring reset pull handle
    new BlockSwitch("pulseswitch3",
      new AxisAlignedBB((5.5d/16),(5.5d/16),(0d/16),(10.5d/16),(10.5d/16),(2d/16)), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Arrow target
    new BlockSwitch("arrowtarget",
      new AxisAlignedBB((5d/16),(5d/16),(0d/16),(11d/16),(11d/16),(1d/16)), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
      BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Mechanical spring reset push button
    new BlockSwitch("pulseswitch5",
      new AxisAlignedBB((5.5d/16),(0.5d/16),(0d/16),(10.5d/16),(5.0d/16),(4d/16)), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Retro double pole switch
    new BlockSwitch("pulseswitch6",
      new AxisAlignedBB((7d/16),(6d/16),(0d/16),(12d/16),(10d/16),(4d/16)), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Old fancy (golden decorated) button
    new BlockSwitch("pulseswitch_oldfancy1",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1.5d/16)), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Old fancy (golden decorated) chain pulse switch
    new BlockSwitch("pulseswitch_oldfancy2",
      new AxisAlignedBB((6.5d/16),(4.8d/16),(0d/16),(9.5d/16),(13d/16),(4d/16)),
      new AxisAlignedBB((6.5d/16),(3.8d/16),(0d/16),(9.5d/16),(12d/16),(4d/16)),
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Old fancy (golden decorated) crank
    new BlockSwitch("pulseswitch_oldfancy3",
      new AxisAlignedBB((6.5d/16),(4.8d/16),(0d/16),(9.5d/16),(13d/16),(4d/16)),
      new AxisAlignedBB((6.5d/16),(3.8d/16),(0d/16),(9.5d/16),(12d/16),(4d/16)),
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Old fancy (golden decorated) tiny button
    new BlockSwitch("pulseswitch_oldfancy4",
      new AxisAlignedBB((7d/16),(7d/16),(0d/16),(9d/16),(9d/16),(1.5d/16)), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Rustic button 1
    new BlockSwitch("pulseswitch_rustic1",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(2.5d/16)), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Rustic button 2 (bolted)
    new BlockSwitch("pulseswitch_rustic2",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(2.5d/16)), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Rustic button 3 (pull chain)
    new BlockSwitch("pulseswitch_rustic3",
      new AxisAlignedBB((6d/16),(6.5d/16),(0d/16),(10d/16),(14.5d/16),(2.5d/16)), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Rustic button 4 (weight reset pull chain)
    new BlockSwitch("pulseswitch_rustic4",
      new AxisAlignedBB((3d/16),(5.5d/16),(0d/16),(14d/16),(15d/16),(2.5d/16)), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Rustic button 5 (pull handle)
    new BlockSwitch("pulseswitch_rustic5",
      new AxisAlignedBB((3.5d/16),(6.0d/16),(0d/16), (12.5d/16),(11.0d/16),(4.5d/16)), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Rustic button 6 (handle)
    new BlockSwitch("pulseswitch_rustic6",
      new AxisAlignedBB((6.0d/16),(5.0d/16),(0d/16), (10.0d/16),(12.0d/16),(3.0d/16)), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Rustic button 7 (pull nail)
    new BlockSwitch("pulseswitch_rustic7",
      new AxisAlignedBB((6.0d/16),(7.0d/16),(0d/16), (9.0d/16),(10.0d/16),(3.0d/16)), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Thin star shaped glass button
    new BlockSwitch("pulseswitch_glass1",
      new AxisAlignedBB((5.5d/16),(5.5d/16),(0d/16),(10.5d/16),(10.5d/16),(0.5d/16)), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
      BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|BlockSwitch.SWITCH_CONFIG_FAINT_LIGHTSOURCE|
      BlockSwitch.SWITCH_CONFIG_COLOR_TINT_SUPPORT|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Thin small star shaped glass button
    new BlockSwitch("pulseswitch_glass2",
      new AxisAlignedBB((5.5d/16),(5.5d/16),(0d/16),(10.5d/16),(10.5d/16),(0.5d/16)), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
      BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|BlockSwitch.SWITCH_CONFIG_FAINT_LIGHTSOURCE|
      BlockSwitch.SWITCH_CONFIG_COLOR_TINT_SUPPORT|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Glass touch button
    new BlockSwitch("pulseswitch_glass3",
      new AxisAlignedBB((5.5d/16),(5.5d/16),(0d/16),(10.5d/16),(10.5d/16),(0.5d/16)), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
      BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|BlockSwitch.SWITCH_CONFIG_FAINT_LIGHTSOURCE|
      BlockSwitch.SWITCH_CONFIG_COLOR_TINT_SUPPORT|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),

    // -----------------------------------------------------------------------------------------------------------------
    // -- contact switches
    // -----------------------------------------------------------------------------------------------------------------

    // Door contact mat
    new BlockContactSwitch("contactmat1",
      new AxisAlignedBB((1d/16),(0.0d/16),(0d/16), (15d/16),(0.5d/16),(13d/16)), null,
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_DATA_WEAK|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
    ),
    // Sensitive full size contact mat
    new BlockContactSwitch("contactmat2",
      new AxisAlignedBB((0d/16),(0.0d/16),(0d/16), (16d/16),(0.5d/16),(16d/16)), null,
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
    ),
    // Rustic door contact mat
    new BlockContactSwitch("contactmat_rustic1",
      new AxisAlignedBB((1d/16),(0.0d/16),(0d/16), (15d/16),(0.5d/16),(12.5d/16)),
      new AxisAlignedBB((1d/16),(0.0d/16),(0d/16), (15d/16),(0.2d/16),(12.5d/16)),
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_DATA_WEAK|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.5f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.0f)
    ),
    // Glass plate
    new BlockContactSwitch("contactmat_glass1",
      new AxisAlignedBB((0d/16),(0.0d/16),(0d/16), (16d/16),(0.25d/16),(16d/16)),null,
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|
      BlockSwitch.SWITCH_CONFIG_COLOR_TINT_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.2f)
    ),
    // Glass plate
    new BlockContactSwitch("contactmat_glass2",
      new AxisAlignedBB((0d/16),(0.0d/16),(0d/16), (16d/16),(0.25d/16),(16d/16)), null,
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|
      BlockSwitch.SWITCH_CONFIG_COLOR_TINT_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.2f)
    ),
    // Industrial shock sensor contact mat
    new BlockContactSwitch("contactmat3",
      new AxisAlignedBB((0d/16),(0.0d/16),(0d/16), (16d/16),(0.5d/16),(16d/16)), null,
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_SHOCK_SENSITIVE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
    ),
    // Rustic full-size contact plate
    new BlockContactSwitch("contactmat_rustic2",
      new AxisAlignedBB((0d/16),(0.0d/16),(0d/16), (16d/16),(0.5d/16),(16d/16)),
      new AxisAlignedBB((0d/16),(0.0d/16),(0d/16), (16d/16),(0.2d/16),(16d/16)),
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.5f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.0f)
    ),
    // Rustic shock sensor plate
    new BlockContactSwitch("contactmat_rustic3",
      new AxisAlignedBB((0d/16),(0.0d/16),(0d/16), (16d/16),(0.5d/16),(16d/16)),
      new AxisAlignedBB((0d/16),(0.0d/16),(0d/16), (16d/16),(0.2d/16),(16d/16)),
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_SHOCK_SENSITIVE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.5f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.0f)
    ),

    // Industrial trap door switch (shock vibration sensitive)
    new BlockTrapdoorSwitch("trapdoorswitch1",
      new AxisAlignedBB((0d/16),(15.6d/16),(0d/16), (16d/16),(16d/16),(16.0d/16)),
      new AxisAlignedBB((0d/16),( 2.0d/16),(0d/16), (16d/16),(16d/16),( 0.1d/16)),
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_NOT_PASSABLE|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_SHOCK_SENSITIVE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 3.0f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.5f)
    ),
    // Industrial trap door switch (high sensitive shock vibration sensitive)
    new BlockTrapdoorSwitch("trapdoorswitch2",
      new AxisAlignedBB((0d/16),(15.6d/16),(0d/16), (16d/16),(16d/16),(16.0d/16)),
      new AxisAlignedBB((0d/16),( 2.0d/16),(0d/16), (16d/16),(16d/16),( 0.1d/16)),
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_NOT_PASSABLE|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_SHOCK_SENSITIVE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
      BlockSwitch.SWITCH_CONFIG_HIGH_SENSITIVE,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.5f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.0f),
      ModAuxiliaries.RsMaterials.MATERIAL_TRAPDOORSWITCH
    ),
    // Industrial trap door switch (item trap door)
    new BlockTrapdoorSwitch("trapdoorswitch3",
      new AxisAlignedBB((0d/16),(12.6d/16),(0d/16), (16d/16),(13d/16),(16.0d/16)),
      new AxisAlignedBB((0d/16),(12.6d/16),(0d/16), (16d/16),(13d/16),(16.0d/16)),
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
      BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.05f, 2.5f),
      null
    ),
    // Rustic trap door switch (shock vibration sensitive)
    new BlockTrapdoorSwitch("trapdoorswitch_rustic1",
      new AxisAlignedBB((0d/16),(15.6d/16),(0d/16), (16d/16),(16d/16),(16.0d/16)),
      new AxisAlignedBB((0d/16),( 2.0d/16),(0d/16), (16d/16),(16d/16),( 0.1d/16)),
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_NOT_PASSABLE|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_SHOCK_SENSITIVE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.5f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.0f)
    ),
    // Rustic trap door switch (high sensitive shock vibration sensitive)
    new BlockTrapdoorSwitch("trapdoorswitch_rustic2",
      new AxisAlignedBB((0d/16),(15.6d/16),(0d/16), (16d/16),(16d/16),(16.0d/16)),
      new AxisAlignedBB((0d/16),( 2.0d/16),(0d/16), (16d/16),(16d/16),( 0.1d/16)),
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_NOT_PASSABLE|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_SHOCK_SENSITIVE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_HIGH_SENSITIVE|
      BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.5f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.0f),
      ModAuxiliaries.RsMaterials.MATERIAL_TRAPDOORSWITCH
    ),
    // Rustic trap door switch (item trap door)
    new BlockTrapdoorSwitch("trapdoorswitch_rustic3",
      new AxisAlignedBB((0d/16),(12.6d/16),(0d/16), (16d/16),(13d/16),(16.0d/16)),
      new AxisAlignedBB((0d/16),(12.6d/16),(0d/16), (16d/16),(13d/16),(16.0d/16)),
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
      BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.5f),
      null // new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.0f)
    ),
    // Yellow power plant
    new BlockContactSwitch("powerplant_yellow",
      new AxisAlignedBB((5d/16),(0.0d/16),(5d/16), (11d/16),(9d/16),(11d/16)), null,
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_GRASS_BREAK, 0.09f, 3.6f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_GRASS_BREAK, 0.04f, 3.0f),
      ModAuxiliaries.RsMaterials.MATERIAL_PLANT
    ),
    // Red power plant
    new BlockContactSwitch("powerplant_red",
      new AxisAlignedBB((5d/16),(0.0d/16),(5d/16), (11d/16),(9d/16),(11d/16)), null,
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_GRASS_BREAK, 0.09f, 3.6f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_GRASS_BREAK, 0.04f, 3.0f),
      ModAuxiliaries.RsMaterials.MATERIAL_PLANT
    ),

    // -----------------------------------------------------------------------------------------------------------------
    // -- auto switches
    // -----------------------------------------------------------------------------------------------------------------

    // Infrared motion_sensor
    new BlockAutoSwitch("automaticswitch1",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
      BlockSwitch.SWITCH_CONFIG_SENSOR_VOLUME|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
    ),
    // Linear laser motion sensor
    new BlockAutoSwitch("automaticswitch2",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
      BlockSwitch.SWITCH_CONFIG_SENSOR_LINEAR|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
    ),
    // Local light sensor
    new BlockAutoSwitch("automaticswitch3",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
      BlockSwitch.SWITCH_CONFIG_SENSOR_LIGHT|
      BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
    ),
    // Day time switch
    new BlockAutoSwitch("automaticswitch4",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
      BlockSwitch.SWITCH_CONFIG_TIMER_DAYTIME|
      BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
    ),
    // Rain sensor switch
    new BlockAutoSwitch("automaticswitch5",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
      BlockSwitch.SWITCH_CONFIG_SENSOR_RAIN|
      BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
    ),
    // Lightning sensor switch
    new BlockAutoSwitch("automaticswitch6",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
      BlockSwitch.SWITCH_CONFIG_SENSOR_LIGHTNING|
      BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
    ),
    // Interval signal timer
    new BlockAutoSwitch("automaticswitch7",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
      BlockSwitch.SWITCH_CONFIG_TIMER_INTERVAL|
      BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
    ),
    // Glass infrared motion sensor
    new BlockAutoSwitch("detectorswitch_glass1",
      new AxisAlignedBB((5.5d/16),(5.5d/16),(0d/16),(10.5d/16),(10.5d/16),(0.1d/16)),
      BlockSwitch.SWITCH_CONFIG_SENSOR_VOLUME|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.2f)
    ),
    // Glass laser motion sensor
    new BlockAutoSwitch("detectorswitch_glass2",
      new AxisAlignedBB((5.5d/16),(5.5d/16),(0d/16),(10.5d/16),(10.5d/16),(0.1d/16)),
      BlockSwitch.SWITCH_CONFIG_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_SENSOR_LINEAR|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.2f)
    ),
    // Glass Day time switch
    new BlockAutoSwitch("daytimeswitch_glass1",
      new AxisAlignedBB((5.5d/16),(5.5d/16),(0d/16),(10.5d/16),(10.5d/16),(0.1d/16)),
      BlockSwitch.SWITCH_CONFIG_TIMER_DAYTIME|
      BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.2f)
    ),
    // Glass interval signal timer
    new BlockAutoSwitch("timerswitch_glass1",
      new AxisAlignedBB((5.5d/16),(5.5d/16),(0d/16),(10.5d/16),(10.5d/16),(0.1d/16)),
      BlockSwitch.SWITCH_CONFIG_TIMER_INTERVAL|
      BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.2f)
    ),

    // -----------------------------------------------------------------------------------------------------------------
    // -- Block observer and inspection switches
    // -----------------------------------------------------------------------------------------------------------------

    // Uni-directional block detector switch
    new BlockObserverSwitch("observerswitch1",
      new AxisAlignedBB(0.5/16,0.5/16,0.5/16, 15.5/16,15.5/16,13.5/16), null,
      BlockSwitch.SWITCH_CONFIG_SENSOR_BLOCKDETECT|BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_FULLCUBIC_BLOCK|BlockSwitch.SWITCH_CONFIG_NOT_PASSABLE|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
      BlockSwitch.SWITCH_DATA_SIDE_ENABLED_BOTTOM|BlockSwitch.SWITCH_DATA_SIDE_ENABLED_TOP|
      BlockSwitch.SWITCH_DATA_SIDE_ENABLED_FRONT|BlockSwitch.SWITCH_DATA_SIDE_ENABLED_LEFT|
      BlockSwitch.SWITCH_DATA_SIDE_ENABLED_RIGHT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.2f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.2f, 1.2f),
      ModAuxiliaries.RsMaterials.MATERIAL_METALLIC
    ),

    // -----------------------------------------------------------------------------------------------------------------
    // -- link relay switches
    // -----------------------------------------------------------------------------------------------------------------

    // Industrial pulse link relay receiver switch
    new BlockSwitch("relay_pulseswitchrx1",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
      new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
      BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.9f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.7f)
    ),
    // Industrial full block pulse link relay receiver switch
    new BlockSwitch("relay_pulseswitchrx2",
      new AxisAlignedBB((0),(0),(0),(1),(1),(1)),
      new AxisAlignedBB((0),(0),(0),(1),(1),(1)),
      BlockSwitch.SWITCH_CONFIG_FULLCUBIC_BLOCK|BlockSwitch.SWITCH_CONFIG_NOT_PASSABLE|
        BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_DATA_WEAK|
        BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
        BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_DATA_SIDE_ENABLED_ALL|
        BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.9f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.7f),
      ModAuxiliaries.RsMaterials.MATERIAL_METALLIC
    ),
    // Industrial bistable link relay receiver switch
    new BlockSwitch("relay_bistableswitchrx1",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
      new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
      BlockSwitch.SWITCH_CONFIG_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_BISTABLE|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_DATA_WEAK|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.9f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.7f)
    ),
    // Industrial full block bistable link relay receiver switch
    new BlockSwitch("relay_bistableswitchrx2",
      new AxisAlignedBB((0),(0),(0),(1),(1),(1)),
      new AxisAlignedBB((0),(0),(0),(1),(1),(1)),
      BlockSwitch.SWITCH_CONFIG_FULLCUBIC_BLOCK|BlockSwitch.SWITCH_CONFIG_NOT_PASSABLE|
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_DATA_WEAK|
      BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_DATA_SIDE_ENABLED_ALL|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.9f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.7f),
      ModAuxiliaries.RsMaterials.MATERIAL_METALLIC
    ),
    // Industrial pulse link relay
    new BlockSwitch("relay_pulseswitchtx1",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
      new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_LINK_RELAY|
      BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_DATA_WEAK|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.9f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.7f)
    ),
    // Industrial bistable link relay
    new BlockSwitch("relay_bistableswitchtx1",
      new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
      new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
      BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_LINK_RELAY|
      BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_DATA_WEAK|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.9f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.7f)
    ),

    // -----------------------------------------------------------------------------------------------------------------
    // -- Seismic sensor surge switches
    // -----------------------------------------------------------------------------------------------------------------

    // Bistable industrial knock surge detctor
    new BlockKnockBistableSwitch("industrial_knock_switch",
      new AxisAlignedBB(0.5/16,0.5/16,0.5/16, 15.5/16,15.5/16,15.5/16),
      null,
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
      BlockSwitch.SWITCH_CONFIG_FULLCUBIC_BLOCK|BlockSwitch.SWITCH_CONFIG_OPPOSITE_PLACEMENT|
      BlockSwitch.SWITCH_CONFIG_NOT_PASSABLE|BlockSwitch.SWITCH_DATA_SIDE_ENABLED_ALL,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.2f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.2f, 1.2f)
    ),

    // Pulse industrial knock surge detctor
    new BlockKnockPulseSwitch("industrial_knock_button",
      new AxisAlignedBB(0.5/16,0.5/16,0.5/16, 15.5/16,15.5/16,15.5/16),
      null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
      BlockSwitch.SWITCH_CONFIG_FULLCUBIC_BLOCK|BlockSwitch.SWITCH_CONFIG_OPPOSITE_PLACEMENT|
      BlockSwitch.SWITCH_CONFIG_NOT_PASSABLE|BlockSwitch.SWITCH_DATA_SIDE_ENABLED_ALL,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.2f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.2f, 1.2f)
    ),

    // -----------------------------------------------------------------------------------------------------------------
    // -- Manual dimmers
    // -----------------------------------------------------------------------------------------------------------------

    // Industrial dimmer switch
    new BlockDimmerSwitch("dimmerswitch1",
      new AxisAlignedBB((5d/16),(1.5d/16),(0d/16), (11d/16),(14.25d/16),(0.60d/16)),
      BlockSwitch.SWITCH_CONFIG_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.9f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.7f),
      ModAuxiliaries.RsMaterials.MATERIAL_METALLIC
    ),

    // -----------------------------------------------------------------------------------------------------------------
    // -- sensitive glass
    // -----------------------------------------------------------------------------------------------------------------
    new BlockSensitiveGlass("sensitiveglass"          , 0x000f|0x0020, 0xffffff), // light value if on 0xf | off 0x1, color multiplier
    new BlockSensitiveGlass("sensitiveglass_white"    , 0x0000|0x0000, ModAuxiliaries.DyeColorFilters.WHITE),       // 0xf3f3f3
    new BlockSensitiveGlass("sensitiveglass_red"      , 0x0000|0x0000, ModAuxiliaries.DyeColorFilters.RED),         // 0xB02E26
    new BlockSensitiveGlass("sensitiveglass_green"    , 0x0000|0x0000, ModAuxiliaries.DyeColorFilters.GREEN),       // 0x5E7C16
    new BlockSensitiveGlass("sensitiveglass_blue"     , 0x0000|0x0000, ModAuxiliaries.DyeColorFilters.BLUE),        // 0x3C44AA
    new BlockSensitiveGlass("sensitiveglass_yellow"   , 0x0000|0x0000, ModAuxiliaries.DyeColorFilters.YELLOW),      // 0xFED83D
    new BlockSensitiveGlass("sensitiveglass_orange"   , 0x0000|0x0000, ModAuxiliaries.DyeColorFilters.ORANGE),      // 0xF9801D
    new BlockSensitiveGlass("sensitiveglass_magenta"  , 0x0000|0x0000, ModAuxiliaries.DyeColorFilters.MAGENTA),     // 0xC74EBD
    new BlockSensitiveGlass("sensitiveglass_lightblue", 0x0000|0x0000, ModAuxiliaries.DyeColorFilters.LIGHTBLUE),   // 0x3AB3DA
    new BlockSensitiveGlass("sensitiveglass_lime"     , 0x0000|0x0000, ModAuxiliaries.DyeColorFilters.LIME),        // 0x80C71F
    new BlockSensitiveGlass("sensitiveglass_pink"     , 0x0000|0x0000, ModAuxiliaries.DyeColorFilters.PINK),        // 0xF38BAA
    new BlockSensitiveGlass("sensitiveglass_gray"     , 0x0000|0x0000, ModAuxiliaries.DyeColorFilters.GRAY),        // 0x474F52
    new BlockSensitiveGlass("sensitiveglass_lightgray", 0x0000|0x0000, ModAuxiliaries.DyeColorFilters.SILVER),      // 0x9D9D97
    new BlockSensitiveGlass("sensitiveglass_cyan"     , 0x0000|0x0000, ModAuxiliaries.DyeColorFilters.CYAN),        // 0x169C9C
    new BlockSensitiveGlass("sensitiveglass_purple"   , 0x0000|0x0000, ModAuxiliaries.DyeColorFilters.PURPLE),      // 0x8932B8
    new BlockSensitiveGlass("sensitiveglass_brown"    , 0x0000|0x0000, ModAuxiliaries.DyeColorFilters.BROWN),       // 0x835432
    new BlockSensitiveGlass("sensitiveglass_black"    , 0x0000|0x0000, ModAuxiliaries.DyeColorFilters.BLACK),       // 0x111111
    new BlockSensitiveGlass("sensitiveglass_inverted" , 0x0002|0x00f0, 0xffffff)
  };

  private static final Block devBlocks[] = {
    new BlockObserverSwitch("qube",
      new AxisAlignedBB(0,0,0,1,1,1), null,
      BlockSwitch.SWITCH_CONFIG_FULLCUBIC_BLOCK|BlockSwitch.SWITCH_CONFIG_NOT_PASSABLE|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT
      , null, null,
      ModAuxiliaries.RsMaterials.MATERIAL_METALLIC
    ),

  };

  // ------------------------------------------------------------------------------------------------------------------>
  // TEMPORARY CODE TO PREVENT BREAKING SAVED GAMES WHEN UPDATING FROM v1.0.x to v1.1.0, REMOVE LATEST AT v1.1.3 release
  private static final class VersionTransition
  {
    public static final class V10xTileEntityGauge extends BlockGauge.TileEntityGauge {}
    public static final class V10xTileEntitySwitch extends BlockSwitch.TileEntitySwitch {}
    public static final class V10xTileEntityContactSwitch extends BlockContactSwitch.TileEntityContactSwitch {}
    public static final class V10xTileEntityAutoSwitch extends BlockAutoSwitch.TileEntityAutoSwitch {}
    public static final class V10xTileEntityDetectorSwitch extends BlockAutoSwitch.TileEntityDetectorSwitch {}
    public static final class V10xTileEntityEnvironmentalSensorSwitch extends BlockAutoSwitch.TileEntityEnvironmentalSensorSwitch {}
    public static final class V10xTileEntityIntervalTimerSwitch extends BlockAutoSwitch.TileEntityIntervalTimerSwitch {}

    public static final void registerTileEntity(Class<? extends net.minecraft.tileentity.TileEntity> tileEntityClass, String key)
    {
      // ok, depite of data fixing the TEs don't work correctly if these old names are not registered, and I've no idea why.
      ModRsGauges.logger.info("Registering old underscore-prefixed tile entity for version transition, this may cause a warning.");
      GameRegistry.registerTileEntity(tileEntityClass, new ResourceLocation("minecraft", ModRsGauges.MODID + "_" + key));
    }
  }
  // <------------------------------------------------------------------------------------------------------------------

  /**
   * Returns true if the given (rsgauges RS) block is enabled, false if opt'ed out in the config.
   */
  public static boolean enabled(Block block) {
    if(block instanceof BlockIndicator) {
      BlockIndicator bl = ((BlockIndicator)block);
      if(ModConfig.optouts.without_indicators) return false;
      if((ModConfig.optouts.without_blinking_indicators) && (bl.blink_interval() > 0)) return false;
      if((ModConfig.optouts.without_sound_indicators) && ((bl.power_on_sound != null) || (bl.power_off_sound != null))) return false;
    } if(block instanceof BlockGauge) {
      BlockGauge bl = ((BlockGauge)block);
      if(ModConfig.optouts.without_gauges) return false;
    } else if(block instanceof BlockSwitch) {
      BlockSwitch bl = ((BlockSwitch)block);
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

  private static ArrayList<Block> registeredBlocks = new ArrayList<>();

  @Nonnull
  public static List<Block> getRegisteredBlocks()
  { return Collections.unmodifiableList(registeredBlocks); }

  // Invoked from CommonProxy.registerBlocks()
  public static final void registerBlocks(RegistryEvent.Register<Block> event)
  {
    // Config based registry selection
    ArrayList<Block> allBlocks = new ArrayList<>();
    Collections.addAll(allBlocks, modBlocks);
    if(ModConfig.zmisc.with_experimental) Collections.addAll(allBlocks, devBlocks);
    if(ModConfig.zmisc.without_optout_registrations) ModRsGauges.logger.info("Registration opt-out configured, disabled block categories will not be registered at all.");

    // note to self: first trying to use the switch config to check which switch entities
    // are needed, not e.g. a map<classid,bool> from createTileEntity().getClass() or so.
    boolean gauge_entity_needed = false;
    long switch_config_bits_union_set = 0;
    for(Block e:allBlocks) {
      if(ModConfig.zmisc.without_optout_registrations && (!enabled(e))) continue;
      if(e instanceof BlockGauge) {
        gauge_entity_needed = true;
      } else if(e instanceof BlockSwitch) {
        switch_config_bits_union_set |= ((BlockSwitch)e).config;
      }
      registeredBlocks.add(e);
    }

    // Tile entities
    if(gauge_entity_needed) {
      GameRegistry.registerTileEntity(BlockGauge.TileEntityGauge.class, new ResourceLocation(ModRsGauges.MODID, "gauge_entity"));
      VersionTransition.registerTileEntity(VersionTransition.V10xTileEntityGauge.class,"gauge_entity");
      ModRsGauges.logger.info("Registered gauge tile entity.");
    }
    if((switch_config_bits_union_set & (BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_LINK_RELAY)) != 0) {
      GameRegistry.registerTileEntity(BlockSwitch.TileEntitySwitch.class, new ResourceLocation(ModRsGauges.MODID, "switch_entity"));
      VersionTransition.registerTileEntity(VersionTransition.V10xTileEntitySwitch.class, "pulseswitch_entity");
      ModRsGauges.logger.info("Registered switch tile entity.");
    }
    if((switch_config_bits_union_set & (BlockSwitch.SWITCH_CONFIG_CONTACT)) != 0) {
      GameRegistry.registerTileEntity(BlockContactSwitch.TileEntityContactSwitch.class, new ResourceLocation(ModRsGauges.MODID, "contactswitch_entity"));
      VersionTransition.registerTileEntity(VersionTransition.V10xTileEntityContactSwitch.class,"contactswitch_entity");
      ModRsGauges.logger.info("Registered contact switch tile entity.");
    }
    if((switch_config_bits_union_set & (BlockSwitch.SWITCH_CONFIG_AUTOMATIC)) != 0) {
      GameRegistry.registerTileEntity(BlockAutoSwitch.TileEntityAutoSwitch.class, new ResourceLocation(ModRsGauges.MODID, "autoswitch_entity"));
      VersionTransition.registerTileEntity(VersionTransition.V10xTileEntityAutoSwitch.class, "autoswitch_entity");
      ModRsGauges.logger.info("Registered auto switch tile entity.");
      if((switch_config_bits_union_set & (BlockSwitch.SWITCH_CONFIG_SENSOR_DETECTOR)) != 0) {
        GameRegistry.registerTileEntity(BlockAutoSwitch.TileEntityDetectorSwitch.class, new ResourceLocation(ModRsGauges.MODID, "detectorswitch_entity"));
        VersionTransition.registerTileEntity(VersionTransition.V10xTileEntityDetectorSwitch.class, "detectorswitch_entity");
        ModRsGauges.logger.info("Registered detector auto switch tile entity.");
      }
      if((switch_config_bits_union_set & (BlockSwitch.SWITCH_CONFIG_SENSOR_ENVIRONMENTAL)) != 0) {
        GameRegistry.registerTileEntity(BlockAutoSwitch.TileEntityEnvironmentalSensorSwitch.class, new ResourceLocation(ModRsGauges.MODID, "sensorswitch_entity"));
        VersionTransition.registerTileEntity(VersionTransition.V10xTileEntityEnvironmentalSensorSwitch.class, "sensorswitch_entity");
        ModRsGauges.logger.info("Registered environmental auto switch tile entity.");
      }
      if((switch_config_bits_union_set & (BlockSwitch.SWITCH_CONFIG_SENSOR_TIME)) != 0) {
        GameRegistry.registerTileEntity(BlockAutoSwitch.TileEntityIntervalTimerSwitch.class, new ResourceLocation(ModRsGauges.MODID, "timerswitch_entity"));
        VersionTransition.registerTileEntity(VersionTransition.V10xTileEntityIntervalTimerSwitch.class, "timerswitch_entity");
        ModRsGauges.logger.info("Registered timer auto switch tile entity.");
      }
      if((switch_config_bits_union_set & (BlockSwitch.SWITCH_CONFIG_SENSOR_BLOCKDETECT)) != 0) {
        GameRegistry.registerTileEntity(BlockObserverSwitch.TileEntityObserverSwitch.class, new ResourceLocation(ModRsGauges.MODID, "observerswitch_entity"));
        ModRsGauges.logger.info("Registered inspection switch tile entity.");
      }
    }

    // Register blocks
    for(Block e:registeredBlocks) event.getRegistry().register(e);
    ModRsGauges.logger.info("Registered " + Integer.toString(registeredBlocks.size()) + " blocks.");
  }

  // Invoked from ClientProxy.registerModels()
  @SideOnly(Side.CLIENT)
  public static final void initModels()
  {
    int n = 0;
    for(Block e:registeredBlocks) {
      if(e instanceof RsBlock) {
        ((RsBlock)e).initModel();
        ++n;
      } else if(e instanceof BlockSensitiveGlass) {
        ((BlockSensitiveGlass)e).initModel();
        ++n;
      }
    }
  }

  // Invoked from CommonProxy.registerItems()
  public static final void registerItemBlocks(RegistryEvent.Register<Item> event)
  {
    int n = 0;
    for(Block e:registeredBlocks) {
      ResourceLocation rl = e.getRegistryName();
      if(rl == null) continue;
      event.getRegistry().register(new ItemBlock(e).setRegistryName(rl));
      ++n;
    }
  }

  /**
   * Encapsulates colour handling for blocks and their item representations.
   */
  @Mod.EventBusSubscriber(modid=ModRsGauges.MODID)
  public static final class Colors
  {
    public static interface ColorTintSupport
    {
      /**
       * Return true if the specific block shall be registered for tinting.
       */
      default public boolean hasColorMultiplierRGBA() { return false; }

      /**
       * Unified forwarded for items and blocks to retrieve the color filter .
       */
      default public int getColorMultiplierRGBA(@Nullable IBlockState state, @Nullable IBlockAccess world, @Nullable BlockPos pos) { return 0xffffffff; }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static final void registerBlockColourHandlers(final ColorHandlerEvent.Block event)
    {
      final IBlockColor blockSpecifiedColorHandler = (state, blockAccess, pos, tintIndex) -> (((ColorTintSupport)state.getBlock()).getColorMultiplierRGBA(state, blockAccess, pos));
      final BlockColors bc = event.getBlockColors();
      int n = 0;
      for(Block e:registeredBlocks) {
        if((e instanceof Colors.ColorTintSupport) && (((Colors.ColorTintSupport)e).hasColorMultiplierRGBA())) {
          bc.registerBlockColorHandler(blockSpecifiedColorHandler, e);
          ++n;
        }
      }
      ModRsGauges.logger.info("Registered " + Integer.toString(n) + " block color handlers.");
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static final void registerItemColourHandlers(final ColorHandlerEvent.Item event)
    {
      final ItemColors ic = event.getItemColors();
      final IItemColor constantBlockColorHandler = (stack, tintIndex) -> (((ColorTintSupport)((ItemBlock)stack.getItem()).getBlock()).getColorMultiplierRGBA(null, null, null));
      for(Block e:registeredBlocks) {
        if((e instanceof Colors.ColorTintSupport) && (((Colors.ColorTintSupport)e).hasColorMultiplierRGBA())) {
          ic.registerItemColorHandler(constantBlockColorHandler, e);
        }
      }
    }
  }

}
