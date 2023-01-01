package com.brownbear85.agricultism.common.mixin;

import com.brownbear85.agricultism.Util;
import com.brownbear85.agricultism.common.item.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CropBlock.class)
public class CropBlockMixin {

    @Inject(at = @At(value = "HEAD"), method = "getCloneItemStack(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/item/ItemStack;", cancellable = true)
    public void getCloneItemStack(BlockGetter getter, BlockPos pos, BlockState state, CallbackInfoReturnable<ItemStack> cir) {
        if (state.is(Blocks.POTATOES)) {
            cir.cancel();
            cir.setReturnValue(Util.item(ItemRegistry.POTATO_SEEDS.get()));
        }
        if (state.is(Blocks.CARROTS)) {
            cir.cancel();
            cir.setReturnValue(Util.item(ItemRegistry.CARROT_SEEDS.get()));
        }
    }
}
