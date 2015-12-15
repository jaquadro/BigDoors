package com.jaquadro.minecraft.bigdoors.block;

import com.jaquadro.minecraft.bigdoors.BigDoors;
import com.jaquadro.minecraft.bigdoors.block.tile.MetalDoor3x3Tile;
import net.malisis.core.renderer.icon.MalisisIcon;
import net.malisis.core.renderer.icon.provider.IBlockIconProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMetalDoor3x3 extends Door3x3
{
    public enum Type {
        IRON("doors_iron_3x3", Items.oak_door),
        RUSTY("doors_rusty_3x3", Items.birch_door);

        public String name;
        public Item door;

        private Type (String name, Item door) {
            this.name = name;
            this.door = door;
        }
    }

    //public static final String[] names = new String[] {"iron", "rusty"};
    private Type type;

    public BlockMetalDoor3x3 (Type type) {
        this(Material.iron);

        this.type = type;
        setName(type.name);
    }

    protected BlockMetalDoor3x3 (Material material) {
        super(material);

        setHardness(5.0f);
        setResistance(200.0f);
        setStepSound(soundTypeMetal);
        setName("metalDoor3x3");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void createIconProvider (Object object) {
        iconProvider = new Door3x3IconProvider(type);
    }

    /*@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons (IIconRegister register) {
        icons = new IIcon[names.length];

        for (int i = 0; i < icons.length; i++)
            icons[i] = register.registerIcon(BigDoors.MOD_ID + ":doors_" + names[i] + "_3x3");
    }*/

    @Override
    public TileEntity createNewTileEntity (World world, int metadata) {
        return new MetalDoor3x3Tile();
    }

    public static class Door3x3IconProvider implements IBlockIconProvider
    {
        MalisisIcon itemIcon;
        MalisisIcon doorIcon;

        public Door3x3IconProvider (Type type) {
            itemIcon = new MalisisIcon(BigDoors.MOD_ID + ":items/" + type.name + "_item");
            doorIcon = new MalisisIcon(BigDoors.MOD_ID + ":blocks/" + type.name);
        }

        @Override
        public void registerIcons (TextureMap textureMap) {
            itemIcon = itemIcon.register(textureMap);
            doorIcon = doorIcon.register(textureMap);
        }

        @Override
        public MalisisIcon getIcon () {
            return itemIcon;
        }

        public MalisisIcon getDoorIcon () {
            return doorIcon;
        }
    }
}
