package com.kaboomroads.lostfeatures.mixin;

import com.kaboomroads.lostfeatures.entity.ModEntityTypes;
import com.kaboomroads.lostfeatures.entity.custom.CopperGolem;
import com.kaboomroads.lostfeatures.entity.custom.TuffGolem;
import com.kaboomroads.lostfeatures.tag.ModTags;
import com.kaboomroads.lostfeatures.utils.MutablePair;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
    private BlockPattern copperGolemBase;
    @Nullable
    private BlockPattern copperGolemFull;
    @Nullable
    private BlockPattern tuffGolemBase;
    @Nullable
    private BlockPattern tuffGolemFull;

    private final MutablePair<Boolean, Integer> currentPair = MutablePair.of(false, 0);

    private Predicate<BlockState> copperPredicate() {
        return blockState -> {
            if (blockState.is(ModTags.Blocks.COPPER_BLOCKS)) {
                int age = 0;
                if (blockState.is(ModTags.Blocks.EXPOSED_COPPER))
                    age = 1;
                else if (blockState.is(ModTags.Blocks.WEATHERED_COPPER))
                    age = 2;
                else if (blockState.is(ModTags.Blocks.OXIDIZED_COPPER))
                    age = 3;
                currentPair.setSecond(age);
                currentPair.setFirst(blockState.is(ModTags.Blocks.WAXED_COPPER));
                return true;
            }
            return false;
        };
    }

    private BlockPattern getOrCreateCopperGolemFull() {
        if (this.copperGolemFull == null)
            this.copperGolemFull = BlockPatternBuilder.start().aisle("^", "#").where('#', BlockInWorld.hasState(copperPredicate())).where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE)).build();
        return copperGolemFull;
    }

    private BlockPattern getOrCreateCopperGolemBase() {
        if (this.copperGolemBase == null)
            this.copperGolemBase = BlockPatternBuilder.start().aisle(" ", "#").where('#', BlockInWorld.hasState(copperPredicate())).build();
        return copperGolemBase;
    }

    private BlockPattern getOrCreateTuffGolemFull() {
        if (this.tuffGolemFull == null)
            this.tuffGolemFull = BlockPatternBuilder.start().aisle("^", "#").where('#', BlockInWorld.hasState(BlockPredicate.forBlock(Blocks.TUFF))).where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE)).build();
        return tuffGolemFull;
    }

    private BlockPattern getOrCreateTuffGolemBase() {
        if (this.tuffGolemBase == null)
            this.tuffGolemBase = BlockPatternBuilder.start().aisle(" ", "#").where('#', BlockInWorld.hasState(BlockPredicate.forBlock(Blocks.TUFF))).build();
        return tuffGolemBase;
    }

    @Inject(method = "canSpawnGolem", at = @At(value = "HEAD"), cancellable = true)
    public void injectCanSpawnGolem(LevelReader levelReader, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if (getOrCreateTuffGolemBase().find(levelReader, blockPos) != null || getOrCreateCopperGolemBase().find(levelReader, blockPos) != null)
            cir.setReturnValue(true);
    }

    @Inject(method = "trySpawnGolem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/CarvedPumpkinBlock;getOrCreateIronGolemFull()Lnet/minecraft/world/level/block/state/pattern/BlockPattern;"))
    public void injectTrySpawnGolem(Level level, BlockPos blockPos, CallbackInfo ci) {
        BlockPattern.BlockPatternMatch copperPattern = getOrCreateCopperGolemFull().find(level, blockPos);
        if (copperPattern != null) {
            CopperGolem golem = ModEntityTypes.COPPER_GOLEM.get().create(level);
            if (golem != null) {
                golem.setWaxed(currentPair.getFirst());
                golem.setStage(currentPair.getSecond());
                spawnGolemInWorld(level, copperPattern, golem, copperPattern.getBlock(0, 1, 0).getPos());
            }
        } else {
            BlockPattern.BlockPatternMatch tuffPattern = getOrCreateTuffGolemFull().find(level, blockPos);
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
