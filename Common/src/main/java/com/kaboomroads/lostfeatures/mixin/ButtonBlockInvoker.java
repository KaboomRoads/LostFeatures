package com.kaboomroads.lostfeatures.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ButtonBlock.class)
public interface ButtonBlockInvoker {
    @Invoker("<init>")
    static ButtonBlock invokeInit(BlockBehaviour.Properties properties, int ticksToStayPressed, boolean arrowsCanPress, SoundEvent offSound, SoundEvent onSound) {
        throw new IllegalStateException();
    }
}