package com.techmage.magetech.event;

import com.techmage.magetech.block.BlockShelfScroll;
import com.techmage.magetech.block.BlockTable;
import com.techmage.magetech.reference.Names;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModelBakeEventHandler
{
    @SubscribeEvent
    public void onModelBake(ModelBakeEvent event)
    {
        BlockTable.replaceBlocksModels(Names.Blocks.TABLE, event);
        BlockShelfScroll.replaceBlocksModels(Names.Blocks.SHELF_SCROLL, event);
    }
}
