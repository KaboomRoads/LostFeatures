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
import com.kaboomroads.lostfeatures.particle.ModParticles;
import com.kaboomroads.lostfeatures.platform.services.RegistryHelper;
import com.kaboomroads.lostfeatures.tag.ModTags;
import com.kaboomroads.lostfeatures.worldgen.*;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
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
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;
import java.util.function.Supplier;

public class RegistryHelperImpl implements RegistryHelper {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Constants.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Constants.MOD_ID);
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Constants.MOD_ID);
    public static final DeferredRegister<Activity> ACTIVITIES = DeferredRegister.create(Registries.ACTIVITY, Constants.MOD_ID);
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES = DeferredRegister.create(Registries.MEMORY_MODULE_TYPE, Constants.MOD_ID);
    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(Registries.SENSOR_TYPE, Constants.MOD_ID);
    public static final DeferredRegister<GameEvent> GAME_EVENTS = DeferredRegister.create(Registries.GAME_EVENT, Constants.MOD_ID);
    public static final DeferredRegister<TrunkPlacerType<?>> TRUNK_PLACER_TYPES = DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, Constants.MOD_ID);
    public static final DeferredRegister<FoliagePlacerType<?>> FOLIAGE_PLACER_TYPES = DeferredRegister.create(Registries.FOLIAGE_PLACER_TYPE, Constants.MOD_ID);
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, Constants.MOD_ID);
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registries.CONFIGURED_FEATURE, Constants.MOD_ID);
    public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registries.PLACED_FEATURE, Constants.MOD_ID);

    public static void register(IEventBus eventBus) {
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
        ITEMS.register(eventBus);
        BLOCKS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
        ENTITY_TYPES.register(eventBus);
        PARTICLE_TYPES.register(eventBus);
        ACTIVITIES.register(eventBus);
        MEMORY_MODULE_TYPES.register(eventBus);
        SENSOR_TYPES.register(eventBus);
        GAME_EVENTS.register(eventBus);
        TRUNK_PLACER_TYPES.register(eventBus);
        FOLIAGE_PLACER_TYPES.register(eventBus);
        FEATURES.register(eventBus);
        CONFIGURED_FEATURES.register(eventBus);
        PLACED_FEATURES.register(eventBus);
    }

    @Override
    public <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    @Override
    public <T extends Block> Supplier<T> registerBlockAndItem(String name, Supplier<T> block) {
        Supplier<T> toReturn = BLOCKS.register(name, block);
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
        return ITEMS.register(name, item);
    }

    @Override
    public Item getSpawnEggItem(Supplier<? extends EntityType<? extends Mob>> entityType, int backgroundColor, int highlightColor, Item.Properties properties) {
        return new ForgeSpawnEggItem(entityType, backgroundColor, highlightColor, properties);
    }

    @Override
    public <T extends BlockEntityType<?>> Supplier<T> registerBlockEntity(String name, Supplier<T> blockEntity) {
        return BLOCK_ENTITIES.register(name, blockEntity);
    }

    @Override
    public <T extends EntityType<?>> Supplier<T> registerEntity(String name, Supplier<T> entity) {
        return ENTITY_TYPES.register(name, entity);
    }

    @Override
    public <T extends Feature<?>> Supplier<T> registerFeature(String name, Supplier<T> feature) {
        return FEATURES.register(name, feature);
    }

    @Override
    public ResourceKey<ConfiguredFeature<?, ?>> registerConfiguredFeature(String name) {
        return ResourceKey.create(CONFIGURED_FEATURES.getRegistryKey(), new ResourceLocation(Constants.MOD_ID, name));
    }

    @Override
    public ResourceKey<PlacedFeature> registerPlacedFeature(String name) {
        return ResourceKey.create(PLACED_FEATURES.getRegistryKey(), new ResourceLocation(Constants.MOD_ID, name));
    }

    @Override
    public <T extends ParticleType<?>> Supplier<T> registerParticle(String name, Supplier<T> particle) {
        return PARTICLE_TYPES.register(name, particle);
    }

    @Override
    public Supplier<Activity> registerActivity(String name) {
        return ACTIVITIES.register(name, () -> new Activity(name));
    }

    @Override
    public <U> Supplier<MemoryModuleType<U>> registerMemoryModuleType(String name, Codec<U> codec) {
        return MEMORY_MODULE_TYPES.register(name, () -> new MemoryModuleType<>(Optional.of(codec)));
    }

    @Override
    public <U> Supplier<MemoryModuleType<U>> registerMemoryModuleType(String name) {
        return MEMORY_MODULE_TYPES.register(name, () -> new MemoryModuleType<>(Optional.empty()));
    }

    @Override
    public <U extends Sensor<?>> Supplier<SensorType<U>> registerSensorType(String name, Supplier<U> sensor) {
        return SENSOR_TYPES.register(name, () -> new SensorType<>(sensor));
    }

    @Override
    public Supplier<GameEvent> registerGameEvent(String name) {
        return registerGameEvent(name, 16);
    }

    @Override
    public Supplier<GameEvent> registerGameEvent(String name, int radius) {
        return GAME_EVENTS.register(name, () -> new GameEvent(name, radius));
    }

    @Override
    public SimpleParticleType getSimpleParticleType(boolean b) {
        return new SimpleParticleType(b);
    }

    @Override
    public StairBlock getStairBlock(Supplier<BlockState> blockState, BlockBehaviour.Properties properties) {
        return new StairBlock(blockState, properties);
    }

    @Override
    public <P extends TrunkPlacer> Supplier<TrunkPlacerType<P>> registerTrunkPlacerType(String name, Codec<P> codec) {
        return TRUNK_PLACER_TYPES.register(name, () -> new TrunkPlacerType<>(codec));
    }

    @Override
    public <P extends FoliagePlacer> Supplier<FoliagePlacerType<P>> registerFoliagePlacerType(String name, Codec<P> codec) {
        return FOLIAGE_PLACER_TYPES.register(name, () -> new FoliagePlacerType<>(codec));
    }
}