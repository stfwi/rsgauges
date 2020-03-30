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

import wile.rsgauges.detail.ModAuxiliaries;
import wile.rsgauges.blocks.*;
import wile.rsgauges.detail.ModResources;
import wile.rsgauges.items.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.PushReaction;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraft.util.SoundEvents;
import java.util.*;
import javax.annotation.Nonnull;


public class ModContent
{
  // -----------------------------------------------------------------------------------------------------------------
  // -- Internal constants, default block properties
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
  private static final Block.Properties GAUGE_METALLIC_BLOCK_PROPERTIES = (Block.Properties
    .create(METAL_MATERIAL, MaterialColor.IRON)
    .hardnessAndResistance(0.5f, 15f)
    .sound(SoundType.METAL)
    .harvestLevel(0)
    .notSolid()
  );
  private static final Block.Properties GAUGE_GLASS_BLOCK_PROPERTIES = (Block.Properties
    .create(METAL_MATERIAL, MaterialColor.IRON)
    .hardnessAndResistance(0.5f, 15f)
    .sound(SoundType.METAL)
    .harvestLevel(0)
    .notSolid()
  );
  private static final Block.Properties INDICATOR_METALLIC_BLOCK_PROPERTIES = (Block.Properties
    .create(GLASS_MATERIAL, MaterialColor.IRON)
    .hardnessAndResistance(0.5f, 15f)
    .sound(SoundType.METAL)
    .harvestLevel(0)
    .lightValue(3)
    .notSolid()
  );
  private static final Block.Properties INDICATOR_GLASS_BLOCK_PROPERTIES = (Block.Properties
    .create(GLASS_MATERIAL, MaterialColor.IRON)
    .hardnessAndResistance(0.5f, 15f)
    .sound(SoundType.METAL)
    .harvestLevel(0)
    .lightValue(3)
    .notSolid()
  );
  private static final Block.Properties ALARM_LAMP_BLOCK_PROPERTIES = (Block.Properties
    .create(GLASS_MATERIAL, MaterialColor.IRON)
    .hardnessAndResistance(0.5f, 15f)
    .sound(SoundType.METAL)
    .harvestLevel(0)
    .lightValue(8)
    .notSolid()
  );
  private static final Block.Properties SENSITIVE_GLASS_BLOCK_PROPERTIES = (Block.Properties
    .create(Material.REDSTONE_LIGHT, MaterialColor.IRON)
    .hardnessAndResistance(0.35f, 15f)
    .sound(SoundType.METAL)
    .harvestLevel(0)
    .lightValue(15)
    .notSolid()
  );
  private static final Block.Properties SWITCH_METALLIC_BLOCK_PROPERTIES = GAUGE_METALLIC_BLOCK_PROPERTIES;
  private static final Block.Properties SWITCH_GLASS_BLOCK_PROPERTIES = GAUGE_GLASS_BLOCK_PROPERTIES;

  private static final Block.Properties SWITCH_METALLIC_FAINT_LIGHT_EMITTING_BLOCK_PROPERTIES = (Block.Properties
    .create(METAL_MATERIAL, MaterialColor.IRON)
    .hardnessAndResistance(0.5f, 15f)
    .sound(SoundType.METAL)
    .harvestLevel(0)
    .lightValue(5)
  );

  // -----------------------------------------------------------------------------------------------------------------
  // -- industrual
  // -----------------------------------------------------------------------------------------------------------------

  // Contact lever switch
  public static final BlockBistableSwitch INDUSTRIAL_SMALL_LEVER = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(4,4,0,12,15,4),
    ModAuxiliaries.getPixeledAABB(4,1,0,12,12,4)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_small_lever"));

  // Mechanical lever
  public static final BlockBistableSwitch INDUSTRIAL_LEVER = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(5,4,0,11,15,5),
    ModAuxiliaries.getPixeledAABB(5,1,0,11,12,5)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_lever"));

  // Mechanical rotary lever
  public static final BlockBistableSwitch INDUSTRIAL_ROTARY_LEVER = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(1,4,0,12,12,6),
    ModAuxiliaries.getPixeledAABB(1,1,0,12,12,6)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_rotary_lever"));

  // Rotary machine switch
  public static final BlockBistableSwitch INDUSTRIAL_ROTARY_MACHINE_SWITCH = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null
  )).setRegistryName(new ResourceLocation(MODID, "industrial_rotary_machine_switch"));

  // Two-button machine switch
  public static final BlockBistableSwitch INDUSTRIAL_MACHINE_SWITCH = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null
  )).setRegistryName(new ResourceLocation(MODID, "industrial_machine_switch"));

  // ESTOP button
  public static final BlockBistableSwitch INDUSTRIAL_ESTOP_SWITCH = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE_OFF,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(5,5,0, 11, 11, 2.5),
    ModAuxiliaries.getPixeledAABB(5,5,0, 11, 11, 3.5)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_estop_switch"));

  // Hopper blocking switch
  public static final BlockBistableSwitch INDUSTRIAL_HOPPER_BLOCKING_SWITCH = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    BlockSwitch.SWITCH_DATA_WEAK,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(3,10,0, 13, 12, 6.7),
    ModAuxiliaries.getPixeledAABB(3,10,0, 13, 12, 3.7)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_hopper_switch"));

  // Square machine pulse switch
  public static final BlockPulseSwitch INDUSTRIAL_BUTTON = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(5,5,0, 11, 11, 2), null
  )).setRegistryName(new ResourceLocation(MODID, "industrial_button"));

  // Fenced round machine pulse switch
  public static final BlockPulseSwitch INDUSTRIAL_FENCED_BUTTON = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(5,5,0, 11, 11, 2), null
  )).setRegistryName(new ResourceLocation(MODID, "industrial_fenced_button"));

  // Retro double pole switch
  public static final BlockPulseSwitch INDUSTRIAL_DOUBLE_POLE_BUTTON = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 3),
    ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 2)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_double_pole_button"));

  // Mechanical spring reset push button
  public static final BlockPulseSwitch INDUSTRIAL_FOOT_BUTTON = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(5,3,0, 11, 7, 4), null
  )).setRegistryName(new ResourceLocation(MODID, "industrial_foot_button"));

  // Mechanical spring reset pull handle
  public static final BlockPulseSwitch INDUSTRIAL_PULL_HANDLE = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 2),
    ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 2)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_pull_handle"));

  // Manual dimmer
  public static final BlockDimmerSwitch INDUSTRIAL_DIMMER = (BlockDimmerSwitch)(new BlockDimmerSwitch(
    BlockSwitch.SWITCH_CONFIG_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(4,1,0, 12, 15, 2),
    null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.9f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.7f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_dimmer"));

  // Door contact mat
  public static final BlockContactMat INDUSTRIAL_DOOR_CONTACT_MAT = (BlockContactMat)(new BlockContactMat(
    BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_DATA_WEAK|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(1,0,0, 15, 1, 13), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_door_contact_mat"));

  // Sensitive full size contact mat
  public static final BlockContactMat INDUSTRIAL_CONTACT_MAT = (BlockContactMat)(new BlockContactMat(
    BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(0,0,0, 16, 1, 16), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_contact_mat"));

  // Industrial shock sensor contact mat
  public static final BlockContactMat INDUSTRIAL_SHOCK_SENSITIVE_CONTACT_MAT = (BlockContactMat)(new BlockContactMat(
    BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_SHOCK_SENSITIVE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(0,0,0, 16, 1, 16), null,
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
    ModAuxiliaries.getPixeledAABB(0,15.6,0, 16, 16, 16),
    ModAuxiliaries.getPixeledAABB(0,2,0, 16, 1, 0.1),
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
    ModAuxiliaries.getPixeledAABB(0,15.6,0, 16, 16, 16),
    ModAuxiliaries.getPixeledAABB(0,2,0, 16, 1, 0.1),
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
    ModAuxiliaries.getPixeledAABB(0.1,12.6,0.1, 15.9,13, 15.9),
    ModAuxiliaries.getPixeledAABB(0.1,12.6,0.1, 15.9,13, 15.9),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.05f, 2.5f),
    null
  )).setRegistryName(new ResourceLocation(MODID, "industrial_fallthrough_detector"));

  // Day time switch
  public static final BlockDayTimerSwitch INDUSTRIAL_DAY_TIMER = (BlockDayTimerSwitch)(new BlockDayTimerSwitch(
    BlockSwitch.SWITCH_CONFIG_TIMER_DAYTIME|
    BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_day_timer"));

  // Interval signal timer
  public static final BlockIntervalTimerSwitch INDUSTRIAL_INTERVAL_TIMER = (BlockIntervalTimerSwitch)(new BlockIntervalTimerSwitch(
    BlockSwitch.SWITCH_CONFIG_TIMER_INTERVAL|
    BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_interval_timer"));

  // Infrared motion_sensor
  public static final BlockEntityDetectorSwitch INDUSTRIAL_ENTITY_DETECTOR = (BlockEntityDetectorSwitch)(new BlockEntityDetectorSwitch(
    BlockSwitch.SWITCH_CONFIG_SENSOR_VOLUME|
    BlockSwitch.SWITCH_CONFIG_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(6,6,0, 10, 10, 1), null,
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
    ModAuxiliaries.getPixeledAABB(6,6,0, 10, 10, 1), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_linear_entity_detector"));

  // Local light sensor
  public static final BlockEnvironmentalSensorSwitch INDUSTRIAL_LIGHT_SENSOR = (BlockEnvironmentalSensorSwitch)(new BlockEnvironmentalSensorSwitch(
    BlockSwitch.SWITCH_CONFIG_SENSOR_LIGHT|
    BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
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
    ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
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
    ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_lightning_sensor"));

  // Comparator output level observing switch
  public static final BlockComparatorSwitch INDUSTRIAL_COMPARATOR_SWITCH = (BlockComparatorSwitch)(new BlockComparatorSwitch(
    BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(4,10,0, 12, 15, 1.5), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_comparator_switch"));

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
    ModAuxiliaries.getPixeledAABB(0.5,0.5,0.5, 15.5, 15.5, 15.5), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.2f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.2f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_block_detector"));

  // Industrial bistable link relay receiver switch
  public static final BlockLinkReceiverSwitch INDUSTRIAL_SWITCHLINK_RECEIVER = (BlockLinkReceiverSwitch)(new BlockLinkReceiverSwitch(
    BlockSwitch.SWITCH_CONFIG_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_BISTABLE|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_DATA_WEAK|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
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
    ModAuxiliaries.getPixeledAABB(0,0,0, 16, 16, 16), null,
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
    ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
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
    ModAuxiliaries.getPixeledAABB(0,0,0, 16, 16, 16), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.9f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.7f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_switchlink_cased_pulse_receiver"));

  // Industrial bistable link relay
  public static final BlockLinkRelaySwitch INDUSTRIAL_SWITCHLINK_RELAY = (BlockLinkRelaySwitch)(new BlockLinkRelaySwitch(
    BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_LINK_RELAY|
    BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_DATA_WEAK|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
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
    ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.9f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.7f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_switchlink_pulse_relay"));

  // Bistable industrial knock surge detctor
  public static final BlockKnockBistableSwitch INDUSTRIAL_BISTABLE_KNOCK_SWITCH = (BlockKnockBistableSwitch)(new BlockKnockBistableSwitch(
    BlockSwitch.RSBLOCK_CONFIG_OPOSITE_PLACEMENT|BlockSwitch.RSBLOCK_CONFIG_FULLCUBE|
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    BlockSwitch.SWITCH_CONFIG_NOT_PASSABLE|BlockSwitch.SWITCH_DATA_SIDE_ENABLED_ALL,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(0.5,0.5,0.5, 15.5, 15.5, 15.5), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.2f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.2f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_knock_switch"));

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
    ModAuxiliaries.getPixeledAABB(0.5,0.5,0.5, 15.5, 15.5, 15.5), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.2f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.2f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_knock_button"));

  public static final BlockGauge INDUSTRIAL_ANALOG_GAUGE = (BlockGauge)(new BlockGauge(
    BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT,
    GAUGE_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(2,2,0, 14,14,1)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_analog_angular_gauge"));

  public static final BlockGauge INDUSTRIAL_ANALOG_HORIZONTAL_GAUGE = (BlockGauge)(new BlockGauge(
    BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT,
    GAUGE_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(2,4,0, 14, 12, 1)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_analog_horizontal_gauge"));

  public static final BlockGauge INDUSTRIAL_VERTICAL_BAR_GAUGE = (BlockGauge)(new BlockGauge(
    BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT,
    GAUGE_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(4,2,0, 12, 14, 1)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_vertical_bar_gauge"));

  public static final BlockGauge INDUSTRIAL_SMALL_DIGITAL_GAUGE = (BlockGauge)(new BlockGauge(
    BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT,
    GAUGE_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(4,5,0, 12, 11, 1)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_small_digital_gauge"));

  public static final BlockGauge INDUSTRIAL_TUBE_GAUGE = (BlockGauge)(new BlockGauge(
    BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT,
    GAUGE_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(7,4,0, 9, 12, 3)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_tube_gauge"));

  public static final BlockIndicator INDUSTRIAL_ALARM_LAMP = (BlockIndicator)(new BlockIndicator(
    BlockGauge.GAUGE_DATA_BLINKING,
    ALARM_LAMP_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(6,6,0, 10, 10, 4)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_alarm_lamp"));

  public static final BlockIndicator INDUSTRIAL_ALARM_SIREN = (BlockIndicator)(new BlockIndicator(
    BlockGauge.GAUGE_DATA_BLINKING,
    INDICATOR_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(4,6.5,0, 11.5, 9.5, 4),
    new ModResources.BlockSoundEvent(ModResources.ALARM_SIREN_SOUND),
    null
  )).setRegistryName(new ResourceLocation(MODID, "industrial_alarm_siren"));

  // square LED
  public static final BlockIndicator INDUSTRIAL_GREEN_LED_INDICATOR = (BlockIndicator)(new BlockIndicator(
    (13<<BlockGauge.GAUGE_DATA_COLOR_SHIFT), // green
    INDICATOR_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_green_led"));

  public static final BlockIndicator INDUSTRIAL_YELLOW_LED_INDICATOR = (BlockIndicator)(new BlockIndicator(
    (5<<BlockGauge.GAUGE_DATA_COLOR_SHIFT), // yellow
    INDICATOR_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_yellow_led"));

  public static final BlockIndicator INDUSTRIAL_RED_LED_INDICATOR = (BlockIndicator)(new BlockIndicator(
    (0<<BlockGauge.GAUGE_DATA_COLOR_SHIFT), // red
    INDICATOR_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_red_led"));

  public static final BlockIndicator INDUSTRIAL_WHITE_LED_INDICATOR = (BlockIndicator)(new BlockIndicator(
    BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT, // default white
    INDICATOR_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_white_led"));

  public static final BlockIndicator INDUSTRIAL_GREEN_BLINK_LED_INDICATOR = (BlockIndicator)(new BlockIndicator(
    BlockGauge.GAUGE_DATA_BLINKING|(13<<BlockGauge.GAUGE_DATA_COLOR_SHIFT), // green
    INDICATOR_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_green_blinking_led"));

  public static final BlockIndicator INDUSTRIAL_YELLOW_BLINK_LED_INDICATOR = (BlockIndicator)(new BlockIndicator(
    BlockGauge.GAUGE_DATA_BLINKING|(5<<BlockGauge.GAUGE_DATA_COLOR_SHIFT), // yellow
    INDICATOR_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_yellow_blinking_led"));

  public static final BlockIndicator INDUSTRIAL_RED_BLINK_LED_INDICATOR = (BlockIndicator)(new BlockIndicator(
    BlockGauge.GAUGE_DATA_BLINKING|(0<<BlockGauge.GAUGE_DATA_COLOR_SHIFT), // red
    INDICATOR_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_red_blinking_led"));

  public static final BlockIndicator INDUSTRIAL_WHITE_BLINK_LED_INDICATOR = (BlockIndicator)(new BlockIndicator(
    BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT, // default white
    INDICATOR_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_white_blinking_led"));

  // -----------------------------------------------------------------------------------------------------------------
  // -- Rustic
  // -----------------------------------------------------------------------------------------------------------------

  // Rustic lever 1
  public static final BlockBistableSwitch RUSTIC_LEVER = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(6,5,0, 10.3, 15, 4.5),
    ModAuxiliaries.getPixeledAABB(6,2,0, 10.3, 11, 4.5)
  )).setRegistryName(new ResourceLocation(MODID, "rustic_lever"));

  // Rustic lever 2 (bolted)
  public static final BlockBistableSwitch RUSTIC_TWO_HINGE_LEVER = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(2,6,0, 14,13,4.5),
    ModAuxiliaries.getPixeledAABB(2,4,0, 14,10,4.5)
  )).setRegistryName(new ResourceLocation(MODID, "rustic_two_hinge_lever"));

  // Rustic lever 3 (big angular)
  public static final BlockBistableSwitch RUSTIC_ANGULAR_LEVER = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(0,10,0, 14,15,4.5),
    ModAuxiliaries.getPixeledAABB(6, 2,0, 14,15,4.5)
  )).setRegistryName(new ResourceLocation(MODID, "rustic_angular_lever"));

  // Rustic lever 7 (The Nail)
  public static final BlockBistableSwitch RUSTIC_NAIL_LEVER = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(6,7,0, 9,10,3), null
  )).setRegistryName(new ResourceLocation(MODID, "rustic_nail_lever"));

  // Rustic button 1
  public static final BlockPulseSwitch RUSTIC_BUTTON = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(5,5,0,11,11,2.5), null
  )).setRegistryName(new ResourceLocation(MODID, "rustic_button"));

  // Rustic button 2 (bolted)
  public static final BlockPulseSwitch RUSTIC_SMALL_BUTTON = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(6,6,0,10,10,2.5), null
  )).setRegistryName(new ResourceLocation(MODID, "rustic_small_button"));

  // Rustic button 3 (pull chain)
  public static final BlockPulseSwitch RUSTIC_SPRING_RESET_CHAIN = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(5,3.5,0,11,15,4), null
  )).setRegistryName(new ResourceLocation(MODID, "rustic_spring_reset_chain"));

  // Rustic button 7 (pull nail)
  public static final BlockPulseSwitch RUSTIC_NAIL_BUTTON = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(6,7,0, 9,10,3), null
  )).setRegistryName(new ResourceLocation(MODID, "rustic_nail_button"));

  // Rustic door contact mat
  public static final BlockContactMat RUSTIC_DOOR_CONTACT_PLATE = (BlockContactMat)(new BlockContactMat(
    BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_DATA_WEAK|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(1,0,0, 15,1,12),
    ModAuxiliaries.getPixeledAABB(1,0,0, 15,0.5,12),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.5f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.0f)
  )).setRegistryName(new ResourceLocation(MODID, "rustic_door_contact_plate"));

  // Rustic full-size contact plate
  public static final BlockContactMat RUSTIC_CONTACT_PLATE = (BlockContactMat)(new BlockContactMat(
    BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(0,0,0, 16,1,16),
    ModAuxiliaries.getPixeledAABB(0,0,0, 16,0.5,16),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.5f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.0f)
  )).setRegistryName(new ResourceLocation(MODID, "rustic_contact_plate"));

  // Rustic shock sensor plate
  public static final BlockContactMat RUSTIC_SHOCK_SENSITIVE_CONTACT_PLATE = (BlockContactMat)(new BlockContactMat(
    BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_SHOCK_SENSITIVE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(0,0,0, 16,1,16),
    ModAuxiliaries.getPixeledAABB(0,0,0, 16,0.5,16),
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
    ModAuxiliaries.getPixeledAABB(0,15.6,0, 16,16,16),
    ModAuxiliaries.getPixeledAABB(0, 2.0,0, 16,16, 0.1),
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
    ModAuxiliaries.getPixeledAABB(0,15.6,0, 16,16,16),
    ModAuxiliaries.getPixeledAABB(0, 2.0,0, 16,16, 0.1),
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
    ModAuxiliaries.getPixeledAABB(0,12.6,0, 16,13,16),
    ModAuxiliaries.getPixeledAABB(0,12.6,0, 16,13,16),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.5f),
    null // new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.0f)
    //ModAuxiliaries.RsMaterials.MATERIAL_TRAPDOORSWITCH
  )).setRegistryName(new ResourceLocation(MODID, "rustic_fallthrough_detector"));

  public static final BlockGauge RUSTIC_CIRCULAR_GAUGE = (BlockGauge)(new BlockGauge(
    0, // no color tint.
    GAUGE_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(2,2,0, 14,14,1)
  )).setRegistryName(new ResourceLocation(MODID, "rustic_circular_gauge"));

  public static final BlockIndicator RUSTIC_SEMAPHORE_INDICATOR = (BlockIndicator)(new BlockIndicator(
    0,
    INDICATOR_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(3,4,0, 13,11,1),
    null,
    null
  )).setRegistryName(new ResourceLocation(MODID, "rustic_semaphore"));


  // -----------------------------------------------------------------------------------------------------------------
  // -- Glass
  // -----------------------------------------------------------------------------------------------------------------

  // Thin star shaped glass switch
  public static final BlockBistableSwitch GLASS_ROTARY_SWITCH = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|BlockSwitch.SWITCH_CONFIG_COLOR_TINT_SUPPORT,
    SWITCH_METALLIC_FAINT_LIGHT_EMITTING_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.5), null
  )).setRegistryName(new ResourceLocation(MODID, "glass_rotary_switch"));

  // Bistable glass touch switch
  public static final BlockBistableSwitch GLASS_TOUCH_SWITCH = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|BlockSwitch.SWITCH_CONFIG_COLOR_TINT_SUPPORT,
    SWITCH_METALLIC_FAINT_LIGHT_EMITTING_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.5), null
  )).setRegistryName(new ResourceLocation(MODID, "glass_touch_switch"));

  // Thin star shaped glass button
  public static final BlockPulseSwitch GLASS_BUTTON = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
    BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|BlockSwitch.SWITCH_CONFIG_COLOR_TINT_SUPPORT|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_FAINT_LIGHT_EMITTING_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.5), null
  )).setRegistryName(new ResourceLocation(MODID, "glass_button"));

  // Thin small star shaped glass button
  public static final BlockPulseSwitch GLASS_SMALL_BUTTON = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
    BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|BlockSwitch.SWITCH_CONFIG_COLOR_TINT_SUPPORT|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_FAINT_LIGHT_EMITTING_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.5), null
  )).setRegistryName(new ResourceLocation(MODID, "glass_small_button"));

  // Glass touch button
  public static final BlockPulseSwitch GLASS_TOUCH_BUTTON = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
    BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|BlockSwitch.SWITCH_CONFIG_COLOR_TINT_SUPPORT|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_FAINT_LIGHT_EMITTING_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.5), null
  )).setRegistryName(new ResourceLocation(MODID, "glass_touch_button"));

  // Glass door plate
  public static final BlockContactMat GLASS_DOOR_CONTACT_MAT = (BlockContactMat)(new BlockContactMat(
    BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|
    BlockSwitch.SWITCH_CONFIG_COLOR_TINT_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(0,0,0, 16,0.25,16), null,
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
    ModAuxiliaries.getPixeledAABB(0,0,0, 16,0.25,16), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "glass_contact_mat"));

  // Glass Day time switch
  public static final BlockDayTimerSwitch GLASS_DAY_TIMER = (BlockDayTimerSwitch)(new BlockDayTimerSwitch(
    BlockSwitch.SWITCH_CONFIG_TIMER_DAYTIME|
    BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.1), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "glass_day_timer"));

  // Glass interval signal timer
  public static final BlockIntervalTimerSwitch GLASS_INTERVAL_TIMER = (BlockIntervalTimerSwitch)(new BlockIntervalTimerSwitch(
    BlockSwitch.SWITCH_CONFIG_TIMER_INTERVAL|
    BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.1), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "glass_interval_timer"));

  // Glass infrared motion sensor
  public static final BlockEntityDetectorSwitch GLASS_ENTITY_DETECTOR = (BlockEntityDetectorSwitch)(new BlockEntityDetectorSwitch(
    BlockSwitch.SWITCH_CONFIG_SENSOR_VOLUME|
    BlockSwitch.SWITCH_CONFIG_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.1), null,
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
    ModAuxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.1), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "glass_linear_entity_detector"));

  public static final BlockGauge GLASS_VERTICAL_BAR_GAUGE = (BlockGauge)(new BlockGauge(
    BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT,
    GAUGE_GLASS_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(7,3.7,0, 10,12,0.4)
  )).setRegistryName(new ResourceLocation(MODID, "glass_vertical_bar_gauge"));

  // -----------------------------------------------------------------------------------------------------------------
  // -- Old Fancy
  // -----------------------------------------------------------------------------------------------------------------

  // Old fancy gold decorated lever
  public static final BlockBistableSwitch OLDFANCY_BISTABLE_SWITCH1 = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(6,6.5,0, 10.3,13.5,4.5),
    ModAuxiliaries.getPixeledAABB(6,3.5,0, 10.3,10.0,4.5)
  )).setRegistryName(new ResourceLocation(MODID, "oldfancy_bistableswitch1"));

  // Old fancy angular lever
  public static final BlockBistableSwitch OLDFANCY_BISTABLE_SWITCH2 = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(2.5,6.0,0, 9.7,10,4.5),
    ModAuxiliaries.getPixeledAABB(4.5,3.5,0, 9.2,10,4.5)
  )).setRegistryName(new ResourceLocation(MODID, "oldfancy_bistableswitch2"));

  // Old fancy (golden decorated) button
  public static final BlockPulseSwitch OLDFANCY_BUTTON = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(6,6,0,10,10,1.5), null
  )).setRegistryName(new ResourceLocation(MODID, "oldfancy_button"));

  // Old fancy (golden decorated) chain pulse switch
  public static final BlockPulseSwitch OLDFANCY_SPRING_RESET_CHAIN = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(6.5,4.8,0,9.5,13,4),
    ModAuxiliaries.getPixeledAABB(6.5,3.8,0,9.5,12,4)
  )).setRegistryName(new ResourceLocation(MODID, "oldfancy_spring_reset_chain"));

  // Old fancy (golden decorated) tiny button
  public static final BlockPulseSwitch OLDFANCY_SMALL_BUTTON = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(7,7,0,9,9,1.5), null
  )).setRegistryName(new ResourceLocation(MODID, "oldfancy_small_button"));

  // -----------------------------------------------------------------------------------------------------------------
  // -- Other
  // -----------------------------------------------------------------------------------------------------------------

  // Yellow power plant
  public static final BlockPowerPlant YELLOW_POWER_PLANT = (BlockPowerPlant)(new BlockPowerPlant(
    BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
    BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(5,0,5, 11,9,11), null,
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
    ModAuxiliaries.getPixeledAABB(5,0,5, 11,9,11), null,
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_GRASS_BREAK, 0.09f, 3.6f),
    new ModResources.BlockSoundEvent(SoundEvents.BLOCK_GRASS_BREAK, 0.04f, 3.0f)
    // ModAuxiliaries.RsMaterials.MATERIAL_PLANT
  )).setRegistryName(new ResourceLocation(MODID, "red_power_plant"));

  // Light flip switch
  public static final BlockBistableSwitch LIGHT_SWITCH = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(7,6,0,9,10,1.5), null
  )).setRegistryName(new ResourceLocation(MODID, "light_switch"));

  // Arrow target
  public static final BlockPulseSwitch ARROW_TARGET_SWITCH = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
    BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
    BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(5,5,0,11,11,1), null
  )).setRegistryName(new ResourceLocation(MODID, "arrow_target"));

  // Valve Wheel
  public static final BlockBistableSwitch BISTABLE_VALVE_WHEEL_SWITCH = (BlockBistableSwitch)(new BlockBistableSwitch(
    BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
    BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_DATA_WEAK|
    BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(4,4,0,12,12,3.5), null
  )).setRegistryName(new ResourceLocation(MODID, "valve_wheel_switch"));

  // Elevator button
  public static final BlockPulseSwitch ELEVATOR_BUTTON = (BlockPulseSwitch)(new BlockPulseSwitch(
    BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    SWITCH_METALLIC_FAINT_LIGHT_EMITTING_BLOCK_PROPERTIES,
    ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 1), null
  )).setRegistryName(new ResourceLocation(MODID, "elevator_button"));

  // -----------------------------------------------------------------------------------------------------------------
  // -- sensitive glass
  // -----------------------------------------------------------------------------------------------------------------

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

  private static final ArrayList<Block> modBlocks = new ArrayList<Block>(Arrays.asList(
    // Industrial
    INDUSTRIAL_SMALL_LEVER,
    INDUSTRIAL_LEVER,
    INDUSTRIAL_ROTARY_LEVER,
    INDUSTRIAL_ROTARY_MACHINE_SWITCH,
    INDUSTRIAL_MACHINE_SWITCH,
    INDUSTRIAL_ESTOP_SWITCH,
    INDUSTRIAL_HOPPER_BLOCKING_SWITCH,
    INDUSTRIAL_BUTTON,
    INDUSTRIAL_FENCED_BUTTON,
    INDUSTRIAL_DOUBLE_POLE_BUTTON,
    INDUSTRIAL_FOOT_BUTTON,
    INDUSTRIAL_PULL_HANDLE,
    INDUSTRIAL_DIMMER,
    INDUSTRIAL_DOOR_CONTACT_MAT,
    INDUSTRIAL_CONTACT_MAT,
    INDUSTRIAL_SHOCK_SENSITIVE_CONTACT_MAT,
    INDUSTRIAL_SHOCK_SENSITIVE_TRAPDOOR,
    INDUSTRIAL_HIGH_SENSITIVE_TRAPDOOR,
    INDUSTRIAL_FALLTHROUGH_DETECTOR,
    INDUSTRIAL_DAY_TIMER,
    INDUSTRIAL_INTERVAL_TIMER,
    INDUSTRIAL_ENTITY_DETECTOR,
    INDUSTRIAL_LINEAR_ENTITY_DETECTOR,
    INDUSTRIAL_LIGHT_SENSOR,
    INDUSTRIAL_RAIN_SENSOR,
    INDUSTRIAL_LIGHTNING_SENSOR,
    INDUSTRIAL_COMPARATOR_SWITCH,
    INDUSTRIAL_BLOCK_DETECTOR,
    INDUSTRIAL_SWITCHLINK_RECEIVER,
    INDUSTRIAL_SWITCHLINK_CASED_RECEIVER,
    INDUSTRIAL_SWITCHLINK_PULSE_RECEIVER,
    INDUSTRIAL_SWITCHLINK_CASED_PULSE_RECEIVER,
    INDUSTRIAL_SWITCHLINK_RELAY,
    INDUSTRIAL_SWITCHLINK_PULSE_RELAY,
    INDUSTRIAL_BISTABLE_KNOCK_SWITCH,
    INDUSTRIAL_PULSE_KNOCK_SWITCH,
    INDUSTRIAL_ANALOG_GAUGE,
    INDUSTRIAL_VERTICAL_BAR_GAUGE,
    INDUSTRIAL_SMALL_DIGITAL_GAUGE,
    INDUSTRIAL_TUBE_GAUGE,
    INDUSTRIAL_ANALOG_HORIZONTAL_GAUGE,
    INDUSTRIAL_ALARM_LAMP,
    INDUSTRIAL_ALARM_SIREN,
    INDUSTRIAL_GREEN_LED_INDICATOR,
    INDUSTRIAL_GREEN_BLINK_LED_INDICATOR,
    INDUSTRIAL_YELLOW_LED_INDICATOR,
    INDUSTRIAL_YELLOW_BLINK_LED_INDICATOR,
    INDUSTRIAL_RED_LED_INDICATOR,
    INDUSTRIAL_RED_BLINK_LED_INDICATOR,
    INDUSTRIAL_WHITE_LED_INDICATOR,
    INDUSTRIAL_WHITE_BLINK_LED_INDICATOR,
    // Rustic
    RUSTIC_LEVER,
    RUSTIC_TWO_HINGE_LEVER,
    RUSTIC_ANGULAR_LEVER,
    RUSTIC_NAIL_LEVER,
    RUSTIC_BUTTON,
    RUSTIC_SMALL_BUTTON,
    RUSTIC_SPRING_RESET_CHAIN,
    RUSTIC_NAIL_BUTTON,
    RUSTIC_DOOR_CONTACT_PLATE,
    RUSTIC_CONTACT_PLATE,
    RUSTIC_SHOCK_SENSITIVE_CONTACT_PLATE,
    RUSTIC_SHOCK_SENSITIVE_TRAPDOOR,
    RUSTIC_HIGH_SENSITIVE_TRAPDOOR,
    RUSTIC_FALLTHROUGH_DETECTOR,
    RUSTIC_SEMAPHORE_INDICATOR,
    // Glass
    GLASS_ROTARY_SWITCH,
    GLASS_TOUCH_SWITCH,
    GLASS_BUTTON,
    GLASS_SMALL_BUTTON,
    GLASS_TOUCH_BUTTON,
    GLASS_DOOR_CONTACT_MAT,
    GLASS_CONTACT_MAT,
    GLASS_DAY_TIMER,
    GLASS_INTERVAL_TIMER,
    GLASS_ENTITY_DETECTOR,
    GLASS_LINEAR_ENTITY_DETECTOR,
    GLASS_VERTICAL_BAR_GAUGE,
    // Old Fancy
    OLDFANCY_BISTABLE_SWITCH1,
    OLDFANCY_BISTABLE_SWITCH2,
    OLDFANCY_BUTTON,
    OLDFANCY_SPRING_RESET_CHAIN,
    OLDFANCY_SMALL_BUTTON,
    // Other switches
    YELLOW_POWER_PLANT,
    RED_POWER_PLANT,
    LIGHT_SWITCH,
    ARROW_TARGET_SWITCH,
    BISTABLE_VALVE_WHEEL_SWITCH,
    ELEVATOR_BUTTON,
    // Senesitive Glass
    SENSITIVE_GLASS_BLOCK,
    WHITE_SENSITIVE_GLASS_BLOCK,
    GREEN_SENSITIVE_GLASS_BLOCK,
    RED_SENSITIVE_GLASS_BLOCK,
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
  ));

  private static ArrayList<Block> devBlocks = new ArrayList<Block>(Arrays.asList(
    TESTING_QUBE
  ));

  //--------------------------------------------------------------------------------------------------------------------
  // Tile entities bound exclusively to the blocks above
  //--------------------------------------------------------------------------------------------------------------------

  private static Block[] blocks_of_type(Class<? extends Block> clazz)
  { return modBlocks.stream().filter(clazz::isInstance).toArray(Block[]::new); }

  public static final TileEntityType<?> TET_GAUGE = TileEntityType.Builder
    .create(BlockGauge.TileEntityGauge::new, blocks_of_type(BlockGauge.class))
    .build(null)
    .setRegistryName(ModRsGauges.MODID, "te_gauge");

  public static final TileEntityType<?> TET_SWITCH = TileEntityType.Builder
    .create(BlockSwitch.TileEntitySwitch::new, blocks_of_type(BlockSwitch.class))
    .build(null)
    .setRegistryName(ModRsGauges.MODID, "te_switch");

  public static final TileEntityType<?> TET_CONTACT_SWITCH = TileEntityType.Builder
    .create(BlockContactSwitch.TileEntityContactSwitch::new, blocks_of_type(BlockContactSwitch.class))
    .build(null)
    .setRegistryName(ModRsGauges.MODID, "te_contact_switch");

  public static final TileEntityType<?> TET_DETECTOR_SWITCH = TileEntityType.Builder
    .create(BlockEntityDetectorSwitch.TileEntityDetectorSwitch::new, blocks_of_type(BlockEntityDetectorSwitch.class))
    .build(null)
    .setRegistryName(ModRsGauges.MODID, "te_detector_switch");

  public static final TileEntityType<?> TET_ENVSENSOR_SWITCH = TileEntityType.Builder
    .create(BlockEnvironmentalSensorSwitch.TileEntityEnvironmentalSensorSwitch::new, blocks_of_type(BlockEnvironmentalSensorSwitch.class))
    .build(null)
    .setRegistryName(ModRsGauges.MODID, "te_envsensor_switch");

  public static final TileEntityType<?> TET_DAYTIMER_SWITCH = TileEntityType.Builder
    .create(BlockDayTimerSwitch.TileEntityDayTimerSwitch::new, blocks_of_type(BlockDayTimerSwitch.class))
    .build(null)
    .setRegistryName(ModRsGauges.MODID, "te_daytimer_switch");

  public static final TileEntityType<?> TET_TIMER_SWITCH = TileEntityType.Builder
    .create(BlockIntervalTimerSwitch.TileEntityIntervalTimerSwitch::new, blocks_of_type(BlockIntervalTimerSwitch.class))
    .build(null)
    .setRegistryName(ModRsGauges.MODID, "te_timer_switch");

  public static final TileEntityType<?> TET_COMPARATOR_SWITCH = TileEntityType.Builder
    .create(BlockComparatorSwitch.TileEntityComparatorSwitch::new, blocks_of_type(BlockComparatorSwitch.class))
    .build(null)
    .setRegistryName(ModRsGauges.MODID, "te_comparator_switch");

  public static final TileEntityType<?> TET_OBSERVER_SWITCH = TileEntityType.Builder
    .create(BlockObserverSwitch.TileEntityObserverSwitch::new, blocks_of_type(BlockObserverSwitch.class))
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
    TET_COMPARATOR_SWITCH,
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
            RenderTypeLookup.setRenderLayer(block, RenderType.getCutout());
            break;
          case CUTOUT_MIPPED:
            RenderTypeLookup.setRenderLayer(block, RenderType.getCutoutMipped());
            break;
          case TRANSLUCENT:
            RenderTypeLookup.setRenderLayer(block, RenderType.getTranslucent());
            break;
          case SOLID:
            break;
        }
      }
    }
  }
}
