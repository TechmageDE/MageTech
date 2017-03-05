package com.techmage.magetech.utility;

import com.techmage.magetech.reference.Reference;
import net.minecraft.util.ResourceLocation;

public class ResourceHelper
{
    public static String resource(String res)
    {
        return String.format("%s:%s", Reference.MOD_ID.toLowerCase(), res);
    }

    public static ResourceLocation getResource(String res)
    {
        return new ResourceLocation(Reference.MOD_ID.toLowerCase(), res);
    }
}
