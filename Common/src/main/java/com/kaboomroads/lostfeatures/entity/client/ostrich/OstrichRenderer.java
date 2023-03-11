package com.kaboomroads.lostfeatures.entity.client.ostrich;

import com.kaboomroads.lostfeatures.Constants;
import com.kaboomroads.lostfeatures.entity.custom.Ostrich;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class OstrichRenderer<T extends Ostrich> extends MobRenderer<T, OstrichModel<T>> {
    private static final ResourceLocation OSTRICH_LOCATION = new ResourceLocation(Constants.MOD_ID, "textures/entity/ostrich.png");

    public OstrichRenderer(EntityRendererProvider.Context context) {
        super(context, new OstrichModel<>(context.bakeLayer(OstrichModel.LAYER_LOCATION)), 0.5F);
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull T entity) {
        return OSTRICH_LOCATION;
    }
}