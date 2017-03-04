package com.techmage.magetech.client.model.bakedmodel;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.techmage.magetech.block.BlockTable;
import com.techmage.magetech.client.model.CompositeModel;
import com.techmage.magetech.utility.LogHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BakedModelTable implements IPerspectiveAwareModel, IResourceManagerReloadListener
{

    protected final IModelState modelState;
    protected final ImmutableList<ResourceLocation> modelLocation;
    protected final ResourceLocation textureLocation;
    protected final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms;
    protected final VertexFormat format;
    protected final IModel baseModel;

    public BakedModelTable(IModelState modelState, ImmutableList<ResourceLocation> modelLocation, ResourceLocation textureLocation, VertexFormat fmt,
                           ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms)
    {
        this.modelState = modelState;
        this.modelLocation = modelLocation;
        this.textureLocation = textureLocation;
        this.format = fmt;
        this.transforms = transforms;

        this.baseModel = ModelLoaderRegistry.getModelOrLogError(modelLocation.get(0), "Base model not found " + modelLocation.get(0));
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        this.cache.clear();
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
    {
        if (state instanceof IExtendedBlockState)
        {
            IExtendedBlockState extendedState = (IExtendedBlockState) state;

            if (extendedState.getValue(BlockTable.WOOD_CRAFTED_WITH) != null)
            {
                ItemStack woodCraftedWith = extendedState.getValue(BlockTable.WOOD_CRAFTED_WITH);

                return this.getCachedModel(woodCraftedWith).getQuads(state, side, rand);
            }
        }

        return ImmutableList.of();
    }

    protected final Map<String, IBakedModel> cache = new HashMap<>();

    public IBakedModel getCachedModel(ItemStack woodCraftedWith)
    {
        if (!this.cache.containsKey(woodCraftedWith.getUnlocalizedName()))
        {
            ImmutableMap.Builder<String, String> newTexture = ImmutableMap.builder();

            String unlocalizedName = woodCraftedWith.getUnlocalizedName();
            String texturePath = woodCraftedWith.getItem().getRegistryName().getResourceDomain() + ":textures/blocks/planks_";
            texturePath += unlocalizedName.substring(unlocalizedName.lastIndexOf('.') + 1, unlocalizedName.length());

            newTexture.put("texture", texturePath);

            LogHelper.info(texturePath);
            LogHelper.info("Table add cached model: " + woodCraftedWith.getUnlocalizedName());

            this.cache.put(woodCraftedWith.getUnlocalizedName(), generateModel(newTexture.build()));
        }

        return this.cache.get(woodCraftedWith.getUnlocalizedName());
    }


    protected IBakedModel generateModel(ImmutableMap<String, String> texture)
    {
        ImmutableList.Builder<IBakedModel> builder = ImmutableList.builder();

        builder.add(ModelProcessingHelper.retexture(this.baseModel, texture).bake(this.modelState, this.format, ModelLoader.defaultTextureGetter()));

        return new CompositeModel(builder.build());
    }


    protected IBakedModel generateModel(ImmutableMap<String, String> texture, IModel... models)
    {
        ImmutableList.Builder<IBakedModel> builder = ImmutableList.builder();

        builder.add(this.generateModel(texture));

        for (IModel model : models)
            builder.add(model.bake(this.modelState, this.format, ModelLoader.defaultTextureGetter()));

        return new CompositeModel(builder.build());
    }

    @Override
    public boolean isAmbientOcclusion()
    {
        return true;
    }

    @Override
    public boolean isGui3d()
    {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer()
    {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture()
    {
        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(textureLocation.toString());
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms()
    {
        return baseModel.bake(this.modelState, this.format, ModelLoader.defaultTextureGetter()).getItemCameraTransforms();
    }

    @Override
    public ItemOverrideList getOverrides()
    {
        return itemHandler;
    }

    private final ItemOverrideList itemHandler = new ItemOverrideList(Lists.<ItemOverride> newArrayList())
    {
        @Override
        public IBakedModel handleItemState(IBakedModel model, ItemStack stack, World world, EntityLivingBase entity)
        {
            return BakedModelTable.this.getCachedModel(new ItemStack(Blocks.WOODEN_SLAB, 1, 0));
        }
    };

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType)
    {
        return IPerspectiveAwareModel.MapWrapper.handlePerspective(baseModel.bake(this.modelState, this.format, ModelLoader.defaultTextureGetter()), transforms, cameraTransformType);
    }
}