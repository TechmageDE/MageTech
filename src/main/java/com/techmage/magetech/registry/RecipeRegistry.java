package com.techmage.magetech.registry;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecipeRegistry
{
    private static ArrayList<IRecipe> craftingList = new ArrayList<IRecipe>();
    private static Map<ItemStack, ItemStack> smeltingList = new HashMap<ItemStack, ItemStack>();

    public static ArrayList<IRecipe> getCraftingList()
    {
        return craftingList;
    }

    public static Map<ItemStack, ItemStack> getSmeltingList()
    {
        return smeltingList;
    }

    public static void registerCraftingRecipe(IRecipe recipe)
    {
        GameRegistry.addRecipe(recipe);

        craftingList.add(recipe);
    }

    public static void registerFurnaceRecipe(ItemStack input, ItemStack output, float experience)
    {
        FurnaceRecipes.instance().addSmeltingRecipe(input, output, experience);

        smeltingList.put(input, output);
    }
}