package com.kaboomroads.lostfeatures;

import com.kaboomroads.lostfeatures.block.ModBlocks;
import com.kaboomroads.lostfeatures.entity.ModEntityTypes;
import com.kaboomroads.lostfeatures.entity.custom.*;
import com.kaboomroads.lostfeatures.platform.RegistryHelperImpl;
import com.kaboomroads.lostfeatures.platform.Services;
import com.kaboomroads.lostfeatures.worldgen.ModEntitySpawning;
import com.kaboomroads.lostfeatures.worldgen.ModPlacedFeatures;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;

public class LostFeatures implements ModInitializer {
    @Override
    public void onInitialize() {
        Services.init();
        RegistryHelperImpl.init();
        FabricDefaultAttributeRegistry.register(ModEntityTypes.BARNACLE.get(), Barnacle.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.WILDFIRE.get(), Wildfire.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.MOOBLOOM.get(), Moobloom.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.CHILLAGER.get(), Chillager.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.COPPER_GOLEM.get(), CopperGolem.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.TUFF_GOLEM.get(), TuffGolem.createAttributes());
        FabricDefaultAttributeRegistry.register(ModEntityTypes.OSTRICH.get(), Ostrich.createAttributes());
        biomeModifications();
        ModEntitySpawning.init();
        flammables();
    }

    public static void biomeModifications() {
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.BAOBAB_TREE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.THICK_BAOBAB_TREE);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.TERMITE_NEST);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.TERMITE_CORE_NEST);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.BADLANDS, Biomes.ERODED_BADLANDS, Biomes.WOODED_BADLANDS),
                GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.BADLANDS_CACTUS);
    }

    public static void flammables() {
        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.BAOBAB_LOG.get(), 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.BAOBAB_WOOD.get(), 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.STRIPPED_BAOBAB_LOG.get(), 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.STRIPPED_BAOBAB_WOOD.get(), 5, 5);
        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.BAOBAB_PLANKS.get(), 5, 20);
        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.BAOBAB_SLAB.get(), 5, 20);
        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.BAOBAB_FENCE_GATE.get(), 5, 20);
        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.BAOBAB_FENCE.get(), 5, 20);
        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.BAOBAB_STAIRS.get(), 5, 20);
        FlammableBlockRegistry.getDefaultInstance().add(ModBlocks.BAOBAB_LEAVES.get(), 30, 60);
    }
}
