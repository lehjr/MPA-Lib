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

package com.github.lehjr.mpalib.client.model.helper;

import com.github.lehjr.mpalib.basemod.MPALibLogger;
import com.github.lehjr.mpalib.math.Colour;
import com.google.common.collect.ImmutableList;
import forge.MPAOBJLoader;
import forge.MPAOBJModel;
import forge.OBJBakedCompositeModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;
import net.minecraftforge.client.model.pipeline.VertexTransformer;
import net.minecraftforge.common.model.TransformationHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public class ModelHelper {
    public static TransformationMatrix get(float transformX, float transformY, float transformZ, float angleX, float angleY, float angleZ, float scaleX, float scaleY, float scaleZ) {
        // (@Nullable Vector3f translationIn,
        // @Nullable Quaternion rotationLeftIn,
        // @Nullable Vector3f scaleIn,
        // @Nullable Quaternion rotationRightIn)


        return new TransformationMatrix(
                // Transform
                new Vector3f(transformX / 16, transformY / 16, transformZ / 16),
                // Angles
                TransformationHelper.quatFromXYZ(new Vector3f(angleX, angleY, angleZ), true),

                // Scale
                new Vector3f(scaleX, scaleY, scaleZ),
                null);
    }

    public static TransformationMatrix get(float transformX, float transformY, float transformZ, float angleX, float angleY, float angleZ, float scale) {
        return get(transformX, transformY, transformZ, angleX, angleY, angleZ, scale, scale, scale);
    }

//    /**
//     * gets the texture from the texture map. Registers the texture if not already registered
//     */
//    private enum DefaultTextureGetter implements Function<ResourceLocation, TextureAtlasSprite> {
//        INSTANCE;
//
//        @Override
//        public TextureAtlasSprite apply(ResourceLocation location) {
//            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureMap().getSprite(location);
//            if (sprite == MissingTextureSprite.func_217790_a()) {
//                sprite = Minecraft.getInstance().getTextureMap().getSprite(location);
//            }
//            return sprite;
//        }
//    }

    /**
     * Get the default texture getter the models will be baked with.
     */
    public static Function<ResourceLocation, TextureAtlasSprite> defaultTextureGetter(ResourceLocation location) {
        return Minecraft.getInstance().getAtlasSpriteGetter(location);
    }

    @Nullable
    public static MPAOBJModel getOBJModel(ResourceLocation location, int attempt) {
        MPAOBJModel model;
        try {
            model = MPAOBJLoader.INSTANCE.loadModel(
                    new MPAOBJModel.ModelSettings(location, true, false, true, true, null));
        } catch (Exception e) {
            if (attempt < 6) {
                model = getOBJModel(location, attempt + 1);
                MPALibLogger.logError("Model loading failed on attempt #" + attempt + "  :( " + location.toString());
            } else {
                model = null;
                MPALibLogger.logError("Failed to load model. " + e);
            }
        }
        return model;
    }

    @Nullable
    public static OBJBakedCompositeModel loadBakedModel(IModelConfiguration owner,
                                                        ModelBakery bakery,
                                                        Function<RenderMaterial, TextureAtlasSprite> spriteGetter,
                                                        IModelTransform modelTransform,
                                                        ItemOverrideList overrides,
                                                        ResourceLocation modelLocation) {
        MPAOBJModel model = getOBJModel(modelLocation, 0);

        if (model != null) {
            OBJBakedCompositeModel bakedModel = model.bake(
                    owner,
                    bakery,
                    spriteGetter,
                    modelTransform,
                    overrides,
                    modelLocation);
            return bakedModel;
        }
        return null;
    }

    /*
     * Here we can color the quads or change the transform using the setup below.
     * This is better than changing material colors for Wavefront models because it means that you can use a single material for the entire model
     * instead of unique ones for each group. It also means you don't necessarily need a Wavefront model.
     */
    public static List<BakedQuad> getColouredQuadsWithGlowAndTransform(List<BakedQuad> quadList, Colour colour, final TransformationMatrix transform, boolean glow) {
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
        quadList.forEach(quad -> builder.add(colouredQuadWithGlowAndTransform(colour, quad, !glow, transform)));
        return builder.build();
    }

    public static BakedQuad colouredQuadWithGlowAndTransform(Colour colour, BakedQuad quad, boolean applyDifuse, TransformationMatrix transform) {
        QuadTransformer transformer = new QuadTransformer(colour, transform, quad.func_187508_a(), applyDifuse);
        quad.pipe(transformer);
        return transformer.build();
    }


    public static List<BakedQuad> getColoredQuadsWithGlow(List<BakedQuad> quadList, Colour color, boolean glow) {
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
        quadList.forEach(quad -> builder.add(colorQuad(color, quad, !glow)));
        return builder.build();
    }

    public static List<BakedQuad> getColoredQuads(List<BakedQuad> quadList, Colour color) {
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
        for (BakedQuad quad : quadList) {
            builder.add(colorQuad(color, quad, quad.func_239287_f_() /*.shouldApplyDiffuseLighting()*/));
        }
        return builder.build();
    }

    public static BakedQuad colorQuad(Colour color, BakedQuad quad, boolean applyDifuse) {
        QuadTransformer transformer = new QuadTransformer(color, quad.func_187508_a(), applyDifuse);
        quad.pipe(transformer);
        return transformer.build();
    }

    // see TRSRTransformer as example
    private static class QuadTransformer extends VertexTransformer {
        Colour colour;
        Boolean applyDiffuse;
        TransformationMatrix transform;

        public QuadTransformer(Colour colour, TextureAtlasSprite texture, boolean applyDiffuse) {
            super(new BakedQuadBuilder(texture));
            this.colour = colour;
            this.applyDiffuse = applyDiffuse;
        }

        public QuadTransformer(Colour colour, final TransformationMatrix transform, TextureAtlasSprite texture, boolean applyDiffuse) {
            super(new BakedQuadBuilder(texture));
            this.transform = transform;
            this.colour = colour;
            this.applyDiffuse = applyDiffuse;
        }

        @Override
        public void put(int element, float... data) {
            VertexFormatElement.Usage usage = parent.getVertexFormat().getElements().get(element).getUsage();
            // change color
            if (colour != null && usage == VertexFormatElement.Usage.COLOR && data.length >= 4) {
                data[0] = colour.r;
                data[1] = colour.g;
                data[2] = colour.b;
                data[3] = colour.a;
                super.put(element, data);
                // transform normals and position
            } else if (transform != null && usage == VertexFormatElement.Usage.POSITION && data.length >= 4) {
                Vector4f pos = new Vector4f(data[0], data[1], data[2], data[3]);
                transform.transformPosition(pos);
                data[0] = pos.getX();
                data[1] = pos.getY();
                data[2] = pos.getZ();
                data[3] = pos.getW();
                parent.put(element, data);
            } else {
                super.put(element, data);
            }
        }

        @Override
        public void setApplyDiffuseLighting(boolean diffuse) {
            super.setApplyDiffuseLighting(applyDiffuse != null ? applyDiffuse : diffuse);
        }

        public BakedQuad build() {
            return ((BakedQuadBuilder) parent).build();
        }
    }
}