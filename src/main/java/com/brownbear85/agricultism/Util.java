package com.brownbear85.agricultism;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class Util {
    public static final Direction[] CARDINAL_DIRECTIONS = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};

    /** returns a Vec3 at the center of the given BlockPos */
    public static Vec3 blockCenterVec(BlockPos pos) {
        return new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }

    public static ItemStack item(Item item) {
        return item(item, 1);
    }

    public static ItemStack item(Item item, int count) {
        ItemStack stack = new ItemStack(item, count);
        stack.getOrCreateTag();
        return stack;
    }

    // from BaseEntityBlock
    @SuppressWarnings("unchecked")
    @Nullable
    public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> pServerType, BlockEntityType<E> pClientType, BlockEntityTicker<? super E> pTicker) {
        return pClientType == pServerType ? (BlockEntityTicker<A>)pTicker : null;
    }

    public static int rgbaToDecimal(int r, int g, int b, int a) {
        return a * 16777216 + r * 65536 + g * 256 + b;
    }

    public static Vec2 pointOnCircle(float radius, float angleDegrees, float originX, float originY) {
        return new Vec2(originX + radius * (float) Math.cos(Math.toRadians(Mth.wrapDegrees(angleDegrees))), originY + radius * (float) Math.sin(Math.toRadians(Mth.wrapDegrees(angleDegrees))));
    }
}
