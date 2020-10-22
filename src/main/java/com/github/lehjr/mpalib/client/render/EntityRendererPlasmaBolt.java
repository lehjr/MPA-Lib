//package com.github.lehjr.mpalib.client.render;
//
//import com.github.lehjr.mpalib.util.client.gui.geometry.DrawableCircle;
//import com.github.lehjr.mpalib.util.math.Colour;
//import net.minecraft.client.renderer.model.ItemCameraTransforms;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//
//import java.nio.DoubleBuffer;
//
//import static net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND;
//import static net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND;
//
//public class EntityRendererPlasmaBolt extends MuseEntityRenderer<EntityPlasmaBolt> {
//    public static DoubleBuffer unrotatebuffer;
//    protected static DrawableCircle circle1;
//    protected static DrawableCircle circle2;
//    protected static DrawableCircle circle3;
//    protected static DrawableCircle circle4;
//
//    public EntityRendererPlasmaBolt(RenderManager renderManager) {
//        super(renderManager);
//        Colour c1 = new Colour(.3, .3, 1, 0.3);
//        circle1 = new DrawableCircle(c1, c1);
//        c1 = new Colour(.3, .3, 1, 0.6);
//        circle2 = new DrawableCircle(c1, c1);
//        c1 = new Colour(.3, .3, 1, 1);
//        circle3 = new DrawableCircle(c1, c1);
//        circle4 = new DrawableCircle(c1, new Colour(1, 1, 1, 1));
//    }
//
//    public static void doRender(double size) {
//        GL11.glPushMatrix();
//        MPALibRenderer.unRotate();
//        double scale = size / 16.0;
//        GL11.glScaled(scale, scale, scale);
//        int millisPerCycle = 500;
//        double timeScale = Math.cos((System.currentTimeMillis() % millisPerCycle) * 2.0 / millisPerCycle - 1.0);
//        MPALibRenderState.glowOn();
//        circle1.draw(4, 0, 0);
//        GL11.glTranslated(0, 0, 0.001);
//        circle2.draw(3 + timeScale / 2, 0, 0);
//        GL11.glTranslated(0, 0, 0.001);
//        circle3.draw(2 + timeScale, 0, 0);
//        GL11.glTranslated(0, 0, 0.001);
//        circle4.draw(1 + timeScale, 0, 0);
//        for (int i = 0; i < 3; i++) {
//            double angle1 = (Math.random() * 2 * Math.PI);
//            double angle2 = (Math.random() * 2 * Math.PI);
//            MPALibRenderer.drawLightning(Math.cos(angle1) * 0.5, Math.sin(angle1) * 0.5, 0, Math.cos(angle2) * 5, Math.sin(angle2) * 5, 1,
//                    new Colour(1, 1, 1, 0.9));
//        }
//        MPALibRenderState.glowOff();
//        GL11.glPopMatrix();
//    }
//
//    public static void doRender(double boltSizeIn, ItemCameraTransforms.TransformType cameraTransformTypeIn) {
//        if (boltSizeIn != 0) {
//            GL11.glPushMatrix();
//            if (cameraTransformTypeIn == FIRST_PERSON_RIGHT_HAND || cameraTransformTypeIn == FIRST_PERSON_LEFT_HAND) {
//                GL11.glScaled(0.0625f, 0.0625f, 0.0625f); // negative scale mirrors the model
//                GL11.glRotatef(-182, 1, 0, 0);
//
//            } else {
//                GL11.glScaled(0.0625f, 0.0625f, 0.0625f);
//                GL11.glTranslatef(0, 0, 20.3f);
////                GL11.glTranslatef(0, 0, 1.3f);
//                GL11.glRotatef(-196, 1, 0, 0);
//            }
//            //---
//            GL11.glTranslated(-1, 1, 16);
//            GL11.glPushMatrix();
//            EntityRendererPlasmaBolt.doRender(boltSizeIn);
//            GL11.glPopMatrix();
//            // ---
//            GL11.glPopMatrix();
//        }
//    }
//
//    /**
//     * Actually renders the given argument. This is a synthetic bridge method,
//     * always casting down its argument and then handing it off to a worker
//     * function which does the actual work. In all probabilty, the class Render
//     * is generic (Render<T extends Entity) and this method has signature public
//     * void doRender(T entity, double d, double d1, double d2, float f, float
//     * f1). But JAD is pre 1.5 so doesn't do that.
//     */
////    @Override
//    public void doRender(EntityPlasmaBolt entity, double x, double y, double z, float entityYaw, float partialTicks) {
//        double size = (entity.size) / 10.0;
//        GL11.glPushMatrix();
//        GL11.glTranslated(x, y, z);
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glScalef(0.5F, 0.5F, 0.5F);
//        doRender(size);
//        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//        GL11.glPopMatrix();
//    }
//}
