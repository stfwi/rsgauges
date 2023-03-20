/*
 * @file OptionalRecipeCondition.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2020 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Recipe condition to enable opt'ing out JSON based recipes.
 */
package wile.rsgauges.libmc.detail;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


public class OptionalRecipeCondition implements ICondition
{
  private static ResourceLocation NAME;

  private final List<ResourceLocation> all_required;
  private final List<ResourceLocation> any_missing;
  private final List<ResourceLocation> all_required_tags;
  private final List<ResourceLocation> any_missing_tags;
  private final @Nullable ResourceLocation result;
  private final boolean result_is_tag;
  private final boolean experimental;

  private static boolean with_experimental = false;
  private static boolean without_recipes = false;
  private static Predicate<Block> block_optouts = (block)->false;
  private static Predicate<Item> item_optouts = (item)->false;

  public static void init(String modid, Logger logger)
  {
    NAME = new ResourceLocation(modid, "optional");
  }

  public static void on_config(boolean enable_experimental, boolean disable_all_recipes,
                               Predicate<Block> block_optout_provider,
                               Predicate<Item> item_optout_provider)
  {
    with_experimental = enable_experimental;
    without_recipes = disable_all_recipes;
    block_optouts = block_optout_provider;
    item_optouts = item_optout_provider;
  }

  public OptionalRecipeCondition(ResourceLocation result, List<ResourceLocation> required, List<ResourceLocation> missing, List<ResourceLocation> required_tags, List<ResourceLocation> missing_tags, boolean isexperimental, boolean result_is_tag)
  {
    all_required = required;
    any_missing = missing;
    all_required_tags = required_tags;
    any_missing_tags = missing_tags;
    this.result = result;
    this.result_is_tag = result_is_tag;
    experimental=isexperimental;
  }

  @Override
  public ResourceLocation getID()
  { return NAME; }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("Optional recipe, all-required: [");
    for(ResourceLocation e:all_required) sb.append(e.toString()).append(",");
    for(ResourceLocation e:all_required_tags) sb.append("#").append(e.toString()).append(",");
    sb.delete(sb.length()-1, sb.length()).append("], any-missing: [");
    for(ResourceLocation e:any_missing) sb.append(e.toString()).append(",");
    for(ResourceLocation e:any_missing_tags) sb.append("#").append(e.toString()).append(",");
    sb.delete(sb.length()-1, sb.length()).append("]");
    if(experimental) sb.append(" EXPERIMENTAL");
    return sb.toString();
  }

  @Override
  public boolean test(IContext context)
  {
    if(without_recipes) return false;
    if((experimental) && (!with_experimental)) return false;
    final IForgeRegistry<Item> item_registry = ForgeRegistries.ITEMS;
    //final Collection<ResourceLocation> item_tags = SerializationTags.getInstance().getOrEmpty(Registry.ITEM_REGISTRY).getAvailableTags();
    if(result != null) {
      boolean item_registered = item_registry.containsKey(result);
      if(!item_registered) return false; // required result not registered
      if(item_optouts.test(item_registry.getValue(result))) return false;
      if(ForgeRegistries.BLOCKS.containsKey(result) && block_optouts.test(ForgeRegistries.BLOCKS.getValue(result))) return false;
    }
    if(!all_required.isEmpty()) {
      for(ResourceLocation rl:all_required) {
        if(!item_registry.containsKey(rl)) return false;
      }
    }
    if(!all_required_tags.isEmpty()) {
      for(ResourceLocation rl:all_required_tags) {
        if(item_registry.tags().getTagNames().noneMatch(tk->tk.location().equals(rl))) return false;  // if(!item_tags.contains(rl)) return false;
      }
    }
    if(!any_missing.isEmpty()) {
      for(ResourceLocation rl:any_missing) {
        if(!item_registry.containsKey(rl)) return true;
      }
      return false;
    }
    if(!any_missing_tags.isEmpty()) {
      for(ResourceLocation rl:any_missing_tags) {
        if(item_registry.tags().getTagNames().noneMatch(tk->tk.location().equals(rl))) return true; // if(!item_tags.contains(rl)) return true;
      }
      return false;
    }
    return true;
  }

  public static class Serializer implements IConditionSerializer<OptionalRecipeCondition>
  {
    public static final Serializer INSTANCE = new Serializer();

    @Override
    public ResourceLocation getID()
    { return OptionalRecipeCondition.NAME; }

    @Override
    public void write(JsonObject json, OptionalRecipeCondition condition)
    {
      JsonArray required = new JsonArray();
      JsonArray missing = new JsonArray();
      for(ResourceLocation e:condition.all_required) required.add(e.toString());
      for(ResourceLocation e:condition.any_missing) missing.add(e.toString());
      json.add("required", required);
      json.add("missing", missing);
      if(condition.result != null) {
        json.addProperty("result", (condition.result_is_tag ? "#" : "") + condition.result);
      }
    }

    @Override
    public OptionalRecipeCondition read(JsonObject json)
    {
      List<ResourceLocation> required = new ArrayList<>();
      List<ResourceLocation> missing = new ArrayList<>();
      List<ResourceLocation> required_tags = new ArrayList<>();
      List<ResourceLocation> missing_tags = new ArrayList<>();
      ResourceLocation result = null;
      boolean experimental = false;
      boolean result_is_tag = false;
      if(json.has("result")) {
        String s = json.get("result").getAsString();
        if(s.startsWith("#")) {
          result = new ResourceLocation(s.substring(1));
          result_is_tag = true;
        } else {
          result = new ResourceLocation(s);
        }
      }
      if(json.has("required")) {
        for(JsonElement e:GsonHelper.getAsJsonArray(json, "required")) {
          String s = e.getAsString();
          if(s.startsWith("#")) {
            required_tags.add(new ResourceLocation(s.substring(1)));
          } else {
            required.add(new ResourceLocation(s));
          }
        }
      }
      if(json.has("missing")) {
        for(JsonElement e:GsonHelper.getAsJsonArray(json, "missing")) {
          String s = e.getAsString();
          if(s.startsWith("#")) {
            missing_tags.add(new ResourceLocation(s.substring(1)));
          } else {
            missing.add(new ResourceLocation(s));
          }
        }
      }
      if(json.has("experimental")) experimental = json.get("experimental").getAsBoolean();
      return new OptionalRecipeCondition(result, required, missing, required_tags, missing_tags, experimental, result_is_tag);
    }
  }
}
