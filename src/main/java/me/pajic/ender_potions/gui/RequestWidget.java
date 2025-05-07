package me.pajic.ender_potions.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import me.pajic.ender_potions.keybind.ModKeybinds;
import me.pajic.ender_potions.network.ModNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class RequestWidget {
    public static ObjectArrayFIFOQueue<ObjectObjectImmutablePair<UUID, String>> requests = new ObjectArrayFIFOQueue<>();
    public static ObjectObjectImmutablePair<UUID, String> activeRequest = null;
    private static float timer = 0;

    public static void initOverlay() {
        HudRenderCallback.EVENT.register(RequestWidgetOverlay.INSTANCE::render);
    }

    public static class RequestWidgetOverlay implements LayeredDraw.Layer {
        protected static final RequestWidgetOverlay INSTANCE = new RequestWidgetOverlay();
        private static final Minecraft MC = Minecraft.getInstance();

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
            if (activeRequest == null) {
                if (!requests.isEmpty()) {
                    activeRequest = requests.dequeue();
                    timer = 200;
                }
            } else if (timer > 0) {
                Component top = Component.translatable(
                        "gui.ender_potions.request_prompt_top",
                        Component.literal(activeRequest.right()).withStyle(ChatFormatting.RED)
                );
                Component center = Component.translatable(
                        "gui.ender_potions.request_prompt_center",
                        Component.keybind(ModKeybinds.ACCEPT_REQUEST.getName()).withStyle(ChatFormatting.GREEN)
                );
                Component bottom = Component.translatable(
                        "gui.ender_potions.request_prompt_bottom",
                        Math.round(timer / 20)
                );
                guiGraphics.flush();
                RenderSystem.enableBlend();
                renderText(MC, top, guiGraphics, 24);
                renderText(MC, center, guiGraphics, 36);
                renderText(MC, bottom, guiGraphics, 48);
                guiGraphics.flush();
                RenderSystem.disableBlend();
                timer -= deltaTracker.getGameTimeDeltaTicks();
            } else {
                ClientPlayNetworking.send(new ModNetworking.C2SSendTpResponse(activeRequest.left(), false));
                activeRequest = null;
            }
        }
    }

    public static void renderText(Minecraft mc, Component text, GuiGraphics guiGraphics, int offset) {
        guiGraphics.drawString(
                mc.font, text,
                mc.getWindow().getGuiScaledWidth() / 2 - mc.font.width(text) / 2,
                mc.getWindow().getGuiScaledHeight() / 2 + offset,
                16777215
        );
    }
}
