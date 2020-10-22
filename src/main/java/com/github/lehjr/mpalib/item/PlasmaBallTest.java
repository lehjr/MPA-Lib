package com.github.lehjr.mpalib.item;

import com.github.lehjr.mpalib.basemod.ModObjects;
import com.github.lehjr.mpalib.client.render.item.PlasmaBallTestRenderer;
import net.minecraft.item.Item;

public class PlasmaBallTest extends Item {
    public PlasmaBallTest() {
        super(new Item.Properties()
                .maxStackSize(1)
                .group(ModObjects.creativeTab)
                .defaultMaxDamage(-1)
                .setNoRepair()
                .setISTER(()-> PlasmaBallTestRenderer::new));
    }
}