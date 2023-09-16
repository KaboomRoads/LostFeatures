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
    protected ClientLevelMixin(WritableLevelData p_270739_, ResourceKey<Level> p_270683_, RegistryAccess p_270200_, Holder<DimensionType> p_270240_, Supplier<ProfilerFiller> p_270692_, boolean p_270904_, boolean p_270470_, long p_270248_, int p_270466_) {
        super(p_270739_, p_270683_, p_270200_, p_270240_, p_270692_, p_270904_, p_270470_, p_270248_, p_270466_);
    }

    @ModifyExpressionValue(remap = false, method = "lambda$doAnimateTick$8", at = @At(remap = true, value = "INVOKE", target = "Lnet/minecraft/world/level/biome/AmbientParticleSettings;canSpawn(Lnet/minecraft/util/RandomSource;)Z"))
    public boolean modifyCanSpawn(boolean original, BlockPos.MutableBlockPos pos, AmbientParticleSettings instance) {
        int time = (int) (this.getDayTime() % 24000L);
        return original && (!(instance.getOptions().getType() == ModParticles.FIREFLY_PARTICLE.get()) || time <= 23000 && time >= 13000);
    }
}
