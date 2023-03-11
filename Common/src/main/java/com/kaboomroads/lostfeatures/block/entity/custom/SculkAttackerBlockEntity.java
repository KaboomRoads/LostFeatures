package com.kaboomroads.lostfeatures.block.entity.custom;

import com.kaboomroads.lostfeatures.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class SculkAttackerBlockEntity extends BlockEntity implements SculkAttacker {
    public int animationState = 0;

    public SculkAttackerBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
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

    static void incrementAnimation(SculkAttackerBlockEntity sculkAttacker, int amount) {
        sculkAttacker.setAnimationState(Utils.loopClamp(sculkAttacker.getAnimationState() + amount, 0, sculkAttacker.getAnimationLength()));
    }

    public abstract int getAnimationLength();

    public abstract int[] getAnimationDamageTicks();

    public int getAnimationState() {
        return animationState;
    }

    public void setAnimationState(int animationState) {
        this.animationState = animationState;
    }

    public abstract void updateState();
}
