package com.brownbear85.agricultism;

import com.brownbear85.agricultism.common.block.BlockRegistry;
import com.brownbear85.agricultism.common.block.custom.VegetableOilCauldronBlock;
import com.brownbear85.agricultism.common.block.entities.BlockEntityRegistry;
import com.brownbear85.agricultism.common.enchantment.EnchantmentRegistry;
import com.brownbear85.agricultism.common.events.ToolEvents;
import com.brownbear85.agricultism.common.item.ItemRegistry;
import com.brownbear85.agricultism.common.item.custom.SeedPouchItem;
import com.brownbear85.agricultism.common.menu.DrumBlockScreen;
import com.brownbear85.agricultism.common.menu.MenuRegistry;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

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

    private void commonSetup(final FMLCommonSetupEvent event) {
        MenuScreens.register(MenuRegistry.DRUM_BLOCK_MENU.get(), DrumBlockScreen::new);
        VegetableOilCauldronBlock.registerInteractions();
        ToolEvents.registerBark2Logs();
    }
}
