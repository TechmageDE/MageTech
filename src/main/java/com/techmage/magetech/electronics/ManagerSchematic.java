package com.techmage.magetech.electronics;

import com.techmage.magetech.utility.LogHelper;
import scala.collection.Iterator;
import scala.reflect.io.Directory;
import scala.reflect.io.Path;

import java.io.File;
import java.util.ArrayList;

public class ManagerSchematic
{
    private final String MOD_ID;

    private LoaderSchematic loaderSchematic;

    public ArrayList<Schematic> loadedSchematics;

    public ManagerSchematic(String MOD_ID)
    {
        this.MOD_ID = MOD_ID;

        this.loaderSchematic = new LoaderSchematic(MOD_ID);

        this.loadedSchematics = loadAllSchematics();
    }

    public ArrayList<Schematic> loadAllSchematics()
    {
        LogHelper.info("---------------------------------------------------------------------------------");
        LogHelper.info("                              LOADING SCHEMATICS:                                ");
        LogHelper.info("---------------------------------------------------------------------------------");

        ArrayList<Schematic> schematicsLoaded = new ArrayList<>();

        String path = getClass().getClassLoader().getResource("assets/" + MOD_ID.toLowerCase() + "/electronics/schematic/").getPath();

        Iterator<Path> dir = new Directory(new File(path)).list();

        while (dir.hasNext())
            schematicsLoaded.add(loaderSchematic.loadSchematic(dir.next().name()));

        for (Schematic schematic : schematicsLoaded)
        {
            LogHelper.info("LOAD SCHEMATIC: " + schematic.getName());
            LogHelper.info("  - IO:");

            for (IOPin ioPin : schematic.getIOPins())
                LogHelper.info(String.format("    - %s : %s", ioPin.getName(), ioPin.getIoType()));

            LogHelper.info("  - COMPONENTS:" );

            for (Component component : schematic.getComponents())
            {
                LogHelper.info(String.format("    - %s : %s", component.getName(), component.getComponentType().getName()));
                LogHelper.info("      - CONNECTIONS");

                for (Connection connection : component.getConnections())
                {
                    if (connection.getPinDst() != null)
                        LogHelper.info(String.format("        - %s -> %s", connection.getPinSrc().getNameCombined(), connection.getPinDst().getNameCombined()));

                    else
                        LogHelper.info(String.format("        - %s -> %s", connection.getPinSrc().getNameCombined(), connection.getPinDstIO().getName()));
                }
            }

            LogHelper.info("-----------------------");
        }

        return schematicsLoaded;
    }
}