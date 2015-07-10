package com.jaquadro.minecraft.bigdoors.core;

import com.jaquadro.minecraft.bigdoors.block.Door3x3;
import com.jaquadro.minecraft.bigdoors.block.render.Door3x3Renderer;
import com.jaquadro.minecraft.bigdoors.block.tile.Door3x3Tile;

public class ClientProxy extends CommonProxy
{
    @Override
    public void init () {
        Door3x3Renderer render = new Door3x3Renderer();
        render.registerFor(Door3x3.class, Door3x3Tile.class);
    }
}
