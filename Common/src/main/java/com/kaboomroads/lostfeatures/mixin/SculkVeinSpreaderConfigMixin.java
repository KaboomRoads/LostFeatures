package com.kaboomroads.lostfeatures.mixin;

import com.kaboomroads.lostfeatures.block.ModBlocks;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(remap = false, targets = {"net.minecraft.world.level.block.SculkVeinBlock$SculkVeinSpreaderConfig"})
public abstract class SculkVeinSpreaderConfigMixin {
    @ModifyExpressionValue(remap = true, method = "stateCanBeReplaced", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 1))
    public boolean modifyIs(boolean original, BlockGetter getter, BlockPos pos1, BlockPos pos2, @Local(ordinal = 1) LocalRef<BlockState> stateRef) {
        return original || stateRef.get().is(ModBlocks.SCULK_JAW.get());
    }
}
