package com.techmage.magetech.proxy;

import com.techmage.magetech.init.MageTechBlocks;
import com.techmage.magetech.init.MageTechItems;
import com.techmage.magetech.init.MageTechRecipes;
import com.techmage.magetech.init.MageTechTileEntities;
import com.techmage.magetech.utility.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CommonProxy implements IProxy
{
    @Override
    public ClientProxy getClientProxy()
    {
        return null;
    }

    @Override
    public EntityPlayer getPlayerEntity(MessageContext ctx)
    {
        return ctx.getServerHandler().playerEntity;
    }

    @Override
    public void preInit()
    {
        MageTechItems.preInit();
        MageTechBlocks.preInit();

        LogHelper.info("CommonProxy: Pre Initialization Complete!");
    }

    @Override
    public void init()
    {
        MageTechRecipes.init();
        MageTechTileEntities.init();

        LogHelper.info("CommonProxy: Initialization Complete!");
    }

    @Override
    public void postInit()
    {
        LogHelper.info("CommonProxy: Post Initialization Complete!");
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