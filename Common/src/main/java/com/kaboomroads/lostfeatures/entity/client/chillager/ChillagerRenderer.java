package com.kaboomroads.lostfeatures.entity.client.chillager;

import com.kaboomroads.lostfeatures.Constants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import org.jetbrains.annotations.NotNull;

public class ChillagerRenderer<T extends SpellcasterIllager> extends IllagerRenderer<T> {
    private static final ResourceLocation CHILLAGER_LOCATION = new ResourceLocation(Constants.MOD_ID, "textures/entity/chillager.png");

    public ChillagerRenderer(EntityRendererProvider.Context context) {
        super(context, new IllagerModel<>(context.bakeLayer(ModelLayers.EVOKER)), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()) {
            public void render(@NotNull PoseStack p_114569_, @NotNull MultiBufferSource p_114570_, int p_114571_, @NotNull T entity, float p_114573_, float p_114574_, float p_114575_, float p_114576_, float p_114577_, float p_114578_) {
                if (entity.isCastingSpell())
                    super.render(p_114569_, p_114570_, p_114571_, entity, p_114573_, p_114574_, p_114575_, p_114576_, p_114577_, p_114578_);
            }
        });
        this.model.getHat().visible = true;
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull T entity) {
        return CHILLAGER_LOCATION;
    }
}