package com.kaboomroads.lostfeatures.block;

import com.kaboomroads.lostfeatures.mixin.WoodTypeInvoker;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ModWoodTypes {
    public static final WoodType BAOBAB = WoodTypeInvoker.invokeRegister(WoodTypeInvoker.invokeInit("baobab"));

    public static void init() {
    }
}
