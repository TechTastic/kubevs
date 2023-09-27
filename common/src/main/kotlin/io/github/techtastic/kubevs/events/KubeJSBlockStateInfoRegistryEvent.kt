package io.github.techtastic.kubevs.events

import dev.latvian.mods.kubejs.event.EventJS
import io.github.techtastic.kubevs.registration.BlockStateInfoProviderJS

class KubeJSBlockStateInfoRegistryEvent(private val providers: MutableList<BlockStateInfoProviderJS>): EventJS() {
    fun getProviders(): List<BlockStateInfoProviderJS> =
            this.providers.toList()

    fun registerProvider(id: String, priority: Int?, mass: BlockStateInfoProviderJS.MassCallback?, type: BlockStateInfoProviderJS.TypeCallback?): BlockStateInfoProviderJS {
        val provider = BlockStateInfoProviderJS(id, priority, mass, type)
        this.providers.add(provider)
        return provider
    }
}