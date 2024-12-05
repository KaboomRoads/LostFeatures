package com.kaboomroads.lostfeatures.mixin;

import com.kaboomroads.lostfeatures.entity.ModEntityTypes;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.structure.structures.NetherFortressStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(NetherFortressStructure.class)
public abstract class NetherFortressStructureMixin {
    @ModifyExpressionValue(method = "<clinit>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/random/WeightedRandomList;create([Lnet/minecraft/util/random/WeightedEntry;)Lnet/minecraft/util/random/WeightedRandomList;", ordinal = 0))
    private static WeightedRandomList<MobSpawnSettings.SpawnerData> modify(WeightedRandomList<MobSpawnSettings.SpawnerData> original) {
        List<MobSpawnSettings.SpawnerData> list = original.unwrap();
        ArrayList<MobSpawnSettings.SpawnerData> spawns = new ArrayList<>(list.size() + 1);
        spawns.addAll(list);
        spawns.add(new MobSpawnSettings.SpawnerData(ModEntityTypes.WILDFIRE.get(), 5, 1, 1));
        return WeightedRandomList.create(spawns);
    }
}
