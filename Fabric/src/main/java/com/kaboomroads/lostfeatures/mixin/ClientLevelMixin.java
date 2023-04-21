package com.kaboomroads.lostfeatures.mixin;

import com.kaboomroads.lostfeatures.particle.ModParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level {
    protected ClientLevelMixin(WritableLevelData writableLevelData, ResourceKey<Level> resourceKey, RegistryAccess registryAccess, Holder<DimensionType> holder, Supplier<ProfilerFiller> supplier, boolean bl, boolean bl2, long l, int i) {
        super(writableLevelData, resourceKey, registryAccess, holder, supplier, bl, bl2, l, i);
    }

    @Redirect(remap = false, method = "method_24462", at = @At(remap = true, value = "INVOKE", target = "Lnet/minecraft/world/level/biome/AmbientParticleSettings;canSpawn(Lnet/minecraft/util/RandomSource;)Z"))
    public boolean redirectCanSpawn(AmbientParticleSettings instance, RandomSource random) {
        int time = (int) (this.getDayTime() % 24000L);
        return instance.canSpawn(random) && (!(instance.getOptions().getType() == ModParticles.FIREFLY_PARTICLE.get()) || time <= 23000 && time >= 13000);
    }
}
