package me.pajic.ender_potions.screen;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.InputConstants;
import me.pajic.ender_potions.networking.payload.C2SWormholeCancel;
import me.pajic.ender_potions.networking.payload.C2SWormholeRequest;
import me.pajic.ender_potions.platform.MultiLoaderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.scores.PlayerTeam;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

//? >=26.1 {
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
//?}

public class WormholeScreen extends Screen {

    private static final int PANEL_W = 240, ROW_H = 12, VISIBLE_ROWS = 8, LIST_H = VISIBLE_ROWS * ROW_H;

    private record Entry(UUID id, String name) {}

    private final Minecraft MC = Minecraft.getInstance();

    private List<Entry> players = List.of();
    private @Nullable UUID selected;
    private boolean confirmed;
    private int scrollRow;
    private int left, top, listY;

    private EditBox searchBox;
    private Button confirmButton;

    public WormholeScreen() {
        super(Component.translatable("screen.ender_potions.wormholePlayerSelection.title"));
    }

    @Override
    protected void init() {
        left = width / 2 - PANEL_W / 2;
        top = height / 2 - (LIST_H + 66) / 2;
        listY = top + 36;

        String previousSearch = searchBox != null ? searchBox.getValue() : "";
        searchBox = new EditBox(
                font, left, top + 14, PANEL_W, 16,
                Component.translatable("gui.ender_potions.wormhole.search")
        );
        searchBox.setHint(Component.translatable("gui.ender_potions.wormhole.search"));
        searchBox.setValue(previousSearch);
        searchBox.setResponder(s -> refresh());
        addRenderableWidget(searchBox);
        setInitialFocus(searchBox);

        int buttonsY = listY + LIST_H + 6;
        confirmButton = addRenderableWidget(Button.builder(
                Component.translatable("gui.ender_potions.wormhole.confirm"), b -> confirm()
        ).pos(left, buttonsY).size(PANEL_W / 2 - 1, 20).build());
        addRenderableWidget(Button.builder(
                Component.translatable("gui.cancel"), b -> onClose()
        ).pos(left + PANEL_W / 2 + 1, buttonsY).size(PANEL_W / 2 - 1, 20).build());
        refresh();
    }

    @Override
    public void tick() {
        super.tick();
        refresh();
    }

    private void refresh() {
        ClientPacketListener connection = MC.getConnection();
        if (connection == null || MC.player == null) {
            players = List.of();
        } else {
            String filter = searchBox.getValue().strip().toLowerCase(Locale.ROOT);
            List<Entry> list = new ArrayList<>();
            PlayerTeam ownTeam = MC.player.getTeam();
            for (PlayerInfo info : connection.getOnlinePlayers()) {
                GameProfile profile = info.getProfile();
                //~ if <26.1 'profile.id()' -> 'profile.getId()'
                UUID id = profile.id();
                //~ if <26.1 'profile.name()' -> 'profile.getName()'
                String name = profile.name();
                if (id.equals(MC.player.getUUID())) continue;
                if (ownTeam != null && info.getTeam() != null && !MC.player.isAlliedTo(info.getTeam())) continue;
                if (!filter.isEmpty() && !name.toLowerCase(Locale.ROOT).contains(filter)) continue;
                list.add(new Entry(id, name));
            }
            list.sort(Comparator.comparing(Entry::name, String.CASE_INSENSITIVE_ORDER));
            players = list;
        }
        if (selected != null && players.stream().noneMatch(e -> e.id().equals(selected))) selected = null;
        scrollRow = Mth.clamp(scrollRow, 0, maxScroll());
        confirmButton.active = selected != null;
    }

    private int maxScroll() {
        return Math.max(0, players.size() - VISIBLE_ROWS);
    }

    private int rowAt(double mx, double my) {
        if (mx < left || mx >= left + PANEL_W || my < listY || my >= listY + LIST_H) return -1;
        int index = scrollRow + (int) ((my - listY) / ROW_H);
        return index < players.size() ? index : -1;
    }

    private void confirm() {
        if (selected == null) return;
        MultiLoaderUtil.INSTANCE.c2s(new C2SWormholeRequest(selected));
        confirmed = true;
        onClose();
    }

    @Override
    //~ if <26.1 'extractBackground' -> 'renderBackground'
    public void extractBackground(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        //~ if <26.1 'extractBackground' -> 'renderBackground'
        super.extractBackground(graphics, mouseX, mouseY, a);
        int bottom = confirmButton.getY() + 28;
        graphics.fill(left - 9, top - 9, left + PANEL_W + 9, bottom + 1, 0xFF000000);
        graphics.fill(left - 8, top - 8, left + PANEL_W + 8, bottom, 0xE0202020);
    }

    @Override
    //~ if <26.1 'extractRenderState' -> 'render'
    public void extractRenderState(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        //~ if <26.1 'extractRenderState' -> 'render'
        super.extractRenderState(graphics, mouseX, mouseY, a);

        graphics.text(font, title, width / 2 - font.width(title) / 2, top, -1);

        graphics.fill(left - 2, listY - 2, left + PANEL_W + 2, listY + LIST_H + 2, 0x80000000);
        if (players.isEmpty()) {
            Component none = Component.translatable("gui.ender_potions.wormhole.no_players");
            graphics.text(font, none, width / 2 - font.width(none) / 2, listY + LIST_H / 2 - 4, 0xFFA0A0A0);
        } else {
            int hovered = rowAt(mouseX, mouseY);
            int last = Math.min(players.size(), scrollRow + VISIBLE_ROWS);
            for (int i = scrollRow; i < last; i++) {
                Entry entry = players.get(i);
                int ry = listY + (i - scrollRow) * ROW_H;
                boolean isSelected = entry.id().equals(selected);
                if (isSelected) graphics.fill(left, ry, left + PANEL_W, ry + ROW_H, 0xFF505050);
                else if (i == hovered) graphics.fill(left, ry, left + PANEL_W, ry + ROW_H, 0x40FFFFFF);
                graphics.text(font, entry.name(), left + 4, ry + 2, isSelected ? 0xFFFFFFFF : 0xFFD0D0D0);
            }
            if (maxScroll() > 0) {
                int barX = left + PANEL_W + 3;
                int thumbH = Math.max(6, LIST_H * VISIBLE_ROWS / players.size());
                int thumbY = listY + (LIST_H - thumbH) * scrollRow / maxScroll();
                graphics.fill(barX, listY, barX + 3, listY + LIST_H, 0xFF202020);
                graphics.fill(barX, thumbY, barX + 3, thumbY + thumbH, 0xFFA0A0A0);
            }
        }
    }

    @Override
    //~ if <26.1 'MouseButtonEvent event, boolean doubleClick' -> 'double mouseX, double mouseY, int button'
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        //~ if <26.1 'event.button()' -> 'button'
        //~ if >26.2 '0' -> '1'
        if (event.button() == 1) {
            //~ if <26.1 'event.x(), event.y()' -> 'mouseX, mouseY'
            int index = rowAt(event.x(), event.y());
            if (index >= 0) {
                selected = players.get(index).id();
                confirmButton.active = true;
                MC.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                return true;
            }
        }
        //~ if <26.1 'event, doubleClick' -> 'mouseX, mouseY, button'
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (maxScroll() > 0 && mouseX >= left && mouseX < left + PANEL_W + 6 && mouseY >= listY && mouseY < listY + LIST_H) {
            scrollRow = Mth.clamp(scrollRow - (int) Math.signum(scrollY), 0, maxScroll());
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    //~ if <26.1 '@NotNull KeyEvent event' -> 'int keyCode, int scanCode, int modifiers'
    public boolean keyPressed(@NotNull KeyEvent event) {
        //~ if <26.1 'event.key()' -> 'keyCode'
        int key = event.key();
        if ((key == InputConstants.KEY_RETURN || key == InputConstants.KEY_NUMPADENTER) && confirmButton.active) {
            confirm();
            return true;
        }
        //~ if <26.1 'event' -> 'keyCode, scanCode, modifiers'
        return super.keyPressed(event);
    }

    @Override
    public void removed() {
        super.removed();
        if (!confirmed && MC.getConnection() != null) MultiLoaderUtil.INSTANCE.c2s(new C2SWormholeCancel());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    //? >=26.1 {
    @Override
    public boolean isInGameUi() {
        return true;
    }
    //?}
}
