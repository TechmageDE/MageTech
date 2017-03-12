package com.techmage.magetech.electronics.component;

import com.techmage.magetech.electronics.schematic.IOPin;

public class Connection
{
    private ComponentPin pinSrc;
    private ComponentPin pinDst;

    private IOPin pinDstIO;

    public Connection(ComponentPin pinSrc, ComponentPin pinDst)
    {
        this.pinSrc = pinSrc;
        this.pinDst = pinDst;
    }

    public Connection(ComponentPin pinSrc, IOPin pinDstIO)
    {
        this.pinSrc = pinSrc;
        this.pinDstIO = pinDstIO;
    }

    public ComponentPin getPinSrc()
    {
        return pinSrc;
    }

    public ComponentPin getPinDst()
    {
        return pinDst;
    }

    public IOPin getPinDstIO()
    {
        return pinDstIO;
    }

    @Override
    public String toString()
    {
        if (pinDst != null)
            return String.format("magetech.electronics.component.connection:%s->%s", pinSrc.getName(), pinDst.getName());

        else
            return String.format("magetech.electronics.component.connection:%s->%s", pinSrc.getName(), pinDstIO.getName());
    }
}