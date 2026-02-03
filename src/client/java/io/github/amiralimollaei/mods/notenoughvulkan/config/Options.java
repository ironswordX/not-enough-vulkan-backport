package io.github.amiralimollaei.mods.notenoughvulkan.config;

import io.github.amiralimollaei.mods.notenoughvulkan.NotEnoughVulkanClientMod;
import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import me.flashyreese.mods.sodiumextra.client.config.FogTypeConfig;
import me.flashyreese.mods.sodiumextra.client.config.SodiumExtraGameOptions;
import net.minecraft.client.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.FogType;
import net.vulkanmod.config.Platform;
import net.vulkanmod.config.gui.OptionBlock;
import net.vulkanmod.config.option.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class Options {
    static SodiumExtraGameOptions sodiumExtraOptions = SodiumExtraClientMod.options();
    static NotEnoughVulkanGameOptions notEnoughVulkanOptions = NotEnoughVulkanClientMod.options();
    static Minecraft minecraft = Minecraft.getInstance();

    public static Component parseVanillaString(String key) {
        // Strip formatting codes like "§a"
        return Component.literal(Component.translatable(key).getString().replaceAll("§.", ""));
    }

    private static Component translatableName(Identifier identifier, String category) {
        String key = identifier.toLanguageKey("options.".concat(category));
        Component translatable = Component.translatable(key);

        if (!ComponentUtils.isTranslationResolvable(translatable)) {
            translatable = Component.literal(
                    Arrays.stream(key.substring(key.lastIndexOf('.') + 1).split("_"))
                            .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
                            .collect(Collectors.joining(" "))
            );
        }
        return translatable;
    }

    private static Component translatableTooltip(Identifier identifier, String category) {
        String key = identifier.toLanguageKey("options.".concat(category)).concat(".tooltip");
        Component translatable = Component.translatable(key);

        if (!ComponentUtils.isTranslationResolvable(translatable)) {
            translatable = Component.translatable(
                    "sodium-extra.option.".concat(category).concat(".tooltips"),
                    translatableName(identifier, category)
            );
        }
        return translatable;
    }

    private static Component fogTypeName(FogType type) {
        String key = "sodium-extra.option.fog_type." + type.name().toLowerCase();
        Component translated = Component.translatable(key);

        if (!ComponentUtils.isTranslationResolvable(translated)) {
            String pretty = Arrays.stream(type.name().split("_"))
                    .map(s -> s.charAt(0) + s.substring(1).toLowerCase())
                    .collect(Collectors.joining(" ")) + " Fog";
            return Component.literal(pretty);
        }
        return translated;
    }

    private static Component fogTypeTooltip(FogType type) {
        String key = "sodium-extra.option.fog_type." + type.name().toLowerCase() + ".tooltip";
        Component translated = Component.translatable(key);

        if (!ComponentUtils.isTranslationResolvable(translated)) {
            return Component.translatable("sodium-extra.option.fog_type.default.tooltip", fogTypeName(type));
        }
        return translated;
    }

    public static OptionBlock[] getAnimationsOpts() {
        return new OptionBlock[]{
                new OptionBlock("", new Option[]{
                        new SwitchOption(parseVanillaString("gui.socialInteractions.tab_all"),
                                value -> sodiumExtraOptions.animationSettings.animation = value,
                                () -> sodiumExtraOptions.animationSettings.animation)
                                .setTooltip(Component.translatable("sodium-extra.option.animations_all.tooltip")),
                        new SwitchOption(parseVanillaString("block.minecraft.water"),
                                value -> sodiumExtraOptions.animationSettings.water = value,
                                () -> sodiumExtraOptions.animationSettings.water)
                                .setTooltip(Component.translatable("sodium-extra.option.animate_water.tooltip")),
                        new SwitchOption(parseVanillaString("block.minecraft.lava"),
                                value -> sodiumExtraOptions.animationSettings.animation = value,
                                () -> sodiumExtraOptions.animationSettings.lava)
                                .setTooltip(Component.translatable("sodium-extra.option.animate_lava.tooltip")),
                        new SwitchOption(parseVanillaString("block.minecraft.fire"),
                                value -> sodiumExtraOptions.animationSettings.animation = value,
                                () -> sodiumExtraOptions.animationSettings.fire)
                                .setTooltip(Component.translatable("sodium-extra.option.animate_fire.tooltip")),
                        new SwitchOption(parseVanillaString("block.minecraft.nether_portal"),
                                value -> sodiumExtraOptions.animationSettings.animation = value,
                                () -> sodiumExtraOptions.animationSettings.portal)
                                .setTooltip(Component.translatable("sodium-extra.option.animate_portal.tooltip")),
                        new SwitchOption(parseVanillaString("sodium-extra.option.block_animations"),
                                value -> sodiumExtraOptions.animationSettings.blockAnimations = value,
                                () -> sodiumExtraOptions.animationSettings.portal)
                                .setTooltip(Component.translatable("sodium-extra.option.block_animations.tooltip")),
                        new SwitchOption(parseVanillaString("block.minecraft.sculk_sensor"),
                                value -> sodiumExtraOptions.animationSettings.sculkSensor = value,
                                () -> sodiumExtraOptions.animationSettings.sculkSensor)
                                .setTooltip(Component.translatable("sodium-extra.option.animate_sculk_sensor.tooltip")),
                })
        };
    }

    public static OptionBlock[] getParticlesOpts() {
        OptionBlock otherParticlesGroup = new OptionBlock(
                "all", new Option[]{
                new SwitchOption(parseVanillaString("gui.socialInteractions.tab_all"),
                        value -> sodiumExtraOptions.particleSettings.particles = value,
                        () -> sodiumExtraOptions.particleSettings.particles)
                        .setTooltip(Component.translatable("sodium-extra.option.particles_all.tooltip"))
                }
        );

        OptionBlock generalParticlesGroup = new OptionBlock(
                "general", new Option[]{
                new SwitchOption(parseVanillaString("subtitles.entity.generic.splash"),
                        value -> sodiumExtraOptions.particleSettings.rainSplash = value,
                        () -> sodiumExtraOptions.particleSettings.rainSplash)
                        .setTooltip(Component.translatable("sodium-extra.option.rain_splash.tooltip")),
                new SwitchOption(parseVanillaString("subtitles.block.generic.break"),
                        value -> sodiumExtraOptions.particleSettings.blockBreak = value,
                        () -> sodiumExtraOptions.particleSettings.blockBreak)
                        .setTooltip(Component.translatable("sodium-extra.option.block_break.tooltip")),
                new SwitchOption(parseVanillaString("subtitles.block.generic.hit"),
                value -> sodiumExtraOptions.particleSettings.blockBreaking = value,
                        () -> sodiumExtraOptions.particleSettings.blockBreaking)
                        .setTooltip(Component.translatable("sodium-extra.option.block_breaking.tooltip")),
            }
        );

        List<SwitchOption> options = new ArrayList<>();
        BuiltInRegistries.PARTICLE_TYPE.keySet().stream()
                .sorted((a, b) -> translatableName(a, "particles")
                        .getString()
                        .compareToIgnoreCase(translatableName(b, "particles").getString()))
                .forEach(id -> options.add(newParticleSwitchOption(id)));
        Option<?>[] optionsArray = new Option<?>[options.size()];
        optionsArray = options.toArray(optionsArray);
        OptionBlock allParticlesGroup = new OptionBlock(
                "individual", optionsArray
        );

        return new OptionBlock[]{
                otherParticlesGroup,
                generalParticlesGroup,
                allParticlesGroup
        };
    }

    private static SwitchOption newParticleSwitchOption(Identifier id) {
        SwitchOption switchOption = new SwitchOption(
                translatableName(id, "particles"),
                value -> sodiumExtraOptions.particleSettings.otherMap.put(id, value),
                () -> sodiumExtraOptions.particleSettings.otherMap.computeIfAbsent(id, k -> true)
        );
        switchOption.setTooltip(translatableTooltip(id, "particles"));
        return switchOption;
    }

    private static SwitchOption newStandardSwitchOption(String key, Consumer<Boolean> setter, Supplier<Boolean> getter) {
        SwitchOption option = new SwitchOption(Component.translatable("sodium-extra.option."+key), setter, getter);
        option.setTooltip(Component.translatable("sodium-extra.option."+key+".tooltip"));
        return option;
    }

    public static OptionBlock[] getDetailsOpts() {
        return new OptionBlock[]{
                new OptionBlock("general", new Option[]{
                        newStandardSwitchOption("sky",
                                value -> sodiumExtraOptions.detailSettings.sky = value,
                                () -> sodiumExtraOptions.detailSettings.sky
                        ),
                        newStandardSwitchOption("stars",
                                value -> sodiumExtraOptions.detailSettings.stars = value,
                                () -> sodiumExtraOptions.detailSettings.stars
                        ),
                        newStandardSwitchOption("sun",
                                value -> sodiumExtraOptions.detailSettings.sun = value,
                                () -> sodiumExtraOptions.detailSettings.sun
                        ),
                        newStandardSwitchOption("moon",
                                value -> sodiumExtraOptions.detailSettings.moon = value,
                                () -> sodiumExtraOptions.detailSettings.moon
                        ),
                        new SwitchOption(parseVanillaString("soundCategory.weather"),
                                value -> sodiumExtraOptions.detailSettings.sky = value,
                                () -> sodiumExtraOptions.detailSettings.sky
                        ).setTooltip(Component.translatable("sodium-extra.option.rain_snow.tooltip")),
                        newStandardSwitchOption("biome_colors",
                                value -> sodiumExtraOptions.detailSettings.biomeColors = value,
                                () -> sodiumExtraOptions.detailSettings.biomeColors
                        ),
                        newStandardSwitchOption("sky_colors",
                                value -> sodiumExtraOptions.detailSettings.skyColors = value,
                                () -> sodiumExtraOptions.detailSettings.skyColors
                        ),
                })
        };
    }

    public static OptionBlock[] getRenderOpts() {
        List<OptionBlock> optionBlocks = new ArrayList<>();

        optionBlocks.add(new OptionBlock("global fog", new Option[]{
                newStandardSwitchOption("global_fog",
                        value -> sodiumExtraOptions.renderSettings.globalFog = value,
                        () -> sodiumExtraOptions.renderSettings.globalFog
                )
        }));

        Arrays.stream(FogType.values())
                .sorted(Comparator.comparing(Enum::name))
                .filter(type -> type != FogType.NONE)
                .forEach(fogtype -> optionBlocks.add(Options.newFogRenderOptionBlock(fogtype)));

        optionBlocks.add(new OptionBlock("light updates", new Option[]{
                newStandardSwitchOption("light_updates",
                        value -> sodiumExtraOptions.renderSettings.lightUpdates = value,
                        () -> sodiumExtraOptions.renderSettings.lightUpdates
                )
        }));

        optionBlocks.add(new OptionBlock("render", new Option[]{
                new SwitchOption(
                        parseVanillaString("entity.minecraft.item_frame"),
                        value -> sodiumExtraOptions.renderSettings.itemFrame = value,
                        () -> sodiumExtraOptions.renderSettings.itemFrame
                ).setTooltip(Component.translatable("sodium-extra.option.item_frames.tooltip")),
                new SwitchOption(
                        parseVanillaString("entity.minecraft.armor_stand"),
                        value -> sodiumExtraOptions.renderSettings.armorStand = value,
                        () -> sodiumExtraOptions.renderSettings.armorStand
                ).setTooltip(Component.translatable("sodium-extra.option.armor_stands.tooltip")),
                new SwitchOption(
                        parseVanillaString("entity.minecraft.painting"),
                        value -> sodiumExtraOptions.renderSettings.painting = value,
                        () -> sodiumExtraOptions.renderSettings.painting
                ).setTooltip(Component.translatable("sodium-extra.option.paintings.tooltip"))
        }));

        optionBlocks.add(new OptionBlock("render.block", new Option[]{
                new SwitchOption(
                        Component.translatable("sodium-extra.option.beacon_beam"),
                        value -> sodiumExtraOptions.renderSettings.beaconBeam = value,
                        () -> sodiumExtraOptions.renderSettings.beaconBeam
                ).setTooltip(Component.translatable("sodium-extra.option.beacon_beam.tooltip")),
                new SwitchOption(
                        Component.translatable("sodium-extra.option.limit_beacon_beam_height"),
                        value -> sodiumExtraOptions.renderSettings.limitBeaconBeamHeight = value,
                        () -> sodiumExtraOptions.renderSettings.limitBeaconBeamHeight
                ).setTooltip(Component.translatable("sodium-extra.option.limit_beacon_beam_height.tooltip")),
                new SwitchOption(
                        Component.translatable("sodium-extra.option.enchanting_table_book"),
                        value -> sodiumExtraOptions.renderSettings.enchantingTableBook = value,
                        () -> sodiumExtraOptions.renderSettings.enchantingTableBook
                ).setTooltip(Component.translatable("sodium-extra.option.enchanting_table_book.tooltip")),
                new SwitchOption(
                        parseVanillaString("block.minecraft.piston"),
                        value -> sodiumExtraOptions.renderSettings.piston = value,
                        () -> sodiumExtraOptions.renderSettings.piston
                ).setTooltip(Component.translatable("sodium-extra.option.piston.tooltip"))
        }));

        optionBlocks.add(new OptionBlock("name tag", new Option[]{
                new SwitchOption(
                        Component.translatable("sodium-extra.option.item_frame_name_tag"),
                        value -> sodiumExtraOptions.renderSettings.itemFrameNameTag = value,
                        () -> sodiumExtraOptions.renderSettings.itemFrameNameTag
                ).setTooltip(Component.translatable("sodium-extra.option.item_frame_name_tag.tooltip")),
                new SwitchOption(
                        Component.translatable("sodium-extra.option.player_name_tag"),
                        value -> sodiumExtraOptions.renderSettings.playerNameTag = value,
                        () -> sodiumExtraOptions.renderSettings.playerNameTag
                ).setTooltip(Component.translatable("sodium-extra.option.player_name_tag.tooltip")),
        }));

        OptionBlock[] optionBlockArray = new OptionBlock[optionBlocks.size()];
        optionBlockArray = optionBlocks.toArray(optionBlockArray);

        return optionBlockArray;
    }

    private static OptionBlock newFogRenderOptionBlock(FogType fogType) {
        // Environment Start
        RangeOption envStart = new RangeOption(
                Component.translatable("sodium-extra.option.fog_type.environment_start", fogTypeName(fogType)),
                0, 300, 1,
                val -> {
                    FogTypeConfig ftconfig = sodiumExtraOptions.renderSettings.fogTypeConfig.computeIfAbsent(fogType, k -> new FogTypeConfig());
                    ftconfig.environmentStartMultiplier = val;
                },
                () -> sodiumExtraOptions.renderSettings.fogTypeConfig.computeIfAbsent(fogType, k -> new FogTypeConfig()).environmentStartMultiplier
        );
        envStart.setTooltip(Component.translatable("sodium-extra.option.fog_type.environment_start.tooltip"));

        // Environment End
        RangeOption envEnd = new RangeOption(
                Component.translatable("sodium-extra.option.fog_type.environment_end", fogTypeName(fogType)),
                0, 300, 1,
                val -> {
                    FogTypeConfig ftconfig = sodiumExtraOptions.renderSettings.fogTypeConfig.computeIfAbsent(fogType, k -> new FogTypeConfig());
                    ftconfig.environmentEndMultiplier = val;
                },
                () -> sodiumExtraOptions.renderSettings.fogTypeConfig.computeIfAbsent(fogType, k -> new FogTypeConfig()).environmentEndMultiplier
        );
        envEnd.setTooltip(Component.translatable("sodium-extra.option.fog_type.environment_end.tooltip"));

        // Render Start
        RangeOption renderStart = new RangeOption(
                Component.translatable("sodium-extra.option.fog_type.render_distance_start", fogTypeName(fogType)),
                0, 300, 1,
                val -> {
                    FogTypeConfig ftconfig = sodiumExtraOptions.renderSettings.fogTypeConfig.computeIfAbsent(fogType, k -> new FogTypeConfig());
                    ftconfig.renderDistanceStartMultiplier = val;
                },
                () -> sodiumExtraOptions.renderSettings.fogTypeConfig.computeIfAbsent(fogType, k -> new FogTypeConfig()).renderDistanceStartMultiplier
        );
        renderStart.setTooltip(Component.translatable("sodium-extra.option.fog_type.render_distance_start.tooltip"));

        // Render End
        RangeOption renderEnd = new RangeOption(
                Component.translatable("sodium-extra.option.fog_type.render_distance_end", fogTypeName(fogType)),
                0, 300, 1,
                val -> {
                    FogTypeConfig ftconfig = sodiumExtraOptions.renderSettings.fogTypeConfig.computeIfAbsent(fogType, k -> new FogTypeConfig());
                    ftconfig.renderDistanceEndMultiplier = val;
                },
                () -> sodiumExtraOptions.renderSettings.fogTypeConfig.computeIfAbsent(fogType, k -> new FogTypeConfig()).renderDistanceEndMultiplier
        );
        renderEnd.setTooltip(Component.translatable("sodium-extra.option.fog_type.render_distance_end.tooltip"));

        // Sky End
        RangeOption skyEnd = new RangeOption(
                Component.translatable("sodium-extra.option.fog_type.sky_end", fogTypeName(fogType)),
                0, 300, 1,
                val -> {
                    FogTypeConfig ftconfig = sodiumExtraOptions.renderSettings.fogTypeConfig.computeIfAbsent(fogType, k -> new FogTypeConfig());
                    ftconfig.skyEndMultiplier = val;
                },
                () -> sodiumExtraOptions.renderSettings.fogTypeConfig.computeIfAbsent(fogType, k -> new FogTypeConfig()).skyEndMultiplier
        );
        skyEnd.setTooltip(Component.translatable("sodium-extra.option.fog_type.sky_end.tooltip"));

        // Sky End
        RangeOption cloudEnd = new RangeOption(
                Component.translatable("sodium-extra.option.fog_type.cloud_end", fogTypeName(fogType)),
                0, 300, 1,
                val -> {
                    FogTypeConfig ftconfig = sodiumExtraOptions.renderSettings.fogTypeConfig.computeIfAbsent(fogType, k -> new FogTypeConfig());
                    ftconfig.cloudEndMultiplier = val;
                },
                () -> sodiumExtraOptions.renderSettings.fogTypeConfig.computeIfAbsent(fogType, k -> new FogTypeConfig()).cloudEndMultiplier
        );
        cloudEnd.setTooltip(Component.translatable("sodium-extra.option.fog_type.cloud_end.tooltip"));

        SwitchOption all = new SwitchOption(
                fogTypeName(fogType),
                (val) -> sodiumExtraOptions.renderSettings.fogTypeConfig.computeIfAbsent(fogType, k -> new FogTypeConfig()).enable = val,
                () -> sodiumExtraOptions.renderSettings.fogTypeConfig.computeIfAbsent(fogType, k -> new FogTypeConfig()).enable
        );
        all.setTooltip(fogTypeTooltip(fogType));

        // Add group
        return new OptionBlock(
                "fogtype: "+fogType.name(), new Option[]{
                all,
                envStart,
                envEnd,
                renderStart,
                renderEnd,
                skyEnd,
                cloudEnd
        });
    }

    public static OptionBlock[] getExtrasOpts() {
        SwitchOption reduceResolutionOnMac = new SwitchOption(
                Component.translatable("sodium-extra.option.reduce_resolution_on_mac"),
                (value) -> sodiumExtraOptions.extraSettings.reduceResolutionOnMac = value,
                () -> sodiumExtraOptions.extraSettings.reduceResolutionOnMac
        );
        reduceResolutionOnMac.setTooltip(Component.translatable("sodium-extra.option.reduce_resolution_on_mac.tooltip"));
        reduceResolutionOnMac.setActivationFn(
                () -> SodiumExtraClientMod.mixinConfig().getOptions().get("mixin.reduce_resolution_on_mac").isEnabled() && Platform.isMacOS()
        );
        SwitchOption skipWaylandPatches = new SwitchOption(
                Component.translatable("not-enough-vulkan.option.skip_wayland_patches"),
                (value) -> notEnoughVulkanOptions.vulkanSettings.skipWaylandPatches = value,
                () -> notEnoughVulkanOptions.vulkanSettings.skipWaylandPatches
        );
        skipWaylandPatches.setTooltip(Component.translatable("not-enough-vulkan.option.skip_wayland_patches.tooltip"));
        skipWaylandPatches.setActivationFn(
                () -> NotEnoughVulkanClientMod.mixinConfig().getOptions().get("mixin.compat.skip_wayland_patches").isEnabled() && Platform.isWayLand()
        );
        return new OptionBlock[]{
                new OptionBlock("reduce_resolution_on_mac", new Option[]{
                        reduceResolutionOnMac
                }),
                new OptionBlock("skip_wayland_compatibility_patches", new Option[]{
                        skipWaylandPatches
                }),
                new OptionBlock("overlay", new Option[]{
                        new CyclingOption<>(
                                Component.translatable("sodium-extra.option.overlay_corner"),
                                SodiumExtraGameOptions.OverlayCorner.values(),
                                (value) -> sodiumExtraOptions.extraSettings.overlayCorner = value,
                                () -> sodiumExtraOptions.extraSettings.overlayCorner
                        ).setTooltip(Component.translatable("sodium-extra.option.overlay_corner.tooltip"))
                        .setTranslator(SodiumExtraGameOptions.OverlayCorner::getLocalizedName),
                        new CyclingOption<>(
                                Component.translatable("sodium-extra.option.text_contrast"),
                                SodiumExtraGameOptions.TextContrast.values(),
                                (value) -> sodiumExtraOptions.extraSettings.textContrast = value,
                                () -> sodiumExtraOptions.extraSettings.textContrast
                        ).setTooltip(Component.translatable("sodium-extra.option.text_contrast.tooltip"))
                        .setTranslator(SodiumExtraGameOptions.TextContrast::getLocalizedName),
                        new SwitchOption(Component.translatable("sodium-extra.option.show_fps"),
                                (value) -> sodiumExtraOptions.extraSettings.showFps = value,
                                () -> sodiumExtraOptions.extraSettings.showFps
                        ).setTooltip(Component.translatable("sodium-extra.option.show_fps.tooltip")),
                        new SwitchOption(Component.translatable("sodium-extra.option.show_fps_extended"),
                                (value) -> sodiumExtraOptions.extraSettings.showFPSExtended = value,
                                () -> sodiumExtraOptions.extraSettings.showFPSExtended
                        ).setTooltip(Component.translatable("sodium-extra.option.show_coordinates.tooltip")),
                        new SwitchOption(Component.translatable("sodium-extra.option.show_coordinates"),
                                (value) -> sodiumExtraOptions.extraSettings.showCoords = value,
                                () -> sodiumExtraOptions.extraSettings.showCoords
                        ).setTooltip(Component.translatable("sodium-extra.option.show_coordinates.tooltip")),
                        new RangeOption(Component.translatable("sodium-extra.option.cloud_height"),
                                -64, 319, 1,
                                (value) -> sodiumExtraOptions.extraSettings.cloudHeight = value,
                                () -> sodiumExtraOptions.extraSettings.cloudHeight
                        ).setTooltip(Component.translatable("sodium-extra.option.cloud_height.tooltip"))
                }),
                new OptionBlock("advanced item tooltips", new Option[]{
                        new SwitchOption(
                                Component.translatable("sodium-extra.option.advanced_item_tooltips"),
                                (value) -> {
                                    Minecraft.getInstance().options.advancedItemTooltips = value;
                                    Minecraft.getInstance().options.save();
                                },
                                () -> Minecraft.getInstance().options.advancedItemTooltips
                        ).setTooltip(Component.translatable("sodium-extra.option.advanced_item_tooltips.tooltip"))
                }),
                new OptionBlock("toasts", new Option[]{
                        new SwitchOption(
                                Component.translatable("sodium-extra.option.toasts"),
                                (value) -> sodiumExtraOptions.extraSettings.toasts = value,
                                () -> sodiumExtraOptions.extraSettings.toasts
                        ).setTooltip(Component.translatable("sodium-extra.option.toasts.tooltip")),
                        new SwitchOption(
                                Component.translatable("sodium-extra.option.advancement_toast"),
                                (value) -> sodiumExtraOptions.extraSettings.advancementToast = value,
                                () -> sodiumExtraOptions.extraSettings.advancementToast
                        ).setTooltip(Component.translatable("sodium-extra.option.advancement_toast.tooltip")),
                        new SwitchOption(
                                Component.translatable("sodium-extra.option.recipe_toast"),
                                (value) -> sodiumExtraOptions.extraSettings.recipeToast = value,
                                () -> sodiumExtraOptions.extraSettings.recipeToast
                        ).setTooltip(Component.translatable("sodium-extra.option.recipe_toast.tooltip")),
                        new SwitchOption(
                                Component.translatable("sodium-extra.option.system_toast"),
                                (value) -> sodiumExtraOptions.extraSettings.systemToast = value,
                                () -> sodiumExtraOptions.extraSettings.systemToast
                        ).setTooltip(Component.translatable("sodium-extra.option.system_toast.tooltip")),
                        new SwitchOption(
                                Component.translatable("sodium-extra.option.tutorial_toast"),
                                (value) -> sodiumExtraOptions.extraSettings.tutorialToast = value,
                                () -> sodiumExtraOptions.extraSettings.tutorialToast
                        ).setTooltip(Component.translatable("sodium-extra.option.tutorial_toast.tooltip"))
                }),
                new OptionBlock("other", new Option[]{
                        new SwitchOption(
                                Component.translatable("sodium-extra.option.instant_sneak"),
                                (value) -> sodiumExtraOptions.extraSettings.instantSneak = value,
                                () -> sodiumExtraOptions.extraSettings.instantSneak
                        ).setTooltip(Component.translatable("sodium-extra.option.instant_sneak.tooltip")),
                        new SwitchOption(
                                Component.translatable("sodium-extra.option.prevent_shaders"),
                                (value) -> {
                                    sodiumExtraOptions.extraSettings.preventShaders = value;
                                    minecraft.levelRenderer.allChanged();
                                },
                                () -> sodiumExtraOptions.extraSettings.preventShaders
                        ).setTooltip(Component.translatable("sodium-extra.option.prevent_shaders.tooltip"))
                }),
                new OptionBlock("debug hud", new Option[]{
                        new SwitchOption(
                                Component.translatable("sodium-extra.option.steady_debug_hud"),
                                (value) -> sodiumExtraOptions.extraSettings.steadyDebugHud = value,
                                () -> sodiumExtraOptions.extraSettings.steadyDebugHud
                        ).setTooltip(Component.translatable("sodium-extra.option.steady_debug_hud.tooltip")),
                        new RangeOption(
                                Component.translatable("sodium-extra.option.steady_debug_hud_refresh_interval"),
                                1, 20, 1,
                                (value) -> sodiumExtraOptions.extraSettings.steadyDebugHudRefreshInterval = value,
                                () -> sodiumExtraOptions.extraSettings.steadyDebugHudRefreshInterval
                        ).setTooltip(Component.translatable("sodium-extra.option.steady_debug_hud_refresh_interval.tooltip"))
                }),
        };
    }
}