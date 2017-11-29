package com.techmage.magetech;

import com.techmage.magetech.handler.GuiHandler;
import com.techmage.magetech.proxy.CommonProxy;
import com.techmage.magetech.proxy.IProxy;
import com.techmage.magetech.reference.Reference;
import com.techmage.magetech.utility.LogHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME,  version = Reference.VERSION)
public class MageTech
{
    @Mod.Instance(Reference.MOD_ID)
    public static MageTech instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static IProxy sidedProxy;
    private static CommonProxy commonProxy = new CommonProxy();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        commonProxy.preInit();
        sidedProxy.preInit();

        LogHelper.info("Pre Initialization Complete!");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

        commonProxy.init();
        sidedProxy.init();

        commonProxy.registerEventHandlers();
        sidedProxy.registerEventHandlers();

        LogHelper.info("Initialization Complete!");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        commonProxy.postInit();
        sidedProxy.postInit();

        LogHelper.info("Post Initialization Complete!");
    }
}