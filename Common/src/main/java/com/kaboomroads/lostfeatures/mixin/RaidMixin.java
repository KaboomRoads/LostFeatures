package com.kaboomroads.lostfeatures.mixin;

import com.kaboomroads.lostfeatures.utils.ModRaiderType;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Raid.class)
public abstract class RaidMixin {
    @Shadow
    private int groupsSpawned;
    @Final
    @Shadow
    private int numGroups;
    @Final
    @Shadow
    private ServerLevel level;

    @Shadow
    protected abstract boolean shouldSpawnBonusGroup();

    @Shadow
    public abstract void joinRaid(int p_37714_, Raider p_37715_, @Nullable BlockPos p_37716_, boolean p_37717_);

    @Unique
    private int lostFeatures$getModDefaultNumSpawns(ModRaiderType raiderType, int p_37732_, boolean p_37733_) {
        return p_37733_ ? raiderType.spawnsPerWaveBeforeBonus[numGroups] : raiderType.spawnsPerWaveBeforeBonus[p_37732_];
    }

    @ModifyExpressionValue(method = "spawnGroup", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/raid/Raid;shouldSpawnBonusGroup()Z"))
    public boolean modifyShouldSpawnBonusGroup(boolean original, BlockPos pos) {
        return original || groupsSpawned + 1 > numGroups;
    }

    @Inject(method = "spawnGroup", at = @At(value = "HEAD"))
    private void spawnExtraGroup(BlockPos blockPos, CallbackInfo ci) {
        int groups = groupsSpawned + 1;
        boolean spawnExtra = shouldSpawnBonusGroup();
        for (ModRaiderType raiderType : ModRaiderType.VALUES) {
            int j = this.lostFeatures$getModDefaultNumSpawns(raiderType, groups, spawnExtra);
            for (int l = 0; l < j; ++l) {
                Raider raider = raiderType.entityType.create(level);
                if (raider == null) break;
                this.joinRaid(groups, raider, blockPos, false);
            }
        }
    }
}
