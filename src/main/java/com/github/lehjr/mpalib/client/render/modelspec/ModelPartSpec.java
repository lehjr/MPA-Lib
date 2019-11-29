/*
 * MPA-Lib (Formerly known as Numina)
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

package com.github.lehjr.mpalib.client.render.modelspec;

import com.github.lehjr.mpalib.basemod.MPALIbConstants;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Objects;

/**
 * Ported to Java by lehjr on 11/8/16.
 */
@SideOnly(Side.CLIENT)
public class ModelPartSpec extends PartSpecBase {
    private final boolean defaultglow;

    public ModelPartSpec(final ModelSpec modelSpec,
                         final SpecBinding binding,
                         final String partName,
                         final Integer defaultcolourindex,
                         final Boolean defaultglow) {
        super(modelSpec, binding, partName, defaultcolourindex);
        this.defaultglow = (defaultglow != null) ? defaultglow : false;
    }

    @Override
    public String getDisaplayName() {
        return new StringBuilder("model.")
                .append(this.spec.getOwnName())
                .append(".")
                .append(this.partName)
                .append(".partName")
                .toString();
    }

    public boolean getGlow() {
        return this.defaultglow;
    }

    public boolean getGlow(NBTTagCompound nbt) {
        return nbt.hasKey(MPALIbConstants.TAG_GLOW) ? nbt.getBoolean(MPALIbConstants.TAG_GLOW) : this.defaultglow;
    }

    public void setGlow(NBTTagCompound nbt, boolean g) {
        if (g == this.defaultglow) nbt.removeTag(MPALIbConstants.TAG_GLOW);
        else nbt.setBoolean(MPALIbConstants.TAG_GLOW, g);
    }

    public List<BakedQuad> getQuads() {
        return ((ModelSpec) (this.spec)).getModel().getQuadsforPart(this.partName);
    }

    public NBTTagCompound multiSet(NBTTagCompound nbt, Integer colourIndex, Boolean glow) {
        super.multiSet(nbt, colourIndex);
        this.setGlow(nbt, (glow != null) ? glow : false);
        return nbt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ModelPartSpec that = (ModelPartSpec) o;
        return defaultglow == that.defaultglow;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), defaultglow);
    }
}