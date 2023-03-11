package com.kaboomroads.lostfeatures.mixin;

import com.google.common.collect.ImmutableMap;
import com.kaboomroads.lostfeatures.entity.ModEntityTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.VillagerHostilesSensor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerHostilesSensor.class)
public abstract class VillagerHostilesSensorMixin {
    private static final ImmutableMap<EntityType<?>, Float> ACCEPTABLE_DISTANCE_FROM_MOD_HOSTILES = ImmutableMap.<EntityType<?>, Float>builder().put(ModEntityTypes.CHILLAGER.get(), 12.0F).build();

    @Inject(method = "isClose", at = @At("HEAD"), cancellable = true)
    private void isClose(LivingEntity $$0, LivingEntity $$1, CallbackInfoReturnable<Boolean> cir) {
        float f = ACCEPTABLE_DISTANCE_FROM_MOD_HOSTILES.get($$1.getType());
        cir.setReturnValue($$1.distanceToSqr($$0) <= (double) (f * f));
    }

    @Inject(method = "isHostile", at = @At("HEAD"), cancellable = true)
    private void isHostile(LivingEntity $$0, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(ACCEPTABLE_DISTANCE_FROM_MOD_HOSTILES.containsKey($$0.getType()));
    }
}
