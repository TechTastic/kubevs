package io.github.techtastic.kubevs.events

import dev.latvian.mods.kubejs.event.EventJS
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl

class KubeVSShipApplyForcesEvent(val physShip: PhysShipImpl): EventJS() {
    constructor(event: KubeVSEvents.ShipApplyForcesEvent) : this(event.physShip)
}