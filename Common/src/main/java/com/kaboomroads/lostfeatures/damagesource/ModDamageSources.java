package com.kaboomroads.lostfeatures.damagesource;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public interface ModDamageSources {
    DamageSource lostfeatures$iceChunk(Entity entity, @Nullable LivingEntity source);

    DamageSource lostfeatures$sculkAttack();
}
