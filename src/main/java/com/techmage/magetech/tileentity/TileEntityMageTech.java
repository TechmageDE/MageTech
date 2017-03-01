package com.techmage.magetech.tileentity;

import com.techmage.magetech.reference.Names;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

public class TileEntityMageTech extends TileEntity
{
    protected String customName;
    protected byte state;
    protected EnumFacing facing;

    public TileEntityMageTech()
    {
        customName = "";
        state = 0;
        facing = EnumFacing.SOUTH;
    }

    public String getCustomName()
    {
        return customName;
    }

    public void setCustomName(String customName)
    {
        this.customName = customName;
    }

    public byte getState()
    {
        return state;
    }

    public void setState(byte state)
    {
        this.state = state;
    }

    public EnumFacing getOrientation()
    {
        return facing;
    }

    public void setOrientation(EnumFacing orientation)
    {
        facing = orientation;
    }

    public void setOrientation(int orientation)
    {
        facing = EnumFacing.getFront(orientation);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        super.readFromNBT(nbtTagCompound);

        if (nbtTagCompound.hasKey(Names.NBT.CUSTOM_NAME))
            customName = nbtTagCompound.getString(Names.NBT.CUSTOM_NAME);

        if (nbtTagCompound.hasKey(Names.NBT.STATE))
            state = nbtTagCompound.getByte(Names.NBT.STATE);

        if (nbtTagCompound.hasKey(Names.NBT.DIRECTION))
            facing = EnumFacing.getFront(nbtTagCompound.getByte(Names.NBT.DIRECTION));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);

        if (hasCustomName())
            nbtTagCompound.setString(Names.NBT.CUSTOM_NAME, customName);

        nbtTagCompound.setByte(Names.NBT.STATE, state);
        nbtTagCompound.setByte(Names.NBT.DIRECTION, (byte) facing.ordinal());

        return nbtTagCompound;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound tagCompound = new NBTTagCompound();

        writeToNBT(tagCompound);

        return new SPacketUpdateTileEntity(pos, 1, tagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        NBTTagCompound tag = pkt.getNbtCompound();

        readFromNBT(tag);
    }

    public boolean hasCustomName()
    {
        return customName != null && customName.length() > 0;
    }
}