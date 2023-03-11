package com.kaboomroads.lostfeatures.entity.ai.brain;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.kaboomroads.lostfeatures.entity.ai.ModActivities;
import com.kaboomroads.lostfeatures.entity.ai.ModMemoryModuleType;
import com.kaboomroads.lostfeatures.entity.ai.behaviour.PressButtons;
import com.kaboomroads.lostfeatures.entity.custom.CopperGolem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.schedule.Activity;

public class CopperGolemAi {
    private static final float SPEED_MULTIPLIER_WHEN_BUTTON = 1.0F;

    public static Brain<?> makeBrain(Brain<CopperGolem> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initButtonActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void initCoreActivity(Brain<CopperGolem> brain) {
        brain.addActivity(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink()));
    }

    private static void initIdleActivity(Brain<CopperGolem> brain) {
        brain.addActivity(Activity.IDLE, ImmutableList.of(
                Pair.of(0, SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))),
                Pair.of(1, RandomStroll.stroll(0.6F)),
                Pair.of(2, new RandomLookAround(UniformInt.of(150, 250), 30.0F, 0.0F, 0.0F))));
    }

    private static void initButtonActivity(Brain<CopperGolem> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(ModActivities.BUTTON.get(), 5, ImmutableList.of(PressButtons.create(ModMemoryModuleType.INTERESTING_BLOCK_LOCATION.get(), 1.0F, 0, SPEED_MULTIPLIER_WHEN_BUTTON)), ModMemoryModuleType.INTERESTING_BLOCK_LOCATION.get());
    }

    public static void updateActivity(CopperGolem copperGolem) {
        copperGolem.getBrain().setActiveActivityToFirstValid(ImmutableList.of(ModActivities.BUTTON.get(), Activity.IDLE));
    }
}