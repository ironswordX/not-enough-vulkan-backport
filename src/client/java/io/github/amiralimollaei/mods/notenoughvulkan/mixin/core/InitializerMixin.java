package io.github.amiralimollaei.mods.notenoughvulkan.mixin.core;

import io.github.amiralimollaei.mods.notenoughvulkan.NotEnoughVulkanClientMod;
import net.vulkanmod.Initializer;
import net.vulkanmod.config.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.nio.file.Path;

@Mixin(value = Initializer.class, remap = false)
public abstract class InitializerMixin {
    @Redirect(
            method = "onInitializeClient",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/vulkanmod/Initializer;loadConfig(Ljava/nio/file/Path;)Lnet/vulkanmod/config/Config;"
            )
    )
    public Config notenoughvulkan$dontLoadConfig(Path path) {
        return NotEnoughVulkanClientMod.getVulkanModConfig();
    }
}
