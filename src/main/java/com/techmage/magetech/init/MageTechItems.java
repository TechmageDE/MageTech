package com.techmage.magetech.init;

import com.techmage.magetech.item.ItemMageTech;
import com.techmage.magetech.reference.Names;
import com.techmage.magetech.registry.ItemRegistry;
import net.minecraft.item.Item;


public class MageTechItems
{
    public static Item RESEARCH_NOTE;
    public static Item RESEARCH_SCROLL;

    public static void preInit()
    {
        RESEARCH_NOTE = ItemRegistry.registerItem(new ItemMageTech(Names.Items.RESEARCH_NOTE));
        RESEARCH_SCROLL = ItemRegistry.registerItem(new ItemMageTech(Names.Items.RESEARCH_SCROLL));
    }
}