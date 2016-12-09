package com.techmage.magetech.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public interface IProxy
{
    ClientProxy getClientProxy();

    EntityPlayer getPlayerEntity(MessageContext ctx);

    void preInit();
    void init();
    void postInit();

    void registerEventHandlers();
}