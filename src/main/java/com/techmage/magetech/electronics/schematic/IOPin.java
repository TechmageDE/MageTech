package com.techmage.magetech.electronics.schematic;

public class IOPin
{
    private String name;

    private EnumIOType ioType;

    public IOPin(String name, EnumIOType ioType)
    {
        this.name = name;

        this.ioType = ioType;
    }

    public String getName()
    {
        return this.name;
    }

    public EnumIOType getIoType()
    {
        return this.ioType;
    }

    @Override
    public String toString()
    {
        return String.format("magetech.electronics.io.%s:%s", ioType.toString().toLowerCase(), name);
    }
}
