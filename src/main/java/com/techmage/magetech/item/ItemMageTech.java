package com.techmage.magetech.item;

import com.techmage.magetech.creativetab.CreativeTabsMageTech;
import com.techmage.magetech.reference.Reference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemMageTech extends Item
{
    public ItemMageTech(String name)
    {
        super();

        setUnlocalizedName(name);
        setRegistryName(name);

        this.setCreativeTab(CreativeTabsMageTech.MAGETECH);
    }

    @Override
    public String getUnlocalizedName()
    {
        return String.format("item.%s%s", Reference.MOD_ID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        return String.format("item.%s%s", Reference.MOD_ID.toLowerCase() + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    protected String getUnwrappedUnlocalizedName(String unlocalizedName)
    {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }
}