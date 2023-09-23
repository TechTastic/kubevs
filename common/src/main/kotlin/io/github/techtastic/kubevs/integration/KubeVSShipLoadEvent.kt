package io.github.techtastic.kubevs.integration

import dev.latvian.mods.kubejs.event.EventJS
import org.valkyrienskies.core.impl.game.ships.ShipObjectServer
import org.valkyrienskies.core.impl.hooks.VSEvents

class KubeVSShipLoadEvent(val shipObjectServer: ShipObjectServer): EventJS() {
    constructor(event: VSEvents.ShipLoadEvent) : this(event.ship) {
        ShipForcesInducerJS(this.shipObjectServer)
    }

    override fun canCancel(): Boolean = false
}