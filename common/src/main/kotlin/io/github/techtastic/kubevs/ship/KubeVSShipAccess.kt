package io.github.techtastic.kubevs.ship

import dev.latvian.mods.kubejs.script.ScriptType
import io.github.techtastic.kubevs.plugin.KubeVSEvents
import org.valkyrienskies.core.api.attachment.getOrPutAttachment
import org.valkyrienskies.core.api.ships.*
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl

class KubeVSShipAccess: ShipForcesInducer {
    override fun applyForces(physShip: PhysShip) {
        KubeVSEvents.KubeVSShipPhysTickEvent(physShip as PhysShipImpl).post(ScriptType.SERVER, "vs.ship.phys", physShip.id.toString())
    }

    companion object {
        fun getOrCreateAccess(ship: LoadedServerShip): KubeVSShipAccess {
            return ship.getOrPutAttachment<KubeVSShipAccess> {
                val access = KubeVSShipAccess()
                ship.setAttachment(access)
                access
            }
        }
    }
}