package io.github.techtastic.kubevs.util

import org.valkyrienskies.core.apigame.world.chunks.BlockType
import org.valkyrienskies.core.impl.game.BlockTypeImpl

enum class BlockTypeJS(val type: BlockType?) {
    AIR(BlockTypeImpl.AIR),
    SOLID(BlockTypeImpl.SOLID),
    WATER(BlockTypeImpl.WATER),
    LAVA(BlockTypeImpl.LAVA),
    NONE(null);
}