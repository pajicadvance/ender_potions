package me.pajic.ender_potions;

import me.pajic.ender_potions.gui.RequestWidget;
import me.pajic.ender_potions.gui.WormholeScreen;
import me.pajic.ender_potions.keybind.ModKeybinds;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

import java.util.HashMap;
import java.util.UUID;

@Mod(value = "ender_potions", dist = Dist.CLIENT)
public class ClientMain {
    public ClientMain(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        modEventBus.addListener(ModKeybinds::registerKeybinds);
        NeoForge.EVENT_BUS.addListener(RequestWidget::renderRequestWidget);
    }

    public static void openWormholeScreen(HashMap<UUID, String> onlinePlayers) {
        Minecraft.getInstance().setScreen(new WormholeScreen(onlinePlayers));
    }
}
