package com.kaboomroads.lostfeatures.mixin;

import com.kaboomroads.lostfeatures.block.ModBlocks;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Arrays;

@Mixin(BlockColors.class)
public abstract class BlockColorsMixin {
    @Redirect(method = "createDefault", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/color/block/BlockColors;register(Lnet/minecraft/client/color/block/BlockColor;[Lnet/minecraft/world/level/block/Block;)V", ordinal = 4))
    private static void redirectRegister(BlockColors instance, BlockColor color, Block[] blocks) {
        Block[] newBlocks = {
                ModBlocks.BAOBAB_LEAVES.get()
        };
        Block[] combined = Arrays.copyOf(blocks, blocks.length + newBlocks.length);
        System.arraycopy(newBlocks, 0, combined, blocks.length, newBlocks.length);
        instance.register(color, combined);
    }
}
