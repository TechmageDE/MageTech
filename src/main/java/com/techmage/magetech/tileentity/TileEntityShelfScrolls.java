package com.techmage.magetech.tileentity;

public class TileEntityShelfScrolls extends TileEntityWooden
{
    private String[] researchesStored;

    public TileEntityShelfScrolls()
    {
        super();
    }

    public TileEntityShelfScrolls(int slots)
    {
        this();

        researchesStored = new String[slots];
    }
}