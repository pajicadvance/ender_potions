package me.pajic.ender_potions;

import me.pajic.ender_potions.gui.WormholeScreen;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import java.util.HashMap;
import java.util.UUID;

@Mod(value = "ender_potions", dist = Dist.CLIENT)
public class ClientMain {
    public ClientMain(ModContainer modContainer) {
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    public static void openWormholeScreen(HashMap<UUID, String> onlinePlayers) {
        Minecraft.getInstance().setScreen(new WormholeScreen(onlinePlayers));
    }
}
