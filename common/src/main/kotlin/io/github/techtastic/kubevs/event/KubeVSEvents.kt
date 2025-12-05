package io.github.techtastic.kubevs.event

import dev.latvian.mods.kubejs.event.EventJS
import io.github.techtastic.kubevs.registry.KubeVSBSIP
import io.github.techtastic.kubevs.util.BlockTypeJS
import net.minecraft.world.level.block.state.BlockState
import org.joml.Vector3ic
import org.valkyrienskies.core.api.VsBeta
import org.valkyrienskies.core.api.events.CollisionEvent
import org.valkyrienskies.core.api.events.MergeEvent
import org.valkyrienskies.core.api.events.PhysTickEvent
import org.valkyrienskies.core.api.events.ShipLoadEvent
import org.valkyrienskies.core.api.events.ShipLoadEventClient
import org.valkyrienskies.core.api.events.SplitEvent
import org.valkyrienskies.core.api.physics.ContactPoint
import org.valkyrienskies.core.api.ships.ClientShip
import org.valkyrienskies.core.api.ships.LoadedServerShip
import org.valkyrienskies.core.api.ships.properties.ShipId
import org.valkyrienskies.core.api.util.GameTickOnly
import org.valkyrienskies.core.api.util.PhysTickOnly
import org.valkyrienskies.core.api.world.PhysLevel
import org.valkyrienskies.core.api.world.properties.DimensionId
import org.valkyrienskies.mod.common.hooks.VSGameEvents
import java.util.function.Function

object KubeVSEvents {

    // Server Events


    class ShipLoadServerEvent @OptIn(GameTickOnly::class) constructor(val shipObjectServer: LoadedServerShip): EventJS() {
        @OptIn(GameTickOnly::class, VsBeta::class)
        constructor(event: ShipLoadEvent) : this(event.ship)
    }


    class KubeVSPhysTickEvent @OptIn(PhysTickOnly::class, VsBeta::class) constructor(val physLevel: PhysLevel, val delta: Double): EventJS() {
        @OptIn(PhysTickOnly::class, VsBeta::class)
        constructor(event: PhysTickEvent) : this(event.world, event.delta)
    }


    class KubeVSSplitTickEvent(val dimensionId: DimensionId?, val oldRoot: Vector3ic, val newRootA: Vector3ic, val newRootB: Vector3ic, val voxelType: Int, val wasPocket: Boolean): EventJS() {
        @OptIn(GameTickOnly::class, VsBeta::class)
        constructor(event: SplitEvent) : this(event.dimensionId, event.oldRoot, event.newRootA, event.newRootB, event.voxelType, event.wasPocket)
    }


    class KubeVSMergeTickEvent(val dimensionId: DimensionId?, val newRoot: Vector3ic, val oldRootA: Vector3ic, val oldRootB: Vector3ic, val voxelType: Int, val stillPocket: Boolean): EventJS() {
        @OptIn(GameTickOnly::class, VsBeta::class)
        constructor(event: MergeEvent) : this(event.dimensionId, event.newRoot, event.oldRootA, event.oldRootB, event.voxelType, event.stillPocket)
    }


    class KubeVSCollisionEvent @OptIn(PhysTickOnly::class, VsBeta::class) constructor(val dimensionId: DimensionId, val physLevel: PhysLevel, val shipIdA: ShipId, val shipIdB: ShipId, val contactPoints: Collection<ContactPoint>): EventJS() {
        @OptIn(PhysTickOnly::class, VsBeta::class)
        constructor(event: CollisionEvent) : this(event.dimensionId, event.physLevel, event.shipIdA, event.shipIdB, event.contactPoints)
    }


    // Client Events


    class ShipLoadClientEvent @OptIn(GameTickOnly::class) constructor(val shipObjectClient: ClientShip): EventJS() {
        @OptIn(GameTickOnly::class, VsBeta::class)
        constructor(event: ShipLoadEventClient) : this(event.ship)
    }


    class ShipRenderStartEvent @OptIn(GameTickOnly::class) constructor(val clientShip: ClientShip): EventJS() {
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