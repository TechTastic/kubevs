package io.github.techtastic.kubevs.events

import dev.latvian.mods.kubejs.event.EventJS
import org.valkyrienskies.core.impl.game.ships.ShipObjectClient
import org.valkyrienskies.core.impl.hooks.VSEvents

class KubeVSShipLoadClientEvent(val shipObjectClient: ShipObjectClient): EventJS() {
    constructor(event: VSEvents.ShipLoadEventClient) : this(event.ship)

    override fun canCancel(): Boolean = false
}