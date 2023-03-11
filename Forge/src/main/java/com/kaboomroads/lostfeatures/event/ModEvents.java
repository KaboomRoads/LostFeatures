package com.kaboomroads.lostfeatures.event;

import com.kaboomroads.lostfeatures.Constants;
import com.kaboomroads.lostfeatures.entity.ModEntityTypes;
import com.kaboomroads.lostfeatures.entity.custom.*;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {
    @SubscribeEvent
    public static void onSetAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.BARNACLE.get(), Barnacle.createAttributes().build());
        event.put(ModEntityTypes.WILDFIRE.get(), Wildfire.createAttributes().build());
        event.put(ModEntityTypes.MOOBLOOM.get(), Moobloom.createAttributes().build());
        event.put(ModEntityTypes.CHILLAGER.get(), Chillager.createAttributes().build());
        event.put(ModEntityTypes.COPPER_GOLEM.get(), CopperGolem.createAttributes().build());
        event.put(ModEntityTypes.TUFF_GOLEM.get(), TuffGolem.createAttributes().build());
        event.put(ModEntityTypes.OSTRICH.get(), Ostrich.createAttributes().build());
    }

    @SubscribeEvent
    public static void onSpawnPlacementRegister(SpawnPlacementRegisterEvent event) {
        event.register(
                ModEntityTypes.BARNACLE.get(),
                SpawnPlacements.Type.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                WaterAnimal::checkSurfaceWaterAnimalSpawnRules,
                SpawnPlacementRegisterEvent.Operation.OR
        );
        event.register(
                ModEntityTypes.WILDFIRE.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkAnyLightMonsterSpawnRules,
                SpawnPlacementRegisterEvent.Operation.OR
        );
        event.register(
                ModEntityTypes.MOOBLOOM.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                SpawnPlacementRegisterEvent.Operation.OR
        );
        event.register(
                ModEntityTypes.OSTRICH.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                SpawnPlacementRegisterEvent.Operation.OR
        );
    }
}