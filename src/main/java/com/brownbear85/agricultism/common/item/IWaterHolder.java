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

    /** @return leftover mB */
    default int addWater(ItemStack stack, int mB) {
        if (stack.getItem() instanceof IWaterHolder) {
            this.setWater(stack, this.getWater(stack) + mB);
            return Math.max(0, this.getWater(stack) + mB - getCapacity());
        }
        return 0;
    }

    /** @return if operation is possible (has enough water)
     * @param cancel should continue with operation if there isn't enough water (set to zero) */
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

    default boolean isWaterBarVisible() {
        return true;
    }

    default int getWaterBarWidth(ItemStack stack) {
        return Math.round(13.0F - (float)this.getWater(stack) * 13.0F / (float)this.getCapacity());
    }

    default int getWaterBarColor() {
        return 255;
    }
}
