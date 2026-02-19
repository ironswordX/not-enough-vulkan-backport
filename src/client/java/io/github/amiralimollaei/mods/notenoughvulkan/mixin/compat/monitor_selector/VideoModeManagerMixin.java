package io.github.amiralimollaei.mods.notenoughvulkan.mixin.compat.monitor_selector;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.amiralimollaei.mods.notenoughvulkan.NotEnoughVulkanClientMod;
import io.github.amiralimollaei.mods.notenoughvulkan.compat.monitor_selector.FullscreenMonitorManager;
import net.vulkanmod.config.video.VideoModeManager;
import net.vulkanmod.config.video.WindowMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(value = VideoModeManager.class, remap = false)
public abstract class VideoModeManagerMixin {
    @Inject(method = "init", at = @At("HEAD"))
    private static void notenoughvulkan$extendedInit(CallbackInfo ci) {
        FullscreenMonitorManager.init();
    }

    // Use the selected monitor instead of GLFW's default
    @WrapOperation(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/glfw/GLFW;glfwGetPrimaryMonitor()J"
            )
    )
    private static long notenoughvulkan$updateSelectedMonitor(Operation<Long> original) {
        if (!FullscreenMonitorManager.shouldApplyMonitorSelectorPatch()) return original.call();
        if (NotEnoughVulkanClientMod.getVulkanModConfig().windowMode != WindowMode.EXCLUSIVE_FULLSCREEN.mode) return original.call();
        return FullscreenMonitorManager.getSelectedFullscreenMonitor();
    }
}