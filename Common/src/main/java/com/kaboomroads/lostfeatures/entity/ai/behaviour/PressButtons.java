package com.kaboomroads.lostfeatures.entity.ai.behaviour;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class PressButtons {
    public static <E extends Mob> OneShot<E> create(MemoryModuleType<BlockPos> posMemory, float buttonRangeSqr, int closeRange, float speedModifier) {
        return BehaviorBuilder.create(instance -> instance.group(instance.present(posMemory), instance.registered(MemoryModuleType.WALK_TARGET), instance.registered(MemoryModuleType.LOOK_TARGET), instance.registered(MemoryModuleType.ATTACK_COOLING_DOWN)).apply(instance, (posAccessor, walkTargetAccessor, lookTargetAccessor, coolingDownAccessor) -> (level, entity, l) -> {
            Brain<?> brain = entity.getBrain();
            BlockPos blockPos = instance.get(posAccessor);
            BlockState blockState = level.getBlockState(blockPos);
            boolean closerThan = blockPos.closerThan(entity.blockPosition(), closeRange);
            if (!closerThan) BehaviorUtils.setWalkAndLookTargetMemories(entity, blockPos, speedModifier, closeRange);
            if (brain.getMemory(MemoryModuleType.ATTACK_COOLING_DOWN).isEmpty()) {
                double distSqr = entity.distanceToSqr(blockPos.getX() + 0.5f, blockPos.getY() + 0.5f, blockPos.getZ() + 0.5f);
                if (distSqr <= buttonRangeSqr && blockState.getBlock() instanceof ButtonBlock buttonBlock && !blockState.getValue(ButtonBlock.POWERED)) {
                    brain.setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, 20L);
                    brain.eraseMemory(posMemory);
                    entity.level().broadcastEntityEvent(entity, (byte) 64);
                    buttonBlock.press(blockState, level, blockPos);
                    entity.level().gameEvent(entity, GameEvent.BLOCK_ACTIVATE, blockPos);
                    return true;
                }
            }
            return true;
        }));
    }
}
