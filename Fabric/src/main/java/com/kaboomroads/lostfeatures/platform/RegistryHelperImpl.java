package com.kaboomroads.lostfeatures.platform;

import com.kaboomroads.lostfeatures.Constants;
import com.kaboomroads.lostfeatures.block.ModBlocks;
import com.kaboomroads.lostfeatures.block.entity.ModBlockEntities;
import com.kaboomroads.lostfeatures.entity.ModEntityTypes;
import com.kaboomroads.lostfeatures.entity.ai.ModActivities;
import com.kaboomroads.lostfeatures.entity.ai.ModMemoryModuleType;
import com.kaboomroads.lostfeatures.entity.ai.ModSensorType;
import com.kaboomroads.lostfeatures.gameevent.ModGameEvent;
import com.kaboomroads.lostfeatures.item.ModItems;
import com.kaboomroads.lostfeatures.mixin.FoliagePlacerTypeInvoker;
import com.kaboomroads.lostfeatures.mixin.TrunkPlacerTypeInvoker;
import com.kaboomroads.lostfeatures.particle.ModParticles;
import com.kaboomroads.lostfeatures.platform.services.RegistryHelper;
import com.kaboomroads.lostfeatures.tag.ModTags;
import com.kaboomroads.lostfeatures.worldgen.*;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
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

import java.util.Optional;
import java.util.function.Supplier;

public class RegistryHelperImpl implements RegistryHelper {
    public static void init() {
        ModBlocks.init();
        ModItems.init();
        ModEntityTypes.init();
        ModBlockEntities.init();
        ModActivities.init();
        ModMemoryModuleType.init();
        ModSensorType.init();
        ModGameEvent.init();
        ModTrunkPlacerTypes.init();
        ModFoliagePlacerTypes.init();
        ModFeatures.init();
        ModConfiguredFeatures.init();
        ModPlacedFeatures.init();
        ModParticles.init();
        ModTags.init();
    }

    @Override
    public <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        T registry = Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Constants.MOD_ID, name), block.get());
        return () -> registry;
    }

    @Override
    public <T extends Block> Supplier<T> registerBlockAndItem(String name, Supplier<T> block) {
        Supplier<T> toReturn = registerBlock(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    @Override
    public <T extends Block> Supplier<Item> registerBlockItem(String name, Supplier<T> block) {
        return registerItem(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    @Override
    public Supplier<Item> registerBlockItem(String name, BlockItem item) {
        return registerItem(name, () -> item);
    }

    @Override
    public <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
        T registry = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Constants.MOD_ID, name), item.get());
        return () -> registry;
    }

    @Override
    public Item getSpawnEggItem(Supplier<? extends EntityType<? extends Mob>> entityType, int backgroundColor, int highlightColor, Item.Properties properties) {
        return new SpawnEggItem(entityType.get(), backgroundColor, highlightColor, properties);
    }

    @Override
    public <T extends BlockEntityType<?>> Supplier<T> registerBlockEntity(String name, Supplier<T> blockEntity) {
        T registry = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, new ResourceLocation(Constants.MOD_ID, name), blockEntity.get());
        return () -> registry;
    }

    @Override
    public <T extends EntityType<?>> Supplier<T> registerEntity(String name, Supplier<T> entity) {
        T registry = Registry.register(BuiltInRegistries.ENTITY_TYPE, new ResourceLocation(Constants.MOD_ID, name), entity.get());
        return () -> registry;
    }

    @Override
    public <T extends Feature<?>> Supplier<T> registerFeature(String name, Supplier<T> feature) {
        T registry = Registry.register(BuiltInRegistries.FEATURE, new ResourceLocation(Constants.MOD_ID, name), feature.get());
        return () -> registry;
    }

    @Override
    public ResourceKey<ConfiguredFeature<?, ?>> registerConfiguredFeature(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Constants.MOD_ID, name));
    }

    @Override
    public ResourceKey<PlacedFeature> registerPlacedFeature(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(Constants.MOD_ID, name));
    }

    @Override
    public <T extends ParticleType<?>> Supplier<T> registerParticle(String name, Supplier<T> particle) {
        T registry = Registry.register(BuiltInRegistries.PARTICLE_TYPE, new ResourceLocation(Constants.MOD_ID, name), particle.get());
        return () -> registry;
    }

    @Override
    public Supplier<Activity> registerActivity(String name) {
        Activity registry = Registry.register(BuiltInRegistries.ACTIVITY, new ResourceLocation(Constants.MOD_ID, name), new Activity(name));
        return () -> registry;
    }

    @Override
    public <U> Supplier<MemoryModuleType<U>> registerMemoryModuleType(String name, Codec<U> codec) {
        MemoryModuleType<U> registry = Registry.register(BuiltInRegistries.MEMORY_MODULE_TYPE, new ResourceLocation(Constants.MOD_ID, name), new MemoryModuleType<>(Optional.of(codec)));
        return () -> registry;
    }

    @Override
    public <U> Supplier<MemoryModuleType<U>> registerMemoryModuleType(String name) {
        MemoryModuleType<U> registry = Registry.register(BuiltInRegistries.MEMORY_MODULE_TYPE, new ResourceLocation(Constants.MOD_ID, name), new MemoryModuleType<>(Optional.empty()));
        return () -> registry;
    }

    @Override
    public <U extends Sensor<?>> Supplier<SensorType<U>> registerSensorType(String name, Supplier<U> sensor) {
        SensorType<U> registry = Registry.register(BuiltInRegistries.SENSOR_TYPE, new ResourceLocation(Constants.MOD_ID, name), new SensorType<>(sensor));
        return () -> registry;
    }

    @Override
    public Supplier<GameEvent> registerGameEvent(String name) {
        return registerGameEvent(name, 16);
    }

    @Override
    public Supplier<GameEvent> registerGameEvent(String name, int radius) {
        GameEvent registry = Registry.register(BuiltInRegistries.GAME_EVENT, new ResourceLocation(Constants.MOD_ID, name), new GameEvent(name, radius));
        return () -> registry;
    }

    @Override
    public SimpleParticleType getSimpleParticleType(boolean b) {
        return FabricParticleTypes.simple(b);
    }

    @Override
    public StairBlock getStairBlock(Supplier<BlockState> blockState, BlockBehaviour.Properties properties) {
        return new StairBlock(blockState.get(), properties);
    }

    @Override
    public <P extends TrunkPlacer> Supplier<TrunkPlacerType<P>> registerTrunkPlacerType(String name, Codec<P> codec) {
        TrunkPlacerType<P> registry = Registry.register(BuiltInRegistries.TRUNK_PLACER_TYPE, new ResourceLocation(Constants.MOD_ID, name), TrunkPlacerTypeInvoker.invokeInit(codec));
        return () -> registry;
    }

    @Override
    public <P extends FoliagePlacer> Supplier<FoliagePlacerType<P>> registerFoliagePlacerType(String name, Codec<P> codec) {
        FoliagePlacerType<P> registry = Registry.register(BuiltInRegistries.FOLIAGE_PLACER_TYPE, new ResourceLocation(Constants.MOD_ID, name), FoliagePlacerTypeInvoker.invokeInit(codec));
        return () -> registry;
    }
}