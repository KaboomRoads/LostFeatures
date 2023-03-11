package com.kaboomroads.lostfeatures.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PressurePlateBlock.class)
public interface PressurePlateBlockInvoker {
    @Invoker("<init>")
    static PressurePlateBlock invokeInit(PressurePlateBlock.Sensitivity sensitivity, BlockBehaviour.Properties properties, SoundEvent offSound, SoundEvent onSound) {
        throw new IllegalStateException();
    }
}