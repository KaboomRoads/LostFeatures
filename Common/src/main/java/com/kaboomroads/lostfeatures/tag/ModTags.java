package com.kaboomroads.lostfeatures.tag;

import com.kaboomroads.lostfeatures.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static void init() {
        ModTags.Blocks.init();
        ModTags.Items.init();
        ModTags.EntityTypes.init();
    }

    public static class Blocks {
        public static final TagKey<Block> CUT_COPPER = tag("cut_copper");
        public static final TagKey<Block> COPPER_BLOCKS = tag("copper_blocks");
        public static final TagKey<Block> WAXED_COPPER = tag("waxed_copper");
        public static final TagKey<Block> FRESH_COPPER = tag("fresh_copper");
        public static final TagKey<Block> EXPOSED_COPPER = tag("exposed_copper");
        public static final TagKey<Block> WEATHERED_COPPER = tag("weathered_copper");
        public static final TagKey<Block> OXIDIZED_COPPER = tag("oxidized_copper");
        public static final TagKey<Block> TERMITE_NEST_CAN_GENERATE = tag("termite_nest_can_generate");
        public static final TagKey<Block> TERMITE_NEST_CAN_NOT_GENERATE = tag("termite_nest_can_not_generate");

        private static TagKey<Block> tag(String name) {
            return TagKey.create(Registries.BLOCK, new ResourceLocation(Constants.MOD_ID, name));
        }

        public static void init() {
        }
    }

    public static class Items {
        public static final TagKey<Item> CUT_COPPER = tag("cut_copper");
        public static final TagKey<Item> COPPER_BLOCKS = tag("copper_blocks");
        public static final TagKey<Item> WAXED_COPPER = tag("waxed_copper");
        public static final TagKey<Item> FRESH_COPPER = tag("fresh_copper");
        public static final TagKey<Item> EXPOSED_COPPER = tag("exposed_copper");
        public static final TagKey<Item> WEATHERED_COPPER = tag("weathered_copper");
        public static final TagKey<Item> OXIDIZED_COPPER = tag("oxidized_copper");

        private static TagKey<Item> tag(String name) {
            return TagKey.create(Registries.ITEM, new ResourceLocation(Constants.MOD_ID, name));
        }

        public static void init() {
        }
    }

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> BARNACLE_ALWAYS_HOSTILES = tag("barnacle_always_hostiles");
  
        private static TagKey<EntityType<?>> tag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Constants.MOD_ID, name));
        }

        public static void init() {
        }
    }
}
