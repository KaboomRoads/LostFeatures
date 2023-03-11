package com.kaboomroads.lostfeatures.worldgen.tree;

import com.kaboomroads.lostfeatures.worldgen.ModFoliagePlacerTypes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class PalmFoliagePlacer extends FoliagePlacer {
    public static final Codec<PalmFoliagePlacer> CODEC = RecordCodecBuilder.create(instance ->
            foliagePlacerParts(instance).apply(instance, PalmFoliagePlacer::new)
    );

    public PalmFoliagePlacer(IntProvider $$0, IntProvider $$1) {
        super($$0, $$1);
    }

    @NotNull
    @Override
    protected FoliagePlacerType<?> type() {
        return ModFoliagePlacerTypes.PALM_FOLIAGE_PLACER.get();
    }

    @Override
    protected void createFoliage(@NotNull LevelSimulatedReader levelReader, @NotNull BiConsumer<BlockPos, BlockState> biConsumer, @NotNull RandomSource random, @NotNull TreeConfiguration config, int maxTreeHeight, FoliageAttachment foliageAttachment, int height, int radius, int offset) {
        boolean doubleTrunk = foliageAttachment.doubleTrunk();
        BlockPos pos = foliageAttachment.pos().above(offset);
        placeLeavesRow(levelReader, biConsumer, random, config, pos, radius + foliageAttachment.radiusOffset(), -1 - height, doubleTrunk);
    }

    @Override
    public int foliageHeight(@NotNull RandomSource random, int $$1, @NotNull TreeConfiguration config) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(@NotNull RandomSource random, int $$1, int $$2, int $$3, int $$4, boolean $$5) {
        if ($$2 == 0) return ($$1 > 1 || $$3 > 1) && $$1 != 0 && $$3 != 0;
        else return $$1 == $$4 && $$3 == $$4 && $$4 > 0;
    }
}
