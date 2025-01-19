package io.github.techtastic.kubevs.ship

import dev.latvian.mods.kubejs.script.ScriptType
import io.github.techtastic.kubevs.plugin.KubeVSEvents
import org.valkyrienskies.core.api.ships.*
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl

class KubeVSShipAccess: ShipForcesInducer {
    override fun applyForces(physShip: PhysShip) {
        KubeVSEvents.KubeVSShipPhysTickEvent(physShip as PhysShipImpl).post(ScriptType.SERVER, "vs.ship.phys")
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