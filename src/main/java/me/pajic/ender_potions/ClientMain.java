package me.pajic.ender_potions;

import me.pajic.ender_potions.gui.RequestWidget;
import me.pajic.ender_potions.gui.WormholeScreen;
import me.pajic.ender_potions.keybind.ModKeybinds;
import me.pajic.ender_potions.network.ModNetworking;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;

import java.util.HashMap;
import java.util.UUID;

public class ClientMain implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModNetworking.initClient();
        ModKeybinds.initKeybinds();
        RequestWidget.initOverlay();
    }

    public static void openWormholeScreen(HashMap<UUID, String> onlinePlayers) {
        Minecraft.getInstance().setScreen(new WormholeScreen(onlinePlayers));
    }
}
