package com.kaboomroads.lostfeatures.mixin;

import com.google.common.collect.ImmutableMap;
import com.kaboomroads.lostfeatures.entity.ModEntityTypes;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.structures.NetherFortressStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mixin(NetherFortressStructure.class)
public abstract class NetherFortressStructureMixin extends Structure {
    protected NetherFortressStructureMixin(StructureSettings $$0) {
        super($$0);
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/Structure;<init>(Lnet/minecraft/world/level/levelgen/structure/Structure$StructureSettings;)V", ordinal = 0))
    private static StructureSettings modify(StructureSettings settings) {
        Map<MobCategory, StructureSpawnOverride> spawnOverrides = settings.spawnOverrides();
        StructureSpawnOverride original = spawnOverrides.get(MobCategory.MONSTER);
        ImmutableMap.Builder<MobCategory, StructureSpawnOverride> builder = ImmutableMap.builder();
        for (Map.Entry<MobCategory, StructureSpawnOverride> entry : spawnOverrides.entrySet()) {
            MobCategory category = entry.getKey();
            if (category == MobCategory.MONSTER) {
                List<MobSpawnSettings.SpawnerData> list = original.spawns().unwrap();
                ArrayList<MobSpawnSettings.SpawnerData> spawns = new ArrayList<>(list.size() + 1);
                spawns.addAll(list);
                spawns.add(new MobSpawnSettings.SpawnerData(ModEntityTypes.WILDFIRE.get(), 5, 1, 1));
                builder.put(MobCategory.MONSTER, new StructureSpawnOverride(original.boundingBox(), WeightedRandomList.create(spawns)));
            } else builder.put(entry);
        }
        return new StructureSettings(settings.biomes(), builder.build(), settings.step(), settings.terrainAdaptation());
    }
}
