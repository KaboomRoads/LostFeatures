package com.kaboomroads.lostfeatures.damagesource;

import com.kaboomroads.lostfeatures.Constants;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public class ModDamageSource extends DamageSource {
    public static final DamageSource SCULK_ATTACK = create("sculk_attack").bypassEnchantments();

    public static DamageSource iceChunk(Entity entity, @Nullable LivingEntity source) {
        return new IndirectEntityDamageSource(Constants.MOD_ID + ".ice_chunk", entity, source);
    }

    public static ModDamageSource create(String name) {
        return new ModDamageSource(Constants.MOD_ID + "." + name);
    }


    public ModDamageSource(String string) {
        super(string);
    }
}