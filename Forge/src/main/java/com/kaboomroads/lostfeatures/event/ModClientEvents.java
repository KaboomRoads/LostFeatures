package com.kaboomroads.lostfeatures.event;

import com.kaboomroads.lostfeatures.Constants;
import com.kaboomroads.lostfeatures.block.ModBlocks;
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
import com.kaboomroads.lostfeatures.particle.ModParticles;
import com.kaboomroads.lostfeatures.particle.custom.FireflyParticle;
import com.kaboomroads.lostfeatures.particle.custom.TermiteParticle;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.world.item.*;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientEvents {
    @SubscribeEvent
    public static void onCreativeModeTab(CreativeModeTabEvent.BuildContents event) {
        if (event.getTab() == CreativeModeTabs.NATURAL_BLOCKS) {
            add(event, Blocks.SCULK_CATALYST, ModBlocks.SCULK_JAW.get());
            add(event, Blocks.CACTUS, ModBlocks.BADLANDS_CACTUS.get());
            add(event, Blocks.FLOWERING_AZALEA_LEAVES, ModBlocks.BAOBAB_LEAVES.get());
            add(event, Blocks.MANGROVE_PROPAGULE, ModBlocks.BAOBAB_SAPLING.get());
            add(event, Blocks.SCULK_SENSOR, ModBlocks.TERMITE_NEST_CORE.get());
            add(event, ModBlocks.TERMITE_NEST_CORE.get(), ModBlocks.TERMITE_NEST.get());
            add(event, ModBlocks.TERMITE_NEST.get(), ModBlocks.TERMITE_SPIRES.get());
        }
        if (event.getTab() == CreativeModeTabs.BUILDING_BLOCKS) {
            add(event, Blocks.WAXED_OXIDIZED_CUT_COPPER_SLAB, ModBlocks.COPPER_BUTTON.get());
            add(event, Items.MANGROVE_BUTTON, ModBlocks.BAOBAB_LOG.get());
            add(event, ModBlocks.BAOBAB_LOG.get(), ModBlocks.BAOBAB_WOOD.get());
            add(event, ModBlocks.BAOBAB_WOOD.get(), ModBlocks.STRIPPED_BAOBAB_LOG.get());
            add(event, ModBlocks.STRIPPED_BAOBAB_LOG.get(), ModBlocks.STRIPPED_BAOBAB_WOOD.get());
            add(event, ModBlocks.STRIPPED_BAOBAB_WOOD.get(), ModBlocks.BAOBAB_PLANKS.get());
            add(event, ModBlocks.BAOBAB_PLANKS.get(), ModBlocks.BAOBAB_STAIRS.get());
            add(event, ModBlocks.BAOBAB_STAIRS.get(), ModBlocks.BAOBAB_SLAB.get());
            add(event, ModBlocks.BAOBAB_SLAB.get(), ModBlocks.BAOBAB_FENCE.get());
            add(event, ModBlocks.BAOBAB_FENCE.get(), ModBlocks.BAOBAB_FENCE_GATE.get());
            add(event, ModBlocks.BAOBAB_FENCE_GATE.get(), ModBlocks.BAOBAB_DOOR.get());
            add(event, ModBlocks.BAOBAB_DOOR.get(), ModBlocks.BAOBAB_TRAPDOOR.get());
            add(event, ModBlocks.BAOBAB_TRAPDOOR.get(), ModBlocks.BAOBAB_PRESSURE_PLATE.get());
            add(event, ModBlocks.BAOBAB_PRESSURE_PLATE.get(), ModBlocks.BAOBAB_BUTTON.get());
        }
        if (event.getTab() == CreativeModeTabs.REDSTONE_BLOCKS)
            add(event, Blocks.STONE_BUTTON, ModBlocks.COPPER_BUTTON.get());
        if (event.getTab() == CreativeModeTabs.SPAWN_EGGS) {
            add(event, Items.PHANTOM_SPAWN_EGG, ModItems.BARNACLE_SPAWN_EGG.get());
            add(event, ModItems.BARNACLE_SPAWN_EGG.get(), ModItems.WILDFIRE_SPAWN_EGG.get());
            add(event, ModItems.WILDFIRE_SPAWN_EGG.get(), ModItems.MOOBLOOM_SPAWN_EGG.get());
            add(event, ModItems.MOOBLOOM_SPAWN_EGG.get(), ModItems.CHILLAGER_SPAWN_EGG.get());
            add(event, ModItems.CHILLAGER_SPAWN_EGG.get(), ModItems.COPPER_GOLEM_SPAWN_EGG.get());
            add(event, ModItems.COPPER_GOLEM_SPAWN_EGG.get(), ModItems.TUFF_GOLEM_SPAWN_EGG.get());
            add(event, ModItems.TUFF_GOLEM_SPAWN_EGG.get(), ModItems.OSTRICH_SPAWN_EGG.get());
        }
    }

    public static void add(CreativeModeTabEvent.BuildContents event, ItemLike after, ItemLike item) {
        event.getEntries().putAfter(new ItemStack(after), new ItemStack(item), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
    }

    @SubscribeEvent
    public static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((blockState, tintGetter, blockPos, i) -> tintGetter != null && blockPos != null ? BiomeColors.getAverageFoliageColor(tintGetter, blockPos) : FoliageColor.getDefaultColor(), ModBlocks.BAOBAB_LEAVES.get());
    }

    @SubscribeEvent
    public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((itemStack, i) -> {
            Block blockItem = ((BlockItem) itemStack.getItem()).getBlock();
            BlockState blockstate = blockItem.defaultBlockState();
            return event.getBlockColors().getColor(blockstate, null, null, i);
        }, ModBlocks.BAOBAB_LEAVES.get());
    }

    @SubscribeEvent
    public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.FIREFLY_PARTICLE.get(), FireflyParticle.Provider::new);
        event.registerSpriteSet(ModParticles.TERMITE_PARTICLE.get(), TermiteParticle.Provider::new);
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(BarnacleModel.LAYER_LOCATION, BarnacleModel::createBodyLayer);
        event.registerLayerDefinition(WildfireModel.LAYER_LOCATION, WildfireModel::createBodyLayer);
        event.registerLayerDefinition(IceChunkModel.LAYER_LOCATION, IceChunkModel::createBodyLayer);
        event.registerLayerDefinition(CopperGolemModel.LAYER_LOCATION, CopperGolemModel::createBodyLayer);
        event.registerLayerDefinition(TuffGolemModel.LAYER_LOCATION, TuffGolemModel::createBodyLayer);
        event.registerLayerDefinition(OstrichModel.LAYER_LOCATION, OstrichModel::createBodyLayer);
        LayerDefinition boatModel = BoatModel.createBodyModel();
        LayerDefinition chestBoatModel = ChestBoatModel.createBodyModel();
        for (ModBoat.Type type : ModBoat.Type.values()) {
            event.registerLayerDefinition(ModBoatRenderer.createBoatModelName(type, false), () -> boatModel);
            event.registerLayerDefinition(ModBoatRenderer.createBoatModelName(type, true), () -> chestBoatModel);
        }
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.BARNACLE.get(), BarnacleRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.WILDFIRE.get(), WildfireRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.MOOBLOOM.get(), MoobloomRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.CHILLAGER.get(), ChillagerRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.ICE_CHUNK.get(), IceChunkRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.COPPER_GOLEM.get(), CopperGolemRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.TUFF_GOLEM.get(), TuffGolemRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.OSTRICH.get(), OstrichRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.SIGN_BLOCK_ENTITIES.get(), SignRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.HANGING_SIGN_BLOCK_ENTITIES.get(), HangingSignRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.BOAT.get(), context -> new ModBoatRenderer(context, false, false));
        event.registerEntityRenderer(ModEntityTypes.CHEST_BOAT.get(), context -> new ModBoatRenderer(context, true, false));
    }
}
