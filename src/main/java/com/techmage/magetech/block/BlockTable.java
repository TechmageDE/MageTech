package com.techmage.magetech.block;

import com.techmage.magetech.tileentity.TileEntityWooden;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nonnull;

public class BlockTable extends BlockWooden implements ITileEntityProvider
{
    private static final PropertyBool DOUBLE = PropertyBool.create("double");

    public BlockTable(String name)
    {
        super(name);

        setDefaultState(blockState.getBaseState().withProperty(DOUBLE, false));
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta)
    {
        return new TileEntityWooden();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(DOUBLE, true));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        IProperty[] listedProperties = new IProperty[] { DOUBLE };
        IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[] { WOOD, FACING };

        return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(DOUBLE, (meta & 0b0001) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        byte meta = 0;

        if (state.getValue(DOUBLE))
            meta |= 0b0001;

        return meta;
    }
}