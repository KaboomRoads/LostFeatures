package com.kaboomroads.lostfeatures.block.custom;

import com.kaboomroads.lostfeatures.block.ModBlocks;
import com.kaboomroads.lostfeatures.block.entity.custom.TermiteNestCoreBlockEntity;
import com.kaboomroads.lostfeatures.tag.ModTags;
import com.kaboomroads.lostfeatures.worldgen.ModConfiguredFeatures;
import com.kaboomroads.lostfeatures.worldgen.configuration.TermiteNestConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TermiteNestCoreBlock extends BaseEntityBlock {
    public TermiteNestCoreBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void spawnDestroyParticles(@NotNull Level level, @NotNull Player player, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        super.spawnDestroyParticles(level, player, blockPos, blockState);
        TermiteNestBlock.spawnTermites(level, blockPos, dir -> true, UniformInt.of(7, 10), (pos, vec) -> new Vec3(0, -0.01, 0), Vec3.ZERO, 0.25D);
    }

    @Override
    public void animateTick(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull RandomSource random) {
        super.animateTick(blockState, level, blockPos, random);
        spawnParticles(level, blockPos);
    }

    protected void spawnParticles(@NotNull Level level, @NotNull BlockPos blockPos) {
        TermiteNestBlock.spawnTermites(level, blockPos, dir -> !level.getBlockState(blockPos.relative(dir)).isCollisionShapeFullBlock(level, blockPos), UniformInt.of(1, 2), null, Vec3.ZERO, 0.55D);
    }

    @NotNull
    @Override
    public RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new TermiteNestCoreBlockEntity(blockPos, blockState);
    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState blockState) {
        return true;
    }

    @Override
    public void randomTick(@NotNull BlockState blockState, @NotNull ServerLevel level, @NotNull BlockPos blockPos, @NotNull RandomSource random) {
        super.randomTick(blockState, level, blockPos, random);
        if (level.getBlockEntity(blockPos) instanceof TermiteNestCoreBlockEntity blockEntity) {
            if (blockEntity.largerNest == null) {
                Registry<ConfiguredFeature<?, ?>> registry = level.getServer().registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE);
                ResourceKey<ConfiguredFeature<?, ?>> featureKey = ModConfiguredFeatures.LARGE_TERMITE_NEST;
                Optional<Holder.Reference<ConfiguredFeature<?, ?>>> featureOptional = registry.getHolder(featureKey);
                if (featureOptional.isPresent()) {
                    ConfiguredFeature<?, ?> feature = featureOptional.get().value();
                    TermiteNestConfiguration config = (TermiteNestConfiguration) feature.config();
                    int xSize = config.xSize().sample(random);
                    int zSize = config.zSize().sample(random);
                    float spireChance = config.spireChance().sample(random);
                    int maxSpireCount = config.maxSpireCount().sample(random);
                    blockEntity.largerNest = generateNest(blockPos, xSize, zSize, config.height(), config.depth(), config.stateProvider(), config.spireProvider(), spireChance, maxSpireCount, config.lastResortSpire(), config.core(), config.coreProvider(), blockEntity.types, random);
                }
            }
            if (blockEntity.largerNest != null) {
                HashMap<BlockPos, BlockState> nest = new HashMap<>();
                for (Map.Entry<BlockPos, BlockState> entry : blockEntity.largerNest.entrySet()) {
                    BlockState state = level.getBlockState(entry.getKey());
                    if (state.canSurvive(level, entry.getKey()) && !(state.is(ModTags.Blocks.TERMITE_NEST_CAN_NOT_GENERATE) || (blockEntity.types.contains(state) && !state.is(ModBlocks.TERMITE_SPIRES.get()))) && (state.isAir() || state.canBeReplaced() || state.is(ModTags.Blocks.TERMITE_NEST_CAN_GENERATE) || state.is(ModBlocks.TERMITE_SPIRES.get())))
                        for (Direction direction : Direction.values()) {
                            BlockState relativeState = level.getBlockState(entry.getKey().relative(direction));
                            if (!relativeState.is(ModTags.Blocks.TERMITE_NEST_CAN_NOT_GENERATE) && !relativeState.isAir() && !relativeState.canBeReplaced() && !relativeState.is(ModBlocks.TERMITE_SPIRES.get())) {
                                nest.put(entry.getKey(), entry.getValue());
                                break;
                            }
                        }
                }
                if (!nest.isEmpty()) {
                    Object[] entryArray = nest.entrySet().toArray();
                    @SuppressWarnings("unchecked")
                    Map.Entry<BlockPos, BlockState> entry = (Map.Entry<BlockPos, BlockState>) entryArray[random.nextInt(entryArray.length)];
                    level.setBlock(entry.getKey(), entry.getValue(), 3);
                    blockEntity.largerNest.remove(entry.getKey());
                    if (nest.size() <= 1)
                        level.setBlock(blockPos, ModBlocks.TERMITE_NEST.get().defaultBlockState().setValue(TermiteNestBlock.TERMITES, true), 3);
                }
            }
        }
    }

    public static HashMap<BlockPos, BlockState> generateNest(BlockPos center, int xSize, int zSize, IntProvider height, IntProvider depth, BlockStateProvider stateProvider, BlockStateProvider spireProvider, float spireChance, int maxSpireCount, boolean lastResortSpire, boolean core, BlockStateProvider coreProvider, List<BlockState> types, RandomSource random) {
        HashMap<BlockPos, BlockState> map = new HashMap<>();
        int deltaX = Mth.floor(xSize * 0.75f);
        int deltaZ = Mth.floor(zSize * 0.25f);
        BlockPos.MutableBlockPos cursor = center.mutable();
        int spires = 0;
        boolean b = false;
        xSize *= 2;
        for (int x = 0; x < xSize; x++) {
            for (int z = 0; z < zSize; z++) {
                int ySize = height.sample(random);
                int yDepth = depth.sample(random);
                BlockPos.MutableBlockPos pos = cursor.offset(z - deltaX, -yDepth, z - deltaZ).mutable();
                for (int y = 0; y < ySize; y++) {
                    BlockState state = core && pos.equals(center) ? coreProvider.getState(random, pos) : stateProvider.getState(random, pos);
                    map.put(pos.immutable(), state);
                    if (!types.contains(state)) types.add(state);
                    pos.move(0, 1, 0);
                    if (spires < maxSpireCount)
                        if (y >= ySize - 1 && random.nextFloat() <= spireChance) {
                            ++spires;
                            BlockState spireState = spireProvider.getState(random, pos);
                            map.put(pos.immutable(), spireState);
                            if (!types.contains(spireState)) types.add(spireState);
                        } else if (lastResortSpire && spires <= 0 && x >= xSize - 1) {
                            BlockState spireState = spireProvider.getState(random, pos);
                            map.put(pos.immutable(), spireState);
                            if (!types.contains(spireState)) types.add(spireState);
                        }
                }
            }
            cursor.move(b ? 0 : 1, 0, b ? -1 : 0);
            b = !b;
        }
        return map;
    }
}
