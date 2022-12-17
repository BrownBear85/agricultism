package com.brownbear85.agricultism.common.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PeltItem extends Item {
    public PeltItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        stack.getOrCreateTag().putInt("quality", 3);
        return stack;
    }

    public static int getQuality(ItemStack stack) {
        return stack.getOrCreateTag().getInt("quality");
    }
}
