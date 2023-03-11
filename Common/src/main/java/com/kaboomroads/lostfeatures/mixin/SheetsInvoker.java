package com.kaboomroads.lostfeatures.mixin;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Sheets.class)
public interface SheetsInvoker {
    @Invoker("createSignMaterial")
    static Material createSignMaterial(WoodType $$0) {
        return new Material(Sheets.SIGN_SHEET, new ResourceLocation("entity/signs/" + $$0.name()));
    }

    @Invoker("createHangingSignMaterial")
    static Material createHangingSignMaterial(WoodType $$0) {
        return new Material(Sheets.SIGN_SHEET, new ResourceLocation("entity/signs/hanging/" + $$0.name()));
    }
}
