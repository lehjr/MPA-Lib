package com.github.lehjr.mpalib.client.render.entity;

import com.github.lehjr.mpalib.client.model.item.armorstand.MPAArmorStandArmorModel;
import com.github.lehjr.mpalib.client.model.item.armorstand.MPAArmorStandModel;
import com.github.lehjr.mpalib.entity.MPAArmorStandEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class MPAArmorStandRenderer extends LivingRenderer<MPAArmorStandEntity, MPAArmorStandArmorModel> {
    public MPAArmorStandRenderer(EntityRendererManager manager) {
        super(manager, new MPAArmorStandModel(), 0.0F);
        this.addLayer(new BipedArmorLayer<>(this, new MPAArmorStandArmorModel(0.5F), new MPAArmorStandArmorModel(1.0F)));
        this.addLayer(new HeldItemLayer<>(this));
        this.addLayer(new ElytraLayer<>(this));
        this.addLayer(new HeadLayer<>(this));
        entityModel.hideBase();
    }

    /**
     * Returns the location of an entity's texture.
     */
    @Override
    public ResourceLocation getEntityTexture(MPAArmorStandEntity entity) {
        return MPAArmorStandModel.TEXTURE_ARMOR_STAND;
    }

    protected void applyRotations(ArmorStandEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - rotationYaw));
        float f = (float) (entityLiving.world.getGameTime() - entityLiving.punchCooldown) + partialTicks;
        if (f < 5.0F) {
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(MathHelper.sin(f / 1.5F * (float) Math.PI) * 3.0F));
        }
    }

    protected boolean canRenderName(ArmorStandEntity entity) {
        double d0 = this.renderManager.squareDistanceTo(entity);
        float f = entity.isCrouching() ? 32.0F : 64.0F;
        return d0 >= (double) (f * f) ? false : entity.isCustomNameVisible();
    }

    @Nullable
    protected RenderType func_230496_a_(MPAArmorStandEntity entity, boolean isVisible, boolean p_230496_3_, boolean p_230496_4_) {
        if (!entity.hasMarker()) {
            return super.func_230496_a_(entity, isVisible, p_230496_3_, p_230496_4_);
        } else {
            ResourceLocation resourcelocation = this.getEntityTexture(entity);
            if (p_230496_3_) {
                return RenderType.func_230168_b_(resourcelocation, false);
            } else {
                return isVisible ? RenderType.func_230167_a_(resourcelocation, false) : null;
            }
        }
    }
}
