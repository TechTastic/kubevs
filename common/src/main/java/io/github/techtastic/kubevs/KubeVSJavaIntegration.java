package io.github.techtastic.kubevs;

import dev.latvian.mods.kubejs.script.BindingsEvent;
import io.github.techtastic.kubevs.util.BlockStateInfoJS;
import org.valkyrienskies.core.impl.util.VectorConversionsKt;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;

public class KubeVSJavaIntegration {
    public static void addBindings(BindingsEvent event) {
        event.add("VSGameUtilsKt", VSGameUtilsKt.class);
        event.add("VectorConversionsKt", VectorConversionsKt.class);
        event.add("VectorConversionsMCKt", VectorConversionsMCKt.class);

        if (event.type.isServer())
            event.add("BlockStateInfo", BlockStateInfoJS.INSTANCE);
    }
}
