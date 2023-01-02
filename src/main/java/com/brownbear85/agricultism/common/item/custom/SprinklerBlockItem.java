package com.brownbear85.agricultism.common.item.custom;

import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class SprinklerBlockItem extends BlockItem {
    public SprinklerBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    public InteractionResult useOn(UseOnContext context) {
        return InteractionResult.PASS;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (result.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }
        Direction dir = result.getDirection();
        BlockHitResult placePosition = result.withPosition(result.getBlockPos().offset(dir.getStepX(), dir.getStepY(), dir.getStepZ()));
        InteractionResult interactionResult = super.useOn(new UseOnContext(player, hand, placePosition));
        return new InteractionResultHolder<>(interactionResult, player.getItemInHand(hand));
    }
}
