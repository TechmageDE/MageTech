package com.techmage.magetech.proxy;

import com.techmage.magetech.client.model.ModelManager;
import com.techmage.magetech.utility.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClientProxy extends CommonProxy
{
    @Override
    public ClientProxy getClientProxy()
    {
        return this;
    }

    @Override
    public EntityPlayer getPlayerEntity(MessageContext ctx)
    {
        return (ctx.side.isClient() ? Minecraft.getMinecraft().player : super.getPlayerEntity(ctx));
    }

    @Override
    public void preInit()
    {
        ModelManager.INSTANCE.registerItemModels();
        ModelManager.INSTANCE.registerBlockModels();

        registerEventHandlers();

        LogHelper.info("ClientProxy: Pre Initialization Complete!");
    }

    @Override
    public void init()
    {
        bindTileEntityRenderer();

        LogHelper.info("ClientProxy: Initialization Complete!");
    }

    @Override
    public void postInit()
    {
        LogHelper.info("ClientProxy: Post Initialization Complete!");
    }

    @Override
    public void bindTileEntityRenderer()
    {

    }

    @Override
    public void registerEventHandlers()
    {

    }
}