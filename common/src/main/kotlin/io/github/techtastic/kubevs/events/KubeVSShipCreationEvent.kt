package io.github.techtastic.kubevs.events

import dev.latvian.mods.kubejs.event.EventJS
import io.github.techtastic.kubevs.integration.EventHandlers
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import org.valkyrienskies.core.api.ships.ServerShip

class KubeVSShipCreationEvent(val serverShip: ServerShip, val center: BlockPos, val serverLevel: ServerLevel): EventJS() {
    constructor(event: EventHandlers.ShipCreationEvent) : this(event.serverShip, event.center, event.serverLevel)
}