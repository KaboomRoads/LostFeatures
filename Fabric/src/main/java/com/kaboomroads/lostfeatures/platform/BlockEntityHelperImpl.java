package com.kaboomroads.lostfeatures.platform;

import com.kaboomroads.lostfeatures.block.ModBlocks;
import com.kaboomroads.lostfeatures.block.entity.custom.ModHangingSignBlockEntity;
import com.kaboomroads.lostfeatures.block.entity.custom.ModSignBlockEntity;
import com.kaboomroads.lostfeatures.block.entity.custom.SculkJawBlockEntity;
import com.kaboomroads.lostfeatures.block.entity.custom.TermiteNestCoreBlockEntity;
import com.kaboomroads.lostfeatures.platform.services.BlockEntityHelper;
import com.mojang.datafixers.types.Type;
import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class BlockEntityHelperImpl implements BlockEntityHelper {
    public Supplier<BlockEntityType<SculkJawBlockEntity>> sculkJaw() {
        return Services.REGISTRY.registerBlockEntity("sculk_jaw", () ->
                BlockEntityType.Builder.of(SculkJawBlockEntity::new,
                        ModBlocks.SCULK_JAW.get()).build(null));
    }

    public Supplier<BlockEntityType<TermiteNestCoreBlockEntity>> termiteNestCore() {
        return Services.REGISTRY.registerBlockEntity("termite_nest_core", () ->
                BlockEntityType.Builder.of(TermiteNestCoreBlockEntity::new,
                        ModBlocks.TERMITE_NEST_CORE.get()).build(null));
    }

    public Supplier<BlockEntityType<ModSignBlockEntity>> signs() {
        return Services.REGISTRY.registerBlockEntity("sign_block_entity", () ->
                BlockEntityType.Builder.of(ModSignBlockEntity::new,
                        ModBlocks.BAOBAB_WALL_SIGN.get(),
                        ModBlocks.BAOBAB_SIGN.get()
                ).build(null));
    }

    public Supplier<BlockEntityType<ModHangingSignBlockEntity>> hangingSigns() {
        return Services.REGISTRY.registerBlockEntity("hanging_sign_block_entity", () ->
                BlockEntityType.Builder.of(ModHangingSignBlockEntity::new,
                        ModBlocks.BAOBAB_WALL_HANGING_SIGN.get(),
                        ModBlocks.BAOBAB_HANGING_SIGN.get()
                ).build(null));
    }

    public static Type<?> getType(String name) {
        return Util.fetchChoiceType(References.BLOCK_ENTITY, name);
    }
}