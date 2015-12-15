package com.jaquadro.minecraft.bigdoors.block.render;

import com.jaquadro.minecraft.bigdoors.block.BlockMetalDoor3x3;
import com.jaquadro.minecraft.bigdoors.block.Door3x3;
import com.jaquadro.minecraft.bigdoors.block.tile.Door3x3Tile;
import com.jaquadro.minecraft.bigdoors.block.tile.MetalDoor3x3Tile;
import net.malisis.core.renderer.MalisisRenderer;
import net.malisis.core.renderer.RenderParameters;
import net.malisis.core.renderer.RenderType;
import net.malisis.core.renderer.animation.Animation;
import net.malisis.core.renderer.animation.AnimationRenderer;
import net.malisis.core.renderer.element.Shape;
import net.malisis.core.renderer.element.shape.Cube;
import net.malisis.core.renderer.model.MalisisModel;
import net.malisis.doors.block.Door;
import net.minecraft.util.EnumFacing;
import org.lwjgl.opengl.GL11;

public class Door3x3Renderer extends MalisisRenderer
{
    private Door3x3 block;
    private MalisisModel model;
    private Shape doorLeft;
    private Shape doorRight;
    private RenderParameters rp;
    private AnimationRenderer ar = new AnimationRenderer();
    private Door3x3Tile tile;

    private EnumFacing direction;

    public Door3x3Renderer () {
        registerFor(Door3x3Tile.class);
        registerFor(MetalDoor3x3Tile.class);
        getBlockDamage = true;
    }

    @Override
    protected void initialize () {
        doorLeft = new Cube();
        doorLeft.setSize(0.5f, 3f, Door.DOOR_WIDTH / 3.01f);
        doorLeft.interpolateUV();
        doorLeft.setSize(1.5f, 3f, Door.DOOR_WIDTH);
        doorLeft.scale(1, 1, 0.995f);
        doorLeft.translate(-1f, 0, 1 - Door.DOOR_WIDTH);

        doorRight = new Cube();
        doorRight.setSize(1, 3, Door.DOOR_WIDTH / 3.01f);
        doorRight.limit(0.5f, 0, 0, 1, 3, Door.DOOR_WIDTH);
        doorRight.interpolateUV();
        doorRight.setSize(1.5f, 3f, Door.DOOR_WIDTH);
        doorRight.scale(1, 1, 0.995f);
        doorRight.translate(0, 0, 1 - Door.DOOR_WIDTH);

        model = new MalisisModel();
        model.addShape("left", doorLeft);
        model.addShape("right", doorRight);

        model.storeState();

        rp = new RenderParameters();
        rp.useBlockBounds.set(false);
        rp.interpolateUV.set(false);
    }

    @Override
    public void render () {
        this.block = (Door3x3)super.block;
        if (super.tileEntity == null)
            return;

        if (renderType == RenderType.BLOCK)
            return;

        tile = (Door3x3Tile) super.tileEntity;
        direction = tile.getDirection();
        setup();

        if (renderType == RenderType.TILE_ENTITY)
            renderTileEntity();
    }

    private void renderTileEntity () {
        ar.setStartTime(tile.getTimer().getStart());

        if (tile.getMovement() != null) {
            Animation[] anims = tile.getMovement().getAnimations(tile, model, rp);
            ar.animate(anims);
        }

        next(GL11.GL_POLYGON);
        if (block.getClass() == Door3x3.class)
            rp.icon.set(((Door3x3.Door3x3IconProvider) block.getIconProvider()).getDoorIcon());
        else if (block.getClass() == BlockMetalDoor3x3.class)
            rp.icon.set(((BlockMetalDoor3x3.Door3x3IconProvider) block.getIconProvider()).getDoorIcon());
        drawShape(doorLeft, rp);
        drawShape(doorRight, rp);
    }

    private void setup () {
        model.resetState();
        if (direction == EnumFacing.NORTH)
            model.rotate(180, 0, 1, 0, 0, 0, 0);
        else if (direction == EnumFacing.WEST)
            model.rotate(-90, 0, 1, 0, 0, 0, 0);
        else if (direction == EnumFacing.EAST)
            model.rotate(90, 0, 1, 0, 0, 0, 0);

        rp.brightness.set(block.getMixedBrightnessForBlock(world, pos));
    }
}
