package com.techmage.magetech.block;

import com.techmage.magetech.creativetab.CreativeTabsMageTech;
import com.techmage.magetech.reference.Textures;
import com.techmage.magetech.tileentity.TileEntityMageTech;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

class BlockMageTech extends Block
{
    public BlockMageTech(String name)
    {
        this(Material.GROUND, name);
    }

    BlockMageTech(Material material, String name)
    {
        super(material);

        this.setUnlocalizedName(name);
        this.setRegistryName(name);

        setCreativeTab(CreativeTabsMageTech.MAGETECH);
    }

    @Override
    @Nonnull
    public String getUnlocalizedName()
    {
        return String.format("tile.%s%s", Textures.RESOURCE_PREFIX, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    private String getUnwrappedUnlocalizedName(String unlocalizedName)
    {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }

    @Override
    public void breakBlock(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state)
    {
        dropInventory(worldIn, pos);

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        if (worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileEntityMageTech)
        {
            int direction = 0;
            int facing = MathHelper.floor(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;

            if (facing == 0)
                direction = EnumFacing.NORTH.ordinal();

            else if (facing == 1)
                direction = EnumFacing.EAST.ordinal();

            else if (facing == 2)
                direction = EnumFacing.SOUTH.ordinal();

            else if (facing == 3)
                direction = EnumFacing.WEST.ordinal();

            TileEntityMageTech tileMageTech = (TileEntityMageTech) worldIn.getTileEntity(pos);

            if (tileMageTech != null)
            {
                tileMageTech.setOrientation(direction);

                if (stack.hasDisplayName())
                    tileMageTech.setCustomName(stack.getDisplayName());
            }
        }
    }

    private void dropInventory(World worldIn, BlockPos pos)
    {
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (!(tileEntity instanceof IInventory))
            return;

        IInventory inventory = (IInventory) tileEntity;

        for (int i = 0; i < inventory.getSizeInventory(); i++)
        {
            ItemStack itemStack = inventory.getStackInSlot(i);

            if (!itemStack.isEmpty())
            {
                Random rand = new Random();

                float dX = rand.nextFloat() * 0.8F + 0.1F;
                float dY = rand.nextFloat() * 0.8F + 0.1F;
                float dZ = rand.nextFloat() * 0.8F + 0.1F;

                EntityItem entityItem = new EntityItem(worldIn, pos.getX() + dX, pos.getY() + dY, pos.getZ() + dZ, itemStack.copy());

                if (itemStack.hasTagCompound())
                {
                    entityItem.getEntityItem().setTagCompound(itemStack.getTagCompound().copy());
                }

                float factor = 0.05F;

                entityItem.motionX = rand.nextGaussian() * factor;
                entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                entityItem.motionZ = rand.nextGaussian() * factor;

                worldIn.spawnEntity(entityItem);

                itemStack.setCount(0);
            }
        }
    }
}