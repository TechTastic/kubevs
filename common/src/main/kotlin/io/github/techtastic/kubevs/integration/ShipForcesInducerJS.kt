package io.github.techtastic.kubevs.integration

import io.github.techtastic.kubevs.ship.KubeVSShipForcesInducer
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl
import java.util.function.Consumer

class ShipForcesInducerJS(val ship: ServerShip) {
    private val inducer: KubeVSShipForcesInducer = KubeVSShipForcesInducer.getOrCreate(this.ship)
}