/*
 * Copyright (c) 2019 MachineMuse, Lehjr
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.lehjr.mpalib.util.client.gui.geometry;

import com.github.lehjr.mpalib.util.math.Colour;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

import static net.minecraft.client.renderer.RenderType.makeType;

public class DrawableCircle {
    protected static final RenderState.WriteMaskState COLOR_WRITE = new RenderState.WriteMaskState(true, false);
    protected static final RenderState.TransparencyState NO_TRANSPARENCY = new RenderState.TransparencyState("no_transparency", () -> {
        RenderSystem.disableBlend();
    }, () -> {
    });
    protected static final RenderState.ShadeModelState SHADE_DISABLED = new RenderState.ShadeModelState(false);
    private static final RenderType PLASMA_BALL = makeType(
            "plasma",
            DefaultVertexFormats.POSITION_COLOR,
            GL11.GL_TRIANGLE_FAN,
256,
            false,
            true,
            RenderType.State.getBuilder().writeMask(COLOR_WRITE).shadeModel(SHADE_DISABLED).transparency(NO_TRANSPARENCY).build(false));

    public static final float detail = 4;
    protected static FloatBuffer points;
    protected final FloatBuffer colour;

    public DrawableCircle(Colour c1, Colour c2) {
        if (points == null) {
            FloatBuffer arcPoints = GradientAndArcCalculator.getArcPoints(0, (float)(Math.PI * 2 + 0.0001), detail, 0F, 0F, 0F);
            points = BufferUtils.createFloatBuffer(arcPoints.limit() + 4);
            points.put(new float[]{0, 0});
            points.put(arcPoints);
            arcPoints.rewind();
            points.put(arcPoints.get());
            points.put(arcPoints.get());
            points.flip();
        }
        FloatBuffer colourPoints = GradientAndArcCalculator.getColourGradient(c1, c1, points.limit() / 3);
        colour = BufferUtils.createFloatBuffer(colourPoints.limit() + 4); // space for rgba of c2
        colour.put(c2.asArray());
        colour.put(colourPoints);
        colour.flip();
    }

    public void draw(MatrixStack matrixStack, double radius, double x, double y, float zLevel) {
        float ratio = (System.currentTimeMillis() % 2000) / 2000.0F;
        colour.rewind();
        points.rewind();
        RenderSystem.pushMatrix();
        RenderSystem.translated(x, y, 0);
        RenderSystem.scaled(radius / detail, radius / detail, 1.0);
        RenderSystem.rotatef((float) (-ratio * 360.0), 0, 0, 1);

        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.defaultBlendFunc();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);

        while (points.hasRemaining() && colour.hasRemaining()) {
            buffer.pos(matrixStack.getLast().getMatrix(), points.get(), points.get(), zLevel).color(colour.get(), colour.get(), colour.get(), colour.get()).endVertex();
        }
        tessellator.draw();

        RenderSystem.shadeModel(GL11.GL_FLAT);
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableTexture();

        RenderSystem.popMatrix();
    }

    public void draw(float radius, double x, double y, float z, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        float ratio = (System.currentTimeMillis() % 2000) / 2000.0F;
       colour.rewind();
        points.rewind();

        matrixStackIn.push();
        matrixStackIn.translate(x, y, 0);
        matrixStackIn.scale(radius / detail, radius / detail, 1.0F);

        matrixStackIn.rotate(Vector3f.ZP.rotation(-ratio * 360.0f));
        IVertexBuilder vertBuffer = bufferIn.getBuffer(PLASMA_BALL);

        while (points.hasRemaining() && colour.hasRemaining()) {
            vertBuffer.pos(matrixStackIn.getLast().getMatrix(), points.get(), points.get(), z).color(colour.get(), colour.get(), colour.get(), colour.get()).endVertex();
        }
        matrixStackIn.pop();
    }
}