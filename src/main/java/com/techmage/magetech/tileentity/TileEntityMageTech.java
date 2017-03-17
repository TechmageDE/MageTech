package com.techmage.magetech.tileentity;

import com.techmage.magetech.reference.Names;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityMageTech extends TileEntity
{
    private String customName;

    protected EnumFacing facing;

    public TileEntityMageTech()
    {
        customName = "";

        facing = EnumFacing.NORTH;
    }

    @Nonnull
    public String getCustomName()
    {
        return customName;
    }

    public void setCustomName(String customName)
    {
        this.customName = customName;
    }

    @Nonnull
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

        if (nbtTagCompound.hasKey(Names.NBT.DIRECTION))
            facing = EnumFacing.getFront(nbtTagCompound.getByte(Names.NBT.DIRECTION));
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound)
    {
        super.writeToNBT(nbtTagCompound);

        if (hasCustomName())
            nbtTagCompound.setString(Names.NBT.CUSTOM_NAME, customName);

        nbtTagCompound.setByte(Names.NBT.DIRECTION, (byte) facing.ordinal());

        return nbtTagCompound;
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound tag = getTileData().copy();

        writeToNBT(tag);

        return new SPacketUpdateTileEntity(this.getPos(), this.getBlockMetadata(), tag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
    {
        NBTTagCompound tag = pkt.getNbtCompound();

        readFromNBT(tag);
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void handleUpdateTag(@Nullable NBTTagCompound tag)
    {
        readFromNBT(tag);
    }

    protected boolean hasCustomName()
    {
        return customName != null && customName.length() > 0;
    }
}