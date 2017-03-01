package com.techmage.magetech.block;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockShelfScroll extends BlockMageTech
{
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockShelfScroll(String name)
    {
        super(Material.WOOD, name);

        setHardness(0.5F);
        useNeighborBrightness = true;

        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing()));

        // TileEntityShelfScroll shelfScroll = (TileEntityShelfScroll) worldIn.getTileEntity(pos);
        // shelfScroll.setOrientation(placer.getHorizontalFacing());
    }

    /*
    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityShelfScroll();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntityShelfScroll shelfScroll = (TileEntityShelfScroll) world.getTileEntity(pos);

        if (!player.isSneaking())
        {
            if (heldItem != null && heldItem.getItem() == MageTechItems.RESEARCH_SCROLL)
            {
                if (shelfScroll.canStoreScroll())
                {
                    shelfScroll.storeScroll(heldItem);
                    player.setHeldItem(hand, null);
                }
            }

            else if (heldItem == null)
            {
                if (!world.isRemote)
                        player.openGui(MageTech.instance, GuiID.SHELF_SCROLL.ordinal(), world, pos.getX(), pos.getY(), pos.getZ());
            }
        }

        else
        {
            if (!shelfScroll.isInventoryEmpty())
            {
                ItemStack scroll = shelfScroll.removeLastScroll();

                if (!world.isRemote)
                    world.spawnEntityInWorld(new EntityItem(world, pos.offset(side).getX(), pos.offset(side).getY(), pos.offset(side).getZ(), scroll));
            }
        }

        return false;
    }
    */

    /*
    ----------------------------------------------------
                  Structure of Metadata
    ----------------------------------------------------
    - Facing will return a int between 0 and 5 (3 Bit)

                           0fff
                            |||
                            \\\
                             ------ f: Facing
    */

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing facing = EnumFacing.getFront(meta);

        if (facing.getAxis() == EnumFacing.Axis.Y)
            facing = EnumFacing.NORTH;

        return getDefaultState().withProperty(FACING, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getIndex();
    }

    // CONFIGURE RENDER

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING);
    }

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