package com.kaboomroads.lostfeatures.entity.ai.brain;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.kaboomroads.lostfeatures.entity.custom.TuffGolem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.schedule.Activity;

public class TuffGolemAi {
    public static Brain<?> makeBrain(Brain<TuffGolem> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void initCoreActivity(Brain<TuffGolem> brain) {
        brain.addActivity(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink()));
    }

    private static void initIdleActivity(Brain<TuffGolem> brain) {
        brain.addActivity(Activity.IDLE, ImmutableList.of(
                Pair.of(0, GoToWantedItem.create(golem -> true, 1.0F, true, 32)),
                Pair.of(1, SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))),
                Pair.of(2, RandomStroll.stroll(0.75F)),
                Pair.of(3, new RandomLookAround(UniformInt.of(150, 250), 30.0F, 0.0F, 0.0F))
        ));
    }

    public static void updateActivity(TuffGolem tuffGolem) {
        tuffGolem.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.IDLE));
    }
}