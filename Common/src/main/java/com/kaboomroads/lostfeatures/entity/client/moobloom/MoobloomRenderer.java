package com.kaboomroads.lostfeatures.entity.client.moobloom;

import com.kaboomroads.lostfeatures.Constants;
import com.kaboomroads.lostfeatures.entity.custom.Moobloom;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class MoobloomRenderer<T extends Moobloom> extends MobRenderer<T, CowModel<T>> {
    private static final ResourceLocation MOOBLOOM_LOCATION = new ResourceLocation(Constants.MOD_ID, "textures/entity/moobloom.png");

    public MoobloomRenderer(EntityRendererProvider.Context context) {
        super(context, new CowModel<>(context.bakeLayer(ModelLayers.MOOSHROOM)), 0.7F);
        addLayer(new MoobloomFlowerLayer<>(this, context.getBlockRenderDispatcher()));
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull T entity) {
        return MOOBLOOM_LOCATION;
    }
}