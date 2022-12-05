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
            () -> new WateringCanItem(properties().stacksTo(1), 1000, 0));
    public static final RegistryObject<Item> STONE_WATERING_CAN = ITEMS.register("stone_watering_can",
            () -> new WateringCanItem(properties().stacksTo(1), 4000, 0));
    public static final RegistryObject<Item> FLINT_WATERING_CAN = ITEMS.register("flint_watering_can",
            () -> new WateringCanItem(properties().stacksTo(1), 4500, 1));
    public static final RegistryObject<Item> IRON_WATERING_CAN = ITEMS.register("iron_watering_can",
            () -> new WateringCanItem(properties().stacksTo(1), 16000, 0));
    public static final RegistryObject<Item> GOLD_WATERING_CAN = ITEMS.register("golden_watering_can",
            () -> new WateringCanItem(properties().stacksTo(1), 18000, 1));
    public static final RegistryObject<Item> DIAMOND_WATERING_CAN = ITEMS.register("diamond_watering_can",
            () -> new WateringCanItem(properties().stacksTo(1), 65250, 1));
    public static final RegistryObject<Item> OBSIDIAN_WATERING_CAN = ITEMS.register("obsidian_watering_can",
            () -> new WateringCanItem(properties().stacksTo(1), 68750, 2));
    public static final RegistryObject<Item> NETHERITE_WATERING_CAN = ITEMS.register("netherite_watering_can",
            () -> new WateringCanItem(properties().stacksTo(1), 131250, 2));
    public static final RegistryObject<Item> CREATIVE_WATERING_CAN = ITEMS.register("creative_watering_can",
            () -> new CreativeWateringCanItem(properties().stacksTo(1), Integer.MAX_VALUE, 3));




    public static Item.Properties properties() {
        return new Item.Properties().tab(Agricultism.TAB);
    }
}
