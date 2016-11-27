package com.jaquadro.minecraft.bigdoors.block.tile;

import com.jaquadro.minecraft.bigdoors.block.sound.RustyDoorSound;
import net.malisis.core.util.syncer.Syncable;
import net.malisis.doors.DoorRegistry;
import net.minecraft.nbt.NBTTagCompound;

@Syncable("TileEntity")
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
    public NBTTagCompound writeToNBT (NBTTagCompound tag) {
        return super.writeToNBT(tag);
    }
}
