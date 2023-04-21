package com.kaboomroads.lostfeatures.mixin;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(WoodType.class)
public interface WoodTypeInvoker {
    @Invoker("register")
    static WoodType invokeRegister(WoodType woodType) {
        throw new IllegalStateException();
    }

    @Invoker("<init>")
    static WoodType invokeInit(String name, BlockSetType type) {
        throw new IllegalStateException();
    }
}