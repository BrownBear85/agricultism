package com.brownbear85.agricultism.common.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class QualityItem extends Item {
    public final int defaultQuality;

    public QualityItem(int defaultQuality, Item.Properties properties) {
        super(properties);
        this.defaultQuality = defaultQuality;
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        stack.getOrCreateTag().putInt("quality", defaultQuality);
        return stack;
    }

    public static int getQuality(ItemStack stack) {
        return stack.getOrCreateTag().getInt("quality");
    }
}
