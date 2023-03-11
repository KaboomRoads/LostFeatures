package com.kaboomroads.lostfeatures.entity.custom;

import com.google.common.collect.ImmutableList;
import com.kaboomroads.lostfeatures.entity.ai.brain.TuffGolemAi;
import com.kaboomroads.lostfeatures.utils.ClothColor;
import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class TuffGolem extends AbstractGolem {
    private static final EntityDataAccessor<Integer> CLOTH_COLOR = SynchedEntityData.defineId(TuffGolem.class, EntityDataSerializers.INT);
    private static final Vec3i ITEM_PICKUP_REACH = new Vec3i(1, 1, 1);
    public final AnimationState walkAnimationState = new AnimationState();
    public final AnimationState displayAnimationState = new AnimationState();
    public final AnimationState stopDisplayAnimationState = new AnimationState();
    public final AnimationState shakeAnimationState = new AnimationState();
    protected static final ImmutableList<SensorType<? extends Sensor<? super TuffGolem>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_ITEMS);
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS);
    protected int freezeTicks;
    protected int freezeDuration = random.nextInt(100, 400);
    public Vec3 spawnPosition;
    public Direction spawnDirection;
    public boolean frozen = false;

    public TuffGolem(EntityType<? extends TuffGolem> entityType, Level level) {
        super(entityType, level);
        setCanPickUpLoot(canPickUpLoot());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(CLOTH_COLOR, 0);
    }

    public ClothColor getClothColor() {
        return ClothColor.values()[entityData.get(CLOTH_COLOR)];
    }

    public void setClothColor(ClothColor clothColor) {
        entityData.set(CLOTH_COLOR, clothColor.ordinal());
    }

    @NotNull
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.KNOCKBACK_RESISTANCE, 0.2D);
    }

    @Override
    public boolean canPickUpLoot() {
        return !this.isOnPickupCooldown();
    }

    private boolean isOnPickupCooldown() {
        return this.getBrain().checkMemory(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryStatus.VALUE_PRESENT);
    }

    @Override
    protected void pickUpItem(@NotNull ItemEntity itemEntity) {
        if (itemEntity.getItem().getCount() > 1) {
            itemEntity.getItem().setCount(itemEntity.getItem().getCount() - 1);
            itemEntity = itemEntity.copy();
            itemEntity.getItem().setCount(1);
        }
        super.pickUpItem(itemEntity);
    }

    @NotNull
    @Override
    public ItemStack equipItemIfPossible(@NotNull ItemStack item) {
        EquipmentSlot $$1 = EquipmentSlot.MAINHAND;
        ItemStack $$2 = getItemBySlot($$1);
        boolean $$3 = canReplaceCurrentItem(item, $$2);
        if ($$3 && canHoldItem(item)) {
            double $$4 = getEquipmentDropChance($$1);
            if (!$$2.isEmpty() && (double) Math.max(random.nextFloat() - 0.1F, 0.0F) < $$4) spawnAtLocation($$2);
            if ($$1.isArmor() && item.getCount() > 1) {
                ItemStack $$5 = item.copyWithCount(1);
                setItemSlotAndDropWhenKilled($$1, $$5);
                return $$5;
            } else {
                setItemSlotAndDropWhenKilled($$1, item);
                return item;
            }
        } else return ItemStack.EMPTY;
    }

    @NotNull
    @Override
    protected Vec3i getPickupReach() {
        return ITEM_PICKUP_REACH;
    }

    @Override
    public boolean wantsToPickUp(@NotNull ItemStack itemStack) {
        return super.wantsToPickUp(itemStack);
    }

    @Override
    public boolean canHoldItem(@NotNull ItemStack itemStack) {
        return getItemBySlot(EquipmentSlot.MAINHAND).isEmpty();
    }

    @Override
    public void onItemPickup(@NotNull ItemEntity itemEntity) {
        super.onItemPickup(itemEntity);
        level.broadcastEntityEvent(this, (byte) 64);
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        ItemStack itemstack = getItemBySlot(EquipmentSlot.MAINHAND);
        if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack)) {
            spawnAtLocation(itemstack);
            setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
    }

    @NotNull
    @Override
    public InteractionResult interactAt(@NotNull Player player, @NotNull Vec3 $$1, @NotNull InteractionHand hand) {
        ItemStack handItem = player.getItemInHand(hand);
        if (handItem.getItem() instanceof DyeItem dyeItem) {
            ClothColor color = switch (dyeItem.getDyeColor()) {
                case RED -> ClothColor.RED;
                case WHITE -> ClothColor.WHITE;
                case ORANGE -> ClothColor.ORANGE;
                case MAGENTA -> ClothColor.MAGENTA;
                case LIGHT_BLUE -> ClothColor.LIGHT_BLUE;
                case YELLOW -> ClothColor.YELLOW;
                case LIME -> ClothColor.LIME;
                case PINK -> ClothColor.PINK;
                case GRAY -> ClothColor.GRAY;
                case LIGHT_GRAY -> ClothColor.LIGHT_GRAY;
                case CYAN -> ClothColor.CYAN;
                case PURPLE -> ClothColor.PURPLE;
                case BLUE -> ClothColor.BLUE;
                case BROWN -> ClothColor.BROWN;
                case GREEN -> ClothColor.GREEN;
                case BLACK -> ClothColor.BLACK;
            };
            if (!level.isClientSide && getClothColor() != color) {
                setClothColor(color);
                if (!player.getAbilities().instabuild) handItem.shrink(1);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else if (!getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
            if (!level.isClientSide) {
                getBrain().setMemory(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, 60);
                spawnAtLocation(getItemBySlot(EquipmentSlot.MAINHAND));
                setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                ItemStack itemstack = getItemBySlot(EquipmentSlot.MAINHAND);
                if (!itemstack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemstack)) {
                    spawnAtLocation(itemstack);
                    setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                }
                level.broadcastEntityEvent(this, (byte) 65);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return super.interactAt(player, $$1, hand);
    }

    @NotNull
    @Override
    protected Brain.Provider<TuffGolem> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    @NotNull
    @Override
    protected Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return TuffGolemAi.makeBrain(brainProvider().makeBrain(dynamic));
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public Brain<TuffGolem> getBrain() {
        return (Brain<TuffGolem>) super.getBrain();
    }

    @Override
    protected void customServerAiStep() {
        level.getProfiler().push("tuffGolemBrain");
        getBrain().tick((ServerLevel) level, this);
        level.getProfiler().pop();
        TuffGolemAi.updateActivity(this);
        super.customServerAiStep();
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("FreezeTicks", freezeTicks);
        compoundTag.putInt("FreezeDuration", freezeDuration);
        compoundTag.putBoolean("Frozen", frozen);
        compoundTag.putInt("Color", getClothColor().ordinal());
        if (spawnPosition != null)
            compoundTag.put("SpawnPos", newDoubleList(spawnPosition.x, spawnPosition.y, spawnPosition.z));
        if (spawnDirection != null) compoundTag.putInt("SpawnDir", spawnDirection.ordinal());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        freezeTicks = compoundTag.getInt("FreezeTicks");
        frozen = compoundTag.getBoolean("Frozen");
        setClothColor(ClothColor.values()[compoundTag.getInt("Color")]);
        if (compoundTag.contains("FreezeDuration")) freezeDuration = compoundTag.getInt("FreezeDuration");
        if (compoundTag.contains("SpawnPos")) {
            ListTag pos = compoundTag.getList("SpawnPos", 6);
            spawnPosition = new Vec3(Mth.clamp(pos.getDouble(0), -3.0000512E7D, 3.0000512E7D), Mth.clamp(pos.getDouble(1), -2.0E7D, 2.0E7D), Mth.clamp(pos.getDouble(2), -3.0000512E7D, 3.0000512E7D));
        }
        if (compoundTag.contains("SpawnDir")) {
            int ordinal = compoundTag.getInt("SpawnDir");
            if (ordinal <= Direction.values().length - 1) spawnDirection = Direction.values()[ordinal];
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        switch (id) {
            case 64 -> displayAnimationState.start(tickCount);
            case 65 -> {
                displayAnimationState.stop();
                stopDisplayAnimationState.start(tickCount);
            }
            case 66 -> shakeAnimationState.start(tickCount);
            default -> super.handleEntityEvent(id);
        }
    }

    @Override
    public boolean isImmobile() {
        return frozen || super.isImmobile();
    }

    @Override
    public void push(double $$0, double $$1, double $$2) {
        if (!isImmobile()) super.push($$0, $$1, $$2);
    }

    @Override
    public void tick() {
        super.tick();
        if (frozen) setDeltaMovement(0, 0, 0);
        if (!level.isClientSide) {
            if (spawnPosition == null) spawnPosition = position();
            if (!isNoAi()) ++freezeTicks;
            if (freezeTicks >= freezeDuration) {
                if (frozen) {
                    frozen = false;
                    setDeltaMovement(0, 0, 0);
                    level.broadcastEntityEvent(this, (byte) 66);
                    freezeTicks = 0;
                    freezeDuration = random.nextInt(100, 400);
                } else {
                    Vec3 start = spawnPosition.add(0, 0.99, 0);
                    Vec3 end = new Vec3(spawnPosition.x, level.getMinBuildHeight(), spawnPosition.z);
                    BlockHitResult hitResult = level.clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
                    BlockPos pos = hitResult.getBlockPos();
                    Vec3 targetPos = Vec3.atBottomCenterOf(pos).add(0, 1, 0);
                    getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(Vec3.atBottomCenterOf(pos), 1.0F, 0));
                    if (navigation.isDone() && distanceToSqr(targetPos) <= 1.0F) {
                        frozen = true;
                        teleportTo(targetPos.x, targetPos.y, targetPos.z);
                        if (spawnDirection != null) {
                            float newYRot = spawnDirection.toYRot();
                            setRot(newYRot, 0);
                            yBodyRot = newYRot;
                            yHeadRot = newYRot;
                        }
                        setDeltaMovement(0, 0, 0);
                        level.broadcastEntityEvent(this, (byte) 66);
                        freezeTicks = 0;
                        freezeDuration = random.nextInt(100, 400);
                    }
                }
            }
        } else {
            walkAnimationState.animateWhen((onGround || hasControllingPassenger()) && !isNoAi() && getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D, tickCount);
            if (!getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) displayAnimationState.startIfStopped(tickCount);
            else level.broadcastEntityEvent(this, (byte) 65);
        }
    }
}