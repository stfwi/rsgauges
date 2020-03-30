/*
 * @file JEIPlugin.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2019 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * JEI plugin (see https://github.com/mezz/JustEnoughItems/wiki/Creating-Plugins)
 */
package wile.rsgauges.eapi.jei;

import wile.rsgauges.ModRsGauges;
import wile.rsgauges.detail.ModConfig;
import wile.rsgauges.ModContent;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import java.util.ArrayList;
import java.util.List;

@mezz.jei.api.JeiPlugin
public class JEIPlugin implements mezz.jei.api.IModPlugin
{
  @Override
  public ResourceLocation getPluginUid()
  { return new ResourceLocation(ModRsGauges.MODID, "jei_plugin_uid"); }

  @Override
  public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
  {
    List<ItemStack> blacklisted = new ArrayList<>();
    for(Block e: ModContent.getRegisteredBlocks()) {
      if(ModConfig.isOptedOut(e)) {
        blacklisted.add(new ItemStack(e.asItem()));
      }
    }
    if(!blacklisted.isEmpty()) {
      try {
        jeiRuntime.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM, blacklisted);
      } catch(Exception e) {
        ModRsGauges.logger().warn("Exception in JEI opt-out processing: '" + e.getMessage() + "', skipping further JEI optout processing.");
      }
    }
  }
}
