package com.techmage.magetech.registry;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashSet;
import java.util.Set;

public class ItemRegistry
{
    public static final Set<Item> ITEMS = new HashSet<>();

    public static <T extends Item> T registerItem(T item)
    {
        GameRegistry.register(item);

        ITEMS.add(item);

        return item;
    }
}