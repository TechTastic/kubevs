package io.github.techtastic.kubevs.registry

import io.github.techtastic.kubevs.KubeVS.MOD_ID
import io.github.techtastic.kubevs.util.BlockTypeJS
import net.minecraft.core.Registry
import net.minecraft.world.level.block.state.BlockState
import org.valkyrienskies.core.apigame.world.chunks.BlockType
import org.valkyrienskies.mod.common.BlockStateInfo
import org.valkyrienskies.mod.common.BlockStateInfoProvider
import org.valkyrienskies.physics_api.Lod1BlockStateId
import org.valkyrienskies.physics_api.Lod1LiquidBlockStateId
import org.valkyrienskies.physics_api.Lod1SolidBlockStateId
import org.valkyrienskies.physics_api.voxel.Lod1LiquidBlockState
import org.valkyrienskies.physics_api.voxel.Lod1SolidBlockState
import java.util.function.Function

object KubeVSBSIP : BlockStateInfoProvider {
    override val blockStateData: List<Triple<Lod1SolidBlockStateId, Lod1LiquidBlockStateId, Lod1BlockStateId>>
        get() = listOf()
    override val liquidBlockStates: List<Lod1LiquidBlockState>
        get() = listOf()
    override val priority: Int
        get() = 300
    override val solidBlockStates: List<Lod1SolidBlockState>
        get() = listOf()

    lateinit var MASS_CALLBACK: Function<BlockState, Double?>
    lateinit var TYPE_CALLBACK: Function<BlockState, BlockTypeJS?>

    override fun getBlockStateMass(blockState: BlockState): Double? {
        if (this::MASS_CALLBACK.isInitialized)
            return MASS_CALLBACK.apply(blockState)
        return null
    }

    override fun getBlockStateType(blockState: BlockState): BlockType? {
        if (this::TYPE_CALLBACK.isInitialized)
            return TYPE_CALLBACK.apply(blockState)?.type
        return null
    }

    fun register() {
        Registry.register(BlockStateInfo.REGISTRY, MOD_ID, this)
    }
}