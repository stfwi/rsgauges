/*
 * @file ModBlocks.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Definition and initialization of blocks of this
 * module, along with their tile entities if applicable.
 *
 * Note: Straight forward definition of different blocks/entities
 *       to make recipes, models and texture definitions easier.
 */
package wile.rsgauges;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ObjectHolder;
import wile.rsgauges.blocks.*;
import wile.rsgauges.detail.ModResources;
import wile.rsgauges.items.SwitchLinkPearlItem;
import wile.rsgauges.libmc.detail.Auxiliaries;
import wile.rsgauges.libmc.detail.Registries;


public class ModContent
{
  private static class detail
  {
    public static String MODID = "";

    //public static Boolean disallowSpawn(BlockState state, BlockGetter reader, BlockPos pos, EntityType<?> entity) { return false; }

    public static final BlockBehaviour.Properties gauge_metallic_block_properties()
    { return BlockBehaviour.Properties.of(Material.METAL, MaterialColor.METAL).strength(0.5f, 15f).sound(SoundType.METAL).noCollission().isValidSpawn((s,w,p,e)->false); }

    public static final BlockBehaviour.Properties gauge_glass_block_properties()
    { return (BlockBehaviour.Properties.of(Material.METAL, MaterialColor.METAL).strength(0.5f, 15f).sound(SoundType.METAL).noOcclusion().isValidSpawn((s,w,p,e)->false)); }

    public static final BlockBehaviour.Properties indicator_metallic_block_properties()
    { return BlockBehaviour.Properties.of(Material.METAL, MaterialColor.METAL).strength(0.5f, 15f).sound(SoundType.METAL).lightLevel((state)->3).noOcclusion().isValidSpawn((s,w,p,e)->false); }

    public static final BlockBehaviour.Properties indicator_glass_block_properties()
    { return BlockBehaviour.Properties.of(Material.METAL, MaterialColor.METAL).strength(0.5f, 15f).sound(SoundType.METAL).lightLevel((state)->3).noOcclusion().isValidSpawn((s,w,p,e)->false); }

    public static final BlockBehaviour.Properties alarm_lamp_block_properties()
    { return BlockBehaviour.Properties.of(Material.METAL, MaterialColor.METAL).strength(0.5f, 15f).sound(SoundType.METAL).noOcclusion().lightLevel((state)->state.getValue(IndicatorBlock.POWERED)?12:2).isValidSpawn((s,w,p,e)->false); }

    public static final BlockBehaviour.Properties colored_sensitive_glass_block_properties()
    { return (BlockBehaviour.Properties.of(Material.BUILDABLE_GLASS, MaterialColor.METAL).strength(0.35f, 15f).sound(SoundType.METAL).noOcclusion().isValidSpawn((s,w,p,e)->false)); }

    public static final BlockBehaviour.Properties light_emitting_sensitive_glass_block_properties()
    { return BlockBehaviour.Properties.of(Material.BUILDABLE_GLASS, MaterialColor.METAL).strength(0.35f, 15f).sound(SoundType.METAL).noOcclusion().emissiveRendering((s,w,p)->true).lightLevel((state)->state.getValue(SensitiveGlassBlock.POWERED)?15:0).isValidSpawn((s,w,p,e)->false); }

    public static final BlockBehaviour.Properties switch_metallic_block_properties()
    { return gauge_metallic_block_properties(); }

    public static final BlockBehaviour.Properties switch_glass_block_properties()
    { return gauge_glass_block_properties(); }

    public static final BlockBehaviour.Properties switch_metallic_faint_light_block_properties()
    { return BlockBehaviour.Properties.of(Material.METAL, MaterialColor.METAL).strength(0.5f, 15f).sound(SoundType.METAL).lightLevel((state)->5); }

    private static Item.Properties default_item_properties()
    { return (new Item.Properties()).tab(Registries.getCreativeModeTab()); }
  }

  public static void init(String modid)
  {
    detail.MODID = modid;
    initTags();
    initBlocks();
    initItems();
  }

  private static void initTags()
  {
    Registries.addOptionalBlockTag("clay_like", "minecraft:clay");
    Registries.addOptionalBlockTag("glass_like", "minecraft:glass");
    Registries.addOptionalBlockTag("logs", "minecraft:oak_log");
    Registries.addOptionalBlockTag("ores", "minecraft:iron_ore");
    Registries.addOptionalBlockTag("planks", "minecraft:oak_planks");
    Registries.addOptionalBlockTag("plants", "minecraft:dandelion");
    Registries.addOptionalBlockTag("saplings", "minecraft:oak_sapling");
    Registries.addOptionalBlockTag("slabs", "minecraft:oak_slab");
    Registries.addOptionalBlockTag("soils", "minecraft:farmland");
    Registries.addOptionalBlockTag("stone_like", "minecraft:stone");
    Registries.addOptionalBlockTag("water_like", "minecraft:water");
    Registries.addOptionalBlockTag("wooden", "minecraft:oak_log");
  }

  public static void initBlocks()
  {
    // Contact lever switch
    Registries.addBlock("industrial_small_lever", ()->new BistableSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(4,4,0,12,15,4),
      Auxiliaries.getPixeledAABB(4,1,0,12,12,4)
    ));

    // Mechanical lever
    Registries.addBlock("industrial_lever", ()->new BistableSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(5,4,0,11,15,5),
      Auxiliaries.getPixeledAABB(5,1,0,11,12,5)
    ));

    // Mechanical rotary lever
    Registries.addBlock("industrial_rotary_lever", ()->new BistableSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(1,4,0,12,12,6),
      Auxiliaries.getPixeledAABB(1,1,0,12,12,6)
    ));

    // Rotary machine switch
    Registries.addBlock("industrial_rotary_machine_switch", ()->new BistableSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null
    ));

    // Two-button machine switch
    Registries.addBlock("industrial_machine_switch", ()->new BistableSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null
    ));

    // ESTOP button
    Registries.addBlock("industrial_estop_switch", ()->new BistableSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
        SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE_OFF,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(5,5,0, 11, 11, 2.5),
      Auxiliaries.getPixeledAABB(5,5,0, 11, 11, 3.5)
    ));

    // Hopper blocking switch
    Registries.addBlock("industrial_hopper_switch", ()->new BistableSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
        SwitchBlock.SWITCH_DATA_WEAK,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(3,10,0, 13, 12, 6.7),
      Auxiliaries.getPixeledAABB(3,10,0, 13, 12, 3.7)
    ));

    // Square machine pulse switch
    Registries.addBlock("industrial_button", ()->new PulseSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(5,5,0, 11, 11, 2), null
    ));

    // Fenced round machine pulse switch
    Registries.addBlock("industrial_fenced_button", ()->new PulseSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
        SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(5,5,0, 11, 11, 2), null
    ));

    // Retro double pole switch
    Registries.addBlock("industrial_double_pole_button", ()->new PulseSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
        SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 3),
      Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 2)
    ));

    // Mechanical spring reset push button
    Registries.addBlock("industrial_foot_button", ()->new PulseSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
        SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(5,3,0, 11, 7, 4), null
    ));

    // Mechanical spring reset pull handle
    Registries.addBlock("industrial_pull_handle", ()->new PulseSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
        SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 2),
      Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 2)
    ));

    // Manual dimmer
    Registries.addBlock("industrial_dimmer", ()->new DimmerSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(4,1,0, 12, 15, 2),
      null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.9f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.7f)
    ));

    // Door contact mat
    Registries.addBlock("industrial_door_contact_mat", ()->new ContactMatBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(1,0,0, 15, 1, 13), null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
    ));

    // Sensitive full size contact mat
    Registries.addBlock("industrial_contact_mat", ()->new ContactMatBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
        SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(0,0,0, 16, 1, 16), null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
    ));

    // Industrial shock sensor contact mat
    Registries.addBlock("industrial_shock_sensitive_contact_mat", ()->new ContactMatBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
        SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_SHOCK_SENSITIVE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(0,0,0, 16, 1, 16), null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
    ));

    // Industrial trap door switch (shock vibration sensitive)
    Registries.addBlock("industrial_shock_sensitive_trapdoor", ()->new TrapdoorSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_SHOCK_SENSITIVE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(0,15.6,0, 16, 16, 16),
      Auxiliaries.getPixeledAABB(0,2,0, 16, 1, 0.1),
      new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 3.0f),
      new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.5f)
    ));

    // Industrial trap door switch (high sensitive shock vibration sensitive)
    Registries.addBlock("industrial_high_sensitive_trapdoor", ()->new TrapdoorSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_SHOCK_SENSITIVE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
        SwitchBlock.SWITCH_CONFIG_HIGH_SENSITIVE,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(0,15.6,0, 16, 16, 16),
      Auxiliaries.getPixeledAABB(0,2,0, 16, 1, 0.1),
      new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.5f),
      new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.0f)
      // Auxiliaries.RsMaterials.MATERIAL_TRAPDOORSWITCH
    ));

    // Industrial trap door switch (item trap door)
    Registries.addBlock("industrial_fallthrough_detector", ()->new TrapdoorSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
        SwitchBlock.SWITCH_CONFIG_INVERTABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(0.1,12.6,0.1, 15.9,13, 15.9),
      Auxiliaries.getPixeledAABB(0.1,12.6,0.1, 15.9,13, 15.9),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.05f, 2.5f),
      null
    ));

    // Day time switch
    Registries.addBlock("industrial_day_timer", ()->new DayTimerSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_TIMER_DAYTIME|
        SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
    ));

    // Interval signal timer
    Registries.addBlock("industrial_interval_timer", ()->new IntervalTimerSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_TIMER_INTERVAL|
        SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
    ));

    // Infrared motion_sensor
    Registries.addBlock("industrial_entity_detector", ()->new EntityDetectorSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_SENSOR_VOLUME|
        SwitchBlock.SWITCH_CONFIG_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 1), null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
    ));

    // Linear laser motion sensor
    Registries.addBlock("industrial_linear_entity_detector", ()->new EntityDetectorSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_SENSOR_LINEAR|
        SwitchBlock.SWITCH_CONFIG_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 1), null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
    ));

    // Local light sensor
    Registries.addBlock("industrial_light_sensor", ()->new EnvironmentalSensorSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_SENSOR_LIGHT|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
    ));

    // Rain sensor switch
    Registries.addBlock("industrial_rain_sensor", ()->new EnvironmentalSensorSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_SENSOR_RAIN|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
    ));

    // Lightning sensor switch
    Registries.addBlock("industrial_lightning_sensor", ()->new EnvironmentalSensorSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_SENSOR_LIGHTNING|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
    ));

    // Comparator output level observing switch
    Registries.addBlock("industrial_comparator_switch", ()->new ComparatorSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(4,10,0, 12, 15, 1.5), null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
    ));

    // Uni-directional block detector switch
    Registries.addBlock("industrial_block_detector", ()->new ObserverSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_SENSOR_BLOCKDETECT|
        SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
        SwitchBlock.SWITCH_DATA_SIDE_ENABLED_BOTTOM|SwitchBlock.SWITCH_DATA_SIDE_ENABLED_TOP|
        SwitchBlock.SWITCH_DATA_SIDE_ENABLED_FRONT|SwitchBlock.SWITCH_DATA_SIDE_ENABLED_LEFT|
        SwitchBlock.SWITCH_DATA_SIDE_ENABLED_RIGHT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(0.5,0.5,0.5, 15.5, 15.5, 15.5), null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.2f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.2f, 1.2f)
    ));

    // Industrial bistable link receiver switch
    Registries.addBlock("industrial_switchlink_receiver", ()->new LinkReceiverSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_BISTABLE|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.9f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.7f),
      false
    ));

    // Industrial analog link receiver
    Registries.addBlock("industrial_switchlink_receiver_analog", ()->new LinkReceiverSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_BISTABLE|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.9f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.7f),
      true
    ));

    // Industrial full block bistable link receiver switch
    Registries.addBlock("industrial_switchlink_cased_receiver", ()->new LinkReceiverSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_FULLCUBE|SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|
        SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
        SwitchBlock.SWITCH_CONFIG_INVERTABLE|SwitchBlock.SWITCH_DATA_SIDE_ENABLED_ALL|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(0,0,0, 16, 16, 16), null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.9f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.7f),
      false
    ));

    // Industrial pulse link receiver switch
    Registries.addBlock("industrial_switchlink_pulse_receiver", ()->new LinkReceiverSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
        SwitchBlock.SWITCH_CONFIG_INVERTABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.9f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.7f),
      false
    ));

    // Industrial full block pulse link receiver switch
    Registries.addBlock("industrial_switchlink_cased_pulse_receiver", ()->new LinkReceiverSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_FULLCUBE|SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|
        SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
        SwitchBlock.SWITCH_CONFIG_INVERTABLE|SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
        SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_DATA_SIDE_ENABLED_ALL|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(0,0,0, 16, 16, 16), null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.9f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.7f),
      false
    ));

    // Industrial bistable link relay
    Registries.addBlock("industrial_switchlink_relay", ()->new LinkSenderSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_LINK_SENDER|
        SwitchBlock.SWITCH_CONFIG_INVERTABLE|SwitchBlock.SWITCH_DATA_WEAK|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.9f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.7f),
      false
    ));

    // Industrial analog link relay
    Registries.addBlock("industrial_switchlink_relay_analog", ()->new LinkSenderSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_LINK_SENDER|
        SwitchBlock.SWITCH_CONFIG_INVERTABLE|SwitchBlock.SWITCH_DATA_WEAK|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.9f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.7f),
      true
    ));

    // Industrial pulse link relay
    Registries.addBlock("industrial_switchlink_pulse_relay", ()->new LinkSenderSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_LINK_SENDER|
        SwitchBlock.SWITCH_CONFIG_INVERTABLE|SwitchBlock.SWITCH_DATA_WEAK|
        SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.9f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.01f, 1.7f),
      false
    ));

    // Bistable industrial knock surge detctor
    Registries.addBlock("industrial_knock_switch", ()->new BistableKnockSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_OPOSITE_PLACEMENT|SwitchBlock.RSBLOCK_CONFIG_FULLCUBE|
        SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
        SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|SwitchBlock.SWITCH_DATA_SIDE_ENABLED_ALL,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(0.5,0.5,0.5, 15.5, 15.5, 15.5), null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.2f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.2f, 1.2f)
    ));

    // Pulse industrial knock surge detctor
    Registries.addBlock("industrial_knock_button", ()->new PulseKnockSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.RSBLOCK_CONFIG_OPOSITE_PLACEMENT|SwitchBlock.RSBLOCK_CONFIG_FULLCUBE|
        SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
        SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|SwitchBlock.SWITCH_DATA_SIDE_ENABLED_ALL,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(0.5,0.5,0.5, 15.5, 15.5, 15.5), null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.2f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.2f, 1.2f)
    ));

    Registries.addBlock("industrial_analog_angular_gauge", ()->new GaugeBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
      detail.gauge_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(2,2,0, 14,14,1)
    ));

    Registries.addBlock("industrial_analog_horizontal_gauge", ()->new GaugeBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
      detail.gauge_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(2,4,0, 14, 12, 1)
    ));

    Registries.addBlock("industrial_vertical_bar_gauge", ()->new GaugeBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
      detail.gauge_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(4,2,0, 12, 14, 1)
    ));

    Registries.addBlock("industrial_small_digital_gauge", ()->new GaugeBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
      detail.gauge_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(4,5,0, 12, 11, 1)
    ));

    Registries.addBlock("industrial_tube_gauge", ()->new GaugeBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
      detail.gauge_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(7,4,0, 9, 12, 3)
    ));

    Registries.addBlock("industrial_alarm_lamp", ()->new IndicatorBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|GaugeBlock.GAUGE_DATA_BLINKING,
      detail.alarm_lamp_block_properties(),
      Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 4)
    ));

    Registries.addBlock("industrial_alarm_siren", ()->new IndicatorBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|GaugeBlock.GAUGE_DATA_BLINKING,
      detail.indicator_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(4,6.5,0, 11.5, 9.5, 4),
      new ModResources.BlockSoundEvent(ModResources.ALARM_SIREN_SOUND, 2f),
      null
    ));

    // square LED
    Registries.addBlock("industrial_green_led", ()->new IndicatorBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
      detail.indicator_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
    ));

    Registries.addBlock("industrial_yellow_led", ()->new IndicatorBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
      detail.indicator_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
    ));

    Registries.addBlock("industrial_red_led", ()->new IndicatorBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
      detail.indicator_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
    ));

    Registries.addBlock("industrial_white_led", ()->new IndicatorBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
      detail.indicator_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
    ));

    Registries.addBlock("industrial_green_blinking_led", ()->new IndicatorBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|GaugeBlock.GAUGE_DATA_BLINKING,
      detail.indicator_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
    ));

    Registries.addBlock("industrial_yellow_blinking_led", ()->new IndicatorBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|GaugeBlock.GAUGE_DATA_BLINKING,
      detail.indicator_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
    ));

    Registries.addBlock("industrial_red_blinking_led", ()->new IndicatorBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|GaugeBlock.GAUGE_DATA_BLINKING,
      detail.indicator_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
    ));

    Registries.addBlock("industrial_white_blinking_led", ()->new IndicatorBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
      detail.indicator_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
    ));

    // -----------------------------------------------------------------------------------------------------------------
    // -- Rustic
    // -----------------------------------------------------------------------------------------------------------------

    // Rustic lever 1
    Registries.addBlock("rustic_lever", ()->new BistableSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(6,5,0, 10.3, 15, 4.5),
      Auxiliaries.getPixeledAABB(6,2,0, 10.3, 11, 4.5)
    ));

    // Rustic lever 2 (bolted)
    Registries.addBlock("rustic_two_hinge_lever", ()->new BistableSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(2,6,0, 14,13,4.5),
      Auxiliaries.getPixeledAABB(2,4,0, 14,10,4.5)
    ));

    // Rustic lever 3 (big angular)
    Registries.addBlock("rustic_angular_lever", ()->new BistableSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(0,10,0, 14,15,4.5),
      Auxiliaries.getPixeledAABB(6, 2,0, 14,15,4.5)
    ));

    // Rustic lever 7 (The Nail)
    Registries.addBlock("rustic_nail_lever", ()->new BistableSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(6,7,0, 9,10,3), null
    ));

    // Rustic button 1
    Registries.addBlock("rustic_button", ()->new PulseSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(5,5,0,11,11,2.5), null
    ));

    // Rustic button 2 (bolted)
    Registries.addBlock("rustic_small_button", ()->new PulseSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(6,6,0,10,10,2.5), null
    ));

    // Rustic button 3 (pull chain)
    Registries.addBlock("rustic_spring_reset_chain", ()->new PulseSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
        SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(5,3.5,0,11,15,4), null
    ));

    // Rustic button 7 (pull nail)
    Registries.addBlock("rustic_nail_button", ()->new PulseSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
        SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(6,7,0, 9,10,3), null
    ));

    // Rustic door contact mat
    Registries.addBlock("rustic_door_contact_plate", ()->new ContactMatBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(1,0,0, 15,1,12),
      Auxiliaries.getPixeledAABB(1,0,0, 15,0.5,12),
      new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.5f),
      new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.0f)
    ));

    // Rustic full-size contact plate
    Registries.addBlock("rustic_contact_plate", ()->new ContactMatBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
        SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(0,0,0, 16,1,16),
      Auxiliaries.getPixeledAABB(0,0,0, 16,0.5,16),
      new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.5f),
      new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.0f)
    ));

    // Rustic shock sensor plate
    Registries.addBlock("rustic_shock_sensitive_plate", ()->new ContactMatBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
        SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_SHOCK_SENSITIVE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(0,0,0, 16,1,16),
      Auxiliaries.getPixeledAABB(0,0,0, 16,0.5,16),
      new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.5f),
      new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.0f)
    ));

    // Rustic trap door switch (shock vibration sensitive)
    Registries.addBlock("rustic_shock_sensitive_trapdoor", ()->new TrapdoorSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_SHOCK_SENSITIVE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(0,15.6,0, 16,16,16),
      Auxiliaries.getPixeledAABB(0, 2.0,0, 16,16, 0.1),
      new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.5f),
      new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.0f)
    ));

    // Rustic trap door switch (high sensitive shock vibration sensitive)
    Registries.addBlock("rustic_high_sensitive_trapdoor", ()->new TrapdoorSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_SHOCK_SENSITIVE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_HIGH_SENSITIVE|
        SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(0,15.6,0, 16,16,16),
      Auxiliaries.getPixeledAABB(0, 2.0,0, 16,16, 0.1),
      new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.5f),
      new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.0f)
      //Auxiliaries.RsMaterials.MATERIAL_TRAPDOORSWITCH
    ));

    // Rustic trap door switch (item trap door)
    Registries.addBlock("rustic_fallthrough_detector", ()->new TrapdoorSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
        SwitchBlock.SWITCH_CONFIG_INVERTABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(0,12.6,0, 16,13,16),
      Auxiliaries.getPixeledAABB(0,12.6,0, 16,13,16),
      new ModResources.BlockSoundEvent(SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.5f),
      null
    ));

    Registries.addBlock("rustic_semaphore", ()->new IndicatorBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
      detail.indicator_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(3,4,0, 13,11,1),
      null,
      null
    ));


    // -----------------------------------------------------------------------------------------------------------------
    // -- Glass
    // -----------------------------------------------------------------------------------------------------------------

    // Thin star shaped glass switch
    Registries.addBlock("glass_rotary_switch", ()->new BistableSwitchBlock(
      SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|
        SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_faint_light_block_properties(),
      Auxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.5), null
    ));

    // Bistable glass touch switch
    Registries.addBlock("glass_touch_switch", ()->new BistableSwitchBlock(
      SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|
        SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_faint_light_block_properties(),
      Auxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.5), null
    ));

    // Thin star shaped glass button
    Registries.addBlock("glass_button", ()->new PulseSwitchBlock(
      SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|
        SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
        SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|
        SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_faint_light_block_properties(),
      Auxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.5), null
    ));

    // Thin small star shaped glass button
    Registries.addBlock("glass_small_button", ()->new PulseSwitchBlock(
      SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|
        SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_faint_light_block_properties(),
      Auxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.5), null
    ));

    // Glass touch button
    Registries.addBlock("glass_touch_button", ()->new PulseSwitchBlock(
      SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|
        SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_faint_light_block_properties(),
      Auxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.5), null
    ));

    // Glass door plate
    Registries.addBlock("glass_door_contact_mat", ()->new ContactMatBlock(
      SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|
        SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(0,0,0, 16,0.25,16), null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.0f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.0f, 1.2f)
    ));

    // Glass plate
    Registries.addBlock("glass_contact_mat", ()->new ContactMatBlock(
      SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|
        SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(0,0,0, 16,0.25,16), null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.0f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.0f, 1.2f)
    ));

    // Glass Day time switch
    Registries.addBlock("glass_day_timer", ()->new DayTimerSwitchBlock(
      SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|
        SwitchBlock.SWITCH_CONFIG_TIMER_DAYTIME|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.1), null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.0f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.0f, 1.2f)
    ));

    // Glass interval signal timer
    Registries.addBlock("glass_interval_timer", ()->new IntervalTimerSwitchBlock(
      SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|
        SwitchBlock.SWITCH_CONFIG_TIMER_INTERVAL|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.1), null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.0f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.0f, 1.2f)
    ));

    // Glass infrared motion sensor
    Registries.addBlock("glass_entity_detector", ()->new EntityDetectorSwitchBlock(
      SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|SwitchBlock.SWITCH_CONFIG_SENSOR_VOLUME|
        SwitchBlock.SWITCH_CONFIG_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.1), null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.0f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.0f, 1.2f)
    ));

    // Glass laser motion sensor
    Registries.addBlock("glass_linear_entity_detector", ()->new EntityDetectorSwitchBlock(
      SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_SENSOR_LINEAR|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.1), null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.0f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.0f, 1.2f)
    ));

    Registries.addBlock("glass_vertical_bar_gauge", ()->new GaugeBlock(
      RsBlock.RSBLOCK_CONFIG_CUTOUT,
      detail.gauge_glass_block_properties(),
      Auxiliaries.getPixeledAABB(7,3.7,0, 10,12,0.4)
    ));

    // -----------------------------------------------------------------------------------------------------------------
    // -- Old Fancy
    // -----------------------------------------------------------------------------------------------------------------

    // Old fancy gold decorated lever
    Registries.addBlock("oldfancy_bistableswitch1", ()->new BistableSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(6,6.5,0, 10.3,13.5,4.5),
      Auxiliaries.getPixeledAABB(6,3.5,0, 10.3,10.0,4.5)
    ));

    // Old fancy angular lever
    Registries.addBlock("oldfancy_bistableswitch2", ()->new BistableSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(2.5,6.0,0, 9.7,10,4.5),
      Auxiliaries.getPixeledAABB(4.5,3.5,0, 9.2,10,4.5)
    ));

    // Old fancy (golden decorated) button
    Registries.addBlock("oldfancy_button", ()->new PulseSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(6,6,0,10,10,1.5), null
    ));

    // Old fancy (golden decorated) chain pulse switch
    Registries.addBlock("oldfancy_spring_reset_chain", ()->new PulseSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(6.5,4.8,0,9.5,13,4),
      Auxiliaries.getPixeledAABB(6.5,3.8,0,9.5,12,4)
    ));

    // Old fancy (golden decorated) tiny button
    Registries.addBlock("oldfancy_small_button", ()->new PulseSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(7,7,0,9,9,1.5), null
    ));

    // -----------------------------------------------------------------------------------------------------------------
    // -- Other
    // -----------------------------------------------------------------------------------------------------------------

    // Yellow power plant
    Registries.addBlock("yellow_power_plant", ()->new PowerPlantBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
        SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(5,0,5, 11,9,11), null,
      new ModResources.BlockSoundEvent(SoundEvents.GRASS_BREAK, 0.09f, 3.6f),
      new ModResources.BlockSoundEvent(SoundEvents.GRASS_BREAK, 0.04f, 3.0f)
      // Auxiliaries.RsMaterials.MATERIAL_PLANT
    ));

    // Red power plant
    Registries.addBlock("red_power_plant", ()->new PowerPlantBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
        SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(5,0,5, 11,9,11), null,
      new ModResources.BlockSoundEvent(SoundEvents.GRASS_BREAK, 0.09f, 3.6f),
      new ModResources.BlockSoundEvent(SoundEvents.GRASS_BREAK, 0.04f, 3.0f)
      // Auxiliaries.RsMaterials.MATERIAL_PLANT
    ));

    // Light flip switch
    Registries.addBlock("light_switch", ()->new BistableSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(7,6,0,9,10,1.5), null
    ));

    // Arrow target
    Registries.addBlock("arrow_target", ()->new PulseSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
        SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(5,5,0,11,11,1), null
    ));

    // Valve Wheel
    Registries.addBlock("valve_wheel_switch", ()->new BistableSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(4,4,0,12,12,3.5), null
    ));

    // Elevator button
    Registries.addBlock("elevator_button", ()->new ElevatorSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
        SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
        SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|
        SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_faint_light_block_properties(),
      Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1), null
    ));

    // Door sensor
    Registries.addBlock("door_sensor_switch", ()->new DoorSensorSwitchBlock(
      SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
        SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_LATERAL_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
        SwitchBlock.SWITCH_CONFIG_INVERTABLE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      detail.switch_metallic_block_properties(),
      Auxiliaries.getPixeledAABB(5,0,0, 11,1, 1.5),
      null,
      new ModResources.BlockSoundEvent(SoundEvents.LEVER_CLICK, 0.05f, 2.5f),
      null
    ));

    // -----------------------------------------------------------------------------------------------------------------
    // -- Sensitive glass
    // -----------------------------------------------------------------------------------------------------------------

    Registries.addBlock("sensitive_glass_block", ()->new SensitiveGlassBlock(
      detail.light_emitting_sensitive_glass_block_properties()
    ));

    Registries.addBlock("stained_sensitiveglass", ()->new SensitiveGlassBlock(
      detail.colored_sensitive_glass_block_properties()
    ));

    // -----------------------------------------------------------------------------------------------------------------
    // -- Related block entities
    // -----------------------------------------------------------------------------------------------------------------

    //public static final BlockEntityType<AbstractGaugeBlock.GaugeTileEntity> TET_GAUGE = ModRegistry.register("te_gauge", AbstractGaugeBlock.GaugeTileEntity::new, AbstractGaugeBlock.class);
    Registries.addBlockEntityType("te_gauge", AbstractGaugeBlock.GaugeTileEntity::new, AbstractGaugeBlock.class);

    //public static final BlockEntityType<SwitchBlock.SwitchTileEntity> TET_SWITCH = ModRegistry.register("te_switch", SwitchBlock.SwitchTileEntity::new, SwitchBlock.class);
    Registries.addBlockEntityType("te_switch", SwitchBlock.SwitchTileEntity::new, SwitchBlock.class);

    //public static final BlockEntityType<ContactSwitchBlock.ContactSwitchTileEntity> TET_CONTACT_SWITCH = ModRegistry.register("te_contact_switch", ContactSwitchBlock.ContactSwitchTileEntity::new, ContactSwitchBlock.class);
    Registries.addBlockEntityType("te_contact_switch", ContactSwitchBlock.ContactSwitchTileEntity::new, ContactSwitchBlock.class);

    //public static final BlockEntityType<EntityDetectorSwitchBlock.DetectorSwitchTileEntity> TET_DETECTOR_SWITCH = ModRegistry.register("te_detector_switch", EntityDetectorSwitchBlock.DetectorSwitchTileEntity::new, EntityDetectorSwitchBlock.class);
    Registries.addBlockEntityType("te_detector_switch", EntityDetectorSwitchBlock.DetectorSwitchTileEntity::new, EntityDetectorSwitchBlock.class);

    //public static final BlockEntityType<EnvironmentalSensorSwitchBlock.EnvironmentalSensorSwitchTileEntity> TET_ENVSENSOR_SWITCH = ModRegistry.register("te_envsensor_switch", EnvironmentalSensorSwitchBlock.EnvironmentalSensorSwitchTileEntity::new, EnvironmentalSensorSwitchBlock.class);
    Registries.addBlockEntityType("te_envsensor_switch", EnvironmentalSensorSwitchBlock.EnvironmentalSensorSwitchTileEntity::new, EnvironmentalSensorSwitchBlock.class);

    //public static final BlockEntityType<DayTimerSwitchBlock.DayTimerSwitchTileEntity> TET_DAYTIMER_SWITCH = ModRegistry.register("te_daytimer_switch", DayTimerSwitchBlock.DayTimerSwitchTileEntity::new, DayTimerSwitchBlock.class);
    Registries.addBlockEntityType("te_daytimer_switch", DayTimerSwitchBlock.DayTimerSwitchTileEntity::new, DayTimerSwitchBlock.class);

    //public static final BlockEntityType<IntervalTimerSwitchBlock.IntervalTimerSwitchTileEntity> TET_TIMER_SWITCH = ModRegistry.register("te_intervaltimer_switch", IntervalTimerSwitchBlock.IntervalTimerSwitchTileEntity::new, IntervalTimerSwitchBlock.class);
    Registries.addBlockEntityType("te_intervaltimer_switch", IntervalTimerSwitchBlock.IntervalTimerSwitchTileEntity::new, IntervalTimerSwitchBlock.class);

    //public static final BlockEntityType<ComparatorSwitchBlock.ComparatorSwitchTileEntity> TET_COMPARATOR_SWITCH = ModRegistry.register("te_comparator_switch", ComparatorSwitchBlock.ComparatorSwitchTileEntity::new, ComparatorSwitchBlock.class);
    Registries.addBlockEntityType("te_comparator_switch", ComparatorSwitchBlock.ComparatorSwitchTileEntity::new, ComparatorSwitchBlock.class);

    //public static final BlockEntityType<ObserverSwitchBlock.ObserverSwitchTileEntity> TET_OBSERVER_SWITCH = ModRegistry.register("te_observer_switch", ObserverSwitchBlock.ObserverSwitchTileEntity::new, ObserverSwitchBlock.class);
    Registries.addBlockEntityType("te_observer_switch", ObserverSwitchBlock.ObserverSwitchTileEntity::new, ObserverSwitchBlock.class);

    //public static final BlockEntityType<DoorSensorSwitchBlock.DoorSensorSwitchTileEntity> TET_DOORSENSOR_SWITCH = ModRegistry.register("te_doorsensor_switch", DoorSensorSwitchBlock.DoorSensorSwitchTileEntity::new, DoorSensorSwitchBlock.class);
    Registries.addBlockEntityType("te_doorsensor_switch", DoorSensorSwitchBlock.DoorSensorSwitchTileEntity::new, DoorSensorSwitchBlock.class);
  }

  public static void initItems()
  {
    Registries.addItem("switchlink_pearl", ()->new SwitchLinkPearlItem(detail.default_item_properties()));
  }

  public static Block getBlock(String name)
  { return Registries.getBlock(name); }

  public static Item getItem(String name)
  { return Registries.getItem(name); }

  public static TagKey<Block> getBlockTagKey(String name)
  { return Registries.getBlockTagKey(name); }

  public static TagKey<Item> getItemTagKey(String name)
  { return Registries.getItemTagKey(name); }

  public static BlockEntityType<?> getBlockEntityTypeOfBlock(String block_name)
  { return Registries.getBlockEntityTypeOfBlock(block_name); }


  //--------------------------------------------------------------------------------------------------------------------
  // Tile entities bound exclusively to the blocks above
  //--------------------------------------------------------------------------------------------------------------------

  @ObjectHolder("rsgauges:te_gauge")
  public static final BlockEntityType<AbstractGaugeBlock.GaugeTileEntity> TET_GAUGE = null;
  @ObjectHolder("rsgauges:te_switch")
  public static final BlockEntityType<SwitchBlock.SwitchTileEntity> TET_SWITCH = null;
  @ObjectHolder("rsgauges:te_contact_switch")
  public static final BlockEntityType<ContactSwitchBlock.ContactSwitchTileEntity> TET_CONTACT_SWITCH = null;
  @ObjectHolder("rsgauges:te_detector_switch")
  public static final BlockEntityType<EntityDetectorSwitchBlock.DetectorSwitchTileEntity> TET_DETECTOR_SWITCH = null;
  @ObjectHolder("rsgauges:te_envsensor_switch")
  public static final BlockEntityType<EnvironmentalSensorSwitchBlock.EnvironmentalSensorSwitchTileEntity> TET_ENVSENSOR_SWITCH = null;
  @ObjectHolder("rsgauges:te_daytimer_switch")
  public static final BlockEntityType<DayTimerSwitchBlock.DayTimerSwitchTileEntity> TET_DAYTIMER_SWITCH = null;
  @ObjectHolder("rsgauges:te_intervaltimer_switch")
  public static final BlockEntityType<IntervalTimerSwitchBlock.IntervalTimerSwitchTileEntity> TET_TIMER_SWITCH = null;
  @ObjectHolder("rsgauges:te_comparator_switch")
  public static final BlockEntityType<ComparatorSwitchBlock.ComparatorSwitchTileEntity> TET_COMPARATOR_SWITCH = null;
  @ObjectHolder("rsgauges:te_observer_switch")
  public static final BlockEntityType<ObserverSwitchBlock.ObserverSwitchTileEntity> TET_OBSERVER_SWITCH = null;
  @ObjectHolder("rsgauges:te_doorsensor_switch")
  public static final BlockEntityType<DoorSensorSwitchBlock.DoorSensorSwitchTileEntity> TET_DOORSENSOR_SWITCH = null;
  @ObjectHolder("rsgauges:switchlink_pearl")
  public static final SwitchLinkPearlItem SWITCH_LINK_PEARL = null;

  //--------------------------------------------------------------------------------------------------------------------
  // Initialisation events
  //--------------------------------------------------------------------------------------------------------------------

  @OnlyIn(Dist.CLIENT)
  public static void processContentClientSide(final FMLClientSetupEvent event)
  {
    // Block renderer selection
    for(Block block: Registries.getRegisteredBlocks()) {
      if(block instanceof RsBlock) {
        switch(((RsBlock)block).getRenderTypeHint()) {
          case CUTOUT: ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout()); break;
          case CUTOUT_MIPPED: ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutoutMipped()); break;
          case TRANSLUCENT: ItemBlockRenderTypes.setRenderLayer(block, RenderType.translucent()); break;
          case SOLID: break;
        }
      }
    }
  }

}
