package com.techmage.magetech.event;

import com.techmage.magetech.client.model.bakedmodel.BakedModelWooden;
import com.techmage.magetech.reference.Names;
import com.techmage.magetech.reference.Reference;
import com.techmage.magetech.utility.BlockStateReader;
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
        replaceBlockModels(Names.Blocks.TABLE, event);
        replaceBlockModels(Names.Blocks.SHELF_SCROLL, event);
    }

    private void replaceBlockModels(String blockName, ModelBakeEvent event)
    {
        BlockStateReader blockStateReader = new BlockStateReader(Reference.MOD_ID);

        for (String[] modelForState : blockStateReader.getModelsForStates(blockName))
            replaceBlockModel(blockName, modelForState[0], modelForState[1], event);
    }

    private static void replaceBlockModel(String blockName, String variant, String modelName, ModelBakeEvent event)
    {
        ModelResourceLocation modelVariantLocation = new ModelResourceLocation(ResourceHelper.resource(blockName), variant);

        try
        {
            IModel model = ModelLoaderRegistry.getModel(ResourceHelper.getResource("block/" + modelName));

            if (model instanceof IRetexturableModel)
            {
                IRetexturableModel modelWooden = (IRetexturableModel) model;
                IBakedModel standard = event.getModelRegistry().getObject(modelVariantLocation);

                if (standard instanceof IPerspectiveAwareModel)
                {
                    IBakedModel finalModel;

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