package io.github.techtastic.kubevs.registration

import dev.latvian.mods.kubejs.util.ConsoleJS
import io.github.techtastic.kubevs.util.BlockTypeJS
import net.minecraft.world.level.block.state.BlockState
import org.valkyrienskies.core.apigame.world.chunks.BlockType
import org.valkyrienskies.mod.common.BlockStateInfoProvider

data class BlockStateInfoProviderJS(val id: String, val sortPriority: Int?, val mass: MassCallback?, val type: TypeCallback?): BlockStateInfoProvider {
    @FunctionalInterface
    interface MassCallback {
        fun getMassFromState(state: BlockState): Double
    }

    @FunctionalInterface
    interface TypeCallback {
        fun getTypeFromState(state: BlockState): BlockTypeJS
    }

    override val priority: Int
        get() = this.sortPriority ?: 200

    override fun getBlockStateMass(blockState: BlockState): Double? {
        val mass = this.mass?.getMassFromState(blockState)
        ConsoleJS.SERVER.log("Called upon Mass! $mass")
        return mass
    }

    override fun getBlockStateType(blockState: BlockState): BlockType? =
            this.type?.getTypeFromState(blockState)?.type
}
