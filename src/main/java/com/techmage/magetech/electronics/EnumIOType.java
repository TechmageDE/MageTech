package com.techmage.magetech.electronics;

public enum EnumIOType
{
    INPUT("input"),
    OUTPUT("output"),
    GND("ground");

    private final String id;

    EnumIOType(String id)
    {
        this.id = id;
    }

    public String getID()
    {
        return this.id;
    }

    public static EnumIOType fromID(String id)
    {
        for (EnumIOType ioType : EnumIOType.values())
        {
            if (id.matches(ioType.getID()))
                return ioType;
        }

        return INPUT;
    }
}