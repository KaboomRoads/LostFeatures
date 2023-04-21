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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(DamageSources.class)
public abstract class DamageSourcesMixin implements ModDamageSources {
    private DamageSource sculkAttack;

    @Shadow
    protected abstract DamageSource source(ResourceKey<DamageType> $$0);

    @Shadow
    protected abstract DamageSource source(ResourceKey<DamageType> $$0, @Nullable Entity $$1);

    @Shadow
    protected abstract DamageSource source(ResourceKey<DamageType> $$0, @Nullable Entity $$1, @Nullable Entity $$2);

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void injectInit(RegistryAccess $$0, CallbackInfo ci) {
        sculkAttack = source(ModDamageTypes.SCULK_ATTACK);
    }

    @Override
    public DamageSource iceChunk(Entity entity, @Nullable LivingEntity source) {
        return ((DamageSourcesInvoker) this).invokeSource(ModDamageTypes.ICE_CHUNK, entity, source);
    }

    @Override
    public DamageSource sculkAttack() {
        return sculkAttack;
    }
}
