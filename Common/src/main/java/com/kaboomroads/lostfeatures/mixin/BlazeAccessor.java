package com.kaboomroads.lostfeatures.mixin;

import net.minecraft.world.entity.monster.Blaze;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Blaze.class)
public interface BlazeAccessor {
    @Invoker
    void invokeSetCharged(boolean charged);
}
