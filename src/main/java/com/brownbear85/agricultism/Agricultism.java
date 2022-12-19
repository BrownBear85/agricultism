package com.brownbear85.agricultism;

import com.brownbear85.agricultism.common.block.BlockEntityRegistry;
import com.brownbear85.agricultism.common.block.BlockRegistry;
import com.brownbear85.agricultism.common.enchantment.EnchantmentRegistry;
import com.brownbear85.agricultism.common.item.ItemRegistry;
import com.brownbear85.agricultism.common.item.custom.SeedPouchItem;
import com.brownbear85.agricultism.common.menu.DrumBlockScreen;
import com.brownbear85.agricultism.common.menu.MenuRegistry;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mod(Agricultism.MODID)
public class Agricultism {

    public static final String MODID = "agricultism";

    private static final Logger LOGGER = LogUtils.getLogger();

    public Agricultism() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);

        BlockRegistry.BLOCKS.register(bus);
        BlockEntityRegistry.BLOCK_ENTITY_TYPES.register(bus);
        ItemRegistry.ITEMS.register(bus);
        EnchantmentRegistry.ENCHANTMENTS.register(bus);
        MenuRegistry.MENUS.register(bus);

        bus.addListener(this::commonSetup);
    }

    public static final CreativeModeTab TAB = new CreativeModeTab("agricultism") {
        @Override
        public ItemStack makeIcon() {
            return ItemRegistry.SEED_POUCH.get().getDefaultInstance();
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> list) {
            for(Item item : ForgeRegistries.ITEMS) {
                if (item.getCreativeTabs().contains(TAB)) {
                    list.add(item.getDefaultInstance());

                    if (item.equals(ItemRegistry.ADVANCED_SEED_POUCH.get())) {
                        list.add(SeedPouchItem.filledAdvancedSeedPouch());
                    }
                }
            }
        }
    };

    public static final Map<Item, TagKey<Block>> BARK_2_LOGS_MAP = new Object2ObjectArrayMap<>();
    public static final ArrayList<Block> STRIPPED_LOGS = new ArrayList<>(List.of(Blocks.STRIPPED_OAK_LOG, Blocks.STRIPPED_OAK_WOOD, Blocks.STRIPPED_SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_WOOD, Blocks.STRIPPED_BIRCH_LOG, Blocks.STRIPPED_BIRCH_WOOD, Blocks.STRIPPED_JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_WOOD, Blocks.STRIPPED_ACACIA_LOG, Blocks.STRIPPED_ACACIA_WOOD, Blocks.STRIPPED_DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_WOOD, Blocks.STRIPPED_MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_WOOD, Blocks.STRIPPED_CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_HYPHAE, Blocks.STRIPPED_WARPED_STEM, Blocks.STRIPPED_WARPED_HYPHAE));

    private void commonSetup(final FMLCommonSetupEvent event) {
        MenuScreens.register(MenuRegistry.DRUM_BLOCK_MENU.get(), DrumBlockScreen::new);

        BARK_2_LOGS_MAP.put(ItemRegistry.OAK_BARK.get(), BlockTags.OAK_LOGS);
        BARK_2_LOGS_MAP.put(ItemRegistry.SPRUCE_BARK.get(), BlockTags.SPRUCE_LOGS);
        BARK_2_LOGS_MAP.put(ItemRegistry.BIRCH_BARK.get(), BlockTags.BIRCH_LOGS);
        BARK_2_LOGS_MAP.put(ItemRegistry.JUNGLE_BARK.get(), BlockTags.JUNGLE_LOGS);
        BARK_2_LOGS_MAP.put(ItemRegistry.ACACIA_BARK.get(), BlockTags.ACACIA_LOGS);
        BARK_2_LOGS_MAP.put(ItemRegistry.DARK_OAK_BARK.get(), BlockTags.DARK_OAK_LOGS);
        BARK_2_LOGS_MAP.put(ItemRegistry.MANGROVE_BARK.get(), BlockTags.MANGROVE_LOGS);
        BARK_2_LOGS_MAP.put(ItemRegistry.CRIMSON_BARK.get(), BlockTags.CRIMSON_STEMS);
        BARK_2_LOGS_MAP.put(ItemRegistry.WARPED_BARK.get(), BlockTags.WARPED_STEMS);
    }
}
