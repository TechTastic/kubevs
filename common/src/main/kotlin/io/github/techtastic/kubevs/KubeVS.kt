package io.github.techtastic.kubevs

import io.github.techtastic.kubevs.registry.KubeVSBSIP

object KubeVS {
    const val MOD_ID = "kubevs"

    @JvmStatic
    fun init() {
        KubeVSBSIP.register()
    }

    @JvmStatic
    fun initClient() {}
}
