package io.github.techtastic.kubevs.util

import net.minecraft.world.level.block.state.BlockState
import org.valkyrienskies.mod.common.BlockStateInfo

object BlockStateInfoJS {
    fun getMass(state: BlockState) =
            BlockStateInfo.get(state)?.first

    fun getType(state: BlockState) =
            BlockStateInfo.get(state)?.second
}