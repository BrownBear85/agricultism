package com.brownbear85.agricultism.client;

import com.brownbear85.agricultism.Agricultism;
import com.brownbear85.agricultism.client.tooltip.ClientQualityTooltip;
import com.brownbear85.agricultism.client.tooltip.QualityTooltip;
import com.brownbear85.agricultism.common.item.custom.QualityItem;
import com.mojang.datafixers.util.Either;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientEvents {

    @Mod.EventBusSubscriber(modid = Agricultism.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void registerClientTooltipComponentFactories(RegisterClientTooltipComponentFactoriesEvent event) {
            event.register(QualityTooltip.class, (ClientQualityTooltip::new));
        }
    }

    @Mod.EventBusSubscriber(modid = Agricultism.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
        }

        @SubscribeEvent
        public static void renderTooltip(RenderTooltipEvent.GatherComponents event) {
            if (QualityItem.getQuality(event.getItemStack()) != 0) {
                event.getTooltipElements().add(Either.right(new QualityTooltip(QualityItem.getQuality(event.getItemStack()))));
            }
        }
    }
}
