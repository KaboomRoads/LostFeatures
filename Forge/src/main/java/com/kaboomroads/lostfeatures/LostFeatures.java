package com.kaboomroads.lostfeatures;

import com.google.common.collect.ImmutableMap;
import com.kaboomroads.lostfeatures.block.ModBlocks;
import com.kaboomroads.lostfeatures.block.ModWoodTypes;
import com.kaboomroads.lostfeatures.mixin.AxeItemAccessor;
import com.kaboomroads.lostfeatures.mixin.SheetsInvoker;
import com.kaboomroads.lostfeatures.platform.RegistryHelperImpl;
import com.kaboomroads.lostfeatures.platform.Services;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class LostFeatures {
    public LostFeatures() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        Services.init();
        RegistryHelperImpl.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            AxeItemAccessor.setStrippables(new ImmutableMap.Builder<Block, Block>().putAll(AxeItemAccessor.getStrippables())
                    .put(ModBlocks.BAOBAB_WOOD.get(), ModBlocks.STRIPPED_BAOBAB_WOOD.get())
                    .put(ModBlocks.BAOBAB_LOG.get(), ModBlocks.STRIPPED_BAOBAB_LOG.get())
                    .build());
            FireBlock fireBlock = (FireBlock) Blocks.FIRE;
            fireBlock.setFlammable(ModBlocks.BAOBAB_LOG.get(), 5, 5);
            fireBlock.setFlammable(ModBlocks.BAOBAB_WOOD.get(), 5, 5);
            fireBlock.setFlammable(ModBlocks.STRIPPED_BAOBAB_LOG.get(), 5, 5);
            fireBlock.setFlammable(ModBlocks.STRIPPED_BAOBAB_WOOD.get(), 5, 5);
            fireBlock.setFlammable(ModBlocks.BAOBAB_PLANKS.get(), 5, 20);
            fireBlock.setFlammable(ModBlocks.BAOBAB_SLAB.get(), 5, 20);
            fireBlock.setFlammable(ModBlocks.BAOBAB_FENCE_GATE.get(), 5, 20);
            fireBlock.setFlammable(ModBlocks.BAOBAB_FENCE.get(), 5, 20);
            fireBlock.setFlammable(ModBlocks.BAOBAB_STAIRS.get(), 5, 20);
            fireBlock.setFlammable(ModBlocks.BAOBAB_LEAVES.get(), 30, 60);
        });
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            Sheets.SIGN_MATERIALS.put(ModWoodTypes.BAOBAB, SheetsInvoker.createSignMaterial(ModWoodTypes.BAOBAB));
            Sheets.HANGING_SIGN_MATERIALS.put(ModWoodTypes.BAOBAB, SheetsInvoker.createHangingSignMaterial(ModWoodTypes.BAOBAB));
        });
    }
}