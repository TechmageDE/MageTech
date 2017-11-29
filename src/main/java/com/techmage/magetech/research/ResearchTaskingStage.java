package com.techmage.magetech.research;

import java.util.ArrayList;

public class ResearchTaskingStage
{
    private ArrayList<ResearchTask> tasks;

    public ResearchTaskingStage()
    {
        this.tasks = new ArrayList<>();
    }

    public ResearchTaskingStage(ArrayList<ResearchTask> tasks)
    {
        this.tasks = tasks;
    }

    public ArrayList<ResearchTask> getTasks()
    {
        return this.tasks;
    }

    public void setTasks(ArrayList<ResearchTask> tasks)
    {
        this.tasks = tasks;
    }

    public void addTask(ResearchTask task)
    {
        this.tasks.add(task);
    }
}