package com.github.lehjr.mpalib.client.render.entity;

import com.github.lehjr.mpalib.basemod.MPALIbConstants;
import net.minecraft.client.renderer.entity.ArmorStandRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MPAArmorStandRenderer extends ArmorStandRenderer {
    public MPAArmorStandRenderer(EntityRendererManager manager) {
        super(manager);
    }

    /**
     * Returns the location of an entity's texture.
     */
    @Override
    public ResourceLocation getEntityTexture(ArmorStandEntity entity) {
        return MPALIbConstants.TEXTURE_ARMOR_STAND;
    }
}