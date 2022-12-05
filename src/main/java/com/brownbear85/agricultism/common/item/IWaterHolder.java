package com.brownbear85.agricultism.common.item;

import net.minecraft.world.item.ItemStack;

public interface IWaterHolder {
    /** in milibuckets */
    int getCapacity();

    default int getWater(ItemStack stack) {
        return stack.getOrCreateTag().getInt("storedWater");
    }

    default void setWater(ItemStack stack, int mB) {
        if (stack.getItem() instanceof IWaterHolder) {
            stack.getOrCreateTag().putInt("storedWater", Math.max(0, Math.min(mB, getCapacity())));
        }
    }

    /** */
    default void addWater(ItemStack stack, int mB) {
        if (stack.getItem() instanceof IWaterHolder) {
            this.setWater(stack, this.getWater(stack) + mB);
            this.getWater(stack);
            getCapacity();
        }
    }

    /** @return if the subtraction was successful
     * @param cancel if the subtraction should cancel if it failed */
    default boolean subtractWater(ItemStack stack, int mB, boolean cancel) {
        if (stack.getItem() instanceof IWaterHolder) {
            boolean successful = this.getWater(stack) >= mB;
            if (successful || !cancel) {
                this.addWater(stack, -mB);
            }
            return successful;
        }
        return false;
    }

    default int getWaterBarWidth(ItemStack stack) {
        this.setWater(stack, this.getWater(stack));
        return Math.round(13.0F * ((float)this.getWater(stack) / (float)this.getCapacity()));
    }
}
