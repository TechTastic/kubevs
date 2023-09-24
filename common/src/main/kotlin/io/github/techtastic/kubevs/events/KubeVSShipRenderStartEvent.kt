package io.github.techtastic.kubevs.events

import dev.latvian.mods.kubejs.event.EventJS
import org.valkyrienskies.core.api.ships.ClientShip
import org.valkyrienskies.mod.common.hooks.VSGameEvents

class KubeVSShipRenderStartEvent(val clientShip: ClientShip): EventJS() {
    constructor(event: VSGameEvents.ShipRenderEvent) : this(event.ship)

    override fun canCancel(): Boolean = false
}