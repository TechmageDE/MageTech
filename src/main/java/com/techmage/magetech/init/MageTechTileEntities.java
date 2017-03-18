package com.techmage.magetech.init;

import com.techmage.magetech.reference.Names;
import com.techmage.magetech.tileentity.TileEntityShelfScrolls;
import com.techmage.magetech.tileentity.TileEntityWooden;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MageTechTileEntities
{
    public static void init()
    {
        GameRegistry.registerTileEntity(TileEntityWooden.class, Names.TileEntity.WOODEN);
        GameRegistry.registerTileEntity(TileEntityShelfScrolls.class, Names.TileEntity.SHELF_SCROLLS);
    }
}