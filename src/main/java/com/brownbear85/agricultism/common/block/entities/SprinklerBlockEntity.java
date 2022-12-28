package com.brownbear85.agricultism.common.block.entities;

import com.brownbear85.agricultism.Util;
import com.brownbear85.agricultism.common.block.custom.SprinklerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.common.FarmlandWaterManager;
import net.minecraftforge.common.ticket.AABBTicket;
import org.jetbrains.annotations.NotNull;

public class SprinklerBlockEntity extends BlockEntity {
    private final int range;

    private AABBTicket waterTicket;

    public SprinklerBlockEntity(BlockPos pPos, BlockState pState) {
        super(BlockEntityRegistry.SPRINKLER_BLOCK_ENTITY.get(), pPos, pState);
        if (pState.getBlock() instanceof SprinklerBlock sprinklerBlock) {
            this.range = sprinklerBlock.getRange();
        } else {
            this.range = 0;
        }
    }

    private void removeTicket() {
        if (waterTicket != null) {
            waterTicket.invalidate();
        }
    }

    private void addTicket() {
        removeTicket();
        if (level != null && level instanceof ServerLevel) {
            waterTicket = FarmlandWaterManager.addAABBTicket(level, new AABB(worldPosition.offset(-range, -1, -range), worldPosition.offset(1 + range, 0, 1 + range)));
        }
    }

    @Override
    public void setLevel(Level pLevel) {
        super.setLevel(pLevel);
        addTicket();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        removeTicket();
    }
}
