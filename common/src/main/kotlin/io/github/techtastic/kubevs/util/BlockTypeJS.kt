package io.github.techtastic.kubevs.util

import org.valkyrienskies.core.internal.world.chunks.VsiBlockType
import org.valkyrienskies.mod.common.ValkyrienSkiesMod

enum class BlockTypeJS(val type: VsiBlockType?) {
    AIR(ValkyrienSkiesMod.vsCore.blockTypes.air),
    SOLID(ValkyrienSkiesMod.vsCore.blockTypes.solid),
    WATER(ValkyrienSkiesMod.vsCore.blockTypes.water),
    LAVA(ValkyrienSkiesMod.vsCore.blockTypes.lava),
    NONE(null)
}