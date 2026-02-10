package io.github.amiralimollaei.mods.notenoughvulkan.mixin.compat.monitor_selector;


import net.vulkanmod.config.option.Option;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Option.class)
public interface OptionAccessor<T> {
    @Accessor(value = "value")
    T getValue();
}
