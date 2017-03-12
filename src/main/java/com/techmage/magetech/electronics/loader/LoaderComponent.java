package com.techmage.magetech.electronics.loader;

import com.techmage.magetech.electronics.component.ComponentPin;
import com.techmage.magetech.electronics.component.ComponentType;
import com.techmage.magetech.utility.LogHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LoaderComponent extends LoaderElectronics
{
    private static final String DATA_SEGMENT_NAME = "#NAME";
    private static final String DATA_SEGMENT_ID = "#ID";
    private static final String DATA_SEGMENT_PINS = "#PINS";

    public LoaderComponent(String MOD_ID)
    {
        super(MOD_ID);
    }

    public synchronized ComponentType loadComponent(String name)
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
            if (line.contains(DATA_SEGMENT_NAME))
                name = readDataLine(line);

            if (line.contains(DATA_SEGMENT_ID))
                id = readDataLine(line);

            if (line.contains(DATA_SEGMENT_PINS))
                pins = readPins(readerComponent);
        }

        return new ComponentType(name, id, pins);
    }

    private ArrayList<ComponentPin> readPins(BufferedReader readerComponent)
    {
        ArrayList<ComponentPin> pins = new ArrayList<>();

        for (String dataLine : readDataBlock(readerComponent, DATA_SEGMENT_PINS))
        {
            String name = substringStart(dataLine, ":=");

            byte cordX = Byte.parseByte(substringMid(dataLine, ":=", "."));
            byte cordY = Byte.parseByte(substringEnd(dataLine, ".", true));

            pins.add(new ComponentPin(name, cordX, cordY));
        }

        return pins;
    }
}