package com.kaboomroads.lostfeatures.entity.client.coppergolem;

import com.kaboomroads.lostfeatures.Constants;
import com.kaboomroads.lostfeatures.entity.custom.CopperGolem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CopperGolemRenderer<T extends CopperGolem> extends MobRenderer<T, CopperGolemModel<T>> {
    private static final ResourceLocation GOLEM_LOCATION = new ResourceLocation(Constants.MOD_ID, "textures/entity/copper_golem/copper_golem.png");
    private static final ResourceLocation EXPOSED_GOLEM_LOCATION = new ResourceLocation(Constants.MOD_ID, "textures/entity/copper_golem/copper_golem_exposed.png");
    private static final ResourceLocation WEATHERED_GOLEM_LOCATION = new ResourceLocation(Constants.MOD_ID, "textures/entity/copper_golem/copper_golem_weathered.png");
    private static final ResourceLocation OXIDIZED_GOLEM_LOCATION = new ResourceLocation(Constants.MOD_ID, "textures/entity/copper_golem/copper_golem_oxidized.png");

    public CopperGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new CopperGolemModel<>(context.bakeLayer(CopperGolemModel.LAYER_LOCATION)), 0.5F);
    }

    @Override
    protected void scale(@NotNull T entity, @NotNull PoseStack poseStack, float $$2) {
        poseStack.scale(0.85f, 0.85f, 0.85f);
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull T entity) {
        return switch (entity.getStage()) {
            case 1 -> EXPOSED_GOLEM_LOCATION;
            case 2 -> WEATHERED_GOLEM_LOCATION;
            case 3 -> OXIDIZED_GOLEM_LOCATION;
            default -> GOLEM_LOCATION;
        };
    }
}