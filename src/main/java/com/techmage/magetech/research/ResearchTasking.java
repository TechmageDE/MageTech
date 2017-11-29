package com.techmage.magetech.research;

import com.techmage.magetech.reference.Reference;

import java.util.ArrayList;

public class ResearchTasking
{
    private Research research;

    private ArrayList<ResearchTaskingStage> stages;

    ResearchTasking() { }

    public ResearchTasking(Research research)
    {
        this.research = research;

        this.stages = new ArrayList<>();
    }

    public ResearchTasking(Research research, ArrayList<ResearchTaskingStage> stages)
    {
        this(research);

        this.stages = stages;
    }

    public Research getResearch()
    {
        return this.research;
    }

    public ArrayList<ResearchTaskingStage> getStages()
    {
        return this.stages;
    }

    public void setStages(ArrayList<ResearchTaskingStage> stages)
    {
        this.stages = stages;
    }

    public void addStage(ResearchTaskingStage stage)
    {
        this.stages.add(stage);
    }

    @Override
    public String toString()
    {
        return String.format("%s:research.tasking.%s", Reference.MOD_ID, research.getID());
    }
}