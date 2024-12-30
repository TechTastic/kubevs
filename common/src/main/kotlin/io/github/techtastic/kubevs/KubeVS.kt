package io.github.techtastic.kubevs

import io.github.techtastic.kubevs.registry.KubeVSBSIP
import io.github.techtastic.kubevs.ship.KubeVSShipAccess
import org.valkyrienskies.mod.api.vsApi

object KubeVS {
    const val MOD_ID = "kubevs"

    @JvmStatic
    fun init() {
        KubeVSBSIP.register()

        vsApi.registerAttachment(KubeVSShipAccess::class.java)
    }

    @JvmStatic
    fun initClient() {}
}
