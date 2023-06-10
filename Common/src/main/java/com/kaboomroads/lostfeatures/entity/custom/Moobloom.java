package com.kaboomroads.lostfeatures.entity.custom;

import com.kaboomroads.lostfeatures.entity.ModEntityTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class Moobloom extends Cow implements Shearable {
    public Moobloom(EntityType<? extends Moobloom> type, Level level) {
        super(type, level);
    }

    @NotNull
    @Override
    public InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);
        if (item.is(Items.SHEARS) && readyForShearing()) {
            shear(SoundSource.PLAYERS);
            gameEvent(GameEvent.SHEAR, player);
            if (!level().isClientSide) item.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            return InteractionResult.sidedSuccess(level().isClientSide);
        } else return super.mobInteract(player, hand);
    }

    @Override
    public void shear(@NotNull SoundSource soundSource) {
        level().playSound(null, this, SoundEvents.MOOSHROOM_SHEAR, soundSource, 1.0F, 1.0F);
        if (!level().isClientSide()) {
            Cow cow = EntityType.COW.create(level());
            if (cow != null) {
                ((ServerLevel) level()).sendParticles(ParticleTypes.EXPLOSION, getX(), getY(0.5D), getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
                discard();
                cow.moveTo(getX(), getY(), getZ(), getYRot(), getXRot());
                cow.setHealth(getHealth());
                cow.setXRot(getXRot());
                cow.setYRot(getYRot());
                cow.xRotO = xRotO;
                cow.yRotO = yRotO;
                cow.yBodyRot = yBodyRot;
                cow.yBodyRotO = yBodyRotO;
                cow.yHeadRot = yHeadRot;
                cow.yHeadRotO = yHeadRotO;
                if (hasCustomName()) {
                    cow.setCustomName(getCustomName());
                    cow.setCustomNameVisible(isCustomNameVisible());
                }
                if (isPersistenceRequired()) cow.setPersistenceRequired();
                cow.setInvulnerable(isInvulnerable());
                level().addFreshEntity(cow);
                for (int i = 0; i < 5; ++i)
                    level().addFreshEntity(new ItemEntity(level(), getX(), getY(1.0D), getZ(), new ItemStack(Items.DANDELION)));
            }
        }

    }

    public boolean readyForShearing() {
        return isAlive() && !isBaby();
    }

    @Nullable
    public Moobloom getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mob) {
        return ModEntityTypes.MOOBLOOM.get().create(level);
    }
}
