package com.kaboomroads.lostfeatures.utils;

import com.kaboomroads.lostfeatures.entity.ModEntityTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.raid.Raider;

public enum ModRaiderType {
    CHILLAGER(ModEntityTypes.CHILLAGER.get(), new int[]{0, 0, 0, 0, 0, 1, 1, 2});

    public static final ModRaiderType[] VALUES = values();
    public final EntityType<? extends Raider> entityType;
    public final int[] spawnsPerWaveBeforeBonus;

    ModRaiderType(EntityType<? extends Raider> p_37821_, int[] p_37822_) {
        this.entityType = p_37821_;
        this.spawnsPerWaveBeforeBonus = p_37822_;
    }
}
