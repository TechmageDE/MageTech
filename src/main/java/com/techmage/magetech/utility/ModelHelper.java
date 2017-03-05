package com.techmage.magetech.utility;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelHelper
{
    public static TextureAtlasSprite getTextureFromBlock(Block block, int meta)
    {
        IBlockState state = block.getStateFromMeta(meta);
        return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state);
    }

    public static ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> getTransforms(IPerspectiveAwareModel model)
    {
        ImmutableMap.Builder<ItemCameraTransforms.TransformType, TRSRTransformation> builder = ImmutableMap.builder();

        for (ItemCameraTransforms.TransformType type : ItemCameraTransforms.TransformType.values())
        {
            TRSRTransformation transformation = new TRSRTransformation(model.handlePerspective(type).getRight());

            if(!transformation.equals(TRSRTransformation.identity()))
                builder.put(type, TRSRTransformation.blockCenterToCorner(transformation));
        }

        return builder.build();
    }

    public static ResourceLocation getModelLocation(ResourceLocation location)
    {
        return new ResourceLocation(location.getResourceDomain(), "models/" + location.getResourcePath() + ".json");
    }
}
