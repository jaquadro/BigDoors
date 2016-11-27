package com.jaquadro.minecraft.bigdoors.block.sound;

import com.jaquadro.minecraft.bigdoors.BigDoors;
import net.malisis.core.registry.MalisisRegistry;
import net.malisis.doors.DoorState;
import net.malisis.doors.sound.IDoorSound;
import net.minecraft.util.SoundEvent;

public class RustyDoorSound implements IDoorSound
{
    private SoundEvent opening;
    private SoundEvent closing;
    private SoundEvent closed;

    @Override
    public void register () {
        opening = MalisisRegistry.registerSound(BigDoors.MOD_ID, "rustydoor_opening");
        closing = MalisisRegistry.registerSound(BigDoors.MOD_ID, "rustydoor_closing");
        closed = MalisisRegistry.registerSound(BigDoors.MOD_ID, "rustydoor_closed");
    }

    @Override
    public SoundEvent getSound (DoorState doorState) {
        switch (doorState) {
            case OPENING:
                return opening;
            case CLOSING:
                return closing;
            case CLOSED:
                return closed;
            default:
                return null;
        }
    }
}
