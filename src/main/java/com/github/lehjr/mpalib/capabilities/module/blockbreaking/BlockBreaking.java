//package net.machinemuse.numina.capabilities.module.blockbreaking;
//
//import net.minecraft.block.BlockState;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import net.minecraftforge.event.entity.player.PlayerEvent;
//
//import javax.annotation.Nonnull;
//
//public class BlockBreaking implements IBlockBreakingModule {
//    @Override
//    public boolean onBlockDestroyed(@Nonnull ItemStack itemStack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving, int playerEnergy) {
//        return false;
//    }
//
//    @Override
//    public void handleBreakSpeed(PlayerEvent.BreakSpeed event) {
//
//    }
//
//    @Override
//    public int getEnergyUsage() {
//        return 0;
//    }
//
//    @Nonnull
//    @Override
//    public ItemStack getEmulatedTool() {
//        return ItemStack.EMPTY;
//    }
//}
