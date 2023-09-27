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
import dev.latvian.mods.kubejs.util.ConsoleJS
import io.github.techtastic.kubevs.KubeVSJavaIntegration
import io.github.techtastic.kubevs.events.*
import io.github.techtastic.kubevs.ship.ShipControlJS
import io.github.techtastic.kubevs.util.BlockTypeJS
import io.github.techtastic.kubevs.util.ShipAssemblyCallback
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
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

        VSEvents.shipLoadEvent.on(this::onShipLoadServer)
        KubeVSEvents.shipApplyForcesEvent.on(this::onShipApplyForces)
        KubeVSEvents.shipTickEvent.on(this::onShipTick)
    }

    override fun clientInit() {
        super.clientInit()

        VSEvents.shipLoadEventClient.on(this::onShipLoad)
        VSGameEvents.postRenderShip.on(this::onShipRender)
    }

    override fun initStartup() {
        super.initStartup()

        val event = KubeJSBlockStateInfoRegistryEvent(mutableListOf())
        event.post(ScriptType.STARTUP, "vs.info_providers")

        event.getProviders().forEach {
            ConsoleJS.STARTUP.log("Registering Provider: ${it.id}\nRegistry: ${BlockStateInfo.REGISTRY}\nProvider: ${it.sortPriority}, ${it.mass}, and ${it.type}")
            Registry.register(BlockStateInfo.REGISTRY, it.id, it)
        }
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

        event.add("BlockType", BlockTypeJS::class.java)
        event.add("ShipControl", ShipControlJS::class.java)

        if (event.type.isServer) {
            event.addFunction("createNewShipWithBlocks", ShipAssemblyCallback(), BlockPos::class.java, DenseBlockPosSet::class.java, ServerLevel::class.java)
        }

        KubeVSJavaIntegration.addBindings(event)

        super.addBindings(event)
    }

    override fun addClasses(type: ScriptType, filter: ClassFilter) {
        filter.deny("org.valkyrienskies.mod.common.config")
        filter.deny("io.github.techtastic.kubevs.registration.KubeVSBlockStateInfoProvider.INSTANCE.getPriority")
        filter.deny("io.github.techtastic.kubevs.registration.KubeVSBlockStateInfoProvider.INSTANCE.getBlockStateMass")
        filter.deny("io.github.techtastic.kubevs.registration.KubeVSBlockStateInfoProvider.INSTANCE.getBlockStateType")
        filter.deny("io.github.techtastic.kubevs.registration.KubeVSBlockStateInfoProvider.INSTANCE.register")

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

    // Server Event Methods

    fun onShipLoadServer(event: VSEvents.ShipLoadEvent): EventResult {
        if (KubeVSShipLoadServerEvent(event).post(ScriptType.SERVER, "vs.ship.load", event.ship.id.toString()))
            return EventResult.interruptTrue()
        return EventResult.pass()
    }

    fun onShipApplyForces(event: KubeVSEvents.ShipApplyForcesEvent): EventResult {
        if (KubeVSShipApplyForcesEvent(event).post(ScriptType.SERVER, "vs.ship.phys_tick", event.physShip.id.toString()))
            return EventResult.interruptTrue()
        return EventResult.pass()
    }

    fun onShipTick(event: KubeVSEvents.ShipTickEvent): EventResult {
        if (KubeVSShipTickEvent(event).post(ScriptType.SERVER, "vs.ship.game_tick", event.serverShip.id.toString()))
            return EventResult.interruptTrue()
        return EventResult.pass()
    }

    // Client Event Methods

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