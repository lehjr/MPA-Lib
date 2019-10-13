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

package com.github.lehjr.mpalib.client.gui.hud.meters;

import com.github.lehjr.mpalib.client.render.MPALibRenderer;
import com.github.lehjr.mpalib.client.render.RenderState;
import com.github.lehjr.mpalib.math.Colour;
import org.lwjgl.opengl.GL11;

public class EnergyMeter extends HeatMeter {
    public Colour getColour() {
        //return Colour.WHITE;
        return Colour.PURPLE;
    }

    public void draw(double xpos, double ypos, double value) {
        float lineWidth = GL11.glGetFloat(GL11.GL_LINE_WIDTH);
        boolean smooth  = GL11.glIsEnabled(GL11.GL_LINE_SMOOTH);

        super.draw(xpos, ypos, value);
        GL11.glLineWidth(0.5f);
        if (value < 0.0001) {
            Colour.RED.doGL();
        } else if (Math.random() / value < 1) {
            RenderState.texturelessOn();
            MPALibRenderer.drawMPDLightning(xpos + xsize * value, ypos + ysize * (Math.random() / 2 + 0.25), 1,
                    xpos, ypos + ysize * (Math.random() / 2 + 0.25), 1, Colour.WHITE, 4, 1);
            RenderState.texturelessOff();
        }
        if (!smooth)
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(lineWidth);
    }
}