package com.kaboomroads.lostfeatures.mixin;

import com.kaboomroads.lostfeatures.damagesource.ModDamageSources;
import com.kaboomroads.lostfeatures.damagesource.ModDamageTypes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DamageSources.class)
public abstract class DamageSourcesMixin implements ModDamageSources {
    @Unique
    private DamageSource lostfeatures$sculkAttack;

    @Shadow
    protected abstract DamageSource source(ResourceKey<DamageType> $$0);

    @Shadow
    protected abstract DamageSource source(ResourceKey<DamageType> p_270076_, @Nullable Entity p_270656_, @Nullable Entity p_270242_);

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void injectInit(RegistryAccess $$0, CallbackInfo ci) {
        lostfeatures$sculkAttack = source(ModDamageTypes.SCULK_ATTACK);
    }

    @Override
    public DamageSource lostfeatures$iceChunk(Entity entity, @Nullable LivingEntity source) {
        return source(ModDamageTypes.ICE_CHUNK, entity, source);
    }

    @Override
    public DamageSource lostfeatures$sculkAttack() {
        return lostfeatures$sculkAttack;
    }
}
