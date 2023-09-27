package io.github.techtastic.kubevs.events

import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl
import org.valkyrienskies.core.impl.util.events.EventEmitter
import org.valkyrienskies.core.impl.util.events.EventEmitterImpl

object KubeVSEvents {

    val shipApplyForcesEvent = EventEmitterImpl<ShipApplyForcesEvent>()

    data class ShipApplyForcesEvent(val physShip: PhysShipImpl) {
        companion object : EventEmitter<ShipApplyForcesEvent> by shipApplyForcesEvent
    }

    val shipTickEvent = EventEmitterImpl<ShipTickEvent>()

    data class ShipTickEvent(val serverShip: ServerShip) {
        companion object : EventEmitter<ShipTickEvent> by shipTickEvent
    }
}