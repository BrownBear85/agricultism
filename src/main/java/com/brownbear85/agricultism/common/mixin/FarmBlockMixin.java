package com.brownbear85.agricultism.common.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.FarmlandWaterManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmBlock.class)
public class FarmBlockMixin {
    private static final int RANGE = 1;

    @Inject(at = @At(value = "HEAD"), method = "randomTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V", cancellable = true)
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        ci.cancel();

        double rand = random.nextDouble();

        int moisture = state.getValue(FarmBlock.MOISTURE);
        if (!isNearWater(level, pos) && !level.isRainingAt(pos.above())) {
            if (moisture == 1 && rand > 0.9) {
                FarmBlock.turnToDirt(state, level, pos);
            } else if ((moisture == 7 && rand > 0.98) || (moisture > 0 && moisture < 7 && rand > 0.7)) {
                level.setBlock(pos, state.setValue(FarmBlock.MOISTURE, moisture - 1), 2);
            }
        } else if (moisture < 7) {
            level.setBlock(pos, state.setValue(FarmBlock.MOISTURE, 7), 2);
        }
    }

    private static boolean isNearWater(LevelReader levelReader, BlockPos pos) {
        boolean foundWater = false;
        BlockState state = levelReader.getBlockState(pos);
        for (BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-RANGE, 0, -RANGE), pos.offset(RANGE, 1, RANGE))) {
            if (state.canBeHydrated(levelReader, pos, levelReader.getFluidState(blockpos), blockpos)) {
                foundWater = true;
            }
        }

        return foundWater || FarmlandWaterManager.hasBlockWaterTicket(levelReader, pos);
    }
}
