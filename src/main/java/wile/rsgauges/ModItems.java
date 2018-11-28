/*
 * @file ModItems.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Definition and initialisation of items of this
 * module.
 *
 */
package wile.rsgauges;

import wile.rsgauges.items.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.item.Item;
import java.util.ArrayList;
import java.util.Arrays;

public class ModItems
{

  @GameRegistry.ObjectHolder("rsgauges:switchlink_pearl")       public static final ItemSwitchLinkPearl SWITCH_LINK_PEARL = null;
  private static final Item modItems[] = {
    new ItemSwitchLinkPearl("switchlink_pearl")
  };

  public static ArrayList<Item> registeredItems = new ArrayList<>();

  // Invoked from CommonProxy.registerItems()
  public static final void registerItems(RegistryEvent.Register<Item> event)
  {
    registeredItems.addAll(Arrays.asList(modItems));
    for(Item e:registeredItems) event.getRegistry().register(e);
  }

  // Invoked from ClientProxy.registerModels()
  @SideOnly(Side.CLIENT)
  public static final void initModels()
  {
    for(Item e:registeredItems) {
      if(e instanceof RsItem) ((RsItem)e).initModel();
    }
  }

}
