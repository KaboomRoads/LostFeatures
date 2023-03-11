package com.kaboomroads.lostfeatures.mixin;

import com.kaboomroads.lostfeatures.block.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(remap = false, targets = {"net.minecraft.world.level.block.SculkVeinBlock$SculkVeinSpreaderConfig"})
public abstract class SculkVeinSpreaderConfigMixin {
    @Redirect(remap = true, method = "stateCanBeReplaced", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 1))
    public boolean is(BlockState instance, Block block) {
        return instance.is(block) || instance.is(ModBlocks.SCULK_JAW.get());
    }
}
