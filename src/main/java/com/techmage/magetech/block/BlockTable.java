package com.techmage.magetech.block;

import com.techmage.magetech.block.unlistedproperties.UnlistedPropertyItemStack;
import com.techmage.magetech.client.model.bakedmodel.ModelTable;
import com.techmage.magetech.utility.NBTHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class BlockTable extends BlockMageTech
{
    private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    private static final PropertyBool DOUBLE = PropertyBool.create("double");

    public static final UnlistedPropertyItemStack WOOD_CRAFTED_WITH = new UnlistedPropertyItemStack("wood_crafted_with");

    private ItemStack woodCraftedWith = ItemStack.EMPTY;

    private final static AxisAlignedBB BOUNDS_SINGLE = new AxisAlignedBB(0.05F, 0F, 0.05F, 0.95F, 0.95F, 0.95F);
    private final static AxisAlignedBB BOUNDS_DOUBLE_NORTH = new AxisAlignedBB(0.05F, 0F, 0F, 0.95F, 0.95F, 0.95F);
    private final static AxisAlignedBB BOUNDS_DOUBLE_EAST = new AxisAlignedBB(0.05F, 0F, 0.05F, 1F, 0.95F, 0.95F);
    private final static AxisAlignedBB BOUNDS_DOUBLE_SOUTH = new AxisAlignedBB(0.05F, 0F, 0.05F, 0.95F, 0.95F, 1F);
    private final static AxisAlignedBB BOUNDS_DOUBLE_WEST = new AxisAlignedBB(0F, 0F, 0.05F, 0.95F, 0.95F, 0.95F);

    public BlockTable(String name)
    {
        super(Material.WOOD, name);

        setHardness(0.5F);
        useNeighborBrightness = true;

        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(DOUBLE, false));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        String displayName;

        if (NBTHelper.hasTag(stack, "woodCraftedWithDisplayName"))
            displayName = NBTHelper.getString(stack, "woodCraftedWithDisplayName");

        else
            displayName = new ItemStack(Blocks.WOODEN_SLAB, 1, 0).getDisplayName();

        tooltip.add(displayName.substring(0, displayName.length() - 5));
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing()).withProperty(DOUBLE, false));

        if (NBTHelper.hasTag(stack, "woodCraftedWithRegistryName"))
        {
            String registryName = NBTHelper.getString(stack, "woodCraftedWithRegistryName");
            int meta = NBTHelper.getInt(stack, "woodCraftedWithMeta");

            woodCraftedWith = new ItemStack(GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(registryName)), 1, meta);
        }

        else
            woodCraftedWith = new ItemStack(Blocks.WOODEN_SLAB, 1, 0);

        worldIn.markBlockRangeForRenderUpdate(pos.add(- 1, - 1, - 1), pos.add(1, 1, 1));
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (state.getValue(DOUBLE))
        {
            if (worldIn.getBlockState(pos.offset(state.getValue(FACING))).getBlock() != this
                    || worldIn.getBlockState(pos.offset(state.getValue(FACING))).getValue(FACING) != state.getValue(FACING).getOpposite())
                worldIn.setBlockState(pos, state.withProperty(FACING, state.getValue(FACING).rotateYCCW()).withProperty(DOUBLE, false));
        }

        if (!state.getValue(DOUBLE))
        {
            for (EnumFacing facing : EnumFacing.HORIZONTALS)
            {
                if (worldIn.getBlockState(pos.offset(facing)).getBlock() == this)
                {
                    if (!worldIn.getBlockState(pos.offset(facing)).getValue(DOUBLE))
                    {
                        worldIn.setBlockState(pos.offset(facing), state.withProperty(FACING, facing.getOpposite()).withProperty(DOUBLE, true));
                        worldIn.setBlockState(pos, state.withProperty(FACING, facing).withProperty(DOUBLE, true));

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

                           0dff
                            |||
                            |\\____ f: Facing
                            |______ d: isDouble
    */

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(FACING, getFacing(meta + 2)).withProperty(DOUBLE, (meta & 0b0100) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int meta = state.getValue(FACING).getIndex() - 2;

        if (state.getValue(DOUBLE))
            meta |= 0b0100;

        return meta;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        IProperty[] listedProperties = new IProperty[] { FACING, DOUBLE };
        IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[] { WOOD_CRAFTED_WITH };

        return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;

        return extendedBlockState
                .withProperty(WOOD_CRAFTED_WITH, getWoodCraftedWith());
    }

    private ItemStack getWoodCraftedWith()
    {
        return woodCraftedWith;
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