package com.kaboomroads.lostfeatures.mixin;

import com.kaboomroads.lostfeatures.particle.ModParticles;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level {
    protected ClientLevelMixin(WritableLevelData writableLevelData, ResourceKey<Level> resourceKey, RegistryAccess registryAccess, Holder<DimensionType> holder, Supplier<ProfilerFiller> supplier, boolean bl, boolean bl2, long l, int i) {
        super(writableLevelData, resourceKey, registryAccess, holder, supplier, bl, bl2, l, i);
    }

    //m_263888_ for building lambda$doAnimateTick$8 for testing
    @ModifyExpressionValue(remap = false, method = "m_263888_", at = @At(remap = true, value = "INVOKE", target = "Lnet/minecraft/world/level/biome/AmbientParticleSettings;canSpawn(Lnet/minecraft/util/RandomSource;)Z"))
    public boolean modifyCanSpawn(boolean original, BlockPos.MutableBlockPos pos, AmbientParticleSettings instance) {
        int time = (int) (this.getDayTime() % 24000L);
        return original && (!(instance.getOptions().getType() == ModParticles.FIREFLY_PARTICLE.get()) || time <= 23000 && time >= 13000);
    }
}
