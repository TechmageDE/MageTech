package com.techmage.magetech.client.model.bakedmodel;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.techmage.magetech.block.BlockTable;
import com.techmage.magetech.reference.Names;
import com.techmage.magetech.utility.ResourceHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.vecmath.Matrix4f;
import java.util.List;
import java.util.Map;

public class BakedModelTable extends BakedModelWooden
{
    protected final Map<Map<String, EnumFacing>, IBakedModel> cache = Maps.newHashMap();

    public BakedModelTable(IPerspectiveAwareModel modelDefault, IRetexturableModel modelWooden, VertexFormat format)
    {
        super (modelDefault, modelWooden, format);
    }

    protected IBakedModel getActualModel(String texture, EnumFacing facing, boolean isDouble)
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

                try
                {
                    if (false)
                        modelWooden = (IRetexturableModel) ModelLoaderRegistry.getModel(ResourceHelper.getResource("block/" + Names.Blocks.TABLE + "_double"));

                    else
                        modelWooden = (IRetexturableModel) ModelLoaderRegistry.getModel(ResourceHelper.getResource("block/" + Names.Blocks.TABLE + "_single"));
                }

                catch (Exception e)
                {
                    e.printStackTrace();
                }

                IModel modelRetextured = modelWooden.retexture(builder.build());

                Matrix4f transformationMatrix = TRSRTransformation.getMatrix(facing);
                transformationMatrix.setScale(2);

                bakedModel = modelRetextured.bake(new TRSRTransformation(transformationMatrix), format, textureGetter);

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
        Boolean isDouble = false;

        if (state instanceof IExtendedBlockState)
        {
            IExtendedBlockState extendedState = (IExtendedBlockState) state;

            if (extendedState.getUnlistedNames().contains(BlockTable.WOOD))
                texture = extendedState.getValue(BlockTable.WOOD);

            if (extendedState.getUnlistedNames().contains(BlockTable.FACING))
                face = extendedState.getValue(BlockTable.FACING);

            if (extendedState.getUnlistedNames().contains(BlockTable.DOUBLE))
                isDouble = extendedState.getValue(BlockTable.DOUBLE);

            state = extendedState.withProperty(BlockTable.FACING, null);
        }

        if (texture.matches("missingno"))
            texture = "minecraft:blocks/planks_oak";

        return getActualModel(texture, face, isDouble).getQuads(state, side, rand);
    }
}