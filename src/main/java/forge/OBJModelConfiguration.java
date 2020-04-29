package forge;

import com.github.lehjr.mpalib.basemod.MPALibLogger;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Either;
import net.minecraft.client.renderer.model.IModelTransform;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraftforge.client.model.IModelConfiguration;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * This is just the bare minimum needed to bake an OBJ model not attached to a block or item.
 */
public class OBJModelConfiguration implements IModelConfiguration {
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
        return false;
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
