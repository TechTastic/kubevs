package io.github.techtastic.kubevs.ship

import org.valkyrienskies.core.api.ships.PhysShip
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.impl.api.ServerShipUser
import org.valkyrienskies.core.impl.api.ShipForcesInducer
import org.valkyrienskies.core.impl.api.Ticked
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl

class KubeVSShipControl(override var ship: ServerShip?, val tickCallback: ShipControlJS.Ticking, val forcesCallback: ShipControlJS.ApplyForces) :
        ServerShipUser, Ticked, ShipForcesInducer {
    override fun applyForces(physShip: PhysShip) {
        this.forcesCallback.applyForces(physShip as PhysShipImpl)
    }

    override fun tick() {
        this.tickCallback.tick(this.ship)
    }
}