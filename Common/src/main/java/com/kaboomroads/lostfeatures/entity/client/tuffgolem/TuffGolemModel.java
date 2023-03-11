package com.kaboomroads.lostfeatures.entity.client.tuffgolem;

import com.kaboomroads.lostfeatures.Constants;
import com.kaboomroads.lostfeatures.entity.custom.TuffGolem;
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

public class TuffGolemModel<T extends TuffGolem> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Constants.MOD_ID, "tuff_golem"), "main");
    private final ModelPart golem;
    private final ModelPart rightArm;
    private final ModelPart leftArm;

    public TuffGolemModel(ModelPart root) {
        golem = root.getChild("golem");
        leftArm = golem.getChild("body").getChild("arms").getChild("arm1");
        rightArm = golem.getChild("body").getChild("arms").getChild("arm2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition golem = partdefinition.addOrReplaceChild("golem", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition body = golem.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -6.0F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, 0.0F));
        PartDefinition legs = body.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 9.0F, 0.0F));
        legs.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(28, 6).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, -3.0F, 0.0F));
        legs.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(28, 0).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, -3.0F, 0.0F));
        PartDefinition arms = body.addOrReplaceChild("arms", CubeListBuilder.create(), PartPose.offset(0.0F, -0.5F, 0.0F));
        arms.addOrReplaceChild("arm1", CubeListBuilder.create().texOffs(0, 18).addBox(0.0F, -1.5F, -1.5F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, 0.0F, 0.0F));
        arms.addOrReplaceChild("arm2", CubeListBuilder.create().texOffs(12, 18).addBox(-3.0F, -1.5F, -1.5F, 3.0F, 10.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, 0.0F, 0.0F));
        body.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(28, 12).addBox(-1.0F, -0.5F, -2.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.5F, -3.0F));
        PartDefinition cloth = body.addOrReplaceChild("cloth", CubeListBuilder.create().texOffs(46, 10).addBox(-4.0F, -1.0F, 6.0F, 8.0F, 8.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -3.0F));
        PartDefinition moving = cloth.addOrReplaceChild("moving", CubeListBuilder.create().texOffs(46, 0).addBox(-4.0F, 0.0F, -1.0F, 8.0F, 6.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));
        moving.addOrReplaceChild("part", CubeListBuilder.create().texOffs(46, 7).addBox(-3.999F, 0.001F, -0.01F, 8.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, -1.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        root().getAllParts().forEach(ModelPart::resetPose);
        float f = (float) entity.getDeltaMovement().horizontalDistanceSqr();
        float f1 = Mth.clamp(f * 10000.0F, 0.5F, 1.5F);
        animate(entity.walkAnimationState, TuffGolemAnimation.WALK, ageInTicks, f1);
        animate(entity.displayAnimationState, TuffGolemAnimation.DISPLAY, ageInTicks, 1.0f);
        animate(entity.stopDisplayAnimationState, TuffGolemAnimation.STOP_DISPLAY, ageInTicks, 1.0f);
        animate(entity.shakeAnimationState, TuffGolemAnimation.SHAKE, ageInTicks, 1.0f);
        if (!entity.displayAnimationState.isStarted()) {
            rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;
            leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F;
            rightArm.zRot = 0.0F;
            leftArm.zRot = 0.0F;
        }
    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        golem.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @NotNull
    @Override
    public ModelPart root() {
        return golem;
    }
}