package com.brownbear85.agricultism.client.rendering;

import com.brownbear85.agricultism.Util;
import com.brownbear85.agricultism.common.block.entities.VegetableOilCauldronBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec2;

public class VegetableOilCauldronBlockEntityRenderer implements BlockEntityRenderer<VegetableOilCauldronBlockEntity> {

    public VegetableOilCauldronBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {}

    @Override
    public void render(VegetableOilCauldronBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        if (!pBlockEntity.shouldRender()) {
            return;
        }
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        pPoseStack.pushPose();
        Vec2 point = Util.pointOnCircle(0.24F, -90 - pBlockEntity.getRotation(pPartialTick), 0, 0);
        pPoseStack.translate(0.5F + point.x, pBlockEntity.getItemHeight(pPartialTick), 0.5F + point.y);
        pPoseStack.scale(0.5F, 0.5F, 0.5F);
        pPoseStack.mulPose(Vector3f.YP.rotationDegrees(pBlockEntity.getRotation(pPartialTick)));
        itemRenderer.renderStatic(pBlockEntity.getRenderStack(), ItemTransforms.TransformType.FIXED, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, pBufferSource, (int) pBlockEntity.getBlockPos().asLong());
        pPoseStack.popPose();
    }
}