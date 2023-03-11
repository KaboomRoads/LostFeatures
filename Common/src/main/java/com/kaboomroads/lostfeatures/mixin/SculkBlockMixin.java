package com.kaboomroads.lostfeatures.mixin;

import com.kaboomroads.lostfeatures.block.ModBlocks;
import com.kaboomroads.lostfeatures.block.custom.SculkJawBlock;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SculkBlock;
import net.minecraft.world.level.block.SculkShriekerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SculkBlock.class)
public abstract class SculkBlockMixin {
    private Pair<BlockState, BlockPos> pair;

    @Redirect(method = "attemptUseCharge", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/SculkBlock;getRandomGrowthState(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;Z)Lnet/minecraft/world/level/block/state/BlockState;"))
    public BlockState redirectGRGS(SculkBlock instance, LevelAccessor $$0, BlockPos $$1, RandomSource $$2, boolean $$3) {
        pair = getRandomGrowthState($$0, $$1, $$2, $$3);
        return pair.getFirst();
    }

    @Redirect(method = "attemptUseCharge", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelAccessor;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    public boolean redirectSB(LevelAccessor instance, BlockPos blockPos, BlockState blockState, int i) {
        return instance.setBlock(pair.getSecond(), blockState, i);
    }

    @Redirect(method = "canPlaceGrowth", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 2))
    private static boolean redirectCPG(BlockState instance, Block block) {
        return instance.is(ModBlocks.SCULK_JAW.get()) && instance.is(block);
    }

    public Pair<BlockState, BlockPos> getRandomGrowthState(LevelAccessor levelAccessor, BlockPos blockPos, RandomSource random, boolean isWorldGen) {
        BlockState blockState;
        switch (random.nextInt(14)) {
            case 0 -> blockState = Blocks.SCULK_SHRIEKER.defaultBlockState().setValue(SculkShriekerBlock.CAN_SUMMON, isWorldGen);
            case 1, 2, 3 -> {
                blockPos = blockPos.below();
                blockState = ModBlocks.SCULK_JAW.get().defaultBlockState().setValue(SculkJawBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(random));
            }
            default -> blockState = Blocks.SCULK_SENSOR.defaultBlockState();
        }
        return new Pair<>(blockState.hasProperty(BlockStateProperties.WATERLOGGED) && !levelAccessor.getFluidState(blockPos).isEmpty() ? blockState.setValue(BlockStateProperties.WATERLOGGED, true) : blockState, blockPos);
    }
}
