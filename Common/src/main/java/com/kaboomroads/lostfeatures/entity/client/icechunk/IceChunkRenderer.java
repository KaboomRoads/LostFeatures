package com.kaboomroads.lostfeatures.entity.client.icechunk;

import com.kaboomroads.lostfeatures.Constants;
import com.kaboomroads.lostfeatures.entity.custom.IceChunk;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class IceChunkRenderer<T extends IceChunk> extends EntityRenderer<T> {
    private static final ResourceLocation ICE_CHUNK_LOCATION = new ResourceLocation(Constants.MOD_ID, "textures/entity/ice_chunk.png");
    private final IceChunkModel<IceChunk> model;

    public IceChunkRenderer(EntityRendererProvider.Context context) {
        super(context);
        shadowRadius = 2;
        model = new IceChunkModel<>(context.bakeLayer(IceChunkModel.LAYER_LOCATION));
    }

    @Override
    public void render(@NotNull T entity, float p_115247_, float p_115248_, PoseStack poseStack, MultiBufferSource source, int p_115251_) {
        poseStack.pushPose();
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        model.setupAnim(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        VertexConsumer vertexconsumer = source.getBuffer(this.model.renderType(getTextureLocation(entity)));
        model.renderToBuffer(poseStack, vertexconsumer, p_115251_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(entity, p_115247_, p_115248_, poseStack, source, p_115251_);
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull T entity) {
        return ICE_CHUNK_LOCATION;
    }
}