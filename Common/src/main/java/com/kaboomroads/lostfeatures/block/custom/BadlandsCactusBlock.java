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
import net.minecraft.world.level.pathfinder.PathComputationType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class BadlandsCactusBlock extends PipeBlock {
    public BadlandsCactusBlock(Properties properties) {
        super(0.25F, properties);
        registerDefaultState(stateDefinition.any().setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false).setValue(UP, false).setValue(DOWN, false));
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
                .setValue(DOWN, below.is(this) || below.is(ModBlocks.BADLANDS_CACTUS.get()) || below.is(Blocks.SAND) || below.is(Blocks.RED_SAND))
                .setValue(UP, above.is(this) || above.is(ModBlocks.BADLANDS_CACTUS.get()))
                .setValue(NORTH, north.is(this) || north.is(ModBlocks.BADLANDS_CACTUS.get()))
                .setValue(EAST, east.is(this) || east.is(ModBlocks.BADLANDS_CACTUS.get()))
                .setValue(SOUTH, south.is(this) || south.is(ModBlocks.BADLANDS_CACTUS.get()))
                .setValue(WEST, west.is(this) || west.is(ModBlocks.BADLANDS_CACTUS.get()));
    }

    @Override
    public void entityInside(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, Entity entity) {
        entity.hurt(DamageSource.CACTUS, 1.0F);
    }

    @NotNull
    @Override
    public BlockState updateShape(BlockState blockState, @NotNull Direction direction, @NotNull BlockState blockState1, @NotNull LevelAccessor levelAccessor, @NotNull BlockPos blockPos, @NotNull BlockPos blockPos1) {
        if (!blockState.canSurvive(levelAccessor, blockPos)) {
            levelAccessor.scheduleTick(blockPos, this, 1);
            return super.updateShape(blockState, direction, blockState1, levelAccessor, blockPos, blockPos1);
        } else {
            boolean $$6 = blockState1.is(this) || blockState1.is(ModBlocks.BADLANDS_CACTUS.get()) || direction == Direction.DOWN && (blockState1.is(Blocks.SAND) || blockState1.is(Blocks.RED_SAND));
            return blockState.setValue(PROPERTY_BY_DIRECTION.get(direction), $$6);
        }
    }

    @Override
    public void tick(BlockState blockState, @NotNull ServerLevel level, @NotNull BlockPos blockPos, @NotNull RandomSource random) {
        if (!blockState.canSurvive(level, blockPos)) level.destroyBlock(blockPos, true);
    }

    //TODO: PREVENT FLOATY BITS
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
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    }

    @Override
    public boolean isPathfindable(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull PathComputationType type) {
        return false;
    }
}