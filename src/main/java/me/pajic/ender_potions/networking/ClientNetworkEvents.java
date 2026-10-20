package me.pajic.ender_potions.networking;

import me.pajic.ender_potions.screen.WormholeScreen;
import net.minecraft.client.Minecraft;

public class ClientNetworkEvents {

    public static void openWormholeScreen() {
        //~ if <26.1 'setScreenAndShow' -> 'setScreen'
        Minecraft.getInstance().setScreenAndShow(new WormholeScreen());
    }
}
