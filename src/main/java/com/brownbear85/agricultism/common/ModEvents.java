package com.brownbear85.agricultism.common;

import com.brownbear85.agricultism.common.enchantment.EnchantmentRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
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
        public static void farmlandTrampled(BlockEvent.FarmlandTrampleEvent event) {
            if (event.getFallDistance() < 2.5 ||
                    (event.getFallDistance() < 5 && !event.getLevel().getBlockState(event.getPos().above()).isAir())) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void playerRightClickedBlock(PlayerInteractEvent.RightClickBlock event) {
            ItemStack stack = event.getItemStack();

            if (stack.is(Items.POTATO) || stack.is(Items.CARROT)) {
                event.setCanceled(true);
            }
            if (stack.getItem() instanceof HoeItem) {
                Player player = event.getEntity();
                Level level = player.getLevel();

                HitResult result = Minecraft.getInstance().hitResult;
                if (result != null && result.getType() == HitResult.Type.BLOCK) {
                    InteractionHand hand = event.getHand();

                    boolean swing = false;

                    BlockPos centerPos = ((BlockHitResult) result).getBlockPos();
                    int range = stack.getEnchantmentLevel(EnchantmentRegistry.HOE_RANGE.get());

                    if (range > 0) {
                        event.setCanceled(true);
                        for (BlockPos pos : BlockPos.betweenClosed(centerPos.offset(-range, 0, -range), centerPos.offset(range, 0, range))) {
                            InteractionResult interactionResult = Items.WOODEN_HOE.useOn(new UseOnContext(player, hand, new BlockHitResult(new Vec3(pos.getX(), pos.getY(), pos.getZ()), ((BlockHitResult) result).getDirection(), pos, ((BlockHitResult) result).isInside())));
                            boolean harvested = harvestCrop(level, pos, player, stack, hand);
                            if (!swing) {
                                swing = interactionResult == InteractionResult.SUCCESS || harvested;
                            }

                        }
                    } else {
                        swing = harvestCrop(level, centerPos, player, stack, hand);
                    }

                    if (swing) {
                        player.swing(hand);
                    }
                }
            }
        }
    }

    public static boolean harvestCrop(Level level, BlockPos pos, Player player, ItemStack stack, InteractionHand hand) {
        boolean success = false;
        BlockState state = level.getBlockState(pos);

        if (state.getBlock() instanceof CropBlock cropBlock && cropBlock.isMaxAge(state)) {
            success = true;

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
        return success;
    }
}
