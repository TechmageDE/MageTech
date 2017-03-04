package com.techmage.magetech.block.unlistedproperties;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedPropertyItemStack implements IUnlistedProperty<ItemStack>
{
    private final String name;

    public UnlistedPropertyItemStack(String name)
    {
        this.name = name;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public boolean isValid(ItemStack stack)
    {
        return true;
    }

    @Override
    public Class<ItemStack> getType()
    {
        return ItemStack.class;
    }

    @Override
    public String valueToString(ItemStack stack)
    {
        return stack.toString();
    }
}