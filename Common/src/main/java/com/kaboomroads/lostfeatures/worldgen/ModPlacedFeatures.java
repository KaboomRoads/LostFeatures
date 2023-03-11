package com.kaboomroads.lostfeatures.worldgen;

import com.kaboomroads.lostfeatures.platform.Services;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> BAOBAB_TREE = Services.REGISTRY.registerPlacedFeature("baobab");
    public static final ResourceKey<PlacedFeature> THICK_BAOBAB_TREE = Services.REGISTRY.registerPlacedFeature("thick_baobab");
    public static final ResourceKey<PlacedFeature> TERMITE_NEST = Services.REGISTRY.registerPlacedFeature("termite_nest");
    public static final ResourceKey<PlacedFeature> TERMITE_CORE_NEST = Services.REGISTRY.registerPlacedFeature("termite_core_nest");

    public static void init() {
    }
}
