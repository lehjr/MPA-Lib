package com.github.lehjr.mpalib.client.model.item.armor;

import com.github.lehjr.mpalib.util.capabilities.render.IArmorModelSpecNBT;
import com.github.lehjr.mpalib.util.capabilities.render.ModelSpecNBTCapability;
import com.github.lehjr.mpalib.util.capabilities.render.modelspec.EnumSpecType;
import com.github.lehjr.mpalib.util.math.Colour;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IDyeableArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MPAArmorLayer<T extends LivingEntity, M extends BipedModel<T>, A extends BipedModel<T>> extends BipedArmorLayer<T, M, A> {
    public MPAArmorLayer(IEntityRenderer<T, M> entityRenderer, A modelLeggings, A modelArmor) {
        super(entityRenderer, modelLeggings, modelArmor);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, T entityIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        this.renderPart(matrixStackIn, bufferIn, entityIn, EquipmentSlotType.CHEST, packedLightIn, this.getModelFromSlot(EquipmentSlotType.CHEST));
        this.renderPart(matrixStackIn, bufferIn, entityIn, EquipmentSlotType.LEGS, packedLightIn, this.getModelFromSlot(EquipmentSlotType.LEGS));
        this.renderPart(matrixStackIn, bufferIn, entityIn, EquipmentSlotType.FEET, packedLightIn, this.getModelFromSlot(EquipmentSlotType.FEET));
        this.renderPart(matrixStackIn, bufferIn, entityIn, EquipmentSlotType.HEAD, packedLightIn, this.getModelFromSlot(EquipmentSlotType.HEAD));
    }

    private A getModelFromSlot(EquipmentSlotType p_241736_1_) {
        return this.isLegSlot(p_241736_1_) ? this.modelLeggings : this.modelArmor;
    }

    private boolean isLegSlot(EquipmentSlotType slotIn) {
        return slotIn == EquipmentSlotType.LEGS;
    }

    private void renderPart(MatrixStack matrixIn, IRenderTypeBuffer bufferIn, T entityIn, EquipmentSlotType  slotIn, int packedLightIn, A model) {
        ItemStack itemstack = entityIn.getItemStackFromSlot( slotIn);
        boolean hasEffect = itemstack.hasEffect();

        if (itemstack.getItem() instanceof ArmorItem) {
            ArmorItem armoritem = (ArmorItem)itemstack.getItem();
            if (armoritem.getEquipmentSlot() ==  slotIn) {
                // ideally, this would replace the getArmorModel
                if (itemstack.getCapability(ModelSpecNBTCapability.RENDER).isPresent()) {
                    itemstack.getCapability(ModelSpecNBTCapability.RENDER).ifPresent(spec->{
                        // gets the actual model from the
                        A actualModel = this.getArmorModelHook(entityIn, itemstack, slotIn, model);
                        this.getEntityModel().setModelAttributes(actualModel);
                        this.setModelSlotVisible(actualModel, slotIn);
                        if (spec.getSpecType() == EnumSpecType.ARMOR_SKIN) {
                            Colour colour = spec.getColorFromItemStack();
                            renderArmor(matrixIn, bufferIn, packedLightIn, hasEffect, actualModel, colour.r, colour.g, colour.b, this.getArmorResource(entityIn, itemstack, slotIn, null));
                            renderArmor(matrixIn, bufferIn, packedLightIn, hasEffect, actualModel, 1.0F, 1.0F, 1.0F, this.getArmorResource(entityIn, itemstack, slotIn, "overlay"));
                        } else {
                            renderArmor(matrixIn, bufferIn, packedLightIn, hasEffect, actualModel, 1.0F, 1.0F, 1.0F, this.getArmorResource(entityIn, itemstack, slotIn, null));
                        }
                    });
                } else {
                    this.getEntityModel().setModelAttributes(model);
                    this.setModelSlotVisible(model,  slotIn);

                    if (armoritem instanceof IDyeableArmorItem) {
                        int colorInt = ((IDyeableArmorItem)armoritem).getColor(itemstack);
                        float red = (float)(colorInt >> 16 & 255) / 255.0F;
                        float green = (float)(colorInt >> 8 & 255) / 255.0F;
                        float blue = (float)(colorInt & 255) / 255.0F;
                        this.func_241738_a_(matrixIn, bufferIn, packedLightIn, hasEffect, model, red, green, blue, this.getArmorResource(entityIn, itemstack,  slotIn, null));
                        this.func_241738_a_(matrixIn, bufferIn, packedLightIn, hasEffect, model, 1.0F, 1.0F, 1.0F, this.getArmorResource(entityIn, itemstack,  slotIn, "overlay"));
                    } else {
                        this.func_241738_a_(matrixIn, bufferIn, packedLightIn, hasEffect, model, 1.0F, 1.0F, 1.0F, this.getArmorResource(entityIn, itemstack,  slotIn, null));
                    }
                }
            }
        }
    }

    private void func_241738_a_(MatrixStack matrixIn, IRenderTypeBuffer bufferIn, int packedLightIn, boolean hasEffect, A model, float red, float green, float blue, ResourceLocation armorResource) {
        IVertexBuilder ivertexbuilder = ItemRenderer.getArmorVertexBuilder(bufferIn, RenderType.getArmorCutoutNoCull(armorResource), false, hasEffect);
        model.render(matrixIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
    }

    /**
     * Sets the render type
     *
     * @param matrixStackIn
     * @param bufferIn
     * @param packedLightIn
     * @param glintIn
     * @param modelIn
     * @param red
     * @param green
     * @param blue
     * @param armorResource
     */
    private void renderArmor(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, boolean glintIn, A modelIn, float red, float green, float blue, ResourceLocation armorResource) {
        RenderType renderType;
        if (armorResource == AtlasTexture.LOCATION_BLOCKS_TEXTURE) {
            renderType = Atlases.getTranslucentCullBlockType();
        } else {
            renderType = RenderType.getEntityCutoutNoCull(armorResource);
        }
        IVertexBuilder ivertexbuilder = ItemRenderer.getBuffer(bufferIn, renderType, false, glintIn);
        modelIn.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
    }

    /**
     * More generic ForgeHook version of the above function, it allows for Items to have more control over what texture they provide.
     *
     * @param entity Entity wearing the armor
     * @param stack  ItemStack for the armor
     * @param slot   Slot ID that the item is in
     * @param type   Subtype, can be null or "overlay"
     * @return ResourceLocation pointing at the armor's texture
     */
    @Override
    public ResourceLocation getArmorResource(Entity entity, @Nonnull ItemStack stack, EquipmentSlotType slot, @Nullable String type) {
        return stack.getCapability(ModelSpecNBTCapability.RENDER).map(spec->{
            if (spec.getSpecType() == EnumSpecType.ARMOR_SKIN && spec instanceof IArmorModelSpecNBT) {
                return new ResourceLocation(((IArmorModelSpecNBT) spec).getArmorTexture());
            }
            return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
        }).orElse(super.getArmorResource(entity, stack, slot, type));
    }

    @Override
    protected A getArmorModelHook(T entity, ItemStack itemStack, EquipmentSlotType slot, A model) {
        return super.getArmorModelHook(entity, itemStack, slot, model);
    }
}