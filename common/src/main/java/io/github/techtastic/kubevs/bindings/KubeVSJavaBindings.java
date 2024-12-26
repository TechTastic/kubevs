package io.github.techtastic.kubevs.bindings;

import dev.latvian.mods.kubejs.script.BindingsEvent;
import org.valkyrienskies.core.util.VectorConversionsKt;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.assembly.ShipAssemblyKt;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

public class KubeVSJavaBindings {
    public static void addBindings(BindingsEvent event) {
        event.add("VSGameUtilsKt", VSGameUtilsKt.class);
        event.add("VectorConversionsKt", VectorConversionsKt.class);
        event.add("VectorConversionsMCKt", VectorConversionsMCKt.class);
        event.add("ShipAssemblyKt", ShipAssemblyKt.class);
    }
}
