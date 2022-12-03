package com.brownbear85.agricultism.common.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.FarmlandWaterManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FarmBlock.class)
public class FarmlandMixin {
    private static final int RANGE = 1;

    @Inject(at = @At(value = "HEAD"), method = "isNearWater(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;)Z", cancellable = true)
    private static void agricultism_isNearWater(LevelReader levelReader, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        cir.cancel();

        boolean foundWater = false;
        BlockState state = levelReader.getBlockState(pos);
        for (BlockPos blockpos : BlockPos.betweenClosed(pos.offset(-RANGE, 0, -RANGE), pos.offset(RANGE, 1, RANGE))) {
            if (state.canBeHydrated(levelReader, pos, levelReader.getFluidState(blockpos), blockpos)) {
                foundWater = true;
                cir.setReturnValue(true);
            }
        }

        cir.setReturnValue(!foundWater && FarmlandWaterManager.hasBlockWaterTicket(levelReader, pos));
    }
}
