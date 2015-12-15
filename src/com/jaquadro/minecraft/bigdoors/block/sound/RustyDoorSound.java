package com.jaquadro.minecraft.bigdoors.block.sound;

import com.jaquadro.minecraft.bigdoors.BigDoors;
import net.malisis.doors.DoorState;
import net.malisis.doors.sound.IDoorSound;

public class RustyDoorSound implements IDoorSound
{
    @Override
    public String getSoundPath (DoorState doorState) {
        switch (doorState) {
            case OPENING:
                return BigDoors.MOD_ID + ":rustydoor_opening";
            case CLOSING:
                return BigDoors.MOD_ID + ":rustydoor_closing";
            case CLOSED:
                return BigDoors.MOD_ID + ":rustydoor_closed";
            default:
                return null;
        }
    }
}
