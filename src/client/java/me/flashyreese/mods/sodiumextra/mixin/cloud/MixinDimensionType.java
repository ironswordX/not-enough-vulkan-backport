package me.flashyreese.mods.sodiumextra.mixin.cloud;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.Optional;

@Mixin(DimensionType.class)
public class MixinDimensionType {
    @Inject(method = "cloudHeight", at = @At("TAIL"), cancellable = true)
    public void cloudHeight(CallbackInfoReturnable<Optional<Integer>> cir) {
        // Only override if vanilla actually has a cloud height (i.e. we're in the overworld)
        if (cir.getReturnValue().isPresent()) {
            cir.setReturnValue(Optional.of(SodiumExtraClientMod.options().extraSettings.cloudHeight));
        }
    }
}