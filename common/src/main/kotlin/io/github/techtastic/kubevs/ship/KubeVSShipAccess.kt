package io.github.techtastic.kubevs.ship

import io.github.techtastic.kubevs.bindings.event.ShipEvents
import io.github.techtastic.kubevs.event.KubeVSEvents
import org.valkyrienskies.core.api.ships.*
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl

class KubeVSShipAccess: ShipForcesInducer {
    override fun applyForces(physShip: PhysShip) {
        ShipEvents.PHYS_TICK.post(KubeVSEvents.KubeVSShipPhysTickEvent(physShip as PhysShipImpl), physShip.id)
    }

    companion object {
        fun getOrCreateAccess(ship: LoadedServerShip): KubeVSShipAccess {
            return ship.getAttachment<KubeVSShipAccess>() ?: run {
                val access = KubeVSShipAccess()
                ship.saveAttachment<KubeVSShipAccess>(access)
                access
            }
        }
    }
}