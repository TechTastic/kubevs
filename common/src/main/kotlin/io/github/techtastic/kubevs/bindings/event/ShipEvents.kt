package io.github.techtastic.kubevs.bindings.event

import dev.latvian.mods.kubejs.event.EventGroup
import io.github.techtastic.kubevs.event.KubeVSEvents

object ShipEvents {
    val GROUP = EventGroup.of("ShipEvents")

    val LOAD_SERVER = GROUP.server("load_server") { KubeVSEvents.ShipLoadServerEvent::class.java }
    val PHYS_TICK = GROUP.server("phys") { KubeVSEvents.KubeVSPhysTickEvent::class.java }
    val SPLIT = GROUP.server("split") { KubeVSEvents.KubeVSSplitTickEvent::class.java }
    val MERGE = GROUP.server("merge") { KubeVSEvents.KubeVSMergeTickEvent::class.java }
    val COLLISION_START = GROUP.server("collision_start") { KubeVSEvents.KubeVSCollisionEvent::class.java }
    val COLLISION_PERSIST = GROUP.server("collision_persist") { KubeVSEvents.KubeVSCollisionEvent::class.java }
    val COLLISION_END = GROUP.server("collision_end") { KubeVSEvents.KubeVSCollisionEvent::class.java }

    val LOAD_CLIENT = GROUP.client("load_client") { KubeVSEvents.ShipLoadClientEvent::class.java }
    val RENDER = GROUP.client("render") { KubeVSEvents.ShipRenderStartEvent::class.java }

    val BLOCKSTATE_INFO = GROUP.startup("blockStateInfo") { KubeVSEvents.KubeVSBlockStateInfoEvent::class.java }
}