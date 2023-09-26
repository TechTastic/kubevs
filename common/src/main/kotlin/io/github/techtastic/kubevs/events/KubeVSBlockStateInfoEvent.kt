package io.github.techtastic.kubevs.events

import dev.latvian.mods.kubejs.event.EventJS
import io.github.techtastic.kubevs.registration.KubeVSBlockStateInfoProvider

class KubeVSBlockStateInfoEvent(val provider: KubeVSBlockStateInfoProvider): EventJS() {
    constructor(event: KubeVSEvents.BlockStateInfoEvent) : this(event.provider)
}