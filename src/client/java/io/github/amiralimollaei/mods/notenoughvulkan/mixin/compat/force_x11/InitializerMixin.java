package io.github.amiralimollaei.mods.notenoughvulkan.mixin.compat.force_x11;

import io.github.amiralimollaei.mods.notenoughvulkan.NotEnoughVulkanClientMod;
import io.github.amiralimollaei.mods.notenoughvulkan.compat.force_x11.ExtendedPlatform;
import net.vulkanmod.Initializer;
import net.vulkanmod.config.Platform;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(value = Initializer.class, remap = false)
public abstract class InitializerMixin {
    @Redirect(
            method = "onInitializeClient",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/vulkanmod/config/Platform;init()V"
            )
    )
    private static void modifyInit() {
        if (NotEnoughVulkanClientMod.options().compatSettings.forceX11) {
            ExtendedPlatform.init();
        } else {
            Platform.init();
        }
    }
}
