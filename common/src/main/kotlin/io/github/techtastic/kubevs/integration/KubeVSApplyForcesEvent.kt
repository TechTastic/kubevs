package io.github.techtastic.kubevs.integration

import dev.latvian.mods.kubejs.event.EventJS
import io.github.techtastic.kubevs.events.ShipControlEvents
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl

class KubeVSApplyForcesEvent(val physShip: PhysShipImpl): EventJS() {
    constructor(event: ShipControlEvents.ApplyForcesEvent) : this(event.physShip)
}