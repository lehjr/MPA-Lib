package com.github.lehjr.mpalib.basemod;

import com.github.lehjr.mpalib.block.MPAArmorStandBaseBlock;
import com.github.lehjr.mpalib.client.render.item.MPAArmorStationItemRenderer;
import com.github.lehjr.mpalib.container.ArmorStandModdingContainer;
import com.github.lehjr.mpalib.container.CraftingContainer;
import com.github.lehjr.mpalib.entity.MPAArmorStandEntity;
import com.github.lehjr.mpalib.item.MPAArmorStandItem;
import com.github.lehjr.mpalib.tileentity.MPAArmorStandBaseTileEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModObjects {
    /**
     * Blocks ------------------------------------------------------------------------------------
     */
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MPALIbConstants.MOD_ID);

    public static final RegistryObject<MPAArmorStandBaseBlock> ARMOR_STATION_BLOCK = BLOCKS.register(MPALIbConstants.AMOR_WORKSTATION__REGNAME,
            () -> new MPAArmorStandBaseBlock());

    /**
     * Items -------------------------------------------------------------------------------------
     */
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MPALIbConstants.MOD_ID);

    public static final RegistryObject<Item> ARMOR_STATION_ITEM = ITEMS.register(MPALIbConstants.AMOR_WORKSTATION__REGNAME,
            () -> new MPAArmorStandItem(ARMOR_STATION_BLOCK.get(),
                    new Item.Properties().group(ItemGroup.DECORATIONS).setISTER(() -> MPAArmorStationItemRenderer::new)));

    /**
     * Tile Entity Types -------------------------------------------------------------------------
     */
    public static final DeferredRegister<TileEntityType<?>> TILE_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MPALIbConstants.MOD_ID);

    public static final RegistryObject<TileEntityType<MPAArmorStandBaseTileEntity>> ARMOR_STATION_TILE = TILE_TYPES.register(MPALIbConstants.AMOR_WORKSTATION__REGNAME,
            () -> TileEntityType.Builder.create(MPAArmorStandBaseTileEntity::new, ARMOR_STATION_BLOCK.get()).build(null));


    /**
     * Entity Types ------------------------------------------------------------------------------
     */
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, MPALIbConstants.MOD_ID);

    public static final RegistryObject<EntityType<MPAArmorStandEntity>> ARMOR_WORKSTATION__ENTITY_TYPE = ENTITY_TYPES.register(MPALIbConstants.ARMOR_WORKSTATION__ENTITY_TYPE_REGNAME,
            () -> EntityType.Builder.create(MPAArmorStandEntity::new, EntityClassification.CREATURE)
                    .size(0.5F, 1.975F) // Hitbox Size
                    .build(new ResourceLocation(MPALIbConstants.MOD_ID, MPALIbConstants.ARMOR_WORKSTATION__ENTITY_TYPE_REGNAME).toString()));

    /**
     * Container Types ---------------------------------------------------------------------------
     */
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, MPALIbConstants.MOD_ID);

    public static final RegistryObject<ContainerType<ArmorStandModdingContainer>> ARMOR_STAND_MODDING_CONTAINER_CONTAINER_TYPE = CONTAINER_TYPES.register("armorstand_modding_container",
            () -> IForgeContainerType.create((windowId, inv, data) -> {
//        BlockPos pos = data.readBlockPos();
                return new ArmorStandModdingContainer(windowId, inv);
            }));

    public static final RegistryObject<ContainerType<CraftingContainer>> CRAFTING_CONTAINER_TYPE = CONTAINER_TYPES.register("crafting_container",
            () -> IForgeContainerType.create((windowId, inv, data) -> {
//        BlockPos pos = data.readBlockPos();
                return new CraftingContainer(windowId, inv);
            }));
}