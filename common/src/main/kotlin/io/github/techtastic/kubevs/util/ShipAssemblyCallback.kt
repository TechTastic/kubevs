package io.github.techtastic.kubevs.util

import dev.latvian.mods.rhino.util.DynamicFunction
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import org.valkyrienskies.core.impl.datastructures.DenseBlockPosSet
import org.valkyrienskies.mod.common.assembly.createNewShipWithBlocks

class ShipAssemblyCallback: DynamicFunction.Callback {
    override fun call(args: Array<out Any>): Any {
        return createNewShipWithBlocks(args[0] as BlockPos, args[2] as DenseBlockPosSet, args[3] as ServerLevel)
    }
}