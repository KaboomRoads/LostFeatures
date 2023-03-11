package com.kaboomroads.lostfeatures.entity.client.coppergolem;

import com.kaboomroads.lostfeatures.Constants;
import com.kaboomroads.lostfeatures.entity.custom.CopperGolem;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class CopperGolemModel<T extends CopperGolem> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Constants.MOD_ID, "copper_golem"), "main");
    private final ModelPart root;
    private final ModelPart head;

    public CopperGolemModel(ModelPart root) {
        this.root = root.getChild("golem");
        this.head = this.root.getChild("body").getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition golem = partdefinition.addOrReplaceChild("golem", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition body = golem.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 14).addBox(-4.0F, -7.0F, -2.0F, 8.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));
        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -7.0F, -4.0F, 10.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 0.0F));
        head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(36, 18).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, -4.0F));
        head.addOrReplaceChild("rod", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).texOffs(28, 0).addBox(-2.0F, -8.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 0.0F));
        PartDefinition arms = body.addOrReplaceChild("arms", CubeListBuilder.create(), PartPose.offset(0.0F, 4.0F, 0.0F));
        arms.addOrReplaceChild("arm1", CubeListBuilder.create().texOffs(0, 25).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(6.0F, -10.0F, 0.0F));
        arms.addOrReplaceChild("arm2", CubeListBuilder.create().texOffs(20, 21).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -10.0F, 0.0F));
        PartDefinition legs = body.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(0.0F, 4.0F, 0.0F));
        legs.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(32, 33).addBox(-3.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -4.0F, 0.0F));
        legs.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(32, 10).addBox(-1.0F, 0.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -4.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        root().getAllParts().forEach(ModelPart::resetPose);
        float f = (float) entity.getDeltaMovement().horizontalDistanceSqr();
        float f1 = Mth.clamp(f * 10000.0F, 0.5F, 1.5F);
        applyHeadRotation(netHeadYaw, headPitch);
        animate(entity.walkAnimationState, CopperGolemAnimation.WALK, ageInTicks, f1);
        animate(entity.buttonAnimationState, CopperGolemAnimation.BUTTON, ageInTicks, 1.0f);
    }

    private void applyHeadRotation(float p_233517_, float p_233518_) {
        this.head.xRot = p_233518_ * ((float) Math.PI / 180F);
        this.head.yRot = p_233517_ * ((float) Math.PI / 180F);
    }

    @NotNull
    @Override
    public ModelPart root() {
        return root;
    }
}