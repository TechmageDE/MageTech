package com.techmage.magetech.electronics.loader;

import java.io.BufferedReader;
import java.util.ArrayList;

class LoaderElectronics
{
    final String MOD_ID;

    LoaderElectronics(String MOD_ID)
    {
        this.MOD_ID = MOD_ID;
    }

    String readDataLine(String dataLine)
    {
        return substringEnd(dataLine, " ", false);
    }

    ArrayList<String> readDataBlock(BufferedReader readerComponent, String dataSegment)
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

    String substringStart(String dataLine, String op)
    {
        return dataLine.substring(0, dataLine.indexOf(op));
    }

    String substringEnd(String dataLine, String op, boolean mode)
    {
        if (mode)
            return dataLine.substring(dataLine.lastIndexOf(op) + op.length(), dataLine.length());

        else
            return dataLine.substring(dataLine.indexOf(op) + op.length(), dataLine.length());
    }

    String substringMid(String dataLine, String op1, String op2)
    {
        return dataLine.substring(dataLine.indexOf(op1) + op1.length(), dataLine.indexOf(op2, dataLine.indexOf(op1)));
    }
}