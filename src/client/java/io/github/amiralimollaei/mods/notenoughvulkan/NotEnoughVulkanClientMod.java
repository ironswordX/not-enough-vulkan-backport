package io.github.amiralimollaei.mods.notenoughvulkan;

import io.github.amiralimollaei.mods.notenoughvulkan.config.NotEnoughVulkanGameOptions;
import net.caffeinemc.caffeineconfig.CaffeineConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.vulkanmod.config.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class NotEnoughVulkanClientMod {
    private static NotEnoughVulkanGameOptions CONFIG;
    private static CaffeineConfig MIXIN_CONFIG;
    private static Logger LOGGER;

    public static Logger logger() {
        if (LOGGER == null) {
            LOGGER = LoggerFactory.getLogger("Sodium Extra");
        }

        return LOGGER;
    }

    private static boolean packageExists(String packageName) {
        return Arrays.stream(Package.getPackages()).anyMatch((p) -> p.getName().equals(packageName));
    }

    private static NotEnoughVulkanGameOptions loadConfig() {
        return NotEnoughVulkanGameOptions.load(FabricLoader.getInstance().getConfigDir().resolve("not-enough-vulkan-options.json").toFile());
    }

    public static NotEnoughVulkanGameOptions options() {
        if (CONFIG == null) {
            CONFIG = loadConfig();
        }

        return CONFIG;
    }

    public static CaffeineConfig mixinConfig() {
        if (MIXIN_CONFIG == null) {
            MIXIN_CONFIG = CaffeineConfig.builder("Not Enough Vulkan").withSettingsKey("not-enough-vulkan:options")
                    .addMixinOption("core", true, false)
                    .addMixinOption("compat", true)
                    .addMixinOption("compat.bobby", packageExists("de.johni0702.minecraft.bobby"), false)
                    .addMixinOption("compat.skip_wayland_patches", Platform.isWayLand())
                    .addMixinOption("compat.monitor_selector", true)

                    //.withInfoUrl("https://github.com/amiralimollaei/not-enough-vulkan/wiki/Configuration-File")
                    .build(FabricLoader.getInstance().getConfigDir().resolve("not-enough-vulkan.properties"));
        }
        return MIXIN_CONFIG;
    }
}
