package com.kaboomroads.lostfeatures.block;

import com.kaboomroads.lostfeatures.mixin.BlockSetTypeInvoker;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class ModBlockSetTypes {
    public static final BlockSetType BAOBAB = BlockSetTypeInvoker.register(new BlockSetType("baobab"));
}
