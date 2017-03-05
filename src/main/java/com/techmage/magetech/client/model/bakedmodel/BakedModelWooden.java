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
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;
import java.util.List;
import java.util.Map;

public class BakedModelWooden implements IPerspectiveAwareModel
{
    private final IPerspectiveAwareModel modelDefault;
    private final IRetexturableModel modelWooden;

    private final Map<String, IBakedModel> cache = Maps.newHashMap();
    private final Function<ResourceLocation, TextureAtlasSprite> textureGetter;
    private final VertexFormat format;
    private final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms;

    public BakedModelWooden(IPerspectiveAwareModel modelDefault, IRetexturableModel modelWooden, VertexFormat format)
    {
        this.modelDefault = modelDefault;
        this.modelWooden = modelWooden;

        this.textureGetter = location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
        this.format = format;
        this.transforms = ModelHelper.getTransforms(modelDefault);
    }

    protected IBakedModel getActualModel(String texture, EnumFacing facing)
    {
        IBakedModel bakedModel = modelDefault;

        if (texture != null)
        {
            if (cache.containsKey(texture))
                bakedModel = cache.get(texture);

            else if (modelWooden != null)
            {
                ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();

                builder.put("wood", texture);

                IModel modelRetextured = modelWooden.retexture(builder.build());
                IModelState modelState = new SimpleModelState(transforms);

                bakedModel = modelRetextured.bake(modelState, format, textureGetter);

                cache.put(texture, bakedModel);
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

            if (extendedState.getUnlistedNames().contains(BlockWooden.UP_WOOD))
                texture = extendedState.getValue(BlockWooden.UP_WOOD);

            if (extendedState.getUnlistedNames().contains(BlockWooden.UP_FACING))
                face = extendedState.getValue(BlockWooden.UP_FACING);

            state = extendedState.withProperty(BlockWooden.UP_FACING, null);
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