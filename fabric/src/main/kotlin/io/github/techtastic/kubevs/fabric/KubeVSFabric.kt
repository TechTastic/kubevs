package io.github.techtastic.kubevs.fabric

import io.github.techtastic.kubevs.KubeVS.init
import io.github.techtastic.kubevs.KubeVS.initClient
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.api.ModInitializer
import org.valkyrienskies.mod.fabric.common.ValkyrienSkiesModFabric

object KubeVSFabric: ModInitializer {
    override fun onInitialize() {
        // force VS2 to load before eureka
        ValkyrienSkiesModFabric().onInitialize()

        init()
    }

    @Environment(EnvType.CLIENT)
    class Client : ClientModInitializer {
        override fun onInitializeClient() {
            initClient()
        }
    }
}
