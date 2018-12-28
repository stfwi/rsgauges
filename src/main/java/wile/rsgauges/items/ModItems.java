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
package wile.rsgauges.items;

import wile.rsgauges.ModRsGauges;
import wile.rsgauges.detail.ModConfig;
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

  // <------------------------------------------------------------------------------------------------------------------

  private static final Item modItems[] = {
    new ItemSwitchLinkPearl("switchlink_pearl")
  };

  // <------------------------------------------------------------------------------------------------------------------

  public static boolean enabled(Item item) {
    if((ModConfig.optouts.without_switch_linking) && (item instanceof ItemSwitchLinkPearl)) return false;
    return true;
  }

  public static ArrayList<Item> registeredItems = new ArrayList<>();

  // Invoked from CommonProxy.registerItems()
  public static final void registerItems(RegistryEvent.Register<Item> event)
  {
    for(Item e:modItems) {
      if(ModConfig.zmisc.without_optout_registrations && (!enabled(e))) continue;
      registeredItems.add(e);
    }
    if(ModConfig.zmisc.without_optout_registrations) ModRsGauges.logger.info("Registration opt-out configured, disabled item categories will not be registered at all.");
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
