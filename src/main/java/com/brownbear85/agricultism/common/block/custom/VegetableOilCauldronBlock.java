package com.brownbear85.agricultism.common.block.custom;

import com.brownbear85.agricultism.Util;
import com.brownbear85.agricultism.common.block.BlockRegistry;
import com.brownbear85.agricultism.common.block.entities.BlockEntityRegistry;
import com.brownbear85.agricultism.common.block.entities.VegetableOilCauldronBlockEntity;
import com.brownbear85.agricultism.common.item.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class VegetableOilCauldronBlock extends LayeredCauldronBlock implements EntityBlock {

    public VegetableOilCauldronBlock(Properties pProperties, Map<Item, CauldronInteraction> pInteractions) {
        super(pProperties, (precipitation) -> false, pInteractions);
    }

    public static void registerInteractions() {
        BlockRegistry.VEGETABLE_OIL_INTERACTIONS.put(ItemRegistry.VEGETABLE_OIL_BOTTLE.get(), (state, level, pos, player, hand, stack) -> {
            if (level.getBlockEntity(pos) instanceof VegetableOilCauldronBlockEntity entity && entity.getRenderStack() == ItemStack.EMPTY && state.getValue(LayeredCauldronBlock.LEVEL) != 3) {
                if (!level.isClientSide) {
                    player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, Util.item(Items.GLASS_BOTTLE)));
                    level.setBlockAndUpdate(pos, state.cycle(LayeredCauldronBlock.LEVEL));
                    level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });

        BlockRegistry.VEGETABLE_OIL_INTERACTIONS.put(Items.GLASS_BOTTLE, (state, level, pos, player, hand, stack) -> {
            if (level.getBlockEntity(pos) instanceof VegetableOilCauldronBlockEntity entity && entity.getRenderStack() == ItemStack.EMPTY) {
                if (!level.isClientSide) {
                    player.setItemInHand(hand, ItemUtils.createFilledResult(stack, player, Util.item(ItemRegistry.VEGETABLE_OIL_BOTTLE.get())));
                    LayeredCauldronBlock.lowerFillLevel(state, level, pos);
                    level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });

        BlockRegistry.VEGETABLE_OIL_INTERACTIONS.put(ItemRegistry.CUT_ANIMAL_HIDE.get(), (state, level, pos, player, hand, stack) -> {
            if (level.getBlockEntity(pos) instanceof VegetableOilCauldronBlockEntity entity && entity.getRenderStack() == ItemStack.EMPTY) {
                entity.setItem(1);
                level.playSound(null, pos, SoundEvents.VILLAGER_WORK_LEATHERWORKER, SoundSource.BLOCKS, 1.0F, 1.0F);
                stack.shrink(1);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        });

        BlockRegistry.VEGETABLE_OIL_INTERACTIONS.put(Items.STICK, (state, level, pos, player, hand, stack) -> {
            if (level.getBlockEntity(pos) instanceof VegetableOilCauldronBlockEntity entity) {
                if (entity.canStir()) {
                    level.playSound(null, pos, SoundEvents.BOAT_PADDLE_WATER, SoundSource.BLOCKS, 1.0F, 1.0F);
                    entity.stir();
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.FAIL;
        });
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        InteractionResult interactionResult = super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
        if (!interactionResult.consumesAction()) {
            if (pLevel.getBlockEntity(pPos) instanceof VegetableOilCauldronBlockEntity entity) {
                if (!entity.canTakeItem()) {
                    return InteractionResult.sidedSuccess(pLevel.isClientSide);
                }
                ItemStack cauldronStack = entity.getRenderStack();
                if (cauldronStack != ItemStack.EMPTY) {
                    if (cauldronStack.is(ItemRegistry.PRESERVED_ANIMAL_HIDE.get())) {
                        LayeredCauldronBlock.lowerFillLevel(pState, pLevel, pPos);
                    }
                    entity.setItem(0);

                    ItemStack stack = pPlayer.getItemInHand(pHand);
                    if (stack.isEmpty()) {
                        pPlayer.setItemInHand(InteractionHand.MAIN_HAND, cauldronStack);
                    } else if (stack.sameItem(cauldronStack) && stack.getCount() < stack.getMaxStackSize()) {
                        stack.grow(1);
                    } else {
                        if (!pPlayer.addItem(cauldronStack)) {
                            pPlayer.drop(cauldronStack, false);
                        }
                    }

                    pLevel.playSound(null, pPos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return interactionResult;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.is(pNewState.getBlock())) {
            if (pLevel.getBlockEntity(pPos) instanceof VegetableOilCauldronBlockEntity blockEntity) {
                blockEntity.drop();
            }

            super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    /* block entity */

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new VegetableOilCauldronBlockEntity(pPos, pState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pServerType) {
        return Util.createTickerHelper(pServerType, BlockEntityRegistry.VEGETABLE_OIL_CAULDRON_BLOCK_ENTITY.get(), VegetableOilCauldronBlockEntity::tick);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
        return Items.CAULDRON.getDefaultInstance();
    }
}