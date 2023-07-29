package com.kaboomroads.lostfeatures.mixin;

import com.kaboomroads.lostfeatures.block.ModBlocks;
import com.kaboomroads.lostfeatures.block.custom.SculkJawBlock;
import com.kaboomroads.lostfeatures.utils.SculkPlacement;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SculkBlock;
import net.minecraft.world.level.block.SculkShriekerBlock;
import net.minecraft.world.level.block.SculkSpreader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SculkBlock.class)
public abstract class SculkBlockMixin {
    @Inject(method = "attemptUseCharge", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/level/block/SculkBlock;getRandomGrowthState(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;Z)Lnet/minecraft/world/level/block/state/BlockState;"))
    public void setNewGrowthState(SculkSpreader.ChargeCursor cursor, LevelAccessor level, BlockPos pos, RandomSource random, SculkSpreader spreader, boolean isWorldGen, CallbackInfoReturnable<Integer> cir, @Share("placement") LocalRef<SculkPlacement> placementRef, @Local(ordinal = 2) LocalRef<BlockPos> posRef, @Local(ordinal = 0) LocalRef<BlockState> stateRef) {
        SculkPlacement placement = lostFeatures$getRandomGrowthState(level, posRef.get(), random, isWorldGen);
        placementRef.set(placement);
        stateRef.set(placement.state());
        posRef.set(placement.pos());
    }

    @ModifyExpressionValue(method = "canPlaceGrowth", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 2))
    private static boolean modifyCanPlaceGrowth(boolean original, @Local(ordinal = 1) BlockState state) {
        return original || state.is(ModBlocks.SCULK_JAW.get());
    }

    @Unique
    public SculkPlacement lostFeatures$getRandomGrowthState(LevelAccessor levelAccessor, BlockPos blockPos, RandomSource random, boolean isWorldGen) {
        BlockState blockState;
        switch (random.nextInt(14)) {
            case 0 -> blockState = Blocks.SCULK_SHRIEKER.defaultBlockState().setValue(SculkShriekerBlock.CAN_SUMMON, isWorldGen);
            case 1, 2, 3 -> {
                blockPos = blockPos.below();
                blockState = ModBlocks.SCULK_JAW.get().defaultBlockState().setValue(SculkJawBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random));
            }
            default -> blockState = Blocks.SCULK_SENSOR.defaultBlockState();
        }
        return new SculkPlacement(blockPos, blockState.hasProperty(BlockStateProperties.WATERLOGGED) && !levelAccessor.getFluidState(blockPos).isEmpty() ? blockState.setValue(BlockStateProperties.WATERLOGGED, true) : blockState);
    }
}
