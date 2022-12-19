package com.brownbear85.agricultism.common;

import com.brownbear85.agricultism.Agricultism;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.Map;

import static com.brownbear85.agricultism.Agricultism.BARK_2_LOGS_MAP;
import static com.brownbear85.agricultism.Agricultism.MODID;

public class ModEvents {

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class CommonModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }

    @Mod.EventBusSubscriber(modid = MODID)
    public static class CommonForgeEvents {

        @SubscribeEvent
        public static void blockModified(BlockEvent.BlockToolModificationEvent event) {
            if (event.getToolAction().equals(ToolActions.AXE_STRIP)) {
                ItemStack stack = ItemStack.EMPTY;
                for (Map.Entry<Item, TagKey<Block>> entry : BARK_2_LOGS_MAP.entrySet()) {
                    if (event.getState().is(entry.getValue()) && !Agricultism.STRIPPED_LOGS.contains(event.getState().getBlock())) {
                        stack = new ItemStack(entry.getKey());
                    }
                }
                Level level = event.getContext().getLevel();
                BlockPos pos = event.getPos();
                Direction face = event.getContext().getClickedFace();
                if (!stack.equals(ItemStack.EMPTY)) {
                    ItemEntity entity = new ItemEntity(level,
                            pos.getX() + 0.5 + face.getStepX() * 0.65,
                            pos.getY() + 0.5 + face.getStepY() * 0.65,
                            pos.getZ() + 0.5 + face.getStepZ() * 0.65,
                            stack);
                    level.addFreshEntity(entity);
                }
            }
        }

        @SubscribeEvent
        public static void farmlandTrampled(BlockEvent.FarmlandTrampleEvent event) {
            if (event.getFallDistance() < 2.5 ||
                    (event.getFallDistance() < 5 && !event.getLevel().getBlockState(event.getPos().above()).isAir())) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void playerRightClickedBlock(PlayerInteractEvent.RightClickBlock event) {
            Level level = event.getLevel();
            BlockPos pos = event.getHitVec().getBlockPos();
            BlockState state = level.getBlockState(pos);
            ItemStack stack = event.getItemStack();
            Player player = event.getEntity();
            InteractionHand hand = event.getHand();

            if (stack.is(Items.POTATO) || stack.is(Items.CARROT)) {
                event.setCanceled(true);
            }

            if (stack.getItem() instanceof PickaxeItem) {
                if (state.getBlock() instanceof FarmBlock) {
                    player.swing(hand);
                    stack.hurtAndBreak(1, player, (entity) -> {
                        entity.broadcastBreakEvent(hand);
                    });

                    level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.DRIPSTONE_BLOCK_FALL, SoundSource.PLAYERS, 1.0F, 1.0F);

                    FarmBlock.turnToDirt(state, level, pos);
                    return;
                }
                BlockPos belowPos = pos.below();
                BlockState below = level.getBlockState(pos.below());
                if (state.getBlock() instanceof IPlantable && below.getBlock() instanceof FarmBlock) {
                    player.swing(hand);
                    stack.hurtAndBreak(1, player, (entity) -> {
                        entity.broadcastBreakEvent(hand);
                    });

                    level.playSound(null, belowPos.getX() + 0.5, belowPos.getY() + 0.5, belowPos.getZ() + 0.5, SoundEvents.DRIPSTONE_BLOCK_FALL, SoundSource.PLAYERS, 1.0F, 1.0F);

                    FarmBlock.turnToDirt(below, level, belowPos);
                }
            }
        }
    }
}
