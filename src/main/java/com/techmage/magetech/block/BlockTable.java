package com.techmage.magetech.block;

import com.techmage.magetech.utility.NBTHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockTable extends BlockMageTech
{
    private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    private static final PropertyBool DOUBLE = PropertyBool.create("double");
    private static final PropertyBool SIDE = PropertyBool.create("side");

    private static AxisAlignedBB BOUNDS_SINGLE = new AxisAlignedBB(0.05F, 0F, 0.05F, 0.95F, 0.95F, 0.95F);
    private static AxisAlignedBB BOUNDS_DOUBLE_NORTH = new AxisAlignedBB(0.05F, 0F, 0F, 0.95F, 0.95F, 0.95F);
    private static AxisAlignedBB BOUNDS_DOUBLE_EAST = new AxisAlignedBB(0.05F, 0F, 0.05F, 1F, 0.95F, 0.95F);
    private static AxisAlignedBB BOUNDS_DOUBLE_SOUTH = new AxisAlignedBB(0.05F, 0F, 0.05F, 0.95F, 0.95F, 1F);
    private static AxisAlignedBB BOUNDS_DOUBLE_WEST = new AxisAlignedBB(0F, 0F, 0.05F, 0.95F, 0.95F, 0.95F);

    public BlockTable(String name)
    {
        super(Material.WOOD, name);

        setHardness(0.5F);
        useNeighborBrightness = true;

        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(DOUBLE, false).withProperty(SIDE, false));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        if (NBTHelper.hasTag(stack, "woodCraftedWith"))
        {
            ItemStack woodCraftedWith = new ItemStack(NBTHelper.getTagCompound(stack, "woodCraftedWith"));

            tooltip.add(woodCraftedWith.getDisplayName().substring(0, woodCraftedWith.getDisplayName().length() - 5));
        }
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing()).withProperty(DOUBLE, false).withProperty(SIDE, false));
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (state.getValue(DOUBLE))
        {
            if (worldIn.getBlockState(pos.offset(state.getValue(FACING))).getBlock() != this
                    || worldIn.getBlockState(pos.offset(state.getValue(FACING))).getValue(FACING) != state.getValue(FACING).getOpposite())
                worldIn.setBlockState(pos, state.withProperty(FACING, state.getValue(FACING).rotateYCCW()).withProperty(DOUBLE, false).withProperty(SIDE, false));
        }

        if (!state.getValue(DOUBLE))
        {
            for (EnumFacing facing : EnumFacing.HORIZONTALS)
            {
                if (worldIn.getBlockState(pos.offset(facing)).getBlock() == this)
                {
                    if (!worldIn.getBlockState(pos.offset(facing)).getValue(DOUBLE))
                    {
                        boolean side = pos.getX() - pos.offset(facing).getX() > 0 || pos.getY() - pos.offset(facing).getY() > 0;

                        worldIn.setBlockState(pos.offset(facing), state.withProperty(FACING, facing.getOpposite()).withProperty(DOUBLE, true).withProperty(SIDE, !side));
                        worldIn.setBlockState(pos, state.withProperty(FACING, facing).withProperty(DOUBLE, true).withProperty(SIDE, side));

                        break;
                    }
                }
            }
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        if (state.getValue(DOUBLE))
        {
            EnumFacing facing = state.getValue(FACING);

            switch (facing)
            {
                case NORTH:
                    return BOUNDS_DOUBLE_NORTH;

                case EAST:
                    return BOUNDS_DOUBLE_EAST;

                case SOUTH:
                    return BOUNDS_DOUBLE_SOUTH;

                case WEST:
                    return BOUNDS_DOUBLE_WEST;
            }
        }

        return BOUNDS_SINGLE;
    }

    EnumFacing getFacing(int meta)
    {
        EnumFacing facing = EnumFacing.getFront(meta);

        if (facing.getAxis() == EnumFacing.Axis.Y)
            facing = EnumFacing.NORTH;

        return facing;
    }

    /*
    ----------------------------------------------------
                  Structure of Metadata
    ----------------------------------------------------
    - Facing will return a int between 0 and 3 (2 Bit)
    - isDouble will return a boolean (1 Bit)
    - Side will return a boolean (1 Bit)

                           sdff
                           ||||
                           ||\\____ f: Facing
                           ||______ d: isDouble
                           \_______ s: Side
    */

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(FACING, getFacing(meta + 2)).withProperty(DOUBLE, (meta & 0b0100) > 0).withProperty(SIDE, (meta & 0b1000) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int meta = state.getValue(FACING).getIndex() - 2;

        if (state.getValue(DOUBLE))
            meta |= 0b0100;

        if (state.getValue(SIDE))
            meta |= 0b1000;

        return meta;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING, DOUBLE, SIDE);
    }

    // CONFIGURE RENDER

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
}