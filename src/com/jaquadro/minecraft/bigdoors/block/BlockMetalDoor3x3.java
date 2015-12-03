package com.jaquadro.minecraft.bigdoors.block;

import com.jaquadro.minecraft.bigdoors.BigDoors;
import com.jaquadro.minecraft.bigdoors.block.tile.MetalDoor3x3Tile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockMetalDoor3x3 extends Door3x3
{
    public static final String[] names = new String[] {"iron", "rusty"};

    public BlockMetalDoor3x3 () {
        super(Material.iron);

        setHardness(5.0f);
        setResistance(200.0f);
        setStepSound(soundTypeMetal);
        setUnlocalizedName("metalDoor3x3");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons (IIconRegister register) {
        icons = new IIcon[names.length];

        for (int i = 0; i < icons.length; i++)
            icons[i] = register.registerIcon(BigDoors.MOD_ID + ":doors_" + names[i] + "_3x3");
    }

    @Override
    public TileEntity createNewTileEntity (World world, int metadata) {
        return new MetalDoor3x3Tile();
    }
}
