package com.kaboomroads.lostfeatures.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TrapDoorBlock.class)
public interface TrapDoorBlockInvoker {
    @Invoker("<init>")
    static TrapDoorBlock invokeInit(BlockBehaviour.Properties properties, SoundEvent closeSound, SoundEvent openSound) {
        throw new IllegalStateException();
    }
}