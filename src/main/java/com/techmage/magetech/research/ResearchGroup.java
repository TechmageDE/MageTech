package com.techmage.magetech.research;

import com.techmage.magetech.reference.Reference;

import java.util.ArrayList;

public class ResearchGroup
{
    private String name;
    private String ID;

    private ResearchCategory category;

    private ArrayList<Research> researches;

    ResearchGroup() { }

    public ResearchGroup(String name, String ID)
    {
        this.name = name;
        this.ID = ID;

        this.category = new ResearchCategory();

        this.researches = new ArrayList<>();
    }

    public ResearchGroup(String name, String ID, ResearchCategory category)
    {
        this(name, ID);

        this.category = category;
    }

    public String getName()
    {
        return this.name;
    }

    public String getID()
    {
        return this.ID;
    }

    public ResearchCategory getCategory()
    {
        return this.category;
    }

    public ArrayList<Research> getResearches()
    {
        return this.researches;
    }

    public void setResearches(ArrayList<Research> researches)
    {
        this.researches = researches;
    }

    public void addResearch(Research research)
    {
        this.researches.add(research);
    }

    @Override
    public String toString()
    {
        return String.format("%s:research.group.%s", Reference.MOD_ID, ID);
    }
}