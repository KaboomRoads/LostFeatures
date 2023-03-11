package com.kaboomroads.lostfeatures.entity.ai.behaviour;

import com.kaboomroads.lostfeatures.mixin.SensorAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.NearestVisibleLivingEntitySensor;
import net.minecraft.world.entity.ai.sensing.Sensor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class AttackablesSensor extends NearestVisibleLivingEntitySensor {
    public final TagKey<EntityType<?>> alwaysHostiles;
    public final Predicate<LivingEntity> predicate;

    public AttackablesSensor(TagKey<EntityType<?>> alwaysHostiles, Predicate<LivingEntity> predicate, int interval) {
        ((SensorAccessor) this).setScanRate(interval);
        ((SensorAccessor) this).setTimeToTick(SensorAccessor.getRandom().nextInt(interval));
        this.alwaysHostiles = alwaysHostiles;
        this.predicate = predicate;
    }

    protected boolean isMatchingEntity(@NotNull LivingEntity entity, @NotNull LivingEntity otherEntity) {
        return this.isClose(entity, otherEntity) && isHostileTarget(otherEntity) && Sensor.isEntityAttackable(entity, otherEntity);
    }

    private boolean isHostileTarget(LivingEntity entity) {
        return entity.getType().is(alwaysHostiles);
    }

    private boolean isClose(LivingEntity entity, LivingEntity otherEntity) {
        double followRange = entity.getAttributeValue(Attributes.FOLLOW_RANGE);
        return otherEntity.distanceToSqr(entity) <= followRange * followRange;
    }

    protected void doTick(@NotNull ServerLevel level, @NotNull LivingEntity entity) {
        if (predicate.test(entity)) super.doTick(level, entity);
    }

    @NotNull
    protected MemoryModuleType<LivingEntity> getMemory() {
        return MemoryModuleType.NEAREST_ATTACKABLE;
    }
}