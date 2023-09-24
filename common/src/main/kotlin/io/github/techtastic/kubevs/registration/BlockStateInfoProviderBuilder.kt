package io.github.techtastic.kubevs.registration

import dev.latvian.mods.kubejs.BuilderBase
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.state.BlockState
import org.valkyrienskies.core.apigame.world.chunks.BlockType
import org.valkyrienskies.core.impl.game.BlockTypeImpl
import org.valkyrienskies.mod.common.BlockStateInfoProvider

class BlockStateInfoProviderBuilder(i: ResourceLocation) : BuilderBase<BlockStateInfoProvider>(i) {
    @FunctionalInterface
    interface MassProvider {
        fun inputState(state: BlockState): Double?
    }

    @FunctionalInterface
    interface TypeProvider {
        fun inputState(state: BlockState): Type
    }

    enum class Type(val type: BlockType?) {
        AIR(BlockTypeImpl.AIR),
        SOLID(BlockTypeImpl.SOLID),
        WATER(BlockTypeImpl.WATER),
        LAVA(BlockTypeImpl.LAVA),
        NONE(null);
    }

    var sortPriority: Int = 100
    var massProvider: MassProvider = object : MassProvider {
        override fun inputState(state: BlockState): Double? = null

    }
    var typeProvider: TypeProvider = object : TypeProvider {
        override fun inputState(state: BlockState): Type = Type.NONE

    }

    override fun getRegistryType(): RegistryObjectBuilderTypes<in BlockStateInfoProvider> =
            KubeVSBuilderTypes.BLOCK_INFO_PROVIDER

    override fun createObject(): BlockStateInfoProvider = object : BlockStateInfoProvider {
        override val priority: Int
            get() = sortPriority

        override fun getBlockStateMass(blockState: BlockState): Double? =
                massProvider.inputState(blockState)

        override fun getBlockStateType(blockState: BlockState): BlockType? =
                typeProvider.inputState(blockState).type
    }

    fun priority(priority: Int): BlockStateInfoProviderBuilder {
        this.sortPriority = priority
        return this
    }

    fun mass(provider: MassProvider): BlockStateInfoProviderBuilder {
        this.massProvider = provider
        return this
    }

    fun type(provider: TypeProvider): BlockStateInfoProviderBuilder {
        this.typeProvider = provider
        return this
    }
}