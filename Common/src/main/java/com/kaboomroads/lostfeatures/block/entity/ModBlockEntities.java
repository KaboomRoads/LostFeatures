package com.kaboomroads.lostfeatures.block.entity;

import com.kaboomroads.lostfeatures.block.entity.custom.ModHangingSignBlockEntity;
import com.kaboomroads.lostfeatures.block.entity.custom.ModSignBlockEntity;
import com.kaboomroads.lostfeatures.block.entity.custom.SculkJawBlockEntity;
import com.kaboomroads.lostfeatures.block.entity.custom.TermiteNestCoreBlockEntity;
import com.kaboomroads.lostfeatures.platform.Services;
import com.mojang.datafixers.types.Type;
import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final Supplier<BlockEntityType<SculkJawBlockEntity>> SCULK_JAW = Services.BLOCK_ENTITY.sculkJaw();
    public static final Supplier<BlockEntityType<TermiteNestCoreBlockEntity>> TERMITE_NEST_CORE = Services.BLOCK_ENTITY.termiteNestCore();
    public static final Supplier<BlockEntityType<ModSignBlockEntity>> SIGN_BLOCK_ENTITIES = Services.BLOCK_ENTITY.signs();
    public static final Supplier<BlockEntityType<ModHangingSignBlockEntity>> HANGING_SIGN_BLOCK_ENTITIES = Services.BLOCK_ENTITY.hangingSigns();

    public static Type<?> getType(String name) {
        return Util.fetchChoiceType(References.BLOCK_ENTITY, name);
    }

    public static void init() {
    }
}