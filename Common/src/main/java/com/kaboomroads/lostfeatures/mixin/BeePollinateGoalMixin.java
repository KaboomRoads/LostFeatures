package com.kaboomroads.lostfeatures.mixin;

import com.kaboomroads.lostfeatures.entity.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Mixin(targets = {"net.minecraft.world.entity.animal.Bee$BeePollinateGoal"})
public abstract class BeePollinateGoalMixin {
    @Final
    @Shadow(aliases = "field_20377")
    Bee this$0;

    @Inject(method = "findNearbyFlower", at = @At("RETURN"))
    public void redirectFindNearbyFlower(CallbackInfoReturnable<Optional<BlockPos>> cir) {
        Optional<LivingEntity> nearest = getNearestEntity(this$0);
        if (nearest.isPresent() && (cir.getReturnValue().isEmpty() || nearest.get().blockPosition().distSqr(this$0.blockPosition()) < cir.getReturnValue().get().distSqr(this$0.blockPosition()))) {
            BlockPos blockPos = nearest.get().blockPosition();
            this$0.getNavigation().moveTo((double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 3.0D, (double) blockPos.getZ() + 0.5D, 1.2D);
        }
    }

    private Optional<LivingEntity> getNearestEntity(LivingEntity entity) {
        return getVisibleEntities(entity, ((ServerLevel) entity.level)).flatMap(($$1) -> $$1.findClosest(($$1x) -> isMatchingEntity(entity, $$1x)));
    }

    private boolean isMatchingEntity(@NotNull LivingEntity entity, @NotNull LivingEntity otherEntity) {
        return this.isClose(entity, otherEntity) && otherEntity.getType() == ModEntityTypes.MOOBLOOM.get();
    }

    private boolean isClose(LivingEntity entity, LivingEntity otherEntity) {
        double followRange = entity.getAttributeValue(Attributes.FOLLOW_RANGE);
        return otherEntity.distanceToSqr(entity) <= followRange * followRange;
    }

    private Optional<NearestVisibleLivingEntities> getVisibleEntities(LivingEntity entity, ServerLevel level) {
        AABB aabb = entity.getBoundingBox().inflate(entity.getAttributeValue(Attributes.FOLLOW_RANGE), entity.getAttributeValue(Attributes.FOLLOW_RANGE), entity.getAttributeValue(Attributes.FOLLOW_RANGE));
        List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, aabb, (p_26717_) -> p_26717_ != entity && p_26717_.isAlive());
        list.sort(Comparator.comparingDouble(entity::distanceToSqr));
        return Optional.of(new NearestVisibleLivingEntities(entity, list));
    }
}
