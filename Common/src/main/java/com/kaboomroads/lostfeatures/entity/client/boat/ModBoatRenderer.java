package com.kaboomroads.lostfeatures.entity.client.boat;

import com.google.common.collect.ImmutableMap;
import com.kaboomroads.lostfeatures.Constants;
import com.kaboomroads.lostfeatures.entity.custom.ModBoat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.Boat;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;

import java.util.Map;
import java.util.stream.Stream;

public class ModBoatRenderer extends EntityRenderer<ModBoat> {
    private final Map<ModBoat.Type, Pair<ResourceLocation, ListModel<Boat>>> boatResources;

    public ModBoatRenderer(EntityRendererProvider.Context context, boolean hasChest, boolean raft) {
        super(context);
        this.shadowRadius = 0.8F;
        this.boatResources = Stream.of(ModBoat.Type.values()).collect(ImmutableMap.toImmutableMap((type) -> type, (type) -> Pair.of(new ResourceLocation(Constants.MOD_ID, getTextureLocation(type, hasChest)), this.createBoatModel(context, type, hasChest, raft))));
    }

    protected ListModel<Boat> createBoatModel(EntityRendererProvider.Context modelPart, ModBoat.Type type, boolean hasChest, boolean raft) {
        ModelLayerLocation modellayerlocation = createBoatModelName(type, hasChest);
        ModelPart modelpart = modelPart.bakeLayer(modellayerlocation);
        if (raft) return hasChest ? new ChestRaftModel(modelpart) : new RaftModel(modelpart);
        else return hasChest ? new ChestBoatModel(modelpart) : new BoatModel(modelpart);
    }

    public static ModelLayerLocation createBoatModelName(ModBoat.Type type, boolean hasChest) {
        return new ModelLayerLocation(new ResourceLocation(Constants.MOD_ID, hasChest ? "chest_boat/" : "boat/" + type.getName()), "main");
    }

    protected static String getTextureLocation(ModBoat.Type p_234566_, boolean hasChest) {
        return hasChest ? "textures/entity/chest_boat/" + p_234566_.getName() + ".png" : "textures/entity/boat/" + p_234566_.getName() + ".png";
    }

    public void render(ModBoat boat, float p_113930_, float p_113931_, PoseStack p_113932_, @NotNull MultiBufferSource multiBufferSource, int p_113934_) {
        p_113932_.pushPose();
        p_113932_.translate(0.0F, 0.375F, 0.0F);
        p_113932_.mulPose(Axis.YP.rotationDegrees(180.0F - p_113930_));
        float f = (float) boat.getHurtTime() - p_113931_;
        float f1 = boat.getDamage() - p_113931_;
        if (f1 < 0.0F) f1 = 0.0F;
        if (f > 0.0F)
            p_113932_.mulPose(Axis.XP.rotationDegrees(Mth.sin(f) * f * f1 / 10.0F * (float) boat.getHurtDir()));
        float f2 = boat.getBubbleAngle(p_113931_);
        if (!Mth.equal(f2, 0.0F))
            p_113932_.mulPose((new Quaternionf()).setAngleAxis(boat.getBubbleAngle(p_113931_) * ((float) Math.PI / 180F), 1.0F, 0.0F, 1.0F));
        Pair<ResourceLocation, ListModel<Boat>> pair = getModelWithLocation(boat);
        ResourceLocation resourcelocation = pair.getFirst();
        ListModel<Boat> listmodel = pair.getSecond();
        p_113932_.scale(-1.0F, -1.0F, 1.0F);
        p_113932_.mulPose(Axis.YP.rotationDegrees(90.0F));
        listmodel.setupAnim(boat, p_113931_, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer vertexconsumer = multiBufferSource.getBuffer(listmodel.renderType(resourcelocation));
        listmodel.renderToBuffer(p_113932_, vertexconsumer, p_113934_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        if (!boat.isUnderWater()) {
            VertexConsumer vertexconsumer1 = multiBufferSource.getBuffer(RenderType.waterMask());
            if (listmodel instanceof WaterPatchModel waterpatchmodel)
                waterpatchmodel.waterPatch().render(p_113932_, vertexconsumer1, p_113934_, OverlayTexture.NO_OVERLAY);
        }
        p_113932_.popPose();
        super.render(boat, p_113930_, p_113931_, p_113932_, multiBufferSource, p_113934_);
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull ModBoat boat) {
        return getModelWithLocation(boat).getFirst();
    }

    public Pair<ResourceLocation, ListModel<Boat>> getModelWithLocation(ModBoat boat) {
        return this.boatResources.get(boat.getCustomVariant());
    }
}