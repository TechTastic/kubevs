package io.github.techtastic.kubevs.integration

import org.valkyrienskies.core.api.ships.ClientShip
import org.valkyrienskies.core.api.ships.LoadedShip
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.api.ships.Ship

class ShipJS(val ship: Ship?) {
    val isLoadedShip: Boolean = ship is LoadedShip
    val isServerShip: Boolean = ship is ServerShip
    val isClientShip: Boolean = ship is ClientShip

    fun getAsLoadedShip(): LoadedShip? {
        if (isLoadedShip)
            return ship as LoadedShip
        return null
    }

    fun getAsServerShip(): ServerShip? {
        if (isServerShip)
            return ship as ServerShip
        return null
    }

    fun getAsClientShip(): ClientShip? {
        if (isClientShip)
            return ship as ClientShip
        return null
    }
}