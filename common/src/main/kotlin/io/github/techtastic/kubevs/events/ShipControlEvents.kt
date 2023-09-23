package io.github.techtastic.kubevs.events

import org.valkyrienskies.core.impl.game.ships.PhysShipImpl
import org.valkyrienskies.core.impl.util.events.EventEmitter
import org.valkyrienskies.core.impl.util.events.EventEmitterImpl

object ShipControlEvents {
    val applyForcesEvent = EventEmitterImpl<ApplyForcesEvent>()

    data class ApplyForcesEvent(val physShip: PhysShipImpl) {
        companion object : EventEmitter<ApplyForcesEvent> by applyForcesEvent
    }
}