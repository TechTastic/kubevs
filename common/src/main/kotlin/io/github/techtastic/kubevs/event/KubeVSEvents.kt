package io.github.techtastic.kubevs.event

import dev.latvian.mods.kubejs.event.EventJS
import io.github.techtastic.kubevs.registry.KubeVSBSIP
import io.github.techtastic.kubevs.util.BlockTypeJS
import net.minecraft.world.level.block.state.BlockState
import org.valkyrienskies.core.api.ships.ClientShip
import org.valkyrienskies.core.impl.game.ships.PhysShipImpl
import org.valkyrienskies.core.impl.game.ships.ShipObjectClient
import org.valkyrienskies.core.impl.game.ships.ShipObjectServer
import org.valkyrienskies.core.impl.hooks.VSEvents
import org.valkyrienskies.mod.common.hooks.VSGameEvents
import java.util.function.Function

object KubeVSEvents {

    // Server Events


    class ShipLoadServerEvent(val shipObjectServer: ShipObjectServer): EventJS() {
        constructor(event: VSEvents.ShipLoadEvent) : this(event.ship)
    }


    class KubeVSShipPhysTickEvent(val physShip: PhysShipImpl): EventJS()


    // Client Events


    class ShipLoadClientEvent(val shipObjectClient: ShipObjectClient): EventJS() {
        constructor(event: VSEvents.ShipLoadEventClient) : this(event.ship)
    }


    class ShipRenderStartEvent(val clientShip: ClientShip): EventJS() {
        constructor(event: VSGameEvents.ShipRenderEvent) : this(event.ship)
    }


    // Startup Events


    class KubeVSBlockStateInfoEvent: EventJS() {
        fun mass(callback: Function<BlockState, Double?>) {
            KubeVSBSIP.MASS_CALLBACK = callback
        }

        fun type(callback: Function<BlockState, BlockTypeJS?>) {
            KubeVSBSIP.TYPE_CALLBACK = callback
        }
    }
}