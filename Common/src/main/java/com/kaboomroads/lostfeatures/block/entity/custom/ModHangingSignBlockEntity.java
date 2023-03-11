package com.kaboomroads.lostfeatures.block.entity.custom;

import com.kaboomroads.lostfeatures.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class ModHangingSignBlockEntity extends HangingSignBlockEntity {
    public ModHangingSignBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(blockPos, blockState);
    }

    @NotNull
    @Override
    public BlockEntityType<?> getType() {
        return ModBlockEntities.HANGING_SIGN_BLOCK_ENTITIES.get();
    }
}
