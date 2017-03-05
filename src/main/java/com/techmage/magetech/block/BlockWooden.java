package com.techmage.magetech.block;

import com.techmage.magetech.block.unlistedproperties.UnlistedPropertyFacing;
import com.techmage.magetech.block.unlistedproperties.UnlistedPropertyString;
import com.techmage.magetech.client.model.bakedmodel.BakedModelWooden;
import com.techmage.magetech.tileentity.TileEntityWooden;
import com.techmage.magetech.utility.LogHelper;
import com.techmage.magetech.utility.ResourceHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.IRetexturableModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockWooden extends BlockMageTech implements ITileEntityProvider
{
    public static final UnlistedPropertyString UP_WOOD = new UnlistedPropertyString("wood");
    public static final UnlistedPropertyFacing UP_FACING = new UnlistedPropertyFacing("facing");

    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public BlockWooden(String name)
    {
        super(Material.WOOD, name);

        setHardness(0.5F);
        useNeighborBrightness = true;

        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Override
    public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta)
    {
        return new TileEntityWooden();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        NBTTagCompound tag = stack.getTagCompound();

        if (tag != null)
        {
            NBTTagCompound tagWood = tag.getCompoundTag(TileEntityWooden.TAG_WOOD);
            ItemStack wood = new ItemStack(tagWood);

            tooltip.add(wood.getDisplayName().substring(0, (wood.getDisplayName().length() - 5)));
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        super.onBlockPlacedBy(world, pos, state, placer, stack);

        world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing()));

        NBTTagCompound tag = stack.getTagCompound();

        if (tag == null)
        {
            tag = new NBTTagCompound();

            ItemStack blockStack = new ItemStack(Blocks.WOODEN_SLAB, 1, 0);
            NBTTagCompound subTag = new NBTTagCompound();

            blockStack.writeToNBT(subTag);

            tag.setTag(TileEntityWooden.TAG_WOOD, subTag);

            stack.setTagCompound(tag);
        }

        TileEntity tile = world.getTileEntity(pos);

        if (tile != null && tile instanceof TileEntityWooden)
        {
            TileEntityWooden tileWooden = (TileEntityWooden) tile;
            NBTTagCompound tagWood = tag.getCompoundTag(TileEntityWooden.TAG_WOOD);

            tileWooden.updateTextureBlock(tagWood);
            tileWooden.setFacing(placer.getHorizontalFacing().getOpposite());
        }
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world,BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        this.onBlockDestroyedByPlayer(world, pos, state);

        if (willHarvest)
            this.harvestBlock(world, player, pos, state, world.getTileEntity(pos), player.getHeldItemMainhand());

        world.setBlockToAir(pos);

        return false;
    }

    private void writeDataOntoItemStack(ItemStack item, IBlockAccess world, BlockPos pos, IBlockState state, boolean inventorySave)
    {
        TileEntity tile = world.getTileEntity(pos);

        if (tile != null && tile instanceof TileEntityWooden)
        {
            TileEntityWooden tileWooden = (TileEntityWooden) tile;
            NBTTagCompound tag = item.getTagCompound();

            if (tag == null)
                tag = new NBTTagCompound();

            NBTTagCompound data = tileWooden.getTextureBlock();

            if (!data.hasNoTags())
                tag.setTag(TileEntityWooden.TAG_WOOD, data);

            if (!tag.hasNoTags())
                item.setTagCompound(tag);
        }
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        if (!worldIn.isRemote && !worldIn.restoringBlockSnapshots)
        {
            List<ItemStack> items = this.getDrops(worldIn, pos, state, fortune);
            chance = net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, fortune, chance, false, harvesters.get());

            for (ItemStack item : items)
            {
                if (item.getItem() == Item.getItemFromBlock(this))
                    writeDataOntoItemStack(item, worldIn, pos, state, chance >= 1f);
            }

            for (ItemStack item : items)
            {
                if (worldIn.rand.nextFloat() <= chance)
                    spawnAsEntity(worldIn, pos, item);
            }
        }
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        List<ItemStack> drops = getDrops(world, pos, world.getBlockState(pos), 0);

        if (drops.size() > 0)
        {
            ItemStack stack = drops.get(0);
            writeDataOntoItemStack(stack, world, pos, state, false);

            return stack;
        }

        return super.getPickBlock(state, target, world, pos, player);
    }

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

    @Override
    protected BlockStateContainer createBlockState()
    {
        IProperty[] listedProperties = new IProperty[] { FACING };
        IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[] {UP_WOOD, UP_FACING };

        return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    @Override
    public IBlockState getExtendedState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos)
    {
        IExtendedBlockState extendedState = (IExtendedBlockState) state;

        TileEntity tile = world.getTileEntity(pos);

        if (tile != null && tile instanceof TileEntityWooden)
        {
            TileEntityWooden tileWooden = (TileEntityWooden) tile;

            return tileWooden.writeExtendedBlockState(extendedState);
        }

        return super.getExtendedState(state, world, pos);
    }

    public static ItemStack createItemStack(BlockWooden table, int tableMeta, Block block, int blockMeta)
    {
        ItemStack stack = new ItemStack(table, 1, tableMeta);

        if (block != null)
        {
            ItemStack blockStack = new ItemStack(block, 1, blockMeta);
            NBTTagCompound tag = new NBTTagCompound();
            NBTTagCompound subTag = new NBTTagCompound();

            blockStack.writeToNBT(subTag);

            tag.setTag(TileEntityWooden.TAG_WOOD, subTag);

            stack.setTagCompound(tag);
        }

        return stack;
    }

    public static void replaceBlocksModels(String location, ModelBakeEvent event)
    {
        ResourceLocation modelLocation =  ResourceHelper.getResource("block/" + location);

        for (EnumFacing facing : FACING.getAllowedValues())
        {
            ModelResourceLocation modelVariantLocation = new ModelResourceLocation(ResourceHelper.resource(location),
                    String.format("%s=%s", FACING.getName(), facing));

            replaceBlockModel(modelVariantLocation, modelLocation, event);
        }
    }

    private static void replaceBlockModel(ModelResourceLocation modelVariantLocation, ResourceLocation modelLocation, ModelBakeEvent event)
    {
        try
        {
            IModel model = ModelLoaderRegistry.getModel(modelLocation);

            if (model instanceof IRetexturableModel)
            {
                IRetexturableModel tableModel = (IRetexturableModel) model;
                IBakedModel standard = event.getModelRegistry().getObject(modelVariantLocation);

                if (standard instanceof IPerspectiveAwareModel)
                {
                    IBakedModel finalModel = new BakedModelWooden((IPerspectiveAwareModel) standard, tableModel, DefaultVertexFormats.BLOCK);

                    event.getModelRegistry().putObject(modelVariantLocation, finalModel);
                }
            }
        }

        catch(Exception e)
        {
            LogHelper.error(e);
        }
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