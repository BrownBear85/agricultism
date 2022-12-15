package com.brownbear85.agricultism.common.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CreativeWateringCanItem extends WateringCanItem {
    public CreativeWateringCanItem(Item.Properties properties, int storage, int range) {
        super(properties, storage, range);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        stack.getOrCreateTag().putInt("storedWater", storage);
        return stack;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
