package com.github.lehjr.mpalib.tileentity;

import com.github.lehjr.mpalib.basemod.ModObjects;
import net.minecraft.util.Direction;

public class MPAArmorStandBaseTileEntity extends MPALibTileEntity {
    Direction facing;

    public MPAArmorStandBaseTileEntity() {
        super(ModObjects.ARMOR_STATION_TILE.get());
        this.facing = Direction.NORTH;
    }


}