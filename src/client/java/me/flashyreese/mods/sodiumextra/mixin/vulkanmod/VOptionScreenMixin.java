package me.flashyreese.mods.sodiumextra.mixin.vulkanmod;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import net.minecraft.network.chat.Component;
import net.vulkanmod.config.gui.VOptionScreen;
import net.vulkanmod.config.option.OptionPage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static me.flashyreese.mods.sodiumextra.client.config.option.Options.*;

@Mixin(VOptionScreen.class)
public class VOptionScreenMixin {
    @Shadow @Final private List<OptionPage> optionPages;
    @Inject(method = "addPages", at = @At("TAIL"))
    void vulkanextra$addPages(CallbackInfo ci){
        OptionPage othersPage = this.optionPages.getLast();
        this.optionPages.removeLast();
        this.optionPages.add(
                new OptionPage(Component.translatable("vulkan-extra.option.animations").getString(), getAnimationsOpts())
        );
        this.optionPages.add(
                new OptionPage(parseVanillaString("options.particles").getString(), getParticlesOpts())
        );
        this.optionPages.add(
                new OptionPage(Component.translatable("vulkan-extra.option.details").getString(), getDetailsOpts())
        );
        this.optionPages.add(
                new OptionPage(Component.translatable("vulkan-extra.option.render").getString(), getRenderOpts())
        );
        this.optionPages.add(
                new OptionPage(Component.translatable("vulkan-extra.option.extras").getString(), getExtrasOpts())
        );
        this.optionPages.add(othersPage);
    }

    @Inject(method = "applyOptions", at = @At("TAIL"))
    void vulkcanextra$applyOptions(CallbackInfo ci) {
        SodiumExtraClientMod.options().writeChanges();
    }
}
