package com.kaboomroads.lostfeatures.block.entity.custom;

import com.kaboomroads.lostfeatures.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.IntStream;

public abstract class SculkBiterBlockEntity extends SculkAttackerBlockEntity {
    public SculkBiterBlockEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        super.load(compoundTag);
        updateState();
    }

    public static void tickAnimation(Level level, BlockPos blockPos, BlockState blockState, SculkBiterBlockEntity blockEntity) {
        if (level.isClientSide) return;
        List<LivingEntity> attackEntities = SculkAttacker.getAttackEntities(level, blockEntity);
        boolean entities = !attackEntities.isEmpty();
        blockEntity.tickStart(level, blockPos, blockState, attackEntities);
        boolean increment = false;
        if (!entities && blockEntity.getAnimationState() != 0)
            increment(blockEntity, 1);
        else if (entities) {
            for (LivingEntity livingEntity : attackEntities)
                if (Utils.entityIsDamageable(livingEntity) && SculkAttacker.testAttackable(livingEntity)) {
                    increment = true;
                    if (IntStream.of(blockEntity.getAnimationDamageTicks()).anyMatch(i -> i == blockEntity.getAnimationState())) {
                        blockEntity.tickOnFound(level, blockPos, blockState, livingEntity);
                        SculkAttacker.sculkDamage(livingEntity);
                    }
                }
            if (increment || (blockEntity.getAnimationState() != 0)) increment(blockEntity, 1);
        }
        blockEntity.tickEnd(level, blockPos, blockState, increment);
    }

    public static void increment(SculkAttackerBlockEntity blockEntity, int amount) {
        SculkAttackerBlockEntity.incrementAnimation(blockEntity, amount);
        blockEntity.updateState();
    }

    public void tickStart(Level level, BlockPos blockPos, BlockState blockState, List<? extends Entity> entities) {
    }

    public void tickOnFound(Level level, BlockPos blockPos, BlockState blockState, LivingEntity entity) {
    }

    public void tickEnd(Level level, BlockPos blockPos, BlockState blockState, boolean increment) {
    }

    @Override
    public double getLevelX() {
        return (double) this.worldPosition.getX() + 0.5D;
    }

    @Override
    public double getLevelY() {
        return (double) this.worldPosition.getY() + 0.5D;
    }

    @Override
    public double getLevelZ() {
        return (double) this.worldPosition.getZ() + 0.5D;
    }
}
