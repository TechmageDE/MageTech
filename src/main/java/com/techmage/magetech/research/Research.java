package com.techmage.magetech.research;

import com.techmage.magetech.reference.Reference;

import java.util.ArrayList;

public class Research
{
    private String name;
    private String ID;

    private ResearchCategory category;
    private ResearchGroup group;

    private ArrayList<ArrayList<Research>> requirements;

    private ResearchInformation information;
    private ResearchTasking tasking;

    public Research(String name, String ID)
    {
        this.name = name;
        this.ID = ID;

        this.category = new ResearchCategory();
        this.group = new ResearchGroup();

        this.requirements = new ArrayList<>();

        this.information = new ResearchInformation();
        this.tasking = new ResearchTasking();
    }

    public Research(String name, String ID, ResearchCategory category, ResearchGroup group)
    {
        this(name, ID);

        this.category = category;
        this.group = group;
    }

    public Research(String name, String ID, ResearchCategory category, ResearchGroup group, ArrayList<ArrayList<Research>> requirements)
    {
        this(name, ID, category, group);

        this.requirements = requirements;
    }

    public Research(String name, String ID, ResearchCategory category, ResearchGroup group, ArrayList<ArrayList<Research>> requirements, ResearchInformation information, ResearchTasking tasking)
    {
        this(name, ID, category, group, requirements);

        this.information = information;
        this.tasking = tasking;
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

    public ResearchGroup getGroup()
    {
        return this.group;
    }

    public ArrayList<ArrayList<Research>> getRequirements()
    {
        return this.requirements;
    }

    public ResearchInformation getInformation()
    {
        return this.information;
    }

    public ResearchTasking getTasking()
    {
        return this.tasking;
    }

    public void setCategory(ResearchCategory category)
    {
        this.category = category;
    }

    public void setGroup(ResearchGroup group)
    {
        this.group = group;
    }

    public void setRequirements(ArrayList<ArrayList<Research>> requirements)
    {
        this.requirements = requirements;
    }

    public void addRequirement(ArrayList<Research> requirement)
    {
        this.requirements.add(requirement);
    }

    public void setInformation(ResearchInformation information)
    {
        this.information = information;
    }

    public void setTasking(ResearchTasking tasking)
    {
        this.tasking = tasking;
    }

    @Override
    public String toString()
    {
        return String.format("%s:research.%s", Reference.MOD_ID, ID);
    }
}