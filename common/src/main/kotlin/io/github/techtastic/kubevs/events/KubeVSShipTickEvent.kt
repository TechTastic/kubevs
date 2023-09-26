package io.github.techtastic.kubevs.events

import dev.latvian.mods.kubejs.event.EventJS
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl

class KubeVSShipTickEvent(val serverShip: ServerShip): EventJS() {
    constructor(event: KubeVSEvents.ShipTickEvent) : this(event.serverShip)
}