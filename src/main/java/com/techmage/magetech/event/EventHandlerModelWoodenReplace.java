package com.techmage.magetech.event;

import com.techmage.magetech.client.model.bakedmodel.BakedModelTable;
import com.techmage.magetech.client.model.bakedmodel.BakedModelWooden;
import com.techmage.magetech.reference.Names;
import com.techmage.magetech.utility.LogHelper;
import com.techmage.magetech.utility.ResourceHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandlerModelWoodenReplace
{
    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event)
    {
        replaceBlockModel(Names.Blocks.TABLE, "normal", event);
        replaceBlockModel(Names.Blocks.SHELF_SCROLL, "normal", event);
    }

    private static void replaceBlockModel(String blockName, String variant, ModelBakeEvent event)
    {
        ModelResourceLocation modelVariantLocation = new ModelResourceLocation(ResourceHelper.resource(blockName), variant);

        try
        {
            IModel model = ModelLoaderRegistry.getModel(ResourceHelper.getResource("block/" + blockName));

            if (model instanceof IRetexturableModel)
            {
                IRetexturableModel modelWooden = (IRetexturableModel) model;
                IBakedModel standard = event.getModelRegistry().getObject(modelVariantLocation);

                if (standard instanceof IPerspectiveAwareModel)
                {
                    IBakedModel finalModel;

                    if (blockName == Names.Blocks.TABLE)
                        finalModel = new BakedModelTable((IPerspectiveAwareModel) standard, modelWooden, DefaultVertexFormats.BLOCK);
                    else
                        finalModel = new BakedModelWooden((IPerspectiveAwareModel) standard, modelWooden, DefaultVertexFormats.BLOCK);

                    event.getModelRegistry().putObject(modelVariantLocation, finalModel);
                }
            }
        }

        catch(Exception e)
        {
            LogHelper.error(e);
        }
    }
}