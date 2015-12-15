package com.jaquadro.minecraft.bigdoors;

import static com.jaquadro.minecraft.bigdoors.BigDoors.Blocks.*;

import com.jaquadro.minecraft.bigdoors.block.BlockMetalDoor3x3;
import com.jaquadro.minecraft.bigdoors.block.Door3x3;
import com.jaquadro.minecraft.bigdoors.block.tile.Door3x3Tile;
import com.jaquadro.minecraft.bigdoors.block.tile.MetalDoor3x3Tile;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Registers
{
    public static void init() {
        registerStandardDoors();
        registerMetalDoors();
    }

    private static void registerStandardDoors () {
        bigDoorOak = new Door3x3(Door3x3.Type.OAK);
        bigDoorOak.register();

        bigDoorBirch = new Door3x3(Door3x3.Type.BIRCH);
        bigDoorBirch.register();

        bigDoorSpruce = new Door3x3(Door3x3.Type.SPRUCE);
        bigDoorSpruce.register();

        bigDoorJungle = new Door3x3(Door3x3.Type.JUNGLE);
        bigDoorJungle.register();

        bigDoorAcacia = new Door3x3(Door3x3.Type.ACACIA);
        bigDoorAcacia.register();

        bigDoorDarkOak = new Door3x3(Door3x3.Type.DARK_OAK);
        bigDoorDarkOak.register();

        GameRegistry.registerTileEntity(Door3x3Tile.class, "door3x3");

        GameRegistry.addRecipe(new ItemStack(bigDoorOak), "xx", "yy", 'x', Items.oak_door, 'y', new ItemStack(Blocks.planks, 1, 0));
        GameRegistry.addRecipe(new ItemStack(bigDoorBirch), "xx", "yy", 'x', Items.birch_door, 'y', new ItemStack(Blocks.planks, 1, 1));
        GameRegistry.addRecipe(new ItemStack(bigDoorSpruce), "xx", "yy", 'x', Items.spruce_door, 'y', new ItemStack(Blocks.planks, 1, 2));
        GameRegistry.addRecipe(new ItemStack(bigDoorJungle), "xx", "yy", 'x', Items.jungle_door, 'y', new ItemStack(Blocks.planks, 1, 3));
        GameRegistry.addRecipe(new ItemStack(bigDoorAcacia), "xx", "yy", 'x', Items.acacia_door, 'y', new ItemStack(Blocks.planks, 1, 4));
        GameRegistry.addRecipe(new ItemStack(bigDoorDarkOak), "xx", "yy", 'x', Items.dark_oak_door, 'y', new ItemStack(Blocks.planks, 1, 5));
    }

    private static void registerMetalDoors () {
        bigDoorIron = new BlockMetalDoor3x3(BlockMetalDoor3x3.Type.IRON);
        bigDoorIron.register();

        bigDoorRusty = new BlockMetalDoor3x3(BlockMetalDoor3x3.Type.RUSTY);
        bigDoorRusty.register();

        GameRegistry.registerTileEntity(MetalDoor3x3Tile.class, "metalDoors3x3");

        GameRegistry.addRecipe(new ItemStack(bigDoorIron), "xx", "yy", 'x', Items.iron_door, 'y', Items.iron_ingot);
        GameRegistry.addRecipe(new ItemStack(bigDoorRusty, 1, 1), "xxx", "xyx", "xxx", 'x', Blocks.dirt, 'y', new ItemStack(bigDoorIron));
    }
}
