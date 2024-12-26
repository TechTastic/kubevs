package io.github.techtastic.kubevs.forge

import dev.architectury.platform.forge.EventBuses
import dev.latvian.mods.kubejs.KubeJSEvents
import dev.latvian.mods.kubejs.KubeJSOtherEventHandler
import dev.latvian.mods.kubejs.KubeJSPlugin
import dev.latvian.mods.kubejs.util.KubeJSPlugins
import io.github.techtastic.kubevs.KubeVS.MOD_ID
import io.github.techtastic.kubevs.KubeVS.init
import io.github.techtastic.kubevs.KubeVS.initClient
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import thedarkcolour.kotlinforforge.KotlinModLoadingContext

@Mod(MOD_ID)
class KubeVSForge {
    init {
        val bus = KotlinModLoadingContext.get().getKEventBus()
        EventBuses.registerModEventBus(MOD_ID, bus)

        bus.addListener { event: FMLClientSetupEvent? ->
            clientSetup(
                event
            )
        }
        init()
    }

    private fun clientSetup(event: FMLClientSetupEvent?) {
        initClient()
    }
}
