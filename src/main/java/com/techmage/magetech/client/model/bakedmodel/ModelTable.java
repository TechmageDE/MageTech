package com.techmage.magetech.client.model.bakedmodel;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.techmage.magetech.reference.Reference;
import com.techmage.magetech.utility.LogHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import java.io.IOException;
import java.util.Collection;

public class ModelTable implements IRetexturableModel
{
    public static final ModelTable MODEL = new ModelTable(ImmutableList.<ResourceLocation>of(), new ResourceLocation("magetech:blocks/table"));

    private final ImmutableList<ResourceLocation> modelLocation;
    private final ResourceLocation textureLocation;

    public ModelTable(ImmutableList<ResourceLocation> modelLocation, ResourceLocation textureLocation)
    {
        this.modelLocation = modelLocation;
        this.textureLocation = textureLocation;
    }

    @Override
    public Collection<ResourceLocation> getDependencies()
    {
        return ImmutableList.of();
    }

    @Override
    public Collection<ResourceLocation> getTextures()
    {
        ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();

        if (textureLocation != null)
            builder.add(textureLocation);

        return builder.build();
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
    {
        return new BakedModelTable(state, modelLocation, textureLocation, format, IPerspectiveAwareModel.MapWrapper.getTransforms(state));
    }

    @Override
    public IModelState getDefaultState()
    {
        return TRSRTransformation.identity();
    }

    @Override
    public IModel retexture(ImmutableMap<String, String> textures)
    {
        ResourceLocation base = textureLocation;

        if (textures.containsKey("texture"))
            base = new ResourceLocation(textures.get("texture"));

        return new ModelTable(this.modelLocation, base);
    }

    public enum ModelTableLoader implements ICustomModelLoader
    {
        instance;

        @Override
        public boolean accepts(ResourceLocation modelLocation)
        {
            return modelLocation.getResourceDomain().equals(Reference.MOD_ID) && (modelLocation.getResourcePath().equals("models/block/table"));
        }

        @Override
        public IModel loadModel(ResourceLocation modelLocation) throws IOException
        {
            if (modelLocation.getResourcePath().equals("models/block/table"))
                return new ModelTable(ImmutableList.<ResourceLocation> of(new ModelResourceLocation("magetech:table")), new ResourceLocation("magetech:blocks/table"));

            return ModelTable.MODEL;
        }

        @Override
        public void onResourceManagerReload(IResourceManager resourceManager)
        {

        }
    }
}