package com.jaquadro.minecraft.bigdoors.block;

import com.jaquadro.minecraft.bigdoors.BigDoors;
import com.jaquadro.minecraft.bigdoors.block.tile.Door3x3Tile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.malisis.core.block.BoundingBoxType;
import net.malisis.core.block.MalisisBlock;
import net.malisis.core.util.*;
import net.malisis.core.util.chunkcollision.ChunkCollision;
import net.malisis.core.util.chunkcollision.IChunkCollidable;
import net.malisis.core.util.chunklistener.IBlockListener;
import net.malisis.doors.door.DoorState;
import net.malisis.doors.door.block.Door;
import net.malisis.doors.door.tileentity.DoorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWood;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Door3x3 extends MalisisBlock implements ITileEntityProvider, IChunkCollidable, IBlockListener
{
    public static int renderId;

    @SideOnly(Side.CLIENT)
    IIcon[] icons;

    private AxisAlignedBB defaultBoundingBox = AxisAlignedBB.getBoundingBox(-1, 0, 1 - Door.DOOR_WIDTH, 2, 3, 1);

    public Door3x3 () {
        super(Material.wood);

        setHardness(5.0f);
        setResistance(10.0f);
        setStepSound(soundTypeWood);
        setUnlocalizedName("door3x3");
        setCreativeTab(CreativeTabs.tabRedstone);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons (IIconRegister register) {
        icons = new IIcon[BlockWood.field_150096_a.length];

        for (int i = 0; i < icons.length; i++)
            icons[i] = register.registerIcon(BigDoors.MOD_ID + ":doors_" + BlockWood.field_150096_a[i] + "_3x3");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon (int side, int meta) {
        return icons[meta % icons.length];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon (IBlockAccess worldIn, int x, int y, int z, int side) {
        Door3x3Tile tile = TileEntityUtils.getTileEntity(Door3x3Tile.class ,worldIn, x, y, z);
        if (tile == null)
            return getIcon(side, 0);

        return getIcon(side, tile.getWoodType());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getItemIconName () {
        return BigDoors.MOD_ID + ":doors_oak";
    }

    @Override
    public void getSubBlocks (Item itemIn, CreativeTabs tab, List list) {
        for (int i = 0; i < icons.length; i++)
            list.add(new ItemStack(itemIn, 1, i));
    }

    @Override
    public boolean canPlaceBlockOnSide (World world, int x, int y, int z, int side) {
        if (side != 1)
            return false;

        ForgeDirection dir = ForgeDirection.getOrientation(side).getOpposite();
        return world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ).isSideSolid(world, x, y, z, dir);
    }

    @Override
    public void onBlockPlacedBy (World world, int x, int y, int z, EntityLivingBase player, ItemStack itemStack) {
        ForgeDirection dir = EntityUtils.getEntityFacing(player);
        int metadata = Door.dirToInt(dir);
        world.setBlockMetadataWithNotify(x, y, z, metadata, 2);

        ChunkCollision.get().replaceBlocks(world, new BlockState(world, x, y, z));
    }

    @Override
    public boolean onBlockActivated (World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (world.isRemote)
            return true;

        Door3x3Tile te = TileEntityUtils.getTileEntity(Door3x3Tile.class, world, x, y, z);
        if (te == null)
            return true;

        te.openOrCloseDoor();
        return true;
    }

    @Override
    public AxisAlignedBB[] getPlacedBoundingBox (IBlockAccess world, int x, int y, int z, int side, EntityPlayer player, ItemStack itemStack) {
        ForgeDirection dir = EntityUtils.getEntityFacing(player);
        return AABBUtils.rotate(new AxisAlignedBB[] { defaultBoundingBox.copy() }, dir);
    }

    @Override
    public AxisAlignedBB[] getBoundingBox (IBlockAccess world, int x, int y, int z, BoundingBoxType type) {
        Door3x3Tile te = TileEntityUtils.getTileEntity(Door3x3Tile.class, world, x, y, z);
        if (te == null)
            return AABBUtils.identities();

        AxisAlignedBB[] aabbs = new AxisAlignedBB[] { defaultBoundingBox.copy() };
        if (type == BoundingBoxType.RENDER)
            aabbs[0].minZ = -.5f;
        else if ((type == BoundingBoxType.COLLISION || type == BoundingBoxType.CHUNKCOLLISION || type == BoundingBoxType.RAYTRACE) && (te.isOpened() || te.isMoving()))
            aabbs = new AxisAlignedBB[] {
                AxisAlignedBB.getBoundingBox(-1, 0, -.5f, Door.DOOR_WIDTH - 1, 3, 1),
                AxisAlignedBB.getBoundingBox(2 - Door.DOOR_WIDTH, 0, -.5f, 2, 3, 1)
            };

        return AABBUtils.rotate(aabbs, Door.intToDir(te.getDirection()));
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        Door3x3Tile te = TileEntityUtils.getTileEntity(Door3x3Tile.class, world, x, y, z);
        if(te != null && !te.isMoving() && te.getMovement() != null) {
            AxisAlignedBB aabb = te.getMovement().getBoundingBox(te, te.isTopBlock(x, y, z), BoundingBoxType.COLLISION);
            return aabb == null ? null : this.setBlockBounds(aabb.offset(x, y, z));
        } else {
            return null;
        }
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World worldIn, int x, int y, int z, Vec3 startVec, Vec3 endVec)
    {
        this.setBlockBoundsBasedOnState(worldIn, x, y, z);
        return super.collisionRayTrace(worldIn, x, y, z, startVec, endVec);
    }

    protected AxisAlignedBB setBlockBounds(AxisAlignedBB aabb) {
        if(aabb == null) {
            aabb = AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        }

        this.setBlockBounds((float)aabb.minX, (float)aabb.minY, (float)aabb.minZ, (float)aabb.maxX, (float)aabb.maxY, (float)aabb.maxZ);
        return aabb;
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
    public boolean renderAsNormalBlock () {
        return false;
    }

    @Override
    public int getRenderType () {
        return renderId;
    }

    @Override
    public boolean onBlockSet (World world, BlockPos pos, BlockState state) {
        if (!state.getBlock().isReplaceable(world, state.getX(), state.getY(), state.getZ()))
            return true;

        for (AxisAlignedBB aabb : AABBUtils.getCollisionBoundingBoxes(world, new BlockState(pos, this), true)) {
            if (state.getPos().isInside(aabb))
                return false;
        }

        return true;
    }

    @Override
    public boolean onBlockRemoved (World world, BlockPos pos, BlockPos blockRemoved) {
        return true;
    }

    @Override
    public ArrayList<ItemStack> getDrops (World world, int x, int y, int z, int metadata, int fortune) {
        Door3x3Tile tile = TileEntityUtils.getTileEntity(Door3x3Tile.class, world, x, y, z);
        if (tile == null)
            return super.getDrops(world, x, y, z, metadata, fortune);

        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        int count = quantityDropped(metadata, fortune, world.rand);
        for (int i = 0; i < count; i++) {
            Item item = getItemDropped(metadata, world.rand, fortune);
            if (item != null)
                items.add(new ItemStack(item, 1, tile.getWoodType()));
        }

        return items;
    }

    @Override
    public boolean removedByPlayer (World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
        if (willHarvest)
            return true;

        return super.removedByPlayer(world, player, x, y, z, willHarvest);
    }

    @Override
    public void harvestBlock (World worldIn, EntityPlayer player, int x, int y, int z, int meta) {
        super.harvestBlock(worldIn, player, x, y, z, meta);
        worldIn.setBlockToAir(x, y, z);
    }

    @Override
    public void onNeighborBlockChange (World worldIn, int x, int y, int z, Block neighbor) {
        Door3x3Tile te = TileEntityUtils.getTileEntity(Door3x3Tile.class, worldIn, x, y, z);
        if (te != null) {
            boolean powered = te.isPowered();
            if ((powered || neighbor.canProvidePower()) && neighbor != this)
                te.setPowered(powered);
        }
    }

    @Override
    public void updateTick (World worldIn, int x, int y, int z, Random random) {
        Door3x3Tile te = TileEntityUtils.getTileEntity(Door3x3Tile.class, worldIn, x, y, z);
        if (te != null) {
            if (te.getDescriptor().getAutoCloseTime() > 0) {
                if (te.getState() != DoorState.CLOSED && te.getState() != DoorState.CLOSING)
                    te.openOrCloseDoor();
            }
        }
    }
}
