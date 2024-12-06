package com.kaboomroads.lostfeatures.worldgen.custom;

import com.kaboomroads.lostfeatures.worldgen.configuration.TermiteNestConfiguration;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class TermiteNestFeature extends Feature<TermiteNestConfiguration> {
    public TermiteNestFeature(Codec<TermiteNestConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<TermiteNestConfiguration> context) {
        BlockPos pos = context.origin();
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        TermiteNestConfiguration config = context.config();
        BlockPos.MutableBlockPos cursor = pos.mutable();
        for (int i = 0; i < 32 && pos.getY() > level.getMinBuildHeight() + 4; i++) {
            BlockState state = level.getBlockState(cursor);
            if (state.is(config.canNotGenerateOn())) return false;
            else if (state.is(config.canGenerateOn())) break;
            cursor.move(Direction.DOWN);
        }
        if (cursor.getY() <= level.getMinBuildHeight() + 4 || !level.getBlockState(cursor).is(config.canGenerateOn()))
            return false;
        else {
            int xSize = config.xSize().sample(random);
            int zSize = config.zSize().sample(random);
            float spireChance = config.spireChance().sample(random);
            int maxSpireCount = config.maxSpireCount().sample(random);
            generateNest(level, cursor, xSize, zSize, config.height(), config.depth(), config.stateProvider(), config.spireProvider(), spireChance, maxSpireCount, config.lastResortSpire(), config.core(), config.coreProvider(), random);
            return true;
        }
    }

    public static void generateNest(WorldGenLevel level, BlockPos center, int xSize, int zSize, IntProvider height, IntProvider depth, BlockStateProvider stateProvider, BlockStateProvider spireProvider, float spireChance, int maxSpireCount, boolean lastResortSpire, boolean core, BlockStateProvider coreProvider, RandomSource random) {
        int deltaX = Mth.floor(xSize * 0.5f);
        int deltaZ = Mth.floor(zSize * 0.5f);
        BlockPos.MutableBlockPos cursor = center.mutable();
        int spires = 0;
        boolean b = false;
        xSize *= 2;
        for (int x = 0; x < xSize; x++) {
            for (int z = 0; z < zSize; z++) {
                int ySize = height.sample(random);
                int yDepth = depth.sample(random);
                BlockPos.MutableBlockPos pos = cursor.offset(z - deltaX, -yDepth, z - deltaZ).mutable();
                for (int y = 0; y < ySize; y++) {
                    level.setBlock(pos, core && pos.equals(center) ? coreProvider.getState(random, pos) : stateProvider.getState(random, pos), 4);
                    pos.move(0, 1, 0);
                    if (spires < maxSpireCount)
                        if (y >= ySize - 1 && random.nextFloat() <= spireChance) {
                            ++spires;
                            level.setBlock(pos, spireProvider.getState(random, pos), 4);
                        } else if (lastResortSpire && spires <= 0 && x >= xSize - 1)
                            level.setBlock(pos, spireProvider.getState(random, pos), 4);
                }
            }
            cursor.move(b ? 0 : 1, 0, b ? -1 : 0);
            b = !b;
        }
    }
}
