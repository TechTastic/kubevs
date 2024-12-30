package io.github.techtastic.kubevs.util

import org.valkyrienskies.core.apigame.world.chunks.BlockType
import org.valkyrienskies.mod.common.vsCore

enum class BlockTypeJS(val type: BlockType?) {
    AIR(vsCore.blockTypes.air),
    SOLID(vsCore.blockTypes.solid),
    WATER(vsCore.blockTypes.water),
    LAVA(vsCore.blockTypes.lava)
}