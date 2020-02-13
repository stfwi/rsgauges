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

import wile.rsgauges.blocks.*;
import wile.rsgauges.items.*;
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
public class ModContent
{
  //--------------------------------------------------------------------------------------------------------------------
  //-- Blocks
  //--------------------------------------------------------------------------------------------------------------------

  @GameRegistry.ObjectHolder("rsgauges:bistableswitch2") public static final BlockSwitch ITEMGROUP_BLOCK = null;

  private static final Block modBlocks[] = {

    // -----------------------------------------------------------------------------------------------------------------
    // -- industrual
    // -----------------------------------------------------------------------------------------------------------------

    // Contact lever switch
    new BlockSwitch("bistableswitch2",
      ModAuxiliaries.getPixeledAABB(4,4,0,12,15,4),
      ModAuxiliaries.getPixeledAABB(4,1,0,12,12,4),
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // mechanical lever
    new BlockSwitch("bistableswitch8",
      ModAuxiliaries.getPixeledAABB(5,4,0,11,15,5),
      ModAuxiliaries.getPixeledAABB(5,1,0,11,12,5),
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // mechanical rotary lever
    new BlockSwitch("bistableswitch7",
      ModAuxiliaries.getPixeledAABB(1,4,0,12,12,6),
      ModAuxiliaries.getPixeledAABB(1,1,0,12,12,6),
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
      BlockSwitch.SWITCH_CONFIG_HOPPER_MOUNTBALE,
      null, null
    ),
    // Rotary machine switch
    new BlockSwitch("bistableswitch1",
      ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // two-button machine switch
    new BlockSwitch("bistableswitch3",
      ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // ESTOP button
    new BlockSwitch("bistableswitch5",
      ModAuxiliaries.getPixeledAABB(5,5,0, 11, 11, 2.5),
      ModAuxiliaries.getPixeledAABB(5,5,0, 11, 11, 3.5),
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
      BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE_OFF,
      null, null
    ),
    // Hopper blocking switch
    new BlockSwitch("bistableswitch6",
      ModAuxiliaries.getPixeledAABB(3,10,0, 13, 12, 6.7),
      ModAuxiliaries.getPixeledAABB(3,10,0, 13, 12, 3.7),
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
      BlockSwitch.SWITCH_CONFIG_HOPPER_MOUNTBALE|BlockSwitch.SWITCH_DATA_WEAK,
      null, null
    ),

    // ------------------------------------------------------------------------------

    // Square machine pulse switch
    new BlockSwitch("pulseswitch1",
      ModAuxiliaries.getPixeledAABB(5,5,0, 11, 11, 2), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Fenced round machine pulse switch
    new BlockSwitch("pulseswitch2",
      ModAuxiliaries.getPixeledAABB(5,5,0, 11, 11, 2), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Retro double pole switch
    new BlockSwitch("pulseswitch6",
      ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 3),
      ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 2),
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Mechanical spring reset push button
    new BlockSwitch("pulseswitch5",
      ModAuxiliaries.getPixeledAABB(5,3,0, 11, 7, 4), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Mechanical spring reset pull handle
    new BlockSwitch("pulseswitch3",
      ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 2),
      ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 2),
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Industrial dimmer switch
    new BlockDimmerSwitch("dimmerswitch1",
      ModAuxiliaries.getPixeledAABB(4,1,0, 12, 15, 2),
      BlockSwitch.SWITCH_CONFIG_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.9f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.7f),
      ModAuxiliaries.RsMaterials.MATERIAL_METALLIC
    ),

    // ------------------------------------------------------------------------------

    // Door contact mat
    new BlockContactSwitch("contactmat1",
      ModAuxiliaries.getPixeledAABB(1,0,0, 15, 1, 13), null,
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_DATA_WEAK|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
    ),
    // Sensitive full size contact mat
    new BlockContactSwitch("contactmat2",
      ModAuxiliaries.getPixeledAABB(0,0,0, 16, 1, 16), null,
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
    ),
    // Industrial shock sensor contact mat
    new BlockContactSwitch("contactmat3",
      ModAuxiliaries.getPixeledAABB(0,0,0, 16, 1, 16), null,
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_SHOCK_SENSITIVE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
    ),

    // ------------------------------------------------------------------------------

    // Industrial trap door switch (shock vibration sensitive)
    new BlockTrapdoorSwitch("trapdoorswitch1",
      ModAuxiliaries.getPixeledAABB(0,15.6,0, 16, 16, 16),
      ModAuxiliaries.getPixeledAABB(0,2,0, 16, 1, 0.1),
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_NOT_PASSABLE|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_SHOCK_SENSITIVE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 3.0f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.5f)
    ),
    // Industrial trap door switch (high sensitive shock vibration sensitive)
    new BlockTrapdoorSwitch("trapdoorswitch2",
      ModAuxiliaries.getPixeledAABB(0,15.6,0, 16, 16, 16),
      ModAuxiliaries.getPixeledAABB(0,2,0, 16, 1, 0.1),
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
      ModAuxiliaries.getPixeledAABB(0,12.6,0, 16, 13, 16),
      ModAuxiliaries.getPixeledAABB(0,12.6,0, 16, 13, 16),
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
      BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.05f, 2.5f),
      null
    ),

    // ------------------------------------------------------------------------------

    // Day time switch
    new BlockAutoSwitch("automaticswitch4",
      ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5),
      BlockSwitch.SWITCH_CONFIG_TIMER_DAYTIME|
        BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
        BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
        BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
    ),
    // Interval signal timer
    new BlockAutoSwitch("automaticswitch7",
      ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5),
      BlockSwitch.SWITCH_CONFIG_TIMER_INTERVAL|
        BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
        BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
        BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
    ),
    // Infrared motion_sensor
    new BlockAutoSwitch("automaticswitch1",
      ModAuxiliaries.getPixeledAABB(6,6,0, 10, 10, 1),
      BlockSwitch.SWITCH_CONFIG_SENSOR_VOLUME|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
    ),
    // Linear laser motion sensor
    new BlockAutoSwitch("automaticswitch2",
      ModAuxiliaries.getPixeledAABB(6,6,0, 10, 10, 1),
      BlockSwitch.SWITCH_CONFIG_SENSOR_LINEAR|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
    ),
    // Local light sensor
    new BlockAutoSwitch("automaticswitch3",
      ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5),
      BlockSwitch.SWITCH_CONFIG_SENSOR_LIGHT|
      BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
    ),
    // Rain sensor switch
    new BlockAutoSwitch("automaticswitch5",
      ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5),
      BlockSwitch.SWITCH_CONFIG_SENSOR_RAIN|
      BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
    ),
    // Lightning sensor switch
    new BlockAutoSwitch("automaticswitch6",
      ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5),
      BlockSwitch.SWITCH_CONFIG_SENSOR_LIGHTNING|
      BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
    ),

    // ------------------------------------------------------------------------------

    // Industrial uni-directional block detector switch
    new BlockObserverSwitch("observerswitch1",
      ModAuxiliaries.getPixeledAABB(0.5,0.5,0.5, 15.5, 15.5, 15.5), null,
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

    // ------------------------------------------------------------------------------

    // Industrial pulse link relay receiver switch
    new BlockSwitch("relay_pulseswitchrx1",
      ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
      BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.9f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.7f)
    ),
    // Industrial full block pulse link relay receiver switch
    new BlockSwitch("relay_pulseswitchrx2",
      ModAuxiliaries.getPixeledAABB(0,0,0, 16, 16, 16), null,
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
      ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
      BlockSwitch.SWITCH_CONFIG_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_BISTABLE|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_DATA_WEAK|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.9f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.7f)
    ),
    // Industrial full block bistable link relay receiver switch
    new BlockSwitch("relay_bistableswitchrx2",
      ModAuxiliaries.getPixeledAABB(0,0,0, 16, 16, 16), null,
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
      ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
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
      ModAuxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
      BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_LINK_RELAY|
      BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_DATA_WEAK|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.9f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.01f, 1.7f)
    ),

    // ------------------------------------------------------------------------------

    // Industrial bistable industrial knock surge detctor
    new BlockKnockBistableSwitch("industrial_knock_switch",
      ModAuxiliaries.getPixeledAABB(0.5,0.5,0.5, 15.5, 15.5, 15.5), null,
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
      ModAuxiliaries.getPixeledAABB(0.5,0.5,0.5, 15.5, 15.5, 15.5), null,
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

    // ------------------------------------------------------------------------------

    new BlockGauge("flatgauge1",
      ModAuxiliaries.getPixeledAABB(2,2,0, 14, 14, 1),
      BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT
    ),
    new BlockGauge("flatgauge6",
      ModAuxiliaries.getPixeledAABB(2,4,0, 14, 12, 1),
      BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT
    ),
    new BlockGauge("flatgauge2",
      ModAuxiliaries.getPixeledAABB(4,2,0, 12, 14, 1),
      BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT
    ),
    new BlockGauge("flatgauge3",
      ModAuxiliaries.getPixeledAABB(4,5,0, 12, 11, 1),
      BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT
    ),
    new BlockGauge("flatgauge5",
      ModAuxiliaries.getPixeledAABB(7,4,0, 9, 12, 3),
      BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT
    ),

    // ------------------------------------------------------------------------------

    // alarm lamp
    new BlockIndicator("indicator4",
      ModAuxiliaries.getPixeledAABB(6,6,0, 10, 10, 4),
      (8<<BlockGauge.GAUGE_DATA_LIGHT_SHIFT)
    ),
    // Alarm siren
    new BlockIndicator("soundindicator1",
      ModAuxiliaries.getPixeledAABB(4,6.5,0, 11.5, 9.5, 4),
      (1<<BlockGauge.GAUGE_DATA_LIGHT_SHIFT) | (8<<BlockGauge.GAUGE_DATA_BLINK_SHIFT),
      new ModResources.BlockSoundEvent(ModResources.ALARM_SIREN_SOUND),
      null
    ),
    new BlockIndicator("indicator1",
      ModAuxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5),
      (2<<BlockGauge.GAUGE_DATA_LIGHT_SHIFT)|(13<<BlockGauge.GAUGE_DATA_COLOR_SHIFT) // green
    ),
    new BlockIndicator("indicator1blink1",
      ModAuxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5),
      (2<<BlockGauge.GAUGE_DATA_LIGHT_SHIFT)
    ),
    new BlockIndicator("indicator2",
      ModAuxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5),
      (2<<BlockGauge.GAUGE_DATA_LIGHT_SHIFT)|(5<<BlockGauge.GAUGE_DATA_COLOR_SHIFT) // yellow
    ),
    new BlockIndicator("indicator2blink1",
      ModAuxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5),
      (2<<BlockGauge.GAUGE_DATA_LIGHT_SHIFT)
    ),
    new BlockIndicator("indicator3",
      ModAuxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5),
      (2<<BlockGauge.GAUGE_DATA_LIGHT_SHIFT) // red
    ),
    new BlockIndicator("indicator3blink1",
      ModAuxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5),
      (2<<BlockGauge.GAUGE_DATA_LIGHT_SHIFT)
    ),
    new BlockIndicator("indicator_led_white",
      ModAuxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5),
      (2<<BlockGauge.GAUGE_DATA_LIGHT_SHIFT)|BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT // default white
    ),
    new BlockIndicator("indicator_led_white_blink",
      ModAuxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5),
      (2<<BlockGauge.GAUGE_DATA_LIGHT_SHIFT)|BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT // default white
    ),

    // -----------------------------------------------------------------------------------------------------------------
    // -- Rustic
    // -----------------------------------------------------------------------------------------------------------------

    // Rustic lever 1
    new BlockSwitch("bistableswitch_rustic1",
      ModAuxiliaries.getPixeledAABB(6,5,0, 10.3, 15, 4.5),
      ModAuxiliaries.getPixeledAABB(6,2,0, 10.3, 11, 4.5),
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Rustic lever 2 (bolted)
    new BlockSwitch("bistableswitch_rustic2",
      ModAuxiliaries.getPixeledAABB(2,6,0, 14,13,4.5),
      ModAuxiliaries.getPixeledAABB(2,4,0, 14,10,4.5),
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Rustic lever 3 (big angular)
    new BlockSwitch("bistableswitch_rustic3",
      ModAuxiliaries.getPixeledAABB(0,10,0, 14,15,4.5),
      ModAuxiliaries.getPixeledAABB(6, 2,0, 14,15,4.5),
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
      BlockSwitch.SWITCH_CONFIG_HOPPER_MOUNTBALE,
      null, null
    ),
    // Rustic lever 7 (The Nail)
    new BlockSwitch("bistableswitch_rustic7",
      ModAuxiliaries.getPixeledAABB(6,7,0, 9,10,3), null,
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
      BlockSwitch.SWITCH_CONFIG_HOPPER_MOUNTBALE,
      null, null
    ),

    // ------------------------------------------------------------------------------

    // Rustic button 1
    new BlockSwitch("pulseswitch_rustic1",
      ModAuxiliaries.getPixeledAABB(5,5,0,11,11,2.5), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
        BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
        BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE|
        BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Rustic button 2 (bolted)
    new BlockSwitch("pulseswitch_rustic2",
      ModAuxiliaries.getPixeledAABB(6,6,0,10,10,2.5), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
        BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
        BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE|
        BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Rustic button 3 (pull chain)
    new BlockSwitch("pulseswitch_rustic3",
      ModAuxiliaries.getPixeledAABB(5,3.5,0,11,15,4), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
        BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
        BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
        BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
        BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Rustic button 7 (pull nail)
    new BlockSwitch("pulseswitch_rustic7",
      ModAuxiliaries.getPixeledAABB(6,7,0, 9,10,3), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),

    // ------------------------------------------------------------------------------

    // Rustic door contact mat
    new BlockContactSwitch("contactmat_rustic1",
      ModAuxiliaries.getPixeledAABB(1,0,0, 15,1,12),
      ModAuxiliaries.getPixeledAABB(1,0,0, 15,0.5,12),
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_DATA_WEAK|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.5f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.0f)
    ),
    // Rustic full-size contact plate
    new BlockContactSwitch("contactmat_rustic2",
      ModAuxiliaries.getPixeledAABB(0,0,0, 16,1,16),
      ModAuxiliaries.getPixeledAABB(0,0,0, 16,0.5,16),
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.5f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.0f)
    ),
    // Rustic shock sensor plate
    new BlockContactSwitch("contactmat_rustic3",
      ModAuxiliaries.getPixeledAABB(0,0,0, 16,1,16),
      ModAuxiliaries.getPixeledAABB(0,0,0, 16,0.5,16),
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_SHOCK_SENSITIVE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.5f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.0f)
    ),

    // ------------------------------------------------------------------------------

    // Rustic trap door switch (shock vibration sensitive)
    new BlockTrapdoorSwitch("trapdoorswitch_rustic1",
      ModAuxiliaries.getPixeledAABB(0,15.6,0, 16,16,16),
      ModAuxiliaries.getPixeledAABB(0, 2.0,0, 16,16, 0.1),
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_NOT_PASSABLE|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_SHOCK_SENSITIVE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.5f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.0f)
    ),
    // Rustic trap door switch (high sensitive shock vibration sensitive)
    new BlockTrapdoorSwitch("trapdoorswitch_rustic2",
      ModAuxiliaries.getPixeledAABB(0,15.6,0, 16,16,16),
      ModAuxiliaries.getPixeledAABB(0, 2.0,0, 16,16, 0.1),
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
      ModAuxiliaries.getPixeledAABB(0,12.6,0, 16,13,16),
      ModAuxiliaries.getPixeledAABB(0,12.6,0, 16,13,16),
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
      BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.5f),
      null // new ModResources.BlockSoundEvent(SoundEvents.BLOCK_IRON_DOOR_CLOSE, 0.05f, 2.0f)
    ),

    // ------------------------------------------------------------------------------

    new BlockGauge("gauge_rustic2",
      ModAuxiliaries.getPixeledAABB(2,2,0, 14,14,1),
      0 // no color tint.
    ),
    new BlockIndicator("indicator_rustic_flag",
      ModAuxiliaries.getPixeledAABB(3,4,0, 13,11,1),
      BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT // default white
    ),

    // -----------------------------------------------------------------------------------------------------------------
    // -- Glass
    // -----------------------------------------------------------------------------------------------------------------

    // Thin star shaped glass switch
    new BlockSwitch("bistableswitch_glass1",
      ModAuxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.5), null,
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
      BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|BlockSwitch.SWITCH_CONFIG_FAINT_LIGHTSOURCE|
      BlockSwitch.SWITCH_CONFIG_COLOR_TINT_SUPPORT,
      null, null
    ),
    // Bistable glass touch switch
    new BlockSwitch("bistableswitch_glass2",
      ModAuxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.5), null,
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
      BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|BlockSwitch.SWITCH_CONFIG_FAINT_LIGHTSOURCE|
      BlockSwitch.SWITCH_CONFIG_COLOR_TINT_SUPPORT,
      null, null
    ),

    // ------------------------------------------------------------------------------

    // Thin star shaped glass button
    new BlockSwitch("pulseswitch_glass1",
      ModAuxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.5), null,
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
      ModAuxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.5), null,
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
      ModAuxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.5), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
      BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|BlockSwitch.SWITCH_CONFIG_FAINT_LIGHTSOURCE|
      BlockSwitch.SWITCH_CONFIG_COLOR_TINT_SUPPORT|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),

    // ------------------------------------------------------------------------------

    // Glass plate
    new BlockContactSwitch("contactmat_glass1",
      ModAuxiliaries.getPixeledAABB(0,0,0, 16,0.25,16), null,
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|
      BlockSwitch.SWITCH_CONFIG_COLOR_TINT_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.2f)
    ),
    // Glass plate
    new BlockContactSwitch("contactmat_glass2",
      ModAuxiliaries.getPixeledAABB(0,0,0, 16,0.25,16), null,
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|
      BlockSwitch.SWITCH_CONFIG_COLOR_TINT_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.2f)
    ),

    // ------------------------------------------------------------------------------

    // Glass Day time switch
    new BlockAutoSwitch("daytimeswitch_glass1",
      ModAuxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.1),
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
      ModAuxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.1),
      BlockSwitch.SWITCH_CONFIG_TIMER_INTERVAL|
      BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.2f)
    ),
    // Glass infrared motion sensor
    new BlockAutoSwitch("detectorswitch_glass1",
      ModAuxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.1),
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
      ModAuxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.1),
      BlockSwitch.SWITCH_CONFIG_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_SENSOR_LINEAR|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_TRANSLUCENT|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.0f, 1.2f)
    ),
    // Comparator output level observing switch
    new BlockComparatorSwitch(
      "industrial_comparator_switch",
      ModAuxiliaries.getPixeledAABB(4,10,0, 12, 15, 1.5),
      BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_TOUCH_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
    ),

    // ------------------------------------------------------------------------------

    new BlockGauge("flatgauge4",
      ModAuxiliaries.getPixeledAABB(7,3.7,0, 10,12,0.4),
      BlockGauge.GAUGE_CONFIG_COLOR_TINT_SUPPORT
    ),

    // -----------------------------------------------------------------------------------------------------------------
    // -- Old fancy
    // -----------------------------------------------------------------------------------------------------------------

    // Old fancy gold decorated lever
    new BlockSwitch("bistableswitch_oldfancy1",
      ModAuxiliaries.getPixeledAABB(6,6.5,0, 10.3,13.5,4.5),
      ModAuxiliaries.getPixeledAABB(6,3.5,0, 10.3,10.0,4.5),
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Old fancy angular lever
    new BlockSwitch("bistableswitch_oldfancy2",
      ModAuxiliaries.getPixeledAABB(2.5,6.0,0, 9.7,10,4.5),
      ModAuxiliaries.getPixeledAABB(4.5,3.5,0, 9.2,10,4.5),
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
      BlockSwitch.SWITCH_CONFIG_HOPPER_MOUNTBALE,
      null, null
    ),
    // Old fancy (golden decorated) button
    new BlockSwitch("pulseswitch_oldfancy1",
      ModAuxiliaries.getPixeledAABB(6,6,0,10,10,1.5), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Old fancy (golden decorated) chain pulse switch
    new BlockSwitch("pulseswitch_oldfancy2",
      ModAuxiliaries.getPixeledAABB(6.5,4.8,0,9.5,13,4),
      ModAuxiliaries.getPixeledAABB(6.5,3.8,0,9.5,12,4),
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),

    // Old fancy (golden decorated) tiny button
    new BlockSwitch("pulseswitch_oldfancy4",
      ModAuxiliaries.getPixeledAABB(7,7,0,9,9,1.5), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),

    // -----------------------------------------------------------------------------------------------------------------
    // -- Other
    // -----------------------------------------------------------------------------------------------------------------

    // Yellow power plant
    new BlockContactSwitch("powerplant_yellow",
      ModAuxiliaries.getPixeledAABB(5,0,5, 11,9,11), null,
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_GRASS_BREAK, 0.09f, 3.6f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_GRASS_BREAK, 0.04f, 3.0f),
      ModAuxiliaries.RsMaterials.MATERIAL_PLANT
    ),
    // Red power plant
    new BlockContactSwitch("powerplant_red",
      ModAuxiliaries.getPixeledAABB(5,0,5, 11,9,11), null,
      BlockSwitch.SWITCH_CONFIG_CONTACT|BlockSwitch.SWITCH_CONFIG_LATERAL|BlockSwitch.SWITCH_CONFIG_WEAKABLE|
      BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_GRASS_BREAK, 0.09f, 3.6f),
      new ModResources.BlockSoundEvent(SoundEvents.BLOCK_GRASS_BREAK, 0.04f, 3.0f),
      ModAuxiliaries.RsMaterials.MATERIAL_PLANT
    ),

    // -----------------------------------------------------------------------------------------------------------------

    // Light flip switch
    new BlockSwitch("bistableswitch4",
      ModAuxiliaries.getPixeledAABB(7,6,0,9,10,1.5), null,
      BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
    ),
    // Arrow target
    new BlockSwitch("arrowtarget",
      ModAuxiliaries.getPixeledAABB(5,5,0,11,11,1), null,
      BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|
      BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|
      BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|
      BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
      BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
      null, null
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
    new BlockSensitiveGlass("sensitiveglass_inverted" , 0x0002|0x00f0, 0xffffff),

    //--------------------------------------------------------------------------------------------------------------------
    //-- OBSOLETE
    //--------------------------------------------------------------------------------------------------------------------

    // Rustic button 4 (weight reset pull chain)
    new BlockSwitch("pulseswitch_rustic4",ModAuxiliaries.getPixeledAABB(0,1,0, 16,15,2), null,RsBlock.RSBLOCK_CONFIG_OBSOLETE|BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,null, null),
    // Rustic button 5 (pull handle)
    new BlockSwitch("pulseswitch_rustic5",ModAuxiliaries.getPixeledAABB(0,1,0, 16,15,2), null,RsBlock.RSBLOCK_CONFIG_OBSOLETE|BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,null, null),
    // Rustic button 6 (handle)
    new BlockSwitch("pulseswitch_rustic6",ModAuxiliaries.getPixeledAABB(0,1,0, 16,15,2), null,RsBlock.RSBLOCK_CONFIG_OBSOLETE|BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,null, null),
    // Old fancy (golden decorated) crank
    new BlockSwitch("pulseswitch_oldfancy3",ModAuxiliaries.getPixeledAABB(0,1,0, 16,15,2), null,RsBlock.RSBLOCK_CONFIG_OBSOLETE|BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_CONFIG_PULSE_EXTENDABLE|BlockSwitch.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|BlockSwitch.SWITCH_CONFIG_LCLICK_RESETTABLE|BlockSwitch.SWITCH_CONFIG_PROJECTILE_SENSE|BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,null, null),
    // Old fancy bent lever
    new BlockSwitch("bistableswitch_oldfancy3",ModAuxiliaries.getPixeledAABB(0,1,0, 16,15,2), null,RsBlock.RSBLOCK_CONFIG_OBSOLETE|BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,null, null),
    // Old fancy gold decorated manual slide sphere connector
    new BlockSwitch("bistableswitch_oldfancy4",ModAuxiliaries.getPixeledAABB(0,1,0, 16,15,2), null,RsBlock.RSBLOCK_CONFIG_OBSOLETE|BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,null, null),
    // Old fancy gold decorated rotary sphere connector
    new BlockSwitch("bistableswitch_oldfancy5",ModAuxiliaries.getPixeledAABB(0,1,0, 16,15,2), null,RsBlock.RSBLOCK_CONFIG_OBSOLETE|BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,null, null),
    // Old fancy gold decorated rotary sphere connector 2
    new BlockSwitch("bistableswitch_oldfancy6",ModAuxiliaries.getPixeledAABB(0,1,0, 16,15,2), null,RsBlock.RSBLOCK_CONFIG_OBSOLETE|BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,null, null),
    // Old fancy gold decorated manual contactor
    new BlockSwitch("bistableswitch_oldfancy7",ModAuxiliaries.getPixeledAABB(0,1,0, 16,15,2), null,RsBlock.RSBLOCK_CONFIG_OBSOLETE|BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,null, null),
    // Rustic lever 4 (counter weighted rotary lever)
    new BlockSwitch("bistableswitch_rustic4",ModAuxiliaries.getPixeledAABB(0,1,0, 16,15,2), null,RsBlock.RSBLOCK_CONFIG_OBSOLETE|BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|BlockSwitch.SWITCH_CONFIG_HOPPER_MOUNTBALE,null, null),
    // Rustic lever 5
    new BlockSwitch("bistableswitch_rustic5",ModAuxiliaries.getPixeledAABB(0,1,0, 16,15,2), null,RsBlock.RSBLOCK_CONFIG_OBSOLETE|BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,null, null),
    // Rustic lever 6 (latch slide)
    new BlockSwitch("bistableswitch_rustic6",ModAuxiliaries.getPixeledAABB(0,1,0, 16,15,2), null,RsBlock.RSBLOCK_CONFIG_OBSOLETE|BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_WALLMOUNT|BlockSwitch.SWITCH_CONFIG_WEAKABLE|BlockSwitch.SWITCH_CONFIG_INVERTABLE|BlockSwitch.SWITCH_CONFIG_LINK_TARGET_SUPPORT|BlockSwitch.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,null, null),
  };

  //--------------------------------------------------------------------------------------------------------------------
  //-- Development blocks
  //--------------------------------------------------------------------------------------------------------------------

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

  //--------------------------------------------------------------------------------------------------------------------
  //-- Items
  //--------------------------------------------------------------------------------------------------------------------

  @GameRegistry.ObjectHolder("rsgauges:switchlink_pearl")       public static final ItemSwitchLinkPearl SWITCH_LINK_PEARL = null;

  private static final Item modItems[] = {
    new ItemSwitchLinkPearl("switchlink_pearl")
  };

  //--------------------------------------------------------------------------------------------------------------------
  //-- Init
  //--------------------------------------------------------------------------------------------------------------------

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
      if(ModConfig.zmisc.without_optout_registrations && (ModConfig.isOptedOut(e))) continue;
      if(e instanceof BlockGauge) {
        gauge_entity_needed = true;
      } else if(e instanceof BlockSwitch) {
        switch_config_bits_union_set |= ((BlockSwitch)e).config;
      }
      registeredBlocks.add(e);
    }

    // Tile entities
    int num_tile_entities = 0;
    if(gauge_entity_needed) {
      GameRegistry.registerTileEntity(BlockGauge.TileEntityGauge.class, new ResourceLocation(ModRsGauges.MODID, "gauge_entity"));
      ++num_tile_entities;
    }
    if((switch_config_bits_union_set & (BlockSwitch.SWITCH_CONFIG_BISTABLE|BlockSwitch.SWITCH_CONFIG_PULSE|BlockSwitch.SWITCH_CONFIG_LINK_RELAY)) != 0) {
      GameRegistry.registerTileEntity(BlockSwitch.TileEntitySwitch.class, new ResourceLocation(ModRsGauges.MODID, "switch_entity"));
      ++num_tile_entities;
    }
    if((switch_config_bits_union_set & (BlockSwitch.SWITCH_CONFIG_CONTACT)) != 0) {
      GameRegistry.registerTileEntity(BlockContactSwitch.TileEntityContactSwitch.class, new ResourceLocation(ModRsGauges.MODID, "contactswitch_entity"));
      ++num_tile_entities;
    }
    if((switch_config_bits_union_set & (BlockSwitch.SWITCH_CONFIG_AUTOMATIC)) != 0) {
      GameRegistry.registerTileEntity(BlockAutoSwitch.TileEntityAutoSwitch.class, new ResourceLocation(ModRsGauges.MODID, "autoswitch_entity"));
      ++num_tile_entities;
      if((switch_config_bits_union_set & (BlockSwitch.SWITCH_CONFIG_SENSOR_DETECTOR)) != 0) {
        GameRegistry.registerTileEntity(BlockAutoSwitch.TileEntityDetectorSwitch.class, new ResourceLocation(ModRsGauges.MODID, "detectorswitch_entity"));
        ++num_tile_entities;
      }
      if((switch_config_bits_union_set & (BlockSwitch.SWITCH_CONFIG_SENSOR_ENVIRONMENTAL)) != 0) {
        GameRegistry.registerTileEntity(BlockAutoSwitch.TileEntityEnvironmentalSensorSwitch.class, new ResourceLocation(ModRsGauges.MODID, "sensorswitch_entity"));
        ++num_tile_entities;
      }
      if((switch_config_bits_union_set & (BlockSwitch.SWITCH_CONFIG_SENSOR_TIME)) != 0) {
        GameRegistry.registerTileEntity(BlockAutoSwitch.TileEntityIntervalTimerSwitch.class, new ResourceLocation(ModRsGauges.MODID, "timerswitch_entity"));
        ++num_tile_entities;
      }
      if((switch_config_bits_union_set & (BlockSwitch.SWITCH_CONFIG_SENSOR_BLOCKDETECT)) != 0) {
        GameRegistry.registerTileEntity(BlockObserverSwitch.TileEntityObserverSwitch.class, new ResourceLocation(ModRsGauges.MODID, "observerswitch_entity"));
        ++num_tile_entities;
      }
      GameRegistry.registerTileEntity(BlockComparatorSwitch.TileEntityComparatorSwitch.class, new ResourceLocation(ModRsGauges.MODID, "comparatorswitch_entity"));
      ++num_tile_entities;
    }

    // Register blocks
    for(Block e:registeredBlocks) event.getRegistry().register(e);
    ModRsGauges.logger.info("Registered " + Integer.toString(registeredBlocks.size()) + " blocks.");
    ModRsGauges.logger.info("Registered " + num_tile_entities + " tile entities.");
  }

  public static ArrayList<Item> registeredItems = new ArrayList<>();

  // Invoked from CommonProxy.registerItems()
  public static final void registerItems(RegistryEvent.Register<Item> event)
  {
    for(Item e:modItems) {
      if(ModConfig.zmisc.without_optout_registrations && (ModConfig.isOptedOut(e))) continue;
      registeredItems.add(e);
    }
    if(ModConfig.zmisc.without_optout_registrations) ModRsGauges.logger.info("Registration opt-out configured, disabled item categories will not be registered at all.");
    for(Item e:registeredItems) event.getRegistry().register(e);
  }

  @SideOnly(Side.CLIENT)
  public static final void initModels()
  {
    for(Block e:registeredBlocks) {
      if(e instanceof RsBlock) {
        ((RsBlock)e).initModel();
      } else if(e instanceof BlockSensitiveGlass) {
        ((BlockSensitiveGlass)e).initModel();
      }
    }

    for(Item e:registeredItems) {
      if(e instanceof RsItem) ((RsItem)e).initModel();
    }
  }

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
