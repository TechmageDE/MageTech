package com.techmage.magetech.init;

import com.techmage.magetech.item.crafting.MageTechRecipeTable;
import com.techmage.magetech.registry.RecipeRegistry;

public class MageTechRecipes
{
    public static void init()
    {
        initCraftingRecipes();
    }

    private static void initCraftingRecipes()
    {
        RecipeRegistry.registerCraftingRecipe(new MageTechRecipeTable(MageTechBlocks.TABLE_OAK, "sss", " p ", "sss", 's', "slabWood", 'p', "stickWood"));
        //RecipeRegistry.registerCraftingRecipe(new MageTechRecipeTable(MageTechBlocks.SHELF_SCROLL, "sss", "p p", "sss", 's', "slabWood", 'p', "stickWood"));
    }
}