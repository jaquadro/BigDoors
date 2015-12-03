package com.jaquadro.minecraft.bigdoors.block.tile;

import com.jaquadro.minecraft.bigdoors.block.sound.RustyDoorSound;
import net.malisis.doors.door.DoorRegistry;
import net.minecraft.nbt.NBTTagCompound;

public class MetalDoor3x3Tile extends Door3x3Tile
{
    private int doorType;

    public MetalDoor3x3Tile () {
        getDescriptor().setSound(DoorRegistry.getSound(RustyDoorSound.class));
    }

    public void setDoorType (int meta) {
        doorType = meta;
    }

    public int getDoorType () {
        return doorType;
    }

    @Override
    public void readFromNBT (NBTTagCompound tag) {
        super.readFromNBT(tag);

        if (tag.hasKey("doorType"))
            doorType = tag.getByte("doorType");
    }

    @Override
    public void writeToNBT (NBTTagCompound tag) {
        super.writeToNBT(tag);

        if (doorType != 0)
            tag.setByte("doorType", (byte)doorType);
    }
}
