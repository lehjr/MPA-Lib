//package net.machinemuse.numina.capabilities.module.enchantment;
//
//import net.minecraft.nbt.INBT;
//import net.minecraft.util.Direction;
//import net.minecraftforge.common.capabilities.Capability;
//import net.minecraftforge.common.capabilities.CapabilityInject;
//import net.minecraftforge.common.capabilities.CapabilityManager;
//
//public class EnchantmentCapability {
//    @CapabilityInject(IEnchantmentModule.class)
//    public static Capability<IEnchantmentModule> ENCHANTMENT_MODULE = null;
//
//    public static void register() {
//        CapabilityManager.INSTANCE.register(IEnchantmentModule.class, new Capability.IStorage<IEnchantmentModule>() {
//                    @Override
//                    public INBT writeNBT(Capability<IEnchantmentModule> capability, IEnchantmentModule instance, Direction side) {
//                        return null;
//                    }
//
//                    @Override
//                    public void readNBT(Capability<IEnchantmentModule> capability, IEnchantmentModule instance, Direction side, INBT nbt) {
//                        if (!(instance instanceof EnchantmentModule))
//                            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
//
//                    }
//                },
//                () -> new EnchantmentModule());
//    }
//}