package com.techmage.magetech.init;

import com.techmage.magetech.item.crafting.MageTechRecipeWood;
import com.techmage.magetech.registry.RecipeRegistry;

public class MageTechRecipes
{
    public static void init()
    {
        initCraftingRecipes();
    }

    private static void initCraftingRecipes()
    {
        RecipeRegistry.registerCraftingRecipe(new MageTechRecipeWood(MageTechBlocks.TABLE, "sss", " p ", "sss", 's', "slabWood", 'p', "stickWood"));
        RecipeRegistry.registerCraftingRecipe(new MageTechRecipeWood(MageTechBlocks.SHELF_SCROLL, "sss", "p p", "sss", 's', "slabWood", 'p', "stickWood"));
    }
}