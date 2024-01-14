package io.github.techtastic.kubevs

import io.github.techtastic.kubevs.registration.KubeVSBlockStateInfoProvider

object KubeVSMod {
    const val MOD_ID = "kubevs"

    @JvmStatic
    fun init() {
        KubeVSBlockStateInfoProvider.register()
    }

    @JvmStatic
    fun initClient() {
    }
}
