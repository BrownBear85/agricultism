package com.brownbear85.agricultism.common.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class WateringCan extends Item implements IWaterHolder{
    public WateringCan(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        BlockState state = level.getBlockState(pos);
        ItemStack stack = context.getItemInHand();
        if (state.is(Blocks.WATER)) {
            addWater(stack, 1000);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public int getCapacity() {
        return 1000;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return isWaterBarVisible();
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return getWaterBarWidth(stack);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return getWaterBarColor();
    }
}
