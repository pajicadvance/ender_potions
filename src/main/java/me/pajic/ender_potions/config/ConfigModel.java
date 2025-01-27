package me.pajic.ender_potions.config;

import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.RestartRequired;
import io.wispforest.owo.config.annotation.Sync;

@Modmenu(modId = "ender_potions")
@Config(name = "ender_potions", wrapperName = "ModConfig")
@Sync(Option.SyncMode.OVERRIDE_CLIENT)
@SuppressWarnings("unused")
public class ConfigModel {
    @RestartRequired public boolean enablePotionOfWormhole = true;
    @RestartRequired public boolean enablePotionOfTeleportation = true;
    public int teleportRadius = 1000;
    public int teleportMaxHeight = 384;
}
