package com.techmage.magetech.electronics;

import com.techmage.magetech.MageTech;
import com.techmage.magetech.utility.LogHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LoaderSchematic
{
    private static final String DATA_SEGMENT_NAME = "#NAME";
    private static final String DATA_SEGMENT_ID = "#ID";
    private static final String DATA_SEGMENT_IO = "#IO";
    private static final String DATA_SEGMENT_COMPONENTS = "#COMPONENTS";
    private static final String DATA_SEGMENT_CONNECTIONS = "#CONNECTIONS";

    private final String MOD_ID;

    public LoaderSchematic(String MOD_ID)
    {
        this.MOD_ID = MOD_ID;
    }

    synchronized Schematic loadSchematic(String name)
    {
        BufferedReader readerSchematic;

        try
        {
            readerSchematic = new BufferedReader(new InputStreamReader(getClass().getClassLoader()
                    .getResourceAsStream("assets/" + MOD_ID.toLowerCase() + "/electronics/schematic/" + name), "UTF-8"));

            return readSchematic(readerSchematic);
        }

        catch (IOException e)
        {
            LogHelper.error(e);
        }

        return null;
    }

    private Schematic readSchematic(BufferedReader readerSchematic)
    {
        String name = "";

        String id = "";

        ArrayList<IOPin> ioPins = new ArrayList<>();
        ArrayList<Component> components = new ArrayList<>();

        for (String line : (Iterable<String>) readerSchematic.lines()::iterator)
        {
            switch (line)
            {
                case DATA_SEGMENT_NAME:       name = readName(readerSchematic);
                case DATA_SEGMENT_ID:         id = readID(readerSchematic);
                case DATA_SEGMENT_IO:         ioPins = readIO(readerSchematic);
                case DATA_SEGMENT_COMPONENTS: components = readComponents(readerSchematic, ioPins);
            }
        }

        return new Schematic(name, id, ioPins, components);
    }

    private String readName(BufferedReader readerSchematic)
    {
        String name = "";

        for (String dataLine : readDataBlock(readerSchematic, DATA_SEGMENT_NAME))
            name = dataLine;

        return name;
    }

    private String readID(BufferedReader readerSchematic)
    {
        String id = "";

        for (String dataLine : readDataBlock(readerSchematic, DATA_SEGMENT_ID))
            id = dataLine;

        return id;
    }

    private ArrayList<IOPin> readIO(BufferedReader readerSchematic)
    {
        ArrayList<IOPin> ioPins = new ArrayList<>();

        for (String dataLine : readDataBlock(readerSchematic, DATA_SEGMENT_IO))
        {
            String name = dataLine.substring(0, dataLine.indexOf(":="));

            EnumIOType ioType = EnumIOType.fromID(dataLine.substring(dataLine.indexOf(":=") + 2, dataLine.length()));

            ioPins.add(new IOPin(name, ioType));
        }

        return ioPins;
    }

    private ArrayList<Component> readComponents(BufferedReader readerSchematic, ArrayList<IOPin> ioPins)
    {
        ArrayList<Component> components = new ArrayList<>();

        for (String dataLine : readDataBlock(readerSchematic, DATA_SEGMENT_COMPONENTS))
        {
            String name = dataLine.substring(0, dataLine.indexOf(":="));

            ComponentType componentType = MageTech.instance.managerComponent.getComponentType(dataLine.substring(dataLine.indexOf(":=") + 2, dataLine.length()));

            components.add(new Component(name, componentType));
        }

        return readConnections(readerSchematic, components, ioPins);
    }

    private ArrayList<Component> readConnections(BufferedReader readerSchematic, ArrayList<Component> components, ArrayList<IOPin> ioPins)
    {
        ArrayList<Component> componentsNew = new ArrayList<>();

        Component lastComponent = null;

        ArrayList<Connection> connections = new ArrayList<>();

        ArrayList<String> dataLinesConnections = readDataBlock(readerSchematic, DATA_SEGMENT_CONNECTIONS);

        for (int index = 0; index < dataLinesConnections.size(); index ++)
        {
            String dataLine = dataLinesConnections.get(index);

            String componentSrcName = dataLine.substring(0, dataLine.indexOf('.'));
            String componentDstName = "";

            if (dataLine.lastIndexOf('.') > dataLine.indexOf('.'))
                componentDstName = dataLine.substring(dataLine.indexOf("->") + 2, dataLine.lastIndexOf('.'));

            Component componentSrc = null;
            Component componentDst = null;

            for (Component componentPCB : components)
            {
                if (componentSrcName.matches(componentPCB.getName()))
                    componentSrc = componentPCB;

                if (componentDstName.matches(componentPCB.getName()))
                    componentDst = componentPCB;
            }

            if (lastComponent == null)
                lastComponent = componentSrc;

            else if (!lastComponent.getName().matches(componentSrcName))
            {
                componentsNew.add(new Component(lastComponent.getName(), lastComponent.getComponentType(), new ArrayList<>(connections)));

                lastComponent = componentSrc;

                connections.clear();
            }

            if (componentDst == null)
            {
                String pinSrcName = dataLine.substring(dataLine.indexOf('.') + 1, dataLine.indexOf("->"));
                String pinDstName = dataLine.substring(dataLine.indexOf("->") + 2, dataLine.length());

                ComponentPin pinSrc = componentSrc.getComponentType().getPin(pinSrcName);
                final IOPin[] pinDst = new IOPin[1];

                ioPins.stream().filter(ioPin -> pinDstName.matches(ioPin.getName())).forEach(ioPin -> pinDst[0] = ioPin);

                pinSrc.setNameCombined(String.format("%s.%s", componentSrc.getName(), pinSrc.getName()));

                connections.add(new Connection(pinSrc, pinDst[0]));
            }

            else
            {
                String pinSrcName = dataLine.substring(dataLine.indexOf('.') + 1, dataLine.indexOf("->"));
                String pinDstName = dataLine.substring(dataLine.lastIndexOf('.') + 1, dataLine.length());

                ComponentPin pinSrc = componentSrc.getComponentType().getPin(pinSrcName);
                ComponentPin pinDst = componentDst.getComponentType().getPin(pinDstName);

                pinSrc.setNameCombined(String.format("%s.%s", componentSrc.getName(), pinSrc.getName()));
                pinDst.setNameCombined(String.format("%s.%s", componentDst.getName(), pinDst.getName()));

                connections.add(new Connection(pinSrc, pinDst));
            }

            if (index == dataLinesConnections.size() - 1)
                componentsNew.add(new Component(lastComponent.getName(), lastComponent.getComponentType(), connections));
        }

        return componentsNew;
    }


    private ArrayList<String> readDataBlock(BufferedReader readerSchematic, String dataSegment)
    {
        ArrayList<String> dataLines = new ArrayList<>();

        for (String line : (Iterable<String>) readerSchematic.lines()::iterator)
        {
            if (line.equals(String.format("%s_END", dataSegment)))
                return dataLines;

            else if (!line.equals("") && !line.startsWith("//") && !line.startsWith("#"))
                dataLines.add(line);
        }

        return dataLines;
    }
}