package com.kaboomroads.lostfeatures.block;

import com.kaboomroads.lostfeatures.block.custom.*;
import com.kaboomroads.lostfeatures.mixin.*;
import com.kaboomroads.lostfeatures.platform.Services;
import com.kaboomroads.lostfeatures.worldgen.tree.BaobabTreeGrower;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.util.function.Supplier;

public class ModBlocks {
    public static final Supplier<Block> SCULK_JAW = Services.REGISTRY.registerBlockAndItem("sculk_jaw", () ->
            new SculkJawBlock(BlockBehaviour.Properties.of(Material.SCULK, MaterialColor.COLOR_CYAN).strength(1.5F).sound(SoundType.SCULK_SENSOR)));
    public static final Supplier<Block> COPPER_BUTTON = Services.REGISTRY.registerBlockAndItem("copper_button", () ->
            ButtonBlockInvoker.invokeInit(BlockBehaviour.Properties.of(Material.DECORATION).noCollission().sound(SoundType.STONE), ModBlockSetTypes.BAOBAB, 20, false));

    public static final Supplier<Block> STRIPPED_BAOBAB_LOG = Services.REGISTRY.registerBlockAndItem("stripped_baobab_log", () ->
            log(MaterialColor.WOOD, MaterialColor.WOOD));
    public static final Supplier<Block> BAOBAB_LOG = Services.REGISTRY.registerBlockAndItem("baobab_log", () ->
            log(MaterialColor.WOOD, MaterialColor.PODZOL));
    public static final Supplier<Block> STRIPPED_BAOBAB_WOOD = Services.REGISTRY.registerBlockAndItem("stripped_baobab_wood", () ->
            log(MaterialColor.WOOD, MaterialColor.WOOD));
    public static final Supplier<Block> BAOBAB_WOOD = Services.REGISTRY.registerBlockAndItem("baobab_wood", () ->
            log(MaterialColor.PODZOL, MaterialColor.PODZOL));
    public static final Supplier<Block> BAOBAB_SAPLING = Services.REGISTRY.registerBlockAndItem("baobab_sapling", () ->
            SaplingBlockInvoker.invokeInit(new BaobabTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));
    public static final Supplier<Block> BAOBAB_LEAVES = Services.REGISTRY.registerBlockAndItem("baobab_leaves", () ->
            leaves(SoundType.GRASS));

    public static final Supplier<Block> BAOBAB_PLANKS = Services.REGISTRY.registerBlockAndItem("baobab_planks", () ->
            new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final Supplier<Block> BAOBAB_STAIRS = Services.REGISTRY.registerBlockAndItem("baobab_stairs", () ->
            Services.REGISTRY.getStairBlock(() -> BAOBAB_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)));
    public static final Supplier<Block> BAOBAB_SLAB = Services.REGISTRY.registerBlockAndItem("baobab_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));
    public static final Supplier<Block> BAOBAB_FENCE = Services.REGISTRY.registerBlockAndItem("baobab_fence", () ->
            new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));
    public static final Supplier<Block> BAOBAB_FENCE_GATE = Services.REGISTRY.registerBlockAndItem("baobab_fence_gate", () ->
            new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), ModWoodTypes.BAOBAB));
    public static final Supplier<Block> BAOBAB_DOOR = Services.REGISTRY.registerBlockAndItem("baobab_door", () ->
            DoorBlockInvoker.invokeInit(BlockBehaviour.Properties.of(Material.WOOD, BAOBAB_PLANKS.get().defaultMaterialColor()).noOcclusion(), ModBlockSetTypes.BAOBAB));
    public static final Supplier<Block> BAOBAB_TRAPDOOR = Services.REGISTRY.registerBlockAndItem("baobab_trapdoor", () ->
            TrapDoorBlockInvoker.invokeInit(BlockBehaviour.Properties.of(Material.WOOD, BAOBAB_PLANKS.get().defaultMaterialColor()).noOcclusion().isValidSpawn(ModBlocks::never), ModBlockSetTypes.BAOBAB));
    public static final Supplier<Block> BAOBAB_BUTTON = Services.REGISTRY.registerBlockAndItem("baobab_button", () ->
            ButtonBlockInvoker.invokeInit(BlockBehaviour.Properties.of(Material.DECORATION).noCollission(), ModBlockSetTypes.BAOBAB, 30, true));
    public static final Supplier<Block> BAOBAB_PRESSURE_PLATE = Services.REGISTRY.registerBlockAndItem("baobab_pressure_plate", () ->
            PressurePlateBlockInvoker.invokeInit(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.of(Material.WOOD, BAOBAB_PLANKS.get().defaultMaterialColor()).noCollission(), ModBlockSetTypes.BAOBAB));
    public static final Supplier<Block> BAOBAB_SIGN = Services.REGISTRY.registerBlock("baobab_sign", () ->
            new ModStandingSignBlock(BlockBehaviour.Properties.of(Material.WOOD, BAOBAB_PLANKS.get().defaultMaterialColor()).noCollission(), ModWoodTypes.BAOBAB));
    public static final Supplier<Block> BAOBAB_WALL_SIGN = Services.REGISTRY.registerBlock("baobab_wall_sign", () ->
            new ModWallSignBlock(BlockBehaviour.Properties.of(Material.WOOD, BAOBAB_PLANKS.get().defaultMaterialColor()).noCollission().dropsLike(BAOBAB_SIGN.get()), ModWoodTypes.BAOBAB));
    public static final Supplier<Block> BAOBAB_HANGING_SIGN = Services.REGISTRY.registerBlock("baobab_hanging_sign", () ->
            new ModCeilingHangingSignBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_CYAN).noCollission().sound(SoundType.HANGING_SIGN), ModWoodTypes.BAOBAB));
    public static final Supplier<Block> BAOBAB_WALL_HANGING_SIGN = Services.REGISTRY.registerBlock("baobab_wall_hanging_sign", () ->
            new ModWallHangingSignBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_CYAN).noCollission().sound(SoundType.HANGING_SIGN).dropsLike(BAOBAB_HANGING_SIGN.get()), ModWoodTypes.BAOBAB));

    public static final Supplier<Block> TERMITE_NEST_CORE = Services.REGISTRY.registerBlockAndItem("termite_nest_core", () ->
            new TermiteNestCoreBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.PODZOL).requiresCorrectToolForDrops().strength(2.0F, 1.0F)));
    public static final Supplier<Block> TERMITE_NEST = Services.REGISTRY.registerBlockAndItem("termite_nest", () ->
            new TermiteNestBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.PODZOL).requiresCorrectToolForDrops().strength(1.0F, 0.5F)));
    public static final Supplier<Block> TERMITE_SPIRES = Services.REGISTRY.registerBlockAndItem("termite_spires", () ->
            new TermiteSpiresBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.PODZOL).noCollission().requiresCorrectToolForDrops().strength(0.75F, 0.5F)));
    public static final Supplier<Block> BADLANDS_CACTUS = Services.REGISTRY.registerBlockAndItem("badlands_cactus", () ->
            new BadlandsCactusBlock(BlockBehaviour.Properties.of(Material.CACTUS).randomTicks().strength(0.4F).sound(SoundType.WOOL)));

    private static LeavesBlock leaves(SoundType soundType) {
        return new LeavesBlock(BlockBehaviour.Properties.of(Material.LEAVES).strength(0.2F).randomTicks().sound(soundType).noOcclusion().isValidSpawn(ModBlocks::ocelotOrParrot).isSuffocating(ModBlocks::never).isViewBlocking(ModBlocks::never));
    }

    private static RotatedPillarBlock log(MaterialColor color1, MaterialColor color2) {
        return new RotatedPillarBlock(BlockBehaviour.Properties.of(Material.WOOD, blockState -> blockState.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? color1 : color2).strength(2.0F).sound(SoundType.WOOD));
    }

    private static Boolean ocelotOrParrot(BlockState $$0, BlockGetter $$1, BlockPos $$2, EntityType<?> $$3) {
        return $$3 == EntityType.OCELOT || $$3 == EntityType.PARROT;
    }

    private static boolean never(BlockState $$0, BlockGetter $$1, BlockPos $$2) {
        return false;
    }

    private static Boolean never(BlockState $$0, BlockGetter $$1, BlockPos $$2, EntityType<?> $$3) {
        return false;
    }

    public static void init() {
    }
}