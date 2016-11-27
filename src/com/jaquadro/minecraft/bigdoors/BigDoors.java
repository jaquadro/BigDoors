package com.jaquadro.minecraft.bigdoors;

import com.jaquadro.minecraft.bigdoors.block.BlockMetalDoor3x3;
import com.jaquadro.minecraft.bigdoors.block.Door3x3;
import com.jaquadro.minecraft.bigdoors.block.movement.Door3x3Movement;
import com.jaquadro.minecraft.bigdoors.block.sound.RustyDoorSound;
import com.jaquadro.minecraft.bigdoors.core.CommonProxy;
import net.malisis.core.IMalisisMod;
import net.malisis.core.configuration.Settings;
import net.malisis.doors.DoorRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = BigDoors.MOD_ID, name = BigDoors.MOD_NAME, version = BigDoors.MOD_VERSION, dependencies = "required-after:malisiscore; required-after:malisisdoors")
public class BigDoors implements IMalisisMod
{
    public static final String MOD_ID = "bigdoors";
    public static final String MOD_NAME = "Big Doors";
    public static final String MOD_VERSION = "@VERSION@";
    public static final String SOURCE_PATH = "com.jaquadro.minecraft.bigdoors.";

    @Override
    public String getModId () {
        return MOD_ID;
    }

    @Override
    public String getName () {
        return MOD_NAME;
    }

    @Override
    public String getVersion () {
        return MOD_VERSION;
    }

    @Override
    public Settings getSettings () {
        return null;
    }

    @Mod.Instance(MOD_ID)
    public static BigDoors instance;

    @SidedProxy(clientSide = SOURCE_PATH + "core.ClientProxy", serverSide = SOURCE_PATH + "core.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit (FMLPreInitializationEvent event) {
        DoorRegistry.registerMovement("door3x3", new Door3x3Movement());
        DoorRegistry.registerSound("rustyDoor", new RustyDoorSound());

        Registers.init();

        proxy.init();
    }

    @Mod.EventHandler
    public void init (FMLInitializationEvent event) { }

    public static class Blocks {
        public static Door3x3 bigDoorOak;
        public static Door3x3 bigDoorBirch;
        public static Door3x3 bigDoorSpruce;
        public static Door3x3 bigDoorJungle;
        public static Door3x3 bigDoorAcacia;
        public static Door3x3 bigDoorDarkOak;

        public static BlockMetalDoor3x3 bigDoorIron;
        public static BlockMetalDoor3x3 bigDoorRusty;

        public Blocks() { }
    }
}
