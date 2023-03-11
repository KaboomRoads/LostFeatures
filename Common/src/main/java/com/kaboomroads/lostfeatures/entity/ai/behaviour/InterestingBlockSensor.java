package com.kaboomroads.lostfeatures.entity.ai.behaviour;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InterestingBlockSensor extends Sensor<LivingEntity> {
    private final Predicate<BlockState> targetType;
    private final boolean nextTo;
    private final int xRange, yRangeN, yRangeP, zRange, memoryDuration;
    private final MemoryModuleType<BlockPos> memoryType;
    @Nullable
    private final MemoryModuleType<BlockPos> originalBlockMemory;

    public InterestingBlockSensor(int interval, Predicate<BlockState> targetType, boolean nextTo, int xRange, int yRangeN, int yRangeP, int zRange, int memoryDuration, MemoryModuleType<BlockPos> memoryType, @Nullable MemoryModuleType<BlockPos> originalBlockMemory) {
        super(interval);
        this.targetType = targetType;
        this.nextTo = nextTo;
        this.xRange = xRange;
        this.yRangeN = yRangeN;
        this.yRangeP = yRangeP;
        this.zRange = zRange;
        this.memoryDuration = memoryDuration;
        this.memoryType = memoryType;
        this.originalBlockMemory = originalBlockMemory;
    }

    @NotNull
    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(memoryType);
    }

    @Override
    protected void doTick(@NotNull ServerLevel serverLevel, LivingEntity livingEntity) {
        Brain<?> brain = livingEntity.getBrain();
        Optional<BlockPos> blockPosition = brain.getMemory(memoryType);
        if (blockPosition.isEmpty()) {
            Optional<BlockPos> blockPos = findBlock(serverLevel, targetType, livingEntity, xRange, yRangeN, yRangeP, zRange);
            BlockPos pos = blockPos.orElse(null);
            if (originalBlockMemory != null) brain.setMemory(originalBlockMemory, pos);
            if (nextTo && pos != null) {
                ArrayList<Direction> possibleDirs = Arrays.stream(Direction.values()).filter(Direction.Plane.HORIZONTAL).collect(Collectors.toCollection(ArrayList::new));
                final BlockPos finalPos = pos;
                possibleDirs.removeIf(dir -> !serverLevel.getBlockState(finalPos.relative(dir)).isAir());
                if (!possibleDirs.isEmpty()) {
                    Direction direction = possibleDirs.get(livingEntity.getRandom().nextInt(possibleDirs.size()));
                    pos = pos.relative(direction);
                }
            }
            if (blockPos.isPresent()) setInterestingBlockLocation(livingEntity, memoryType, memoryDuration, pos);
        } else if (!targetType.test(serverLevel.getBlockState(blockPosition.get())))
            setInterestingBlockLocation(livingEntity, memoryType, memoryDuration, null);
    }

    private static Optional<BlockPos> findBlock(ServerLevel serverLevel, Predicate<BlockState> targetType, LivingEntity livingEntity, int xRange, int yRangeN, int yRangeP, int zRange) {
        BlockPos blockPos = livingEntity.blockPosition();
        LinkedList<BlockPos> positions = new LinkedList<>();
        for (BlockPos pos : BlockPos.betweenClosed(blockPos.offset(-xRange, -yRangeN, -zRange), blockPos.offset(xRange, yRangeP, zRange)))
            if (targetType.test(serverLevel.getBlockState(pos))) positions.add(pos.immutable());
        if (!positions.isEmpty())
            return Optional.ofNullable(positions.get(livingEntity.getRandom().nextInt(positions.size())));
        return Optional.empty();
    }

    public static void setInterestingBlockLocation(LivingEntity entity, MemoryModuleType<BlockPos> memoryType, int memoryDuration, @Nullable BlockPos blockPos) {
        Brain<?> brain = entity.getBrain();
        if (blockPos != null && entity.level.getWorldBorder().isWithinBounds(blockPos)) {
            brain.setMemoryWithExpiry(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(blockPos), memoryDuration);
            brain.setMemoryWithExpiry(memoryType, blockPos, memoryDuration);
            brain.eraseMemory(MemoryModuleType.WALK_TARGET);
        } else if (blockPos == null) {
            brain.eraseMemory(memoryType);
            brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
            brain.eraseMemory(MemoryModuleType.WALK_TARGET);
        }
    }
}