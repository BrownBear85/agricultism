package com.brownbear85.agricultism.common.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;

public class SeedItem extends Item {
    private final Block block;

    public SeedItem(Block block, Item.Properties properties) {
        super(properties);
        this.block = block;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        return block.asItem().useOn(context);
    }
}
