package com.kaboomroads.lostfeatures.entity.custom;

import com.kaboomroads.lostfeatures.mixin.BlazeAccessor;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public class Wildfire extends Blaze {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState hurtAnimationState = new AnimationState();
    private static final EntityDataAccessor<Integer> SHIELDS = SynchedEntityData.defineId(Wildfire.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SHIELD_REGENERATION = SynchedEntityData.defineId(Wildfire.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SHIELD_REGENERATING = SynchedEntityData.defineId(Wildfire.class, EntityDataSerializers.BOOLEAN);
    public static final int SHEILD_REGEN_START_TIME = 60;
    public static final int SHEILD_REGEN_TIME = 40;

    public Wildfire(EntityType<? extends Blaze> entityType, Level level) {
        super(entityType, level);
        idleAnimationState.startIfStopped(tickCount);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(SHIELDS, 4);
        entityData.define(SHIELD_REGENERATION, 0);
        entityData.define(SHIELD_REGENERATING, false);
    }

    public int getShields() {
        return entityData.get(SHIELDS);
    }

    public void setShields(int shields) {
        entityData.set(SHIELDS, shields);
    }

    public int getShieldRegeneration() {
        return entityData.get(SHIELD_REGENERATION);
    }

    public void setShieldRegeneration(int shieldRegeneration) {
        entityData.set(SHIELD_REGENERATION, shieldRegeneration);
    }

    public boolean getShieldRegenerating() {
        return entityData.get(SHIELD_REGENERATING);
    }

    public void setShieldRegenerating(boolean shieldRegenerating) {
        entityData.set(SHIELD_REGENERATING, shieldRegenerating);
    }

    @NotNull
    public static AttributeSupplier.Builder createAttributes() {
        return Blaze.createAttributes().add(Attributes.MAX_HEALTH, 40.0).add(Attributes.ATTACK_DAMAGE, 8.0D);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 64) hurtAnimationState.start(tickCount);
        else super.handleEntityEvent(id);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level().isClientSide) return;
        if (getShieldRegenerating()) {
            setShieldRegeneration(getShieldRegeneration() + 1);
            int shieldRegen = getShieldRegeneration();
            if (shieldRegen >= SHEILD_REGEN_START_TIME && shieldRegen % SHEILD_REGEN_TIME == 0) {
                setShields(getShields() + 1);
                if (getShields() >= 4) {
                    setShieldRegenerating(false);
                    setShieldRegeneration(0);
                }
            }
        }
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource damageSource) {
        return (damageSource.getEntity() != null && damageSource.getEntity().is(this) && damageSource.getDirectEntity() instanceof SmallFireball) || damageSource.is(DamageTypeTags.IS_FIRE) || super.isInvulnerableTo(damageSource);
    }

    @Override
    public boolean hurt(@NotNull DamageSource damageSource, float damage) {
        float originalDamage = damage;
        if (!damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) damage *= 1f - getShields() / 4f;
        if (super.hurt(damageSource, damage)) {
            if (!level().isClientSide && !damageSource.is(DamageTypes.MAGIC) && !damageSource.is(DamageTypeTags.IS_FALL) && originalDamage >= 3) {
                int shields = getShields();
                if (shields > 0) {
                    setShields(shields - 1);
                    if (shields <= 4) setShieldRegenerating(true);
                } else if (shields != 0) setShields(0);
                level().broadcastEntityEvent(this, (byte) 64);
            }
            return true;
        }
        return false;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(4, new WildfireAttackGoal(this, 3));
        goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
        goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.0F));
        goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static class WildfireAttackGoal extends Goal {
        private final Blaze blaze;
        private int attackStep;
        private int attackTime;
        private int lastSeen;
        private final int burst;

        public WildfireAttackGoal(Blaze blaze, int burst) {
            this.blaze = blaze;
            this.burst = burst;
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity target = blaze.getTarget();
            return target != null && target.isAlive() && blaze.canAttack(target);
        }

        public void start() {
            attackStep = 0;
        }

        public void stop() {
            ((BlazeAccessor) blaze).invokeSetCharged(false);
            lastSeen = 0;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            --attackTime;
            LivingEntity target = blaze.getTarget();
            if (target != null) {
                boolean hasLineOfSight = blaze.getSensing().hasLineOfSight(target);
                if (hasLineOfSight) lastSeen = 0;
                else ++lastSeen;
                double distanceSqr = blaze.distanceToSqr(target);
                if (distanceSqr < 4.0D) {
                    if (!hasLineOfSight) return;
                    if (attackTime <= 0) {
                        attackTime = 20;
                        blaze.doHurtTarget(target);
                    }
                    blaze.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), 1.0D);
                } else if (distanceSqr < getFollowDistance() * getFollowDistance() && hasLineOfSight) {
                    double dx = target.getX() - blaze.getX();
                    double dy = target.getY(0.5D) - blaze.getY(0.5D);
                    double dz = target.getZ() - blaze.getZ();
                    if (attackTime <= 0) {
                        ++attackStep;
                        if (attackStep == 1) {
                            attackTime = 60;
                            ((BlazeAccessor) blaze).invokeSetCharged(true);
                        } else if (attackStep <= 4) attackTime = 6;
                        else {
                            attackTime = 100;
                            attackStep = 0;
                            ((BlazeAccessor) blaze).invokeSetCharged(false);
                        }
                        if (attackStep > 1) {
                            double distanceSqrt = Math.sqrt(Math.sqrt(distanceSqr)) * 0.5D;
                            if (!blaze.isSilent()) blaze.level().levelEvent(null, 1018, blaze.blockPosition(), 0);
                            for (int i = 0; i < burst; ++i) {
                                SmallFireball fireball = new SmallFireball(blaze.level(), blaze, blaze.getRandom().triangle(dx, 1.5D * distanceSqrt), dy, blaze.getRandom().triangle(dz, 1.5D * distanceSqrt));
                                fireball.setPos(fireball.getX(), blaze.getY(0.5D) + 0.5D, fireball.getZ());
                                blaze.level().addFreshEntity(fireball);
                            }
                        }
                    }
                    blaze.getLookControl().setLookAt(target, 10.0F, 10.0F);
                } else if (lastSeen < 5)
                    blaze.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), 1.0D);
                super.tick();
            }
        }

        private double getFollowDistance() {
            return blaze.getAttributeValue(Attributes.FOLLOW_RANGE);
        }
    }
}
