package io.github.techtastic.kubevs;

import dev.latvian.mods.kubejs.script.BindingsEvent;
import org.valkyrienskies.core.impl.util.VectorConversionsKt;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

public class KubeVSJavaIntegration {
    public static void addBindings(BindingsEvent event) {
        event.add("VSGameUtilsKt", VSGameUtilsKt.class);
        event.add("VectorConversionsKt", VectorConversionsKt.class);
        event.add("VectorConversionsMCKt", VectorConversionsMCKt.class);
    }
}
