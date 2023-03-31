package com.kaboomroads.lostfeatures.block.custom;

import com.kaboomroads.lostfeatures.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class BadlandsCactusBlock extends PipeBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_15;

    public BadlandsCactusBlock(Properties properties) {
        super(0.25F, properties);
        registerDefaultState(stateDefinition.any().setValue(AGE, 0).setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false).setValue(UP, false).setValue(DOWN, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return getStateForPlacement(context.getLevel(), context.getClickedPos());
    }

    public BlockState getStateForPlacement(BlockGetter blockGetter, BlockPos blockPos) {
        BlockState below = blockGetter.getBlockState(blockPos.below());
        BlockState above = blockGetter.getBlockState(blockPos.above());
        BlockState north = blockGetter.getBlockState(blockPos.north());
        BlockState east = blockGetter.getBlockState(blockPos.east());
        BlockState south = blockGetter.getBlockState(blockPos.south());
        BlockState west = blockGetter.getBlockState(blockPos.west());
        return defaultBlockState()
                .setValue(DOWN, below.is(this) || below.is(Blocks.SAND) || below.is(Blocks.RED_SAND))
                .setValue(UP, above.is(this))
                .setValue(NORTH, north.is(this))
                .setValue(EAST, east.is(this))
                .setValue(SOUTH, south.is(this))
                .setValue(WEST, west.is(this));
    }

    @Override
    public void entityInside(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, Entity entity) {
        entity.hurt(DamageSource.CACTUS, 1.0F);
    }

    @NotNull
    @Override
    public BlockState updateShape(BlockState blockState, @NotNull Direction direction, @NotNull BlockState blockState1, @NotNull LevelAccessor levelAccessor, @NotNull BlockPos blockPos, @NotNull BlockPos blockPos1) {
        boolean $$6 = blockState1.is(this) || blockState1.is(ModBlocks.BADLANDS_CACTUS.get()) || direction == Direction.DOWN && (blockState1.is(Blocks.SAND) || blockState1.is(Blocks.RED_SAND));
        return blockState.setValue(PROPERTY_BY_DIRECTION.get(direction), $$6);
    }

    @Override
    public void tick(BlockState blockState, @NotNull ServerLevel level, @NotNull BlockPos blockPos, @NotNull RandomSource random) {
        if (!blockState.canSurvive(level, blockPos)) level.destroyBlock(blockPos, true);
    }

    @Override
    public boolean canSurvive(@NotNull BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        BlockState belowState = levelReader.getBlockState(blockPos.below());
        if (belowState.is(Blocks.SAND) || belowState.is(Blocks.RED_SAND)) return true;
        else return Arrays.stream(Direction.values()).anyMatch(direction -> {
            BlockState state = levelReader.getBlockState(blockPos.relative(direction));
            return state.is(this) || state.is(ModBlocks.BADLANDS_CACTUS.get());
        });
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AGE, NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    @Override
    public boolean isPathfindable(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull PathComputationType type) {
        return false;
    }

    protected boolean canPlace(@NotNull ServerLevel level, @NotNull BlockPos blockPos) {
        int i = 0;
        for (Direction dir : Direction.values()) {
            BlockPos rel = blockPos.relative(dir);
            BlockState relState = level.getBlockState(rel);
            if (dir == Direction.DOWN && (relState.is(Blocks.SAND) || relState.is(Blocks.RED_SAND)) || (relState.is(this) && ++i >= 2))
                return false;
        }
        return true;
    }

    @Override
    public void randomTick(@NotNull BlockState blockState, @NotNull ServerLevel level, @NotNull BlockPos blockPos, @NotNull RandomSource random) {
        ArrayList<BlockPos> possibilities = new ArrayList<>(5);
        for (Direction dir : Direction.values()) {
            BlockPos rel = blockPos.relative(dir);
            if (dir != Direction.DOWN && canPlace(level, rel)) possibilities.add(rel);
        }
        if (possibilities.isEmpty()) return;
        BlockPos relative = possibilities.get(random.nextInt(possibilities.size()));
        if (level.isEmptyBlock(relative)) {
            int j = blockState.getValue(AGE);
            if (j == 15) {
                level.setBlockAndUpdate(relative, getStateForPlacement(level, relative));
                BlockState newState = getStateForPlacement(level, blockPos).setValue(AGE, 0);
                level.setBlock(blockPos, newState, 3);
                level.neighborChanged(newState, relative, this, blockPos, false);
            } else level.setBlock(blockPos, blockState.setValue(AGE, j + 1), 4);
        }
    }
}