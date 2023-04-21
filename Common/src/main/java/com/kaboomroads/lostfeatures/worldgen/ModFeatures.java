package com.kaboomroads.lostfeatures.worldgen;

import com.kaboomroads.lostfeatures.platform.Services;
import com.kaboomroads.lostfeatures.worldgen.configuration.TermiteNestConfiguration;
import com.kaboomroads.lostfeatures.worldgen.custom.BadlandsCactusFeature;
import com.kaboomroads.lostfeatures.worldgen.custom.TermiteNestFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.function.Supplier;

public class ModFeatures {
    public static final Supplier<TermiteNestFeature> TERMITE_NEST = Services.REGISTRY.registerFeature("termite_nest", () -> new TermiteNestFeature(TermiteNestConfiguration.CODEC));
    public static final Supplier<BadlandsCactusFeature> BADLANDS_CACTUS = Services.REGISTRY.registerFeature("badlands_cactus", () -> new BadlandsCactusFeature(NoneFeatureConfiguration.CODEC));

    public static void init() {
    }
}
