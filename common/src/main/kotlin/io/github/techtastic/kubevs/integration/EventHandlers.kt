package io.github.techtastic.kubevs.integration

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl
import org.valkyrienskies.core.impl.util.events.EventEmitter
import org.valkyrienskies.core.impl.util.events.EventEmitterImpl

object EventHandlers {
    val applyForcesEvent = EventEmitterImpl<ApplyForcesEvent>()

    data class ApplyForcesEvent(val physShip: PhysShipImpl) {
        companion object : EventEmitter<ApplyForcesEvent> by applyForcesEvent
    }
}