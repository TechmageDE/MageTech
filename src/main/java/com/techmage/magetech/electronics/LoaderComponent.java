package com.techmage.magetech.electronics;

import com.techmage.magetech.utility.LogHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LoaderComponent
{
    private static final String DATA_SEGMENT_NAME = "#NAME";
    private static final String DATA_SEGMENT_ID = "#ID";
    private static final String DATA_SEGMENT_PINS = "#PINS";

    private final String MOD_ID;

    public LoaderComponent(String MOD_ID)
    {
        this.MOD_ID = MOD_ID;
    }

    synchronized ComponentType loadComponent(String name)
    {
        BufferedReader readerComponent;

        try
        {
            readerComponent = new BufferedReader(new InputStreamReader(getClass().getClassLoader()
                    .getResourceAsStream("assets/" + MOD_ID.toLowerCase() + "/electronics/component/" + name), "UTF-8"));

            return readComponent(readerComponent);
        }

        catch (IOException e)
        {
            LogHelper.error(e);
        }

        return null;
    }

    private ComponentType readComponent(BufferedReader readerComponent)
    {
        String name = "";

        String id = "";

        ArrayList<ComponentPin> pins = new ArrayList<>();

        for (String line : (Iterable<String>) readerComponent.lines()::iterator)
        {
            switch (line)
            {
                case DATA_SEGMENT_NAME: name = readName(readerComponent);
                case DATA_SEGMENT_ID: id     = readID(readerComponent);
                case DATA_SEGMENT_PINS: pins = readPins(readerComponent);
            }
        }

        return new ComponentType(name, id, pins);
    }

    private String readName(BufferedReader readerComponent)
    {
        String name = "";

        for (String dataLine : readDataBlock(readerComponent, DATA_SEGMENT_NAME))
            name = dataLine;

        return name;
    }

    private String readID(BufferedReader readerComponent)
    {
        String id = "";

        for (String dataLine : readDataBlock(readerComponent, DATA_SEGMENT_ID))
            id = dataLine;

        return id;
    }

    private ArrayList<ComponentPin> readPins(BufferedReader readerComponent)
    {
        ArrayList<ComponentPin> pins = new ArrayList<>();

        for (String dataLine : readDataBlock(readerComponent, DATA_SEGMENT_PINS))
        {
            String name = dataLine.substring(0, dataLine.indexOf(":="));

            byte cordX = Byte.parseByte(dataLine.substring(dataLine.indexOf(":=") + 2, dataLine.indexOf('.')));
            byte cordY = Byte.parseByte(dataLine.substring(dataLine.indexOf('.') + 1, dataLine.length()));

            pins.add(new ComponentPin(name, cordX, cordY));
        }

        return pins;
    }

    private ArrayList<String> readDataBlock(BufferedReader readerComponent, String dataSegment)
    {
        ArrayList<String> dataLines = new ArrayList<>();

        for (String line : (Iterable<String>) readerComponent.lines()::iterator)
        {
            if (line.equals(String.format("%s_END", dataSegment)))
                return dataLines;

            else if (!line.equals("") && !line.startsWith("//") && !line.startsWith("#"))
                dataLines.add(line);
        }

        return dataLines;
    }
}