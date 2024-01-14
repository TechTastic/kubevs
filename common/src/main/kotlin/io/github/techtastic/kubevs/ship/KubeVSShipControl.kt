package io.github.techtastic.kubevs.ship

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.github.techtastic.kubevs.events.KubeVSEvents
import org.valkyrienskies.core.api.ships.PhysShip
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.api.ships.ServerTickListener
import org.valkyrienskies.core.api.ships.ShipForcesInducer
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl

@JsonAutoDetect(
        fieldVisibility = JsonAutoDetect.Visibility.ANY,
        getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE,
        setterVisibility = JsonAutoDetect.Visibility.NONE
)
@JsonIgnoreProperties(ignoreUnknown = true)
class KubeVSShipControl(@JsonIgnore var ship: ServerShip?) :
        ServerTickListener, ShipForcesInducer {

    override fun applyForces(physShip: PhysShip) {
        KubeVSEvents.shipApplyForcesEvent.emit(KubeVSEvents.ShipApplyForcesEvent(physShip as PhysShipImpl))
    }

    override fun onServerTick() {
        if (ship != null)
            KubeVSEvents.shipTickEvent.emit(KubeVSEvents.ShipTickEvent(ship!!))
    }
}