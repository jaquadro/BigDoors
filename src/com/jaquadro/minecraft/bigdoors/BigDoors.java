package com.jaquadro.minecraft.bigdoors;

import com.jaquadro.minecraft.bigdoors.block.Door3x3;
import com.jaquadro.minecraft.bigdoors.block.movement.Door3x3Movement;
import com.jaquadro.minecraft.bigdoors.block.tile.Door3x3Tile;
import com.jaquadro.minecraft.bigdoors.core.CommonProxy;
import com.jaquadro.minecraft.bigdoors.item.ItemDoor3x3;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.malisis.doors.door.DoorRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWood;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

@Mod(modid = BigDoors.MOD_ID, name = BigDoors.MOD_NAME, version = BigDoors.MOD_VERSION, dependencies = "required-after:malisiscore; required-after:malisisdoors")
public class BigDoors
{
    public static final String MOD_ID = "BigDoors";
    public static final String MOD_NAME = "Big Doors";
    public static final String MOD_VERSION = "@VERSION@";
    public static final String SOURCE_PATH = "com.jaquadro.minecraft.bigdoors.";

    private static Block door3x3;

    @Mod.Instance(MOD_ID)
    public static BigDoors instance;

    @SidedProxy(clientSide = SOURCE_PATH + "core.ClientProxy", serverSide = SOURCE_PATH + "core.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit (FMLPreInitializationEvent event) {
        DoorRegistry.registerMovement("door3x3", new Door3x3Movement());

        door3x3 = new Door3x3();
        GameRegistry.registerBlock(door3x3, ItemDoor3x3.class, "door3x3");
        GameRegistry.registerTileEntity(Door3x3Tile.class, "door3x3");

        proxy.init();
    }

    @Mod.EventHandler
    public void init (FMLInitializationEvent event) {
        for (int i = 0; i < BlockWood.field_150096_a.length; i++) {
            GameRegistry.addRecipe(new ItemStack(door3x3, 1, i), "xx", "yy", 'x', Items.wooden_door, 'y', new ItemStack(Blocks.planks, 1, i));
        }
    }
}
