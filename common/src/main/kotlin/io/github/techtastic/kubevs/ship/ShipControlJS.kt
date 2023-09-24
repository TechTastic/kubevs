package io.github.techtastic.kubevs.ship

import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.api.ships.saveAttachment
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl

class ShipControlJS(val tickCallback: Ticking, val forcesCallback: ApplyForces) {
    constructor(ship: ServerShip, tickCallback: Ticking, forcesCallback: ApplyForces) :
            this(tickCallback, forcesCallback) {
        attachToShip(ship)
    }

    @FunctionalInterface
    interface Ticking {
        fun tick(serverShip: ServerShip?)
    }

    @FunctionalInterface
    interface ApplyForces {
        fun applyForces(physShip: PhysShipImpl)
    }

    fun attachToShip(ship: ServerShip): Boolean {
        if (hasControl(ship)) return false
        ship.saveAttachment(KubeVSShipControl(ship, tickCallback, forcesCallback))
        return true
    }

    fun hasControl(ship: ServerShip): Boolean {
        val control = ship.getAttachment(KubeVSShipControl::class.java) ?: return false
        return control.tickCallback == this.tickCallback && control.forcesCallback == this.forcesCallback
    }
}