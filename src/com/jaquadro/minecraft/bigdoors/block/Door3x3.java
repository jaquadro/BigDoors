package com.jaquadro.minecraft.bigdoors.block;

import com.jaquadro.minecraft.bigdoors.BigDoors;
import com.jaquadro.minecraft.bigdoors.block.render.Door3x3Renderer;
import com.jaquadro.minecraft.bigdoors.block.tile.Door3x3Tile;
import net.malisis.core.MalisisCore;
import net.malisis.core.block.BoundingBoxType;
import net.malisis.core.block.IBlockDirectional;
import net.malisis.core.block.MalisisBlock;
import net.malisis.core.renderer.DefaultRenderer;
import net.malisis.core.renderer.MalisisRendered;
import net.malisis.core.renderer.icon.MalisisIcon;
import net.malisis.core.renderer.icon.provider.IBlockIconProvider;
import net.malisis.core.util.*;
import net.malisis.core.util.chunkcollision.ChunkCollision;
import net.malisis.core.util.chunkcollision.IChunkCollidable;
import net.malisis.core.util.chunklistener.IBlockListener;
import net.malisis.doors.block.Door;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@MalisisRendered(block = Door3x3Renderer.class, item = DefaultRenderer.Item.class)
public class Door3x3 extends MalisisBlock implements ITileEntityProvider, IChunkCollidable, IBlockListener, IBlockDirectional
{
    public enum Type {
        OAK("doors_oak_3x3", Items.oak_door),
        SPRUCE("doors_spruce_3x3", Items.spruce_door),
        BIRCH("doors_birch_3x3", Items.birch_door),
        JUNGLE("doors_jungle_3x3", Items.jungle_door),
        ACACIA("doors_acacia_3x3", Items.acacia_door),
        DARK_OAK("doors_dark_oak_3x3", Items.dark_oak_door);

        public String name;
        public Item door;

        Type (String name, Item door) {
            this.name = name;
            this.door = door;
        }
    }

    private AxisAlignedBB defaultBoundingBox = new AxisAlignedBB(-1, 0, 1 - Door.DOOR_WIDTH, 2, 3, 1);
    private Type type;

    public Door3x3 (Type type) {
        this(Material.wood);

        this.type = type;
        setName(type.name);
    }

    protected Door3x3 (Material material) {
        super(material);

        setHardness(5.0f);
        setResistance(10.0f);
        setStepSound(soundTypeWood);
        setCreativeTab(CreativeTabs.tabRedstone);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void createIconProvider (Object object) {
        iconProvider = new Door3x3IconProvider(type);
    }

    @Override
    public void onBlockPlacedBy (World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack itemStack) {
        super.onBlockPlacedBy(world, pos, state, player, itemStack);

        ChunkCollision.get().replaceBlocks(world, new MBlockState(world, pos));

        Door3x3Tile te = TileEntityUtils.getTileEntity(Door3x3Tile.class, world, pos);
        if (te != null)
            te.setFrameState(MBlockState.fromNBT(itemStack.getTagCompound()));
    }

    @Override
    public boolean onBlockActivated (World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote)
            return true;

        Door3x3Tile te = TileEntityUtils.getTileEntity(Door3x3Tile.class, world, pos);
        if (te == null)
            return true;

        te.openOrCloseDoor();
        return true;
    }

    @Override
    public AxisAlignedBB[] getBoundingBoxes (IBlockAccess world, BlockPos pos, BoundingBoxType type) {
        if (type == BoundingBoxType.PLACEDBOUNDINGBOX)
            return new AxisAlignedBB[] { defaultBoundingBox };

        Door3x3Tile te = TileEntityUtils.getTileEntity(Door3x3Tile.class, world, pos);
        if (te == null)
            return AABBUtils.identities();

        AxisAlignedBB[] aabbs = new AxisAlignedBB[] { defaultBoundingBox };
        if ((type == BoundingBoxType.COLLISION || type == BoundingBoxType.RAYTRACE) && (te.isOpened() || te.isMoving()))
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
    public boolean isOpaqueCube () {
        return false;
    }

    @Override
    public boolean isFullCube () {
        return false;
    }

    @Override
    public int getRenderType () {
        return MalisisCore.malisisRenderType;
    }

    @Override
    public boolean canRenderInLayer (EnumWorldBlockLayer layer) {
        return true;
    }

    @Override
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
    }

    @Override
    public void onNeighborBlockChange (World worldIn, BlockPos pos, IBlockState state, Block neighbor) {
        Door3x3Tile te = TileEntityUtils.getTileEntity(Door3x3Tile.class, worldIn, pos);
        if (te == null)
            return;

        boolean powered = te.isPowered();
        if ((powered || neighbor.canProvidePower()) && neighbor != this)
            te.setPowered(powered);
    }

    public static class Door3x3IconProvider implements IBlockIconProvider
    {
        MalisisIcon itemIcon;
        MalisisIcon doorIcon;

        public Door3x3IconProvider (Type type) {
            itemIcon = new MalisisIcon(BigDoors.MOD_ID + ":items/" + type.name + "_item");
            doorIcon = new MalisisIcon(BigDoors.MOD_ID + ":blocks/" + type.name);
        }

        @Override
        public void registerIcons (TextureMap textureMap) {
            itemIcon = itemIcon.register(textureMap);
            doorIcon = doorIcon.register(textureMap);
        }

        @Override
        public MalisisIcon getIcon () {
            return itemIcon;
        }

        public MalisisIcon getDoorIcon () {
            return doorIcon;
        }
    }
}
