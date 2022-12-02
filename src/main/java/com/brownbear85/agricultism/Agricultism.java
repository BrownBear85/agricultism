package com.brownbear85.agricultism;

import com.brownbear85.agricultism.common.block.BlockRegistry;
import com.brownbear85.agricultism.common.item.ItemRegistry;
import com.mojang.logging.LogUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(Agricultism.MODID)
public class Agricultism {

    public static final String MODID = "agricultism";

    private static final Logger LOGGER = LogUtils.getLogger();

    public Agricultism() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);

        BlockRegistry.BLOCKS.register(bus);
        ItemRegistry.ITEMS.register(bus);

        bus.addListener(this::commonSetup);

    }

    public static final CreativeModeTab TAB = new CreativeModeTab("agricultism") {
        @Override
        public ItemStack makeIcon() {
            return Items.STONE_HOE.getDefaultInstance();
        }

        @Override
        public void fillItemList(NonNullList<ItemStack> itemList) {
            for (RegistryObject<Item> item : ItemRegistry.ITEMS.getEntries()) {
                itemList.add(item.get().getDefaultInstance());
            }
        }
    };

    private void commonSetup(final FMLCommonSetupEvent event) {
    }
}
