package io.github.techtastic.kubevs.registration

import io.github.techtastic.kubevs.KubeVSMod
import io.github.techtastic.kubevs.events.KubeVSEvents
import io.github.techtastic.kubevs.util.BlockTypeJS
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.state.BlockState
import org.valkyrienskies.core.apigame.world.chunks.BlockType
import org.valkyrienskies.mod.common.BlockStateInfo
import org.valkyrienskies.mod.common.BlockStateInfoProvider
import org.valkyrienskies.physics_api.Lod1BlockStateId
import org.valkyrienskies.physics_api.Lod1LiquidBlockStateId
import org.valkyrienskies.physics_api.Lod1SolidBlockStateId
import org.valkyrienskies.physics_api.voxel.Lod1LiquidBlockState
import org.valkyrienskies.physics_api.voxel.Lod1SolidBlockState
import java.util.function.Predicate

object KubeVSBlockStateInfoProvider: BlockStateInfoProvider {
    private val TESTCASES = mutableMapOf<Predicate<BlockState>, Pair<Double?, BlockTypeJS>>()
    override val blockStateData: List<Triple<Lod1SolidBlockStateId, Lod1LiquidBlockStateId, Lod1BlockStateId>>
        get() = listOf()
    override val liquidBlockStates: List<Lod1LiquidBlockState>
        get() = listOf()

    override val priority: Int
        get() = 300
    override val solidBlockStates: List<Lod1SolidBlockState>
        get() = listOf()

    override fun getBlockStateMass(blockState: BlockState): Double? {
        KubeVSEvents.blockStateInfoEvent.emit(KubeVSEvents.BlockStateInfoEvent(this))
        TESTCASES.forEach {
            val (predicate, pair) = it
            if (predicate.test(blockState))
                return pair.first
        }
        return null
    }

    override fun getBlockStateType(blockState: BlockState): BlockType? {
        KubeVSEvents.blockStateInfoEvent.emit(KubeVSEvents.BlockStateInfoEvent(this))
        TESTCASES.forEach {
            val (predicate, pair) = it
            if (predicate.test(blockState))
                return pair.second.type
        }
        return null
    }

    fun addPredicateAndValues(predicate: Predicate<BlockState>, mass: Double?, type: BlockTypeJS) {
        if (!this.TESTCASES.containsKey(predicate))
            this.TESTCASES[predicate] = Pair(mass, type)
    }

    fun register() {
        Registry.register(BlockStateInfo.REGISTRY, ResourceLocation(KubeVSMod.MOD_ID, "script"), this)
    }
}