package com.kaboomroads.lostfeatures.entity.client.moobloom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class MoobloomFlowerLayer<T extends Cow> extends RenderLayer<T, CowModel<T>> {
    private final BlockRenderDispatcher blockRenderer;

    public MoobloomFlowerLayer(RenderLayerParent<T, CowModel<T>> $$0, BlockRenderDispatcher $$1) {
        super($$0);
        this.blockRenderer = $$1;
    }

    public void render(@NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int $$2, T entity, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9) {
        if (!entity.isBaby()) {
            Minecraft $$10 = Minecraft.getInstance();
            boolean $$11 = $$10.shouldEntityAppearGlowing(entity) && entity.isInvisible();
            if (!entity.isInvisible() || $$11) {
                BlockState $$12 = Blocks.DANDELION.defaultBlockState();
                int $$13 = LivingEntityRenderer.getOverlayCoords(entity, 0.0F);
                BakedModel $$14 = blockRenderer.getBlockModel($$12);
                poseStack.pushPose();
                poseStack.translate(0.2F, -0.35F, 0.5F);
                thingize(poseStack, bufferSource, $$2, $$11, $$12, $$13, $$14);
                poseStack.translate(0.2F, -0.35F, 0.5F);
                poseStack.mulPose(Axis.YP.rotationDegrees(42.0F));
                poseStack.translate(0.1F, 0.0F, -0.6F);
                thingize(poseStack, bufferSource, $$2, $$11, $$12, $$13, $$14);
                getParentModel().getHead().translateAndRotate(poseStack);
                poseStack.translate(0.0F, -0.7F, -0.2F);
                poseStack.mulPose(Axis.YP.rotationDegrees(-78.0F));
                poseStack.scale(-1.0F, -1.0F, 1.0F);
                poseStack.translate(-0.5F, -0.5F, -0.5F);
                renderFlowerBlock(poseStack, bufferSource, $$2, $$11, $$12, $$13, $$14);
                poseStack.popPose();
            }
        }
    }

    private void thingize(@NotNull PoseStack $$0, @NotNull MultiBufferSource $$1, int $$2, boolean $$11, BlockState $$12, int $$13, BakedModel $$14) {
        $$0.mulPose(Axis.YP.rotationDegrees(-48.0F));
        $$0.scale(-1.0F, -1.0F, 1.0F);
        $$0.translate(-0.5F, -0.5F, -0.5F);
        renderFlowerBlock($$0, $$1, $$2, $$11, $$12, $$13, $$14);
        $$0.popPose();
        $$0.pushPose();
    }

    private void renderFlowerBlock(PoseStack $$0, MultiBufferSource $$1, int $$2, boolean $$3, BlockState $$4, int $$5, BakedModel $$6) {
        if ($$3)
            blockRenderer.getModelRenderer().renderModel($$0.last(), $$1.getBuffer(RenderType.outline(TextureAtlas.LOCATION_BLOCKS)), $$4, $$6, 0.0F, 0.0F, 0.0F, $$2, $$5);
        else blockRenderer.renderSingleBlock($$4, $$0, $$1, $$2, $$5);
    }
}