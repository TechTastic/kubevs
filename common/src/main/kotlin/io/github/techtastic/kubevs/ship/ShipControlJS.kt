package io.github.techtastic.kubevs.ship

import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.api.ships.saveAttachment
import org.valkyrienskies.core.impl.game.ships.ShipDataCommon

object ShipControlJS {
    @JvmStatic
    fun attachToShip(ship: ServerShip): Boolean {
        if (hasControl(ship)) return false
        ship.saveAttachment(KubeVSShipControl(ship))
        return true
    }

    @JvmStatic
    fun hasControl(ship: ServerShip): Boolean {
        return ship.getAttachment(KubeVSShipControl::class.java) != null
    }
}