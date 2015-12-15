package com.jaquadro.minecraft.bigdoors.block.tile;

import com.jaquadro.minecraft.bigdoors.block.sound.RustyDoorSound;
import net.malisis.doors.DoorRegistry;
import net.minecraft.nbt.NBTTagCompound;

public class MetalDoor3x3Tile extends Door3x3Tile
{
    public MetalDoor3x3Tile () {
        getDescriptor().setSound(DoorRegistry.getSound(RustyDoorSound.class));
    }

    @Override
    public void readFromNBT (NBTTagCompound tag) {
        super.readFromNBT(tag);
    }

    @Override
    public void writeToNBT (NBTTagCompound tag) {
        super.writeToNBT(tag);
    }
}
