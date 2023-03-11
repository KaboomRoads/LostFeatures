package com.kaboomroads.lostfeatures.gameevent;

import com.kaboomroads.lostfeatures.platform.Services;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.function.Supplier;

public class ModGameEvent {
    public static final Supplier<GameEvent> SCULK_ATTACK = Services.REGISTRY.registerGameEvent("sculk_attack");

    public static void init() {
    }
}
