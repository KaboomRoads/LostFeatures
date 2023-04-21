package com.kaboomroads.lostfeatures.entity.client.ostrich;

import com.kaboomroads.lostfeatures.Constants;
import com.kaboomroads.lostfeatures.entity.custom.Ostrich;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class OstrichModel<T extends Ostrich> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Constants.MOD_ID, "ostrich"), "main");
    private final ModelPart ostrich;
    private final ModelPart neckHead;

    public OstrichModel(ModelPart root) {
        ostrich = root.getChild("ostrich");
        neckHead = ostrich.getChild("body").getChild("neck");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition ostrich = partdefinition.addOrReplaceChild("ostrich", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition body = ostrich.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -4.8333F, -5.8333F, 10.0F, 9.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(18, 21).addBox(-4.0F, -3.8333F, 6.1667F, 8.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(18, 31).addBox(-3.5F, -3.3333F, -8.8333F, 7.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -14.1667F, -0.1667F));
        body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(38, 33).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 7.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.8333F, 8.1667F));
        PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create(), PartPose.offset(0.0F, 0.1667F, -8.8333F));
        PartDefinition part3 = neck.addOrReplaceChild("part3", CubeListBuilder.create().texOffs(0, 21).addBox(-2.5F, -14.5F, -1.999F, 5.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -4.0F));
        part3.addOrReplaceChild("beak", CubeListBuilder.create().texOffs(35, 27).addBox(-2.5F, -1.0F, -4.0F, 5.0F, 2.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.5F, -2.0F));
        neck.addOrReplaceChild("part4", CubeListBuilder.create().texOffs(32, 0).addBox(-2.5F, -2.5F, -2.0F, 5.0F, 5.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition legs = ostrich.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, -10.0F, 0.0F));
        PartDefinition leg1 = legs.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(16, 41).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 0.0F, 0.0F));
        leg1.addOrReplaceChild("part", CubeListBuilder.create().texOffs(32, 41).addBox(-1.01F, 0.0F, -2.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 1.0F));
        PartDefinition leg2 = legs.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, 0.0F, 0.0F));
        leg2.addOrReplaceChild("part2", CubeListBuilder.create().texOffs(24, 41).addBox(-0.99F, 0.0F, -2.0F, 2.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 5.0F, 1.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        root().getAllParts().forEach(ModelPart::resetPose);
        float f = (float) entity.getDeltaMovement().horizontalDistanceSqr();
        float f1 = Mth.clamp(f * (young ? 1600.0F : 1100.0F), 0.5F, 1.5F);
        applyHeadRotation(netHeadYaw, headPitch);
        animate(entity.walkAnimationState, OstrichAnimation.WALK, ageInTicks, f1);
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (young) {
            float x = 1.1F;
            float y = 1.5F;
            poseStack.pushPose();
            poseStack.scale(1 / (x * y), 1 / (x * x * y), 1 / (x * y));
            poseStack.translate(0.0F, x + 0.125, 0.0F);
            root().render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
            poseStack.popPose();
        } else root().render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    private void applyHeadRotation(float p_233517_, float p_233518_) {
        neckHead.xRot = p_233518_ * ((float) Math.PI / 180F);
        neckHead.yRot = p_233517_ * ((float) Math.PI / 180F);
    }

    @NotNull
    @Override
    public ModelPart root() {
        return ostrich;
    }
}