package com.kaboomroads.lostfeatures.entity.client.tuffgolem;

import com.kaboomroads.lostfeatures.Constants;
import com.kaboomroads.lostfeatures.entity.custom.TuffGolem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class TuffGolemRenderer<T extends TuffGolem> extends MobRenderer<T, TuffGolemModel<T>> {
    private static final ResourceLocation DEFAULT_GOLEM_LOCATION = new ResourceLocation(Constants.MOD_ID, "textures/entity/tuff_golem/tuff_golem_red.png");
    private final ItemRenderer itemRenderer;

    public TuffGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new TuffGolemModel<>(context.bakeLayer(TuffGolemModel.LAYER_LOCATION)), 0.5F);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(@NotNull T entity, float $$1, float $$2, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, $$1, $$2, poseStack, bufferSource, packedLight);
        ItemStack item = entity.getItemBySlot(EquipmentSlot.MAINHAND);
        if (!item.isEmpty()) {
            poseStack.pushPose();
            Vec3 vec3 = getRenderOffset(entity, $$2);
            poseStack.translate(-vec3.x(), -vec3.y() + 0.59375, -vec3.z());
            float yaw = (float) Math.toRadians($$1);
            float x = (float) -Math.sin(yaw);
            float z = (float) Math.cos(yaw);
            poseStack.translate(x * 0.4375F, 0, z * 0.4375F);
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - entity.getYRot()));
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            poseStack.scale(0.5F, 0.5F, 0.5F);
            itemRenderer.renderStatic(entity, item, ItemDisplayContext.FIXED, false, poseStack, bufferSource, entity.getLevel(), packedLight, OverlayTexture.NO_OVERLAY, entity.getId());
            poseStack.popPose();
        }
    }

    @Nullable
    protected RenderType getClothRenderType(ResourceLocation $$4, boolean $$1, boolean $$2, boolean $$3) {
        if ($$2) return RenderType.itemEntityTranslucentCull($$4);
        else if ($$1) return this.model.renderType($$4);
        else return $$3 ? RenderType.outline($$4) : null;
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull T entity) {
        return entity.getClothColor().getTextureLocation();
    }
}