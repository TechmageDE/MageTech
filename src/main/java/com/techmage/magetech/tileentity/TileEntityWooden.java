package com.techmage.magetech.tileentity;

import com.techmage.magetech.block.BlockWooden;
import com.techmage.magetech.utility.ModelHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;

public class TileEntityWooden extends TileEntityMageTech
{
    public static final String TAG_WOOD = "wood";
    public static final String TAG_FACING = "facing";

    public TileEntityWooden()
    {
        super();
    }

    public IExtendedBlockState writeExtendedBlockState(IExtendedBlockState state)
    {
        String texture = getTileData().getString("texture");

        if (texture.isEmpty())
        {
            ItemStack stack = new ItemStack(getTileData().getCompoundTag(TAG_WOOD));

            if (stack.getItem() == Item.getItemFromBlock(Blocks.AIR))
                stack = new ItemStack(Blocks.WOODEN_SLAB, 1, 0);

            Block block = Block.getBlockFromItem(stack.getItem());
            texture = ModelHelper.getTextureFromBlock(block, stack.getItemDamage()).getIconName();

            getTileData().setString("texture", texture);
        }

        if (!texture.isEmpty())
            state = state.withProperty(BlockWooden.WOOD, texture);

        EnumFacing facing = getFacing();
        state = state.withProperty(BlockWooden.FACING, facing);

        return state;
    }

    @Override
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

        NBTBase feet = tag.getTag(TAG_WOOD);
        NBTBase facing = tag.getTag(TAG_FACING);

        getTileData().setTag(TAG_WOOD, feet);
        getTileData().setTag(TAG_FACING, facing);

        readFromNBT(tag);
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag)
    {
        readFromNBT(tag);
    }

    public void setFacing(EnumFacing face)
    {
        getTileData().setInteger(TAG_FACING, face.getIndex());
    }

    public EnumFacing getFacing()
    {
        return EnumFacing.getFront(getTileData().getInteger(TAG_FACING));
    }

    public void updateTextureBlock(NBTTagCompound tag)
    {
        getTileData().setTag(TAG_WOOD, tag);
    }

    public NBTTagCompound getTextureBlock()
    {
        return getTileData().getCompoundTag(TAG_WOOD);
    }
}