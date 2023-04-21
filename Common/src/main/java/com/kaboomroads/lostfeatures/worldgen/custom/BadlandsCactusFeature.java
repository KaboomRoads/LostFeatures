package com.kaboomroads.lostfeatures.worldgen.custom;

import com.kaboomroads.lostfeatures.block.ModBlocks;
import com.kaboomroads.lostfeatures.block.custom.BadlandsCactusBlock;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.ArrayList;
import java.util.LinkedList;

public class BadlandsCactusFeature extends Feature<NoneFeatureConfiguration> {
    public BadlandsCactusFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos blockPos = context.origin();
        RandomSource random = context.random();
        BlockState belowState = level.getBlockState(blockPos.below());
        if (level.isEmptyBlock(blockPos) && (belowState.is(Blocks.SAND) || belowState.is(Blocks.RED_SAND))) {
            int amount = random.nextInt(7, 10);
            BadlandsCactusBlock cactus = ((BadlandsCactusBlock) ModBlocks.BADLANDS_CACTUS.get());
            level.setBlock(blockPos, cactus.getStateForPlacement(level, blockPos), 4);
            LinkedList<BlockPos> list = new LinkedList<>();
            list.add(blockPos);
            for (int i = 0; i < amount; i++) {
                LinkedList<BlockPos> newPositions = new LinkedList<>();
                for (BlockPos pos : list)
                    if (random.nextInt(2) == 0) {
                        BlockPos grow = grow(cactus, level, pos, level.getRandom());
                        if (grow != null) newPositions.add(grow);
                    }
                list.addAll(newPositions);
            }
            return true;
        } else return false;
    }

    public static BlockPos grow(BadlandsCactusBlock block, WorldGenLevel level, BlockPos blockPos, RandomSource random) {
        ArrayList<BlockPos> possibilities = new ArrayList<>(5);
        for (Direction dir : Direction.values()) {
            BlockPos rel = blockPos.relative(dir);
            if (dir != Direction.DOWN && block.canPlace(level, rel)) possibilities.add(rel);
        }
        if (possibilities.isEmpty()) return null;
        BlockPos relative = possibilities.get(random.nextInt(possibilities.size()));
        if (level.getBlockState(relative).isAir()) {
            level.setBlock(relative, block.getStateForPlacement(level, relative), 3);
            BlockState newState = block.getStateForPlacement(level, blockPos);
            level.setBlock(blockPos, newState, 4);
            level.getLevel().neighborChanged(newState, relative, block, blockPos, false);
            return relative;
        }
        return null;
    }
}
