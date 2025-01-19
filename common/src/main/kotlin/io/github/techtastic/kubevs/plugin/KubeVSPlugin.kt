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
import io.github.techtastic.kubevs.ship.KubeVSShipAccess
import io.github.techtastic.kubevs.util.BlockTypeJS
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import org.joml.*
import org.joml.primitives.*
import org.valkyrienskies.core.api.ships.*
import org.valkyrienskies.core.api.world.ClientShipWorld
import org.valkyrienskies.core.api.world.ServerShipWorld
import org.valkyrienskies.core.apigame.ships.ClientShipCore
import org.valkyrienskies.core.apigame.ships.LoadedShipCore
import org.valkyrienskies.core.apigame.ships.ServerShipCore
import org.valkyrienskies.core.apigame.world.ClientShipWorldCore
import org.valkyrienskies.core.apigame.world.IPlayer
import org.valkyrienskies.core.apigame.world.ServerShipWorldCore
import org.valkyrienskies.core.impl.hooks.VSEvents
import org.valkyrienskies.core.util.datastructures.DenseBlockPosSet
import org.valkyrienskies.mod.common.*
import org.valkyrienskies.mod.common.hooks.VSGameEvents
import org.valkyrienskies.mod.common.util.IEntityDraggingInformationProvider

class KubeVSPlugin: KubeJSPlugin() {
    override fun init() {
        super.init()

        VSEvents.shipLoadEvent.on { event ->
            KubeVSShipAccess.getOrCreateAccess(event.ship)

            ShipEvents.LOAD_SERVER.post(KubeVSEvents.ShipLoadServerEvent(event))
        }
    }

    override fun clientInit() {
        super.clientInit()

        VSEvents.shipLoadEventClient.on { event ->
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

    override fun registerBindings(event: BindingsEvent) {
        event.add("Ship", Ship::class.java)
        event.add("LoadedShip", LoadedShip::class.java)
        event.add("LoadedShipCore", LoadedShipCore::class.java)

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

        if (event.type.isServer) {
            event.add("ServerShip", ServerShip::class.java)
            event.add("LoadedServerShip", LoadedServerShip::class.java)
            event.add("ServerShipCore", ServerShipCore::class.java)
            event.add("ServerShipWorld", ServerShipWorld::class.java)
            event.add("ServerShipWorldCore", ServerShipWorldCore::class.java)
            event.add("BlockStateInfo", BlockStateInfo::class.java)
        }

        if (event.type.isClient) {
            event.add("ClientShip", ClientShip::class.java)
            event.add("ClientShipCore", ClientShipCore::class.java)
            event.add("ClientShipWorld", ClientShipWorld::class.java)
            event.add("ClientShipWorldCore", ClientShipWorldCore::class.java)
        }

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

    override fun attachServerData(event: AttachedData<MinecraftServer>) {
        val server = event.parent

        event.add("serverShipObjectWorld", server.shipObjectWorld)
        event.add("vsPipeline", server.vsPipeline)
    }

    override fun attachLevelData(event: AttachedData<Level>) {
        val level = event.parent

        event.add("shipObjectWorld", level.shipObjectWorld)
        event.add("allShips", level.allShips)
        event.add("dimensionId", level.dimensionId)
        event.add("shipWorldNullable", level.shipWorldNullable)

        if (level is ServerLevel)
            event.add("serverShipObjectWorld", level.shipObjectWorld)
        if (level is ClientLevel)
            event.add("clientShipObjectWorld", level.shipObjectWorld)
    }

    override fun attachPlayerData(event: AttachedData<Player>) {
        val player = event.parent

        if (player is IPlayer)
            event.add("dimensionId", player.dimension)

        if (player is IEntityDraggingInformationProvider)
            event.add("draggingInformation", player.draggingInformation)

        super.attachPlayerData(event)
    }
}