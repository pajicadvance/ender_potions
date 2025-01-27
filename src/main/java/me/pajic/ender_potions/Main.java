package me.pajic.ender_potions;

import me.pajic.ender_potions.config.ModConfig;
import me.pajic.ender_potions.item.ModItems;
import me.pajic.ender_potions.network.ModNetworking;
import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {

    public static final ModConfig CONFIG = ModConfig.createAndLoad();

    @Override
    public void onInitialize() {
        ModItems.init();
        ModNetworking.init();
    }
}
