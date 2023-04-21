package com.kaboomroads.lostfeatures.mixin;

import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ButtonBlock.class)
public interface ButtonBlockInvoker {
    @Invoker("<init>")
    static ButtonBlock invokeInit(BlockBehaviour.Properties properties, BlockSetType type, int pressTime, boolean arrowPressable) {
        throw new IllegalStateException();
    }
}