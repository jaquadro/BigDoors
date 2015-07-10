package com.jaquadro.minecraft.bigdoors.block.tile;

import com.jaquadro.minecraft.bigdoors.block.Door3x3;
import com.jaquadro.minecraft.bigdoors.block.movement.Door3x3Movement;
import net.malisis.core.block.BoundingBoxType;
import net.malisis.core.util.BlockState;
import net.malisis.core.util.MultiBlock;
import net.malisis.core.util.chunkblock.ChunkBlockHandler;
import net.malisis.core.util.chunkcollision.ChunkCollision;
import net.malisis.doors.door.DoorDescriptor;
import net.malisis.doors.door.DoorRegistry;
import net.malisis.doors.door.DoorState;
import net.malisis.doors.door.block.Door;
import net.malisis.doors.door.sound.VanillaDoorSound;
import net.malisis.doors.door.tileentity.DoorTileEntity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

public class Door3x3Tile extends DoorTileEntity
{
    private boolean delete = false;
    private boolean processed = true;
    private ForgeDirection direction = ForgeDirection.NORTH;
    private int woodType;

    public Door3x3Tile () {
        DoorDescriptor descriptor = new DoorDescriptor();
        descriptor.setMovement(DoorRegistry.getMovement(Door3x3Movement.class));
        descriptor.setSound(DoorRegistry.getSound(VanillaDoorSound.class));
        descriptor.setDoubleDoor(false);
        descriptor.setOpeningTime(15);
        setDescriptor(descriptor);
    }

    public void setWoodType (int meta) {
        woodType = meta;
    }

    public int getWoodType () {
        return woodType;
    }

    @Override
    public boolean isTopBlock (int x, int y, int z) {
        return false;
    }

    @Override
    public boolean isReversed () {
        return false;
    }

    @Override
    public boolean isPowered () {
        return getWorld().isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
    }

    @Override
    public void setDoorState (DoorState newState) {
        boolean moving = this.moving;
        BlockState state = null;

        if (getWorld() != null) {
            state = new BlockState(xCoord, yCoord, zCoord, getBlockType());
            ChunkCollision.get().updateBlocks(getWorld(), state);
        }

        super.setDoorState(newState);
        if (getWorld() != null && moving && !this.moving)
            ChunkCollision.get().replaceBlocks(getWorld(), state);
    }

    @Override
    public void updateEntity () {
        if (!processed && getWorld() != null) {
            if (delete) {
                getWorld().setBlockToAir(xCoord, yCoord, zCoord);
            }
            else {
                ChunkBlockHandler.get().updateCoordinates(getWorld().getChunkFromBlockCoords(xCoord, zCoord), xCoord, yCoord, zCoord, Blocks.air, getBlockType());
                getWorld().setBlockMetadataWithNotify(xCoord, yCoord, zCoord, Door.dirToInt(direction), 2);
                processed = true;
            }
            return;
        }

        super.updateEntity();
    }

    @Override
    public void readFromNBT (NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey("multiblock")) {
            MultiBlock mb = new MultiBlock(tag);
            delete = !mb.isOrigin(xCoord, yCoord, zCoord);
            direction = mb.getDirection();
            processed = true;
        }

        if (tag.hasKey("wood"))
            woodType = tag.getByte("wood");
    }

    @Override
    public void writeToNBT (NBTTagCompound tag) {
        super.writeToNBT(tag);

        if (woodType != 0)
            tag.setByte("wood", (byte)woodType);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox () {
        return ((Door3x3) getBlockType()).getBoundingBox(getWorld(), xCoord, yCoord, zCoord, BoundingBoxType.RENDER)[0].offset(xCoord, yCoord, zCoord);
    }
}
