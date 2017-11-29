package com.techmage.magetech.research;

import com.techmage.magetech.reference.Reference;

import java.util.ArrayList;

public class ResearchCategory
{
    private String name;
    private String ID;

    private ArrayList<ResearchGroup> groups;

    ResearchCategory() { }

    public ResearchCategory(String name, String ID)
    {
        this.name = name;
        this.ID = ID;

        this.groups = new ArrayList<>();
    }

    public String getName()
    {
        return this.name;
    }

    public String getID()
    {
        return this.ID;
    }

    public ArrayList<ResearchGroup> getGroups()
    {
        return this.groups;
    }

    public void setGroups(ArrayList<ResearchGroup> groups)
    {
        this.groups = groups;
    }

    public void addGroup(ResearchGroup group)
    {
        this.groups.add(group);
    }

    @Override
    public String toString()
    {
        return String.format("%s:research.category.%s", Reference.MOD_ID, ID);
    }
}