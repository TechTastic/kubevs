package io.github.techtastic.kubevs.ship;

import io.github.techtastic.kubevs.integration.EventHandlers;
import org.jetbrains.annotations.NotNull;
import org.valkyrienskies.core.api.ships.PhysShip;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.core.impl.api.ShipForcesInducer;
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl;

public class KubeVSShipForcesInducer implements ShipForcesInducer {
    @Override
    public void applyForces(@NotNull PhysShip physShip) {
        EventHandlers.INSTANCE.getApplyForcesEvent().emit(new EventHandlers.ApplyForcesEvent((PhysShipImpl) physShip));
    }

    public static KubeVSShipForcesInducer getOrCreate(ServerShip ship) {
        if (ship.getAttachment(KubeVSShipForcesInducer.class) == null)
            ship.saveAttachment(KubeVSShipForcesInducer.class, new KubeVSShipForcesInducer());
        return ship.getAttachment(KubeVSShipForcesInducer.class);
    }
}
