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

package com.github.lehjr.mpalib.client.render.modelspec;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.inventory.EntityEquipmentSlot;

import java.util.Arrays;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 2:09 AM, 29/04/13
 * <p>
 * Ported to Java by lehjr on 11/8/16.
 */
public enum MorphTarget {
    Head("HEAD", EntityEquipmentSlot.HEAD),
    Body("BODY", EntityEquipmentSlot.CHEST),
    RightArm("RIGHTARM", EntityEquipmentSlot.CHEST),
    LeftArm("LEFTARM", EntityEquipmentSlot.CHEST),
    RightLeg("RIGHTLEG", EntityEquipmentSlot.LEGS),
    LeftLeg("LEFTLEG", EntityEquipmentSlot.LEGS),
    RightFoot("RIGHTFOOT", EntityEquipmentSlot.FEET),
    LeftFoot("LEFTFOOT", EntityEquipmentSlot.FEET),

    /**
     * Note that these may be reversed and special checks are needed for rendering
     * hand-dependant models.
     */
    RightHand("RIGHTHAND", EntityEquipmentSlot.MAINHAND),
    Lefthand("LEFTHAND", EntityEquipmentSlot.OFFHAND);

    String name;
    EntityEquipmentSlot slot;

    MorphTarget(String name, EntityEquipmentSlot slot) {
        this.name = name;
        this.slot = slot;
    }

    public static MorphTarget getMorph(final String name) {
        return Arrays.stream(values()).filter(morph -> name.toUpperCase().equals(morph.name)).findAny().orElseGet(null);
    }

    public ModelRenderer apply(ModelBiped m) {
        switch (this) {
            case Head:
                return m.bipedHead;

            case Body:
                return m.bipedBody;

            case RightHand:
            case RightArm:
                return m.bipedRightArm;

            case Lefthand:
            case LeftArm:
                return m.bipedLeftArm;

            case RightFoot:
            case RightLeg:
                return m.bipedRightLeg;

            case LeftFoot:
            case LeftLeg:
                return m.bipedLeftLeg;

            default:
                return null;
        }
    }
}