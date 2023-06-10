package com.kaboomroads.lostfeatures.entity.custom;

import com.kaboomroads.lostfeatures.entity.ModEntityTypes;
import com.kaboomroads.lostfeatures.item.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.ContainerEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEvent.Context;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ModChestBoat extends ModBoat implements HasCustomInventoryScreen, ContainerEntity {
    private static final int CONTAINER_SIZE = 27;
    private NonNullList<ItemStack> itemStacks;
    @Nullable
    private ResourceLocation lootTable;
    private long lootTableSeed;

    public ModChestBoat(EntityType<? extends ModBoat> $$0, Level $$1) {
        super($$0, $$1);
        this.itemStacks = NonNullList.withSize(27, ItemStack.EMPTY);
    }

    public ModChestBoat(Level $$0, double $$1, double $$2, double $$3) {
        this(ModEntityTypes.CHEST_BOAT.get(), $$0);
        this.setPos($$1, $$2, $$3);
        this.xo = $$1;
        this.yo = $$2;
        this.zo = $$3;
    }

    protected float getSinglePassengerXOffset() {
        return 0.15F;
    }

    protected int getMaxPassengers() {
        return 1;
    }

    protected void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        this.addChestVehicleSaveData(tag);
    }

    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.readChestVehicleSaveData(tag);
    }

    public void destroy(@NotNull DamageSource damageSource) {
        super.destroy(damageSource);
        this.chestVehicleDestroyed(damageSource, level(), this);
    }

    public void remove(@NotNull RemovalReason reason) {
        if (!level().isClientSide && reason.shouldDestroy()) Containers.dropContents(level(), this, this);
        super.remove(reason);
    }

    @NotNull
    public InteractionResult interact(@NotNull Player player, @NotNull InteractionHand hand) {
        if (canAddPassenger(player) && !player.isSecondaryUseActive()) return super.interact(player, hand);
        else {
            InteractionResult $$2 = interactWithContainerVehicle(player);
            if ($$2.consumesAction()) {
                gameEvent(GameEvent.CONTAINER_OPEN, player);
                PiglinAi.angerNearbyPiglins(player, true);
            }
            return $$2;
        }
    }

    public void openCustomInventoryScreen(Player player) {
        player.openMenu(this);
        if (!player.level().isClientSide) {
            gameEvent(GameEvent.CONTAINER_OPEN, player);
            PiglinAi.angerNearbyPiglins(player, true);
        }
    }

    @NotNull
    @Override
    public Item getDropItem() {
        return switch (getCustomVariant()) {
            case BAOBAB -> ModItems.BAOBAB_BOAT.get();
        };
    }

    public void clearContent() {
        clearChestVehicleContent();
    }

    public int getContainerSize() {
        return 27;
    }

    @NotNull
    public ItemStack getItem(int $$0) {
        return getChestVehicleItem($$0);
    }

    @NotNull
    public ItemStack removeItem(int $$0, int $$1) {
        return removeChestVehicleItem($$0, $$1);
    }

    @NotNull
    public ItemStack removeItemNoUpdate(int $$0) {
        return removeChestVehicleItemNoUpdate($$0);
    }

    public void setItem(int $$0, @NotNull ItemStack itemStack) {
        this.setChestVehicleItem($$0, itemStack);
    }

    @NotNull
    public SlotAccess getSlot(int $$0) {
        return getChestVehicleSlot($$0);
    }

    public void setChanged() {
    }

    public boolean stillValid(@NotNull Player player) {
        return this.isChestVehicleStillValid(player);
    }

    @Nullable
    public AbstractContainerMenu createMenu(int $$0, @NotNull Inventory inventory, @NotNull Player player) {
        if (lootTable != null && player.isSpectator()) return null;
        else {
            unpackLootTable(inventory.player);
            return ChestMenu.threeRows($$0, inventory, this);
        }
    }

    public void unpackLootTable(@Nullable Player player) {
        unpackChestVehicleLootTable(player);
    }

    @Nullable
    public ResourceLocation getLootTable() {
        return this.lootTable;
    }

    public void setLootTable(@Nullable ResourceLocation location) {
        lootTable = location;
    }

    public long getLootTableSeed() {
        return lootTableSeed;
    }

    public void setLootTableSeed(long seed) {
        lootTableSeed = seed;
    }

    @NotNull
    public NonNullList<ItemStack> getItemStacks() {
        return itemStacks;
    }

    public void clearItemStacks() {
        itemStacks = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
    }

    public void stopOpen(@NotNull Player player) {
        level().gameEvent(GameEvent.CONTAINER_CLOSE, position(), Context.of(player));
    }
}
