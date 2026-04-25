package io.github.amiralimollaei.mods.notenoughvulkan.mixin.core;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.amiralimollaei.mods.notenoughvulkan.NotEnoughVulkanClientMod;
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

import static io.github.amiralimollaei.mods.notenoughvulkan.config.Options.*;

@Mixin(value = VOptionScreen.class, remap = false)
public class VOptionScreenMixin {
    @Shadow @Final private List<OptionPage> optionPages;
    @ModifyReturnValue(method = "addPages", at = @At("TAIL"))
    void notenoughvulkan$addPages(CallbackInfo ci) {
        OptionPage othersPage = this.optionPages.getLast();
        this.optionPages.removeLast();
        this.optionPages.add(
                new OptionPage(Component.translatable("sodium-extra.option.animations").getString(), getAnimationsOpts())
        );
        this.optionPages.add(
                new OptionPage(parseVanillaString("options.particles").getString(), getParticlesOpts())
        );
        this.optionPages.add(
                new OptionPage(Component.translatable("sodium-extra.option.details").getString(), getDetailsOpts())
        );
        this.optionPages.add(
                new OptionPage(Component.translatable("sodium-extra.option.render").getString(), getRenderOpts())
        );
        this.optionPages.add(
                new OptionPage(Component.translatable("sodium-extra.option.extras").getString(), getExtrasOpts())
        );
        this.optionPages.add(othersPage);
    }

    @Inject(method = "applyOptions", at = @At("TAIL"))
    void notenoughvulkan$applyOptions(CallbackInfo ci) {
        SodiumExtraClientMod.options().writeChanges();
        NotEnoughVulkanClientMod.options().writeChanges();
    }
}
