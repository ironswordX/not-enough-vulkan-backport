package me.flashyreese.mods.sodiumextra.client.config.option;

import me.flashyreese.mods.sodiumextra.client.SodiumExtraClientMod;
import me.flashyreese.mods.sodiumextra.client.config.FogTypeConfig;
import me.flashyreese.mods.sodiumextra.client.config.SodiumExtraGameOptions;
import net.minecraft.client.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.FogType;
import net.vulkanmod.config.gui.OptionBlock;
import net.vulkanmod.config.option.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class Options {
    static SodiumExtraGameOptions config = SodiumExtraClientMod.options();
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
                                value -> config.animationSettings.animation = value,
                                () -> config.animationSettings.animation)
                                .setTooltip(Component.translatable("sodium-extra.option.animations_all.tooltip")),
                        new SwitchOption(parseVanillaString("block.minecraft.water"),
                                value -> config.animationSettings.water = value,
                                () -> config.animationSettings.water)
                                .setTooltip(Component.translatable("sodium-extra.option.animate_water.tooltip")),
                        new SwitchOption(parseVanillaString("block.minecraft.lava"),
                                value -> config.animationSettings.animation = value,
                                () -> config.animationSettings.lava)
                                .setTooltip(Component.translatable("sodium-extra.option.animate_lava.tooltip")),
                        new SwitchOption(parseVanillaString("block.minecraft.fire"),
                                value -> config.animationSettings.animation = value,
                                () -> config.animationSettings.fire)
                                .setTooltip(Component.translatable("sodium-extra.option.animate_fire.tooltip")),
                        new SwitchOption(parseVanillaString("block.minecraft.nether_portal"),
                                value -> config.animationSettings.animation = value,
                                () -> config.animationSettings.portal)
                                .setTooltip(Component.translatable("sodium-extra.option.animate_portal.tooltip")),
                        new SwitchOption(parseVanillaString("sodium-extra.option.block_animations"),
                                value -> config.animationSettings.blockAnimations = value,
                                () -> config.animationSettings.portal)
                                .setTooltip(Component.translatable("sodium-extra.option.block_animations.tooltip")),
                        new SwitchOption(parseVanillaString("block.minecraft.sculk_sensor"),
                                value -> config.animationSettings.sculkSensor = value,
                                () -> config.animationSettings.sculkSensor)
                                .setTooltip(Component.translatable("sodium-extra.option.animate_sculk_sensor.tooltip")),
                })
        };
    }

    public static OptionBlock[] getParticlesOpts() {
        OptionBlock otherParticlesGroup = new OptionBlock(
                "all", new Option[]{
                new SwitchOption(parseVanillaString("gui.socialInteractions.tab_all"),
                        value -> config.particleSettings.particles = value,
                        () -> config.particleSettings.particles)
                        .setTooltip(Component.translatable("sodium-extra.option.particles_all.tooltip"))
                }
        );

        OptionBlock generalParticlesGroup = new OptionBlock(
                "general", new Option[]{
                new SwitchOption(parseVanillaString("subtitles.entity.generic.splash"),
                        value -> config.particleSettings.rainSplash = value,
                        () -> config.particleSettings.rainSplash)
                        .setTooltip(Component.translatable("sodium-extra.option.rain_splash.tooltip")),
                new SwitchOption(parseVanillaString("subtitles.block.generic.break"),
                        value -> config.particleSettings.blockBreak = value,
                        () -> config.particleSettings.blockBreak)
                        .setTooltip(Component.translatable("sodium-extra.option.block_break.tooltip")),
                new SwitchOption(parseVanillaString("subtitles.block.generic.hit"),
                value -> config.particleSettings.blockBreaking = value,
                        () -> config.particleSettings.blockBreaking)
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
                value -> config.particleSettings.otherMap.put(id, value),
                () -> config.particleSettings.otherMap.computeIfAbsent(id, k -> true)
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
                                value -> config.detailSettings.sky = value,
                                () -> config.detailSettings.sky
                        ),
                        newStandardSwitchOption("stars",
                                value -> config.detailSettings.stars = value,
                                () -> config.detailSettings.stars
                        ),
                        newStandardSwitchOption("sun",
                                value -> config.detailSettings.sun = value,
                                () -> config.detailSettings.sun
                        ),
                        newStandardSwitchOption("moon",
                                value -> config.detailSettings.moon = value,
                                () -> config.detailSettings.moon
                        ),
                        new SwitchOption(parseVanillaString("soundCategory.weather"),
                                value -> config.detailSettings.sky = value,
                                () -> config.detailSettings.sky
                        ).setTooltip(Component.translatable("sodium-extra.option.rain_snow.tooltip")),
                        newStandardSwitchOption("biome_colors",
                                value -> config.detailSettings.biomeColors = value,
                                () -> config.detailSettings.biomeColors
                        ),
                        newStandardSwitchOption("sky_colors",
                                value -> config.detailSettings.skyColors = value,
                                () -> config.detailSettings.skyColors
                        ),
                })
        };
    }

    public static OptionBlock[] getRenderOpts() {
        List<OptionBlock> optionBlocks = new ArrayList<>();

        optionBlocks.add(new OptionBlock("global fog", new Option[]{
                newStandardSwitchOption("global_fog",
                        value -> config.renderSettings.globalFog = value,
                        () -> config.renderSettings.globalFog
                )
        }));

        Arrays.stream(FogType.values())
                .sorted(Comparator.comparing(Enum::name))
                .filter(type -> type != FogType.NONE)
                .forEach(fogtype -> optionBlocks.add(Options.newFogRenderOptionBlock(fogtype)));

        optionBlocks.add(new OptionBlock("light updates", new Option[]{
                newStandardSwitchOption("light_updates",
                        value -> config.renderSettings.lightUpdates = value,
                        () -> config.renderSettings.lightUpdates
                )
        }));

        optionBlocks.add(new OptionBlock("render", new Option[]{
                new SwitchOption(
                        parseVanillaString("entity.minecraft.item_frame"),
                        value -> config.renderSettings.itemFrame = value,
                        () -> config.renderSettings.itemFrame
                ).setTooltip(Component.translatable("sodium-extra.option.item_frames.tooltip")),
                new SwitchOption(
                        parseVanillaString("entity.minecraft.armor_stand"),
                        value -> config.renderSettings.armorStand = value,
                        () -> config.renderSettings.armorStand
                ).setTooltip(Component.translatable("sodium-extra.option.armor_stands.tooltip")),
                new SwitchOption(
                        parseVanillaString("entity.minecraft.painting"),
                        value -> config.renderSettings.painting = value,
                        () -> config.renderSettings.painting
                ).setTooltip(Component.translatable("sodium-extra.option.paintings.tooltip"))
        }));

        optionBlocks.add(new OptionBlock("render.block", new Option[]{
                new SwitchOption(
                        Component.translatable("sodium-extra.option.beacon_beam"),
                        value -> config.renderSettings.beaconBeam = value,
                        () -> config.renderSettings.beaconBeam
                ).setTooltip(Component.translatable("sodium-extra.option.beacon_beam.tooltip")),
                new SwitchOption(
                        Component.translatable("sodium-extra.option.limit_beacon_beam_height"),
                        value -> config.renderSettings.limitBeaconBeamHeight = value,
                        () -> config.renderSettings.limitBeaconBeamHeight
                ).setTooltip(Component.translatable("sodium-extra.option.limit_beacon_beam_height.tooltip")),
                new SwitchOption(
                        Component.translatable("sodium-extra.option.enchanting_table_book"),
                        value -> config.renderSettings.enchantingTableBook = value,
                        () -> config.renderSettings.enchantingTableBook
                ).setTooltip(Component.translatable("sodium-extra.option.enchanting_table_book.tooltip")),
                new SwitchOption(
                        parseVanillaString("block.minecraft.piston"),
                        value -> config.renderSettings.piston = value,
                        () -> config.renderSettings.piston
                ).setTooltip(Component.translatable("sodium-extra.option.piston.tooltip"))
        }));

        optionBlocks.add(new OptionBlock("name tag", new Option[]{
                new SwitchOption(
                        Component.translatable("sodium-extra.option.item_frame_name_tag"),
                        value -> config.renderSettings.itemFrameNameTag = value,
                        () -> config.renderSettings.itemFrameNameTag
                ).setTooltip(Component.translatable("sodium-extra.option.item_frame_name_tag.tooltip")),
                new SwitchOption(
                        Component.translatable("sodium-extra.option.player_name_tag"),
                        value -> config.renderSettings.playerNameTag = value,
                        () -> config.renderSettings.playerNameTag
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
                    FogTypeConfig ftconfig = config.renderSettings.fogTypeConfig.computeIfAbsent(fogType, k -> new FogTypeConfig());
                    ftconfig.environmentStartMultiplier = val;
                },
                () -> config.renderSettings.fogTypeConfig.computeIfAbsent(fogType, k -> new FogTypeConfig()).environmentStartMultiplier
        );
        envStart.setTooltip(Component.translatable("sodium-extra.option.fog_type.environment_start.tooltip"));

        // Environment End
        RangeOption envEnd = new RangeOption(
                Component.translatable("sodium-extra.option.fog_type.environment_end", fogTypeName(fogType)),
                0, 300, 1,
                val -> {
                    FogTypeConfig ftconfig = config.renderSettings.fogTypeConfig.computeIfAbsent(fogType, k -> new FogTypeConfig());
                    ftconfig.environmentEndMultiplier = val;
                },
                () -> config.renderSettings.fogTypeConfig.computeIfAbsent(fogType, k -> new FogTypeConfig()).environmentEndMultiplier
        );
        envEnd.setTooltip(Component.translatable("sodium-extra.option.fog_type.environment_end.tooltip"));

        // Render Start
        RangeOption renderStart = new RangeOption(
                Component.translatable("sodium-extra.option.fog_type.render_distance_start", fogTypeName(fogType)),
                0, 300, 1,
                val -> {
                    FogTypeConfig ftconfig = config.renderSettings.fogTypeConfig.computeIfAbsent(fogType, k -> new FogTypeConfig());
                    ftconfig.renderDistanceStartMultiplier = val;
                },
                () -> config.renderSettings.fogTypeConfig.computeIfAbsent(fogType, k -> new FogTypeConfig()).renderDistanceStartMultiplier
        );
        renderStart.setTooltip(Component.translatable("sodium-extra.option.fog_type.render_distance_start.tooltip"));

        // Render End
        RangeOption renderEnd = new RangeOption(
                Component.translatable("sodium-extra.option.fog_type.render_distance_end", fogTypeName(fogType)),
                0, 300, 1,
                val -> {
                    FogTypeConfig ftconfig = config.renderSettings.fogTypeConfig.computeIfAbsent(fogType, k -> new FogTypeConfig());
                    ftconfig.renderDistanceEndMultiplier = val;
                },
                () -> config.renderSettings.fogTypeConfig.computeIfAbsent(fogType, k -> new FogTypeConfig()).renderDistanceEndMultiplier
        );
        renderEnd.setTooltip(Component.translatable("sodium-extra.option.fog_type.render_distance_end.tooltip"));

        // Sky End
        RangeOption skyEnd = new RangeOption(
                Component.translatable("sodium-extra.option.fog_type.sky_end", fogTypeName(fogType)),
                0, 300, 1,
                val -> {
                    FogTypeConfig ftconfig = config.renderSettings.fogTypeConfig.computeIfAbsent(fogType, k -> new FogTypeConfig());
                    ftconfig.skyEndMultiplier = val;
                },
                () -> config.renderSettings.fogTypeConfig.computeIfAbsent(fogType, k -> new FogTypeConfig()).skyEndMultiplier
        );
        skyEnd.setTooltip(Component.translatable("sodium-extra.option.fog_type.sky_end.tooltip"));

        // Sky End
        RangeOption cloudEnd = new RangeOption(
                Component.translatable("sodium-extra.option.fog_type.cloud_end", fogTypeName(fogType)),
                0, 300, 1,
                val -> {
                    FogTypeConfig ftconfig = config.renderSettings.fogTypeConfig.computeIfAbsent(fogType, k -> new FogTypeConfig());
                    ftconfig.cloudEndMultiplier = val;
                },
                () -> config.renderSettings.fogTypeConfig.computeIfAbsent(fogType, k -> new FogTypeConfig()).cloudEndMultiplier
        );
        cloudEnd.setTooltip(Component.translatable("sodium-extra.option.fog_type.cloud_end.tooltip"));

        SwitchOption all = new SwitchOption(
                fogTypeName(fogType),
                (val) -> config.renderSettings.fogTypeConfig.computeIfAbsent(fogType, k -> new FogTypeConfig()).enable = val,
                () -> config.renderSettings.fogTypeConfig.computeIfAbsent(fogType, k -> new FogTypeConfig()).enable
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
        return new OptionBlock[]{
                new OptionBlock("reduce_resolution_on_mac", (SodiumExtraClientMod.mixinConfig().getOptions().get("mixin.reduce_resolution_on_mac").isEnabled() && System.getProperty("os.name").toLowerCase().contains("mac")) ? new Option[]{
                        new SwitchOption(
                                Component.translatable("sodium-extra.option.reduce_resolution_on_mac"),
                                (value) -> config.extraSettings.reduceResolutionOnMac = value,
                                () -> config.extraSettings.reduceResolutionOnMac
                        ).setTooltip(Component.translatable("sodium-extra.option.reduce_resolution_on_mac.tooltip"))
                } : new Option<?>[]{}),
                new OptionBlock("overlay", new Option[]{
                        new CyclingOption<>(
                                Component.translatable("sodium-extra.option.overlay_corner"),
                                SodiumExtraGameOptions.OverlayCorner.values(),
                                (value) -> config.extraSettings.overlayCorner = value,
                                () -> config.extraSettings.overlayCorner
                        ).setTooltip(Component.translatable("sodium-extra.option.overlay_corner.tooltip"))
                        .setTranslator(SodiumExtraGameOptions.OverlayCorner::getLocalizedName),
                        new CyclingOption<>(
                                Component.translatable("sodium-extra.option.text_contrast"),
                                SodiumExtraGameOptions.TextContrast.values(),
                                (value) -> config.extraSettings.textContrast = value,
                                () -> config.extraSettings.textContrast
                        ).setTooltip(Component.translatable("sodium-extra.option.text_contrast.tooltip"))
                        .setTranslator(SodiumExtraGameOptions.TextContrast::getLocalizedName),
                        new SwitchOption(Component.translatable("sodium-extra.option.show_fps"),
                                (value) -> config.extraSettings.showFps = value,
                                () -> config.extraSettings.showFps
                        ).setTooltip(Component.translatable("sodium-extra.option.show_fps.tooltip")),
                        new SwitchOption(Component.translatable("sodium-extra.option.show_fps_extended"),
                                (value) -> config.extraSettings.showFPSExtended = value,
                                () -> config.extraSettings.showFPSExtended
                        ).setTooltip(Component.translatable("sodium-extra.option.show_coordinates.tooltip")),
                        new SwitchOption(Component.translatable("sodium-extra.option.show_coordinates"),
                                (value) -> config.extraSettings.showCoords = value,
                                () -> config.extraSettings.showCoords
                        ).setTooltip(Component.translatable("sodium-extra.option.show_coordinates.tooltip")),
                        new RangeOption(Component.translatable("sodium-extra.option.cloud_height"),
                                -64, 319, 1,
                                (value) -> config.extraSettings.cloudHeight = value,
                                () -> config.extraSettings.cloudHeight
                        ).setTooltip(Component.translatable("sodium-extra.option.cloud_height.tooltip"))
                }),
                new OptionBlock("advanced item tooltips", new Option[]{
                        new SwitchOption(
                                Component.translatable("sodium-extra.option.advanced_item_tooltips"),
                                (value) -> Minecraft.getInstance().options.advancedItemTooltips = value,
                                () -> Minecraft.getInstance().options.advancedItemTooltips
                        ).setTooltip(Component.translatable("sodium-extra.option.advanced_item_tooltips.tooltip"))
                        .setOnApply(v -> Minecraft.getInstance().options.save()),
                }),
                new OptionBlock("toasts", new Option[]{
                        new SwitchOption(
                                Component.translatable("sodium-extra.option.toasts"),
                                (value) -> config.extraSettings.toasts = value,
                                () -> config.extraSettings.toasts
                        ).setTooltip(Component.translatable("sodium-extra.option.toasts.tooltip")),
                        new SwitchOption(
                                Component.translatable("sodium-extra.option.advancement_toast"),
                                (value) -> config.extraSettings.advancementToast = value,
                                () -> config.extraSettings.advancementToast
                        ).setTooltip(Component.translatable("sodium-extra.option.advancement_toast.tooltip")),
                        new SwitchOption(
                                Component.translatable("sodium-extra.option.recipe_toast"),
                                (value) -> config.extraSettings.recipeToast = value,
                                () -> config.extraSettings.recipeToast
                        ).setTooltip(Component.translatable("sodium-extra.option.recipe_toast.tooltip")),
                        new SwitchOption(
                                Component.translatable("sodium-extra.option.system_toast"),
                                (value) -> config.extraSettings.systemToast = value,
                                () -> config.extraSettings.systemToast
                        ).setTooltip(Component.translatable("sodium-extra.option.system_toast.tooltip")),
                        new SwitchOption(
                                Component.translatable("sodium-extra.option.tutorial_toast"),
                                (value) -> config.extraSettings.tutorialToast = value,
                                () -> config.extraSettings.tutorialToast
                        ).setTooltip(Component.translatable("sodium-extra.option.tutorial_toast.tooltip"))
                }),
                new OptionBlock("other", new Option[]{
                        new SwitchOption(
                                Component.translatable("sodium-extra.option.instant_sneak"),
                                (value) -> config.extraSettings.instantSneak = value,
                                () -> config.extraSettings.instantSneak
                        ).setTooltip(Component.translatable("sodium-extra.option.instant_sneak.tooltip")),
                        new SwitchOption(
                                Component.translatable("sodium-extra.option.prevent_shaders"),
                                (value) -> config.extraSettings.preventShaders = value,
                                () -> config.extraSettings.preventShaders
                        ).setTooltip(Component.translatable("sodium-extra.option.prevent_shaders.tooltip"))
                        .setOnApply(v -> minecraft.delayTextureReload())
                }),
                new OptionBlock("debug hud", new Option[]{
                        new SwitchOption(
                                Component.translatable("sodium-extra.option.steady_debug_hud"),
                                (value) -> config.extraSettings.steadyDebugHud = value,
                                () -> config.extraSettings.steadyDebugHud
                        ).setTooltip(Component.translatable("sodium-extra.option.steady_debug_hud.tooltip")),
                        new RangeOption(
                                Component.translatable("sodium-extra.option.steady_debug_hud_refresh_interval"),
                                1, 20, 1,
                                (value) -> config.extraSettings.steadyDebugHudRefreshInterval = value,
                                () -> config.extraSettings.steadyDebugHudRefreshInterval
                        ).setTooltip(Component.translatable("sodium-extra.option.steady_debug_hud_refresh_interval.tooltip"))
                }),
        };
    }
}