package com.kaboomroads.lostfeatures.worldgen.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record TermiteNestConfiguration(BlockStateProvider stateProvider,
                                       HolderSet<Block> canGenerateOn,
                                       HolderSet<Block> canNotGenerateOn,
                                       IntProvider xSize,
                                       IntProvider zSize,
                                       IntProvider depth,
                                       IntProvider height,
                                       FloatProvider spireChance,
                                       BlockStateProvider spireProvider,
                                       IntProvider maxSpireCount,
                                       boolean lastResortSpire,
                                       boolean core,
                                       BlockStateProvider coreProvider) implements FeatureConfiguration {
    public static final Codec<TermiteNestConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockStateProvider.CODEC.fieldOf("state_provider").forGetter(config -> config.stateProvider),
            RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("can_generate_on").forGetter(config -> config.canGenerateOn),
            RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("can_not_generate_on").forGetter(config -> config.canNotGenerateOn),
            IntProvider.POSITIVE_CODEC.fieldOf("x_size").forGetter(config -> config.xSize),
            IntProvider.POSITIVE_CODEC.fieldOf("z_size").forGetter(config -> config.zSize),
            IntProvider.NON_NEGATIVE_CODEC.fieldOf("depth").forGetter(config -> config.depth),
            IntProvider.POSITIVE_CODEC.fieldOf("height").forGetter(config -> config.height),
            FloatProvider.codec(0.0F, 1.0F).fieldOf("spire_chance").forGetter(config -> config.spireChance),
            BlockStateProvider.CODEC.fieldOf("spire_provider").forGetter(config -> config.spireProvider),
            IntProvider.NON_NEGATIVE_CODEC.fieldOf("max_spire_count").forGetter(config -> config.maxSpireCount),
            Codec.BOOL.fieldOf("last_resort_spire").forGetter(config -> config.lastResortSpire),
            Codec.BOOL.fieldOf("core").forGetter(config -> config.core),
            BlockStateProvider.CODEC.fieldOf("core_provider").forGetter(config -> config.coreProvider)
    ).apply(instance, TermiteNestConfiguration::new));
}
