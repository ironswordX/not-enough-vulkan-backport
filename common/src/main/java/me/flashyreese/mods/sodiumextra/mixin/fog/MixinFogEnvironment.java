package me.flashyreese.mods.sodiumextra.mixin.fog;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import me.flashyreese.mods.sodiumextra.client.fog.FogEnvironmentExtended;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.FogEnvironment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FogEnvironment.class)
public abstract class MixinFogEnvironment implements FogEnvironmentExtended {

    @Override
    public void sodium_extra$applyFogSettings(FogType fogType, FogData fogData, Entity entity, BlockPos blockPos, ClientLevel level, float viewDistance, DeltaTracker deltaTracker) {
        if (fogType == FogType.ATMOSPHERIC) {
            if (!SodiumExtraClientMod.options().renderSettings.fog) {
                fogData.environmentalStart = Float.MAX_VALUE;
                fogData.environmentalEnd = Float.MAX_VALUE;
                fogData.renderDistanceStart = Float.MAX_VALUE;
                fogData.renderDistanceEnd = Float.MAX_VALUE;
                return;
            }

            float fogStart = (SodiumExtraClientMod.options().renderSettings.multiDimensionFogControl
                    ? SodiumExtraClientMod.options().renderSettings.dimensionFogStartMap.getOrDefault(level.dimensionType().effectsLocation(), 100)
                    : SodiumExtraClientMod.options().renderSettings.fogStart) / 100.0F;

            fogData.environmentalStart = fogData.environmentalStart * fogStart;
            fogData.renderDistanceStart = fogData.renderDistanceStart * fogStart;
        }
    }
}
