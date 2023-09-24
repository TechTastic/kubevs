package io.github.techtastic.kubevs.events

import dev.latvian.mods.kubejs.event.EventJS
import io.github.techtastic.kubevs.integration.EventHandlers
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl

class KubeVSApplyForcesEvent(val physShip: PhysShipImpl): EventJS() {
    constructor(event: EventHandlers.ApplyForcesEvent) : this(event.physShip)
}