package io.github.techtastic.kubevs.registration

import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import org.valkyrienskies.mod.common.BlockStateInfo
import org.valkyrienskies.mod.common.BlockStateInfoProvider

object KubeVSBuilderTypes {
    val BLOCK_INFO_PROVIDER = RegistryObjectBuilderTypes.add(BlockStateInfo.REGISTRY.key() as ResourceKey<Registry<BlockStateInfoProvider>>, BlockStateInfoProvider::class.java)
}