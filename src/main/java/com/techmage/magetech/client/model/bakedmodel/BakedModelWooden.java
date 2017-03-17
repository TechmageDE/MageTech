package com.techmage.magetech.client.model.bakedmodel;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.techmage.magetech.block.BlockWooden;
import com.techmage.magetech.tileentity.TileEntityWooden;
import com.techmage.magetech.utility.ModelHelper;
import com.techmage.magetech.utility.NBTHelper;
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
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class BakedModelWooden implements IPerspectiveAwareModel
{
    private IPerspectiveAwareModel modelDefault;
    private IRetexturableModel modelWooden;

    private final Function<ResourceLocation, TextureAtlasSprite> textureGetter;
    protected final VertexFormat format;
    protected final Map<Map<String, EnumFacing>, IBakedModel> cache = Maps.newHashMap();

    public BakedModelWooden(IPerspectiveAwareModel modelDefault, IRetexturableModel modelWooden, VertexFormat format)
    {
        this.modelDefault = modelDefault;
        this.modelWooden = modelWooden;

        this.textureGetter = location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
        this.format = format;
    }

    private IBakedModel getActualModel(@Nonnull String texture, EnumFacing facing)
    {
        IBakedModel bakedModel = modelDefault;

        Map<String, EnumFacing> cacheKey = Maps.newHashMap();
        cacheKey.put(texture, facing);

        if (cache.containsKey(cacheKey))
            bakedModel = cache.get(cacheKey);

        else if (modelWooden != null)
        {
            ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();

            builder.put("wood", texture);
            IModel modelRetextured = modelWooden.retexture(builder.build());

            facing = facing != null ? facing : EnumFacing.NORTH;

            bakedModel = modelRetextured.bake(new TRSRTransformation(TRSRTransformation.getMatrix(facing)), format, textureGetter::apply);

            cache.put(cacheKey, bakedModel);
        }

        return bakedModel;
    }

    @Override
    @Nonnull
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
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

        assert texture != null;

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
    @Nonnull
    public TextureAtlasSprite getParticleTexture()
    {
        return modelDefault.getParticleTexture();
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public ItemCameraTransforms getItemCameraTransforms()
    {
        return modelDefault.getItemCameraTransforms();
    }

    @Override
    @Nonnull
    public ItemOverrideList getOverrides()
    {
        return WoodenItemOverrideList.INSTANCE;
    }

    private static class WoodenItemOverrideList extends ItemOverrideList
    {
        static WoodenItemOverrideList INSTANCE = new WoodenItemOverrideList();

        private WoodenItemOverrideList()
        {
            super(ImmutableList.of());
        }

        @Override
        @Nonnull
        public IBakedModel handleItemState(@Nonnull IBakedModel modelOriginal, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity)
        {
            if (modelOriginal instanceof BakedModelWooden)
            {
                ItemStack blockStack = new ItemStack(NBTHelper.getTagCompound(stack, TileEntityWooden.TAG_WOOD));

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