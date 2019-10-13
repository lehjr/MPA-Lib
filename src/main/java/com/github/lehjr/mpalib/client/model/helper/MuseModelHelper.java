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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.UnmodifiableIterator;
import com.github.lehjr.mpalib.basemod.MPALibLogger;
import com.github.lehjr.forge.obj.OBJPlusLoader;
import com.github.lehjr.forge.obj.OBJPlusModel;
import com.github.lehjr.mpalib.math.Colour;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.BasicState;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.client.model.pipeline.VertexTransformer;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.Models;
import net.minecraftforge.common.model.TRSRTransformation;

import javax.annotation.Nullable;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

public class MuseModelHelper {
    public static TRSRTransformation get(float transformX, float transformY, float transformZ, float angleX, float angleY, float angleZ, float scaleX, float scaleY, float scaleZ) {
        return new TRSRTransformation(
                // Transform
                new Vector3f(transformX / 16, transformY / 16, transformZ / 16),
                // Angles
                TRSRTransformation.quatFromXYZDegrees(new Vector3f(angleX, angleY, angleZ)),
                // Scale
                new Vector3f(scaleX, scaleY, scaleZ),
                null);
    }

    public static TRSRTransformation get(float transformX, float transformY, float transformZ, float angleX, float angleY, float angleZ, float scale) {
        return get(transformX, transformY, transformZ, angleX, angleY, angleZ, scale, scale, scale);
    }

    /**
     * gets the texture from the texture map. Registers the texture if not already registered
     */
    private enum DefaultTextureGetter implements Function<ResourceLocation, TextureAtlasSprite> {
        INSTANCE;

        @Override
        public TextureAtlasSprite apply(ResourceLocation location) {
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureMap().getSprite(location);
            if (sprite == MissingTextureSprite.func_217790_a()) {
                sprite = Minecraft.getInstance().getTextureMap().getSprite(location);
            }
            return sprite;
        }
    }

    /**
     * Get the default texture getter the models will be baked with.
     */
    public static Function<ResourceLocation, TextureAtlasSprite> defaultTextureGetter() {
        return DefaultTextureGetter.INSTANCE;
    }

    public static IModel getModel(ResourceLocation resource) {
        IModel model = null;
        try {
            model = OBJPlusLoader.INSTANCE.loadModel(resource);
            model = ((OBJPlusModel) model).process(ImmutableMap.of("flip-v", "true"));
        } catch (Exception e) {
            e.printStackTrace();
            MPALibLogger.logError("Model loading failed :( " + resource);
        }
        return model;
    }

    public static IModel getIModel(ResourceLocation location, int attempt) {
        IModel model;
        try {
            model = OBJPlusLoader.INSTANCE.loadModel(location);
            model = ((OBJPlusModel) model).process(ImmutableMap.of("flip-v", "true"));
        } catch (Exception e) {
            if (attempt < 6) {
                model = getIModel(location, attempt + 1);
                MPALibLogger.logError("Model loading failed on attempt #" + attempt + "  :( " + location.toString());
            } else {
                model = ModelLoaderRegistry.getMissingModel();
                MPALibLogger.logError("Failed to load model. " + e);
            }
        }
        return model;
    }

    //FIXME!!

    @Nullable
    public static IBakedModel loadBakedModel(ResourceLocation resource, IModelState state, ModelBakery bakery) {
        IModel model = getIModel(resource, 0);
        if (model != null) {
            IBakedModel bakedModel = model.bake(
                    bakery,
                    MuseModelHelper.defaultTextureGetter(),
                    new BasicState(state, false),
                    DefaultVertexFormats.ITEM);
            return bakedModel;
        }
        return null;
    }

    public List<BakedQuad> getQuadsByGroups(IBakedModel bakedModelIn, final List<String> visibleGroups, TRSRTransformation transformation) {
        List<BakedQuad> quads = null;

        if (bakedModelIn instanceof OBJModel.OBJBakedModel) {
            try {
                OBJModel obj = ((OBJModel.OBJBakedModel) bakedModelIn).getModel();

                // ModelState for handling visibility of each group.
                IModelState modelState = part -> {
                    if (part.isPresent()) {
                        UnmodifiableIterator<String> parts = Models.getParts(part.get());

                        if (parts.hasNext()) {
                            String name = parts.next();

                            if (!parts.hasNext() && visibleGroups.contains(name)) {
                                // Return Absent for NOT invisible group.
                                return Optional.empty();
                            } else {
                                // Return Present for invisible group.
                                return Optional.of(transformation);
                            }
                        }
                    }
                    return Optional.empty();
                };

                // Bake model of visible groups.
                IBakedModel bakedModel = obj.bake(null, ModelLoader.defaultTextureGetter(), new BasicState(modelState, false), DefaultVertexFormats.ITEM);

                quads = bakedModel.getQuads(null, null, new Random());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (quads != null) {
            return quads;
        } else {
            return Collections.emptyList();
        }
    }

    /*
     * Here we can color the quads or change the transform using the setup below.
     * This is better than changing material colors for Wavefront models because it means that you can use a single material for the entire model
     * instead of unique ones for each group. It also means you don't necessarily need a Wavefront model.
     */
    public static List<BakedQuad> getColouredQuadsWithGlowAndTransform(List<BakedQuad> quadList, Colour colour, final TRSRTransformation transform, boolean glow) {
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
        quadList.forEach(quad -> builder.add(colouredQuadWithGlowAndTransform(colour, quad, !glow, transform)));
        return builder.build();
    }

    public static BakedQuad colouredQuadWithGlowAndTransform(Colour colour, BakedQuad quad, boolean applyDifuse, TRSRTransformation transform) {
        QuadTransformer transformer = new QuadTransformer(colour, transform, quad.getFormat(), applyDifuse);
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
            builder.add(colorQuad(color, quad, quad.shouldApplyDiffuseLighting()));
        }
        return builder.build();
    }

    public static BakedQuad colorQuad(Colour color, BakedQuad quad, boolean applyDifuse) {
        QuadTransformer transformer = new QuadTransformer(color, quad.getFormat(), applyDifuse);
        quad.pipe(transformer);
        return transformer.build();
    }


    private static class QuadTransformer extends VertexTransformer {
        Colour colour;
        Boolean applyDiffuse;
        TRSRTransformation transform;

        public QuadTransformer(Colour colour, VertexFormat format, boolean applyDiffuse) {
            super(new UnpackedBakedQuad.Builder(format));
            this.colour = colour;
            this.applyDiffuse = applyDiffuse;
        }

        public QuadTransformer(Colour colour, final TRSRTransformation transform, VertexFormat format, boolean applyDiffuse) {
            super(new UnpackedBakedQuad.Builder(format));
            this.transform = transform;
            this.colour = colour;
            this.applyDiffuse = applyDiffuse;
        }

        @Override
        public void put(int element, float... data) {
            VertexFormatElement.Usage usage = parent.getVertexFormat().getElement(element).getUsage();
//            System.out.println("element: " + element);
//            System.out.println("usage: " + usage.getDisplayName());
            // change color
            if (colour != null &&
                    usage == VertexFormatElement.Usage.COLOR &&
                    data.length >= 4) {
                data[0] = (float) colour.r;
                data[1] = (float) colour.g;
                data[2] = (float) colour.b;
                data[3] = (float) colour.a;
                super.put(element, data);
                // transform normals and position
            } else if (transform != null &&
                    usage == VertexFormatElement.Usage.POSITION &&
                    data.length >= 4) {
                float[] newData = new float[4];
                Vector4f vec = new Vector4f(data);
                transform.getMatrixVec().transform(vec);
                vec.get(newData);
                parent.put(element, newData);
            } else
                super.put(element, data);
        }

        @Override
        public void setApplyDiffuseLighting(boolean diffuse) {
            super.setApplyDiffuseLighting(applyDiffuse != null ? applyDiffuse : diffuse);
        }

        public UnpackedBakedQuad build() {
            return ((UnpackedBakedQuad.Builder) parent).build();
        }
    }
}