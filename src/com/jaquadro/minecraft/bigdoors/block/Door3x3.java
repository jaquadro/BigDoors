package com.jaquadro.minecraft.bigdoors.block;

import com.jaquadro.minecraft.bigdoors.BigDoors;
import com.jaquadro.minecraft.bigdoors.block.render.Door3x3Renderer;
import com.jaquadro.minecraft.bigdoors.block.tile.Door3x3Tile;
import net.malisis.core.MalisisCore;
import net.malisis.core.block.BoundingBoxType;
import net.malisis.core.block.MalisisBlock;
import net.malisis.core.block.component.DirectionalComponent;
import net.malisis.core.renderer.DefaultRenderer;
import net.malisis.core.renderer.MalisisRendered;
import net.malisis.core.renderer.icon.Icon;
import net.malisis.core.renderer.icon.provider.IIconProvider;
import net.malisis.core.util.*;
import net.malisis.core.util.chunkcollision.ChunkCollision;
import net.malisis.core.util.chunkcollision.IChunkCollidable;
import net.malisis.doors.block.Door;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

@MalisisRendered(block = Door3x3Renderer.class, item = DefaultRenderer.Item.class)
public class Door3x3 extends MalisisBlock implements ITileEntityProvider, IChunkCollidable
{
    public enum Type {
        OAK("doors_oak_3x3", Items.OAK_DOOR),
        SPRUCE("doors_spruce_3x3", Items.SPRUCE_DOOR),
        BIRCH("doors_birch_3x3", Items.BIRCH_DOOR),
        JUNGLE("doors_jungle_3x3", Items.JUNGLE_DOOR),
        ACACIA("doors_acacia_3x3", Items.ACACIA_DOOR),
        DARK_OAK("doors_dark_oak_3x3", Items.DARK_OAK_DOOR);

        public String name;
        public Item door;

        Type (String name, Item door) {
            this.name = name;
            this.door = door;
        }
    }

    private AxisAlignedBB defaultBoundingBox = new AxisAlignedBB(-1, 0, 1 - Door.DOOR_WIDTH, 2, 3, 1);

    public Door3x3 (Type type) {
        this(Material.WOOD);

        setName(type.name);

        if (MalisisCore.isClient())
            addComponent(Door3x3IconProvider.get(type));
    }

    protected Door3x3 (Material material) {
        super(material);

        setHardness(5.0f);
        setResistance(10.0f);
        setSoundType(SoundType.WOOD);
        setCreativeTab(CreativeTabs.REDSTONE);
        addComponent(new DirectionalComponent());
    }

    //@Override
    //@SideOnly(Side.CLIENT)
    //public void createIconProvider (Object object) {
    //    iconProvider = new Door3x3IconProvider(type);
    //}

    @Override
    public void onBlockPlacedBy (World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack itemStack) {
        super.onBlockPlacedBy(world, pos, state, player, itemStack);

        ChunkCollision.get().replaceBlocks(world, new MBlockState(world, pos));

        Door3x3Tile te = TileEntityUtils.getTileEntity(Door3x3Tile.class, world, pos);
        if (te != null)
            te.setFrameState(MBlockState.fromNBT(itemStack.getTagCompound()));
    }

    @Override
    public boolean onBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote)
            return true;

        Door3x3Tile te = TileEntityUtils.getTileEntity(Door3x3Tile.class, world, pos);
        if (te == null)
            return true;

        te.openOrCloseDoor();
        return true;
    }

    @Override
    public AxisAlignedBB[] getBoundingBoxes (IBlockAccess world, BlockPos pos, IBlockState state, BoundingBoxType type) {
        if (type == BoundingBoxType.PLACEDBOUNDINGBOX)
            return new AxisAlignedBB[] { defaultBoundingBox };

        Door3x3Tile te = TileEntityUtils.getTileEntity(Door3x3Tile.class, world, pos);
        if (te == null)
            return AABBUtils.identities();

        AxisAlignedBB[] aabbs = new AxisAlignedBB[] { defaultBoundingBox };
        if ((type == BoundingBoxType.COLLISION || type == BoundingBoxType.RAYTRACE || type == BoundingBoxType.RENDER) && (te.isOpened() || te.isMoving()))
            aabbs = new AxisAlignedBB[]  {
                new AxisAlignedBB(-1, 0, -.5f, Door.DOOR_WIDTH - 1, 3, 1),
                new AxisAlignedBB(2 - Door.DOOR_WIDTH, 0, -.5f, 2, 3, 1)
            };

        return aabbs;
    }

    @Override
    public int blockRange () {
        return 3;
    }

    @Override
    public TileEntity createNewTileEntity (World world, int metadata) {
        return new Door3x3Tile();
    }

    @Override
    public boolean removedByPlayer (IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        if (!player.capabilities.isCreativeMode) {
            Door3x3Tile te = TileEntityUtils.getTileEntity(Door3x3Tile.class, world, pos);
            if (te != null)
                spawnAsEntity(world, pos, te.getDroppedItemStack());
        }

        return super.removedByPlayer(state, world, pos, player, willHarvest);
    }

    @Override
    public List<ItemStack> getDrops (IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        return new ArrayList<>();
    }

    @Override
    public boolean isOpaqueCube (IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube (IBlockState state) {
        return false;
    }

    @Override
    public EnumBlockRenderType getRenderType (IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean canRenderInLayer (BlockRenderLayer layer) {
        return true;
    }

    /*@Override
    public boolean onBlockSet (World world, BlockPos pos, MBlockState state) {
        if (!state.getBlock().isReplaceable(world, state.getPos()))
            return true;

        for (AxisAlignedBB aabb : AABBUtils.getCollisionBoundingBoxes(world, new MBlockState(pos, this), true)) {
            if (aabb != null && aabb.intersectsWith(AABBUtils.identity(state.getPos())))
                return false;
        }

        return true;
    }

    @Override
    public boolean onBlockRemoved (World world, BlockPos pos, BlockPos blockRemoved) {
        return true;
    }*/

    @Override
    public void neighborChanged (IBlockState state, World worldIn, BlockPos pos, Block neighbor) {
        Door3x3Tile te = TileEntityUtils.getTileEntity(Door3x3Tile.class, worldIn, pos);
        if (te == null)
            return;

        boolean powered = te.isPowered();
        if ((powered || neighbor.canProvidePower(state)) && neighbor != this)
            te.setPowered(powered);
    }

    public static class Door3x3IconProvider implements IIconProvider
    {
        Icon itemIcon;
        Icon doorIcon;

        public Door3x3IconProvider (Type type) {
            itemIcon = new Icon(BigDoors.MOD_ID + ":items/" + type.name + "_item");
            doorIcon = new Icon(BigDoors.MOD_ID + ":blocks/" + type.name);
        }

        @Override
        public Icon getIcon () {
            return itemIcon;
        }

        public Icon getDoorIcon () {
            return doorIcon;
        }

        public static Door3x3IconProvider get (Type type) {
            return new Door3x3IconProvider(type);
        }
    }
}
