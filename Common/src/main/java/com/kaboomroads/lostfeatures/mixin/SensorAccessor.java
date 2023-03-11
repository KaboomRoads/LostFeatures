package com.kaboomroads.lostfeatures.mixin;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.sensing.Sensor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Sensor.class)
public interface SensorAccessor {
    @Accessor("RANDOM")
    static RandomSource getRandom() {
        return null;
    }

    @Mutable
    @Accessor("scanRate")
    void setScanRate(int scanRate);

    @Accessor("timeToTick")
    void setTimeToTick(long timeToTick);
}
