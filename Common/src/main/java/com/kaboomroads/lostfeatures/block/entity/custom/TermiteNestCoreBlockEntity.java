package com.kaboomroads.lostfeatures.block.entity.custom;

import com.kaboomroads.lostfeatures.block.entity.ModBlockEntities;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class TermiteNestCoreBlockEntity extends BlockEntity {
    public HashMap<BlockPos, BlockState> largerNest;
    public LinkedList<BlockState> types = new LinkedList<>();

    public TermiteNestCoreBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TERMITE_NEST_CORE.get(), blockPos, blockState);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        ListTag nestTag = new ListTag();
        if (largerNest != null && !largerNest.isEmpty())
            for (Map.Entry<BlockPos, BlockState> entry : largerNest.entrySet())
                nestTag.add(savePosState(entry.getKey(), entry.getValue()));
        tag.put("LargerNest", nestTag);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        if ((largerNest == null || largerNest.isEmpty()) && tag.contains("LargerNest")) {
            HashMap<BlockPos, BlockState> map = new HashMap<>();
            ListTag nestTag = tag.getList("LargerNest", Tag.TAG_COMPOUND);
            for (int i = 0; i < nestTag.size(); i++) {
                CompoundTag posState = nestTag.getCompound(i);
                Pair<BlockPos, BlockState> pair = readPosState(posState);
                BlockPos pos = pair.getFirst();
                BlockState state = pair.getSecond();
                map.put(pos, state);
                if (!types.contains(state)) types.add(state);
            }
            largerNest = map;
        }
    }

    private CompoundTag savePosState(BlockPos blockPos, BlockState blockState) {
        CompoundTag tag = new CompoundTag();
        tag.put("Pos", NbtUtils.writeBlockPos(blockPos));
        tag.put("State", NbtUtils.writeBlockState(blockState));
        return tag;
    }

    private Pair<BlockPos, BlockState> readPosState(@NotNull CompoundTag tag) {
        HolderGetter<Block> holdergetter = this.level != null ? this.level.holderLookup(Registries.BLOCK) : BuiltInRegistries.BLOCK.asLookup();
        return new Pair<>(NbtUtils.readBlockPos(tag.getCompound("Pos")), NbtUtils.readBlockState(holdergetter, tag.getCompound("State")));
    }
}
