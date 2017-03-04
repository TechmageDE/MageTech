package com.techmage.magetech.block.unlistedproperties;

import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedPropertyString implements IUnlistedProperty<String>
{
    private final String name;

    public UnlistedPropertyString(String name)
    {
        this.name = name;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public boolean isValid(String txt)
    {
        return true;
    }

    @Override
    public Class<String> getType()
    {
        return String.class;
    }

    @Override
    public String valueToString(String txt)
    {
        return txt;
    }
}