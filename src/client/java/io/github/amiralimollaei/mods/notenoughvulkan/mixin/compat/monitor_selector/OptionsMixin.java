package io.github.amiralimollaei.mods.notenoughvulkan.mixin.compat.monitor_selector;

import com.llamalad7.mixinextras.lib.apache.commons.ArrayUtils;
import io.github.amiralimollaei.mods.notenoughvulkan.compat.monitor_selector.FullscreenMonitorManager;
import net.minecraft.network.chat.Component;
import net.vulkanmod.config.Config;
import net.vulkanmod.config.option.CyclingOption;
import net.vulkanmod.config.option.Option;
import net.vulkanmod.config.option.Options;
import net.vulkanmod.config.video.VideoModeManager;
import net.vulkanmod.config.video.VideoModeSet;
import net.vulkanmod.config.video.WindowMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;

import static org.lwjgl.glfw.GLFW.glfwGetMonitorName;

@Mixin(value = Options.class, remap = false)
public class OptionsMixin {
    @Shadow
    static net.minecraft.client.Options minecraftOptions;

    @Shadow
    public static boolean fullscreenDirty;

    @Shadow
    private static Config config;

    /// modified from [pull #618](https://github.com/xCollateral/VulkanMod/pull/618)
    @ModifyArg(
            method = "getVideoOpts",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/vulkanmod/config/gui/OptionBlock;<init>(Ljava/lang/String;[Lnet/vulkanmod/config/option/Option;)V",
                    ordinal = 0
            )
    )
    private static Option<?>[] notenoughvuklan$addExtraVideoOpts(Option<?>[] options) {
        if (!FullscreenMonitorManager.shouldApplyMonitorSelectorPatch()) return options;

        CyclingOption<VideoModeSet> resolutionOption = (CyclingOption<VideoModeSet>) options[0];
        CyclingOption<Long> monitorOption = new CyclingOption<Long>(
                Component.translatable("not-enough-vulkan.option.monitor_selector"),
                FullscreenMonitorManager.getMonitors(),
                (monitorHandle) -> {
                    FullscreenMonitorManager.setSelectedFullscreenMonitor(monitorHandle);
                    FullscreenMonitorManager.applySelectedFullscreenMonitor();
                    // re-initialize VideoModeManager with the new monitor selected
                    VideoModeManager.init();
                    VideoModeManager.applySelectedVideoMode();

                    if (minecraftOptions.fullscreen().get()) fullscreenDirty = true;
                },
                FullscreenMonitorManager::getSelectedFullscreenMonitor
        );
        monitorOption.setTooltip(Component.translatable("not-enough-vulkan.option.monitor_selector.tooltip"));
        monitorOption.setTranslator(monitor_address -> Component.nullToEmpty(glfwGetMonitorName(monitor_address)));
        monitorOption.setNewValue(FullscreenMonitorManager.getSelectedFullscreenMonitor());
        monitorOption.setOnChange(() -> {
            var newVideoResolutions = VideoModeManager.populateVideoResolutions(monitorOption.getNewValue());
            resolutionOption.setValues(newVideoResolutions);
            resolutionOption.setNewValue(newVideoResolutions != null ? newVideoResolutions[newVideoResolutions.length - 1] : VideoModeSet.getDummy());
        });
        // monitorOption should only be active when window mode is fullscreen
        CyclingOption<WindowMode> windowModeOption = (CyclingOption<WindowMode>) options[2];
        monitorOption.setActivationFn(() -> windowModeOption.getNewValue() == WindowMode.EXCLUSIVE_FULLSCREEN);
        windowModeOption.setOnChange(() -> {
            monitorOption.setActive(windowModeOption.getNewValue() == WindowMode.EXCLUSIVE_FULLSCREEN);
            // reset monitorOption when disabled
            if (windowModeOption.getNewValue() != WindowMode.EXCLUSIVE_FULLSCREEN)
                monitorOption.setNewValue(((OptionAccessor<Long>) monitorOption).getValue());
        });
        return ArrayUtils.add(options, 0, monitorOption);
    }
}