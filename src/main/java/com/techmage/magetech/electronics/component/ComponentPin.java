package com.techmage.magetech.electronics.component;

public class ComponentPin
{
    private String name;

    private byte cordX;
    private byte cordY;

    private String nameCombined;

    public ComponentPin(String name, byte cordX, byte cordY)
    {
        this.name = name;

        this.cordX = cordX;
        this.cordY = cordY;
    }

    public String getName()
    {
        return this.name;
    }

    public byte getCordX()
    {
        return cordX;
    }

    public byte getCordY()
    {
        return cordY;
    }

    public String getNameCombined()
    {
        return nameCombined;
    }

    public void setNameCombined(String nameCombined)
    {
        this.nameCombined = nameCombined;
    }

    @Override
    public String toString()
    {
        if (nameCombined == null)
            return String.format("magetech.electronics.component.pin:%s", this.name);

        else
            return String.format("magetech.electronics.component.pin:%s", this.nameCombined);
    }
}