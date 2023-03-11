package com.kaboomroads.lostfeatures.entity.custom;

import com.google.common.collect.ImmutableList;
import com.kaboomroads.lostfeatures.entity.ModEntityTypes;
import com.kaboomroads.lostfeatures.entity.ai.ModSensorType;
import com.kaboomroads.lostfeatures.entity.ai.brain.OstrichAi;
import com.mojang.serialization.Dynamic;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class Ostrich extends Animal {
    public static final Ingredient FOOD_ITEMS = Ingredient.of(ItemTags.FLOWERS);
    protected static final ImmutableList<SensorType<? extends Sensor<? super Ostrich>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY, SensorType.NEAREST_ADULT, ModSensorType.OSTRICH_TEMPTATIONS.get());
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.IS_PANICKING, MemoryModuleType.HURT_BY, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.BREED_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH);
    public final AnimationState walkAnimationState = new AnimationState();

    public Ostrich(EntityType<? extends Ostrich> type, Level level) {
        super(type, level);
    }

    @NotNull
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.2D);
    }

    @NotNull
    @Override
    protected Brain.Provider<Ostrich> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    @NotNull
    @Override
    protected Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return OstrichAi.makeBrain(brainProvider().makeBrain(dynamic));
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public Brain<Ostrich> getBrain() {
        return (Brain<Ostrich>) super.getBrain();
    }

    @Override
    protected void customServerAiStep() {
        level.getProfiler().push("ostrichBrain");
        getBrain().tick((ServerLevel) level, this);
        level.getProfiler().pop();
        OstrichAi.updateActivity(this);
        super.customServerAiStep();
    }

    @Override
    public boolean isFood(@NotNull ItemStack item) {
        return FOOD_ITEMS.test(item);
    }

    @Nullable
    @Override
    public Ostrich getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob ageableMob) {
        return ModEntityTypes.OSTRICH.get().create(level);
    }

    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide)
            walkAnimationState.animateWhen((onGround || hasControllingPassenger()) && !isNoAi() && getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D, tickCount);
    }
}
