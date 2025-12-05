package io.github.techtastic.kubevs.registry

import io.github.techtastic.kubevs.KubeVS.MOD_ID
import io.github.techtastic.kubevs.util.BlockTypeJS
import net.minecraft.core.Registry
import net.minecraft.world.level.block.state.BlockState
import org.valkyrienskies.core.internal.world.chunks.VsiBlockType
import org.valkyrienskies.mod.common.BlockStateInfo
import org.valkyrienskies.mod.common.BlockStateInfoProvider
import java.util.function.Function

object KubeVSBSIP : BlockStateInfoProvider {
    lateinit var MASS_CALLBACK: Function<BlockState, Double?>
    lateinit var TYPE_CALLBACK: Function<BlockState, BlockTypeJS?>
    override val priority: Int
        get() = 300

    override fun getBlockStateMass(blockState: BlockState): Double? {
        if (this::MASS_CALLBACK.isInitialized)
            return MASS_CALLBACK.apply(blockState)
        return null
    }

    override fun getBlockStateType(blockState: BlockState): VsiBlockType? {
        if (this::TYPE_CALLBACK.isInitialized)
            return TYPE_CALLBACK.apply(blockState)?.type
        return null
    }

    fun register() {
        Registry.register(BlockStateInfo.REGISTRY, MOD_ID, this)
    }
}