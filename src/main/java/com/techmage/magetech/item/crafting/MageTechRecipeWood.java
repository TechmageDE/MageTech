package com.techmage.magetech.item.crafting;

import com.techmage.magetech.block.BlockWooden;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MageTechRecipeWood extends ShapedOreRecipe
{
    List<ItemStack> woodList = new ArrayList<>();
    ItemStack woodCraftedWith = ItemStack.EMPTY;

    public MageTechRecipeWood(Block result, Object... recipe)
    {
        this(new ItemStack(result), recipe);
    }

    public MageTechRecipeWood(Item result, Object... recipe)
    {
        this(new ItemStack(result), recipe);
    }

    public MageTechRecipeWood(@Nonnull ItemStack result, Object... recipe)
    {
        super(result, recipe);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean checkMatch(InventoryCrafting inv, int startX, int startY, boolean mirror)
    {
        for (int x = 0; x < MAX_CRAFT_GRID_WIDTH; x ++)
        {
            for (int y = 0; y < MAX_CRAFT_GRID_HEIGHT; y ++)
            {
                int subX = x - startX;
                int subY = y - startY;
                Object target = null;

                if (subX >= 0 && subY >= 0 && subX < width && subY < height)
                {
                    if (mirror)
                        target = input[width - subX - 1 + subY * width];

                    else
                        target = input[subX + subY * width];
                }

                ItemStack slot = inv.getStackInRowAndColumn(x, y);

                if (target instanceof ItemStack)
                {
                    if (!OreDictionary.itemMatches((ItemStack)target, slot, false))
                        return false;
                }

                else if (target instanceof List)
                {
                    boolean matched = false;

                    Iterator<ItemStack> itr = ((List<ItemStack>)target).iterator();
                    while (itr.hasNext() && !matched)
                        matched = OreDictionary.itemMatches(itr.next(), slot, false);

                    if (matched)
                    {
                        String oreName = OreDictionary.getOreName(OreDictionary.getOreIDs(slot)[0]);

                        if (oreName.matches("slabWood"))
                            woodList.add(slot);
                    }

                    if (!matched)
                        return false;
                }

                else if (target == null && !slot.isEmpty())
                    return false;
            }
        }

        if (!woodList.isEmpty())
        {
            ItemStack woodType = woodList.get(0);

            for (ItemStack wood : woodList)
            {
                if (!wood.isItemEqual(woodType))
                {
                    woodList.clear();

                    return false;
                }
            }

            woodCraftedWith = woodType;
            woodList.clear();
        }

        return true;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting var1)
    {
        BlockWooden block = (BlockWooden) Block.getBlockFromItem(output.getItem());

        return BlockWooden.createItemStack(block, output.getItemDamage(), Block.getBlockFromItem(woodCraftedWith.getItem()), woodCraftedWith.getItemDamage());
    }

    @Override
    public ItemStack getRecipeOutput()
    {
        if (woodCraftedWith != ItemStack.EMPTY && output != null)
        {
            BlockWooden block = (BlockWooden) Block.getBlockFromItem(output.getItem());
            int meta = woodCraftedWith.getItemDamage();

            if (meta == OreDictionary.WILDCARD_VALUE)
                meta = 0;

            return BlockWooden.createItemStack(block, output.getItemDamage(), Block.getBlockFromItem(woodCraftedWith.getItem()), meta);
        }

        return super.getRecipeOutput();
    }
}