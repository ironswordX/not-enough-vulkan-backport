package io.github.amiralimollaei.mods.notenoughvulkan.mixin.compat.monitor_selector;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.platform.Window;
import io.github.amiralimollaei.mods.notenoughvulkan.NotEnoughVulkanClientMod;
import io.github.amiralimollaei.mods.notenoughvulkan.compat.monitor_selector.FullscreenMonitorManager;
import net.vulkanmod.config.video.WindowMode;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;


@Mixin(value = Window.class, remap = false, priority = 1500)
public class WindowMixin {
    @WrapOperation(
            method = "setMode",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/glfw/GLFW;glfwGetPrimaryMonitor()J"
            )
    )
    private long notenoughvulkan$updateSelectedMonitor(Operation<Long> original) {
        if (!FullscreenMonitorManager.shouldApplyMonitorSelectorPatch()) return original.call();
        if (NotEnoughVulkanClientMod.getVulkanModConfig().windowMode != WindowMode.EXCLUSIVE_FULLSCREEN.mode) return original.call();
        return FullscreenMonitorManager.getSelectedFullscreenMonitor();
    }
}