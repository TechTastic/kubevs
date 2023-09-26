package io.github.techtastic.kubevs.fabric;

import io.github.techtastic.kubevs.KubeVSMod;
import net.fabricmc.api.ModInitializer;

public class KubeVSFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        KubeVSMod.init();
    }
}
