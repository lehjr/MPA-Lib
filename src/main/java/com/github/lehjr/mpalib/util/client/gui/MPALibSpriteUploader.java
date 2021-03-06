package com.github.lehjr.mpalib.util.client.gui;

import com.github.lehjr.mpalib.basemod.MPALibConstants;
import net.minecraft.client.renderer.texture.SpriteUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class MPALibSpriteUploader extends SpriteUploader {
    private final Set<ResourceLocation> registeredSprites = new HashSet<>();

    public MPALibSpriteUploader(TextureManager textureManager, String prefix) {
        super(textureManager, MPALibConstants.LOCATION_MPALIB_GUI_TEXTURE_ATLAS, prefix);
    }

    public void registerIcon(ResourceLocation location) {
        registeredSprites.add(location);
    }

    public void registerSprite(ResourceLocation location) {
        registeredSprites.add(location);
    }

    @Override
    protected Stream<ResourceLocation> getResourceLocations() {
        return Collections.unmodifiableSet(registeredSprites).stream();
    }

    /**
     * Overridden to make it public
     */
    @Override
    public TextureAtlasSprite getSprite(ResourceLocation location) {
        return super.getSprite(location);
    }
}