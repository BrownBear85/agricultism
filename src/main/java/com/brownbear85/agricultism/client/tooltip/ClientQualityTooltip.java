package com.brownbear85.agricultism.client.tooltip;

import com.brownbear85.agricultism.Agricultism;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;

public class ClientQualityTooltip implements ClientTooltipComponent {

    private static final ResourceLocation STAR_TEXTURE = new ResourceLocation(Agricultism.MODID, "textures/gui/quality_star.png");

    private final int quality;

    public ClientQualityTooltip(QualityTooltip tooltip) {
        this.quality = tooltip.getQuality();
    }

    public int getQuality() {
        return quality;
    }

    @Override
    public void renderImage(Font font, int x, int y, PoseStack stack, ItemRenderer itemRenderer, int blitOffset) {
        for (int i = 0; i < quality; i++) {
            blit(stack, x + i * 8 - 1, y - 1, blitOffset);
        }
    }

    @Override
    public int getHeight() {
        return 8;
    }

    @Override
    public int getWidth(Font font) {
        return 8 * quality;
    }

    private void blit(PoseStack stack, int x, int y, int offset) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, STAR_TEXTURE);
        GuiComponent.blit(stack, x, y, offset, 0, 0, 8, 8, 8, 8);
    }
}
