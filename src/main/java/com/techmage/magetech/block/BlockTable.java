package com.techmage.magetech.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockTable extends BlockWooden
{
    private final static AxisAlignedBB BOUNDS_SINGLE = new AxisAlignedBB(0.05F, 0F, 0.05F, 0.95F, 0.95F, 0.95F);

    public BlockTable(String name)
    {
        super(name);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return BOUNDS_SINGLE;
    }
}