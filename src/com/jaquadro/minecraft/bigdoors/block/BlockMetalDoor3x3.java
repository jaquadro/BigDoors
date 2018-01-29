package com.jaquadro.minecraft.bigdoors.block;

import com.jaquadro.minecraft.bigdoors.BigDoors;
import com.jaquadro.minecraft.bigdoors.block.tile.MetalDoor3x3Tile;
import net.malisis.core.MalisisCore;
import net.malisis.core.renderer.icon.Icon;
import net.malisis.core.renderer.icon.provider.IIconProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMetalDoor3x3 extends Door3x3
{
    public enum Type {
        IRON("iron_doors_33", Items.OAK_DOOR),
        RUSTY("rusty_iron_doors_33", Items.BIRCH_DOOR);

        public String name;
        public Item door;

        Type (String name, Item door) {
            this.name = name;
            this.door = door;
        }
    }

    public BlockMetalDoor3x3 (Type type) {
        this(Material.IRON);

        setName(type.name);

        if (MalisisCore.isClient())
            addComponent(Door3x3IconProvider.get(type));
    }

    protected BlockMetalDoor3x3 (Material material) {
        super(material);

        setHardness(5.0f);
        setResistance(200.0f);
        setSoundType(SoundType.METAL);
        setName("metalDoor3x3");
    }

    @Override
    public TileEntity createNewTileEntity (World world, int metadata) {
        return new MetalDoor3x3Tile();
    }

    public static class Door3x3IconProvider implements IIconProvider
    {
        Icon itemIcon;
        Icon doorIcon;

        public Door3x3IconProvider (Type type) {
            itemIcon = new Icon(BigDoors.MOD_ID + ":items/" + type.name + "_item");
            doorIcon = new Icon(BigDoors.MOD_ID + ":blocks/" + type.name);
        }

        @Override
        public Icon getIcon () {
            return itemIcon;
        }

        public Icon getDoorIcon () {
            return doorIcon;
        }

        public static Door3x3IconProvider get (Type type) {
            return new Door3x3IconProvider(type);
        }
    }
}
