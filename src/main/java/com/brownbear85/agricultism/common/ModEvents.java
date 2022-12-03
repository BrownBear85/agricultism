package com.brownbear85.agricultism.common;

import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.brownbear85.agricultism.Agricultism.MODID;

public class ModEvents {
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModBusEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }

    @Mod.EventBusSubscriber(modid = MODID)
    public static class ForgeBusEvents {

        @SubscribeEvent
        public static void farmlandTrampled(BlockEvent.FarmlandTrampleEvent event) {
            if (event.getFallDistance() < 2.5 ||
                    (event.getFallDistance() < 5 && !event.getLevel().getBlockState(event.getPos().above()).isAir())) {
                event.setCanceled(true);
            }
        }
    }
}
