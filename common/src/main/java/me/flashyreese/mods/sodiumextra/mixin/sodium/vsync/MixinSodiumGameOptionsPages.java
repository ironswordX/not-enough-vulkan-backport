package me.flashyreese.mods.sodiumextra.mixin.sodium.vsync;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import me.flashyreese.mods.sodiumextra.client.config.SodiumExtraGameOptions;
import net.caffeinemc.mods.sodium.api.config.structure.EnumOptionBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.OptionBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.OptionGroupBuilder;
import net.caffeinemc.mods.sodium.client.gui.SodiumConfigBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = SodiumConfigBuilder.class, remap = false)
public class MixinSodiumGameOptionsPages {

    //todo: https://github.com/CaffeineMC/sodium/pull/3360
    /*@Redirect(method = "buildGeneralPage", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/api/config/structure/OptionGroupBuilder;addOption(Lnet/caffeinemc/mods/sodium/api/config/structure/OptionBuilder;)Lnet/caffeinemc/mods/sodium/api/config/structure/OptionGroupBuilder;", ordinal = 6), remap = false)
    private static OptionGroupBuilder redirectVsyncToggle(OptionGroupBuilder instance, OptionBuilder optionBuilder) {
        return instance.addOption(((EnumOptionBuilder<SodiumExtraGameOptions.VerticalSyncOption>)optionBuilder)
                .setDefaultValue(SodiumExtraGameOptions.VerticalSyncOption.ON)
                .setName(Component.translatable("options.vsync"))
                .setTooltip(Component.literal(Component.translatable("sodium.options.v_sync.tooltip").getString() + "\n- " + Component.translatable("sodium-extra.option.use_adaptive_sync.name").getString() + ": " + Component.translatable("sodium-extra.option.use_adaptive_sync.tooltip").getString()))
                .setBinding((value) -> {
                    switch (value) {
                        case OFF -> {
                            SodiumExtraClientMod.options().extraSettings.useAdaptiveSync = false;
                            Minecraft.getInstance().options.enableVsync().set(false);
                        }
                        case ON -> {
                            SodiumExtraClientMod.options().extraSettings.useAdaptiveSync = false;
                            Minecraft.getInstance().options.enableVsync().set(true);
                        }
                        case ADAPTIVE -> {
                            SodiumExtraClientMod.options().extraSettings.useAdaptiveSync = true;
                            Minecraft.getInstance().options.enableVsync().set(true);
                        }
                    }
                }, () -> {
                    if ( Minecraft.getInstance().options.enableVsync().get() && !SodiumExtraClientMod.options().extraSettings.useAdaptiveSync) {
                        return SodiumExtraGameOptions.VerticalSyncOption.ON;
                    } else if (!Minecraft.getInstance().options.enableVsync().get() && !SodiumExtraClientMod.options().extraSettings.useAdaptiveSync) {
                        return SodiumExtraGameOptions.VerticalSyncOption.OFF;
                    } else {
                        return SodiumExtraGameOptions.VerticalSyncOption.ADAPTIVE;
                    }
                })
                .setStorageHandler(() -> {
                    SodiumExtraClientMod.options().afterSave();
                    Minecraft.getInstance().options.save();
                }));
    }*/
}
