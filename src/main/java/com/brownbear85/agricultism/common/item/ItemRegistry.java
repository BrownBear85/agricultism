package com.brownbear85.agricultism.common.item;

import com.brownbear85.agricultism.Agricultism;
import com.brownbear85.agricultism.common.item.custom.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Agricultism.MODID);


    public static final RegistryObject<Item> POTATO_SEEDS = ITEMS.register("potato_seeds", () -> new SeedItem(Blocks.POTATOES, properties()));
    public static final RegistryObject<Item> CARROT_SEEDS = ITEMS.register("carrot_seeds", () -> new SeedItem(Blocks.CARROTS, properties()));

    public static final RegistryObject<Item> OAK_BARK = ITEMS.register("oak_bark", () -> new Item(properties()));
    public static final RegistryObject<Item> SPRUCE_BARK = ITEMS.register("spruce_bark", () -> new Item(properties()));
    public static final RegistryObject<Item> BIRCH_BARK = ITEMS.register("birch_bark", () -> new Item(properties()));
    public static final RegistryObject<Item> JUNGLE_BARK = ITEMS.register("jungle_bark", () -> new Item(properties()));
    public static final RegistryObject<Item> ACACIA_BARK = ITEMS.register("acacia_bark", () -> new Item(properties()));
    public static final RegistryObject<Item> DARK_OAK_BARK = ITEMS.register("dark_oak_bark", () -> new Item(properties()));
    public static final RegistryObject<Item> MANGROVE_BARK = ITEMS.register("mangrove_bark", () -> new Item(properties()));
    public static final RegistryObject<Item> CRIMSON_BARK = ITEMS.register("crimson_bark", () -> new Item(properties()));
    public static final RegistryObject<Item> WARPED_BARK = ITEMS.register("warped_bark", () -> new Item(properties()));

    public static final RegistryObject<Item> CHAR = ITEMS.register("char", () -> new Item(properties()));

    public static final RegistryObject<Item> WOODEN_WATERING_CAN = ITEMS.register("wooden_watering_can", () -> new WateringCanItem(properties().stacksTo(1), 1000, 0));
    public static final RegistryObject<Item> STONE_WATERING_CAN = ITEMS.register("stone_watering_can", () -> new WateringCanItem(properties().stacksTo(1), 4000, 0));
    public static final RegistryObject<Item> FLINT_WATERING_CAN = ITEMS.register("flint_watering_can", () -> new WateringCanItem(properties().stacksTo(1).rarity(Rarity.UNCOMMON), 4500, 1));
    public static final RegistryObject<Item> IRON_WATERING_CAN = ITEMS.register("iron_watering_can", () -> new WateringCanItem(properties().stacksTo(1), 16000, 0));
    public static final RegistryObject<Item> GOLD_WATERING_CAN = ITEMS.register("golden_watering_can", () -> new WateringCanItem(properties().stacksTo(1).rarity(Rarity.UNCOMMON), 18000, 1));
    public static final RegistryObject<Item> DIAMOND_WATERING_CAN = ITEMS.register("diamond_watering_can", () -> new WateringCanItem(properties().stacksTo(1), 65250, 1));
    public static final RegistryObject<Item> OBSIDIAN_WATERING_CAN = ITEMS.register("obsidian_watering_can", () -> new WateringCanItem(properties().stacksTo(1).rarity(Rarity.UNCOMMON), 68750, 2));
    public static final RegistryObject<Item> NETHERITE_WATERING_CAN = ITEMS.register("netherite_watering_can", () -> new WateringCanItem(properties().stacksTo(1).rarity(Rarity.RARE), 131250, 2));
    public static final RegistryObject<Item> CREATIVE_WATERING_CAN = ITEMS.register("creative_watering_can", () -> new CreativeWateringCanItem(properties().stacksTo(1).rarity(Rarity.EPIC), Integer.MAX_VALUE, 3));

    public static final RegistryObject<Item> SEED_POUCH = ITEMS.register("seed_pouch", () -> new SeedPouchItem(properties().stacksTo(1), 1, 512));
    public static final RegistryObject<Item> ADVANCED_SEED_POUCH = ITEMS.register("advanced_seed_pouch", () -> new SeedPouchItem(properties().stacksTo(1).rarity(Rarity.RARE), 6, 512));

    public static final RegistryObject<Item> FLINT_CUTTING_KNIFE = ITEMS.register("flint_cutting_knife", () -> new CuttingKnifeItem(properties().durability(16)));
    public static final RegistryObject<Item> IRON_CUTTING_KNIFE = ITEMS.register("iron_cutting_knife", () -> new CuttingKnifeItem(properties().durability(64)));
    public static final RegistryObject<Item> DIAMOND_CUTTING_KNIFE = ITEMS.register("diamond_cutting_knife", () -> new CuttingKnifeItem(properties().durability(512)));
    public static final RegistryObject<Item> NETHERITE_CUTTING_KNIFE = ITEMS.register("netherite_cutting_knife", () -> new CuttingKnifeItem(properties().durability(1024)));

    public static final RegistryObject<Item> COW_PELT = ITEMS.register("cow_pelt", () -> new PeltItem(properties().stacksTo(1)));
    public static final RegistryObject<Item> PIG_PELT = ITEMS.register("pig_pelt", () -> new PeltItem(properties().stacksTo(1)));
    public static final RegistryObject<Item> SHEEP_PELT = ITEMS.register("sheep_pelt", () -> new PeltItem(properties().stacksTo(1)));
    public static final RegistryObject<Item> HORSE_PELT = ITEMS.register("horse_pelt", () -> new PeltItem(properties().stacksTo(1)));
    public static final RegistryObject<Item> CUT_ANIMAL_HIDE = ITEMS.register("cut_animal_hide", () -> new Item(properties()));
    public static final RegistryObject<Item> PRESERVED_ANIMAL_HIDE = ITEMS.register("preserved_animal_hide", () -> new Item(properties()));


    public static Item.Properties properties() {
        return new Item.Properties().tab(Agricultism.TAB);
    }

    public static class Tags {

        public static final TagKey<Item> BARK = create("bark_burnable");
        public static final TagKey<Item> SEEDS = create("seeds");
        public static final TagKey<Item> CUTTING_KNIVES = create("cutting_knives");

        private static TagKey<Item> create(String location) {
            return ItemTags.create(new ResourceLocation(Agricultism.MODID, location));
        }
    }
}
