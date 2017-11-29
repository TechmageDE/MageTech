package com.techmage.magetech.research;

public class ResearchTask
{
    private boolean state;

    public ResearchTask()
    {
        this.state = false;
    }

    public boolean getState()
    {
        return this.state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }
}
