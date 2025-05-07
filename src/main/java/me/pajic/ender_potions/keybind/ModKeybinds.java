package me.pajic.ender_potions.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import me.pajic.ender_potions.gui.RequestWidget;
import me.pajic.ender_potions.network.ModNetworking;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class ModKeybinds {
    public static final KeyMapping ACCEPT_REQUEST = KeyBindingHelper.registerKeyBinding(
            new KeyMapping(
                    "key.ender_potions.accept_request",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_Y,
                    "category.ender_potions.keybindings"
            )
    );

    public static void initKeybinds() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (RequestWidget.activeRequest != null && ACCEPT_REQUEST.consumeClick()) {
                ClientPlayNetworking.send(new ModNetworking.C2SSendTpResponse(RequestWidget.activeRequest.left(), true));
                RequestWidget.activeRequest = null;
            }
        });
    }
}
