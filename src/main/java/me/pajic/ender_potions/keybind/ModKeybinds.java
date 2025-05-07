package me.pajic.ender_potions.keybind;

import com.mojang.blaze3d.platform.InputConstants;
import me.pajic.ender_potions.gui.RequestWidget;
import me.pajic.ender_potions.network.ModNetworking;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = "ender_potions", value = Dist.CLIENT)
public class ModKeybinds {
    public static final Lazy<KeyMapping> ACCEPT_REQUEST = Lazy.of(() ->
            new KeyMapping(
                    "key.ender_potions.accept_request",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_Y,
                    "category.ender_potions.keybindings"
            )
    );

    public static void registerKeybinds(RegisterKeyMappingsEvent event) {
        event.register(ACCEPT_REQUEST.get());
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (RequestWidget.activeRequest != null && ACCEPT_REQUEST.get().consumeClick()) {
            PacketDistributor.sendToServer(new ModNetworking.C2SSendTpResponse(RequestWidget.activeRequest.left(), true));
            RequestWidget.activeRequest = null;
        }
    }
}
