package com.jaquadro.minecraft.bigdoors.item;

import com.jaquadro.minecraft.bigdoors.BigDoors;
import com.jaquadro.minecraft.bigdoors.block.tile.Door3x3Tile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWood;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemDoor3x3 extends ItemMultiTexture
{
    @SideOnly(Side.CLIENT)
    protected IIcon[] icons;

    public ItemDoor3x3 (Block block) {
        this(block, BlockWood.field_150096_a);
    }

    protected ItemDoor3x3 (Block block, String[] names) {
        super(block, block, names);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamage (int damage) {
        return icons[damage % icons.length];
    }

    @Override
    public boolean placeBlockAt (ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        if (!super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata))
            return false;

        Door3x3Tile tile = (Door3x3Tile)world.getTileEntity(x, y, z);
        if (tile != null)
            tile.setDoorType(metadata);

        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons (IIconRegister register) {
        icons = new IIcon[BlockWood.field_150096_a.length];
        for (int i = 0; i < icons.length; i++)
            icons[i] = register.registerIcon(BigDoors.MOD_ID + ":doors_" + BlockWood.field_150096_a[i]);
    }
}
