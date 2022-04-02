package wile.rsgauges.items;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import wile.rsgauges.ModConfig;
import wile.rsgauges.libmc.detail.Registries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public final class ModBlockItem extends BlockItem
{
  private static final Collection<CreativeModeTab> DISABLED_TABS = new ArrayList<CreativeModeTab>();

  public ModBlockItem(Block blockIn, Item.Properties builder)
  { super(blockIn, builder); }

  @Override
  public Collection<CreativeModeTab> getCreativeTabs()
  { return ModConfig.isOptedOut(this) ? (DISABLED_TABS) : (Collections.singletonList(Registries.getCreativeModeTab())); }

}
