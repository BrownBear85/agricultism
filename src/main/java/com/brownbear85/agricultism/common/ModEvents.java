package com.brownbear85.agricultism.common;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.List;

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
        public static void onFarmlandTrampled(BlockEvent.FarmlandTrampleEvent event) {
            if (event.getFallDistance() < 2 ||
                    (event.getFallDistance() < 3 && !event.getLevel().getBlockState(event.getPos().above()).isAir())) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onUseItem(PlayerInteractEvent.RightClickBlock event) {
            if (event.getItemStack().getItem() instanceof HoeItem) {
                Player player = event.getEntity();
                Level level = player.getLevel();

                HitResult result = Minecraft.getInstance().hitResult;
                if (result != null && result.getType() == HitResult.Type.BLOCK) {
                    BlockPos pos = ((BlockHitResult) result).getBlockPos();
                    BlockState state = level.getBlockState(pos);

                    if (state.getBlock() instanceof CropBlock cropBlock && cropBlock.isMaxAge(state)) {
                        InteractionHand hand = event.getHand();
                        player.swing(hand);

                        ItemStack stack = event.getItemStack();
                        stack.hurtAndBreak(1, player, (entity1) -> {
                            entity1.broadcastBreakEvent(hand);
                        });

                        level.setBlockAndUpdate(pos, state.getBlock().defaultBlockState());
                        if (level instanceof ServerLevel serverLevel) {
                            List<ItemStack> drops = Block.getDrops(state, serverLevel, pos, null, player, stack);

                            boolean takenSeed = false;
                            for (ItemStack drop : drops) {
                                if (drop.getItem() instanceof ItemNameBlockItem) {
                                    if (!takenSeed) {
                                        drop.shrink(1);
                                        takenSeed = true;
                                    }
                                }
                                if (drop.getCount() > 0) {
                                    Block.popResource(serverLevel, pos, drop);
                                }
                            }
                            serverLevel.playSeededSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F, level.random.nextLong());
                            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 30, 0.3, 0.3, 0.3, 0);
                        }
                    }
                }
            }
        }
    }
}
