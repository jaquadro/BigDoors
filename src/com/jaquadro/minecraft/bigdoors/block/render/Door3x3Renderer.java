package com.jaquadro.minecraft.bigdoors.block.render;

import com.jaquadro.minecraft.bigdoors.block.tile.Door3x3Tile;
import net.malisis.core.renderer.MalisisRenderer;
import net.malisis.core.renderer.RenderParameters;
import net.malisis.core.renderer.RenderType;
import net.malisis.core.renderer.animation.Animation;
import net.malisis.core.renderer.animation.AnimationRenderer;
import net.malisis.core.renderer.element.Shape;
import net.malisis.core.renderer.element.Vertex;
import net.malisis.core.renderer.element.shape.Cube;
import net.malisis.core.renderer.model.MalisisModel;
import net.malisis.doors.door.block.Door;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class Door3x3Renderer extends MalisisRenderer
{
    private MalisisModel model;
    private Shape doorLeft;
    private Shape doorRight;
    private AnimationRenderer ar = new AnimationRenderer();
    private Door3x3Tile tile;
    private boolean overrideAlpha;
    private int overrideAlphaValue;

    private ForgeDirection direction;

    public Door3x3Renderer () {
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
        if (super.tileEntity == null)
            return;

        //rp.interpolateUV.set(true);

        if (renderType == RenderType.ISBRH_WORLD)
            return;

        tile = (Door3x3Tile) super.tileEntity;
        direction = Door.intToDir(tile.getDirection());
        setup();

        if (renderType == RenderType.TESR_WORLD)
            renderTileEntity();
    }

    private void renderTileEntity () {
        ar.setStartTime(tile.getTimer().getStart());

        if (tile.getMovement() != null) {
            Animation[] anims = tile.getMovement().getAnimations(tile, model, rp);
            ar.animate(anims);
        }

        next(GL11.GL_POLYGON);
        rp.icon.reset();
        drawShape(doorLeft, rp);
        drawShape(doorRight, rp);
    }

    private void setup () {
        model.resetState();
        if (direction == ForgeDirection.SOUTH)
            model.rotate(180, 0, 1, 0, 0, 0, 0);
        else if (direction == ForgeDirection.EAST)
            model.rotate(-90, 0, 1, 0, 0, 0, 0);
        else if (direction == ForgeDirection.WEST)
            model.rotate(90, 0, 1, 0, 0, 0, 0);

        rp.brightness.set(block.getMixedBrightnessForBlock(world, x, y, z));
    }

    @Override
    public void renderDestroyProgress()
    {
        if (destroyBlockProgress != null)
            overrideTexture = damagedIcons[destroyBlockProgress.getPartialBlockDamage()];

        if (renderType == RenderType.TESR_WORLD) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            OpenGlHelper.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR, GL11.GL_ONE, GL11.GL_ZERO);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        }

        overrideAlpha = true;
        overrideAlphaValue = 128;

        render();

        overrideAlpha = false;
    }

    @Override
    public boolean shouldRender3DInInventory (int modelId) {
        return false;
    }

    protected void drawVertex(Vertex vertex, int number)
    {
        if (vertex == null)
            vertex = new Vertex(0, 0, 0);

        // brightness
        int brightness = calcVertexBrightness(vertex, (int[][]) params.aoMatrix.get(number));
        vertex.setBrightness(brightness);

        // color
        int color = calcVertexColor(vertex, (int[][]) params.aoMatrix.get(number));
        vertex.setColor(color);

        // alpha
        if (!params.usePerVertexAlpha.get())
            vertex.setAlpha(params.alpha.get());

        int alpha = overrideAlpha ? overrideAlphaValue : vertex.getAlpha();
        t.setColorRGBA_I(vertex.getColor(), alpha);
        t.setBrightness(vertex.getBrightness());

        if (params.useTexture.get())
            t.addVertexWithUV(vertex.getX(), vertex.getY(), vertex.getZ(), vertex.getU(), vertex.getV());
        else
            t.addVertex(vertex.getX(), vertex.getY(), vertex.getZ());

        vertexDrawn = true;
    }
}
