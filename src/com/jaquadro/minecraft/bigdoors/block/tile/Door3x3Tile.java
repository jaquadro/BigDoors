package com.jaquadro.minecraft.bigdoors.block.tile;

import com.google.common.base.Objects;
import com.jaquadro.minecraft.bigdoors.block.movement.Door3x3Movement;
import net.malisis.core.block.IBlockDirectional;
import net.malisis.core.util.MBlockState;
import net.malisis.core.util.TileEntityUtils;
import net.malisis.core.util.chunkcollision.ChunkCollision;
import net.malisis.doors.DoorDescriptor;
import net.malisis.doors.DoorRegistry;
import net.malisis.doors.DoorState;
import net.malisis.doors.sound.VanillaDoorSound;
import net.malisis.doors.tileentity.DoorTileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class Door3x3Tile extends DoorTileEntity
{
    private IBlockState frameState;

    public Door3x3Tile () {
        DoorDescriptor descriptor = new DoorDescriptor();
        descriptor.setMovement(DoorRegistry.getMovement(Door3x3Movement.class));
        descriptor.setSound(DoorRegistry.getSound(VanillaDoorSound.class));
        descriptor.setDoubleDoor(false);
        descriptor.setOpeningTime(15);
        setDescriptor(descriptor);

        frameState = Blocks.quartz_block.getDefaultState();
    }

    public IBlockState getFrameState () {
        return frameState;
    }

    public void setFrameState (IBlockState state) {
        if (state != null)
            frameState = state;
    }

    @Override
    public EnumFacing getDirection () {
        return IBlockDirectional.getDirection(worldObj, pos);
    }

    @Override
    public IBlockState getBlockState () {
        return null;
    }

    @Override
    public boolean isOpened () {
        return state == DoorState.OPENED;
    }

    @Override
    public boolean isTopBlock (BlockPos pos) {
        return false;
    }

    @Override
    public boolean isHingeLeft () {
        return true;
    }

    @Override
    public boolean isPowered () {
        return getWorld().isBlockIndirectlyGettingPowered(getPos()) > 0;
    }

    @Override
    public void setDoorState (DoorState newState) {
        boolean moving = this.moving;
        MBlockState state = null;

        if (getWorld() != null) {
            state = new MBlockState(pos, getBlockType());
            ChunkCollision.get().updateBlocks(getWorld(), state);
        }

        super.setDoorState(newState);
        if (getWorld() != null && moving && !this.moving)
            ChunkCollision.get().replaceBlocks(getWorld(), state);
    }

    @Override
    public void readFromNBT (NBTTagCompound tag) {
        super.readFromNBT(tag);
        frameState = Objects.firstNonNull(MBlockState.fromNBT(tag), Blocks.quartz_block.getDefaultState());
    }

    @Override
    public void writeToNBT (NBTTagCompound tag) {
        super.writeToNBT(tag);

        MBlockState.toNBT(tag, frameState);
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox () {
        return TileEntityUtils.getRenderingBounds(this);
    }
}
