package com.github.lehjr.mpalib.tileentity;

import com.github.lehjr.mpalib.basemod.ModObjects;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;

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
}