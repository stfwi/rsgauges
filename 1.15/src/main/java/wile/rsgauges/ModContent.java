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
package wile.rsgauges;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import wile.rsgauges.detail.ModAuxiliaries;
import wile.rsgauges.blocks.*;
import wile.rsgauges.detail.ModResources;
import wile.rsgauges.items.*;
import net.minecraft.block.material.PushReaction;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.event.RegistryEvent;
import net.minecraft.util.SoundEvents;
import org.apache.commons.lang3.ArrayUtils;
import java.util.*;
import javax.annotation.Nonnull;


public class ModContent
{
  // -----------------------------------------------------------------------------------------------------------------
  // -- Internal constants, normally private
  // -----------------------------------------------------------------------------------------------------------------

  private static final String MODID = ModRsGauges.MODID;

  // (new Material.Builder(MaterialColor.IRON)) -> lacks istoolreq setting -> bite the bullet
  private static final Material METAL_MATERIAL = new Material(
    MaterialColor.IRON, false, true,    // MaterialColor materialMapColorIn, boolean liquid, boolean solid,
    true, true, true,                   // boolean doesBlockMovement, boolean opaque, boolean requiresNoToolIn,
    false, false, PushReaction.DESTROY  // boolean canBurnIn, boolean replaceableIn, PushReaction mobilityFlag
  );
  private static final Material GLASS_MATERIAL = new Material(
    MaterialColor.IRON, false, true,    // MaterialColor materialMapColorIn, boolean liquid, boolean solid,
    true, false, true,                  // boolean doesBlockMovement, boolean opaque, boolean requiresNoToolIn,
    false, false, PushReaction.DESTROY  // boolean canBurnIn, boolean replaceableIn, PushReaction mobilityFlag
  );

  // -----------------------------------------------------------------------------------------------------------------
  // -- gauges
  // -----------------------------------------------------------------------------------------------------------------

  private static final Block.Properties GAUGE_METALLIC_BLOCK_PROPERTIES = (Block.Properties
    .create(METAL_MATERIAL, MaterialColor.IRON)
    .hardnessAndResistance(0.5f, 15f)
    .sound(SoundType.METAL)
    .func_226896_b_() // notsolid
    .harvestLevel(0)
  );

  private static final Block.Properties GAUGE_GLASS_BLOCK_PROPERTIES = (Block.Properties
    .create(METAL_MATERIAL, MaterialColor.IRON)
    .hardnessAndResistance(0.5f, 15f)
    .sound(SoundType.METAL)
    .func_226896_b_() // notsolid
    .harvestLevel(0)
  );

  public static final BlockGauge INDUSTRIAL_ANALOG_GAUGE = (BlockGauge)(new BlockGauge(
    BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT,
    GAUGE_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(2,2,0, 14,14,1)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_analog_angular_gauge"));

  public static final BlockGauge INDUSTRIAL_VERTICAL_BAR_GAUGE = (BlockGauge)(new BlockGauge(
    BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT,
    GAUGE_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((4d/16),(2d/16),(0d/16), (12d/16),(14d/16),(1d/16))
  )).setRegistryName(new ResourceLocation(MODID, "industrial_vertical_bar_gauge"));

  public static final BlockGauge INDUSTRIAL_SMALL_DIGITAL_GAUGE = (BlockGauge)(new BlockGauge(
    BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT,
    GAUGE_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((4d/16),(5d/16),(0d/16), (12d/16),(11d/16),(1d/16))
  )).setRegistryName(new ResourceLocation(MODID, "industrial_small_digital_gauge"));

  public static final BlockGauge GLASS_VERTICAL_BAR_GAUGE = (BlockGauge)(new BlockGauge(
    BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT,
    GAUGE_GLASS_BLOCK_PROPERTIES,
    new AxisAlignedBB((7d/16),(3.7d/16),(0d/16), (10d/16),(12d/16),(0.4d/16))
  )).setRegistryName(new ResourceLocation(MODID, "glass_vertical_bar_gauge"));

  public static final BlockGauge INDUSTRIAL_TUBE_GAUGE = (BlockGauge)(new BlockGauge(
    BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT,
    GAUGE_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((7d/16),(4d/16),(0d/16), (9d/16),(12d/16),(3d/16))
  )).setRegistryName(new ResourceLocation(MODID, "industrial_tube_gauge"));

  public static final BlockGauge INDUSTRIAL_ANALOG_HORIZONTAL_GAUGE = (BlockGauge)(new BlockGauge(
    BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT,
    GAUGE_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((2d/16),(4d/16),(0d/16), (14d/16),(12d/16),(1d/16))
  )).setRegistryName(new ResourceLocation(MODID, "industrial_analog_horizontal_gauge"));

  public static final BlockGauge RUSTIC_CIRCULAR_GAUGE = (BlockGauge)(new BlockGauge(
    0, // no color tint.
    GAUGE_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((2d/16),(2d/16),(0d/16), (14d/16),(14d/16),(1d/16))
  )).setRegistryName(new ResourceLocation(MODID, "rustic_circular_gauge"));

  public static final BlockGauge gauges[] = {
    INDUSTRIAL_ANALOG_GAUGE,
    INDUSTRIAL_VERTICAL_BAR_GAUGE,
    INDUSTRIAL_SMALL_DIGITAL_GAUGE,
    INDUSTRIAL_TUBE_GAUGE,
    INDUSTRIAL_ANALOG_HORIZONTAL_GAUGE,
    GLASS_VERTICAL_BAR_GAUGE
  };

  // -----------------------------------------------------------------------------------------------------------------
  // -- indicators
  // -----------------------------------------------------------------------------------------------------------------

  private static final Block.Properties INDICATOR_METALLIC_BLOCK_PROPERTIES = (Block.Properties
    .create(GLASS_MATERIAL, MaterialColor.IRON)
    .hardnessAndResistance(0.5f, 15f)
    .sound(SoundType.METAL)
    .harvestLevel(0)
    .lightValue(2)
    .func_226896_b_() // notsolid
  );

  private static final Block.Properties INDICATOR_GLASS_BLOCK_PROPERTIES = (Block.Properties
    .create(GLASS_MATERIAL, MaterialColor.IRON)
    .hardnessAndResistance(0.5f, 15f)
    .sound(SoundType.METAL)
    .harvestLevel(0)
    .lightValue(2)
    .func_226896_b_() // notsolid
  );

  private static final Block.Properties ALARM_LAMP_BLOCK_PROPERTIES = (Block.Properties
    .create(GLASS_MATERIAL, MaterialColor.IRON)
    .hardnessAndResistance(0.5f, 15f)
    .sound(SoundType.METAL)
    .harvestLevel(0)
    .lightValue(8)
    .func_226896_b_() // notsolid
  );

  // square LED
  public static final BlockIndicator INDUSTRIAL_GREEN_LED_INDICATOR = (BlockIndicator)(new BlockIndicator(
    (13<<BlockGauge.GAUGE_DATA_COLOR_SHIFT), // green
    INDICATOR_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16))
  )).setRegistryName(new ResourceLocation(MODID, "industrial_green_led"));

  public static final BlockIndicator INDUSTRIAL_YELLOW_LED_INDICATOR = (BlockIndicator)(new BlockIndicator(
    (5<<BlockGauge.GAUGE_DATA_COLOR_SHIFT), // yellow
    INDICATOR_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16))
  )).setRegistryName(new ResourceLocation(MODID, "industrial_yellow_led"));

  public static final BlockIndicator INDUSTRIAL_RED_LED_INDICATOR = (BlockIndicator)(new BlockIndicator(
    (0<<BlockGauge.GAUGE_DATA_COLOR_SHIFT), // red
    INDICATOR_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16))
  )).setRegistryName(new ResourceLocation(MODID, "industrial_red_led"));

  public static final BlockIndicator INDUSTRIAL_WHITE_LED_INDICATOR = (BlockIndicator)(new BlockIndicator(
    BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT, // default white
    INDICATOR_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16))
  )).setRegistryName(new ResourceLocation(MODID, "industrial_white_led"));

  public static final BlockIndicator INDUSTRIAL_GREEN_BLINK_LED_INDICATOR = (BlockIndicator)(new BlockIndicator(
    BlockGauge.GAUGE_DATA_BLINKING|(13<<BlockGauge.GAUGE_DATA_COLOR_SHIFT), // green
    INDICATOR_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16))
  )).setRegistryName(new ResourceLocation(MODID, "industrial_green_blinking_led"));

  public static final BlockIndicator INDUSTRIAL_YELLOW_BLINK_LED_INDICATOR = (BlockIndicator)(new BlockIndicator(
    BlockGauge.GAUGE_DATA_BLINKING|(5<<BlockGauge.GAUGE_DATA_COLOR_SHIFT), // yellow
    INDICATOR_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16))
  )).setRegistryName(new ResourceLocation(MODID, "industrial_yellow_blinking_led"));

  public static final BlockIndicator INDUSTRIAL_RED_BLINK_LED_INDICATOR = (BlockIndicator)(new BlockIndicator(
    BlockGauge.GAUGE_DATA_BLINKING|(0<<BlockGauge.GAUGE_DATA_COLOR_SHIFT), // red
    INDICATOR_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16))
  )).setRegistryName(new ResourceLocation(MODID, "industrial_red_blinking_led"));

  public static final BlockIndicator INDUSTRIAL_WHITE_BLINK_LED_INDICATOR = (BlockIndicator)(new BlockIndicator(
    BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT, // default white
    INDICATOR_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(0.5d/16))
  )).setRegistryName(new ResourceLocation(MODID, "industrial_white_blinking_led"));

  public static final BlockIndicator INDUSTRIAL_ALARM_LAMP = (BlockIndicator)(new BlockIndicator(
    BlockGauge.GAUGE_DATA_BLINKING,
    ALARM_LAMP_BLOCK_PROPERTIES,
    new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(4d/16))
  )).setRegistryName(new ResourceLocation(MODID, "industrial_alarm_lamp"));

  public static final BlockIndicator INDUSTRIAL_ALARM_SIREN = (BlockIndicator)(new BlockIndicator(
    BlockGauge.GAUGE_DATA_BLINKING,
    INDICATOR_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((4d/16),(6.5d/16),(0d/16), (11.5d/16),(9.5d/16),(4d/16)),
    new ModResources.BlockSoundEvent(ModResources.ALARM_SIREN_SOUND),
    null
  )).setRegistryName(new ResourceLocation(MODID, "industrial_alarm_siren"));

  public static final BlockIndicator RUSTIC_SEMAPHORE_INDICATOR = (BlockIndicator)(new BlockIndicator(
    0,
    INDICATOR_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((5d/16),(8d/16),(0d/16), (12d/16),(10d/16),(0.5d/16)),
    null,
    null
  )).setRegistryName(new ResourceLocation(MODID, "rustic_semaphore"));

  public static final BlockIndicator indicators[] = {
    INDUSTRIAL_GREEN_LED_INDICATOR,
    INDUSTRIAL_YELLOW_LED_INDICATOR,
    INDUSTRIAL_RED_LED_INDICATOR,
    INDUSTRIAL_WHITE_LED_INDICATOR,
    INDUSTRIAL_GREEN_BLINK_LED_INDICATOR,
    INDUSTRIAL_YELLOW_BLINK_LED_INDICATOR,
    INDUSTRIAL_RED_BLINK_LED_INDICATOR,
    INDUSTRIAL_WHITE_BLINK_LED_INDICATOR,
    INDUSTRIAL_ALARM_LAMP,
    INDUSTRIAL_ALARM_SIREN,
    RUSTIC_SEMAPHORE_INDICATOR
  };

  // -----------------------------------------------------------------------------------------------------------------
  // -- bi-stable switches
  // -----------------------------------------------------------------------------------------------------------------

  private static final Block.Properties SWITCH_METALLIC_BLOCK_PROPERTIES = GAUGE_METALLIC_BLOCK_PROPERTIES;
  private static final Block.Properties SWITCH_GLASS_BLOCK_PROPERTIES = GAUGE_GLASS_BLOCK_PROPERTIES;

  // Rotary machine switch
  public static final BlockBistableSwitch INDUSTRIAL_ROTARY_MACHINE_SWITCH = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((4.25d/16),(4.25d/16),(0d/16),(11.75d/16),(11.75d/16),(0.80d/16))
  )).setRegistryName(new ResourceLocation(MODID, "industrial_rotary_machine_switch"));

  // Contact lever switch
  public static final BlockBistableSwitch INDUSTRIAL_SMALL_LEVER = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(14d/16),(1.5d/16)),
    new AxisAlignedBB((6d/16),(2d/16),(0d/16),(10d/16),(10d/16),(1.5d/16))
  )).setRegistryName(new ResourceLocation(MODID, "industrial_small_lever"));

  // Two-button machine switch
  public static final BlockBistableSwitch INDUSTRIAL_MACHINE_SWITCH = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((4d/16),(4d/16),(0d/16),(12d/16),(12d/16),(1d/16)), null
  )).setRegistryName(new ResourceLocation(MODID, "industrial_machine_switch"));

  // Light flip switch
  public static final BlockBistableSwitch LIGHT_SWITCH = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((7d/16),(6d/16),(0d/16),(9d/16),(10d/16),(1.5d/16)), null
  )).setRegistryName(new ResourceLocation(MODID, "light_switch"));

  // ESTOP button
  public static final BlockBistableSwitch INDUSTRIAL_ESTOP_SWITCH = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE_OFF,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1.5d/16)),
    new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(2.5d/16))
  )).setRegistryName(new ResourceLocation(MODID, "industrial_estop_switch"));

  // Hopper blocking switch
  public static final BlockBistableSwitch INDUSTRIAL_HOPPER_BLOCKING_SWITCH = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    BlockSwitch.SWITCH_CONFIG_HOPPER_MOUNTBALE|BlockSwitch.SWITCH_DATA_WEAK,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((4.8d/16),(10d/16),(0d/16), (11.2d/16),(11d/16),(6d/16)),
    new AxisAlignedBB((4.8d/16),(10d/16),(0d/16), (11.2d/16),(11d/16),(3d/16))
  )).setRegistryName(new ResourceLocation(MODID, "industrial_hopper_switch"));

  // Mechanical rotary lever
  public static final BlockBistableSwitch INDUSTRIAL_ROTARY_LEVER = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    BlockSwitch.SWITCH_CONFIG_HOPPER_MOUNTBALE,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((2.5d/16),(9.5d/16),(0d/16), (12.3d/16),(13d/16),(2.7d/16)),
    new AxisAlignedBB((8.7d/16),( 3d/16),(0d/16), (12.3d/16),(13d/16),(2.7d/16))
  )).setRegistryName(new ResourceLocation(MODID, "industrial_rotary_lever"));

  // Mechanical lever
  public static final BlockBistableSwitch INDUSTRIAL_LEVER = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6.5d/16),(6.5d/16),(0d/16), (9.7d/16),(14.0d/16),(3d/16)),
    new AxisAlignedBB((6.5d/16),(5.0d/16),(0d/16), (9.7d/16),(12.5d/16),(3d/16))
  )).setRegistryName(new ResourceLocation(MODID, "industrial_lever"));

  // Old fancy gold decorated lever
  public static final BlockBistableSwitch OLDFANCY_BISTABLE_SWITCH1 = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6d/16),(6.5d/16),(0d/16), (10.3d/16),(13.5d/16),(4.5d/16)),
    new AxisAlignedBB((6d/16),(3.5d/16),(0d/16), (10.3d/16),(10.0d/16),(4.5d/16))
  )).setRegistryName(new ResourceLocation(MODID, "oldfancy_bistableswitch1"));

  // Old fancy angular lever
  public static final BlockBistableSwitch OLDFANCY_BISTABLE_SWITCH2 = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    BlockSwitch.SWITCH_CONFIG_HOPPER_MOUNTBALE,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((2.5d/16),(6.0d/16),(0d/16), (9.7d/16),(10.0d/16),(4.5d/16)),
    new AxisAlignedBB((4.5d/16),(3.5d/16),(0d/16), (9.2d/16),(10.0d/16),(4.5d/16))
  )).setRegistryName(new ResourceLocation(MODID, "oldfancy_bistableswitch2"));

  // Old fancy bent lever
  public static final BlockBistableSwitch OLDFANCY_BISTABLE_SWITCH3 = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((4.5d/16),(5.0d/16),(0d/16), (12.0d/16),(11.0d/16),(4.5d/16)),
    new AxisAlignedBB((4.5d/16),(5.0d/16),(0d/16), (12.0d/16),(11.0d/16),(4.5d/16))
  )).setRegistryName(new ResourceLocation(MODID, "oldfancy_bistableswitch3"));

  // Old fancy gold decorated manual slide sphere connector
  public static final BlockBistableSwitch OLDFANCY_BISTABLE_SWITCH4 = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((5.0d/16),(6.0d/16),(0d/16), (13.0d/16),(13.0d/16),(4.5d/16)), null
  )).setRegistryName(new ResourceLocation(MODID, "oldfancy_bistableswitch4"));

  // Old fancy gold decorated rotary sphere connector
  public static final BlockBistableSwitch OLDFANCY_BISTABLE_SWITCH5 = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((5.0d/16),(5.0d/16),(0d/16), (13.0d/16),(13.0d/16),(4.5d/16)),
    new AxisAlignedBB((5.0d/16),(6.0d/16),(0d/16), (13.0d/16),(13.0d/16),(4.5d/16))
  )).setRegistryName(new ResourceLocation(MODID, "oldfancy_bistableswitch5"));

  // Old fancy gold decorated rotary sphere connector 2
  public static final BlockBistableSwitch OLDFANCY_BISTABLE_SWITCH6 = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((5.0d/16),(5.0d/16),(0d/16), (13.0d/16),(13.0d/16),(4.5d/16)), null
  )).setRegistryName(new ResourceLocation(MODID, "oldfancy_bistableswitch6"));

  // Old fancy gold decorated manual contactor
  public static final BlockBistableSwitch OLDFANCY_BISTABLE_SWITCH7 = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((2.0d/16),(6.0d/16),(0d/16), (11.7d/16),(14.0d/16),(4.5d/16)),
    new AxisAlignedBB((3.5d/16),(3.5d/16),(0d/16), (11.2d/16),(14.0d/16),(4.5d/16))
  )).setRegistryName(new ResourceLocation(MODID, "oldfancy_bistableswitch7"));

  // Rustic lever 1
  public static final BlockBistableSwitch RUSTIC_LEVER = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6d/16),(5.0d/16),(0d/16), (10.3d/16),(15.0d/16),(4.5d/16)),
    new AxisAlignedBB((6d/16),(2.0d/16),(0d/16), (10.3d/16),(11.0d/16),(4.5d/16))
  )).setRegistryName(new ResourceLocation(MODID, "rustic_lever"));

  // Rustic lever 2 (bolted)
  public static final BlockBistableSwitch RUSTIC_TWO_HINGE_LEVER = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((4d/16),(6.0d/16),(0d/16), (13.0d/16),(13.0d/16),(4.5d/16)),
    new AxisAlignedBB((4d/16),(4.0d/16),(0d/16), (13.0d/16),(10.0d/16),(4.5d/16))
  )).setRegistryName(new ResourceLocation(MODID, "rustic_two_hinge_lever"));

  // Rustic lever 3 (big angular)
  public static final BlockBistableSwitch RUSTIC_ANGULAR_LEVER = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    BlockSwitch.SWITCH_CONFIG_HOPPER_MOUNTBALE,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((0.2d/16),(10.0d/16),(0d/16), (13.5d/16),(15.0d/16),(4.5d/16)),
    new AxisAlignedBB((5.5d/16),( 2.0d/16),(0d/16), (13.5d/16),(15.0d/16),(4.5d/16))
  )).setRegistryName(new ResourceLocation(MODID, "rustic_angular_lever"));

  // Rustic lever 4 (counter weighted rotary lever)
  public static final BlockBistableSwitch RUSTIC_WEIGHT_BALANCED_LEVER = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    BlockSwitch.SWITCH_CONFIG_HOPPER_MOUNTBALE,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((0.5d/16),(11.0d/16),(0d/16), (14.0d/16),(15.0d/16),(4.5d/16)), null
  )).setRegistryName(new ResourceLocation(MODID, "rustic_weight_balanced_lever"));

  // Rustic lever 5
  public static final BlockBistableSwitch RUSTIC_BISTABLE_HANDLE = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((3.5d/16),(6.0d/16),(0d/16), (12.5d/16),(11.0d/16),(4.5d/16)), null
  )).setRegistryName(new ResourceLocation(MODID, "rustic_bistable_handle"));

  // Rustic lever 6 (latch slide)
  public static final BlockBistableSwitch RUSTIC_BISTABLE_SLIDE = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((3d/16),(6.0d/16),(0d/16), (13.0d/16),(12.0d/16),(4.5d/16)), null
  )).setRegistryName(new ResourceLocation(MODID, "rustic_bistable_slide"));

  // Rustic lever 7 (The Nail)
  public static final BlockBistableSwitch RUSTIC_NAIL_LEVER = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    BlockSwitch.SWITCH_CONFIG_HOPPER_MOUNTBALE,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6.0d/16),(7.0d/16),(0d/16), (9.0d/16),(10.0d/16),(3.0d/16)), null
  )).setRegistryName(new ResourceLocation(MODID, "rustic_nail_lever"));

  // Thin star shaped glass switch
  public static final BlockBistableSwitch GLASS_ROTARY_SWITCH = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|BlockSwitch.SWITCH_CONFIG_FAINT_LIGHTSOURCE|
    BlockSwitch.SWITCH_CONFIG_COLOR_TINT_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((5.5d/16),(5.5d/16),(0d/16),(10.5d/16),(10.5d/16),(0.5d/16)), null
  )).setRegistryName(new ResourceLocation(MODID, "glass_rotary_switch"));

  // Bistable glass touch switch
  public static final BlockBistableSwitch GLASS_TOUCH_SWITCH = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|BlockSwitch.SWITCH_CONFIG_FAINT_LIGHTSOURCE|
    BlockSwitch.SWITCH_CONFIG_COLOR_TINT_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((5.5d/16),(5.5d/16),(0d/16),(10.5d/16),(10.5d/16),(0.5d/16)), null
  )).setRegistryName(new ResourceLocation(MODID, "glass_touch_switch"));

  public static final BlockBistableSwitch bistable_switches[] = {
    INDUSTRIAL_ROTARY_MACHINE_SWITCH,
    INDUSTRIAL_SMALL_LEVER,
    INDUSTRIAL_MACHINE_SWITCH,
    LIGHT_SWITCH,
    INDUSTRIAL_ESTOP_SWITCH,
    INDUSTRIAL_HOPPER_BLOCKING_SWITCH,
    INDUSTRIAL_ROTARY_LEVER,
    INDUSTRIAL_LEVER,
    OLDFANCY_BISTABLE_SWITCH1,
    OLDFANCY_BISTABLE_SWITCH2,
    OLDFANCY_BISTABLE_SWITCH3,
    OLDFANCY_BISTABLE_SWITCH4,
    OLDFANCY_BISTABLE_SWITCH5,
    OLDFANCY_BISTABLE_SWITCH6,
    OLDFANCY_BISTABLE_SWITCH7,
    RUSTIC_LEVER,
    RUSTIC_TWO_HINGE_LEVER,
    RUSTIC_ANGULAR_LEVER,
    RUSTIC_WEIGHT_BALANCED_LEVER,
    RUSTIC_BISTABLE_HANDLE,
    RUSTIC_BISTABLE_SLIDE,
    RUSTIC_NAIL_LEVER,
    GLASS_ROTARY_SWITCH,
    GLASS_TOUCH_SWITCH
  };

  // -----------------------------------------------------------------------------------------------------------------
  // -- pulse switches
  // -----------------------------------------------------------------------------------------------------------------

  // Square machine pulse switch
  public static final BlockPulseSwitch INDUSTRIAL_BUTTON = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((5d/16),(5d/16),(0d/16),(11d/16),(11d/16),(1d/16)), null
  )).setRegistryName(new ResourceLocation(MODID, "industrial_button"));

  // Fenced round machine pulse switch
  public static final BlockPulseSwitch INDUSTRIAL_FENCED_BUTTON = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((5.5d/16),(5.5d/16),(0d/16),(10.5d/16),(10.5d/16),(0.5d/16)), null
  )).setRegistryName(new ResourceLocation(MODID, "industrial_fenced_button"));

  // Mechanical spring reset pull handle
  public static final BlockPulseSwitch INDUSTRIAL_PULL_HANDLE = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((5.5d/16),(5.5d/16),(0d/16),(10.5d/16),(10.5d/16),(2d/16)), null
  )).setRegistryName(new ResourceLocation(MODID, "industrial_pull_handle"));

  // Arrow target
  public static final BlockPulseSwitch ARROW_TARGET_SWITCH = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
    BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((5d/16),(5d/16),(0d/16),(11d/16),(11d/16),(1d/16)), null
  )).setRegistryName(new ResourceLocation(MODID, "arrow_target"));

  // Mechanical spring reset push button
  public static final BlockPulseSwitch INDUSTRIAL_FOOT_BUTTON = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((5.5d/16),(0.5d/16),(0d/16),(10.5d/16),(5.0d/16),(4d/16)), null
  )).setRegistryName(new ResourceLocation(MODID, "industrial_foot_button"));

  // Retro double pole switch
  public static final BlockPulseSwitch INDUSTRIAL_DOUBLE_POLE_BUTTON = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((7d/16),(6d/16),(0d/16),(12d/16),(10d/16),(4d/16)), null
  )).setRegistryName(new ResourceLocation(MODID, "industrial_double_pole_button"));

  // Old fancy (golden decorated) button
  public static final BlockPulseSwitch OLDFANCY_BUTTON = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1.5d/16)), null
  )).setRegistryName(new ResourceLocation(MODID, "oldfancy_button"));

  // Old fancy (golden decorated) chain pulse switch
  public static final BlockPulseSwitch OLDFANCY_SPRING_RESET_CHAIN = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6.5d/16),(4.8d/16),(0d/16),(9.5d/16),(13d/16),(4d/16)),
    new AxisAlignedBB((6.5d/16),(3.8d/16),(0d/16),(9.5d/16),(12d/16),(4d/16))
  )).setRegistryName(new ResourceLocation(MODID, "oldfancy_spring_reset_chain"));

  // Old fancy (golden decorated) crank
  public static final BlockPulseSwitch OLDFANCY_SPRING_RESET_HANDLE = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6.5d/16),(4.8d/16),(0d/16),(9.5d/16),(13d/16),(4d/16)),
    new AxisAlignedBB((6.5d/16),(3.8d/16),(0d/16),(9.5d/16),(12d/16),(4d/16))
  )).setRegistryName(new ResourceLocation(MODID, "oldfancy_spring_reset_handle"));

  // Old fancy (golden decorated) tiny button
  public static final BlockPulseSwitch OLDFANCY_SMALL_BUTTON = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((7d/16),(7d/16),(0d/16),(9d/16),(9d/16),(1.5d/16)), null
  )).setRegistryName(new ResourceLocation(MODID, "oldfancy_small_button"));

  // Rustic button 1
  public static final BlockPulseSwitch RUSTIC_BUTTON = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(2.5d/16)), null
  )).setRegistryName(new ResourceLocation(MODID, "rustic_button"));

  // Rustic button 2 (bolted)
  public static final BlockPulseSwitch RUSTIC_SMALL_BUTTON = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(2.5d/16)), null
  )).setRegistryName(new ResourceLocation(MODID, "rustic_small_button"));

  // Rustic button 3 (pull chain)
  public static final BlockPulseSwitch RUSTIC_SPRING_RESET_CHAIN = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6d/16),(6.5d/16),(0d/16),(10d/16),(14.5d/16),(2.5d/16)), null
  )).setRegistryName(new ResourceLocation(MODID, "rustic_spring_reset_chain"));

  // Rustic button 4 (weight reset pull chain)
  public static final BlockPulseSwitch RUSTIC_WEIGHT_RESET_CHAIN = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((3d/16),(5.5d/16),(0d/16),(14d/16),(15d/16),(2.5d/16)), null
  )).setRegistryName(new ResourceLocation(MODID, "rustic_weight_reset_chain"));

  // Rustic button 5 (pull handle)
  public static final BlockPulseSwitch RUSTIC_SPRING_RESET_PULL_HANDLE = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((3.5d/16),(6.0d/16),(0d/16), (12.5d/16),(11.0d/16),(4.5d/16)), null
  )).setRegistryName(new ResourceLocation(MODID, "rustic_spring_reset_pull_handle"));

  // Rustic button 6 (handle)
  public static final BlockPulseSwitch RUSTIC_SPRING_RESET_PUSH_HANDLE = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6.0d/16),(5.0d/16),(0d/16), (10.0d/16),(12.0d/16),(3.0d/16)), null
  )).setRegistryName(new ResourceLocation(MODID, "rustic_spring_reset_push_handle"));

  // Rustic button 7 (pull nail)
  public static final BlockPulseSwitch RUSTIC_NAIL_BUTTON = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6.0d/16),(7.0d/16),(0d/16), (9.0d/16),(10.0d/16),(3.0d/16)), null
  )).setRegistryName(new ResourceLocation(MODID, "rustic_nail_button"));

  // Thin star shaped glass button
  public static final BlockPulseSwitch GLASS_BUTTON = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
    BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|BlockSwitch.SWITCH_CONFIG_FAINT_LIGHTSOURCE|
    BlockSwitch.SWITCH_CONFIG_COLOR_TINT_SUPPORT|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((5.5d/16),(5.5d/16),(0d/16),(10.5d/16),(10.5d/16),(0.5d/16)), null
  )).setRegistryName(new ResourceLocation(MODID, "glass_button"));

  // Thin small star shaped glass button
  public static final BlockPulseSwitch GLASS_SMALL_BUTTON = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
    BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|BlockSwitch.SWITCH_CONFIG_FAINT_LIGHTSOURCE|
    BlockSwitch.SWITCH_CONFIG_COLOR_TINT_SUPPORT|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((5.5d/16),(5.5d/16),(0d/16),(10.5d/16),(10.5d/16),(0.5d/16)), null
  )).setRegistryName(new ResourceLocation(MODID, "glass_small_button"));

  // Glass touch button
  public static final BlockPulseSwitch GLASS_TOUCH_BUTTON = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
    BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|BlockSwitch.SWITCH_CONFIG_FAINT_LIGHTSOURCE|
    BlockSwitch.SWITCH_CONFIG_COLOR_TINT_SUPPORT|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((5.5d/16),(5.5d/16),(0d/16),(10.5d/16),(10.5d/16),(0.5d/16)), null
  )).setRegistryName(new ResourceLocation(MODID, "glass_touch_button"));

  public static final BlockPulseSwitch pulse_switches[] = {
    INDUSTRIAL_BUTTON,
    INDUSTRIAL_FENCED_BUTTON,
    INDUSTRIAL_PULL_HANDLE,
    ARROW_TARGET_SWITCH,
    INDUSTRIAL_FOOT_BUTTON,
    INDUSTRIAL_DOUBLE_POLE_BUTTON,
    OLDFANCY_BUTTON,
    OLDFANCY_SPRING_RESET_CHAIN,
    OLDFANCY_SPRING_RESET_HANDLE,
    OLDFANCY_SMALL_BUTTON,
    RUSTIC_BUTTON,
    RUSTIC_SMALL_BUTTON,
    RUSTIC_SPRING_RESET_CHAIN,
    RUSTIC_WEIGHT_RESET_CHAIN,
    RUSTIC_SPRING_RESET_PULL_HANDLE,
    RUSTIC_SPRING_RESET_PUSH_HANDLE,
    RUSTIC_NAIL_BUTTON,
    GLASS_BUTTON,
    GLASS_SMALL_BUTTON,
    GLASS_TOUCH_BUTTON
  };

  // -----------------------------------------------------------------------------------------------------------------
  // -- contact switches
  // -----------------------------------------------------------------------------------------------------------------

  // Door contact mat
  public static final BlockContactMat INDUSTRIAL_DOOR_CONTACT_MAT = (BlockContactMat)(new BlockContactMat(
    BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_DATA_WEAK|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((1d/16),(0.0d/16),(0d/16), (15d/16),(0.5d/16),(13d/16)), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_door_contact_mat"));

  // Sensitive full size contact mat
  public static final BlockContactMat INDUSTRIAL_CONTACT_MAT = (BlockContactMat)(new BlockContactMat(
    BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((0d/16),(0.0d/16),(0d/16), (16d/16),(0.5d/16),(16d/16)), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_contact_mat"));

  // Industrial shock sensor contact mat
  public static final BlockContactMat INDUSTRIAL_SHOCK_SENSITIVE_CONTACT_MAT = (BlockContactMat)(new BlockContactMat(
    BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_SHOCK_SENSITIVE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((0d/16),(0.0d/16),(0d/16), (16d/16),(0.5d/16),(16d/16)), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_shock_sensitive_contact_mat"));

  // Industrial trap door switch (shock vibration sensitive)
  public static final BlockTrapdoorSwitch INDUSTRIAL_SHOCK_SENSITIVE_TRAPDOOR = (BlockTrapdoorSwitch)(new BlockTrapdoorSwitch(
    BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_NOT_PASSABLE|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_SHOCK_SENSITIVE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((0d/16),(15.6d/16),(0d/16), (16d/16),(16d/16),(16.0d/16)),
    new AxisAlignedBB((0d/16),( 2.0d/16),(0d/16), (16d/16),(16d/16),( 0.1d/16)),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 3.0f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.5f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_shock_sensitive_trapdoor"));

  // Industrial trap door switch (high sensitive shock vibration sensitive)
  public static final BlockTrapdoorSwitch INDUSTRIAL_HIGH_SENSITIVE_TRAPDOOR = (BlockTrapdoorSwitch)(new BlockTrapdoorSwitch(
    BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_NOT_PASSABLE|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_SHOCK_SENSITIVE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    BlockSwitch.SWITCH_CONFIG_HIGH_SENSITIVE,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((0d/16),(15.6d/16),(0d/16), (16d/16),(16d/16),(16.0d/16)),
    new AxisAlignedBB((0d/16),( 2.0d/16),(0d/16), (16d/16),(16d/16),( 0.1d/16)),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.5f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.0f)
    // ModAuxiliaries.RsMaterials.MATERIAL_TRAPDOORSWITCH
  )).setRegistryName(new ResourceLocation(MODID, "industrial_high_sensitive_trapdoor"));

  // Industrial trap door switch (item trap door)
  public static final BlockTrapdoorSwitch INDUSTRIAL_FALLTHROUGH_DETECTOR = (BlockTrapdoorSwitch)(new BlockTrapdoorSwitch(
    BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
    BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((0d/16),(12.6d/16),(0d/16), (16d/16),(13d/16),(16.0d/16)),
    new AxisAlignedBB((0d/16),(12.6d/16),(0d/16), (16d/16),(13d/16),(16.0d/16)),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.05f, 2.5f),
    null
  )).setRegistryName(new ResourceLocation(MODID, "industrial_fallthrough_detector"));

  // Rustic door contact mat
  public static final BlockContactMat RUSTIC_DOOR_CONTACT_PLATE = (BlockContactMat)(new BlockContactMat(
    BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_DATA_WEAK|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((1d/16),(0.0d/16),(0d/16), (15d/16),(0.5d/16),(12.5d/16)),
    new AxisAlignedBB((1d/16),(0.0d/16),(0d/16), (15d/16),(0.2d/16),(12.5d/16)),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.5f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.0f)
  )).setRegistryName(new ResourceLocation(MODID, "rustic_door_contact_plate"));

  // Rustic full-size contact plate
  public static final BlockContactMat RUSTIC_CONTACT_PLATE = (BlockContactMat)(new BlockContactMat(
    BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((0d/16),(0.0d/16),(0d/16), (16d/16),(0.5d/16),(16d/16)),
    new AxisAlignedBB((0d/16),(0.0d/16),(0d/16), (16d/16),(0.2d/16),(16d/16)),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.5f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.0f)
  )).setRegistryName(new ResourceLocation(MODID, "rustic_contact_plate"));

  // Rustic shock sensor plate
  public static final BlockContactMat RUSTIC_SHOCK_SENSITIVE_CONTACT_PLATE = (BlockContactMat)(new BlockContactMat(
    BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_SHOCK_SENSITIVE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((0d/16),(0.0d/16),(0d/16), (16d/16),(0.5d/16),(16d/16)),
    new AxisAlignedBB((0d/16),(0.0d/16),(0d/16), (16d/16),(0.2d/16),(16d/16)),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.5f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.0f)
  )).setRegistryName(new ResourceLocation(MODID, "rustic_shock_sensitive_plate"));

  // Rustic trap door switch (shock vibration sensitive)
  public static final BlockTrapdoorSwitch RUSTIC_SHOCK_SENSITIVE_TRAPDOOR = (BlockTrapdoorSwitch)(new BlockTrapdoorSwitch(
    BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_NOT_PASSABLE|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_SHOCK_SENSITIVE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((0d/16),(15.6d/16),(0d/16), (16d/16),(16d/16),(16.0d/16)),
    new AxisAlignedBB((0d/16),( 2.0d/16),(0d/16), (16d/16),(16d/16),( 0.1d/16)),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.5f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.0f)
  )).setRegistryName(new ResourceLocation(MODID, "rustic_shock_sensitive_trapdoor"));

  // Rustic trap door switch (high sensitive shock vibration sensitive)
  public static final BlockTrapdoorSwitch RUSTIC_HIGH_SENSITIVE_TRAPDOOR = (BlockTrapdoorSwitch)(new BlockTrapdoorSwitch(
    BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_NOT_PASSABLE|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_SHOCK_SENSITIVE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_HIGH_SENSITIVE|
    BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((0d/16),(15.6d/16),(0d/16), (16d/16),(16d/16),(16.0d/16)),
    new AxisAlignedBB((0d/16),( 2.0d/16),(0d/16), (16d/16),(16d/16),( 0.1d/16)),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.5f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.0f)
    //ModAuxiliaries.RsMaterials.MATERIAL_TRAPDOORSWITCH
  )).setRegistryName(new ResourceLocation(MODID, "rustic_high_sensitive_trapdoor"));

  // Rustic trap door switch (item trap door)
  public static final BlockTrapdoorSwitch RUSTIC_FALLTHROUGH_DETECTOR = (BlockTrapdoorSwitch)(new BlockTrapdoorSwitch(
    BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
    BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((0d/16),(12.6d/16),(0d/16), (16d/16),(13d/16),(16.0d/16)),
    new AxisAlignedBB((0d/16),(12.6d/16),(0d/16), (16d/16),(13d/16),(16.0d/16)),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.5f),
    null // new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.0f)
    //ModAuxiliaries.RsMaterials.MATERIAL_TRAPDOORSWITCH
  )).setRegistryName(new ResourceLocation(MODID, "rustic_fallthrough_detector"));

  // Glass door plate
  public static final BlockContactMat GLASS_DOOR_CONTACT_MAT = (BlockContactMat)(new BlockContactMat(
    BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|
    BlockSwitch.SWITCH_CONFIG_COLOR_TINT_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((0d/16),(0.0d/16),(0d/16), (16d/16),(0.25d/16),(16d/16)), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "glass_door_contact_mat"));

  // Glass plate
  public static final BlockContactMat GLASS_CONTACT_MAT = (BlockContactMat)(new BlockContactMat(
    BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|
    BlockSwitch.SWITCH_CONFIG_COLOR_TINT_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((0d/16),(0.0d/16),(0d/16), (16d/16),(0.25d/16),(16d/16)), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "glass_contact_mat"));

  // Yellow power plant
  public static final BlockPowerPlant YELLOW_POWER_PLANT = (BlockPowerPlant)(new BlockPowerPlant(
    BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((5d/16),(0.0d/16),(5d/16), (11d/16),(9d/16),(11d/16)), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_GRASS_BREAK, 0.09f, 3.6f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_GRASS_BREAK, 0.04f, 3.0f)
    // ModAuxiliaries.RsMaterials.MATERIAL_PLANT
  )).setRegistryName(new ResourceLocation(MODID, "yellow_power_plant"));

  // Red power plant
  public static final BlockPowerPlant RED_POWER_PLANT = (BlockPowerPlant)(new BlockPowerPlant(
    BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((5d/16),(0.0d/16),(5d/16), (11d/16),(9d/16),(11d/16)), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_GRASS_BREAK, 0.09f, 3.6f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_GRASS_BREAK, 0.04f, 3.0f)
    // ModAuxiliaries.RsMaterials.MATERIAL_PLANT
  )).setRegistryName(new ResourceLocation(MODID, "red_power_plant"));

  public static final BlockContactSwitch contact_switches[] = {
    INDUSTRIAL_DOOR_CONTACT_MAT,
    INDUSTRIAL_CONTACT_MAT,
    INDUSTRIAL_SHOCK_SENSITIVE_CONTACT_MAT,
    INDUSTRIAL_SHOCK_SENSITIVE_TRAPDOOR,
    INDUSTRIAL_HIGH_SENSITIVE_TRAPDOOR,
    INDUSTRIAL_FALLTHROUGH_DETECTOR,
    RUSTIC_DOOR_CONTACT_PLATE,
    RUSTIC_CONTACT_PLATE,
    RUSTIC_SHOCK_SENSITIVE_CONTACT_PLATE,
    RUSTIC_SHOCK_SENSITIVE_TRAPDOOR,
    RUSTIC_HIGH_SENSITIVE_TRAPDOOR,
    RUSTIC_FALLTHROUGH_DETECTOR,
    GLASS_DOOR_CONTACT_MAT,
    GLASS_CONTACT_MAT,
    YELLOW_POWER_PLANT,
    RED_POWER_PLANT
  };

  // -----------------------------------------------------------------------------------------------------------------
  // -- auto switches - entity detectors
  // -----------------------------------------------------------------------------------------------------------------

  // Infrared motion_sensor
  public static final BlockEntityDetectorSwitch INDUSTRIAL_ENTITY_DETECTOR = (BlockEntityDetectorSwitch)(new BlockEntityDetectorSwitch(
    BlockSwitch.SWITCH_CONFIG_SENSOR_VOLUME|
    BlockSwitch.SWITCH_CONFIG_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_entity_detector"));

  // Linear laser motion sensor
  public static final BlockEntityDetectorSwitch INDUSTRIAL_LINEAR_ENTITY_DETECTOR = (BlockEntityDetectorSwitch)(new BlockEntityDetectorSwitch(
    BlockSwitch.SWITCH_CONFIG_SENSOR_LINEAR|
    BlockSwitch.SWITCH_CONFIG_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_linear_entity_detector"));

  // Glass infrared motion sensor
  public static final BlockEntityDetectorSwitch GLASS_ENTITY_DETECTOR = (BlockEntityDetectorSwitch)(new BlockEntityDetectorSwitch(
    BlockSwitch.SWITCH_CONFIG_SENSOR_VOLUME|
    BlockSwitch.SWITCH_CONFIG_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((5.5d/16),(5.5d/16),(0d/16),(10.5d/16),(10.5d/16),(0.1d/16)), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "glass_entity_detector"));

  // Glass laser motion sensor
  public static final BlockEntityDetectorSwitch GLASS_LINEAR_ENTITY_DETECTOR = (BlockEntityDetectorSwitch)(new BlockEntityDetectorSwitch(
    BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_SENSOR_LINEAR|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((5.5d/16),(5.5d/16),(0d/16),(10.5d/16),(10.5d/16),(0.1d/16)), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "glass_linear_entity_detector"));

  public static final BlockEntityDetectorSwitch detector_auto_switches[] = {
    INDUSTRIAL_ENTITY_DETECTOR,
    INDUSTRIAL_LINEAR_ENTITY_DETECTOR,
    GLASS_ENTITY_DETECTOR,
    GLASS_LINEAR_ENTITY_DETECTOR
  };

  // -----------------------------------------------------------------------------------------------------------------
  // -- auto switches - environmental sensors
  // -----------------------------------------------------------------------------------------------------------------

  // Local light sensor
  public static final BlockEnvironmentalSensorSwitch INDUSTRIAL_LIGHT_SENSOR = (BlockEnvironmentalSensorSwitch)(new BlockEnvironmentalSensorSwitch(
    BlockSwitch.SWITCH_CONFIG_SENSOR_LIGHT|
    BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_light_sensor"));

  // Rain sensor switch
  public static final BlockEnvironmentalSensorSwitch INDUSTRIAL_RAIN_SENSOR = (BlockEnvironmentalSensorSwitch)(new BlockEnvironmentalSensorSwitch(
    BlockSwitch.SWITCH_CONFIG_SENSOR_RAIN|
    BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_rain_sensor"));

  // Lightning sensor switch
  public static final BlockEnvironmentalSensorSwitch INDUSTRIAL_LIGHTNING_SENSOR = (BlockEnvironmentalSensorSwitch)(new BlockEnvironmentalSensorSwitch(
    BlockSwitch.SWITCH_CONFIG_SENSOR_LIGHTNING|
    BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_lightning_sensor"));

  public static final BlockEnvironmentalSensorSwitch environmental_auto_switches[] = {
    INDUSTRIAL_LIGHT_SENSOR,
    INDUSTRIAL_RAIN_SENSOR,
    INDUSTRIAL_LIGHTNING_SENSOR
  };

  // -----------------------------------------------------------------------------------------------------------------
  // -- auto switches - day timers
  // -----------------------------------------------------------------------------------------------------------------

  // Day time switch
  public static final BlockDayTimerSwitch INDUSTRIAL_DAY_TIMER = (BlockDayTimerSwitch)(new BlockDayTimerSwitch(
    BlockSwitch.SWITCH_CONFIG_TIMER_DAYTIME|
    BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_day_timer"));

  // Glass Day time switch
  public static final BlockDayTimerSwitch GLASS_DAY_TIMER = (BlockDayTimerSwitch)(new BlockDayTimerSwitch(
    BlockSwitch.SWITCH_CONFIG_TIMER_DAYTIME|
    BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((5.5d/16),(5.5d/16),(0d/16),(10.5d/16),(10.5d/16),(0.1d/16)), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "glass_day_timer"));

  public static final BlockDayTimerSwitch daytimer_auto_switches[] = {
    INDUSTRIAL_DAY_TIMER,
    GLASS_DAY_TIMER
  };

  // -----------------------------------------------------------------------------------------------------------------
  // -- auto switches - interval timers
  // -----------------------------------------------------------------------------------------------------------------

  // Interval signal timer
  public static final BlockIntervalTimerSwitch INDUSTRIAL_INTERVAL_TIMER = (BlockIntervalTimerSwitch)(new BlockIntervalTimerSwitch(
    BlockSwitch.SWITCH_CONFIG_TIMER_INTERVAL|
    BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_interval_timer"));

  // Glass interval signal timer
  public static final BlockIntervalTimerSwitch GLASS_INTERVAL_TIMER = (BlockIntervalTimerSwitch)(new BlockIntervalTimerSwitch(
    BlockSwitch.SWITCH_CONFIG_TIMER_INTERVAL|
    BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((5.5d/16),(5.5d/16),(0d/16),(10.5d/16),(10.5d/16),(0.1d/16)), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "glass_interval_timer"));

  public static final BlockIntervalTimerSwitch interval_timer_auto_switches[] = {
    INDUSTRIAL_INTERVAL_TIMER,
    GLASS_INTERVAL_TIMER
  };

  // -----------------------------------------------------------------------------------------------------------------
  // -- Block observer and inspection switches
  // -----------------------------------------------------------------------------------------------------------------

  // Uni-directional block detector switch
  public static final BlockObserverSwitch INDUSTRIAL_BLOCK_DETECTOR = (BlockObserverSwitch)(new BlockObserverSwitch(
    BlockSwitch.SWITCH_CONFIG_SENSOR_BLOCKDETECT|BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_NOT_PASSABLE|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    BlockSwitch.SWITCH_DATA_SIDE_ENABLED_BOTTOM|BlockSwitch.SWITCH_DATA_SIDE_ENABLED_TOP|
    BlockSwitch.SWITCH_DATA_SIDE_ENABLED_FRONT|BlockSwitch.SWITCH_DATA_SIDE_ENABLED_LEFT|
    BlockSwitch.SWITCH_DATA_SIDE_ENABLED_RIGHT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB(0.5/16,0.5/16,0.5/16, 15.5/16,15.5/16,13.5/16), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.2f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.2f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_block_detector"));

  public static final BlockSwitch observer_auto_switches[] = {
    INDUSTRIAL_BLOCK_DETECTOR
  };

  // -----------------------------------------------------------------------------------------------------------------
  // -- link receiver switches (rx relays)
  // -----------------------------------------------------------------------------------------------------------------

  // Industrial bistable link relay receiver switch
  public static final BlockLinkReceiverSwitch INDUSTRIAL_SWITCHLINK_RECEIVER = (BlockLinkReceiverSwitch)(new BlockLinkReceiverSwitch(
    BlockSwitch.SWITCH_CONFIG_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_BISTABLE|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_DATA_WEAK|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
    new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.9f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.7f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_switchlink_receiver"));

  // Industrial full block bistable link relay receiver switch
  public static final BlockLinkReceiverSwitch INDUSTRIAL_SWITCHLINK_CASED_RECEIVER = (BlockLinkReceiverSwitch)(new BlockLinkReceiverSwitch(
    BlockSwitch.RSBLOCK_CONFIG_FULLCUBE|BlockSwitch.SWITCH_CONFIG_NOT_PASSABLE|
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_DATA_WEAK|
    BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_DATA_SIDE_ENABLED_ALL|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((0),(0),(0),(1),(1),(1)),
    new AxisAlignedBB((0),(0),(0),(1),(1),(1)),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.9f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.7f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_switchlink_cased_receiver"));

  // Industrial pulse link relay receiver switch
  public static final BlockLinkReceiverSwitch INDUSTRIAL_SWITCHLINK_PULSE_RECEIVER = (BlockLinkReceiverSwitch)(new BlockLinkReceiverSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
    BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
    new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.9f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.7f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_switchlink_pulse_receiver"));

  // Industrial full block pulse link relay receiver switch
  public static final BlockLinkReceiverSwitch INDUSTRIAL_SWITCHLINK_CASED_PULSE_RECEIVER = (BlockLinkReceiverSwitch)(new BlockLinkReceiverSwitch(
    BlockSwitch.RSBLOCK_CONFIG_FULLCUBE|BlockSwitch.SWITCH_CONFIG_NOT_PASSABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_DATA_WEAK|
    BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_DATA_SIDE_ENABLED_ALL|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((0),(0),(0),(1),(1),(1)),
    new AxisAlignedBB((0),(0),(0),(1),(1),(1)),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.9f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.7f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_switchlink_cased_pulse_receiver"));

  public static final BlockLinkReceiverSwitch link_receiver_switches[] = {
    INDUSTRIAL_SWITCHLINK_RECEIVER,
    INDUSTRIAL_SWITCHLINK_CASED_RECEIVER,
    INDUSTRIAL_SWITCHLINK_PULSE_RECEIVER,
    INDUSTRIAL_SWITCHLINK_CASED_PULSE_RECEIVER
  };

  // -----------------------------------------------------------------------------------------------------------------
  // -- link relay switches (tx relays)
  // -----------------------------------------------------------------------------------------------------------------

  // Industrial bistable link relay
  public static final BlockLinkRelaySwitch INDUSTRIAL_SWITCHLINK_RELAY = (BlockLinkRelaySwitch)(new BlockLinkRelaySwitch(
    BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_LINK_RELAY|
    BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_DATA_WEAK|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
    new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.9f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.7f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_switchlink_relay"));

  // Industrial pulse link relay
  public static final BlockLinkRelaySwitch INDUSTRIAL_SWITCHLINK_PULSE_RELAY = (BlockLinkRelaySwitch)(new BlockLinkRelaySwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_LINK_RELAY|
    BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_DATA_WEAK|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
    new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.9f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.7f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_switchlink_pulse_relay"));

  public static final BlockLinkRelaySwitch link_relay_switches[] = {
    INDUSTRIAL_SWITCHLINK_RELAY,
    INDUSTRIAL_SWITCHLINK_PULSE_RELAY
  };


  // -----------------------------------------------------------------------------------------------------------------
  // -- Seismic sensor surge switches
  // -----------------------------------------------------------------------------------------------------------------

  // Bistable industrial knock surge detctor
  public static final BlockKnockBistableSwitch INDUSTRIAL_BISTABLE_KNOCK_SWITCH = (BlockKnockBistableSwitch)(new BlockKnockBistableSwitch(
    BlockSwitch.RSBLOCK_CONFIG_OPOSITE_PLACEMENT|BlockSwitch.RSBLOCK_CONFIG_FULLCUBE|
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    BlockSwitch.SWITCH_CONFIG_NOT_PASSABLE|BlockSwitch.SWITCH_DATA_SIDE_ENABLED_ALL,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB(0.5/16,0.5/16,0.5/16, 15.5/16,15.5/16,15.5/16), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.2f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.2f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_knock_switch"));

  public static final BlockKnockBistableSwitch bistable_knock_switches[] = {
    INDUSTRIAL_BISTABLE_KNOCK_SWITCH
  };

  // -----------------------------------------------------------------------------------------------------------------

  // Pulse industrial knock surge detctor
  public static final BlockKnockPulseSwitch INDUSTRIAL_PULSE_KNOCK_SWITCH = (BlockKnockPulseSwitch)(new BlockKnockPulseSwitch(
    BlockSwitch.RSBLOCK_CONFIG_OPOSITE_PLACEMENT|BlockSwitch.RSBLOCK_CONFIG_FULLCUBE|
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    BlockSwitch.SWITCH_CONFIG_NOT_PASSABLE|BlockSwitch.SWITCH_DATA_SIDE_ENABLED_ALL,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB(0.5/16,0.5/16,0.5/16, 15.5/16,15.5/16,15.5/16), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.2f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.2f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_knock_button"));

  public static final BlockKnockPulseSwitch pulse_knock_switches[] = {
    INDUSTRIAL_PULSE_KNOCK_SWITCH
  };

  // -----------------------------------------------------------------------------------------------------------------
  // -- Manual dimmers
  // -----------------------------------------------------------------------------------------------------------------

  // Industrial pulse link relay
  public static final BlockDimmerSwitch INDUSTRIAL_DIMMER = (BlockDimmerSwitch)(new BlockDimmerSwitch(
    BlockSwitch.SWITCH_CONFIG_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    new AxisAlignedBB((5d/16),(1.5d/16),(0d/16), (11d/16),(14.25d/16),(0.60d/16)),
    null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.9f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.7f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_dimmer"));

  public static final BlockDimmerSwitch dimmer_switches[] = {
    INDUSTRIAL_DIMMER
  };

  // -----------------------------------------------------------------------------------------------------------------
  // -- sensitive glass
  // -----------------------------------------------------------------------------------------------------------------

  private static final Block.Properties SENSITIVE_GLASS_BLOCK_PROPERTIES = (Block.Properties
    .create(Material.REDSTONE_LIGHT, MaterialColor.IRON)
    .hardnessAndResistance(0.35f, 15f)
    .sound(SoundType.METAL)
    .harvestLevel(0)
    .lightValue(15)
    .func_226896_b_() // notsolid?
  );

  public static final BlockSensitiveGlass SENSITIVE_GLASS_BLOCK = (BlockSensitiveGlass)(new BlockSensitiveGlass(
    SENSITIVE_GLASS_BLOCK_PROPERTIES
  )).setRegistryName(new ResourceLocation(MODID, "sensitive_glass_block"));

  public static final BlockSensitiveGlassColored WHITE_SENSITIVE_GLASS_BLOCK = (BlockSensitiveGlassColored)(new BlockSensitiveGlassColored(
    SENSITIVE_GLASS_BLOCK_PROPERTIES, ModAuxiliaries.DyeColorFilters.WHITE // 0xf3f3f3
  )).setRegistryName(new ResourceLocation(MODID, "white_sensitiveglass"));

  public static final BlockSensitiveGlassColored RED_SENSITIVE_GLASS_BLOCK = (BlockSensitiveGlassColored)(new BlockSensitiveGlassColored(
    SENSITIVE_GLASS_BLOCK_PROPERTIES, ModAuxiliaries.DyeColorFilters.RED // 0xB02E26
  )).setRegistryName(new ResourceLocation(MODID, "red_sensitiveglass"));

  public static final BlockSensitiveGlassColored GREEN_SENSITIVE_GLASS_BLOCK = (BlockSensitiveGlassColored)(new BlockSensitiveGlassColored(
    SENSITIVE_GLASS_BLOCK_PROPERTIES, ModAuxiliaries.DyeColorFilters.GREEN // 0x5E7C16
  )).setRegistryName(new ResourceLocation(MODID, "green_sensitiveglass"));

  public static final BlockSensitiveGlassColored BLUE_SENSITIVE_GLASS_BLOCK = (BlockSensitiveGlassColored)(new BlockSensitiveGlassColored(
    SENSITIVE_GLASS_BLOCK_PROPERTIES, ModAuxiliaries.DyeColorFilters.BLUE // 0x3C44AA
  )).setRegistryName(new ResourceLocation(MODID, "blue_sensitiveglass"));

  public static final BlockSensitiveGlassColored YELLOW_SENSITIVE_GLASS_BLOCK = (BlockSensitiveGlassColored)(new BlockSensitiveGlassColored(
    SENSITIVE_GLASS_BLOCK_PROPERTIES, ModAuxiliaries.DyeColorFilters.YELLOW // 0xFED83D
  )).setRegistryName(new ResourceLocation(MODID, "yellow_sensitiveglass"));

  public static final BlockSensitiveGlassColored ORANGE_SENSITIVE_GLASS_BLOCK = (BlockSensitiveGlassColored)(new BlockSensitiveGlassColored(
    SENSITIVE_GLASS_BLOCK_PROPERTIES, ModAuxiliaries.DyeColorFilters.ORANGE // 0xF9801D
  )).setRegistryName(new ResourceLocation(MODID, "orange_sensitiveglass"));

  public static final BlockSensitiveGlassColored MAGENTA_SENSITIVE_GLASS_BLOCK = (BlockSensitiveGlassColored)(new BlockSensitiveGlassColored(
    SENSITIVE_GLASS_BLOCK_PROPERTIES, ModAuxiliaries.DyeColorFilters.MAGENTA // 0xC74EBD
  )).setRegistryName(new ResourceLocation(MODID, "magenta_sensitiveglass"));

  public static final BlockSensitiveGlassColored LIGHTBLUE_SENSITIVE_GLASS_BLOCK = (BlockSensitiveGlassColored)(new BlockSensitiveGlassColored(
    SENSITIVE_GLASS_BLOCK_PROPERTIES, ModAuxiliaries.DyeColorFilters.LIGHTBLUE // 0x3AB3DA
  )).setRegistryName(new ResourceLocation(MODID, "lightblue_sensitiveglass"));

  public static final BlockSensitiveGlassColored LIME_SENSITIVE_GLASS_BLOCK = (BlockSensitiveGlassColored)(new BlockSensitiveGlassColored(
    SENSITIVE_GLASS_BLOCK_PROPERTIES, ModAuxiliaries.DyeColorFilters.LIME // 0x80C71F
  )).setRegistryName(new ResourceLocation(MODID, "lime_sensitiveglass"));

  public static final BlockSensitiveGlassColored PINK_SENSITIVE_GLASS_BLOCK = (BlockSensitiveGlassColored)(new BlockSensitiveGlassColored(
    SENSITIVE_GLASS_BLOCK_PROPERTIES, ModAuxiliaries.DyeColorFilters.PINK // 0xF38BAA
  )).setRegistryName(new ResourceLocation(MODID, "pink_sensitiveglass"));

  public static final BlockSensitiveGlassColored GRAY_SENSITIVE_GLASS_BLOCK = (BlockSensitiveGlassColored)(new BlockSensitiveGlassColored(
    SENSITIVE_GLASS_BLOCK_PROPERTIES, ModAuxiliaries.DyeColorFilters.GRAY // 0x474F52
  )).setRegistryName(new ResourceLocation(MODID, "gray_sensitiveglass"));

  public static final BlockSensitiveGlassColored LIGHTGRAY_SENSITIVE_GLASS_BLOCK = (BlockSensitiveGlassColored)(new BlockSensitiveGlassColored(
    SENSITIVE_GLASS_BLOCK_PROPERTIES, ModAuxiliaries.DyeColorFilters.SILVER // 0x9D9D97
  )).setRegistryName(new ResourceLocation(MODID, "lightgray_sensitiveglass"));

  public static final BlockSensitiveGlassColored CYAN_SENSITIVE_GLASS_BLOCK = (BlockSensitiveGlassColored)(new BlockSensitiveGlassColored(
    SENSITIVE_GLASS_BLOCK_PROPERTIES, ModAuxiliaries.DyeColorFilters.CYAN // 0x169C9C
  )).setRegistryName(new ResourceLocation(MODID, "cyan_sensitiveglass"));

  public static final BlockSensitiveGlassColored PURPLE_SENSITIVE_GLASS_BLOCK = (BlockSensitiveGlassColored)(new BlockSensitiveGlassColored(
    SENSITIVE_GLASS_BLOCK_PROPERTIES, ModAuxiliaries.DyeColorFilters.PURPLE // 0x8932B8
  )).setRegistryName(new ResourceLocation(MODID, "purple_sensitiveglass"));

  public static final BlockSensitiveGlassColored BROWN_SENSITIVE_GLASS_BLOCK = (BlockSensitiveGlassColored)(new BlockSensitiveGlassColored(
    SENSITIVE_GLASS_BLOCK_PROPERTIES, ModAuxiliaries.DyeColorFilters.BROWN // 0x835432
  )).setRegistryName(new ResourceLocation(MODID, "brown_sensitiveglass"));

  public static final BlockSensitiveGlassColored BLACK_SENSITIVE_GLASS_BLOCK = (BlockSensitiveGlassColored)(new BlockSensitiveGlassColored(
    SENSITIVE_GLASS_BLOCK_PROPERTIES, ModAuxiliaries.DyeColorFilters.BLACK // 0x111111
  )).setRegistryName(new ResourceLocation(MODID, "black_sensitiveglass"));

  public static final BlockSensitiveGlass sensitive_glass[] = {
    SENSITIVE_GLASS_BLOCK,
    WHITE_SENSITIVE_GLASS_BLOCK,
    GREEN_SENSITIVE_GLASS_BLOCK,
    BLUE_SENSITIVE_GLASS_BLOCK,
    YELLOW_SENSITIVE_GLASS_BLOCK,
    ORANGE_SENSITIVE_GLASS_BLOCK,
    MAGENTA_SENSITIVE_GLASS_BLOCK,
    LIGHTBLUE_SENSITIVE_GLASS_BLOCK,
    LIME_SENSITIVE_GLASS_BLOCK,
    PINK_SENSITIVE_GLASS_BLOCK,
    GRAY_SENSITIVE_GLASS_BLOCK,
    LIGHTGRAY_SENSITIVE_GLASS_BLOCK,
    CYAN_SENSITIVE_GLASS_BLOCK,
    PURPLE_SENSITIVE_GLASS_BLOCK,
    BROWN_SENSITIVE_GLASS_BLOCK,
    BLACK_SENSITIVE_GLASS_BLOCK
  };

  // -----------------------------------------------------------------------------------------------------------------
  // -- Testing blocks
  // -----------------------------------------------------------------------------------------------------------------

  // Testing CUBE
  public static final BlockSwitch TESTING_QUBE = (BlockSwitch)(new BlockSwitch(
    BlockSwitch.SWITCH_CONFIG_NOT_PASSABLE|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    Block.Properties.create(Material.WOOL, MaterialColor.IRON).hardnessAndResistance(0.1f, 32000f).sound(SoundType.METAL),
    new AxisAlignedBB(0,0,0,1,1,1), null,
    null, null
  )).setRegistryName(new ResourceLocation(MODID, "qube"));

  // -----------------------------------------------------------------------------------------------------------------
  // -- All blocks
  // -----------------------------------------------------------------------------------------------------------------

  private static final ArrayList<Block> modBlocks;

  static {
    modBlocks = new ArrayList<Block>();
    modBlocks.addAll(Arrays.asList(gauges));
    modBlocks.addAll(Arrays.asList(indicators));
    modBlocks.addAll(Arrays.asList(bistable_switches));
    modBlocks.addAll(Arrays.asList(pulse_switches));
    modBlocks.addAll(Arrays.asList(contact_switches));
    modBlocks.addAll(Arrays.asList(detector_auto_switches));
    modBlocks.addAll(Arrays.asList(environmental_auto_switches));
    modBlocks.addAll(Arrays.asList(daytimer_auto_switches));
    modBlocks.addAll(Arrays.asList(interval_timer_auto_switches));
    modBlocks.addAll(Arrays.asList(observer_auto_switches));
    modBlocks.addAll(Arrays.asList(link_receiver_switches));
    modBlocks.addAll(Arrays.asList(link_relay_switches));
    modBlocks.addAll(Arrays.asList(bistable_knock_switches));
    modBlocks.addAll(Arrays.asList(pulse_knock_switches));
    modBlocks.addAll(Arrays.asList(dimmer_switches));
    modBlocks.addAll(Arrays.asList(sensitive_glass));
  }

  private static ArrayList<Block> devBlocks = new ArrayList<Block>();

  static {
    devBlocks.add(TESTING_QUBE);
  }

  //--------------------------------------------------------------------------------------------------------------------
  // Tile entities bound exclusively to the blocks above
  //--------------------------------------------------------------------------------------------------------------------

  public static final TileEntityType<?> TET_GAUGE = TileEntityType.Builder
    .create(BlockGauge.TileEntityGauge::new, ArrayUtils.addAll(gauges, indicators))
    .build(null)
    .setRegistryName(ModRsGauges.MODID, "te_gauge");

  public static final TileEntityType<?> TET_SWITCH = TileEntityType.Builder
    .create(BlockSwitch.TileEntitySwitch::new, bistable_switches) // pulse_switches, bistable_knock_switches, pulse_knock_switches -> any combinations cause an exception
    .build(null)
    .setRegistryName(ModRsGauges.MODID, "te_switch");

  public static final TileEntityType<?> TET_CONTACT_SWITCH = TileEntityType.Builder
    .create(BlockContactSwitch.TileEntityContactSwitch::new, contact_switches)
    .build(null)
    .setRegistryName(ModRsGauges.MODID, "te_contact_switch");

  public static final TileEntityType<?> TET_DETECTOR_SWITCH = TileEntityType.Builder
    .create(BlockEntityDetectorSwitch.TileEntityDetectorSwitch::new, detector_auto_switches)
    .build(null)
    .setRegistryName(ModRsGauges.MODID, "te_detector_switch");

  public static final TileEntityType<?> TET_ENVSENSOR_SWITCH = TileEntityType.Builder
    .create(BlockEnvironmentalSensorSwitch.TileEntityEnvironmentalSensorSwitch::new, environmental_auto_switches)
    .build(null)
    .setRegistryName(ModRsGauges.MODID, "te_envsensor_switch");

  public static final TileEntityType<?> TET_DAYTIMER_SWITCH = TileEntityType.Builder
    .create(BlockDayTimerSwitch.TileEntityDayTimerSwitch::new, daytimer_auto_switches)
    .build(null)
    .setRegistryName(ModRsGauges.MODID, "te_daytimer_switch");

  public static final TileEntityType<?> TET_TIMER_SWITCH = TileEntityType.Builder
    .create(BlockIntervalTimerSwitch.TileEntityIntervalTimerSwitch::new, interval_timer_auto_switches)
    .build(null)
    .setRegistryName(ModRsGauges.MODID, "te_timer_switch");

  public static final TileEntityType<?> TET_OBSERVER_SWITCH = TileEntityType.Builder
    .create(BlockObserverSwitch.TileEntityObserverSwitch::new, observer_auto_switches)
    .build(null)
    .setRegistryName(ModRsGauges.MODID, "te_observer_switch");

  private static final TileEntityType<?> tile_entity_types[] = {
    TET_GAUGE,
    TET_SWITCH,
    TET_CONTACT_SWITCH,
    TET_DETECTOR_SWITCH,
    TET_ENVSENSOR_SWITCH,
    TET_DAYTIMER_SWITCH,
    TET_TIMER_SWITCH,
    TET_OBSERVER_SWITCH
  };

  //--------------------------------------------------------------------------------------------------------------------
  // Items
  //--------------------------------------------------------------------------------------------------------------------

  private static Item.Properties default_item_properties()
  { return (new Item.Properties()).group(ModRsGauges.ITEMGROUP); }

  public static final ItemSwitchLinkPearl SWITCH_LINK_PEARL = (ItemSwitchLinkPearl)(new ItemSwitchLinkPearl(
    default_item_properties()
  ).setRegistryName(MODID, "switchlink_pearl"));


  private static final Item modItems[] = {
    SWITCH_LINK_PEARL,
  };

  //--------------------------------------------------------------------------------------------------------------------
  // Initialisation events
  //--------------------------------------------------------------------------------------------------------------------

  private static final ArrayList<Block> registeredBlocks;
  private static final ArrayList<Item> registeredItems;

  static {
    registeredBlocks = new ArrayList<Block>();
    registeredBlocks.addAll(modBlocks);
    registeredBlocks.addAll(devBlocks);
    registeredItems = new ArrayList<Item>();
    registeredItems.addAll(Arrays.asList(modItems));
    for(Block e:registeredBlocks) registeredItems.add(new ModBlockItem(e, (new ModBlockItem.Properties())).setRegistryName(e.getRegistryName()));
  }

  @Nonnull
  public static List<Block> getRegisteredBlocks()
  { return Collections.unmodifiableList(registeredBlocks); }

  @Nonnull
  public static List<Item> getRegisteredItems()
  { return Collections.unmodifiableList(registeredItems); }

  @Nonnull
  public static List<Block> getExperimentalBlocks()
  { return devBlocks; }

  public static final void registerBlocks(final RegistryEvent.Register<Block> event)
  {
    for(Block e:registeredBlocks) event.getRegistry().register(e);
    ModAuxiliaries.logInfo("Registered " + Integer.toString(registeredBlocks.size()) + " blocks.");
  }

  public static final void registerItems(final RegistryEvent.Register<Item> event)
  {
    for(Item e:registeredItems) event.getRegistry().register(e);
    ModAuxiliaries.logInfo("Registered " + Integer.toString(registeredItems.size()) + " items.");
  }

  public static final void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event)
  {
    for(final TileEntityType<?> e:tile_entity_types) event.getRegistry().register(e);
    ModAuxiliaries.logInfo("Registered " + Integer.toString(tile_entity_types.length) + " tile entities.");
  }

  public static final void processRegisteredContent()
  {}

  public static final void processContentClientSide(final FMLClientSetupEvent event)
  {
    // Block renderer selection
    for(Block block: getRegisteredBlocks()) {
      if(block instanceof RsBlock) {
        switch(((RsBlock)block).getRenderTypeHint()) {
          case CUTOUT:
            RenderTypeLookup.setRenderLayer(block, RenderType.func_228643_e_()/*cutout*/);
            break;
          case CUTOUT_MIPPED:
            RenderTypeLookup.setRenderLayer(block, RenderType.func_228641_d_()/*cutout_mipped*/);
            break;
          case TRANSLUCENT:
            RenderTypeLookup.setRenderLayer(block, RenderType.func_228645_f_()/*transparent*/);
            break;
          case SOLID:
            break;
        }
      }
    }
  }

}
