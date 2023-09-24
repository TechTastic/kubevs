package io.github.techtastic.kubevs.util

import io.github.techtastic.kubevs.ship.KubeVSShipForcesInducer
import org.valkyrienskies.core.api.ships.ServerShip

class ShipForcesInducerJS(val ship: ServerShip) {
    private val inducer: KubeVSShipForcesInducer = KubeVSShipForcesInducer.getOrCreate(this.ship)
}