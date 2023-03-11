package com.kaboomroads.lostfeatures;

import com.kaboomroads.lostfeatures.block.ModBlocks;
import com.kaboomroads.lostfeatures.block.ModWoodTypes;
import com.kaboomroads.lostfeatures.block.entity.ModBlockEntities;
import com.kaboomroads.lostfeatures.entity.ModEntityTypes;
import com.kaboomroads.lostfeatures.entity.client.barnacle.BarnacleModel;
import com.kaboomroads.lostfeatures.entity.client.barnacle.BarnacleRenderer;
import com.kaboomroads.lostfeatures.entity.client.boat.ModBoatRenderer;
import com.kaboomroads.lostfeatures.entity.client.chillager.ChillagerRenderer;
import com.kaboomroads.lostfeatures.entity.client.coppergolem.CopperGolemModel;
import com.kaboomroads.lostfeatures.entity.client.coppergolem.CopperGolemRenderer;
import com.kaboomroads.lostfeatures.entity.client.icechunk.IceChunkModel;
import com.kaboomroads.lostfeatures.entity.client.icechunk.IceChunkRenderer;
import com.kaboomroads.lostfeatures.entity.client.moobloom.MoobloomRenderer;
import com.kaboomroads.lostfeatures.entity.client.ostrich.OstrichModel;
import com.kaboomroads.lostfeatures.entity.client.ostrich.OstrichRenderer;
import com.kaboomroads.lostfeatures.entity.client.tuffgolem.TuffGolemModel;
import com.kaboomroads.lostfeatures.entity.client.tuffgolem.TuffGolemRenderer;
import com.kaboomroads.lostfeatures.entity.client.wildfire.WildfireModel;
import com.kaboomroads.lostfeatures.entity.client.wildfire.WildfireRenderer;
import com.kaboomroads.lostfeatures.entity.custom.ModBoat;
import com.kaboomroads.lostfeatures.item.ModItems;
import com.kaboomroads.lostfeatures.mixin.SheetsInvoker;
import com.kaboomroads.lostfeatures.particle.ModParticles;
import com.kaboomroads.lostfeatures.particle.custom.FireflyParticle;
import com.kaboomroads.lostfeatures.particle.custom.TermiteParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class LostFeaturesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(ModParticles.FIREFLY_PARTICLE.get(), FireflyParticle.Provider::new);
        ParticleFactoryRegistry.getInstance().register(ModParticles.TERMITE_PARTICLE.get(), TermiteParticle.Provider::new);

        EntityModelLayerRegistry.registerModelLayer(BarnacleModel.LAYER_LOCATION, BarnacleModel::createBodyLayer);
        EntityRendererRegistry.register(ModEntityTypes.BARNACLE.get(), BarnacleRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(WildfireModel.LAYER_LOCATION, WildfireModel::createBodyLayer);
        EntityRendererRegistry.register(ModEntityTypes.WILDFIRE.get(), WildfireRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(IceChunkModel.LAYER_LOCATION, IceChunkModel::createBodyLayer);
        EntityRendererRegistry.register(ModEntityTypes.ICE_CHUNK.get(), IceChunkRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(CopperGolemModel.LAYER_LOCATION, CopperGolemModel::createBodyLayer);
        EntityRendererRegistry.register(ModEntityTypes.COPPER_GOLEM.get(), CopperGolemRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(TuffGolemModel.LAYER_LOCATION, TuffGolemModel::createBodyLayer);
        EntityRendererRegistry.register(ModEntityTypes.TUFF_GOLEM.get(), TuffGolemRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(OstrichModel.LAYER_LOCATION, OstrichModel::createBodyLayer);
        EntityRendererRegistry.register(ModEntityTypes.OSTRICH.get(), OstrichRenderer::new);

        BlockEntityRenderers.register(ModBlockEntities.SIGN_BLOCK_ENTITIES.get(), SignRenderer::new);
        BlockEntityRenderers.register(ModBlockEntities.HANGING_SIGN_BLOCK_ENTITIES.get(), HangingSignRenderer::new);

        LayerDefinition boatModel = BoatModel.createBodyModel();
        LayerDefinition chestBoatModel = ChestBoatModel.createBodyModel();
        for (ModBoat.Type type : ModBoat.Type.values()) {
            EntityModelLayerRegistry.registerModelLayer(ModBoatRenderer.createBoatModelName(type, false), () -> boatModel);
            EntityModelLayerRegistry.registerModelLayer(ModBoatRenderer.createBoatModelName(type, true), () -> chestBoatModel);
        }

        EntityRendererRegistry.register(ModEntityTypes.BOAT.get(), context -> new ModBoatRenderer(context, false, false));
        EntityRendererRegistry.register(ModEntityTypes.CHEST_BOAT.get(), context -> new ModBoatRenderer(context, true, false));

        EntityRendererRegistry.register(ModEntityTypes.MOOBLOOM.get(), MoobloomRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.CHILLAGER.get(), ChillagerRenderer::new);

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(),
                ModBlocks.BAOBAB_LEAVES.get(),
                ModBlocks.BAOBAB_SAPLING.get(),
                ModBlocks.BAOBAB_DOOR.get(),
                ModBlocks.BAOBAB_TRAPDOOR.get(),
                ModBlocks.TERMITE_SPIRES.get()
        );
        Sheets.SIGN_MATERIALS.put(ModWoodTypes.BAOBAB, SheetsInvoker.createSignMaterial(ModWoodTypes.BAOBAB));
        Sheets.HANGING_SIGN_MATERIALS.put(ModWoodTypes.BAOBAB, SheetsInvoker.createHangingSignMaterial(ModWoodTypes.BAOBAB));
        inventory();
    }

    public static void inventory() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register(entries -> entries.addAfter(Items.PHANTOM_SPAWN_EGG,
                ModItems.BARNACLE_SPAWN_EGG.get(),
                ModItems.WILDFIRE_SPAWN_EGG.get(),
                ModItems.MOOBLOOM_SPAWN_EGG.get(),
                ModItems.CHILLAGER_SPAWN_EGG.get(),
                ModItems.COPPER_GOLEM_SPAWN_EGG.get(),
                ModItems.TUFF_GOLEM_SPAWN_EGG.get(),
                ModItems.OSTRICH_SPAWN_EGG.get()
        ));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries -> entries.addAfter(Blocks.SCULK_CATALYST, ModBlocks.SCULK_JAW.get()));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries -> entries.addAfter(Blocks.SCULK_SENSOR,
                ModBlocks.TERMITE_NEST_CORE.get(),
                ModBlocks.TERMITE_NEST.get(),
                ModBlocks.TERMITE_SPIRES.get()
        ));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> entries.addAfter(Items.WAXED_OXIDIZED_CUT_COPPER_SLAB, ModBlocks.COPPER_BUTTON.get()));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.REDSTONE_BLOCKS).register(entries -> entries.addAfter(Items.STONE_BUTTON, ModBlocks.COPPER_BUTTON.get()));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> entries.addAfter(Items.MANGROVE_BUTTON,
                ModBlocks.BAOBAB_LOG.get(),
                ModBlocks.BAOBAB_WOOD.get(),
                ModBlocks.STRIPPED_BAOBAB_LOG.get(),
                ModBlocks.STRIPPED_BAOBAB_WOOD.get(),
                ModBlocks.BAOBAB_PLANKS.get(),
                ModBlocks.BAOBAB_STAIRS.get(),
                ModBlocks.BAOBAB_SLAB.get(),
                ModBlocks.BAOBAB_FENCE.get(),
                ModBlocks.BAOBAB_FENCE_GATE.get(),
                ModBlocks.BAOBAB_DOOR.get(),
                ModBlocks.BAOBAB_TRAPDOOR.get(),
                ModBlocks.BAOBAB_PRESSURE_PLATE.get(),
                ModBlocks.BAOBAB_BUTTON.get()
        ));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> entries.addAfter(
                CreativeModeTabs.CACHED_ENABLED_FEATURES != null && Items.WARPED_HANGING_SIGN.isEnabled(CreativeModeTabs.CACHED_ENABLED_FEATURES)
                        ? Items.WARPED_HANGING_SIGN
                        : Items.WARPED_SIGN,
                ModItems.BAOBAB_SIGN.get(),
                ModItems.BAOBAB_HANGING_SIGN.get()
        ));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> entries.addAfter(Items.MANGROVE_CHEST_BOAT,
                ModItems.BAOBAB_BOAT.get(),
                ModItems.BAOBAB_CHEST_BOAT.get()
        ));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries -> entries.addAfter(Items.FLOWERING_AZALEA_LEAVES, ModBlocks.BAOBAB_LEAVES.get()));
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.NATURAL_BLOCKS).register(entries -> entries.addAfter(Items.MANGROVE_PROPAGULE, ModBlocks.BAOBAB_SAPLING.get()));
    }
}
