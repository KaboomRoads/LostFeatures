package com.kaboomroads.lostfeatures.entity.client.wildfire;

import com.kaboomroads.lostfeatures.Constants;
import com.kaboomroads.lostfeatures.entity.custom.Wildfire;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class WildfireModel<T extends Wildfire> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Constants.MOD_ID, "wildfire"), "main");
    private final ModelPart root;
    private final ModelPart shield1;
    private final ModelPart shield2;
    private final ModelPart shield3;
    private final ModelPart shield4;

    public WildfireModel(ModelPart root) {
        this.root = root.getChild("wildfire");
        ModelPart shields = this.root.getChild("body").getChild("shields");
        shield1 = shields.getChild("shield1");
        shield2 = shields.getChild("shield2");
        shield3 = shields.getChild("shield3");
        shield4 = shields.getChild("shield4");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition wildfire = partdefinition.addOrReplaceChild("wildfire", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition body = wildfire.addOrReplaceChild("body", CubeListBuilder.create().texOffs(24, 52).addBox(-2.0F, -22.0F, -2.0F, 4.0F, 21.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(30, 36).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).texOffs(62, 35).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 9.0F, 8.0F, new CubeDeformation(0.0F).extend(0.5F)), PartPose.offset(0.0F, -22.0F, 0.0F));
        PartDefinition shields = body.addOrReplaceChild("shields", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition shield1 = shields.addOrReplaceChild("shield1", CubeListBuilder.create(), PartPose.offset(0.0F, -20.0F, -10.0F));
        shield1.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(38, 16).addBox(-5.0F, -1.0F, -1.0F, 10.0F, 17.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));
        PartDefinition shield2 = shields.addOrReplaceChild("shield2", CubeListBuilder.create(), PartPose.offset(-10.0F, -20.0F, 0.0F));
        shield2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -1.0F, -5.0F, 2.0F, 17.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2618F));
        PartDefinition shield3 = shields.addOrReplaceChild("shield3", CubeListBuilder.create(), PartPose.offset(0.0F, -20.0F, 10.0F));
        shield3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 44).addBox(-5.0F, -1.0F, -1.0F, 10.0F, 17.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.2618F, 0.0F, 0.0F));
        PartDefinition shield4 = shields.addOrReplaceChild("shield4", CubeListBuilder.create(), PartPose.offset(10.0F, -20.0F, 0.0F));
        shield4.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(14, 17).addBox(-1.0F, -1.0F, -5.0F, 2.0F, 17.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2618F));
        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        root().getAllParts().forEach(ModelPart::resetPose);
        animate(entity.idleAnimationState, WildfireAnimation.IDLE, ageInTicks, 1.0f);
        animate(entity.hurtAnimationState, WildfireAnimation.HURT, ageInTicks, 1.0f);
        int shields = entity.getShields();
        shield1.visible = shields >= 4;
        shield2.visible = shields >= 3;
        shield3.visible = shields >= 2;
        shield4.visible = shields >= 1;
    }

    @NotNull
    @Override
    public ModelPart root() {
        return root;
    }
}