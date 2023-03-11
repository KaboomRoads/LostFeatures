package com.kaboomroads.lostfeatures.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DoorBlock.class)
public interface DoorBlockInvoker {
    @Invoker("<init>")
    static DoorBlock invokeInit(BlockBehaviour.Properties properties, SoundEvent closeSound, SoundEvent openSound) {
        throw new IllegalStateException();
    }
}