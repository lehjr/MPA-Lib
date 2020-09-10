package com.github.lehjr.mpalib.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * TODO: decide if keeping this or just using vanilla armor stand. This really doesn't do anything, ... YET?
 */


public class MPAArmorStandEntity extends ArmorStandEntity {
    private static final BlockPos DEFAULT_TILE_BLOCK_POS = BlockPos.ZERO;
    public static final DataParameter<BlockPos> TILE_BLOCK_POS = EntityDataManager.createKey(MPAArmorStandEntity.class, DataSerializers.BLOCK_POS);
    private BlockPos tileBlockPos = DEFAULT_TILE_BLOCK_POS;

    public MPAArmorStandEntity(EntityType<? extends MPAArmorStandEntity> entityType, World world) {
        super(entityType, world);
    }

    public MPAArmorStandEntity(World worldIn, double posX, double posY, double posZ) {
        super(worldIn, posX, posY, posZ);
    }

    //func_233666_p_ ---> registerAttributes()
    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 10.0D);
    }


    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(TILE_BLOCK_POS, DEFAULT_TILE_BLOCK_POS);
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        if (tileBlockPos != DEFAULT_TILE_BLOCK_POS) {
            CompoundNBT nbt = NBTUtil.writeBlockPos(tileBlockPos);
            compound.put("tilePos", nbt);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        // position of the tile entity and base plate block
        if (compound.contains("tilePos")) {
            CompoundNBT nbt = compound.getCompound("tilePos");
            this.dataManager.set(TILE_BLOCK_POS, NBTUtil.readBlockPos(nbt));
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void tick() {
        super.tick();
        BlockPos tileLocation = this.dataManager.get(TILE_BLOCK_POS);
        if (!this.tileBlockPos.equals(tileLocation)) {
            this.setBlockTilePos(tileLocation);
        }
    }

    /**
     * Sets the position of the associated tile entity and block
     *
     * @param pos
     */
    public void setBlockTilePos(BlockPos pos) {
        this.tileBlockPos = pos;
        this.dataManager.set(TILE_BLOCK_POS, pos);
    }
}
