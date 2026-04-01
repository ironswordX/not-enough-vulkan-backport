package io.github.amiralimollaei.mods.notenoughvulkan.compat.monitor_selector;

import io.github.amiralimollaei.mods.notenoughvulkan.NotEnoughVulkanClientMod;
import net.vulkanmod.config.Config;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

/// modified from [pull #618](https://github.com/xCollateral/VulkanMod/pull/618)
public class FullscreenMonitorManager {
    private static Long[] monitors;
    private static long selectedFullscreenMonitor;

    public static Long[] populateMonitors() {
        List<Long> monitors = new ArrayList<>();

        var monitorsRaw = glfwGetMonitors();
        if (monitorsRaw == null) {
            return null;
        }
        for (int i = 0; i < monitorsRaw.limit(); i++) {
            var m = monitorsRaw.get(i);
            monitors.add(m);
        }

        Long[] arr = new Long[monitors.size()];
        monitors.toArray(arr);

        return arr;
    }

    public static Long[] getMonitors() {
        if (monitors == null) {
            monitors = populateMonitors();
        }
        return monitors;
    }

    /// monitor selector patch should only apply when having multiple monitors
    /// this function checks whether we should apply monitor selector patches or not
    public static boolean shouldApplyMonitorSelectorPatch() {
        if (monitors == null) {
            return false;
        }
        return monitors.length > 1;
    }

    public static long getSelectedFullscreenMonitor() {
        return selectedFullscreenMonitor;
    }

    public static void setSelectedFullscreenMonitor(long new_monitor) {
        selectedFullscreenMonitor = new_monitor;
    }

    public static void applySelectedFullscreenMonitor() {
        NotEnoughVulkanClientMod.options().compatSettings.selectedMonitor = glfwGetMonitorName(selectedFullscreenMonitor);
    }

    public static void init() {
        monitors = populateMonitors();
        setSelectedFullscreenMonitor(findFullscreenMonitorHandle());
    }

    public static long findFullscreenMonitorHandle() {
        Config config = NotEnoughVulkanClientMod.getVulkanModConfig();
        if (config == null) return GLFW.glfwGetPrimaryMonitor();
        return Arrays.stream(FullscreenMonitorManager.getMonitors())
                .filter(v -> {
                    String monitorName = org.lwjgl.glfw.GLFW.glfwGetMonitorName(v);
                    if (monitorName == null) return false;
                    return monitorName.equals(NotEnoughVulkanClientMod.options().compatSettings.selectedMonitor);
                })
                .findFirst().orElse(GLFW.glfwGetPrimaryMonitor());
    }
}