package me.flashyreese.mods.sodiumextra.mixin.fog;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = FogRenderer.class, priority = 1300)
public class MixinFogRenderer {

    @Redirect(method = "setupFog", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/fog/FogData;renderDistanceStart:F", ordinal = 0))
    public void modifyRenderDistanceStart(FogData fogData, float renderDistanceStart) {
        int fogDistance = SodiumExtraClientMod.options().renderSettings.multiDimensionFogControl
                ? SodiumExtraClientMod.options().renderSettings.dimensionFogDistanceMap.getOrDefault(Minecraft.getInstance().level.dimensionType().effectsLocation(), 0)
                : SodiumExtraClientMod.options().renderSettings.fogDistance;

        if (fogDistance == 0) {
            fogData.renderDistanceStart = renderDistanceStart;
        } else if (fogDistance == 33) {
            fogData.renderDistanceStart = Float.MAX_VALUE;
        } else {
            float fogStart = SodiumExtraClientMod.options().renderSettings.fogStart / 100.0F;
            fogData.renderDistanceStart = fogDistance * fogStart * 16F;
        }
    }

    @Redirect(method = "setupFog", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/fog/FogData;renderDistanceEnd:F", ordinal = 0))
    public void modifyRenderDistanceEnd(FogData fogData, float renderDistanceEnd) {
        int fogDistance = SodiumExtraClientMod.options().renderSettings.multiDimensionFogControl
                ? SodiumExtraClientMod.options().renderSettings.dimensionFogDistanceMap.getOrDefault(Minecraft.getInstance().level.dimensionType().effectsLocation(), 0)
                : SodiumExtraClientMod.options().renderSettings.fogDistance;

        if (fogDistance == 0) {
            fogData.renderDistanceEnd = renderDistanceEnd;
        } else if (fogDistance == 33) {
            fogData.renderDistanceEnd = Float.MAX_VALUE;
        } else {
            fogData.renderDistanceEnd = (fogDistance + 1) * 16F;
        }
    }
}
