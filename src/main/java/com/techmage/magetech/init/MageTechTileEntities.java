package com.techmage.magetech.init;

import com.techmage.magetech.tileentity.TileEntityWooden;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class MageTechTileEntities
{
    public static void init()
    {
        GameRegistry.registerTileEntity(TileEntityWooden.class, "tileentity_wooden");
    }
}