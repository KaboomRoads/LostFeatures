package com.kaboomroads.lostfeatures.worldgen;

import com.kaboomroads.lostfeatures.platform.Services;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> BAOBAB = Services.REGISTRY.registerConfiguredFeature("baobab");
    public static final ResourceKey<ConfiguredFeature<?, ?>> THICK_BAOBAB = Services.REGISTRY.registerConfiguredFeature("thick_baobab");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TERMITE_NEST = Services.REGISTRY.registerConfiguredFeature("termite_nest");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TERMITE_CORE_NEST = Services.REGISTRY.registerConfiguredFeature("termite_core_nest");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_TERMITE_NEST = Services.REGISTRY.registerConfiguredFeature("large_termite_nest");

    public static void init() {
    }
}
