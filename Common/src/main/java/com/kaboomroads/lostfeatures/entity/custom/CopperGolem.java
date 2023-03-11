package com.kaboomroads.lostfeatures.entity.custom;

import com.google.common.collect.ImmutableList;
import com.kaboomroads.lostfeatures.entity.ai.ModMemoryModuleType;
import com.kaboomroads.lostfeatures.entity.ai.ModSensorType;
import com.kaboomroads.lostfeatures.entity.ai.brain.CopperGolemAi;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class CopperGolem extends AbstractGolem {
    private static final EntityDataAccessor<Integer> STAGE = SynchedEntityData.defineId(CopperGolem.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> WAXED = SynchedEntityData.defineId(CopperGolem.class, EntityDataSerializers.BOOLEAN);
    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState buttonAnimationState = new AnimationState();
    protected static final ImmutableList<SensorType<? extends Sensor<? super CopperGolem>>> SENSOR_TYPES = ImmutableList.of(ModSensorType.COPPER_BUTTON_SENSOR.get());
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_COOLING_DOWN, ModMemoryModuleType.INTERESTING_BLOCK_LOCATION.get());
    protected int oxidizeTicks;
    protected int oxidizeDuration = random.nextInt(30000, 50000);

    public CopperGolem(EntityType<? extends CopperGolem> entityType, Level level) {
        super(entityType, level);
    }

    @NotNull
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.KNOCKBACK_RESISTANCE, 0.25D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(STAGE, 0);
        entityData.define(WAXED, false);
    }

    public int getStage() {
        return entityData.get(STAGE);
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || getStage() >= 3;
    }

    public void setStage(int stage) {
        if (stage >= 3) {
            getBrain().eraseMemory(ModMemoryModuleType.INTERESTING_BLOCK_LOCATION.get());
            getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
            getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
        } else if (getStage() >= 3) setDeltaMovement(Vec3.ZERO);
        entityData.set(STAGE, stage);
        oxidizeTicks = 0;
    }

    public boolean getWaxed() {
        return entityData.get(WAXED);
    }

    public void setWaxed(boolean waxed) {
        entityData.set(WAXED, waxed);
        oxidizeTicks = 0;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("OxidizeTicks", oxidizeTicks);
        compoundTag.putInt("OxidizeDuration", oxidizeDuration);
        compoundTag.putInt("Stage", getStage());
        compoundTag.putBoolean("Waxed", getWaxed());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        setStage(compoundTag.getInt("Stage"));
        setWaxed(compoundTag.getBoolean("Waxed"));
        oxidizeTicks = compoundTag.getInt("OxidizeTicks");
        if (compoundTag.contains("OxidizeDuration")) oxidizeDuration = compoundTag.getInt("OxidizeDuration");
    }

    @NotNull
    @Override
    protected Brain.Provider<CopperGolem> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    @NotNull
    @Override
    protected Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return CopperGolemAi.makeBrain(brainProvider().makeBrain(dynamic));
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public Brain<CopperGolem> getBrain() {
        return (Brain<CopperGolem>) super.getBrain();
    }

    @Override
    protected void customServerAiStep() {
        level.getProfiler().push("copperGolemBrain");
        getBrain().tick((ServerLevel) level, this);
        level.getProfiler().pop();
        CopperGolemAi.updateActivity(this);
        super.customServerAiStep();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!level.isClientSide && !getWaxed() && getStage() < 3) {
            ++oxidizeTicks;
            if (oxidizeTicks >= oxidizeDuration) {
                setStage(Mth.clamp(getStage() + 1, 0, 3));
                oxidizeTicks = 0;
                oxidizeDuration = random.nextInt(30000, 50000);
            }
        }
    }

    @NotNull
    @Override
    public InteractionResult interactAt(@NotNull Player player, @NotNull Vec3 vec3, @NotNull InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);
        if (item.is(Items.HONEYCOMB) && !getWaxed()) {
            setWaxed(true);
            if (level instanceof ServerLevel serverLevel)
                serverLevel.sendParticles(ParticleTypes.WAX_ON, position().x, position().y, position().z, 50, 0.25f, 0.5f, 0.25f, 0);
            this.level.playLocalSound(blockPosition(), SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1.0F, 1.0F, false);
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else if (item.getItem() instanceof AxeItem) {
            if (getWaxed()) {
                setWaxed(false);
                if (level instanceof ServerLevel serverLevel)
                    serverLevel.sendParticles(ParticleTypes.WAX_OFF, position().x, position().y, position().z, 50, 0.25f, 0.5f, 0.25f, 0);
                return InteractionResult.sidedSuccess(level.isClientSide);
            } else if (getStage() > 0) {
                setStage(getStage() - 1);
                if (level instanceof ServerLevel serverLevel)
                    serverLevel.sendParticles(ParticleTypes.SCRAPE, position().x, position().y, position().z, 50, 0.25f, 0.5f, 0.25f, 0);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return super.interactAt(player, vec3, hand);
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 64) buttonAnimationState.start(tickCount);
        else super.handleEntityEvent(id);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level.isClientSide())
            this.walkAnimationState.animateWhen((this.onGround || this.hasControllingPassenger()) && !isNoAi() && this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D, this.tickCount);
    }
}
