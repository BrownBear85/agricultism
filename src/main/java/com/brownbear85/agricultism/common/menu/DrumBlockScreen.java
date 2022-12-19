package com.brownbear85.agricultism.common.menu;

import com.brownbear85.agricultism.Agricultism;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.Optional;

public class DrumBlockScreen extends AbstractContainerScreen<DrumBlockMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Agricultism.MODID, "textures/gui/drum_block_gui.png");

    private final ArrayList<Component> oilTooltip = new ArrayList<>(2);

    public DrumBlockScreen(DrumBlockMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);

        oilTooltip.add(0, Component.translatable("container.oil_drum.vegetable_oil"));
        oilTooltip.add(1, Component.empty());
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);
        renderProgressArrow(pPoseStack, x, y);
        renderOilBar(pPoseStack, x, y);
    }

    private void renderProgressArrow(PoseStack pPoseStack, int x, int y) {
        if (menu.isInProgress()) {
            blit(pPoseStack, x + 85, y + 29, 176, 0, menu.getScaledProgress(), 23);
        }
    }

    private void renderOilBar(PoseStack pPoseStack, int x, int y) {
        if (menu.hasOil()) {
            int oil = menu.getOilBarHeight();
            blit(pPoseStack, x + 136, y + 16 + 52 - oil, 177, 24 + 52 - oil, 14, oil);
        }
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    @Override
    protected void renderTooltip(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        super.renderTooltip(pPoseStack, pMouseX, pMouseY);

        if (mouseOnOilBar(pMouseX, pMouseY)) {
            oilTooltip.set(1, Component.literal(menu.data.get(1) + " mL").withStyle(ChatFormatting.DARK_GRAY));
            this.renderTooltip(pPoseStack, oilTooltip, Optional.empty(), pMouseX, pMouseY);
        }
    }

    private boolean mouseOnOilBar(int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        return pMouseX > x + 135 && pMouseX < x + 148 && pMouseY > y + 15 && pMouseY < y + 68;
    }
}
