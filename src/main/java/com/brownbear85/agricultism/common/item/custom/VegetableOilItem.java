package com.brownbear85.agricultism.common.item.custom;

import com.brownbear85.agricultism.Util;
import com.brownbear85.agricultism.common.block.BlockRegistry;
import com.brownbear85.agricultism.common.item.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class VegetableOilItem extends Item {
    public VegetableOilItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        ItemStack stack = pLivingEntity.eat(pLevel, pStack);
        if (pLivingEntity instanceof Player player && !player.getAbilities().instabuild) {
            if (pStack.isEmpty()) {
                return Util.item(Items.GLASS_BOTTLE);
            }
            player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
        }
        return stack;
    }

    public int getUseDuration(ItemStack pStack) {
        return 40;
    }

    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.DRINK;
    }

    public SoundEvent getDrinkingSound() {
        return SoundEvents.HONEY_DRINK;
    }

    public SoundEvent getEatingSound() {
        return SoundEvents.HONEY_DRINK;
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        return ItemUtils.startUsingInstantly(pLevel, pPlayer, pHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (pContext.getLevel().getBlockState(pContext.getClickedPos()).is(Blocks.CAULDRON)) {
            Level level = pContext.getLevel();
            BlockPos pos = pContext.getClickedPos();
            BlockState state = pContext.getLevel().getBlockState(pos);
            ItemStack stack = pContext.getItemInHand();
            if (!level.isClientSide && state.is(Blocks.CAULDRON) && stack.is(ItemRegistry.VEGETABLE_OIL_BOTTLE.get())) {
                Player player = pContext.getPlayer();
                if (player != null) {
                    player.swing(pContext.getHand());
                    player.setItemInHand(pContext.getHand(), ItemUtils.createFilledResult(stack, player, Util.item(Items.GLASS_BOTTLE)));
                }
                level.setBlockAndUpdate(pos, BlockRegistry.VEGETABLE_OIL_CAULDRON.get().defaultBlockState());
                level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }
}
