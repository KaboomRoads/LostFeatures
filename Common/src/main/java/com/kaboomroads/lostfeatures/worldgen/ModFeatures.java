package com.kaboomroads.lostfeatures.worldgen;

import com.kaboomroads.lostfeatures.platform.Services;
import com.kaboomroads.lostfeatures.worldgen.configuration.TermiteNestConfiguration;
import com.kaboomroads.lostfeatures.worldgen.custom.TermiteNestFeature;

import java.util.function.Supplier;

public class ModFeatures {
    public static final Supplier<TermiteNestFeature> TERMITE_NEST = Services.REGISTRY.registerFeature("termite_nest", () -> new TermiteNestFeature(TermiteNestConfiguration.CODEC));

    public static void init() {
    }
}
