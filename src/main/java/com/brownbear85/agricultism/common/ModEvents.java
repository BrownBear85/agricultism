package com.brownbear85.agricultism.common;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.brownbear85.agricultism.Agricultism.MODID;

public class ModEvents {
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }

    @Mod.EventBusSubscriber(modid = MODID)
    public static class ForgeBusEvents {

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
            if (stack.getItem() instanceof PickaxeItem) {
                if (state.getBlock() instanceof FarmBlock) {
                    player.swing(hand);
                    stack.hurtAndBreak(1, player, (entity) -> {
                        entity.broadcastBreakEvent(hand);
                    });

                    level.playSeededSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.DRIPSTONE_BLOCK_FALL, SoundSource.PLAYERS, 1.0F, 1.0F, level.random.nextLong());

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

                    level.playSeededSound(null, belowPos.getX() + 0.5, belowPos.getY() + 0.5, belowPos.getZ() + 0.5, SoundEvents.DRIPSTONE_BLOCK_FALL, SoundSource.PLAYERS, 1.0F, 1.0F, level.random.nextLong());

                    FarmBlock.turnToDirt(below, level, belowPos);
                }
            }
        }
    }
}
