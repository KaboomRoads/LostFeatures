package com.kaboomroads.lostfeatures.entity.ai;

import com.kaboomroads.lostfeatures.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.function.Supplier;

public class ModMemoryModuleType {
    public static final Supplier<MemoryModuleType<BlockPos>> INTERESTING_BLOCK_LOCATION = Services.REGISTRY.registerMemoryModuleType("interesting_block_location");

    public static void init() {
    }
}
