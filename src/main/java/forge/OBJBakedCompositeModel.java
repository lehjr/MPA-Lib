package forge;

import com.github.lehjr.mpalib.client.model.helper.ModelHelper;
import com.github.lehjr.mpalib.math.Colour;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class OBJBakedCompositeModel implements IDynamicBakedModel {
    public static final ModelProperty<SubmodelModelData> SUBMODEL_DATA = new ModelProperty<>();
    public static final ModelProperty<Integer> COLOUR = new ModelProperty<>();
    public static final ModelProperty<Boolean> VISIBLE = new ModelProperty<>();
    public static final ModelProperty<Boolean> GLOW = new ModelProperty<>();

    private final ImmutableMap<String, OBJBakedPart> bakedParts; // store the quads for each part
    private final ImmutableMap<String, Boolean> visibilityMap; // default visibility for parts
    private final ImmutableMap<String, Boolean> ambientOcclusionMap; // try to use this for "glow"

    private final boolean isAmbientOcclusion;
    private final boolean isGui3d;
    private final TextureAtlasSprite particle;
    private final ItemOverrideList overrides;
    private final IModelTransform transforms;

    public OBJBakedCompositeModel(boolean isGui3d,
                                  boolean isAmbientOcclusion,
                                  TextureAtlasSprite particle,
                                  ImmutableMap<String, OBJBakedPart> bakedParts, // store the quads for each part
                                  ImmutableMap<String, Boolean> visibilityMap, // default visibility for parts
                                  ImmutableMap<String, Boolean> ambientOcclusionMap, // try to use this for "glow"
                                  IModelTransform combinedTransform,
                                  ItemOverrideList overrides) {
        this.isGui3d = isGui3d;
        this.isAmbientOcclusion = isAmbientOcclusion;
        this.bakedParts = bakedParts;
        this.visibilityMap = visibilityMap;
        this.ambientOcclusionMap = ambientOcclusionMap;
        this.particle = particle;
        this.transforms = combinedTransform;
        this.overrides = overrides;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        List<BakedQuad> quads = new ArrayList<>();

        for (Map.Entry<String, OBJBakedPart> entry : bakedParts.entrySet()) {
            quads.addAll(entry.getValue().getQuads(state, side, rand, getSubmodelData(extraData, entry.getKey())));
        }
        return quads;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return isAmbientOcclusion;
    }

    @Override
    public boolean isGui3d() {
        return isGui3d;
    }

    @Override
    public boolean func_230044_c_() {
        // TODO: Forge: Auto-generated method stub
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return particle;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return overrides;
    }

    @Override
    public boolean doesHandlePerspectives() {
        return true;
    }

    @Override
    public IBakedModel handlePerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat) {
        return PerspectiveMapWrapper.handlePerspective(this, transforms, cameraTransformType, mat);
    }

    @Nullable
    public OBJBakedPart getPart(String name) {
        return bakedParts.get(name);
    }

    private IModelData getSubmodelData(IModelData extraData, String name) {
        SubmodelModelData data = extraData.getData(SUBMODEL_DATA);
        if (data == null) {
            return EmptyModelData.INSTANCE;
        }
        return data.getSubmodelData(name);
    }

    public static class SubmodelModelData {
        private final Map<String, IModelData> parts = new HashMap<>();

        public IModelData getSubmodelData(String name) {
            return parts.getOrDefault(name, EmptyModelData.INSTANCE);
        }

        public void putSubmodelData(String name, IModelData data) {
            parts.put(name, data);
        }
    }
}