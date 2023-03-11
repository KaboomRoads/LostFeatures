package com.kaboomroads.lostfeatures.mixin;

import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SaplingBlock.class)
public interface SaplingBlockInvoker {
    @Invoker("<init>")
    static SaplingBlock invokeInit(AbstractTreeGrower treeGrower, BlockBehaviour.Properties properties) {
        throw new IllegalStateException();
    }
}