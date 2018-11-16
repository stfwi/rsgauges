//
// Rendering tests using baked models and TESR
//
package wile.rsgauges.client;

import wile.rsgauges.ModRsGauges;
import wile.rsgauges.blocks.RsBlock;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;
import com.google.common.collect.ImmutableList;

@SideOnly(Side.CLIENT)
public class JitModelBakery
{
  /**
   * Called from `RsBlock.initModel()`. A baked model to be used can be specified, which
   * has to be extended from JitBakedModel. The `JitBakedModel.getQuads(IBlockstate, ...)` can be overloaded
   * to change the behaviour depending on the `IBlockstate` argument.
   *
   * @param block
   * @param jitbakedmodel
   */
  public static void initModelRegistrations(final RsBlock block, JitBakedModel jitbakedmodel) {
    ModelLoader.setCustomStateMapper(block, new JitModelBakery.JitStateMapper(block));
    ModelLoaderRegistry.registerLoader(new JitModelBakery.JitModelLoader(block, jitbakedmodel));
  }

  /**
   * Maps all requested states of the block to the same JIT model. The JIT model name is the
   * standard blockstate json file (registry name based blockstate json file) with "_jit"
   * appended. The model loader below is adapted to this accordingly.
   */
  public static class JitStateMapper extends StateMapperBase
  {
    final RsBlock block;
    JitStateMapper(final RsBlock block) { this.block = block; }

    @Override protected ModelResourceLocation getModelResourceLocation(IBlockState blockstate) {
      return new ModelResourceLocation(ModRsGauges.MODID + ":" + block.getRegistryName().getResourcePath() + "_jit", this.getPropertyString(blockstate.getProperties()) );
    }
  }

  /**
   * Loads the mapped JIT model locations (blockstate JSONS), returning the
   * JitModels corresponding to the original (JSON) models without "_jit"
   * suffix.
   */
  public static class JitModelLoader implements ICustomModelLoader
  {
    private IResourceManager manager = null;
    private JitBakedModel jitbakedmodel;
    final private RsBlock block;
    final private String match;

    public JitModelLoader(final RsBlock block, JitBakedModel jitbakedmodel) {
      this.block = block;
      this.jitbakedmodel = jitbakedmodel;
      match = ModRsGauges.MODID + ":" + block.getRegistryName().getResourcePath() + "_jit";
      if(manager==null){;} // unused warning supression, I don't like to remove the RM completely now.
    }

    @Override public boolean accepts(ResourceLocation rl) {
      return (rl.toString().equals(match)) || (rl.toString().startsWith(match+"#"));
    }

    @Override public IModel loadModel(ResourceLocation rl) {
      if(!accepts(rl)) return ModelLoaderRegistry.getMissingModel();
      return new JitModelBakery.JitModel(this.block, rl, this.jitbakedmodel);
    }

    @Override public void onResourceManagerReload(IResourceManager rm) { manager = rm; }
  }

  /**
   * Passthrough model, baking a JitBakedModel that corresponds to the JSON model resource path.
   * The original model will be baked and registered at startup as normal. The JitBakedModel
   * will allow to override getQuads(), where it is ensured that all state combinations of the
   * original models are loaded.
   */
  public static class JitModel implements IModel
  {
    protected final ModelResourceLocation modelrl;
    protected JitBakedModel jitbakedmodel;

    public JitModel(final RsBlock block, ResourceLocation rl, JitBakedModel jitbakedmodel) {
      this.jitbakedmodel = jitbakedmodel;
      this.modelrl = new ModelResourceLocation(rl.toString().replace("_jit", ""));
    }

    @Override
    public Collection<ResourceLocation> getDependencies()
    { return ImmutableList.copyOf(new ModelResourceLocation[]{modelrl}); }

    @Override
    public Collection<ResourceLocation> getTextures()
    { return ImmutableList.copyOf(new ResourceLocation[0]); } // should be covered in the model referred to

    @Override
    public IModelState getDefaultState()
    { return null; }

    @Override
    public IBakedModel bake(IModelState modelstate, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)  {
      IBakedModel bakedmodel = ModelLoaderRegistry.getMissingModel().bake(ModelLoaderRegistry.getMissingModel().getDefaultState(), format, bakedTextureGetter);
      {
        IModel model = null;
        try {
          model = ModelLoaderRegistry.getModel(modelrl);
          if(model != null)  bakedmodel = model.bake((modelstate!=null) ? modelstate : model.getDefaultState(), format, bakedTextureGetter);
        } catch (Exception exception) {
          ModRsGauges.logger.error("JitModel.bake() " + ((model==null) ? "" : (model.toString())) + ") exception:" + exception);
          bakedmodel = ModelLoaderRegistry.getMissingModel().bake(ModelLoaderRegistry.getMissingModel().getDefaultState(), format, bakedTextureGetter);
        }
      }
      return jitbakedmodel.push(modelrl.getVariant().hashCode(), bakedmodel);
    }
  }

  /**
   * Baked JIT model generated and returned by JitBakedModel.
   * All model states are pre-baked by the variant loader and bakery at startup.
   * This model corresponds to the model that would loaded anyway if no JIT model
   * would be used at all.
   *
   * -> means this is the one where getQuads() can be overridden to customise the
   *    model rendering.
   */
  public static class JitBakedModel implements IBakedModel
  {
    protected HashMap<Integer,IBakedModel> baked;
    protected IBakedModel firstbaked = null;

    public JitBakedModel()
    { baked = new HashMap<Integer,IBakedModel>(); }

    public JitBakedModel push(final int statehash, final IBakedModel model)
    { baked.put(statehash, model); firstbaked=(firstbaked==null) ? model : firstbaked; return this; }

    @Override
    public boolean isBuiltInRenderer()
    { return false; }

    @Override
    public boolean isAmbientOcclusion()
    { return firstbaked.isAmbientOcclusion(); }

    @Override
    public boolean isGui3d()
    { return firstbaked.isGui3d(); }

    @Override
    public TextureAtlasSprite getParticleTexture()
    { return firstbaked.getParticleTexture(); }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType)
    { return Pair.of(this, firstbaked.handlePerspective(cameraTransformType).getRight()); }

    @Override
    public ItemOverrideList getOverrides()
    { return null; }

    @Override
    @SuppressWarnings("deprecation")
    public ItemCameraTransforms getItemCameraTransforms()
    { return firstbaked.getItemCameraTransforms(); }

    public static int getStateVariantHash(IBlockState blockstate)
    { // I don't trust that blockstate.getPropertes().hashCode() will be the same
      if((blockstate==null) || (blockstate.getPropertyKeys().size()==0)) return 0;
      if(blockstate instanceof IExtendedBlockState) blockstate = ((IExtendedBlockState)blockstate).getClean();
      String a[] = new String[blockstate.getPropertyKeys().size()];
      int i = 0;
      for(IProperty<?> p:blockstate.getPropertyKeys()) a[i++] = p.getName()+"="+blockstate.getValue(p).toString();
      Arrays.sort(a);
      String s = String.join(",", a);
      return s.hashCode();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState blockstate, @Nullable EnumFacing side, long rand)
    {
      if(blockstate instanceof IExtendedBlockState) blockstate = ((IExtendedBlockState)blockstate).getClean();
      return baked.getOrDefault(getStateVariantHash(blockstate), firstbaked).getQuads(blockstate, side, rand);
    }
  }

  /**
   * TESR applying the model corresponding to the actual block state.
   */
  public static class JitModelTesr<TeType extends RsBlock.RsTileEntity<?>> extends TileEntitySpecialRenderer<TeType>
  {
    @Override
    public void render(TeType te, double x, double y, double z, float partialTicks, int destroyStage, float alpha)
    {
      BlockRendererDispatcher mcbrd = Minecraft.getMinecraft().getBlockRendererDispatcher();
      BlockPos pos = te.getPos();
      IBlockAccess world = MinecraftForgeClient.getRegionRenderCache(te.getWorld(), pos);
      IBlockState state = world.getBlockState(pos).getActualState(world, pos);
      IBakedModel model = mcbrd.getBlockModelShapes().getModelForState(state);
      GlStateManager.pushAttrib();
      GlStateManager.pushMatrix();
      RenderHelper.disableStandardItemLighting();
      GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());
      try {
        Tessellator tessellator = Tessellator.getInstance();
        tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        mcbrd.getBlockModelRenderer().renderModel(world, model, state, pos, tessellator.getBuffer(), false);
        tessellator.draw();
      } catch(Exception e) {
        ModRsGauges.logger.error("TESR exception for model " + model.toString() + ": " + e.toString());
      }
      RenderHelper.enableStandardItemLighting();
      GlStateManager.translate(te.getPos().getX(), te.getPos().getY(), te.getPos().getZ());
      GlStateManager.popMatrix();
      GlStateManager.popAttrib();
    }
  }

}
