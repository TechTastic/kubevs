package io.github.techtastic.kubevs.bindings;

import dev.latvian.mods.kubejs.script.BindingsEvent;
import org.valkyrienskies.mod.api.ValkyrienSkies;
import org.valkyrienskies.mod.common.assembly.ShipAssembler;

public class KubeVSJavaBindings {
    public static void addBindings(BindingsEvent event) {
        event.add("ShipAssemblyKt", ShipAssembler.class);
        event.add("ValkyrienSkies", ValkyrienSkies.class);
    }
}
