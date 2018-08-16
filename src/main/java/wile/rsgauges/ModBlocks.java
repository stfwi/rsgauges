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

  // Object holder injection fields for own blocks
  @GameRegistry.ObjectHolder("rsgauges:flatgauge1")           public static final GaugeBlock flatgauge1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:flatgauge2")           public static final GaugeBlock flatgauge2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:flatgauge3")           public static final GaugeBlock flatgauge3Block = null;
  @GameRegistry.ObjectHolder("rsgauges:flatgauge4")           public static final GaugeBlock flatgauge4Block = null;
  @GameRegistry.ObjectHolder("rsgauges:flatgauge5")           public static final GaugeBlock flatgauge5Block = null;
  @GameRegistry.ObjectHolder("rsgauges:flatgauge6")           public static final GaugeBlock flatgauge6Block = null;
  @GameRegistry.ObjectHolder("rsgauges:indicator1")           public static final GaugeBlock indicator1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:indicator2")           public static final GaugeBlock indicator2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:indicator3")           public static final GaugeBlock indicator3Block = null;
  @GameRegistry.ObjectHolder("rsgauges:indicator1blink1")     public static final GaugeBlock indicator1Blink1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:indicator2blink1")     public static final GaugeBlock indicator2Blink1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:indicator3blink1")     public static final GaugeBlock indicator3Blink1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:indicator4")           public static final GaugeBlock indicator4Block = null;
  @GameRegistry.ObjectHolder("rsgauges:soundindicator1")      public static final GaugeBlock soundIndicator1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch1")      public static final SwitchBlock bistableSwitch1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch2")      public static final SwitchBlock bistableSwitch2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch3")      public static final SwitchBlock bistableSwitch3Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch4")      public static final SwitchBlock bistableSwitch4Block = null;
  @GameRegistry.ObjectHolder("rsgauges:bistableswitch5")      public static final SwitchBlock bistableSwitch5Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch1")         public static final SwitchBlock pulseSwitch1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:pulseswitch2")         public static final SwitchBlock pulseSwitch2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:arrowtarget")          public static final SwitchBlock pulseSwitch4Block = null;
  @GameRegistry.ObjectHolder("rsgauges:contactmat1")          public static final ContactSwitchBlock contactSwitch1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:contactmat2")          public static final ContactSwitchBlock contactSwitch2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:automaticswitch1")     public static final AutoSwitchBlock automaticSwitch1Block = null;
  @GameRegistry.ObjectHolder("rsgauges:automaticswitch2")     public static final AutoSwitchBlock automaticSwitch2Block = null;
  @GameRegistry.ObjectHolder("rsgauges:sensitiveglass")       public static final SensitiveGlassBlock sensitiveGlassBlock = null;
  @GameRegistry.ObjectHolder("rsgauges:sensitiveglass_blue")  public static final SensitiveGlassBlock blueSensitiveGlassBlock = null;

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

  private static final GaugeBlock soundIndicators[] = {
      new GaugeBlock("soundindicator1",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16), (10d/16),(10d/16),(4d/16)),
          1, 1500, // light level scaling, blink frequency
          new ModResources.BlockSoundEvent(ModResources.alarm_siren_sound),
          null
      )
  };

  private static final SwitchBlock bistableSwitches[] = {
      new SwitchBlock("bistableswitch1",
          new AxisAlignedBB((4d/16),(4d/16),(0d/16),(12d/16),(12d/16),(1d/16)),
          SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      new SwitchBlock("bistableswitch2",
          new AxisAlignedBB((6d/16),(2d/16),(0d/16),(10d/16),(14d/16),(1.5d/16)),
          SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      new SwitchBlock("bistableswitch3",
          new AxisAlignedBB((4d/16),(4d/16),(0d/16),(12d/16),(12d/16),(1d/16)),
          SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      new SwitchBlock("bistableswitch4",
          new AxisAlignedBB((7d/16),(6d/16),(0d/16),(9d/16),(10d/16),(1.5d/16)),
          SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      new SwitchBlock("bistableswitch5",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(3d/16)),
          SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
          SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE_OFF
      )
  };

  private static final SwitchBlock pulseSwitches[] = {
      new SwitchBlock("pulseswitch1",
          new AxisAlignedBB((5d/16),(5d/16),(0d/16),(11d/16),(11d/16),(1d/16)),
          SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
          SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      new SwitchBlock("pulseswitch2",
          new AxisAlignedBB((5.5d/16),(5.5d/16),(0d/16),(10.5d/16),(10.5d/16),(0.5d/16)),
          SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
          SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      ),
      new SwitchBlock("arrowtarget",
          new AxisAlignedBB((5d/16),(5d/16),(0d/16),(11d/16),(11d/16),(1d/16)),
          SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
          SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE
      )
  };

  private static final ContactSwitchBlock contactSwitches[] = {
      new ContactSwitchBlock("contactmat1",
          new AxisAlignedBB((1d/16),(0.0d/16),(0d/16), (15d/16),(0.5d/16),(12d/16)),
          SwitchBlock.SWITCH_CONFIG_FLOOR_MOUNT|SwitchBlock.SWITCH_DATA_WEAK|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
          SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE,
          new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
          new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
      ),
      new ContactSwitchBlock("contactmat2",
          new AxisAlignedBB((0d/16),(0.0d/16),(0d/16), (16d/16),(0.5d/16),(16d/16)),
          SwitchBlock.SWITCH_CONFIG_FLOOR_MOUNT|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
          SwitchBlock.SWITCH_CONFIG_INVERTABLE,
          new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
          new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
      )
  };

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
      new AutoSwitchBlock("automaticswitch2",
          new AxisAlignedBB((6d/16),(6d/16),(0d/16),(10d/16),(10d/16),(1d/16)),
          SwitchBlock.SWITCH_CONFIG_AUTOMATIC|SwitchBlock.SWITCH_CONFIG_SENSOR_LINEAR|
          SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
          SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE,
          new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.3f),
          new ModResources.BlockSoundEvent(SoundEvents.BLOCK_LEVER_CLICK, 0.1f, 1.2f)
      )
  };

  private static final SensitiveGlassBlock sensitiveGlassBlocks[] = {
      new SensitiveGlassBlock("sensitiveglass", 0x000f|0x0010), // light value if on 0xf | off 0x1
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
