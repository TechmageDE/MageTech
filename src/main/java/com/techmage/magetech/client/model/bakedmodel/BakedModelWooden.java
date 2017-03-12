package com.techmage.magetech.client.model.bakedmodel;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.techmage.magetech.block.BlockWooden;
import com.techmage.magetech.tileentity.TileEntityWooden;
import com.techmage.magetech.utility.ModelHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.vecmath.Matrix4f;
import java.util.List;
import java.util.Map;

public class BakedModelWooden implements IPerspectiveAwareModel
{
    protected IPerspectiveAwareModel modelDefault;
    protected IRetexturableModel modelWooden;

    protected final Map<Map<String, EnumFacing>, IBakedModel> cache = Maps.newHashMap();
    protected final Function<ResourceLocation, TextureAtlasSprite> textureGetter;
    protected final VertexFormat format;

    public BakedModelWooden(IPerspectiveAwareModel modelDefault, IRetexturableModel modelWooden, VertexFormat format)
    {
        this.modelDefault = modelDefault;
        this.modelWooden = modelWooden;

        this.textureGetter = location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
        this.format = format;
    }

    protected IBakedModel getActualModel(String texture, EnumFacing facing)
    {
        IBakedModel bakedModel = modelDefault;

        if (texture != null)
        {
            Map<String, EnumFacing> cacheKey = Maps.newHashMap();
            cacheKey.put(texture, facing);

            if (cache.containsKey(cacheKey))
                bakedModel = cache.get(cacheKey);

            else if (modelWooden != null)
            {
                ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();

                builder.put("wood", texture);
                IModel modelRetextured = modelWooden.retexture(builder.build());

                bakedModel = modelRetextured.bake(new TRSRTransformation(TRSRTransformation.getMatrix(facing)), format, textureGetter);

                cache.put(cacheKey, bakedModel);
            }
        }

        return bakedModel;
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
    {
        String texture = null;
        EnumFacing face = EnumFacing.NORTH;

        if (state instanceof IExtendedBlockState)
        {
            IExtendedBlockState extendedState = (IExtendedBlockState) state;

            if (extendedState.getUnlistedNames().contains(BlockWooden.WOOD))
                texture = extendedState.getValue(BlockWooden.WOOD);

            if (extendedState.getUnlistedNames().contains(BlockWooden.FACING))
                face = extendedState.getValue(BlockWooden.FACING);

            state = extendedState.withProperty(BlockWooden.FACING, null);
        }

        if (texture.matches("missingno"))
            texture = "minecraft:blocks/planks_oak";

        return getActualModel(texture, face).getQuads(state, side, rand);
    }

    @Override
    public boolean isAmbientOcclusion()
    {
        return modelDefault.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d()
    {
        return modelDefault.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer()
    {
        return modelDefault.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture()
    {
        return modelDefault.getParticleTexture();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms()
    {
        return modelDefault.getItemCameraTransforms();
    }

    @Nonnull
    @Override
    public ItemOverrideList getOverrides()
    {
        return TableItemOverrideList.INSTANCE;
    }

    private static class TableItemOverrideList extends ItemOverrideList
    {
        static TableItemOverrideList INSTANCE = new TableItemOverrideList();

        private TableItemOverrideList()
        {
            super(ImmutableList.of());
        }

        @Override
        public IBakedModel handleItemState(IBakedModel modelOriginal, ItemStack stack, World world, EntityLivingBase entity)
        {
            if (modelOriginal instanceof BakedModelWooden)
            {
                ItemStack blockStack = new ItemStack(stack.getTagCompound().getCompoundTag(TileEntityWooden.TAG_WOOD));

                if (blockStack != ItemStack.EMPTY)
                {
                    Block block = Block.getBlockFromItem(blockStack.getItem());
                    String texture = ModelHelper.getTextureFromBlock(block, blockStack.getItemDamage()).getIconName();

                    return ((BakedModelWooden) modelOriginal).getActualModel(texture, null);
                }
            }

            return modelOriginal;
        }
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType)
    {
        Pair<? extends IBakedModel, Matrix4f> pair = modelDefault.handlePerspective(cameraTransformType);

        return Pair.of(this, pair.getRight());
    }
}