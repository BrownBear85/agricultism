package com.brownbear85.agricultism.client.tooltip;

import net.minecraft.world.inventory.tooltip.TooltipComponent;

public class QualityTooltip implements TooltipComponent {
    private final int quality;

    public QualityTooltip(int quality) {
        this.quality = quality;
    }

    public int getQuality() {
        return quality;
    }
}
