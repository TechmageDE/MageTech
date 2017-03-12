package com.techmage.magetech.electronics.schematic;

import com.techmage.magetech.electronics.component.Component;

import java.util.ArrayList;

public class Schematic
{
    private String name;

    private String id;

    private ArrayList<IOPin> ioPins;
    private ArrayList<Component> components;

    public Schematic(String name, String id, ArrayList<IOPin> ioPins, ArrayList<Component> components)
    {
        this.name = name;

        this.id = id;

        this.ioPins = ioPins;
        this.components = components;
    }

    public String getName()
    {
        return name;
    }

    public String getId()
    {
        return id;
    }

    public ArrayList<IOPin> getIOPins()
    {
        return ioPins;
    }

    public ArrayList<Component> getComponents()
    {
        return components;
    }

    @Override
    public String toString()
    {
        return String.format("magetech.electronics.schematic:%s", id);
    }
}