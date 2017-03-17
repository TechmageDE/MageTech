package com.techmage.magetech.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BlockStateReader
{
    private final String MOD_ID;

    public BlockStateReader(String MOD_ID)
    {
        this.MOD_ID = MOD_ID;
    }

    public ArrayList<String[]> getModelsForStates(String name)
    {
        ArrayList<String[]> modelsForStates = new ArrayList<>();

        BufferedReader readerBlockState;

        try
        {
            readerBlockState = new BufferedReader(new InputStreamReader(getClass().getClassLoader()
                    .getResourceAsStream("assets/" + MOD_ID.toLowerCase() + "/blockstates/" + name + ".json"), "UTF-8"));

            for (String line : (Iterable<String>) readerBlockState.lines()::iterator)
            {
                if (line.contains("model"))
                {
                    String[] dataSegments = line.split(":");

                    String blockState = dataSegments[0].substring(dataSegments[0].indexOf('"') + 1, dataSegments[0].lastIndexOf('"'));
                    String model = dataSegments[3].substring(0, dataSegments[3].indexOf('"'));

                    modelsForStates.add(new String[] {blockState, model});
                }
            }
        }

        catch (IOException e)
        {
            LogHelper.error(e);
        }

        return modelsForStates;
    }
}