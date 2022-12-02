package com.brownbear85.agricultism.common.enchantment;

import com.brownbear85.agricultism.Agricultism;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EnchantmentRegistry {
    public static final EnchantmentCategory HOES = EnchantmentCategory.create("HOES", (item) -> item instanceof HoeItem);

    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Agricultism.MODID);

    public static final RegistryObject<Enchantment> HOE_RANGE = ENCHANTMENTS.register("cultivating", () -> new HoeRangeEnchantment(Enchantment.Rarity.RARE, HOES, EquipmentSlot.MAINHAND));
}
