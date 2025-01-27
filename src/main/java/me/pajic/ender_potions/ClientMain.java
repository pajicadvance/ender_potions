package me.pajic.ender_potions;

import me.pajic.ender_potions.network.ModClientNetworking;
import net.fabricmc.api.ClientModInitializer;

public class ClientMain implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModClientNetworking.init();
    }
}
