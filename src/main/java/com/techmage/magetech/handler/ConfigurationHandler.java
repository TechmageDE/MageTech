package com.techmage.magetech.handler;

import com.techmage.magetech.reference.Reference;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

public class ConfigurationHandler
{
    public static net.minecraftforge.common.config.Configuration configuration;

    public static void init(File configFile)
    {
        if (configuration == null)
        {
            configuration = new net.minecraftforge.common.config.Configuration(configFile);
            saveConfiguration();
        }

        configuration.load();

        saveConfiguration();
    }

    private static void saveConfiguration()
    {
        if (configuration.hasChanged())
            configuration.save();
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equalsIgnoreCase(Reference.MOD_ID))
            saveConfiguration();
    }
}