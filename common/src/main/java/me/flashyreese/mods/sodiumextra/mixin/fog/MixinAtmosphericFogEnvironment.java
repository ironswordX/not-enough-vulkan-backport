package me.flashyreese.mods.sodiumextra.mixin.fog;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.AtmosphericFogEnvironment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AtmosphericFogEnvironment.class)
public class MixinAtmosphericFogEnvironment {

    @Inject(method = "setupFog", at = @At("TAIL"))
    public void onSetupFog(FogData fogData, Entity entity, BlockPos blockPos, ClientLevel level, float viewDistance, DeltaTracker deltaTracker, CallbackInfo ci) {
        // Get fog distance from global or dimension-specific config
        int fogDistance = SodiumExtraClientMod.options().renderSettings.multiDimensionFogControl
                ? SodiumExtraClientMod.options().renderSettings.dimensionFogDistanceMap.getOrDefault(level.dimensionType().effectsLocation(), 0)
                : SodiumExtraClientMod.options().renderSettings.fogDistance;

        // Skip if no override
        if (fogDistance == 0) return;

        // Check if user wants to disable fog altogether
        if (fogDistance == 33) {
            fogData.environmentalStart = Float.MAX_VALUE;
            fogData.environmentalEnd = Float.MAX_VALUE;
            fogData.renderDistanceStart = Float.MAX_VALUE;
            fogData.renderDistanceEnd = Float.MAX_VALUE;
            fogData.skyEnd = Float.MAX_VALUE;
            fogData.cloudEnd = Float.MAX_VALUE;
            return;
        }

        // Fog start percentage
        float fogStartPercent = SodiumExtraClientMod.options().renderSettings.fogStart / 100.0F;

        float start = fogDistance * 16 * fogStartPercent;
        float end = (fogDistance + 1) * 16;

        fogData.renderDistanceStart = start;
        fogData.renderDistanceEnd = end;

        fogData.skyEnd = Math.min(end, viewDistance);
        fogData.cloudEnd = end;
    }
}
