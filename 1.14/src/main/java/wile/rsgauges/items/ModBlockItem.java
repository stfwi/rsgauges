package wile.rsgauges.items;

import wile.rsgauges.ModRsGauges;
import wile.rsgauges.detail.ModConfig;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public final class ModBlockItem extends BlockItem
{
  private static final Collection<ItemGroup> ENABLED_TABS  = Collections.singletonList(ModRsGauges.ITEMGROUP);
  private static final Collection<ItemGroup> DISABLED_TABS = new ArrayList<ItemGroup>();

  public ModBlockItem(Block blockIn, Item.Properties builder)
  { super(blockIn, builder); }

  @Override
  public Collection<ItemGroup> getCreativeTabs()
  { return ModConfig.isOptedOut(this) ? (DISABLED_TABS) : (ENABLED_TABS); }

}
