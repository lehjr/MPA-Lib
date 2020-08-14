//package com.github.lehjr.mpalib.client.model.item.armor;
//
//import com.github.lehjr.mpalib.capabilities.render.IArmorModelSpecNBT;
//import com.github.lehjr.mpalib.capabilities.render.ModelSpecNBTCapability;
//import com.github.lehjr.mpalib.capabilities.render.modelspec.EnumSpecType;
//import com.github.lehjr.mpalib.math.Colour;
//import com.mojang.blaze3d.matrix.MatrixStack;
//import com.mojang.blaze3d.vertex.IVertexBuilder;
//import net.minecraft.client.renderer.Atlases;
//import net.minecraft.client.renderer.IRenderTypeBuffer;
//import net.minecraft.client.renderer.ItemRenderer;
//import net.minecraft.client.renderer.RenderType;
//import net.minecraft.client.renderer.entity.IEntityRenderer;
//import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
//import net.minecraft.client.renderer.entity.model.BipedModel;
//import net.minecraft.client.renderer.texture.AtlasTexture;
//import net.minecraft.client.renderer.texture.OverlayTexture;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.inventory.EquipmentSlotType;
//import net.minecraft.item.ArmorItem;
//import net.minecraft.item.IDyeableArmorItem;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.ResourceLocation;
//
//import javax.annotation.Nonnull;
//import javax.annotation.Nullable;
//
//public class MPAArmorLayer<T extends LivingEntity, M extends BipedModel<T>, A extends BipedModel<T>> extends BipedArmorLayer<T, M, A> {
//    public MPAArmorLayer(IEntityRenderer<T, M> entityRenderer, A modelLeggings, A modelArmor) {
//        super(entityRenderer, modelLeggings, modelArmor);
//    }
//
//    /**
//     * From here, we can set the render type to be the same as what the item models use
//     * @param matrixStackIn
//     * @param bufferIn
//     * @param entityLivingBaseIn
//     * @param limbSwing
//     * @param limbSwingAmount
//     * @param partialTicks
//     * @param ageInTicks
//     * @param netHeadYaw
//     * @param headPitch
//     * @param slotIn
//     * @param packedLightIn
//     */
///*
//    private void func_241739_a_(@Nonnull MatrixStack matrixStackIn,
//                                @Nonnull IRenderTypeBuffer bufferIn,
//                                T entityLivingBaseIn,
//                                EquipmentSlotType slotIn,
//                                int packedLightIn, A model) {
//
//    }
//*/
//    private void func_241739_a_(@Nonnull MatrixStack matrixStackIn, @Nonnull IRenderTypeBuffer bufferIn, T entityLivingBaseIn, EquipmentSlotType  slotIn, int packedLightIn, A model) {
//        ItemStack itemstack = entityLivingBaseIn.getItemStackFromSlot( slotIn);
//        if (itemstack.getItem() instanceof ArmorItem) {
//            ArmorItem armoritem = (ArmorItem)itemstack.getItem();
//            if (armoritem.getEquipmentSlot() ==  slotIn) {
//                model = getArmorModelHook(entityLivingBaseIn, itemstack,  slotIn, model);
//
//                if (itemstack.getCapability(ModelSpecNBTCapability.RENDER).isPresent()) {
//                    itemstack.getCapability(ModelSpecNBTCapability.RENDER).ifPresent(spec->{
//                        System.out.println("fixme!!!");
//
////                    A model = this.getModelFromSlot(slotIn);
////                        ((BipedModel)this.getEntityModel()).setModelAttributes(model);
////
////                        model.setLivingAnimations(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks);
////                        this.setModelSlotVisible(model, slotIn);
////                        model.setRotationAngles(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
////                        boolean flag1 = itemstack.hasEffect();
////
////                        if (spec.getSpecType() == EnumSpecType.ARMOR_SKIN) {
////                            Colour colour = spec.getColorFromItemStack();
////                            renderArmor(matrixStackIn, bufferIn, packedLightIn, flag1, model, colour.r, colour.g, colour.b, this.getArmorResource(entityLivingBaseIn, itemstack, slotIn, null));
////                            renderArmor(matrixStackIn, bufferIn, packedLightIn, flag1, model, 1.0F, 1.0F, 1.0F, this.getArmorResource(entityLivingBaseIn, itemstack, slotIn, "overlay"));
////                        } else {
////                            renderArmor(matrixStackIn, bufferIn, packedLightIn, flag1, model, 1.0F, 1.0F, 1.0F, this.getArmorResource(entityLivingBaseIn, itemstack, slotIn, null));
////                        }
//                    });
//                } else {
//                    this.getEntityModel().setModelAttributes(model);
//                    this.setModelSlotVisible(model,  slotIn);
//                    boolean flag = this.isLegSlot( slotIn);
//                    boolean flag1 = itemstack.hasEffect();
//                    if (armoritem instanceof IDyeableArmorItem) {
//                        int i = ((IDyeableArmorItem)armoritem).getColor(itemstack);
//                        float f = (float)(i >> 16 & 255) / 255.0F;
//                        float f1 = (float)(i >> 8 & 255) / 255.0F;
//                        float f2 = (float)(i & 255) / 255.0F;
//                        this.func_241738_a_(matrixStackIn, bufferIn, packedLightIn, flag1, model, f, f1, f2, this.getArmorResource(entityLivingBaseIn, itemstack,  slotIn, null));
//                        this.func_241738_a_(matrixStackIn, bufferIn, packedLightIn, flag1, model, 1.0F, 1.0F, 1.0F, this.getArmorResource(entityLivingBaseIn, itemstack,  slotIn, "overlay"));
//                    } else {
//                        this.func_241738_a_(matrixStackIn, bufferIn, packedLightIn, flag1, model, 1.0F, 1.0F, 1.0F, this.getArmorResource(entityLivingBaseIn, itemstack,  slotIn, null));
//                    }
//                }
//            }
//        }
//    }
//
//
//
//
////    // func_241739_a_
////    @Override
////    protected void /* renderArmorPart*/ func_241739_a_(@Nonnull MatrixStack matrixStackIn,
////                                   @Nonnull IRenderTypeBuffer bufferIn,
////                                   T entityLivingBaseIn,
////                                   float limbSwing,
////                                   float limbSwingAmount,
////                                   float partialTicks,
////                                   float ageInTicks,
////                                   float netHeadYaw,
////                                   float headPitch,
////                                   @Nonnull EquipmentSlotType slotIn,
////                                   int packedLightIn) {
////        ItemStack stack = entityLivingBaseIn.getItemStackFromSlot(slotIn);
////
////        if (stack.getItem() instanceof ArmorItem && stack.getCapability(ModelSpecNBTCapability.RENDER).isPresent()) {
//
////        } else {
////            super.renderArmorPart(matrixStackIn, bufferIn, entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, slotIn, packedLightIn);
////        }
////    }
//
//    /**
//     * Sets the render type
//     *
//     * @param matrixStackIn
//     * @param bufferIn
//     * @param packedLightIn
//     * @param glintIn
//     * @param modelIn
//     * @param red
//     * @param green
//     * @param blue
//     * @param armorResource
//     */
//    private void renderArmor(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, boolean glintIn, A modelIn, float red, float green, float blue, ResourceLocation armorResource) {
//        RenderType renderType;
//        if (armorResource == AtlasTexture.LOCATION_BLOCKS_TEXTURE) {
//            renderType = Atlases.getTranslucentCullBlockType();
//        } else {
//            renderType = RenderType.getEntityCutoutNoCull(armorResource);
//        }
//        IVertexBuilder ivertexbuilder = ItemRenderer.getBuffer(bufferIn, renderType, false, glintIn);
//        modelIn.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);
//    }
//
//    /**
//     * More generic ForgeHook version of the above function, it allows for Items to have more control over what texture they provide.
//     *
//     * @param entity Entity wearing the armor
//     * @param stack  ItemStack for the armor
//     * @param slot   Slot ID that the item is in
//     * @param type   Subtype, can be null or "overlay"
//     * @return ResourceLocation pointing at the armor's texture
//     */
//    @Override
//    public ResourceLocation getArmorResource(Entity entity, @Nonnull ItemStack stack, EquipmentSlotType slot, @Nullable String type) {
//        return stack.getCapability(ModelSpecNBTCapability.RENDER).map(spec->{
//            if (spec.getSpecType() == EnumSpecType.ARMOR_SKIN && spec instanceof IArmorModelSpecNBT) {
//                return new ResourceLocation(((IArmorModelSpecNBT) spec).getArmorTexture());
//            }
//            return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
//        }).orElse(super.getArmorResource(entity, stack, slot, type));
//    }
//
////    @Nonnull
////    @Override
////    public A /*getModelFromSlot*/ func_241736_a_(EquipmentSlotType slot) {
////        return slot == EquipmentSlotType.LEGS ? this.modelLeggings : this.modelArmor;
////    }
//}