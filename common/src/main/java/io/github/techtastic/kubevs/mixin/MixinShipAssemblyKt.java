package io.github.techtastic.kubevs.mixin;

import io.github.techtastic.kubevs.integration.EventHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.core.impl.datastructures.DenseBlockPosSet;
import org.valkyrienskies.mod.common.assembly.ShipAssemblyKt;

@Mixin(ShipAssemblyKt$createNewShipWithBlocks$5.class)
public class MixinShipAssemblyKt {
    @Inject(method = "createNewShipWithBlocks", at = @At("RETURN"))
    private static void KubeVS$addShipCreationCall(BlockPos centerBlock, DenseBlockPosSet blocks, ServerLevel level, CallbackInfoReturnable<ServerShip> cir) {
        EventHandlers.INSTANCE.getShipCreationEvent().emit(new EventHandlers.ShipCreationEvent(cir.getReturnValue(), centerBlock, level));
    }
}
