package com.kaboomroads.lostfeatures.entity.ai;

import com.kaboomroads.lostfeatures.platform.Services;
import net.minecraft.world.entity.schedule.Activity;

import java.util.function.Supplier;

public class ModActivities {
    public static final Supplier<Activity> BUTTON = Services.REGISTRY.registerActivity("button");

    public static void init() {
    }
}
