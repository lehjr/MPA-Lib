package com.github.lehjr.mpalib.basemod;

import com.github.lehjr.mpalib.block.MPAArmorStandBaseBlock;
import com.github.lehjr.mpalib.client.render.item.MPAArmorStationItemRenderer;
import com.github.lehjr.mpalib.entity.MPAArmorStandEntity;
import com.github.lehjr.mpalib.tileentity.MPAArmorStandBaseTileEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModObjects {
    /** Blocks ------------------------------------------------------------------------------------ */
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MPALIbConstants.MOD_ID);

    public static final RegistryObject<MPAArmorStandBaseBlock> ARMOR_STATION_BLOCK = BLOCKS.register(MPALIbConstants.AMOR_WORKSTATION__REGNAME,
            () -> new MPAArmorStandBaseBlock());

    /** Items ------------------------------------------------------------------------------------- */
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MPALIbConstants.MOD_ID);

    public static final RegistryObject<Item> ARMOR_STATION_ITEM = ITEMS.register(MPALIbConstants.AMOR_WORKSTATION__REGNAME,
            () -> new BlockItem(ARMOR_STATION_BLOCK.get(),
                    new Item.Properties().group(ItemGroup.DECORATIONS).setISTER(()-> MPAArmorStationItemRenderer::new)));

    /** Tile Entity Types ------------------------------------------------------------------------- */
    public static final DeferredRegister<TileEntityType<?>> TILE_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MPALIbConstants.MOD_ID);

    public static final RegistryObject<TileEntityType<MPAArmorStandBaseTileEntity>> ARMOR_STATION_TILE = TILE_TYPES.register(MPALIbConstants.AMOR_WORKSTATION__REGNAME,
            () -> TileEntityType.Builder.create(MPAArmorStandBaseTileEntity::new, ARMOR_STATION_BLOCK.get()).build(null));









    /** Entity Types ------------------------------------------------------------------------------ */
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, MPALIbConstants.MOD_ID);

    public static final RegistryObject<EntityType<MPAArmorStandEntity>> ARMOR_WORKSTATION__ENTITY_TYPE = ENTITY_TYPES.register(MPALIbConstants.ARMOR_WORKSTATION__ENTITY_TYPE_REGNAME,
            () -> EntityType.Builder.create(MPAArmorStandEntity::new, EntityClassification.CREATURE)
                    .size(0.5F, 1.975F) // Hitbox Size
                    .build(new ResourceLocation(MPALIbConstants.MOD_ID, MPALIbConstants.ARMOR_WORKSTATION__ENTITY_TYPE_REGNAME).toString()));
}