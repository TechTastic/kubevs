package io.github.techtastic.kubevs.integration

import dev.architectury.event.EventResult
import dev.latvian.mods.kubejs.KubeJSPlugin
import dev.latvian.mods.kubejs.level.LevelJS
import dev.latvian.mods.kubejs.player.PlayerDataJS
import dev.latvian.mods.kubejs.player.PlayerJS
import dev.latvian.mods.kubejs.script.AttachDataEvent
import dev.latvian.mods.kubejs.script.BindingsEvent
import dev.latvian.mods.kubejs.script.ScriptType
import dev.latvian.mods.kubejs.server.ServerJS
import dev.latvian.mods.kubejs.util.ClassFilter
import io.github.techtastic.kubevs.KubeVSJavaIntegration
import io.github.techtastic.kubevs.events.KubeVSApplyForcesEvent
import io.github.techtastic.kubevs.events.KubeVSShipLoadClientEvent
import io.github.techtastic.kubevs.events.KubeVSShipLoadServerEvent
import io.github.techtastic.kubevs.events.KubeVSShipRenderStartEvent
import io.github.techtastic.kubevs.registration.BlockStateInfoProviderBuilder
import io.github.techtastic.kubevs.registration.KubeVSBuilderTypes
import io.github.techtastic.kubevs.ship.ShipControlJS
import io.github.techtastic.kubevs.util.BlockStateInfoJS
import io.github.techtastic.kubevs.util.ShipAssemblyCallback
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import org.joml.*
import org.joml.primitives.*
import org.valkyrienskies.core.api.ships.ClientShip
import org.valkyrienskies.core.api.ships.LoadedShip
import org.valkyrienskies.core.api.ships.ServerShip
import org.valkyrienskies.core.api.ships.Ship
import org.valkyrienskies.core.impl.datastructures.DenseBlockPosSet
import org.valkyrienskies.core.impl.hooks.VSEvents
import org.valkyrienskies.mod.common.*
import org.valkyrienskies.mod.common.hooks.VSGameEvents
import org.valkyrienskies.mod.common.util.IEntityDraggingInformationProvider

class KubeVSIntegration: KubeJSPlugin() {
    override fun init() {
        super.init()

        KubeVSBuilderTypes.BLOCK_INFO_PROVIDER.addType("basic", BlockStateInfoProviderBuilder::class.java, ::BlockStateInfoProviderBuilder)

        VSEvents.shipLoadEvent.on(this::onShipLoadServer)
        EventHandlers.applyForcesEvent.on(this::onApplyForces)
    }

    override fun clientInit() {
        super.clientInit()

        VSEvents.shipLoadEventClient.on(this::onShipLoad)
        VSGameEvents.postRenderShip.on(this::onShipRender)
    }

    override fun addBindings(event: BindingsEvent) {
        event.add("Ship", Ship::class.java)
        event.add("LoadedShip", LoadedShip::class.java)
        event.add("ClientShip", ClientShip::class.java)
        event.add("ServerShip", ServerShip::class.java)

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

        event.add("DenseBlockPosSet", DenseBlockPosSet::class.java)

        event.add("BlockType", BlockStateInfoProviderBuilder.Type::class.java)
        event.add("ShipControl", ShipControlJS::class.java)

        if (event.type.isServer) {
            event.add("BlockStateInfo", BlockStateInfoJS::class.java)
            event.addFunction("createNewShipWithBlocks", ShipAssemblyCallback(), BlockPos::class.java, DenseBlockPosSet::class.java, ServerLevel::class.java)
        }

        KubeVSJavaIntegration.addBindings(event)

        super.addBindings(event)
    }

    override fun addClasses(type: ScriptType, filter: ClassFilter) {
        filter.deny("org.valkyrienskies.mod.common.config")

        super.addClasses(type, filter)
    }

    override fun attachServerData(event: AttachDataEvent<ServerJS>) {
        val server = event.parent.minecraftServer
        event.add("serverShipObjectWorld", server.shipObjectWorld)
        event.add("vsPipeline", server.vsPipeline)

        super.attachServerData(event)
    }

    override fun attachLevelData(event: AttachDataEvent<LevelJS>) {
        val level = event.parent.minecraftLevel
        event.add("shipObjectWorld", level.shipObjectWorld)
        event.add("allShips", level.allShips)
        event.add("dimensionId", level.dimensionId)
        event.add("shipWorldNullable", level.shipWorldNullable)

        if (level is ServerLevel)
            event.add("serverShipObjectWorld", level.shipObjectWorld)
        else if (level is ClientLevel)
            event.add("clientShipObjectWorld", level.shipObjectWorld)

        super.attachLevelData(event)
    }

    override fun attachPlayerData(event: AttachDataEvent<PlayerDataJS<Player, PlayerJS<Player>>>) {
        val player = event.parent.player.minecraftPlayer
        event.add("dimensionId", player.playerWrapper.dimension)

        if (player is IEntityDraggingInformationProvider) {
            event.add("draggingInformation", player.draggingInformation)
            event.add("shipOn", player.getShipManaging())
        }

        super.attachPlayerData(event)
    }

    fun onShipLoadServer(event: VSEvents.ShipLoadEvent): EventResult {
        if (KubeVSShipLoadServerEvent(event).post(ScriptType.SERVER, "vs.ship.load", event.ship.id.toString()))
            return EventResult.interruptTrue()
        return EventResult.pass()
    }

    fun onApplyForces(event: EventHandlers.ApplyForcesEvent): EventResult {
        if (KubeVSApplyForcesEvent(event).post(ScriptType.SERVER, "vs.applyForces", event.physShip.id.toString()))
            return EventResult.interruptTrue()
        return EventResult.pass()
    }

    fun onShipRender(event: VSGameEvents.ShipRenderEvent): EventResult {
        if (KubeVSShipRenderStartEvent(event).post(ScriptType.CLIENT, "vs.ship.render", event.ship.id.toString()))
            return EventResult.interruptTrue()
        return EventResult.pass()
    }

    fun onShipLoad(event: VSEvents.ShipLoadEventClient): EventResult {
        if (KubeVSShipLoadClientEvent(event).post(ScriptType.CLIENT, "vs.ship.load", event.ship.id.toString()))
            return EventResult.interruptTrue()
        return EventResult.pass()
    }
}