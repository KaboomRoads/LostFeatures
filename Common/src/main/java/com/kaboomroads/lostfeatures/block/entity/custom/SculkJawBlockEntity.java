package com.kaboomroads.lostfeatures.block.entity.custom;

import com.kaboomroads.lostfeatures.block.custom.SculkJawBlock;
import com.kaboomroads.lostfeatures.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SculkJawBlockEntity extends SculkBiterBlockEntity {
    public SculkJawBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.SCULK_JAW.get(), pos, blockState);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, SculkJawBlockEntity blockEntity) {
        tickAnimation(level, blockPos, blockState, blockEntity);
    }

    public void updateState() {
        if (level == null) return;
        BlockState blockState = getBlockState();
        BlockPos blockPos = getBlockPos();
        switch (getAnimationState()) {
            case 1, 6 -> level.setBlock(blockPos, blockState.setValue(SculkJawBlock.ANIMATION_STATE, 1), 3);
            case 2, 5 -> level.setBlock(blockPos, blockState.setValue(SculkJawBlock.ANIMATION_STATE, 2), 3);
            case 3, 4 -> level.setBlock(blockPos, blockState.setValue(SculkJawBlock.ANIMATION_STATE, 3), 3);
            default -> level.setBlock(blockPos, blockState.setValue(SculkJawBlock.ANIMATION_STATE, 0), 3);
        }
    }

    @Override
    public int getAnimationLength() {
        return 19;
    }

    @Override
    public int[] getAnimationDamageTicks() {
        return new int[]{0};
    }
}
