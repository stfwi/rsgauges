/**
 * @file CommonProxy.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Module initialisation event handling, client and
 * server.
**/
package wile.rsgauges.proxy;

import wile.rsgauges.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CommonProxy
{
  public void preInit(FMLPreInitializationEvent e)
  {}

  public void init(FMLInitializationEvent e)
  {}

  public void postInit(FMLPostInitializationEvent e)
  {}

  @SubscribeEvent
  public static void registerBlocks(RegistryEvent.Register<Block> event)
  { ModBlocks.registerBlocks(event); }

  @SubscribeEvent
  public static void registerItems(RegistryEvent.Register<Item> event)
  {
    ModBlocks.registerItemBlocks(event);
    ModItems.registerItems(event);
  }

}
