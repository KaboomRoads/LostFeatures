package com.kaboomroads.lostfeatures.mixin;

import com.kaboomroads.lostfeatures.utils.ModRaiderType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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

    private int getModDefaultNumSpawns(ModRaiderType raiderType, int p_37732_, boolean p_37733_) {
        return p_37733_ ? raiderType.spawnsPerWaveBeforeBonus[this.numGroups] : raiderType.spawnsPerWaveBeforeBonus[p_37732_];
    }

    @Redirect(method = "spawnGroup", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/raid/Raid;shouldSpawnBonusGroup()Z"))
    public boolean redirectShouldSpawnBonusGroup(Raid instance) {
        if (this.groupsSpawned + 1 > this.numGroups) return true;
        return this.shouldSpawnBonusGroup();
    }

    @Inject(method = "spawnGroup", at = @At(value = "HEAD"))
    private void spawnExtraGroup(BlockPos blockPos, CallbackInfo ci) {
        int groups = this.groupsSpawned + 1;
        boolean spawnExtra = this.shouldSpawnBonusGroup();
        for (ModRaiderType raiderType : ModRaiderType.VALUES) {
            int j = this.getModDefaultNumSpawns(raiderType, groups, spawnExtra);
            for (int l = 0; l < j; ++l) {
                Raider raider = raiderType.entityType.create(this.level);
                if (raider == null) break;
                this.joinRaid(groups, raider, blockPos, false);
            }
        }
    }
}
