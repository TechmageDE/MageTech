package com.techmage.magetech.client.gui;

import com.techmage.magetech.handler.ConfigurationHandler;
import com.techmage.magetech.reference.Reference;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;

public class MageTechGuiConfig extends GuiConfig
{
    public MageTechGuiConfig(GuiScreen guiScreen)
    {
        super(guiScreen, new ConfigElement(ConfigurationHandler.configuration.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), Reference.MOD_ID, false, false,
                GuiConfig.getAbridgedConfigPath(ConfigurationHandler.configuration.toString()));
    }
}