package com.kaboomroads.lostfeatures.mixin;

import com.kaboomroads.lostfeatures.block.ModBlocks;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Arrays;

@Mixin(ItemColors.class)
public abstract class ItemColorsMixin {
    @Redirect(method = "createDefault", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/color/item/ItemColors;register(Lnet/minecraft/client/color/item/ItemColor;[Lnet/minecraft/world/level/ItemLike;)V", ordinal = 5))
    private static void redirectRegister(ItemColors instance, ItemColor color, ItemLike[] items) {
        ItemLike[] newItems = {
                ModBlocks.BAOBAB_LEAVES.get()
        };
        ItemLike[] combined = Arrays.copyOf(items, items.length + newItems.length);
        System.arraycopy(newItems, 0, combined, items.length, newItems.length);
        instance.register(color, combined);
    }
}
