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

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.BlockItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraft.util.SoundEvents;
import wile.rsgauges.libmc.detail.Auxiliaries;
import wile.rsgauges.libmc.detail.Auxiliaries.IExperimentalFeature;
import wile.rsgauges.detail.ModResources;
import wile.rsgauges.blocks.*;
import wile.rsgauges.items.*;

import java.util.*;
import javax.annotation.Nonnull;


public class ModContent
{
  // -----------------------------------------------------------------------------------------------------------------
  // -- Internal constants, default block properties
  // -----------------------------------------------------------------------------------------------------------------

  private static final String MODID = ModRsGauges.MODID;

  private static final AbstractBlock.Properties gauge_metallic_block_properties()
  {
    return AbstractBlock.Properties.of(Material.METAL, MaterialColor.METAL)
                .strength(0.5f, 15f)
                .sound(SoundType.METAL)
                .harvestLevel(0)
                .noCollission()
                .isValidSpawn((s,w,p,e)->false);
  }

  private static final AbstractBlock.Properties gauge_glass_block_properties() {
    return (AbstractBlock.Properties
      .of(Material.METAL, MaterialColor.METAL)
      .strength(0.5f, 15f)
      .sound(SoundType.METAL)
      .harvestLevel(0)
      .noOcclusion()
      .isValidSpawn((s,w,p,e)->false)
    );
  }

  private static final AbstractBlock.Properties indicator_metallic_block_properties()
  {
    return AbstractBlock.Properties
      .of(Material.METAL, MaterialColor.METAL)
      .strength(0.5f, 15f)
      .sound(SoundType.METAL)
      .harvestLevel(0)
      .lightLevel((state)->3)
      .noOcclusion()
      .isValidSpawn((s,w,p,e)->false);
  }

  private static final AbstractBlock.Properties indicator_glass_block_properties()
  {
    return AbstractBlock.Properties
        .of(Material.METAL, MaterialColor.METAL)
        .strength(0.5f, 15f)
        .sound(SoundType.METAL)
        .harvestLevel(0)
        .lightLevel((state)->3)
        .noOcclusion()
        .isValidSpawn((s,w,p,e)->false);
  }

  private static final AbstractBlock.Properties alarm_lamp_block_properties()
  {
    return AbstractBlock.Properties
      .of(Material.METAL, MaterialColor.METAL)
      .strength(0.5f, 15f)
      .sound(SoundType.METAL)
      .harvestLevel(0)
      .noOcclusion()
      .lightLevel((state)->state.getValue(IndicatorBlock.POWERED)?12:2)
      .isValidSpawn((s,w,p,e)->false);
  }

  private static final AbstractBlock.Properties colored_sensitive_glass_block_properties()
  {
    return (AbstractBlock.Properties
      .of(Material.BUILDABLE_GLASS, MaterialColor.METAL)
      .strength(0.35f, 15f)
      .sound(SoundType.METAL)
      .harvestLevel(0)
      .noOcclusion()
      .isValidSpawn((s,w,p,e)->false)
    );
  }

  private static final AbstractBlock.Properties light_emitting_sensitive_glass_block_properties()
  {
    return AbstractBlock.Properties
      .of(Material.BUILDABLE_GLASS, MaterialColor.METAL)
      .strength(0.35f, 15f)
      .sound(SoundType.METAL)
      .harvestLevel(0)
      .noOcclusion().emissiveRendering((s,w,p)->true)
      .lightLevel((state)->state.getValue(SensitiveGlassBlock.POWERED)?15:0)
      .isValidSpawn((s,w,p,e)->false);
  }

  private static final AbstractBlock.Properties switch_metallic_block_properties()
  { return gauge_metallic_block_properties(); }

  private static final AbstractBlock.Properties switch_glass_block_properties()
  { return gauge_glass_block_properties(); }

  private static final AbstractBlock.Properties switch_metallic_faint_light_block_properties()
  {
    return AbstractBlock.Properties
      .of(Material.METAL, MaterialColor.METAL)
      .strength(0.5f, 15f)
      .sound(SoundType.METAL)
      .harvestLevel(0)
      .lightLevel((state)->5);
  }

  // -----------------------------------------------------------------------------------------------------------------
  // -- industrual
  // -----------------------------------------------------------------------------------------------------------------

  // Contact lever switch
  public static final BistableSwitchBlock INDUSTRIAL_SMALL_LEVER = (BistableSwitchBlock)(new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0,12,15,4),
    Auxiliaries.getPixeledAABB(4,1,0,12,12,4)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_small_lever"));

  // Mechanical lever
  public static final BistableSwitchBlock INDUSTRIAL_LEVER = (BistableSwitchBlock)(new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5,4,0,11,15,5),
    Auxiliaries.getPixeledAABB(5,1,0,11,12,5)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_lever"));

  // Mechanical rotary lever
  public static final BistableSwitchBlock INDUSTRIAL_ROTARY_LEVER = (BistableSwitchBlock)(new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(1,4,0,12,12,6),
    Auxiliaries.getPixeledAABB(1,1,0,12,12,6)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_rotary_lever"));

  // Rotary machine switch
  public static final BistableSwitchBlock INDUSTRIAL_ROTARY_MACHINE_SWITCH = (BistableSwitchBlock)(new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null
  )).setRegistryName(new ResourceLocation(MODID, "industrial_rotary_machine_switch"));

  // Two-button machine switch
  public static final BistableSwitchBlock INDUSTRIAL_MACHINE_SWITCH = (BistableSwitchBlock)(new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null
  )).setRegistryName(new ResourceLocation(MODID, "industrial_machine_switch"));

  // ESTOP button
  public static final BistableSwitchBlock INDUSTRIAL_ESTOP_SWITCH = (BistableSwitchBlock)(new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE_OFF,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5,5,0, 11, 11, 2.5),
    Auxiliaries.getPixeledAABB(5,5,0, 11, 11, 3.5)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_estop_switch"));

  // Hopper blocking switch
  public static final BistableSwitchBlock INDUSTRIAL_HOPPER_BLOCKING_SWITCH = (BistableSwitchBlock)(new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    SwitchBlock.SWITCH_DATA_WEAK,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(3,10,0, 13, 12, 6.7),
    Auxiliaries.getPixeledAABB(3,10,0, 13, 12, 3.7)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_hopper_switch"));

  // Square machine pulse switch
  public static final PulseSwitchBlock INDUSTRIAL_BUTTON = (PulseSwitchBlock)(new PulseSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5,5,0, 11, 11, 2), null
  )).setRegistryName(new ResourceLocation(MODID, "industrial_button"));

  // Fenced round machine pulse switch
  public static final PulseSwitchBlock INDUSTRIAL_FENCED_BUTTON = (PulseSwitchBlock)(new PulseSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5,5,0, 11, 11, 2), null
  )).setRegistryName(new ResourceLocation(MODID, "industrial_fenced_button"));

  // Retro double pole switch
  public static final PulseSwitchBlock INDUSTRIAL_DOUBLE_POLE_BUTTON = (PulseSwitchBlock)(new PulseSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 3),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 2)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_double_pole_button"));

  // Mechanical spring reset push button
  public static final PulseSwitchBlock INDUSTRIAL_FOOT_BUTTON = (PulseSwitchBlock)(new PulseSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5,3,0, 11, 7, 4), null
  )).setRegistryName(new ResourceLocation(MODID, "industrial_foot_button"));

  // Mechanical spring reset pull handle
  public static final PulseSwitchBlock INDUSTRIAL_PULL_HANDLE = (PulseSwitchBlock)(new PulseSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 2),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 2)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_pull_handle"));

  // Manual dimmer
  public static final DimmerSwitchBlock INDUSTRIAL_DIMMER = (DimmerSwitchBlock)(new DimmerSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,1,0, 12, 15, 2),
    null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.9f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.7f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_dimmer"));

  // Door contact mat
  public static final ContactMatBlock INDUSTRIAL_DOOR_CONTACT_MAT = (ContactMatBlock)(new ContactMatBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(1,0,0, 15, 1, 13), null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_door_contact_mat"));

  // Sensitive full size contact mat
  public static final ContactMatBlock INDUSTRIAL_CONTACT_MAT = (ContactMatBlock)(new ContactMatBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0,0,0, 16, 1, 16), null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_contact_mat"));

  // Industrial shock sensor contact mat
  public static final ContactMatBlock INDUSTRIAL_SHOCK_SENSITIVE_CONTACT_MAT = (ContactMatBlock)(new ContactMatBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_SHOCK_SENSITIVE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0,0,0, 16, 1, 16), null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_shock_sensitive_contact_mat"));

  // Industrial trap door switch (shock vibration sensitive)
  public static final TrapdoorSwitchBlock INDUSTRIAL_SHOCK_SENSITIVE_TRAPDOOR = (TrapdoorSwitchBlock)(new TrapdoorSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_SHOCK_SENSITIVE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0,15.6,0, 16, 16, 16),
    Auxiliaries.getPixeledAABB(0,2,0, 16, 1, 0.1),
    new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 3.0f),
    new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.5f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_shock_sensitive_trapdoor"));

  // Industrial trap door switch (high sensitive shock vibration sensitive)
  public static final TrapdoorSwitchBlock INDUSTRIAL_HIGH_SENSITIVE_TRAPDOOR = (TrapdoorSwitchBlock)(new TrapdoorSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_SHOCK_SENSITIVE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    SwitchBlock.SWITCH_CONFIG_HIGH_SENSITIVE,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0,15.6,0, 16, 16, 16),
    Auxiliaries.getPixeledAABB(0,2,0, 16, 1, 0.1),
    new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.5f),
    new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.0f)
    // Auxiliaries.RsMaterials.MATERIAL_TRAPDOORSWITCH
  )).setRegistryName(new ResourceLocation(MODID, "industrial_high_sensitive_trapdoor"));

  // Industrial trap door switch (item trap door)
  public static final TrapdoorSwitchBlock INDUSTRIAL_FALLTHROUGH_DETECTOR = (TrapdoorSwitchBlock)(new TrapdoorSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
    SwitchBlock.SWITCH_CONFIG_INVERTABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0.1,12.6,0.1, 15.9,13, 15.9),
    Auxiliaries.getPixeledAABB(0.1,12.6,0.1, 15.9,13, 15.9),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.05f, 2.5f),
    null
  )).setRegistryName(new ResourceLocation(MODID, "industrial_fallthrough_detector"));

  // Day time switch
  public static final DayTimerSwitchBlock INDUSTRIAL_DAY_TIMER = (DayTimerSwitchBlock)(new DayTimerSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_TIMER_DAYTIME|
    SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_day_timer"));

  // Interval signal timer
  public static final IntervalTimerSwitchBlock INDUSTRIAL_INTERVAL_TIMER = (IntervalTimerSwitchBlock)(new IntervalTimerSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_TIMER_INTERVAL|
    SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_interval_timer"));

  // Infrared motion_sensor
  public static final EntityDetectorSwitchBlock INDUSTRIAL_ENTITY_DETECTOR = (EntityDetectorSwitchBlock)(new EntityDetectorSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_SENSOR_VOLUME|
    SwitchBlock.SWITCH_CONFIG_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 1), null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_entity_detector"));

  // Linear laser motion sensor
  public static final EntityDetectorSwitchBlock INDUSTRIAL_LINEAR_ENTITY_DETECTOR = (EntityDetectorSwitchBlock)(new EntityDetectorSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_SENSOR_LINEAR|
    SwitchBlock.SWITCH_CONFIG_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 1), null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_linear_entity_detector"));

  // Local light sensor
  public static final EnvironmentalSensorSwitchBlock INDUSTRIAL_LIGHT_SENSOR = (EnvironmentalSensorSwitchBlock)(new EnvironmentalSensorSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_SENSOR_LIGHT|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_light_sensor"));

  // Rain sensor switch
  public static final EnvironmentalSensorSwitchBlock INDUSTRIAL_RAIN_SENSOR = (EnvironmentalSensorSwitchBlock)(new EnvironmentalSensorSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_SENSOR_RAIN|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_rain_sensor"));

  // Lightning sensor switch
  public static final EnvironmentalSensorSwitchBlock INDUSTRIAL_LIGHTNING_SENSOR = (EnvironmentalSensorSwitchBlock)(new EnvironmentalSensorSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_SENSOR_LIGHTNING|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_lightning_sensor"));

  // Comparator output level observing switch
  public static final ComparatorSwitchBlock INDUSTRIAL_COMPARATOR_SWITCH = (ComparatorSwitchBlock)(new ComparatorSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,10,0, 12, 15, 1.5), null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_comparator_switch"));

  // Uni-directional block detector switch
  public static final ObserverSwitchBlock INDUSTRIAL_BLOCK_DETECTOR = (ObserverSwitchBlock)(new ObserverSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_SENSOR_BLOCKDETECT|
    SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    SwitchBlock.SWITCH_DATA_SIDE_ENABLED_BOTTOM|SwitchBlock.SWITCH_DATA_SIDE_ENABLED_TOP|
    SwitchBlock.SWITCH_DATA_SIDE_ENABLED_FRONT|SwitchBlock.SWITCH_DATA_SIDE_ENABLED_LEFT|
    SwitchBlock.SWITCH_DATA_SIDE_ENABLED_RIGHT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0.5,0.5,0.5, 15.5, 15.5, 15.5), null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.2f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.2f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_block_detector"));

  // Industrial bistable link receiver switch
  public static final LinkReceiverSwitchBlock INDUSTRIAL_SWITCHLINK_RECEIVER = (LinkReceiverSwitchBlock)(new LinkReceiverSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_BISTABLE|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.9f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.7f),
    false
  )).setRegistryName(new ResourceLocation(MODID, "industrial_switchlink_receiver"));

  // Industrial analog link receiver
  public static final LinkReceiverSwitchBlock INDUSTRIAL_SWITCHLINK_RECEIVER_ANALOG = (LinkReceiverSwitchBlock)(new LinkReceiverSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_BISTABLE|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.9f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.7f),
    true
  )).setRegistryName(new ResourceLocation(MODID, "industrial_switchlink_receiver_analog"));

  // Industrial full block bistable link receiver switch
  public static final LinkReceiverSwitchBlock INDUSTRIAL_SWITCHLINK_CASED_RECEIVER = (LinkReceiverSwitchBlock)(new LinkReceiverSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_FULLCUBE|SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
    SwitchBlock.SWITCH_CONFIG_INVERTABLE|SwitchBlock.SWITCH_DATA_SIDE_ENABLED_ALL|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0,0,0, 16, 16, 16), null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.9f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.7f),
    false
  )).setRegistryName(new ResourceLocation(MODID, "industrial_switchlink_cased_receiver"));

  // Industrial pulse link receiver switch
  public static final LinkReceiverSwitchBlock INDUSTRIAL_SWITCHLINK_PULSE_RECEIVER = (LinkReceiverSwitchBlock)(new LinkReceiverSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
    SwitchBlock.SWITCH_CONFIG_INVERTABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.9f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.7f),
    false
  )).setRegistryName(new ResourceLocation(MODID, "industrial_switchlink_pulse_receiver"));

  // Industrial full block pulse link receiver switch
  public static final LinkReceiverSwitchBlock INDUSTRIAL_SWITCHLINK_CASED_PULSE_RECEIVER = (LinkReceiverSwitchBlock)(new LinkReceiverSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_FULLCUBE|SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
    SwitchBlock.SWITCH_CONFIG_INVERTABLE|SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_DATA_SIDE_ENABLED_ALL|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0,0,0, 16, 16, 16), null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.9f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.7f),
    false
  )).setRegistryName(new ResourceLocation(MODID, "industrial_switchlink_cased_pulse_receiver"));

  // Industrial bistable link relay
  public static final LinkSenderSwitchBlock INDUSTRIAL_SWITCHLINK_RELAY = (LinkSenderSwitchBlock)(new LinkSenderSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_LINK_SENDER|
    SwitchBlock.SWITCH_CONFIG_INVERTABLE|SwitchBlock.SWITCH_DATA_WEAK|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.9f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.7f),
    false
  )).setRegistryName(new ResourceLocation(MODID, "industrial_switchlink_relay"));

  // Industrial analog link relay
  public static final LinkSenderSwitchBlock INDUSTRIAL_SWITCHLINK_RELAY_ANALOG = (LinkSenderSwitchBlock)(new LinkSenderSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_LINK_SENDER|
    SwitchBlock.SWITCH_CONFIG_INVERTABLE|SwitchBlock.SWITCH_DATA_WEAK|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.9f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.7f),
    true
  )).setRegistryName(new ResourceLocation(MODID, "industrial_switchlink_relay_analog"));

  // Industrial pulse link relay
  public static final LinkSenderSwitchBlock INDUSTRIAL_SWITCHLINK_PULSE_RELAY = (LinkSenderSwitchBlock)(new LinkSenderSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_LINK_SENDER|
    SwitchBlock.SWITCH_CONFIG_INVERTABLE|SwitchBlock.SWITCH_DATA_WEAK|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.9f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.7f),
    false
  )).setRegistryName(new ResourceLocation(MODID, "industrial_switchlink_pulse_relay"));

  // Bistable industrial knock surge detctor
  public static final BistableKnockSwitchBlock INDUSTRIAL_BISTABLE_KNOCK_SWITCH = (BistableKnockSwitchBlock)(new BistableKnockSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_OPOSITE_PLACEMENT|SwitchBlock.RSBLOCK_CONFIG_FULLCUBE|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|SwitchBlock.SWITCH_DATA_SIDE_ENABLED_ALL,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0.5,0.5,0.5, 15.5, 15.5, 15.5), null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.2f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.2f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_knock_switch"));

  // Pulse industrial knock surge detctor
  public static final PulseKnockSwitchBlock INDUSTRIAL_PULSE_KNOCK_SWITCH = (PulseKnockSwitchBlock)(new PulseKnockSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.RSBLOCK_CONFIG_OPOSITE_PLACEMENT|SwitchBlock.RSBLOCK_CONFIG_FULLCUBE|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|SwitchBlock.SWITCH_DATA_SIDE_ENABLED_ALL,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0.5,0.5,0.5, 15.5, 15.5, 15.5), null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.2f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.2f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_knock_button"));

  public static final GaugeBlock INDUSTRIAL_ANALOG_GAUGE = (GaugeBlock)(new GaugeBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
    gauge_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(2,2,0, 14,14,1)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_analog_angular_gauge"));

  public static final GaugeBlock INDUSTRIAL_ANALOG_HORIZONTAL_GAUGE = (GaugeBlock)(new GaugeBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
    gauge_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(2,4,0, 14, 12, 1)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_analog_horizontal_gauge"));

  public static final GaugeBlock INDUSTRIAL_VERTICAL_BAR_GAUGE = (GaugeBlock)(new GaugeBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
    gauge_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,2,0, 12, 14, 1)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_vertical_bar_gauge"));

  public static final GaugeBlock INDUSTRIAL_SMALL_DIGITAL_GAUGE = (GaugeBlock)(new GaugeBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
    gauge_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,5,0, 12, 11, 1)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_small_digital_gauge"));

  public static final GaugeBlock INDUSTRIAL_TUBE_GAUGE = (GaugeBlock)(new GaugeBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
    gauge_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(7,4,0, 9, 12, 3)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_tube_gauge"));

  public static final IndicatorBlock INDUSTRIAL_ALARM_LAMP = (IndicatorBlock)(new IndicatorBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|GaugeBlock.GAUGE_DATA_BLINKING,
    alarm_lamp_block_properties(),
    Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 4)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_alarm_lamp"));

  public static final IndicatorBlock INDUSTRIAL_ALARM_SIREN = (IndicatorBlock)(new IndicatorBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|GaugeBlock.GAUGE_DATA_BLINKING,
    indicator_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,6.5,0, 11.5, 9.5, 4),
    new ModResources.BlockSoundEvent(ModResources.ALARM_SIREN_SOUND, 2f),
    null
  )).setRegistryName(new ResourceLocation(MODID, "industrial_alarm_siren"));

  // square LED
  public static final IndicatorBlock INDUSTRIAL_GREEN_LED_INDICATOR = (IndicatorBlock)(new IndicatorBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
    indicator_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_green_led"));

  public static final IndicatorBlock INDUSTRIAL_YELLOW_LED_INDICATOR = (IndicatorBlock)(new IndicatorBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
    indicator_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_yellow_led"));

  public static final IndicatorBlock INDUSTRIAL_RED_LED_INDICATOR = (IndicatorBlock)(new IndicatorBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
    indicator_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_red_led"));

  public static final IndicatorBlock INDUSTRIAL_WHITE_LED_INDICATOR = (IndicatorBlock)(new IndicatorBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
    indicator_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_white_led"));

  public static final IndicatorBlock INDUSTRIAL_GREEN_BLINK_LED_INDICATOR = (IndicatorBlock)(new IndicatorBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|GaugeBlock.GAUGE_DATA_BLINKING,
    indicator_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_green_blinking_led"));

  public static final IndicatorBlock INDUSTRIAL_YELLOW_BLINK_LED_INDICATOR = (IndicatorBlock)(new IndicatorBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|GaugeBlock.GAUGE_DATA_BLINKING,
    indicator_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_yellow_blinking_led"));

  public static final IndicatorBlock INDUSTRIAL_RED_BLINK_LED_INDICATOR = (IndicatorBlock)(new IndicatorBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|GaugeBlock.GAUGE_DATA_BLINKING,
    indicator_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_red_blinking_led"));

  public static final IndicatorBlock INDUSTRIAL_WHITE_BLINK_LED_INDICATOR = (IndicatorBlock)(new IndicatorBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
    indicator_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
  )).setRegistryName(new ResourceLocation(MODID, "industrial_white_blinking_led"));

  // -----------------------------------------------------------------------------------------------------------------
  // -- Rustic
  // -----------------------------------------------------------------------------------------------------------------

  // Rustic lever 1
  public static final BistableSwitchBlock RUSTIC_LEVER = (BistableSwitchBlock)(new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,5,0, 10.3, 15, 4.5),
    Auxiliaries.getPixeledAABB(6,2,0, 10.3, 11, 4.5)
  )).setRegistryName(new ResourceLocation(MODID, "rustic_lever"));

  // Rustic lever 2 (bolted)
  public static final BistableSwitchBlock RUSTIC_TWO_HINGE_LEVER = (BistableSwitchBlock)(new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(2,6,0, 14,13,4.5),
    Auxiliaries.getPixeledAABB(2,4,0, 14,10,4.5)
  )).setRegistryName(new ResourceLocation(MODID, "rustic_two_hinge_lever"));

  // Rustic lever 3 (big angular)
  public static final BistableSwitchBlock RUSTIC_ANGULAR_LEVER = (BistableSwitchBlock)(new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0,10,0, 14,15,4.5),
    Auxiliaries.getPixeledAABB(6, 2,0, 14,15,4.5)
  )).setRegistryName(new ResourceLocation(MODID, "rustic_angular_lever"));

  // Rustic lever 7 (The Nail)
  public static final BistableSwitchBlock RUSTIC_NAIL_LEVER = (BistableSwitchBlock)(new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,7,0, 9,10,3), null
  )).setRegistryName(new ResourceLocation(MODID, "rustic_nail_lever"));

  // Rustic button 1
  public static final PulseSwitchBlock RUSTIC_BUTTON = (PulseSwitchBlock)(new PulseSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5,5,0,11,11,2.5), null
  )).setRegistryName(new ResourceLocation(MODID, "rustic_button"));

  // Rustic button 2 (bolted)
  public static final PulseSwitchBlock RUSTIC_SMALL_BUTTON = (PulseSwitchBlock)(new PulseSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,6,0,10,10,2.5), null
  )).setRegistryName(new ResourceLocation(MODID, "rustic_small_button"));

  // Rustic button 3 (pull chain)
  public static final PulseSwitchBlock RUSTIC_SPRING_RESET_CHAIN = (PulseSwitchBlock)(new PulseSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5,3.5,0,11,15,4), null
  )).setRegistryName(new ResourceLocation(MODID, "rustic_spring_reset_chain"));

  // Rustic button 7 (pull nail)
  public static final PulseSwitchBlock RUSTIC_NAIL_BUTTON = (PulseSwitchBlock)(new PulseSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,7,0, 9,10,3), null
  )).setRegistryName(new ResourceLocation(MODID, "rustic_nail_button"));

  // Rustic door contact mat
  public static final ContactMatBlock RUSTIC_DOOR_CONTACT_PLATE = (ContactMatBlock)(new ContactMatBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(1,0,0, 15,1,12),
    Auxiliaries.getPixeledAABB(1,0,0, 15,0.5,12),
    new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.5f),
    new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.0f)
  )).setRegistryName(new ResourceLocation(MODID, "rustic_door_contact_plate"));

  // Rustic full-size contact plate
  public static final ContactMatBlock RUSTIC_CONTACT_PLATE = (ContactMatBlock)(new ContactMatBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0,0,0, 16,1,16),
    Auxiliaries.getPixeledAABB(0,0,0, 16,0.5,16),
    new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.5f),
    new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.0f)
  )).setRegistryName(new ResourceLocation(MODID, "rustic_contact_plate"));

  // Rustic shock sensor plate
  public static final ContactMatBlock RUSTIC_SHOCK_SENSITIVE_CONTACT_PLATE = (ContactMatBlock)(new ContactMatBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_SHOCK_SENSITIVE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0,0,0, 16,1,16),
    Auxiliaries.getPixeledAABB(0,0,0, 16,0.5,16),
    new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.5f),
    new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.0f)
  )).setRegistryName(new ResourceLocation(MODID, "rustic_shock_sensitive_plate"));

  // Rustic trap door switch (shock vibration sensitive)
  public static final TrapdoorSwitchBlock RUSTIC_SHOCK_SENSITIVE_TRAPDOOR = (TrapdoorSwitchBlock)(new TrapdoorSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_SHOCK_SENSITIVE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0,15.6,0, 16,16,16),
    Auxiliaries.getPixeledAABB(0, 2.0,0, 16,16, 0.1),
    new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.5f),
    new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.0f)
  )).setRegistryName(new ResourceLocation(MODID, "rustic_shock_sensitive_trapdoor"));

  // Rustic trap door switch (high sensitive shock vibration sensitive)
  public static final TrapdoorSwitchBlock RUSTIC_HIGH_SENSITIVE_TRAPDOOR = (TrapdoorSwitchBlock)(new TrapdoorSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_SHOCK_SENSITIVE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_HIGH_SENSITIVE|
    SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0,15.6,0, 16,16,16),
    Auxiliaries.getPixeledAABB(0, 2.0,0, 16,16, 0.1),
    new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.5f),
    new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.0f)
    //Auxiliaries.RsMaterials.MATERIAL_TRAPDOORSWITCH
  )).setRegistryName(new ResourceLocation(MODID, "rustic_high_sensitive_trapdoor"));

  // Rustic trap door switch (item trap door)
  public static final TrapdoorSwitchBlock RUSTIC_FALLTHROUGH_DETECTOR = (TrapdoorSwitchBlock)(new TrapdoorSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
    SwitchBlock.SWITCH_CONFIG_INVERTABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0,12.6,0, 16,13,16),
    Auxiliaries.getPixeledAABB(0,12.6,0, 16,13,16),
    new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.5f),
    null
  )).setRegistryName(new ResourceLocation(MODID, "rustic_fallthrough_detector"));

  public static final GaugeBlock RUSTIC_CIRCULAR_GAUGE = (GaugeBlock)(new GaugeBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
    gauge_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(2,2,0, 14,14,1)
  )).setRegistryName(new ResourceLocation(MODID, "rustic_circular_gauge"));

  public static final IndicatorBlock RUSTIC_SEMAPHORE_INDICATOR = (IndicatorBlock)(new IndicatorBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
    indicator_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(3,4,0, 13,11,1),
    null,
    null
  )).setRegistryName(new ResourceLocation(MODID, "rustic_semaphore"));


  // -----------------------------------------------------------------------------------------------------------------
  // -- Glass
  // -----------------------------------------------------------------------------------------------------------------

  // Thin star shaped glass switch
  public static final BistableSwitchBlock GLASS_ROTARY_SWITCH = (BistableSwitchBlock)(new BistableSwitchBlock(
    SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_faint_light_block_properties(),
    Auxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.5), null
  )).setRegistryName(new ResourceLocation(MODID, "glass_rotary_switch"));

  // Bistable glass touch switch
  public static final BistableSwitchBlock GLASS_TOUCH_SWITCH = (BistableSwitchBlock)(new BistableSwitchBlock(
    SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_faint_light_block_properties(),
    Auxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.5), null
  )).setRegistryName(new ResourceLocation(MODID, "glass_touch_switch"));

  // Thin star shaped glass button
  public static final PulseSwitchBlock GLASS_BUTTON = (PulseSwitchBlock)(new PulseSwitchBlock(
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
    SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|
    SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_faint_light_block_properties(),
    Auxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.5), null
  )).setRegistryName(new ResourceLocation(MODID, "glass_button"));

  // Thin small star shaped glass button
  public static final PulseSwitchBlock GLASS_SMALL_BUTTON = (PulseSwitchBlock)(new PulseSwitchBlock(
    SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_faint_light_block_properties(),
    Auxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.5), null
  )).setRegistryName(new ResourceLocation(MODID, "glass_small_button"));

  // Glass touch button
  public static final PulseSwitchBlock GLASS_TOUCH_BUTTON = (PulseSwitchBlock)(new PulseSwitchBlock(
    SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_faint_light_block_properties(),
    Auxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.5), null
  )).setRegistryName(new ResourceLocation(MODID, "glass_touch_button"));

  // Glass door plate
  public static final ContactMatBlock GLASS_DOOR_CONTACT_MAT = (ContactMatBlock)(new ContactMatBlock(
    SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0,0,0, 16,0.25,16), null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.0f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.0f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "glass_door_contact_mat"));

  // Glass plate
  public static final ContactMatBlock GLASS_CONTACT_MAT = (ContactMatBlock)(new ContactMatBlock(
    SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0,0,0, 16,0.25,16), null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.0f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.0f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "glass_contact_mat"));

  // Glass Day time switch
  public static final DayTimerSwitchBlock GLASS_DAY_TIMER = (DayTimerSwitchBlock)(new DayTimerSwitchBlock(
    SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|
    SwitchBlock.SWITCH_CONFIG_TIMER_DAYTIME|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.1), null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.0f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.0f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "glass_day_timer"));

  // Glass interval signal timer
  public static final IntervalTimerSwitchBlock GLASS_INTERVAL_TIMER = (IntervalTimerSwitchBlock)(new IntervalTimerSwitchBlock(
    SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|
    SwitchBlock.SWITCH_CONFIG_TIMER_INTERVAL|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.1), null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.0f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.0f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "glass_interval_timer"));

  // Glass infrared motion sensor
  public static final EntityDetectorSwitchBlock GLASS_ENTITY_DETECTOR = (EntityDetectorSwitchBlock)(new EntityDetectorSwitchBlock(
    SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|SwitchBlock.SWITCH_CONFIG_SENSOR_VOLUME|
    SwitchBlock.SWITCH_CONFIG_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.1), null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.0f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.0f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "glass_entity_detector"));

  // Glass laser motion sensor
  public static final EntityDetectorSwitchBlock GLASS_LINEAR_ENTITY_DETECTOR = (EntityDetectorSwitchBlock)(new EntityDetectorSwitchBlock(
    SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_SENSOR_LINEAR|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.1), null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.0f, 1.3f),
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.0f, 1.2f)
  )).setRegistryName(new ResourceLocation(MODID, "glass_linear_entity_detector"));

  public static final GaugeBlock GLASS_VERTICAL_BAR_GAUGE = (GaugeBlock)(new GaugeBlock(
    RsBlock.RSBLOCK_CONFIG_CUTOUT,
    gauge_glass_block_properties(),
    Auxiliaries.getPixeledAABB(7,3.7,0, 10,12,0.4)
  )).setRegistryName(new ResourceLocation(MODID, "glass_vertical_bar_gauge"));

  // -----------------------------------------------------------------------------------------------------------------
  // -- Old Fancy
  // -----------------------------------------------------------------------------------------------------------------

  // Old fancy gold decorated lever
  public static final BistableSwitchBlock OLDFANCY_BISTABLE_SWITCH1 = (BistableSwitchBlock)(new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,6.5,0, 10.3,13.5,4.5),
    Auxiliaries.getPixeledAABB(6,3.5,0, 10.3,10.0,4.5)
  )).setRegistryName(new ResourceLocation(MODID, "oldfancy_bistableswitch1"));

  // Old fancy angular lever
  public static final BistableSwitchBlock OLDFANCY_BISTABLE_SWITCH2 = (BistableSwitchBlock)(new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(2.5,6.0,0, 9.7,10,4.5),
    Auxiliaries.getPixeledAABB(4.5,3.5,0, 9.2,10,4.5)
  )).setRegistryName(new ResourceLocation(MODID, "oldfancy_bistableswitch2"));

  // Old fancy (golden decorated) button
  public static final PulseSwitchBlock OLDFANCY_BUTTON = (PulseSwitchBlock)(new PulseSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,6,0,10,10,1.5), null
  )).setRegistryName(new ResourceLocation(MODID, "oldfancy_button"));

  // Old fancy (golden decorated) chain pulse switch
  public static final PulseSwitchBlock OLDFANCY_SPRING_RESET_CHAIN = (PulseSwitchBlock)(new PulseSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6.5,4.8,0,9.5,13,4),
    Auxiliaries.getPixeledAABB(6.5,3.8,0,9.5,12,4)
  )).setRegistryName(new ResourceLocation(MODID, "oldfancy_spring_reset_chain"));

  // Old fancy (golden decorated) tiny button
  public static final PulseSwitchBlock OLDFANCY_SMALL_BUTTON = (PulseSwitchBlock)(new PulseSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(7,7,0,9,9,1.5), null
  )).setRegistryName(new ResourceLocation(MODID, "oldfancy_small_button"));

  // -----------------------------------------------------------------------------------------------------------------
  // -- Other
  // -----------------------------------------------------------------------------------------------------------------

  // Yellow power plant
  public static final PowerPlantBlock YELLOW_POWER_PLANT = (PowerPlantBlock)(new PowerPlantBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5,0,5, 11,9,11), null,
    new ModResources.BlockSoundEvent(SoundEvents.GRASS_BREAK, 0.09f, 3.6f),
    new ModResources.BlockSoundEvent(SoundEvents.GRASS_BREAK, 0.04f, 3.0f)
    // Auxiliaries.RsMaterials.MATERIAL_PLANT
  )).setRegistryName(new ResourceLocation(MODID, "yellow_power_plant"));

  // Red power plant
  public static final PowerPlantBlock RED_POWER_PLANT = (PowerPlantBlock)(new PowerPlantBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5,0,5, 11,9,11), null,
    new ModResources.BlockSoundEvent(SoundEvents.GRASS_BREAK, 0.09f, 3.6f),
    new ModResources.BlockSoundEvent(SoundEvents.GRASS_BREAK, 0.04f, 3.0f)
    // Auxiliaries.RsMaterials.MATERIAL_PLANT
  )).setRegistryName(new ResourceLocation(MODID, "red_power_plant"));

  // Light flip switch
  public static final BistableSwitchBlock LIGHT_SWITCH = (BistableSwitchBlock)(new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(7,6,0,9,10,1.5), null
  )).setRegistryName(new ResourceLocation(MODID, "light_switch"));

  // Arrow target
  public static final PulseSwitchBlock ARROW_TARGET_SWITCH = (PulseSwitchBlock)(new PulseSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
    SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5,5,0,11,11,1), null
  )).setRegistryName(new ResourceLocation(MODID, "arrow_target"));

  // Valve Wheel
  public static final BistableSwitchBlock BISTABLE_VALVE_WHEEL_SWITCH = (BistableSwitchBlock)(new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0,12,12,3.5), null
  )).setRegistryName(new ResourceLocation(MODID, "valve_wheel_switch"));

  // Elevator button
  public static final ElevatorSwitchBlock ELEVATOR_BUTTON = (ElevatorSwitchBlock)(new ElevatorSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_faint_light_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1), null
  )).setRegistryName(new ResourceLocation(MODID, "elevator_button"));

  // Door sensor
  public static final DoorSensorSwitchBlock DOOR_SENSOR_SWITCH = (DoorSensorSwitchBlock)(new DoorSensorSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_LATERAL_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
    SwitchBlock.SWITCH_CONFIG_INVERTABLE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5,0,0, 11,1, 1.5),
    null,
    new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.05f, 2.5f),
    null
  )).setRegistryName(new ResourceLocation(MODID, "door_sensor_switch"));

  // -----------------------------------------------------------------------------------------------------------------
  // -- sensitive glass
  // -----------------------------------------------------------------------------------------------------------------

  public static final SensitiveGlassBlock EMITTING_SENSITIVE_GLASS_BLOCK = (SensitiveGlassBlock)(new SensitiveGlassBlock(
    light_emitting_sensitive_glass_block_properties()
  )).setRegistryName(new ResourceLocation(MODID, "sensitive_glass_block"));

  public static final SensitiveGlassBlock COLORED_SENSITIVE_GLASS_BLOCK = (SensitiveGlassBlock)(new SensitiveGlassBlock(
    colored_sensitive_glass_block_properties()
  )).setRegistryName(new ResourceLocation(MODID, "stained_sensitiveglass"));

  // -----------------------------------------------------------------------------------------------------------------
  // -- Testing blocks
  // -----------------------------------------------------------------------------------------------------------------

  // Testing CUBE
  public static final SwitchBlock TESTING_QUBE = (SwitchBlock)(new SwitchBlock(
    SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    AbstractBlock.Properties.of(Material.WOOL, MaterialColor.METAL).strength(0.1f, 32000f).sound(SoundType.METAL),
    new AxisAlignedBB(0,0,0,1,1,1), null,
    null, null
  )).setRegistryName(new ResourceLocation(MODID, "qube"));

  // -----------------------------------------------------------------------------------------------------------------
  // -- All blocks
  // -----------------------------------------------------------------------------------------------------------------

  private static final ArrayList<Block> registeredBlocks = new ArrayList<Block>(Arrays.asList(
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
    INDUSTRIAL_SWITCHLINK_RECEIVER_ANALOG,
    INDUSTRIAL_SWITCHLINK_CASED_PULSE_RECEIVER,
    INDUSTRIAL_SWITCHLINK_RELAY,
    INDUSTRIAL_SWITCHLINK_PULSE_RELAY,
    INDUSTRIAL_SWITCHLINK_RELAY_ANALOG,
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
    DOOR_SENSOR_SWITCH,
    // Senesitive Glass
    EMITTING_SENSITIVE_GLASS_BLOCK,
    COLORED_SENSITIVE_GLASS_BLOCK
//    TESTING_QUBE
  ));

  //--------------------------------------------------------------------------------------------------------------------
  // Tile entities bound exclusively to the blocks above
  //--------------------------------------------------------------------------------------------------------------------

  private static Block[] blocks_of_type(Class<? extends Block> clazz)
  { return registeredBlocks.stream().filter(clazz::isInstance).toArray(Block[]::new); }

  public static final TileEntityType<?> TET_GAUGE = TileEntityType.Builder
    .of(AbstractGaugeBlock.GaugeTileEntity::new, blocks_of_type(AbstractGaugeBlock.class))
    .build(null)
    .setRegistryName(ModRsGauges.MODID, "te_gauge");

  public static final TileEntityType<?> TET_SWITCH = TileEntityType.Builder
    .of(SwitchBlock.SwitchTileEntity::new, blocks_of_type(SwitchBlock.class))
    .build(null)
    .setRegistryName(ModRsGauges.MODID, "te_switch");

  public static final TileEntityType<?> TET_CONTACT_SWITCH = TileEntityType.Builder
    .of(ContactSwitchBlock.ContactSwitchTileEntity::new, blocks_of_type(ContactSwitchBlock.class))
    .build(null)
    .setRegistryName(ModRsGauges.MODID, "te_contact_switch");

  public static final TileEntityType<?> TET_DETECTOR_SWITCH = TileEntityType.Builder
    .of(EntityDetectorSwitchBlock.DetectorSwitchTileEntity::new, blocks_of_type(EntityDetectorSwitchBlock.class))
    .build(null)
    .setRegistryName(ModRsGauges.MODID, "te_detector_switch");

  public static final TileEntityType<?> TET_ENVSENSOR_SWITCH = TileEntityType.Builder
    .of(EnvironmentalSensorSwitchBlock.EnvironmentalSensorSwitchTileEntity::new, blocks_of_type(EnvironmentalSensorSwitchBlock.class))
    .build(null)
    .setRegistryName(ModRsGauges.MODID, "te_envsensor_switch");

  public static final TileEntityType<?> TET_DAYTIMER_SWITCH = TileEntityType.Builder
    .of(DayTimerSwitchBlock.DayTimerSwitchTileEntity::new, blocks_of_type(DayTimerSwitchBlock.class))
    .build(null)
    .setRegistryName(ModRsGauges.MODID, "te_daytimer_switch");

  public static final TileEntityType<?> TET_TIMER_SWITCH = TileEntityType.Builder
    .of(IntervalTimerSwitchBlock.IntervalTimerSwitchTileEntity::new, blocks_of_type(IntervalTimerSwitchBlock.class))
    .build(null)
    .setRegistryName(ModRsGauges.MODID, "te_timer_switch");

  public static final TileEntityType<?> TET_COMPARATOR_SWITCH = TileEntityType.Builder
    .of(ComparatorSwitchBlock.ComparatorSwitchTileEntity::new, blocks_of_type(ComparatorSwitchBlock.class))
    .build(null)
    .setRegistryName(ModRsGauges.MODID, "te_comparator_switch");

  public static final TileEntityType<?> TET_OBSERVER_SWITCH = TileEntityType.Builder
    .of(ObserverSwitchBlock.ObserverSwitchTileEntity::new, blocks_of_type(ObserverSwitchBlock.class))
    .build(null)
    .setRegistryName(ModRsGauges.MODID, "te_observer_switch");

  public static final TileEntityType<?> TET_DOORSENSOR_SWITCH = TileEntityType.Builder
    .of(DoorSensorSwitchBlock.DoorSensorSwitchTileEntity::new, blocks_of_type(DoorSensorSwitchBlock.class))
    .build(null)
    .setRegistryName(ModRsGauges.MODID, "te_doorsensor_switch");

  private static final TileEntityType<?> tile_entity_types[] = {
    TET_GAUGE,
    TET_SWITCH,
    TET_CONTACT_SWITCH,
    TET_DETECTOR_SWITCH,
    TET_ENVSENSOR_SWITCH,
    TET_DAYTIMER_SWITCH,
    TET_TIMER_SWITCH,
    TET_COMPARATOR_SWITCH,
    TET_OBSERVER_SWITCH,
    TET_DOORSENSOR_SWITCH
  };

  //--------------------------------------------------------------------------------------------------------------------
  // Items
  //--------------------------------------------------------------------------------------------------------------------

  private static Item.Properties default_item_properties()
  { return (new Item.Properties()).tab(ModRsGauges.ITEMGROUP); }

  public static final SwitchLinkPearlItem SWITCH_LINK_PEARL = (SwitchLinkPearlItem)(new SwitchLinkPearlItem(
    default_item_properties()
  ).setRegistryName(MODID, "switchlink_pearl"));

  private static final ArrayList<Item> registeredItems = new ArrayList<Item>(Arrays.asList(
    SWITCH_LINK_PEARL
  ));

  //--------------------------------------------------------------------------------------------------------------------
  // Initialisation events
  //--------------------------------------------------------------------------------------------------------------------

  @Nonnull
  public static List<Block> getRegisteredBlocks()
  { return Collections.unmodifiableList(registeredBlocks); }

  @Nonnull
  public static List<Item> getRegisteredItems()
  { return Collections.unmodifiableList(registeredItems); }

  public static boolean isExperimentalBlock(Block block)
  { return (block==TESTING_QUBE) || (block instanceof IExperimentalFeature); }

  public static final void registerBlocks(final RegistryEvent.Register<Block> event)
  {
    for(Block e:registeredBlocks) event.getRegistry().register(e);
    Auxiliaries.logInfo("Registered " + Integer.toString(registeredBlocks.size()) + " blocks.");
  }

  public static final void registerItems(final RegistryEvent.Register<Item> event)
  {
    for(Item e:registeredItems) event.getRegistry().register(e);
    Auxiliaries.logInfo("Registered " + Integer.toString(registeredItems.size()) + " items.");
  }

  public static final void registerBlockItems(final RegistryEvent.Register<Item> event)
  {
    for(Block e:registeredBlocks) {
      ResourceLocation rl = e.getRegistryName();
      if(rl == null) continue;
      event.getRegistry().register(new BlockItem(e, (new Item.Properties().tab(ModRsGauges.ITEMGROUP))).setRegistryName(rl));
    }
  }

  public static final void registerTileEntities(final RegistryEvent.Register<TileEntityType<?>> event)
  {
    for(final TileEntityType<?> e:tile_entity_types) event.getRegistry().register(e);
    Auxiliaries.logInfo("Registered " + Integer.toString(tile_entity_types.length) + " tile entities.");
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
            RenderTypeLookup.setRenderLayer(block, RenderType.cutout());
            break;
          case CUTOUT_MIPPED:
            RenderTypeLookup.setRenderLayer(block, RenderType.cutoutMipped());
            break;
          case TRANSLUCENT:
            RenderTypeLookup.setRenderLayer(block, RenderType.translucent());
            break;
          case SOLID:
            break;
        }
      }
    }
  }
}
