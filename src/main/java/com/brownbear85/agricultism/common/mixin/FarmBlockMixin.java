package com.brownbear85.agricultism.common.mixin;

import com.brownbear85.agricultism.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.piston.MovingPistonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.FarmlandWaterManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmBlock.class)
public class FarmBlockMixin {

    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        double rand = random.nextDouble();

        int moisture = state.getValue(FarmBlock.MOISTURE);
        if (!isWatered(level, pos) && !level.isRainingAt(pos.above())) {
            if (moisture > 0 && rand > 0.75) {
                level.setBlock(pos, state.setValue(FarmBlock.MOISTURE, moisture - 1), 2);
            }
        } else {
            level.setBlock(pos, state.setValue(FarmBlock.MOISTURE, 7), 2);
        }
    }

    private static boolean isWatered(LevelReader levelReader, BlockPos pos) {
        boolean foundWater = false;

        BlockState state = levelReader.getBlockState(pos);
        for (Direction dir : Util.CARDINAL_DIRECTIONS) {
            BlockPos blockPos = pos.offset(dir.getStepX(), 0, dir.getStepZ());
            if (state.canBeHydrated(levelReader, pos, levelReader.getFluidState(blockPos), blockPos)) {
                foundWater = true;
            }
        }

        return foundWater || FarmlandWaterManager.hasBlockWaterTicket(levelReader, pos);
    }
}
