package com.brownbear85.agricultism.common.item;

import com.brownbear85.agricultism.Agricultism;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Agricultism.MODID);


    public static final RegistryObject<Item> POTATO_SEEDS = ITEMS.register("potato_seeds",
            () -> new ItemNameBlockItem(Blocks.POTATOES, properties()));

    public static final RegistryObject<Item> CARROT_SEEDS = ITEMS.register("carrot_seeds",
            () -> new ItemNameBlockItem(Blocks.CARROTS, properties()));

    private static Item.Properties properties() {return new Item.Properties().tab(Agricultism.TAB);}
}
