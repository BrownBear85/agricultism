package com.brownbear85.agricultism.common.item;

import com.brownbear85.agricultism.common.enchantment.EnchantmentRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.NetherWartBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static com.brownbear85.agricultism.Agricultism.MODID;

public class HoeActions {
    public static boolean harvestCrop(Level level, BlockPos pos, Player player, InteractionHand hand) {
        boolean success = false;
        BlockState state = level.getBlockState(pos);
        ItemStack stack = player.getItemInHand(hand);

        if ((state.getBlock() instanceof CropBlock cropBlock && cropBlock.isMaxAge(state)) ||
            (state.is(Blocks.NETHER_WART) && state.getValue(NetherWartBlock.AGE) == NetherWartBlock.MAX_AGE)) {
            success = true;

            stack.hurtAndBreak(1, player, (entity1) -> {
                entity1.broadcastBreakEvent(hand);
            });

            level.setBlockAndUpdate(pos, state.getBlock().defaultBlockState());
            if (level instanceof ServerLevel serverLevel) {
                List<ItemStack> drops = Block.getDrops(state, serverLevel, pos, null, player, stack);

                List<ItemStack> seedDrops = Block.getDrops(state.getBlock().defaultBlockState(), serverLevel, pos, null, player, stack);

                for (ItemStack drop : drops) {
                    for (ItemStack seed : seedDrops) {
                        if (drop.is(seed.getItem())) {
                            int amount = Math.max(0, seed.getCount() - drop.getCount());
                            seed.shrink(amount);
                            drop.shrink(amount);
                            if (seed.isEmpty()) {
                                seedDrops.remove(seed);
                            }
                        }
                    }
                    if (drop.getCount() > 0) {
                        Block.popResource(serverLevel, pos, drop);
                    }
                }
                serverLevel.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, state.getBlock().getSoundType(state, level, pos, null).getBreakSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
                serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 30, 0.3, 0.3, 0.3, 0);
            }
        }
        return success;
    }

    @Mod.EventBusSubscriber(modid = MODID)
    public static class CommonForgeEvents {
        @SubscribeEvent
        public static void playerRightClickedBlock(PlayerInteractEvent.RightClickBlock event) {
            ItemStack stack = event.getItemStack();

            if (stack.getItem() instanceof HoeItem) {
                Player player = event.getEntity();
                Level level = player.getLevel();

                BlockHitResult result = event.getHitVec();
                InteractionHand hand = event.getHand();

                boolean swing = false;

                BlockPos centerPos = result.getBlockPos();
                int range = stack.getEnchantmentLevel(EnchantmentRegistry.HOE_RANGE.get());

                for (BlockPos pos : BlockPos.betweenClosed(centerPos.offset(-range, 0, -range), centerPos.offset(range, 0, range))) {
                    InteractionResult interactionResult = Items.WOODEN_HOE.useOn(new UseOnContext(player, hand, new BlockHitResult(new Vec3(pos.getX(), pos.getY(), pos.getZ()), result.getDirection(), pos, result.isInside())));
                    boolean harvested = harvestCrop(level, pos, player, hand);
                    if (!swing) {
                        swing = interactionResult == InteractionResult.SUCCESS || harvested;
                    }
                }

                if (swing) {
                    player.swing(hand);
                    event.setCanceled(true);
                }
            }
        }
    }
}
