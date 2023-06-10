package com.kaboomroads.lostfeatures.mixin;

import com.google.common.collect.ImmutableMap;
import com.kaboomroads.lostfeatures.entity.ModEntityTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.sensing.VillagerHostilesSensor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(VillagerHostilesSensor.class)
public abstract class VillagerHostilesSensorMixin {
    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableMap;builder()Lcom/google/common/collect/ImmutableMap$Builder;"))
    private static ImmutableMap.Builder<EntityType<?>, Float> injectBuilder() {
        return ImmutableMap.<EntityType<?>, Float>builder().put(ModEntityTypes.CHILLAGER.get(), 12.0F);
    }
}
