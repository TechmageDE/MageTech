package com.techmage.magetech.init;

import com.techmage.magetech.item.ItemMageTech;
import com.techmage.magetech.reference.Names;
import com.techmage.magetech.reference.Reference;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
@GameRegistry.ObjectHolder(Reference.MOD_ID)
public class MageTechItems
{
    public static Item RESEARCH_NOTE = new ItemMageTech(Names.Items.RESEARCH_NOTE);
    public static Item RESEARCH_SCROLL = new ItemMageTech(Names.Items.RESEARCH_SCROLL);

    @Mod.EventBusSubscriber
    public static class RegistrationHandler
    {
        public static final Set<Item> ITEMS = new HashSet<>();

        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event)
        {
            final Item[] items =
            {
                RESEARCH_NOTE,
                RESEARCH_SCROLL
            };

            final IForgeRegistry<Item> registry = event.getRegistry();

            for (final Item item : items)
            {
                registry.register(item);
                ITEMS.add(item);
            }
        }
    }
}