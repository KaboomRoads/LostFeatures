package com.kaboomroads.lostfeatures.entity;

import com.kaboomroads.lostfeatures.entity.custom.*;
import com.kaboomroads.lostfeatures.platform.Services;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public class ModEntityTypes {
    public static final Supplier<EntityType<ModBoat>> BOAT =
            Services.REGISTRY.registerEntity("boat", () ->
                    EntityType.Builder.<ModBoat>of(ModBoat::new, MobCategory.MISC)
                            .sized(EntityType.BOAT.getWidth(), EntityType.BOAT.getHeight())
                            .build("boat"));

    public static final Supplier<EntityType<ModChestBoat>> CHEST_BOAT =
            Services.REGISTRY.registerEntity("chest_boat", () ->
                    EntityType.Builder.<ModChestBoat>of(ModChestBoat::new, MobCategory.MISC)
                            .sized(EntityType.CHEST_BOAT.getWidth(), EntityType.CHEST_BOAT.getHeight())
                            .build("chest_boat"));

    public static final Supplier<EntityType<Barnacle>> BARNACLE =
            Services.REGISTRY.registerEntity("barnacle", () ->
                    EntityType.Builder.of(Barnacle::new, MobCategory.MONSTER)
                            .sized(1f, 0.85f)
                            .clientTrackingRange(10)
                            .build("barnacle"));

    public static final Supplier<EntityType<Wildfire>> WILDFIRE =
            Services.REGISTRY.registerEntity("wildfire", () ->
                    EntityType.Builder.of(Wildfire::new, MobCategory.MONSTER)
                            .fireImmune()
                            .sized(0.7F, 1.8F)
                            .clientTrackingRange(8)
                            .build("wildfire"));

    public static final Supplier<EntityType<Moobloom>> MOOBLOOM =
            Services.REGISTRY.registerEntity("moobloom", () ->
                    EntityType.Builder.of(Moobloom::new, MobCategory.CREATURE)
                            .sized(0.9F, 1.4F)
                            .clientTrackingRange(10)
                            .build("moobloom"));

    public static final Supplier<EntityType<Chillager>> CHILLAGER =
            Services.REGISTRY.registerEntity("chillager", () ->
                    EntityType.Builder.of(Chillager::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.95F)
                            .clientTrackingRange(8)
                            .build("chillager"));

    public static final Supplier<EntityType<IceChunk>> ICE_CHUNK =
            Services.REGISTRY.registerEntity("ice_chunk", () ->
                    EntityType.Builder.<IceChunk>of(IceChunk::new, MobCategory.MISC)
                            .sized(1.5F, 1F)
                            .clientTrackingRange(6)
                            .build("ice_chunk"));

    public static final Supplier<EntityType<CopperGolem>> COPPER_GOLEM =
            Services.REGISTRY.registerEntity("copper_golem", () ->
                    EntityType.Builder.of(CopperGolem::new, MobCategory.MISC)
                            .sized(0.6F, 0.9F)
                            .clientTrackingRange(10)
                            .build("copper_golem"));

    public static final Supplier<EntityType<TuffGolem>> TUFF_GOLEM =
            Services.REGISTRY.registerEntity("tuff_golem", () ->
                    EntityType.Builder.of(TuffGolem::new, MobCategory.MISC)
                            .sized(0.6F, 0.9F)
                            .clientTrackingRange(10)
                            .build("tuff_golem"));

    public static final Supplier<EntityType<Ostrich>> OSTRICH =
            Services.REGISTRY.registerEntity("ostrich", () ->
                    EntityType.Builder.of(Ostrich::new, MobCategory.CREATURE)
                            .sized(1.0F, 1.25F)
                            .clientTrackingRange(10)
                            .build("ostrich"));

    public static void init() {
    }
}
