package io.github.techtastic.kubevs.plugin

import dev.latvian.mods.kubejs.KubeJSPlugin
import dev.latvian.mods.kubejs.script.BindingsEvent
import dev.latvian.mods.kubejs.script.ScriptType
import dev.latvian.mods.kubejs.util.AttachedData
import dev.latvian.mods.kubejs.util.ClassFilter
import io.github.techtastic.kubevs.bindings.KubeVSJavaBindings
import io.github.techtastic.kubevs.bindings.event.ShipEvents
import io.github.techtastic.kubevs.event.KubeVSEvents
import io.github.techtastic.kubevs.registry.KubeVSBSIP
import io.github.techtastic.kubevs.util.BlockTypeJS
import net.minecraft.server.MinecraftServer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import org.joml.*
import org.joml.primitives.*
import org.valkyrienskies.core.api.VsBeta
import org.valkyrienskies.core.api.util.GameTickOnly
import org.valkyrienskies.core.api.util.PhysTickOnly
import org.valkyrienskies.core.internal.world.VsiPlayer
import org.valkyrienskies.core.util.datastructures.DenseBlockPosSet
import org.valkyrienskies.mod.api.shipWorld
import org.valkyrienskies.mod.api.vsApi
import org.valkyrienskies.mod.common.*
import org.valkyrienskies.mod.common.hooks.VSGameEvents
import org.valkyrienskies.mod.common.util.IEntityDraggingInformationProvider

class KubeVSPlugin: KubeJSPlugin() {
    @OptIn(GameTickOnly::class, VsBeta::class, PhysTickOnly::class)
    override fun init() {
        super.init()

        vsApi.physTickEvent.on { event ->
            ShipEvents.PHYS_TICK.post(KubeVSEvents.KubeVSPhysTickEvent(event))
        }

        vsApi.splitEvent.on { event ->
            ShipEvents.SPLIT.post(KubeVSEvents.KubeVSSplitTickEvent(event))
        }

        vsApi.mergeEvent.on { event ->
            ShipEvents.MERGE.post(KubeVSEvents.KubeVSMergeTickEvent(event))
        }

        vsApi.collisionStartEvent.on { event ->
            ShipEvents.COLLISION_START.post(KubeVSEvents.KubeVSCollisionEvent(event))
        }

        vsApi.collisionPersistEvent.on { event ->
            ShipEvents.COLLISION_PERSIST.post(KubeVSEvents.KubeVSCollisionEvent(event))
        }

        vsApi.collisionEndEvent.on { event ->
            ShipEvents.COLLISION_END.post(KubeVSEvents.KubeVSCollisionEvent(event))
        }

        vsApi.shipLoadEvent.on { event ->
            ShipEvents.LOAD_SERVER.post(KubeVSEvents.ShipLoadServerEvent(event))
        }
    }

    @OptIn(GameTickOnly::class, VsBeta::class)
    override fun clientInit() {
        super.clientInit()

        vsApi.shipLoadEventClient.on { event ->
            ShipEvents.LOAD_CLIENT.post(KubeVSEvents.ShipLoadClientEvent(event))
        }

        VSGameEvents.postRenderShip.on { event ->
            ShipEvents.RENDER.post(KubeVSEvents.ShipRenderStartEvent(event))
        }
    }

    override fun initStartup() {
        super.initStartup()

        ShipEvents.BLOCKSTATE_INFO.post(KubeVSEvents.KubeVSBlockStateInfoEvent())
    }

    override fun registerEvents() {
        ShipEvents.GROUP.register()

        super.registerEvents()
    }

    @OptIn(GameTickOnly::class)
    override fun registerBindings(event: BindingsEvent) {
        event.add("ValkyrienSkies", ValkyrienSkiesMod::class.java)

        event.add("Vector3i", Vector3i::class.java)
        event.add("Vector3ic", Vector3ic::class.java)
        event.add("Vector3d", Vector3d::class.java)
        event.add("Vector3dc", Vector3dc::class.java)
        event.add("Vector3f", Vector3f::class.java)
        event.add("Vector3fc", Vector3fc::class.java)

        event.add("AABBi", AABBi::class.java)
        event.add("AABBic", AABBic::class.java)
        event.add("AABBd", AABBd::class.java)
        event.add("AABBdc", AABBdc::class.java)
        event.add("AABBf", AABBf::class.java)
        event.add("AABBfc", AABBfc::class.java)

        event.add("Matrix2d", Matrix2d::class.java)
        event.add("Matrix2dc", Matrix2dc::class.java)
        event.add("Matrix2f", Matrix2f::class.java)
        event.add("Matrix2dc", Matrix2fc::class.java)
        event.add("Matrix3d", Matrix3d::class.java)
        event.add("Matrix3dc", Matrix3dc::class.java)
        event.add("Matrix3f", Matrix3f::class.java)
        event.add("Matrix3dc", Matrix3fc::class.java)

        event.add("DenseBlockPosSet", DenseBlockPosSet::class.java)
        event.add("BlockType", BlockTypeJS::class.java)

        KubeVSJavaBindings.addBindings(event)

        super.registerBindings(event)
    }

    override fun registerClasses(type: ScriptType, filter: ClassFilter) {
        filter.deny("org.valkyrienskies.mod.common.config")
        filter.deny(BlockStateInfoProvider::class.java)
        filter.deny(BlockStateInfo::class.java)
        filter.deny(KubeVSBSIP::class.java)

        if (type.isServer)
            filter.allow("org.valkyrienskies.mod.common.BlockStateInfo.INSTANCE.get")

        super.registerClasses(type, filter)
    }

    @OptIn(GameTickOnly::class)
    override fun attachServerData(event: AttachedData<MinecraftServer>) {
        val server = event.parent

        event.add("shipObjectWorld", server.shipObjectWorld)
        event.add("vsPipeline", server.vsPipeline)
        event.add("shipWorld", server.shipWorld)
    }

    @OptIn(GameTickOnly::class)
    override fun attachLevelData(event: AttachedData<Level>) {
        val level = event.parent

        event.add("shipObjectWorld", level.shipObjectWorld)
        event.add("allShips", level.allShips)
        event.add("dimensionId", level.dimensionId)
        event.add("shipWorld", level.shipWorld)
        event.add("shipWorldNullable", level.shipWorldNullable)
    }

    override fun attachPlayerData(event: AttachedData<Player>) {
        val player = event.parent

        if (player is VsiPlayer)
            event.add("dimensionId", player.dimension)

        if (player is IEntityDraggingInformationProvider)
            event.add("draggingInformation", player.draggingInformation)

        super.attachPlayerData(event)
    }
}