package forge;

import com.github.lehjr.mpalib.basemod.MPALibLogger;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class OBJModelConfiguration implements IModelConfiguration {

                    /*
        System.out.println("model configuration class: " + owner.getClass());
        System.out.println("model name: " + owner.getModelName());
        System.out.println("model isShadedInGui(): " + owner.isShadedInGui());
        System.out.println("model isSideLit(): " + owner.isSideLit());
        System.out.println("model getOwnerModel().getDependencies()): " + owner.getOwnerModel().getDependencies());

owner.getOwnerModel() class: class net.minecraft.client.renderer.model.BlockModel
owner.getOwnerModel()).getParentLocation(): minecraft:block/block
owner.getOwnerModel()).name: modularpowerarmor:block/luxcapacitor
model owner.getOwnerModel()).getElements(): []

[22:04:32] [Render thread/INFO] [STDOUT/]: [forge.MPAOBJModel:bake:91]: model owner.getOwnerModel() class: class net.minecraft.client.renderer.model.BlockModel
[22:04:32] [Render thread/INFO] [STDOUT/]: [forge.MPAOBJModel:bake:94]: model owner.getOwnerModel()).getParentLocation(): minecraft:block/block
[22:04:32] [Render thread/INFO] [STDOUT/]: [forge.MPAOBJModel:bake:95]: model owner.getOwnerModel()).name: modularpowerarmor:block/luxcapacitor
[22:04:32] [Render thread/INFO] [STDOUT/]: [forge.MPAOBJModel:bake:96]: model owner.getOwnerModel()).getElements(): []
[22:04:32] [Render thread/INFO] [STDOUT/]: [forge.MPAOBJModel:bake:104]: model ((BlockModel) owner.getOwnerModel()).textures key: particle
[22:04:32] [Render thread/INFO] [STDOUT/]: [forge.MPAOBJModel:bake:105]: texture string: not present
[22:04:32] [Render thread/INFO] [STDOUT/]: [forge.MPAOBJModel:bake:106]: material texture location: modularpowerarmor:models/luxcapacitor
[22:04:32] [Render thread/INFO] [STDOUT/]: [forge.MPAOBJModel:bake:107]: material atlas location: minecraft:textures/atlas/blocks.png
[22:04:32] [Render thread/INFO] [STDOUT/]: [forge.MPAOBJModel:bake:91]: model owner.getOwnerModel() class: class net.minecraft.client.renderer.model.BlockModel
[22:04:32] [Render thread/INFO] [STDOUT/]: [forge.MPAOBJModel:bake:94]: model owner.getOwnerModel()).getParentLocation(): minecraft:block/block
[22:04:32] [Render thread/INFO] [STDOUT/]: [forge.MPAOBJModel:bake:95]: model owner.getOwnerModel()).name: modularpowerarmor:block/luxcapacitor
[22:04:32] [Render thread/INFO] [STDOUT/]: [forge.MPAOBJModel:bake:96]: model owner.getOwnerModel()).getElements(): []
[22:04:32] [Render thread/INFO] [STDOUT/]: [forge.MPAOBJModel:bake:104]: model ((BlockModel) owner.getOwnerModel()).textures key: particle
[22:04:32] [Render thread/INFO] [STDOUT/]: [forge.MPAOBJModel:bake:105]: texture string: not present

model configuration class: class net.minecraftforge.client.model.BlockModelConfiguration
model name: modularpowerarmor:block/luxcapacitor
model isShadedInGui(): true
model isSideLit(): true
model getOwnerModel().getDependencies()): [minecraft:block/block]
         */
    IModelTransform combinedTransform;
    String modelLocation;
    public OBJModelConfiguration(String modelLocation, IModelTransform combinedTransform) {
        this.modelLocation = modelLocation;
        this.combinedTransform = combinedTransform;
    }

    /**
     * If available, gets the owning model (usually BlockModel) of this configuration
     */
    @Nullable
    @Override
    public IUnbakedModel getOwnerModel() {
        return null;
    }

    /**
     * @return The name of the model being baked, for logging and cache purposes.
     */
    @Override
    public String getModelName() {
        return modelLocation;
    }

    /**
     * Checks if a texture is present in the model.
     *
     * @param name The name of a texture channel.
     */
    @Override
    public boolean isTexturePresent(String name) {
        return false;
    }

    /**
     * Resolves the final texture name, taking into account texture aliases and replacements.
     *
     * @param nameIn The name of a texture channel.
     * @return The location of the texture, or the missing texture if not found.
     */
    @Override
    public Material resolveTexture(String nameIn) {
        if (startsWithHash(nameIn)) {
            nameIn = nameIn.substring(1);
        }

        List<String> list = Lists.newArrayList();

        while(true) {
            Either<Material, String> either = this.findTexture(nameIn);
            Optional<Material> optional = either.left();
            if (optional.isPresent()) {
                return optional.get();
            }

            nameIn = either.right().get();
            if (list.contains(nameIn)) {
                MPALibLogger.getLogger().warn("Unable to resolve texture due to reference chain {}->{} in {}", Joiner.on("->").join(list), nameIn, this.modelLocation);
                return new Material(AtlasTexture.LOCATION_BLOCKS_TEXTURE, MissingTextureSprite.getLocation());
            }

            list.add(nameIn);
        }
    }

    private static boolean startsWithHash(String strIn) {
        return strIn.charAt(0) == '#';
    }

    private Either<Material, String> findTexture(String nameIn) {
//        for(BlockModel blockmodel = this; blockmodel != null; blockmodel = blockmodel.parent) {
//            Either<Material, String> either = blockmodel.textures.get(nameIn);
//            if (either != null) {
//                return either;
//            }
//        }

        return Either.left(new Material(AtlasTexture.LOCATION_BLOCKS_TEXTURE, MissingTextureSprite.getLocation()));
    }


    /**
     * @return True if the item uses 3D lighting.
     */
    @Override
    public boolean isShadedInGui() {
        return true;
    }

    /**
     * @return True if the item is lit from the side
     */
    @Override
    public boolean isSideLit() {
        return true;
    }

    /**
     * @return True if the item requires per-vertex lighting.
     */
    @Override
    public boolean useSmoothLighting() {
        return true;
    }

    /**
     * Gets the vanilla camera transforms data.
     * Do not use for non-vanilla code. For general usage, prefer getCombinedState.
     */
    @Override
    public ItemCameraTransforms getCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    /**
     * @return The combined transformation state including vanilla and forge transforms data.
     */
    @Override
    public IModelTransform getCombinedTransform() {
        return combinedTransform;
    }
}
