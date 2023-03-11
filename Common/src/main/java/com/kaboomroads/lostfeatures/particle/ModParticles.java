package com.kaboomroads.lostfeatures.particle;

import com.kaboomroads.lostfeatures.platform.Services;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.function.Supplier;

public class ModParticles {
    public static final Supplier<SimpleParticleType> FIREFLY_PARTICLE =
            Services.REGISTRY.registerParticle("firefly", () -> Services.REGISTRY.getSimpleParticleType(true));
    public static final Supplier<SimpleParticleType> TERMITE_PARTICLE =
            Services.REGISTRY.registerParticle("termite", () -> Services.REGISTRY.getSimpleParticleType(true));

    public static void init() {
    }
}
