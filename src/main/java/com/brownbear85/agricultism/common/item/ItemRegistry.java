package com.brownbear85.agricultism.common.item;

import com.brownbear85.agricultism.Agricultism;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Agricultism.MODID);


    public static final RegistryObject<Item> POTATO_SEEDS = ITEMS.register("potato_seeds",
            () -> new ItemNameBlockItem(Blocks.POTATOES, new Item.Properties()));

    public static final RegistryObject<Item> CARROT_SEEDS = ITEMS.register("carrot_seeds",
            () -> new ItemNameBlockItem(Blocks.CARROTS, new Item.Properties()));

    public static final RegistryObject<Item> WOODEN_WATERING_CAN = ITEMS.register("wooden_watering_can",
            () -> new WateringCanItem(properties().stacksTo(1), 2000, 0));




    public static Item.Properties properties() {
        return new Item.Properties().tab(Agricultism.TAB);
    }
}
