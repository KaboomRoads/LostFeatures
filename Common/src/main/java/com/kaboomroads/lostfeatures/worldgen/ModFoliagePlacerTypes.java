package com.kaboomroads.lostfeatures.worldgen;

import com.kaboomroads.lostfeatures.platform.Services;
import com.kaboomroads.lostfeatures.worldgen.tree.PalmFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

import java.util.function.Supplier;

public class ModFoliagePlacerTypes {
    public static final Supplier<FoliagePlacerType<PalmFoliagePlacer>> PALM_FOLIAGE_PLACER = Services.REGISTRY.registerFoliagePlacerType("palm_foliage_placer", PalmFoliagePlacer.CODEC);

    public static void init() {
    }
}
