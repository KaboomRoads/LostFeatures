package com.kaboomroads.lostfeatures.damagesource;

import com.kaboomroads.lostfeatures.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageTypes {
    public static final ResourceKey<DamageType> SCULK_ATTACK = create("sculk_attack");
    public static final ResourceKey<DamageType> ICE_CHUNK = create("ice_chunk");

    public static ResourceKey<DamageType> create(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Constants.MOD_ID, name));
    }
}