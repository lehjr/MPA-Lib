package com.github.lehjr.mpalib.basemod;

import com.github.lehjr.mpalib.block.ChargingBaseBlock;
import com.github.lehjr.mpalib.client.render.item.MPAArmorStationItemRenderer;
import com.github.lehjr.mpalib.container.ArmorStandContainer;
import com.github.lehjr.mpalib.container.ChargingBaseContainer;
import com.github.lehjr.mpalib.container.CraftingContainer;
import com.github.lehjr.mpalib.entity.MPAArmorStandEntity;
import com.github.lehjr.mpalib.item.Battery;
import com.github.lehjr.mpalib.item.MPAArmorStandItem;
import com.github.lehjr.mpalib.tileentity.ChargingBaseTileEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModObjects {
    public static final MPALibCreativeTab creativeTab = new MPALibCreativeTab();

    /**
     * Blocks ------------------------------------------------------------------------------------
     */
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MPALIbConstants.MOD_ID);

    public static final RegistryObject<ChargingBaseBlock> CHARGING_BASE_BLOCK = BLOCKS.register(MPALIbConstants.CHARGING_BASE_REGNAME,
            () -> new ChargingBaseBlock());

    /**
     * Items -------------------------------------------------------------------------------------
     */
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MPALIbConstants.MOD_ID);

    // basic battery
    public static final RegistryObject<Item> BASIC_BATTERY = ITEMS.register(MPALIbConstants.MODULE_BATTERY_BASIC__REGNAME,
            () -> new Battery(1000000, 1000000));

    // advanced battery
    public static final RegistryObject<Item> ADVANCED_BATTERY = ITEMS.register(MPALIbConstants.MODULE_BATTERY_ADVANCED__REGNAME,
            () -> new Battery(5000000,5000000));

    // elite battery
    public static final RegistryObject<Item> ELITE_BATTERY = ITEMS.register(MPALIbConstants.MODULE_BATTERY_ELITE__REGNAME,
            () -> new Battery(50000000,50000000));

    // ultimate battery
    public static final RegistryObject<Item> ULTIMATE_BATTERY = ITEMS.register(MPALIbConstants.MODULE_BATTERY_ULTIMATE__REGNAME,
            () -> new Battery(100000000,100000000));

    // charging base
    public static final RegistryObject<Item> CHARGING_BASE_ITEM = ITEMS.register(MPALIbConstants.CHARGING_BASE_REGNAME,
            () -> new BlockItem(CHARGING_BASE_BLOCK.get(),
                    new Item.Properties().group(ItemGroup.DECORATIONS)));

    // Armor stand
    public static final RegistryObject<Item> ARMOR_STAND_ITEM = ITEMS.register(MPALIbConstants.ARMORSTAND_REGNAME,
            () -> new MPAArmorStandItem(new Item.Properties().group(ItemGroup.DECORATIONS).setISTER(() -> MPAArmorStationItemRenderer::new)));


    /**
     * Tile Entity Types -------------------------------------------------------------------------
     */
    public static final DeferredRegister<TileEntityType<?>> TILE_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MPALIbConstants.MOD_ID);

    public static final RegistryObject<TileEntityType<ChargingBaseTileEntity>> CHARGING_BASE_TILE = TILE_TYPES.register(MPALIbConstants.CHARGING_BASE_REGNAME,
            () -> TileEntityType.Builder.create(ChargingBaseTileEntity::new, CHARGING_BASE_BLOCK.get()).build(null));


    /**
     * Entity Types ------------------------------------------------------------------------------
     */
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, MPALIbConstants.MOD_ID);

    public static final RegistryObject<EntityType<MPAArmorStandEntity>> ARMOR_WORKSTATION__ENTITY_TYPE = ENTITY_TYPES.register(MPALIbConstants.ARMOR_STAND__ENTITY_TYPE_REGNAME,
            () -> EntityType.Builder.<MPAArmorStandEntity>create(MPAArmorStandEntity::new, EntityClassification.CREATURE)
                    .size(0.5F, 1.975F) // Hitbox Size
                    .build(new ResourceLocation(MPALIbConstants.MOD_ID, MPALIbConstants.ARMOR_STAND__ENTITY_TYPE_REGNAME).toString()));

    /**
     * Container Types ---------------------------------------------------------------------------
     */
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, MPALIbConstants.MOD_ID);

    public static final RegistryObject<ContainerType<ArmorStandContainer>> ARMOR_STAND_CONTAINER_TYPE = CONTAINER_TYPES.register("armorstand_modding_container",
            () -> IForgeContainerType.create((windowId, inv, data) -> {
                int entityID = data.readInt();
                Entity armorStand = inv.player.world.getEntityByID(entityID);

                if (armorStand instanceof ArmorStandEntity) {
                    return new ArmorStandContainer(windowId, inv, (ArmorStandEntity) armorStand);
                }
                return null;
            }));

    public static final RegistryObject<ContainerType<ChargingBaseContainer>> CHARGING_BASE_CONTAINER_TYPE = CONTAINER_TYPES.register("charging_base", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        World world = inv.player.getEntityWorld();
        return new ChargingBaseContainer(windowId, world, pos, inv, inv.player);
    }));

    public static final RegistryObject<ContainerType<CraftingContainer>> CRAFTING_CONTAINER_TYPE = CONTAINER_TYPES.register("crafting_container",
            () -> IForgeContainerType.create((windowId, inv, data) -> {
//        BlockPos pos = data.readBlockPos();
                return new CraftingContainer(windowId, inv);
            }));
}