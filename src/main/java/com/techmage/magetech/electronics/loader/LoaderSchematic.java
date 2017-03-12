package com.techmage.magetech.electronics.loader;

import com.techmage.magetech.MageTech;
import com.techmage.magetech.electronics.component.Component;
import com.techmage.magetech.electronics.component.ComponentPin;
import com.techmage.magetech.electronics.component.ComponentType;
import com.techmage.magetech.electronics.component.Connection;
import com.techmage.magetech.electronics.schematic.EnumIOType;
import com.techmage.magetech.electronics.schematic.IOPin;
import com.techmage.magetech.electronics.schematic.Schematic;
import com.techmage.magetech.utility.LogHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LoaderSchematic extends LoaderElectronics
{
    private static final String DATA_SEGMENT_NAME = "#NAME";
    private static final String DATA_SEGMENT_ID = "#ID";
    private static final String DATA_SEGMENT_IO = "#IO";
    private static final String DATA_SEGMENT_COMPONENTS = "#COMPONENTS";
    private static final String DATA_SEGMENT_CONNECTIONS = "#CONNECTIONS";

    public LoaderSchematic(String MOD_ID)
    {
        super(MOD_ID);
    }

    public synchronized Schematic loadSchematic(String name)
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
            if (line.contains(DATA_SEGMENT_NAME))
                name = readDataLine(line);

            if (line.contains(DATA_SEGMENT_ID))
                id = readDataLine(line);

            if (line.contains(DATA_SEGMENT_IO))
                ioPins = readIO(readerSchematic);

            if (line.contains(DATA_SEGMENT_COMPONENTS))
                components = readComponents(readerSchematic, ioPins);
        }

        return new Schematic(name, id, ioPins, components);
    }

    private ArrayList<IOPin> readIO(BufferedReader readerSchematic)
    {
        ArrayList<IOPin> ioPins = new ArrayList<>();

        for (String dataLine : readDataBlock(readerSchematic, DATA_SEGMENT_IO))
        {
            String name = substringStart(dataLine, ":=");

            EnumIOType ioType = EnumIOType.fromID(substringEnd(dataLine, ":=", true));

            ioPins.add(new IOPin(name, ioType));
        }

        return ioPins;
    }

    private ArrayList<Component> readComponents(BufferedReader readerSchematic, ArrayList<IOPin> ioPins)
    {
        ArrayList<Component> components = new ArrayList<>();

        for (String dataLine : readDataBlock(readerSchematic, DATA_SEGMENT_COMPONENTS))
        {
            String name = substringStart(dataLine, ":=");

            ComponentType componentType = MageTech.instance.managerComponent.getComponentType(substringEnd(dataLine, ":=", true));

            components.add(new Component(name, componentType));
        }

        return readConnections(readerSchematic, components, ioPins);
    }

    private ArrayList<Component> readConnections(BufferedReader readerSchematic, ArrayList<Component> components, ArrayList<IOPin> ioPins)
    {
        ArrayList<Component> componentsNew = new ArrayList<>();

        ArrayList<String> dataLinesConnections = readDataBlock(readerSchematic, DATA_SEGMENT_CONNECTIONS);

        String componentScrName = null;
        ArrayList<String> dataLinesConnectionsComponent = new ArrayList<>();

        for (int index = 0; index < dataLinesConnections.size(); index ++)
        {
            String dataLine = dataLinesConnections.get(index);

            if (componentScrName == null)
                componentScrName = substringStart(dataLine, ".");

            if (substringStart(dataLine, ".").matches(componentScrName))
                dataLinesConnectionsComponent.add(dataLine);

            else
            {
                Component componentSrc = getComponentFromName(componentScrName, components);

                componentsNew.add(new Component(componentSrc.getName(), componentSrc.getComponentType(), readConnectionsPart(dataLinesConnectionsComponent, components, ioPins)));

                componentScrName = null;
                dataLinesConnectionsComponent.clear();
            }
        }

        return componentsNew;
    }

    private ArrayList<Connection> readConnectionsPart(ArrayList<String> dataLines, ArrayList<Component> components, ArrayList<IOPin> ioPins)
    {
        ArrayList<Connection> connections = new ArrayList<>();

        for (int index = 0; index < dataLines.size(); index ++)
        {
            String dataLine = dataLines.get(index);

            String componentSrcName = substringStart(dataLine, ".");
            String componentDstName = "";

            if (dataLine.lastIndexOf('.') > dataLine.indexOf('.'))
                componentDstName = substringMid(dataLine, "->", ".");

            Component componentSrc = getComponentFromName(componentSrcName, components);
            Component componentDst = getComponentFromName(componentDstName, components);

            String pinSrcName = substringMid(dataLine, ".", "->");
            String pinDstName = componentDst == null ? substringEnd(dataLine, "->", true) : substringEnd(dataLine, ".", true);

            ComponentPin pinSrc = componentSrc.getComponentType().getPin(pinSrcName);

            if (componentDst == null)
            {
                IOPin pinDst = getIOPinFromName(pinDstName, ioPins);

                pinSrc.setNameCombined(String.format("%s.%s", componentSrc.getName(), pinSrc.getName()));

                connections.add(new Connection(pinSrc, pinDst));
            }

            else
            {
                ComponentPin pinDst = componentDst.getComponentType().getPin(pinDstName);

                pinSrc.setNameCombined(String.format("%s.%s", componentSrc.getName(), pinSrc.getName()));
                pinDst.setNameCombined(String.format("%s.%s", componentDst.getName(), pinDst.getName()));

                connections.add(new Connection(pinSrc, pinDst));
            }
        }

        return connections;
    }

    private Component getComponentFromName(String name, ArrayList<Component> components)
    {
        for (Component component : components)
        {
            if (name.matches(component.getName()))
                return component;
        }

        return null;
    }

    private IOPin getIOPinFromName(String name, ArrayList<IOPin> ioPins)
    {
        final IOPin[] pinDst = new IOPin[1];

        ioPins.stream().filter(ioPin -> name.matches(ioPin.getName())).forEach(ioPin -> pinDst[0] = ioPin);

        return pinDst[0];
    }
}