package com.techmage.magetech.block;

import com.techmage.magetech.block.unlistedproperties.UnlistedPropertyBoolean;
import com.techmage.magetech.tileentity.TileEntityTable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nonnull;

public class BlockTable extends BlockWooden
{
    public static final UnlistedPropertyBoolean DOUBLE = new UnlistedPropertyBoolean("double");

    private static AxisAlignedBB BOUNDS_SINGLE = new AxisAlignedBB(0.05F, 0F, 0.05F, 0.95F, 0.95F, 0.95F);
    private static AxisAlignedBB BOUNDS_DOUBLE_NORTH = new AxisAlignedBB(0.05F, 0F, 0F, 0.95F, 0.95F, 0.95F);
    private static AxisAlignedBB BOUNDS_DOUBLE_EAST = new AxisAlignedBB(0.05F, 0F, 0.05F, 1F, 0.95F, 0.95F);
    private static AxisAlignedBB BOUNDS_DOUBLE_SOUTH = new AxisAlignedBB(0.05F, 0F, 0.05F, 0.95F, 0.95F, 1F);
    private static AxisAlignedBB BOUNDS_DOUBLE_WEST = new AxisAlignedBB(0F, 0F, 0.05F, 0.95F, 0.95F, 0.95F);

    public BlockTable(String name)
    {
        super(name);
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta)
    {
        return new TileEntityTable();
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        TileEntity tile = source.getTileEntity(pos);

        if (tile != null && tile instanceof TileEntityTable)
        {
            TileEntityTable tileTable = (TileEntityTable) tile;

            boolean isDouble = tileTable.getIsDouble();
            EnumFacing facing = tileTable.getFacing();

            if (isDouble)
            {
                switch (facing)
                {
                    case NORTH: return BOUNDS_DOUBLE_NORTH;
                    case EAST:  return BOUNDS_DOUBLE_EAST;
                    case SOUTH: return BOUNDS_DOUBLE_SOUTH;
                    case WEST:  return BOUNDS_DOUBLE_WEST;
                }
            }
        }

        return BOUNDS_SINGLE;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        IProperty[] listedProperties = new IProperty[] {  };
        IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[] { WOOD, FACING, DOUBLE };

        return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    /*
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        TileEntityTable tileTableThis;
        TileEntityTable tileTableOther;

        boolean isDoubleThis;
        EnumFacing facingThis;

        TileEntity tileThis = worldIn.getTileEntity(pos);

        tileTableThis = tileThis != null && tileThis instanceof TileEntityTable ? (TileEntityTable) tileThis : null;

        if (tileTableThis != null)
        {
            isDoubleThis = tileTableThis.getIsDouble();
            facingThis = tileTableThis.getFacing();

            if (isDoubleThis)
            {
                TileEntity tileOther = worldIn.getTileEntity(pos.offset(facingThis));

                tileTableOther = tileOther != null && tileOther instanceof TileEntityTable ? (TileEntityTable) tileOther : null;

                if (tileTableOther == null || tileTableOther.getFacing() != facingThis.getOpposite())
                {
                    tileTableThis.setFacing(facingThis.rotateYCCW());
                    tileTableThis.setIsDouble(false);
                }
            }

            else
            {
                for (EnumFacing facing : EnumFacing.HORIZONTALS)
                {
                    TileEntity tileOther = worldIn.getTileEntity(pos.offset(facing));

                    tileTableOther = tileOther != null && tileOther instanceof TileEntityTable ? (TileEntityTable) tileOther : null;

                    if (tileTableOther != null)
                    {
                        if (!tileTableOther.getIsDouble())
                        {
                            tileTableThis.setFacing(facingThis);
                            tileTableThis.setIsDouble(true);

                            tileTableOther.setFacing(facingThis.getOpposite());
                            tileTableOther.setIsDouble(true);
                        }
                    }
                }
            }
        }
    }
    */
}