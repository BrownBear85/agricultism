package com.brownbear85.agricultism.common.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WateringCanItem extends Item implements IWaterHolder {
    protected final int storage;
    protected final int range;

    public WateringCanItem(Properties properties, int storage, int range) {
        super(properties);
        this.storage = storage;
        this.range = range;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (result.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = result.getBlockPos();
            BlockState state = level.getBlockState(pos);
            if (state.is(Blocks.WATER)) {
                if (getWater(stack) >= getCapacity()) {
                    return InteractionResultHolder.fail(stack);
                }
                setWater(stack, getCapacity());
                level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BUCKET_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);
                return InteractionResultHolder.success(stack);
            } else {
                int usedRange = player.isCrouching() ? 0 : range;
                if (!player.getAbilities().instabuild) {
                    if (!subtractWater(stack, (usedRange * 2 + 1) * (usedRange * 2 + 1) * 250, true)) {
                        level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.SOUL_SAND_STEP, SoundSource.PLAYERS, 1.0F, 1.0F);
                        return InteractionResultHolder.consume(stack);
                    }
                }
                level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BUCKET_EMPTY_TADPOLE, SoundSource.PLAYERS, 1.0F, 1.0F);
                for (BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-usedRange, 0, -usedRange), pos.offset(usedRange, -1, usedRange))) {
                    BlockState blockstate = level.getBlockState(blockpos);
                    if (blockstate.getBlock() instanceof FarmBlock) {
                        level.setBlock(blockpos, blockstate.setValue(FarmBlock.MOISTURE, FarmBlock.MAX_MOISTURE), 3);
                    }
                    if (level instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.FALLING_WATER, blockpos.getX() + 0.5, blockpos.getY() + 1.2, blockpos.getZ() + 0.5, 12, 0.25, 0, 0.25, 0);
                    }
                }
                return InteractionResultHolder.consume(stack);
            }
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public int getCapacity() {
        return storage;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return getWaterBarWidth(stack);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 2895103;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(
            Component.translatable("item.agricultism.watering_can.storedWater",
                Component.literal(String.valueOf(getWater(stack))).withStyle(ChatFormatting.BLUE),
                Component.literal(String.valueOf(getCapacity())).withStyle(ChatFormatting.BLUE)
            ).withStyle(ChatFormatting.DARK_GRAY));

        if (range == 0) {
            tooltip.add(Component.translatable("item.agricultism.watering_can.range_1").withStyle(ChatFormatting.DARK_GRAY));
        } else {
            tooltip.add(
                Component.translatable("item.agricultism.watering_can.range",
                    Component.literal(String.valueOf(range + 1)).withStyle(ChatFormatting.GRAY)
                ).withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        stack.getOrCreateTag().putInt("storedWater", 0);
        return stack;
    }
}
