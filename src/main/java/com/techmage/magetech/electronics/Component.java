package com.techmage.magetech.electronics;

import java.util.ArrayList;

public class Component
{
    private String name;

    private ComponentType componentType;
    private ArrayList<Connection> connections;

    public Component(String name, ComponentType componentType, ArrayList<Connection> connections)
    {
        this.name = name;

        this.componentType = componentType;
        this.connections = connections;
    }

    public Component(String name, ComponentType componentType)
    {
        this(name, componentType, new ArrayList<>());
    }

    public String getName()
    {
        return this.name;
    }

    public ComponentType getComponentType()
    {
        return componentType;
    }

    public ArrayList<Connection> getConnections()
    {
        return connections;
    }

    public String getPinName(ComponentPin pin)
    {
        if (componentType.getPins().contains(pin))
            return String.format("%s.%s", name, pin.getName());

        return "INVALID";
    }

    @Override
    public String toString()
    {
        return String.format("magetech.electronics.component.%s", name);
    }
}