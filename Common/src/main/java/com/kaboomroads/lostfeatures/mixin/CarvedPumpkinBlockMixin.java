package com.kaboomroads.lostfeatures.mixin;

import com.kaboomroads.lostfeatures.entity.ModEntityTypes;
import com.kaboomroads.lostfeatures.entity.custom.CopperGolem;
import com.kaboomroads.lostfeatures.entity.custom.TuffGolem;
import com.kaboomroads.lostfeatures.tag.ModTags;
import com.kaboomroads.lostfeatures.utils.MutablePair;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockPredicate;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.function.Predicate;

@Mixin(CarvedPumpkinBlock.class)
public abstract class CarvedPumpkinBlockMixin {
    @Final
    @Shadow
    private static Predicate<BlockState> PUMPKINS_PREDICATE;

    @Shadow
    private static void spawnGolemInWorld(Level level, BlockPattern.BlockPatternMatch patternMatch, Entity entity, BlockPos blockPos) {
    }

    @Shadow
    @Final
    public static DirectionProperty FACING;
    @Nullable
    @Unique
    private BlockPattern lostfeatures$copperGolemBase;
    @Nullable
    @Unique
    private BlockPattern lostfeatures$copperGolemFull;
    @Nullable
    @Unique
    private BlockPattern lostfeatures$tuffGolemBase;
    @Nullable
    @Unique
    private BlockPattern lostfeatures$tuffGolemFull;
    @Unique
    private final ThreadLocal<MutablePair<Boolean, Integer>> lostfeatures$currentPair = ThreadLocal.withInitial(() -> MutablePair.of(false, 0));

    @Unique
    private Predicate<BlockState> lostfeatures$copperPredicate() {
        return blockState -> {
            if (blockState.is(ModTags.Blocks.COPPER_BLOCKS)) {
                int age = 0;
                if (blockState.is(ModTags.Blocks.EXPOSED_COPPER))
                    age = 1;
                else if (blockState.is(ModTags.Blocks.WEATHERED_COPPER))
                    age = 2;
                else if (blockState.is(ModTags.Blocks.OXIDIZED_COPPER))
                    age = 3;
                lostfeatures$currentPair.get().setSecond(age);
                lostfeatures$currentPair.get().setFirst(blockState.is(ModTags.Blocks.WAXED_COPPER));
                return true;
            }
            return false;
        };
    }

    @Unique
    private BlockPattern lostfeatures$getOrCreateCopperGolemFull() {
        if (this.lostfeatures$copperGolemFull == null)
            this.lostfeatures$copperGolemFull = BlockPatternBuilder.start().aisle("^", "#").where('#', BlockInWorld.hasState(lostfeatures$copperPredicate())).where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE)).build();
        return lostfeatures$copperGolemFull;
    }

    @Unique
    private BlockPattern lostfeatures$getOrCreateCopperGolemBase() {
        if (this.lostfeatures$copperGolemBase == null)
            this.lostfeatures$copperGolemBase = BlockPatternBuilder.start().aisle(" ", "#").where('#', BlockInWorld.hasState(lostfeatures$copperPredicate())).build();
        return lostfeatures$copperGolemBase;
    }

    @Unique
    private BlockPattern lostfeatures$getOrCreateTuffGolemFull() {
        if (this.lostfeatures$tuffGolemFull == null)
            this.lostfeatures$tuffGolemFull = BlockPatternBuilder.start().aisle("^", "#").where('#', BlockInWorld.hasState(BlockPredicate.forBlock(Blocks.TUFF))).where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE)).build();
        return lostfeatures$tuffGolemFull;
    }

    @Unique
    private BlockPattern lostfeatures$getOrCreateTuffGolemBase() {
        if (this.lostfeatures$tuffGolemBase == null)
            this.lostfeatures$tuffGolemBase = BlockPatternBuilder.start().aisle(" ", "#").where('#', BlockInWorld.hasState(BlockPredicate.forBlock(Blocks.TUFF))).build();
        return lostfeatures$tuffGolemBase;
    }

    @ModifyReturnValue(method = "canSpawnGolem", at = @At(value = "RETURN"))
    public boolean injectCanSpawnGolem(boolean original, @Local(ordinal = 0, argsOnly = true) LevelReader levelReader, @Local(ordinal = 0, argsOnly = true) BlockPos blockPos) {
        return original || lostfeatures$getOrCreateTuffGolemBase().find(levelReader, blockPos) != null || lostfeatures$getOrCreateCopperGolemBase().find(levelReader, blockPos) != null;
    }

    @Inject(method = "trySpawnGolem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/CarvedPumpkinBlock;getOrCreateIronGolemFull()Lnet/minecraft/world/level/block/state/pattern/BlockPattern;", ordinal = 0))
    public void injectTrySpawnGolem(Level level, BlockPos blockPos, CallbackInfo ci) {
        BlockPattern.BlockPatternMatch copperPattern = lostfeatures$getOrCreateCopperGolemFull().find(level, blockPos);
        if (copperPattern != null) {
            CopperGolem golem = ModEntityTypes.COPPER_GOLEM.get().create(level);
            if (golem != null) {
                golem.setWaxed(lostfeatures$currentPair.get().getFirst());
                golem.setStage(lostfeatures$currentPair.get().getSecond());
                spawnGolemInWorld(level, copperPattern, golem, copperPattern.getBlock(0, 1, 0).getPos());
            }
        } else {
            BlockPattern.BlockPatternMatch tuffPattern = lostfeatures$getOrCreateTuffGolemFull().find(level, blockPos);
            if (tuffPattern != null) {
                TuffGolem golem = ModEntityTypes.TUFF_GOLEM.get().create(level);
                if (golem != null) {
                    golem.spawnDirection = level.getBlockState(blockPos).getValue(FACING);
                    spawnGolemInWorld(level, tuffPattern, golem, tuffPattern.getBlock(0, 1, 0).getPos());
                }
            }
        }
    }
}
