package io.github.amiralimollaei.mods.notenoughvulkan.mixin.compat.skip_wayland_patches;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.platform.Window;
import io.github.amiralimollaei.mods.notenoughvulkan.NotEnoughVulkanClientMod;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static org.lwjgl.glfw.GLFW.GLFW_TRUE;

@Mixin(value = Window.class, remap = false, priority = 1500)
public abstract class WindowMixinSquared {
    @TargetHandler(
            mixin = "net.vulkanmod.mixin.window.WindowMixin",
            name = "vulkanHint"
    )
    @WrapOperation(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/glfw/GLFW;glfwWindowHint(II)V"
            )
    )
    private static void notenoughvulkan$skipWaylandPatches(int hint, int value, Operation<Void> original) {
        if (hint != GLFW.GLFW_DECORATED) {
            original.call(hint, value);
            return;
        }

        if (NotEnoughVulkanClientMod.options().compatSettings.forceX11) {
            original.call(hint, GLFW_TRUE);
        }
    }
}
