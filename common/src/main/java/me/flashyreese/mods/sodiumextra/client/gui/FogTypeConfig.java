package me.flashyreese.mods.sodiumextra.client.gui;

public class FogTypeConfig {
    public boolean enable;
    public int environmentStartMultiplier;
    public int renderDistanceStartMultiplier;

    public FogTypeConfig() {
        this.enable = true;
        this.environmentStartMultiplier = 100;
        this.renderDistanceStartMultiplier = 100;
    }
}
