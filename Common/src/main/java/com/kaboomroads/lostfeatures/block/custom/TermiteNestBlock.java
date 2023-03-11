package com.kaboomroads.lostfeatures.block.custom;

import com.kaboomroads.lostfeatures.particle.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Predicate;

public class TermiteNestBlock extends Block {
    public static final BooleanProperty TERMITES = BooleanProperty.create("termites");

    public TermiteNestBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(TERMITES, false));
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TERMITES);
    }

    @Override
    protected void spawnDestroyParticles(@NotNull Level level, @NotNull Player player, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        super.spawnDestroyParticles(level, player, blockPos, blockState);
        if (blockState.getValue(TERMITES))
            TermiteNestBlock.spawnTermites(level, blockPos, dir -> true, UniformInt.of(7, 10), (pos, vec) -> new Vec3(0, -0.01, 0), Vec3.ZERO, 0.25D);
    }

    @Override
    public void animateTick(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, @NotNull RandomSource random) {
        super.animateTick(blockState, level, blockPos, random);
        if (blockState.getValue(TERMITES))
            spawnParticles(level, blockPos);
    }

    protected void spawnParticles(@NotNull Level level, @NotNull BlockPos blockPos) {
        TermiteNestBlock.spawnTermites(level, blockPos, dir -> !level.getBlockState(blockPos.relative(dir)).isCollisionShapeFullBlock(level, blockPos), UniformInt.of(1, 2), null, Vec3.ZERO, 0.55D);
    }

    public static void spawnTermites(@NotNull Level level, @NotNull BlockPos blockPos, Predicate<Direction> predicate, IntProvider amount, @Nullable BiFunction<Vec3, Vec3, Vec3> vecFunction, Vec3 offset, double faceDistance) {
        BiFunction<Vec3, Vec3, Vec3> function = (pos, vec) -> pos.subtract(vec).scale(0.025F);
        if (vecFunction != null) function = vecFunction;
        for (Direction direction : Direction.values())
            if (predicate.test(direction))
                spawnParticlesOnBlockFace(level, blockPos, ModParticles.TERMITE_PARTICLE.get(), amount, direction, function, offset, faceDistance);
    }

    public static void spawnParticlesOnBlockFace(Level level, BlockPos blockPos, ParticleOptions particle, IntProvider intProvider, Direction direction, BiFunction<Vec3, Vec3, Vec3> vecFunction, Vec3 offset, double faceDistance) {
        int amount = intProvider.sample(level.random);
        if (amount > 0) for (int i = 0; i < amount; ++i)
            spawnParticleOnFace(level, blockPos, direction, particle, vecFunction, offset, faceDistance);
    }

    public static void spawnParticleOnFace(Level level, BlockPos blockPos, Direction direction, ParticleOptions particle, BiFunction<Vec3, Vec3, Vec3> vecFunction, Vec3 offset, double faceDistance) {
        Vec3 pos = Vec3.atCenterOf(blockPos);
        int stepX = direction.getStepX();
        int stepY = direction.getStepY();
        int stepZ = direction.getStepZ();
        double x = pos.x + (stepX == 0 ? Mth.nextDouble(level.random, -0.5D, 0.5D) : (double) stepX * faceDistance);
        double y = pos.y + (stepY == 0 ? Mth.nextDouble(level.random, -0.5D, 0.5D) : (double) stepY * faceDistance);
        double z = pos.z + (stepZ == 0 ? Mth.nextDouble(level.random, -0.5D, 0.5D) : (double) stepZ * faceDistance);
        double x1 = pos.x + (stepX == 0 ? Mth.nextDouble(level.random, -0.5D, 0.5D) : (double) stepX * faceDistance);
        double y1 = pos.y + (stepY == 0 ? Mth.nextDouble(level.random, -0.5D, 0.5D) : (double) stepY * faceDistance);
        double z1 = pos.z + (stepZ == 0 ? Mth.nextDouble(level.random, -0.5D, 0.5D) : (double) stepZ * faceDistance);
        Vec3 vec = vecFunction.apply(new Vec3(x1, y1, z1), new Vec3(x, y, z));
        double dx = stepX == 0 ? vec.x() : 0.0D;
        double dy = stepY == 0 ? vec.y() : 0.0D;
        double dz = stepZ == 0 ? vec.z() : 0.0D;
        level.addParticle(particle, x + offset.x, y + offset.y, z + offset.z, dx, dy, dz);
    }
}
