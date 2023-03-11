package com.kaboomroads.lostfeatures.item;

import com.kaboomroads.lostfeatures.block.ModBlocks;
import com.kaboomroads.lostfeatures.entity.ModEntityTypes;
import com.kaboomroads.lostfeatures.entity.custom.ModBoat;
import com.kaboomroads.lostfeatures.item.custom.ModBoatItem;
import com.kaboomroads.lostfeatures.platform.Services;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SignItem;

import java.util.function.Supplier;

public class ModItems {
    public static final Supplier<Item> BARNACLE_SPAWN_EGG = Services.REGISTRY.registerItem("barnacle_spawn_egg", () ->
            Services.REGISTRY.getSpawnEggItem(ModEntityTypes.BARNACLE, 0x526b63, 0x332b32, new Item.Properties()));
    public static final Supplier<Item> WILDFIRE_SPAWN_EGG = Services.REGISTRY.registerItem("wildfire_spawn_egg", () ->
            Services.REGISTRY.getSpawnEggItem(ModEntityTypes.WILDFIRE, 0xf6b201, 0x562115, new Item.Properties()));
    public static final Supplier<Item> MOOBLOOM_SPAWN_EGG = Services.REGISTRY.registerItem("moobloom_spawn_egg", () ->
            Services.REGISTRY.getSpawnEggItem(ModEntityTypes.MOOBLOOM, 0xffec4f, 0xffffff, new Item.Properties()));
    public static final Supplier<Item> CHILLAGER_SPAWN_EGG = Services.REGISTRY.registerItem("chillager_spawn_egg", () ->
            Services.REGISTRY.getSpawnEggItem(ModEntityTypes.CHILLAGER, 0x173873, 0xb8c6c9, new Item.Properties()));
    public static final Supplier<Item> COPPER_GOLEM_SPAWN_EGG = Services.REGISTRY.registerItem("copper_golem_spawn_egg", () ->
            Services.REGISTRY.getSpawnEggItem(ModEntityTypes.COPPER_GOLEM, 0xc26b4c, 0xe3826c, new Item.Properties()));
    public static final Supplier<Item> TUFF_GOLEM_SPAWN_EGG = Services.REGISTRY.registerItem("tuff_golem_spawn_egg", () ->
            Services.REGISTRY.getSpawnEggItem(ModEntityTypes.TUFF_GOLEM, 0x6a6e6f, 0xa0a297, new Item.Properties()));
    public static final Supplier<Item> OSTRICH_SPAWN_EGG = Services.REGISTRY.registerItem("ostrich_spawn_egg", () ->
            Services.REGISTRY.getSpawnEggItem(ModEntityTypes.OSTRICH, 0x272022, 0xefb196, new Item.Properties()));

    public static final Supplier<Item> BAOBAB_SIGN = Services.REGISTRY.registerItem("baobab_sign", () ->
            new SignItem(new Item.Properties().stacksTo(16), ModBlocks.BAOBAB_SIGN.get(), ModBlocks.BAOBAB_WALL_SIGN.get()));
    public static final Supplier<Item> BAOBAB_HANGING_SIGN = Services.REGISTRY.registerItem("baobab_hanging_sign", () ->
            new HangingSignItem(ModBlocks.BAOBAB_HANGING_SIGN.get(), ModBlocks.BAOBAB_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
    public static final Supplier<Item> BAOBAB_BOAT = Services.REGISTRY.registerItem("baobab_boat", () ->
            new ModBoatItem(false, ModBoat.Type.BAOBAB, new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> BAOBAB_CHEST_BOAT = Services.REGISTRY.registerItem("baobab_chest_boat", () ->
            new ModBoatItem(true, ModBoat.Type.BAOBAB, new Item.Properties().stacksTo(1)));

    public static void init() {
    }
}