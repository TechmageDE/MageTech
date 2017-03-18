package com.techmage.magetech.block;

import com.techmage.magetech.init.MageTechItems;
import com.techmage.magetech.tileentity.TileEntityShelfScrolls;
import com.techmage.magetech.utility.LogHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.vecmath.Vector3f;

public class BlockShelfScroll extends BlockWooden
{
    public BlockShelfScroll(String name)
    {
        super(name);
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta)
    {
        return new TileEntityShelfScrolls(4);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        float pixel = 1.0F / 16.0F;

        Vector3f[][] slotHitBoxes = new Vector3f[4][2];

        slotHitBoxes[0][0] = new Vector3f(14 * pixel, 15 * pixel, 1.0F);
        slotHitBoxes[0][1] = new Vector3f( 5 * pixel,  8 * pixel, 0.0F);

        slotHitBoxes[1][0] = new Vector3f( 4 * pixel, 15 * pixel, 1.0F);
        slotHitBoxes[1][1] = new Vector3f( 0 * pixel,  8 * pixel, 0.0F);

        slotHitBoxes[2][0] = new Vector3f(16 * pixel,  7 * pixel, 1.0F);
        slotHitBoxes[2][1] = new Vector3f(12 * pixel,  1 * pixel, 0.0F);

        slotHitBoxes[3][0] = new Vector3f(11 * pixel,  7 * pixel, 1.0F);
        slotHitBoxes[3][1] = new Vector3f( 2 * pixel,  1 * pixel, 0.0F);

        if (!playerIn.isSneaking())
        {
            if (playerIn.getHeldItem(hand).getItem() == MageTechItems.RESEARCH_SCROLL)
            {
                for (int index = 0; index < slotHitBoxes.length; index ++)
                {
                    Vector3f[] cords = slotHitBoxes[index];

                    Vector3f cordBgn = cords[1];
                    Vector3f cordEnd = cords[0];

                    switch (facing)
                    {
                        case SOUTH:
                            cordBgn = new Vector3f(1.0F - cords[0].getX(), cords[1].getY(), 1.0F - cords[0].getZ());
                            cordEnd = new Vector3f(1.0F - cords[1].getX(), cords[0].getY(), 1.0F - cords[1].getZ());

                            break;

                        case EAST:
                            cordBgn = new Vector3f(cords[1].getZ(), cords[1].getY(), cords[1].getX());
                            cordEnd = new Vector3f(cords[0].getZ(), cords[0].getY(), cords[0].getX());

                            break;

                        case WEST:
                            cordBgn = new Vector3f(1.0F - cords[0].getZ(), cords[1].getY(), 1.0F - cords[0].getX());
                            cordEnd = new Vector3f(1.0F - cords[1].getZ(), cords[0].getY(), 1.0F - cords[1].getX());

                            break;
                    }

                    if ((hitX >= cordBgn.getX() && hitX <= cordEnd.getX()) && (hitY >= cordBgn.getY() && hitY <= cordEnd.getY()) && (hitZ >= cordBgn.getZ() && hitZ <= cordEnd.getZ()))
                    {
                        LogHelper.info("Slot clicked: " + index);
                    }
                }
            }
        }

        return false;
    }
}