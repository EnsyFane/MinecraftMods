package net.ensyfane.choppingblock.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.ensyfane.choppingblock.block.custom.ChoppingBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

public class ChoppingBlockEntityRenderer implements BlockEntityRenderer<ChoppingBlockEntity> {
    public ChoppingBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(ChoppingBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        var blockItem = pBlockEntity.getItem();
        if (blockItem == null || blockItem == Items.AIR)
        {
            return;
        }

        var scale = 0.6f;
        var offset = (1 - scale) / 2;
        pPoseStack.pushPose();
        pPoseStack.translate(offset, ChoppingBlock.SHAPE.bounds().maxY, offset);
        pPoseStack.scale(scale, scale, scale);
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(Block.byItem(blockItem).defaultBlockState(), pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
        pPoseStack.popPose();
    }
}
