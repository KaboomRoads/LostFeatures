package com.kaboomroads.lostfeatures.entity.ai;

import com.kaboomroads.lostfeatures.block.ModBlocks;
import com.kaboomroads.lostfeatures.entity.ai.behaviour.AttackablesSensor;
import com.kaboomroads.lostfeatures.entity.ai.behaviour.InterestingBlockSensor;
import com.kaboomroads.lostfeatures.entity.custom.Ostrich;
import com.kaboomroads.lostfeatures.platform.Services;
import com.kaboomroads.lostfeatures.tag.ModTags;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.sensing.TemptingSensor;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;

import java.util.function.Supplier;

public class ModSensorType {
    public static final Supplier<SensorType<InterestingBlockSensor>> COPPER_BUTTON_SENSOR = Services.REGISTRY.registerSensorType("button_sensor", () -> new InterestingBlockSensor(10, BlockStatePredicate.forBlock(ModBlocks.COPPER_BUTTON.get()), false, 5, 2, 2, 5, 100, ModMemoryModuleType.INTERESTING_BLOCK_LOCATION.get(), null));
    public static final Supplier<SensorType<AttackablesSensor>> BARNACLE_ATTACKABLES_SENSOR = Services.REGISTRY.registerSensorType("barnacle_attackables_sensor", () -> new AttackablesSensor(ModTags.EntityTypes.BARNACLE_ALWAYS_HOSTILES, e -> true, 10));
    public static final Supplier<SensorType<TemptingSensor>> OSTRICH_TEMPTATIONS = Services.REGISTRY.registerSensorType("ostrich_temptations_sensor", () -> new TemptingSensor(Ostrich.FOOD_ITEMS));

    public static void init() {
    }
}
