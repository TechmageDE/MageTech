package com.techmage.magetech.electronics;

import java.util.ArrayList;

public class ComponentType
{
    private String name;

    String id;

    private ArrayList<ComponentPin> pins;

    public ComponentType(String name, String id, ArrayList<ComponentPin> pins)
    {
        this.name = name;

        this.id = id;

        this.pins = pins;
    }

    public String getName()
    {
        return this.name;
    }

    public String getId()
    {
        return id;
    }

    public ArrayList<ComponentPin> getPins()
    {
        return this.pins;
    }

    public ComponentPin getPin(String name)
    {
        for (ComponentPin pin : pins)
        {
            if (name.matches(pin.getName()))
                return pin;
        }

        return null;
    }

    @Override
    public String toString()
    {
        return String.format("magetech.electronics.component.%s", name.toLowerCase());
    }
}