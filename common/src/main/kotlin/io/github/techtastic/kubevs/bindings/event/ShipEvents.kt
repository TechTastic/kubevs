package io.github.techtastic.kubevs.bindings.event

import dev.latvian.mods.kubejs.event.EventGroup
import dev.latvian.mods.kubejs.event.EventHandler
import io.github.techtastic.kubevs.event.KubeVSEvents

object ShipEvents {
    val GROUP = EventGroup.of("ShipEvents")

    val LOAD_SERVER = GROUP.server("load") { KubeVSEvents.ShipLoadServerEvent::class.java }
    val PHYS_TICK = GROUP.server("phys") { KubeVSEvents.KubeVSShipPhysTickEvent::class.java }

    val LOAD_CLIENT = GROUP.client("load") { KubeVSEvents.ShipLoadClientEvent::class.java }
    val RENDER = GROUP.client("render") { KubeVSEvents.ShipRenderStartEvent::class.java }

    val BLOCKSTATE_INFO = GROUP.startup("blockStateInfo") { KubeVSEvents.KubeVSBlockStateInfoEvent::class.java }
}