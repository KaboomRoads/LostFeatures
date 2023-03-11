package com.kaboomroads.lostfeatures.worldgen;

import com.kaboomroads.lostfeatures.platform.Services;
import com.kaboomroads.lostfeatures.worldgen.tree.BaobabTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.function.Supplier;

public class ModTrunkPlacerTypes {
    public static final Supplier<TrunkPlacerType<BaobabTrunkPlacer>> BAOBAB_TRUNK_PLACER = Services.REGISTRY.registerTrunkPlacerType("baobab_trunk_placer", BaobabTrunkPlacer.CODEC);

    public static void init() {
    }
}
