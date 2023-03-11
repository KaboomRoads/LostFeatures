package com.kaboomroads.lostfeatures.platform.services;

import com.kaboomroads.lostfeatures.block.entity.custom.ModHangingSignBlockEntity;
import com.kaboomroads.lostfeatures.block.entity.custom.ModSignBlockEntity;
import com.kaboomroads.lostfeatures.block.entity.custom.SculkJawBlockEntity;
import com.kaboomroads.lostfeatures.block.entity.custom.TermiteNestCoreBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public interface BlockEntityHelper {
    Supplier<BlockEntityType<SculkJawBlockEntity>> sculkJaw();

    Supplier<BlockEntityType<TermiteNestCoreBlockEntity>> termiteNestCore();

    Supplier<BlockEntityType<ModSignBlockEntity>> signs();

    Supplier<BlockEntityType<ModHangingSignBlockEntity>> hangingSigns();
}