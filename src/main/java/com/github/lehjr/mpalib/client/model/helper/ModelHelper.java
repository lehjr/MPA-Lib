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

package com.github.lehjr.mpalib.client.model.helper;

import com.github.lehjr.forge.obj.MPALibOBJLoader;
import com.github.lehjr.forge.obj.MPALibOBJModel;
import com.github.lehjr.mpalib.basemod.MPALibLogger;
import com.github.lehjr.mpalib.math.Colour;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.client.model.pipeline.VertexTransformer;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.Models;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nullable;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ModelHelper {


    //-------------------------------------
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







    public static IModel getModel(ResourceLocation resource) {
        IModel model = null;
        try {
            model = (MPALibOBJModel) MPALibOBJLoader.INSTANCE.loadModel(resource);
            model = ((MPALibOBJModel) model).process(ImmutableMap.of("flip-v", "true"));
        } catch (Exception e) {
            e.printStackTrace();
            MPALibLogger.logError("Model loading failed :( " + resource);
        }
        return model;
    }

    public static IBakedModel loadBakedModel(ResourceLocation resource, IModelState state) {
        IModel model = getModel(resource);
        if (model != null) {
            IBakedModel bakedModel = model.bake(state,
                    DefaultVertexFormats.ITEM,
                    new Function<ResourceLocation, TextureAtlasSprite>() {
                        public TextureAtlasSprite apply(ResourceLocation resourceLocation) {
                            return Minecraft.getMinecraft().getTextureMapBlocks().registerSprite(resourceLocation);
                        }
                    });
            return bakedModel;
        }
        return null;
    }

    public static IBakedModel loadBakedModel(ResourceLocation resource) {
        IModel model = getModel(resource);
        if (model != null) {
            IBakedModel bakedModel = model.bake(model.getDefaultState(),
                    DefaultVertexFormats.ITEM,
                    new Function<ResourceLocation, TextureAtlasSprite>() {
                        public TextureAtlasSprite apply(ResourceLocation resourceLocation) {
                            return Minecraft.getMinecraft().getTextureMapBlocks().registerSprite(resourceLocation);
                        }
                    });
            return bakedModel;
        }
        return null;
    }




    public static IModel getIModel(ResourceLocation location, int attempt) {
        String domain = location.getNamespace();
        String resourePath = location.getPath().replaceFirst("^models/models", "models");

        location = new ResourceLocation(domain, resourePath);
        IModel model;
        try {
            model = ModelLoaderRegistry.getModel(location);
            model = model.process(ImmutableMap.of("flip-v", "true"));
        } catch (Exception e) {
            model = ModelLoaderRegistry.getMissingModel();
            if (attempt < 6) {
                model = getIModel(location, attempt + 1);
                MPALibLogger.logError("Model loading failed on attempt #" + attempt + "  :( " + location.toString());
            } else {
                MPALibLogger.logError("Failed to load model. " + e);
                return getOBJModel(location, 0);
            }
        }
        return model;
    }

    public static IBakedModel getBakedModel(ResourceLocation modellocation, IModelState modelState) {
        IModel model = getIModel(modellocation, 0);

        try {
            return model.bake(modelState, DefaultVertexFormats.ITEM,
                    location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
        } catch (Exception e) {
            MPALibLogger.logError("Failed to bake model. " + e);
        }
        return ModelLoaderRegistry.getMissingModel().bake(ModelLoaderRegistry.getMissingModel().getDefaultState(), DefaultVertexFormats.ITEM,
                location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
    }

    public static IModel getOBJModel(ResourceLocation location, int attempt) {
        String domain = location.getNamespace();
        IModel model;
        try {
//            model = MuseOBJLoader.INSTANCE.loadModel(location);
            model = ModelLoaderRegistry.getModel(location);
            model = model.process(ImmutableMap.of("flip-v", "true"));
        } catch (Exception e) {
            model = ModelLoaderRegistry.getMissingModel();
            if (attempt < 6) {
                getOBJModel(location, attempt + 1);
                MPALibLogger.logError("Model loading failed on attempt #" + attempt + "  :( " + location.toString());
            } else
                return model;
            MPALibLogger.logError("Failed to load model. " + e);
        }
        return model;
    }

    public static IBakedModel getBakedOBJModel(ResourceLocation modellocation, IModelState modelState) {
        IModel model = getOBJModel(modellocation, 0);
        try {
            return model.bake(modelState, DefaultVertexFormats.ITEM,
                    location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
        } catch (Exception e) {
            MPALibLogger.logError("Failed to bake model. " + e);
        }
        return ModelLoaderRegistry.getMissingModel().bake(ModelLoaderRegistry.getMissingModel().getDefaultState(), DefaultVertexFormats.ITEM,
                location -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString()));
    }

    /*
     * This is a slightly modified version of Forge's example (@author shadekiller666) for the Tesseract model.
     * With this we can generate an extended blockstates to get the quads of any group in a model without
     * having to rebake the model. In this perticular case, the setup is for gettting an extended state that
     * will hide all groups but one. However, this can easily be altered to hide fewer parts if needed.
     *
     * The biggest issue with this setup is that the code. There is a better way out there
     */
    @Nullable
    public static IExtendedBlockState getStateForPart(List<String> shownIn, MPALibOBJModel.MPALibOBJBakedModel objBakedModelIn, @Nullable TRSRTransformation transformation) {
        List<String> hidden = new ArrayList<>(objBakedModelIn.getModel().getMatLib().getGroups().keySet());
        if (transformation == null)
            transformation = TRSRTransformation.identity();
        return getStateForPart(shownIn, hidden, transformation);
    }

    @Nullable
    public static List<String> getPartNames(IBakedModel bakedModel) {
        if (bakedModel != null && bakedModel instanceof MPALibOBJModel.MPALibOBJBakedModel)
            return new ArrayList<String>(((MPALibOBJModel.MPALibOBJBakedModel) bakedModel).getModel().getMatLib().getGroups().keySet());
        return null;
    }

    public static IExtendedBlockState getStateForPart(List<String> shownIn, List<String> hiddenIn, final TRSRTransformation transformation) {
        BlockStateContainer stateContainer = new ExtendedBlockState(null, new IProperty[0], new IUnlistedProperty[]{net.minecraftforge.common.property.Properties.AnimationProperty});
        for (String shown : shownIn)
            hiddenIn.remove(shown);

        try {
            IModelState state = new IModelState() {
                private final java.util.Optional<TRSRTransformation> value = java.util.Optional.of(transformation);

                @Override
                public java.util.Optional<TRSRTransformation> apply(java.util.Optional<? extends IModelPart> part) {
                    if (part.isPresent()) {
                        UnmodifiableIterator<String> parts = Models.getParts(part.get());
                        if (parts.hasNext()) {
                            String name = parts.next();
                            // only interested in the root level
                            if (!parts.hasNext() && hiddenIn.contains(name)) return value;
                        }
                    }
                    return java.util.Optional.empty();
                }
            };
            return ((IExtendedBlockState) stateContainer.getBaseState()).withProperty(net.minecraftforge.common.property.Properties.AnimationProperty, state);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Here we can color the quads or change the transform using the setup below.
     * This is better than changing material colors for Wavefront models because it means that you can use a single material for the entire model
     * instead of unique ones for each group. It also means you don't nescessarily need a Wavefront model.
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
            VertexFormatElement.EnumUsage usage = parent.getVertexFormat().getElement(element).getUsage();
//            System.out.println("element: " + element);
//            System.out.println("usage: " + usage.getDisplayName());
            // change color
            if (colour != null &&
                    usage == VertexFormatElement.EnumUsage.COLOR &&
                    data.length >= 4) {
                data[0] = (float) colour.r;
                data[1] = (float) colour.g;
                data[2] = (float) colour.b;
                data[3] = (float) colour.a;
                super.put(element, data);
                // transform normals and position
            } else if (transform != null &&
                    usage == VertexFormatElement.EnumUsage.POSITION &&
                    data.length >= 4) {
                float[] newData = new float[4];
                Vector4f vec = new Vector4f(data);
                transform.getMatrix().transform(vec);
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