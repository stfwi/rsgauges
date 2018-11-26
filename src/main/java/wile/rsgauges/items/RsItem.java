package wile.rsgauges.items;

import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wile.rsgauges.ModRsGauges;

public class RsItem extends Item
{

  RsItem(String registryName)
  {
    super();
    setRegistryName(ModRsGauges.MODID, registryName);
    setUnlocalizedName(ModRsGauges.MODID + "." + registryName);
    setMaxStackSize(64);
    setCreativeTab(ModRsGauges.CREATIVE_TAB_RSGAUGES);
    setHasSubtypes(false);
    setMaxDamage(0);
    setNoRepair();
  }

  @SideOnly(Side.CLIENT)
  public void initModel()
  {
    ModelResourceLocation rc = new ModelResourceLocation(this.getRegistryName(), "inventory");
    ModelBakery.registerItemVariants(this, rc);
    ModelLoader.setCustomMeshDefinition(this, stack->rc);
  }

}
