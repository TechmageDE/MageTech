package com.techmage.magetech.init;

import com.techmage.magetech.block.BlockShelfScroll;
import com.techmage.magetech.reference.Names;
import com.techmage.magetech.registry.BlockRegistry;
import net.minecraft.block.Block;

public class MageTechBlocks
{
    public static Block SHELF_SCROLL;

    public static void preInit()
    {
        SHELF_SCROLL = BlockRegistry.registerBlock(new BlockShelfScroll(Names.Blocks.SHELF_SCROLL));
    }
}