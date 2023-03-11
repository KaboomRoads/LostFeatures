package com.kaboomroads.lostfeatures.platform.services;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.function.Supplier;

public interface RegistryHelper {
    <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block);

    <T extends Block> Supplier<T> registerBlockAndItem(String name, Supplier<T> block);

    <T extends Block> Supplier<Item> registerBlockItem(String name, Supplier<T> block);

    <T extends Block> Supplier<Item> registerBlockItem(String name, BlockItem item);

    <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item);

    Item getSpawnEggItem(Supplier<? extends EntityType<? extends Mob>> entityType, int backgroundColor, int highlightColor, Item.Properties properties);

    <T extends BlockEntityType<?>> Supplier<T> registerBlockEntity(String name, Supplier<T> blockEntity);

    <T extends EntityType<?>> Supplier<T> registerEntity(String name, Supplier<T> entity);

    <T extends Feature<?>> Supplier<T> registerFeature(String name, Supplier<T> feature);

    ResourceKey<ConfiguredFeature<?, ?>> registerConfiguredFeature(String name);

    ResourceKey<PlacedFeature> registerPlacedFeature(String name);

    <T extends ParticleType<?>> Supplier<T> registerParticle(String name, Supplier<T> particle);

    Supplier<Activity> registerActivity(String name);

    <U> Supplier<MemoryModuleType<U>> registerMemoryModuleType(String name, Codec<U> codec);

    <U> Supplier<MemoryModuleType<U>> registerMemoryModuleType(String name);

    <U extends Sensor<?>> Supplier<SensorType<U>> registerSensorType(String name, Supplier<U> sensor);

    Supplier<GameEvent> registerGameEvent(String name);

    Supplier<GameEvent> registerGameEvent(String name, int radius);

    SimpleParticleType getSimpleParticleType(boolean b);

    StairBlock getStairBlock(Supplier<BlockState> blockState, BlockBehaviour.Properties properties);

    <P extends TrunkPlacer> Supplier<TrunkPlacerType<P>> registerTrunkPlacerType(String name, Codec<P> codec);

    <P extends FoliagePlacer> Supplier<FoliagePlacerType<P>> registerFoliagePlacerType(String name, Codec<P> codec);
}