package io.github.amiralimollaei.mods.notenoughvulkan.compat.force_x11;

import net.vulkanmod.config.Platform;
import org.lwjgl.glfw.GLFW;

import static net.vulkanmod.Initializer.LOGGER;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_ANY_PLATFORM;
import io.github.amiralimollaei.mods.notenoughvulkan.NotEnoughVulkanClientMod;

public class ExtendedPlatform extends Platform {
    private static final int specialActivePlat = specialGetSupportedPlat();

    private static int specialGetSupportedPlat() {
        int activePlat = Platform.getActivePlat();
        if (Platform.isGnome() && activePlat == GLFW_PLATFORM_WAYLAND && NotEnoughVulkanClientMod.options().compatSettings.forceX11) {
            return GLFW_PLATFORM_X11;
        }
        return activePlat; //Linux Or Android
    }

    public static void init() {
        GLFW.glfwInitHint(GLFW_PLATFORM, specialActivePlat);
        LOGGER.info("Selecting Platform: {}", specialGetStringFromPlat(specialActivePlat));
        LOGGER.info("GLFW: {}", GLFW.glfwGetVersionString());
        GLFW.glfwInit();
    }

    private static String specialGetStringFromPlat(int plat) {
        return switch (plat) {
            case GLFW_PLATFORM_WIN32 -> "WIN32";
            case GLFW_PLATFORM_WAYLAND -> "WAYLAND";
            case GLFW_PLATFORM_X11 -> "X11";
            case GLFW_PLATFORM_COCOA -> "MACOS";
            case GLFW_ANY_PLATFORM -> "ANDROID";
            default -> throw new IllegalStateException("Unexpected value: " + plat);
        };
    }

    public static boolean specialIsWayLand() {
        return specialActivePlat == GLFW_PLATFORM_WAYLAND;
    }
}
