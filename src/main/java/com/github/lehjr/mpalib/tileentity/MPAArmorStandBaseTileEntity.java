package com.github.lehjr.mpalib.tileentity;

import com.github.lehjr.mpalib.basemod.ModObjects;
import com.github.lehjr.mpalib.entity.MPAArmorStandEntity;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class MPAArmorStandBaseTileEntity extends MPALibTileEntity {
    UUID uuid;

    public MPAArmorStandBaseTileEntity() {
        super(ModObjects.ARMOR_STATION_TILE.get());
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        super.write(nbt);
        if(uuid != null) {
            nbt.putUniqueId("Id", uuid);
        }
        return nbt;
    }

    @Override
    public void read(BlockState stateIn, CompoundNBT nbt) {
        super.read(stateIn, nbt);
        if (nbt.hasUniqueId("Id")) {
            uuid = nbt.getUniqueId("Id");
        }
    }

    @Nullable
    public MPAArmorStandEntity getEntity() {
        if (uuid != null) {
            List<MPAArmorStandEntity> list = world.getEntitiesWithinAABB(MPAArmorStandEntity.class, new AxisAlignedBB(pos), entity -> entity.getUniqueID() == uuid);
            if (list.size() == 1) {
                return list.get(0);
            }
        }
        return null;
    }

    @Override
    public void remove() {
        if(uuid != null) {
            MPAArmorStandEntity entity = getEntity();
            if (entity != null) {
                // FIXME!!!!  drop items
                // entity.remove();
            }
        }
        super.remove();
    }
}