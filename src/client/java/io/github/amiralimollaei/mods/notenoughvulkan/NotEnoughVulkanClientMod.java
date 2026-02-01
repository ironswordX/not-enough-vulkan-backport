package io.github.amiralimollaei.mods.notenoughvulkan;

import net.caffeinemc.caffeineconfig.CaffeineConfig;
import net.fabricmc.loader.api.FabricLoader;

import java.util.Arrays;

public class NotEnoughVulkanClientMod {
    private static CaffeineConfig MIXIN_CONFIG;

    private static boolean packageExists(String packageName) {
        return Arrays.stream(Package.getPackages()).anyMatch((p) -> p.getName().equals(packageName));
    }

    public static CaffeineConfig mixinConfig() {
        if (MIXIN_CONFIG == null) {
            MIXIN_CONFIG = CaffeineConfig.builder("Not Enough Vulkan").withSettingsKey("not-enough-vulkan:options")
                    .addMixinOption("core", true, false)
                    .addMixinOption("compat", true, false)
                    .addMixinOption("compat.bobby", packageExists("de.johni0702.minecraft.bobby"), false)

                    //.withInfoUrl("https://github.com/amiralimollaei/not-enough-vulkan/wiki/Configuration-File")
                    .build(FabricLoader.getInstance().getConfigDir().resolve("not-enough-vulkan.properties"));
        }
        return MIXIN_CONFIG;
    }
}
