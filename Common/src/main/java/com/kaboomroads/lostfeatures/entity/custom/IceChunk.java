package com.kaboomroads.lostfeatures.entity.custom;

import com.kaboomroads.lostfeatures.damagesource.ModDamageSources;
import com.kaboomroads.lostfeatures.entity.ModEntityTypes;
import com.kaboomroads.lostfeatures.utils.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class IceChunk extends Entity {
    public int time;
    public float damage;
    public LivingEntity source;
    protected static final EntityDataAccessor<BlockPos> DATA_START_POS = SynchedEntityData.defineId(IceChunk.class, EntityDataSerializers.BLOCK_POS);

    public IceChunk(EntityType<? extends IceChunk> entityType, Level level) {
        super(entityType, level);
    }

    public IceChunk(Level level, double x, double y, double z, double dx, double dy, double dz, LivingEntity source) {
        this(ModEntityTypes.ICE_CHUNK.get(), level);
        this.setPos(x, y, z);
        this.setDeltaMovement(new Vec3(dx, dy, dz));
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.source = source;
        this.setStartPos(this.blockPosition());
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    public void setStartPos(BlockPos p_31960_) {
        this.entityData.set(DATA_START_POS, p_31960_);
    }

    public BlockPos getStartPos() {
        return this.entityData.get(DATA_START_POS);
    }

    @NotNull
    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_START_POS, BlockPos.ZERO);
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public void tick() {
        ++time;
        if (!this.isNoGravity()) setDeltaMovement(getDeltaMovement().add(0.0D, -0.04D, 0.0D));
        move(MoverType.SELF, getDeltaMovement());
        if (!level.isClientSide && isOnGround()) {
            setDeltaMovement(getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
            destroy();
        }
        setDeltaMovement(getDeltaMovement().scale(0.98D));
    }

    public void destroy() {
        discard();
        for (Entity entity : level.getEntities(this, new AABB(getX() - 2.0D, getY() - 2.0D, getZ() - 2.0D, getX() + 2.0D, getY() + 2.0D, getZ() + 2.0D), e -> (e instanceof Mob || e instanceof Player) && Utils.entityIsDamageable(e) && !(e instanceof Raider)))
            entity.hurt(((ModDamageSources) damageSources()).iceChunk(this, source), 6.0F);
        if (level instanceof ServerLevel serverLevel)
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.BLUE_ICE.defaultBlockState()), getX(), getY(), getZ(), 100, 0.75D, 0.75D, 0.75D, 0.1D);
    }

    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putInt("Time", time);
        compoundTag.putFloat("Damage", damage);
        if (source != null) compoundTag.putInt("Source", source.getId());
    }

    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        time = compoundTag.getInt("Time");
        damage = compoundTag.getFloat("Damage");
        source = (LivingEntity) level.getEntity(compoundTag.getInt("Source"));
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    public boolean onlyOpCanSetNbt() {
        return true;
    }
}

