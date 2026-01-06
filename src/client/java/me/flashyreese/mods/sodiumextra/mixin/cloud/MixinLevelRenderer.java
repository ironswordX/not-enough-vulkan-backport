package me.flashyreese.mods.sodiumextra.mixin.cloud;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.vulkanmod.render.sky.CloudRenderer;
import org.spongepowered.asm.mixin.Mixin;

// todo: don't mixin into VulkanMod
@Mixin(CloudRenderer.class)
public abstract class MixinLevelRenderer {
    @WrapMethod(method = "renderClouds", require = 1)
    private void modifyCloudHeight(float cloudHeight, int cloudColor, double camX, double camY, double camZ, long gameTime, float partialTicks, Operation<Void> original) {
        // todo: don't force overwrite
        original.call((float) SodiumExtraClientMod.options().extraSettings.cloudHeight, cloudColor, camX, camY, camZ, gameTime, partialTicks);
    }
}
