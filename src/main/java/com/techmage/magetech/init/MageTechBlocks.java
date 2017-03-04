package com.techmage.magetech.init;

import com.techmage.magetech.block.BlockShelfScroll;
import com.techmage.magetech.block.BlockTable;
import com.techmage.magetech.reference.Names;
import com.techmage.magetech.registry.BlockRegistry;
import net.minecraft.block.Block;

public class MageTechBlocks
{
    public static Block TABLE_OAK;
    public static Block TABLE_SPRUCE;
    public static Block TABLE_BIRCH;
    public static Block TABLE_JUNGLE;
    public static Block TABLE_ACACIA;
    public static Block TABLE_DARK_OAK;

    public static Block SHELF_SCROLL;

    public static void preInit()
    {
        TABLE_OAK = BlockRegistry.registerBlock(new BlockTable(Names.Blocks.TABLE_OAK));
        TABLE_SPRUCE = BlockRegistry.registerBlock(new BlockTable(Names.Blocks.TABLE_SPRUCE));
        TABLE_BIRCH = BlockRegistry.registerBlock(new BlockTable(Names.Blocks.TABLE_BIRCH));
        TABLE_JUNGLE = BlockRegistry.registerBlock(new BlockTable(Names.Blocks.TABLE_JUNGLE));
        TABLE_ACACIA = BlockRegistry.registerBlock(new BlockTable(Names.Blocks.TABLE_ACACIA));
        TABLE_DARK_OAK = BlockRegistry.registerBlock(new BlockTable(Names.Blocks.TABLE_DARK_OAK));

        SHELF_SCROLL = BlockRegistry.registerBlock(new BlockShelfScroll(Names.Blocks.SHELF_SCROLL));
    }
}