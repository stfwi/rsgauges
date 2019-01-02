/*
 * @file RecipeCondRegistered.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Recipe condition to enable opt'ing out JSON based recipes, referenced
 * in assets/rsgauges/recipes/_factories.json with full path (therefore
 * I had to make a separate file for that instead of a few lines in
 * ModAuxiliaries).
 */
package wile.rsgauges.detail;

import wile.rsgauges.ModRsGauges;
import wile.rsgauges.blocks.ModBlocks;
import wile.rsgauges.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import com.google.gson.*;
import java.util.function.BooleanSupplier;


public class RecipeCondModSpecific implements IConditionFactory
{
  public static final BooleanSupplier RECIPE_INCLUDE = ()->true;
  public static final BooleanSupplier RECIPE_EXCLUDE = ()->false;

  @Override
  public BooleanSupplier parse(JsonContext context, JsonObject json) {
    try {
      final IForgeRegistry<Block> block_registry = ForgeRegistries.BLOCKS;
      final IForgeRegistry<Item> item_registry = ForgeRegistries.ITEMS;
      final JsonArray items = json.getAsJsonArray("items");
      if(items!=null) {
        for(JsonElement e: items) {
          if(!e.isJsonPrimitive()) continue;
          final ResourceLocation rl = new ResourceLocation(((JsonPrimitive) e).getAsString());
          if((!block_registry.containsKey(rl)) && (!item_registry.containsKey(rl))) return RECIPE_EXCLUDE;
        }
      }
      final JsonPrimitive result = json.getAsJsonPrimitive("result");
      if(result != null) {
        final ResourceLocation rl = new ResourceLocation(result.getAsString());
        if(block_registry.containsKey(rl)) {
          if(!ModBlocks.enabled(block_registry.getValue(rl))) return RECIPE_EXCLUDE;
        } else if(item_registry.containsKey(rl)) {
          if(!ModItems.enabled(item_registry.getValue(rl))) return RECIPE_EXCLUDE;
        } else {
          return RECIPE_EXCLUDE;
        }
      }
      return RECIPE_INCLUDE;
    } catch(Throwable ex) {
      ModRsGauges.logger.error("rsgauges::ResultRegisteredCondition failed: " + ex.toString());
    }
    return RECIPE_EXCLUDE;
  }
}
