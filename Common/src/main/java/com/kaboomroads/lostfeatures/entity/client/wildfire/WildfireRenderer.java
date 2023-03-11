package com.kaboomroads.lostfeatures.entity.client.wildfire;

import com.kaboomroads.lostfeatures.Constants;
import com.kaboomroads.lostfeatures.entity.custom.Wildfire;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class WildfireRenderer<T extends Wildfire> extends MobRenderer<T, WildfireModel<T>> {
    private static final ResourceLocation WILDFIRE_LOCATION = new ResourceLocation(Constants.MOD_ID, "textures/entity/wildfire.png");

    public WildfireRenderer(EntityRendererProvider.Context context) {
        super(context, new WildfireModel<>(context.bakeLayer(WildfireModel.LAYER_LOCATION)), 0.5F);
    }

    @Override
    protected int getBlockLightLevel(@NotNull T entity, @NotNull BlockPos blockPos) {
        return 15;
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull T entity) {
        return WILDFIRE_LOCATION;
    }
}