package me.pajic.ender_potions.gui;

import me.pajic.ender_potions.network.ModNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class WormholeScreen extends Screen {

    private final HashMap<UUID, String> onlinePlayers;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    private PlayerListWidget list;

    public WormholeScreen(HashMap<UUID, String> onlinePlayers) {
        super(Component.translatable("screen.ender_potions.wormholePlayerSelection.title"));
        this.onlinePlayers = onlinePlayers;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void init() {
        LinearLayout linearLayout = this.layout.addToHeader(LinearLayout.vertical().spacing(8));
        linearLayout.defaultCellSetting().alignHorizontallyCenter();
        linearLayout.addChild(new StringWidget(this.getTitle(), this.font));
        this.list = this.layout.addToContents(new PlayerListWidget());
        LinearLayout linearLayout2 = this.layout.addToFooter(LinearLayout.horizontal().spacing(8));
        linearLayout2.addChild(Button.builder(CommonComponents.GUI_DONE, button -> {
            ClientPlayNetworking.send(new ModNetworking.C2SSendTpRequest(list.getSelected().uuid));
            minecraft.player.displayClientMessage(
                    Component.translatable(
                            "gui.ender_potions.request_sent",
                            Component.literal(list.getSelected().name).withStyle(ChatFormatting.RED)
                    ),
                    true
            );
            this.onClose();
        }).build());
        this.layout.visitWidgets(this::addRenderableWidget);
        this.repositionElements();
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
        this.list.updateSize(this.width, this.layout);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @SuppressWarnings("ConstantConditions")
    private class PlayerListWidget extends ObjectSelectionList<PlayerEntry> {

        public PlayerListWidget() {
            super(
                    WormholeScreen.this.minecraft,
                    WormholeScreen.this.width,
                    WormholeScreen.this.height - 77,
                    40,
                    16
            );

            onlinePlayers.forEach((uuid, name) -> addEntry(new PlayerEntry(name, uuid)));
            setSelected(children().stream().findFirst().get());
            centerScrollOn(children().stream().findFirst().get());
        }
    }

    private class PlayerEntry extends ObjectSelectionList.Entry<PlayerEntry> {

        private final String name;
        private final UUID uuid;

        public PlayerEntry(String name, UUID uuid) {
            this.name = name;
            this.uuid = uuid;
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.translatable("narrator.select", name);
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            guiGraphics.drawString(
                    WormholeScreen.this.font,
                    name,
                    left + 5,
                    top + 2,
                    16777215
            );
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            list.setSelected(this);
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }
}
