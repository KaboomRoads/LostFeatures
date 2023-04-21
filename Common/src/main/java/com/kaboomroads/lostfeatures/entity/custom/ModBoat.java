package com.kaboomroads.lostfeatures.entity.custom;

import com.kaboomroads.lostfeatures.block.ModBlocks;
import com.kaboomroads.lostfeatures.entity.ModEntityTypes;
import com.kaboomroads.lostfeatures.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

public class ModBoat extends Boat {
    private static final EntityDataAccessor<Integer> CUSTOM_TYPE = SynchedEntityData.defineId(ModBoat.class, EntityDataSerializers.INT);

    public ModBoat(EntityType<? extends ModBoat> entityType, Level level) {
        super(entityType, level);
    }

    public ModBoat(Level level, double x, double y, double z) {
        this(ModEntityTypes.BOAT.get(), level);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CUSTOM_TYPE, Type.BAOBAB.ordinal());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        if (compoundTag.contains("Type", 8)) {
            this.setCustomVariant(Type.byName(compoundTag.getString("Type")));
        }
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putString("Type", getCustomVariant().getName());
    }

    @NotNull
    @Override
    public Item getDropItem() {
        return switch (getCustomVariant()) {
            case BAOBAB -> ModItems.BAOBAB_BOAT.get();
        };
    }

    public void setCustomVariant(Type type) {
        entityData.set(CUSTOM_TYPE, type.ordinal());
    }

    @NotNull
    public Type getCustomVariant() {
        return Type.byId(entityData.get(CUSTOM_TYPE));
    }

    public enum Type implements StringRepresentable {
        BAOBAB(ModBlocks.BAOBAB_PLANKS.get(), "baobab");

        private final String name;
        private final Block planks;
        public static final EnumCodec<Type> CODEC = StringRepresentable.fromEnum(Type::values);
        private static final IntFunction<Type> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);

        Type(Block planks, String name) {
            this.planks = planks;
            this.name = name;
        }

        @NotNull
        public String getSerializedName() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }

        public Block getPlanks() {
            return this.planks;
        }

        public String toString() {
            return this.name;
        }

        public static Type byId(int id) {
            return BY_ID.apply(id);
        }

        public static Type byName(String name) {
            return CODEC.byName(name, BAOBAB);
        }
    }
}