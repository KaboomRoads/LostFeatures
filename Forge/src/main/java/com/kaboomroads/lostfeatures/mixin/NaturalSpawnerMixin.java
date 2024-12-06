package com.kaboomroads.lostfeatures.mixin;

import com.kaboomroads.lostfeatures.entity.ModEntityTypes;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(NaturalSpawner.class)
public abstract class NaturalSpawnerMixin {
    @ModifyExpressionValue(method = "mobsAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/StructureSpawnOverride;spawns()Lnet/minecraft/util/random/WeightedRandomList;", ordinal = 0))
    private static WeightedRandomList<MobSpawnSettings.SpawnerData> modify(WeightedRandomList<MobSpawnSettings.SpawnerData> original) {
        List<MobSpawnSettings.SpawnerData> list = original.unwrap();
        ArrayList<MobSpawnSettings.SpawnerData> spawns = new ArrayList<>(list.size() + 1);
        spawns.addAll(list);
        spawns.add(new MobSpawnSettings.SpawnerData(ModEntityTypes.WILDFIRE.get(), 5, 1, 1));
        return WeightedRandomList.create(spawns);
    }
}
