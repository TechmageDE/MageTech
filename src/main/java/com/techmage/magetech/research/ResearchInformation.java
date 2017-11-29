package com.techmage.magetech.research;

import com.techmage.magetech.reference.Reference;

public class ResearchInformation
{
    private Research research;

    ResearchInformation() { }

    public ResearchInformation(Research research)
    {
        this.research = research;
    }

    public Research getResearch()
    {
        return this.research;
    }

    @Override
    public String toString()
    {
        return String.format("%s:research.information.%s", Reference.MOD_ID, research.getID());
    }
}