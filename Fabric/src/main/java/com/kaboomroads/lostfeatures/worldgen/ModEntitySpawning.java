package com.kaboomroads.lostfeatures.worldgen;

import com.kaboomroads.lostfeatures.entity.ModEntityTypes;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;

public class ModEntitySpawning {
    public static void init() {
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                        Biomes.DEEP_OCEAN,
                        Biomes.DEEP_LUKEWARM_OCEAN,
                        Biomes.DEEP_COLD_OCEAN,
                        Biomes.DEEP_FROZEN_OCEAN),
                MobCategory.WATER_CREATURE,
                ModEntityTypes.BARNACLE.get(),
                2, 1, 2
        );
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                        Biomes.PLAINS,
                        Biomes.SAVANNA,
                        Biomes.SAVANNA_PLATEAU,
                        Biomes.MEADOW,
                        Biomes.FLOWER_FOREST,
                        Biomes.BIRCH_FOREST,
                        Biomes.SUNFLOWER_PLAINS,
                        Biomes.FOREST),
                MobCategory.CREATURE,
                ModEntityTypes.MOOBLOOM.get(),
                5, 2, 4
        );
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                        Biomes.SAVANNA,
                        Biomes.SAVANNA_PLATEAU
                ),
                MobCategory.CREATURE,
                ModEntityTypes.OSTRICH.get(),
                8, 2, 4
        );
        SpawnPlacements.register(
                ModEntityTypes.BARNACLE.get(),
                SpawnPlacements.Type.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                WaterAnimal::checkSurfaceWaterAnimalSpawnRules
        );
        SpawnPlacements.register(
                ModEntityTypes.WILDFIRE.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkAnyLightMonsterSpawnRules
        );
        SpawnPlacements.register(
                ModEntityTypes.MOOBLOOM.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules
        );
        SpawnPlacements.register(
                ModEntityTypes.OSTRICH.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules
        );
    }
}
