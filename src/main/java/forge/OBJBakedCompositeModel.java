package forge;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.data.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static forge.OBJPartData.getOBJPartData;

public class OBJBakedCompositeModel implements IDynamicBakedModel {
    private final ImmutableMap<String, OBJBakedPart> bakedParts; // store the quads for each part

    private final boolean isAmbientOcclusion;
    private final boolean isGui3d;
    private final TextureAtlasSprite particle;
    private final ItemOverrideList overrides;

    private final IModelTransform transforms;

    public OBJBakedCompositeModel(boolean isGui3d,
                                  boolean isAmbientOcclusion,
                                  TextureAtlasSprite particle,
                                  ImmutableMap<String, OBJBakedPart> bakedParts, // store the quads for each part
                                  IModelTransform combinedTransform,
                                  ItemOverrideList overrides) {
        this.isGui3d = isGui3d;
        this.isAmbientOcclusion = isAmbientOcclusion;
        this.bakedParts = bakedParts;
        this.particle = particle;
        this.transforms = combinedTransform;
        this.overrides = overrides;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        List<BakedQuad> quads = new ArrayList<>();

        for (Map.Entry<String, OBJBakedPart> entry : bakedParts.entrySet()) {
            quads.addAll(entry.getValue().getQuads(state, side, rand, getOBJPartData(extraData, entry.getKey())));
        }
        return quads;
    }

    public IModelTransform getTransforms() {
        return transforms;
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
}