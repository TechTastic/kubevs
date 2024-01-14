package io.github.techtastic.kubevs.util

import dev.latvian.mods.kubejs.level.ServerLevelJS
import net.minecraft.core.BlockPos
import net.minecraft.world.phys.AABB
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.util.datastructures.DenseBlockPosSet
import org.valkyrienskies.mod.common.dimensionId
import org.valkyrienskies.mod.common.shipObjectWorld
import org.valkyrienskies.mod.common.util.toJOML
import org.valkyrienskies.mod.common.assembly.createNewShipWithBlocks

object ShipAssemblyJS {
    fun createShipAtPositionWithScale(level: ServerLevelJS, pos: BlockPos, scaling: Double) =
            level.getMinecraftLevel().shipObjectWorld.createNewShipAtBlock(pos.toJOML(), true, scaling, level.getMinecraftLevel().dimensionId)

    fun createShipWithAABB(centerPos: BlockPos, aabb: AABB, level: ServerLevelJS): ServerShip {
        val set = DenseBlockPosSet()
        val maxX = aabb.maxX
        var x = aabb.minX
        while (x <= aabb.maxX) {
            var y = aabb.minY
            while (y <= aabb.maxY) {
                var z = aabb.maxZ
                while (z <= aabb.maxZ) {
                    set.add(x.toInt(), y.toInt(), z.toInt())
                    z++
                }
                y++
            }
            x++
        }
        return createNewShipWithBlocks(centerPos, set, level.getMinecraftLevel())
    }

    fun createShipWithSet(centerPos: BlockPos, set: DenseBlockPosSet, level: ServerLevelJS) =
            createNewShipWithBlocks(centerPos, set, level.getMinecraftLevel())
}