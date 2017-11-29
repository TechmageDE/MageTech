package com.techmage.magetech.creativetab;

import com.techmage.magetech.reference.Reference;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabsMageTech
{
    public static final CreativeTabs MAGETECH = new CreativeTabs(Reference.MOD_ID.toLowerCase())
    {
        @SideOnly(Side.CLIENT)
        public ItemStack getTabIconItem()
        {
            return new ItemStack(Items.REDSTONE);
        }
    };
}