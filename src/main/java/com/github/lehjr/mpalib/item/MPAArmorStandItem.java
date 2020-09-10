package com.github.lehjr.mpalib.item;

import com.github.lehjr.mpalib.basemod.ModObjects;
import com.github.lehjr.mpalib.entity.MPAArmorStandEntity;
import com.github.lehjr.mpalib.tileentity.MPAArmorStandBaseTileEntity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class MPAArmorStandItem extends BlockItem {
    public MPAArmorStandItem(Block block, Item.Properties builder) {
        super(block, builder);
    }

    /**
     * Called when this item is used while targetting a Block
     *
     * This is a mix of vanilla code from BlockItem and ArmorStandItem so the block and
     * entity are spawned together
     */
//    @Override
    public ActionResultType tryPlace1(BlockItemUseContext context) {
        Direction direction = context.getFace();

        if (!context.canPlace() || direction == Direction.DOWN) {
            return ActionResultType.FAIL;
        }

        BlockItemUseContext blockitemusecontext = new BlockItemUseContext(context);
        if (blockitemusecontext == null) {
            return ActionResultType.FAIL;
        }

        BlockState blockstate = this.getStateForPlacement(blockitemusecontext);
        if (blockstate == null) {
            return ActionResultType.FAIL;
        }

        World world = blockitemusecontext.getWorld();
        BlockPos blockpos = blockitemusecontext.getPos();
        Vector3d vector3d = Vector3d.copyCenteredHorizontally(blockpos);
        AxisAlignedBB axisalignedbb = EntityType.ARMOR_STAND.getSize().func_242285_a(vector3d.getX(), vector3d.getY(), vector3d.getZ());
        if (world.func_234865_b_(null, axisalignedbb, (entity) -> true) && world.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb).isEmpty()) {
            if (!this.placeBlock(blockitemusecontext, blockstate)) {
                return ActionResultType.FAIL;
            }

            ItemStack itemstack = blockitemusecontext.getItem();
            BlockState blockstate1 = world.getBlockState(blockpos);
            PlayerEntity player = blockitemusecontext.getPlayer();
            Block block = blockstate1.getBlock();
            if (block == blockstate.getBlock()) {
                blockstate1 = this.func_219985_a(blockpos, world, itemstack, blockstate1);
                this.onBlockPlaced(blockpos, world, player, itemstack, blockstate1);
                block.onBlockPlacedBy(world, blockpos, blockstate1, player, itemstack);
                if (player instanceof ServerPlayerEntity && world instanceof ServerWorld) {
                    CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity)player, blockpos, itemstack);

                    ServerWorld serverworld = (ServerWorld)world;
                    MPAArmorStandEntity armorstandentity = ModObjects.ARMOR_WORKSTATION__ENTITY_TYPE.get().create(serverworld, itemstack.getTag(), null, context.getPlayer(), blockpos, SpawnReason.SPAWN_EGG, true, true);
                    if (armorstandentity == null) {
                        return ActionResultType.FAIL;
                    }
                    serverworld.func_242417_l(armorstandentity);
                    float f = (float)MathHelper.floor((MathHelper.wrapDegrees(context.getPlacementYaw() - 180.0F) + 22.5F) / 45.0F) * 45.0F;
                    armorstandentity.setLocationAndAngles(armorstandentity.getPosX(), armorstandentity.getPosY(), armorstandentity.getPosZ(), f, 0.0F);
                    this.applyRandomRotations(armorstandentity, world.rand);

                    TileEntity tileEntity = world.getTileEntity(blockpos);
                    // sets the UUID for this entity instance in TileEntity so the coprrect entity instance can be interacted with
                    if (tileEntity instanceof MPAArmorStandBaseTileEntity) {
                        ((MPAArmorStandBaseTileEntity) tileEntity).setUUID(armorstandentity.getUniqueID());
                        armorstandentity.setBlockTilePos(blockpos);
                    }
                    world.playSound(null, armorstandentity.getPosX(), armorstandentity.getPosY(), armorstandentity.getPosZ(), SoundEvents.ENTITY_ARMOR_STAND_PLACE, SoundCategory.BLOCKS, 0.75F, 0.8F);
                }
            }
            if (player == null || !player.abilities.isCreativeMode) {
                itemstack.shrink(1);
            }
            return ActionResultType.func_233537_a_(world.isRemote);
        }
        return ActionResultType.FAIL;
    }

    private void applyRandomRotations(MPAArmorStandEntity armorStand, Random rand) {
        Rotations rotations = armorStand.getHeadRotation();
        float f = rand.nextFloat() * 5.0F;
        float f1 = rand.nextFloat() * 20.0F - 10.0F;
        Rotations rotations1 = new Rotations(rotations.getX() + f, rotations.getY() + f1, rotations.getZ());
        armorStand.setHeadRotation(rotations1);
        rotations = armorStand.getBodyRotation();
        f = rand.nextFloat() * 10.0F - 5.0F;
        rotations1 = new Rotations(rotations.getX(), rotations.getY() + f, rotations.getZ());
        armorStand.setBodyRotation(rotations1);
    }
}