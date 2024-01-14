package io.github.techtastic.kubevs.forge

import dev.architectury.platform.forge.EventBuses
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import io.github.techtastic.kubevs.KubeVSMod
import io.github.techtastic.kubevs.KubeVSMod.init
import io.github.techtastic.kubevs.KubeVSMod.initClient
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(KubeVSMod.MOD_ID)
class KubeVSModForge {

    init {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(KubeVSMod.MOD_ID, MOD_BUS);

        MOD_BUS.addListener { event: FMLClientSetupEvent? ->
            clientSetup(
                event
            )
        }

        init()
    }

    private fun clientSetup(event: FMLClientSetupEvent?) {
        initClient()
    }

    companion object {
        fun getModBus(): IEventBus = MOD_BUS
    }
}
