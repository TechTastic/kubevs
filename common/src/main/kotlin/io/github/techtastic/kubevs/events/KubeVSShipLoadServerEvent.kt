package io.github.techtastic.kubevs.events

import dev.latvian.mods.kubejs.event.EventJS
import org.valkyrienskies.core.impl.game.ships.ShipObjectServer
import org.valkyrienskies.core.impl.hooks.VSEvents

class KubeVSShipLoadServerEvent(val shipObjectServer: ShipObjectServer): EventJS() {
    constructor(event: VSEvents.ShipLoadEvent) : this(event.ship)

    override fun canCancel(): Boolean = false
}