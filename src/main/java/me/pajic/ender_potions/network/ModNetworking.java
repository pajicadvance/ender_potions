package me.pajic.ender_potions.network;

import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import me.pajic.ender_potions.ClientMain;
import me.pajic.ender_potions.gui.RequestWidget;
import me.pajic.ender_potions.item.ModItems;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class ModNetworking {

    public static final ResourceLocation OPEN_WORMHOLE_SCREEN = ResourceLocation.fromNamespaceAndPath("ender_potions", "open_wormhole_screen");
    public static final ResourceLocation SEND_TP_REQUEST = ResourceLocation.fromNamespaceAndPath("ender_potions", "send_tp_request");
    public static final ResourceLocation OPEN_REQUEST_WIDGET = ResourceLocation.fromNamespaceAndPath("ender_potions", "open_request_widget");
    public static final ResourceLocation SEND_TP_RESPONSE = ResourceLocation.fromNamespaceAndPath("ender_potions", "send_tp_response");

    public record S2COpenWormholeScreenPayload(HashMap<UUID, String> onlinePlayers) implements CustomPacketPayload {
        public static final Type<S2COpenWormholeScreenPayload> TYPE = new Type<>(OPEN_WORMHOLE_SCREEN);
        public static final StreamCodec<RegistryFriendlyByteBuf, S2COpenWormholeScreenPayload> CODEC = CustomPacketPayload.codec(
                S2COpenWormholeScreenPayload::write, S2COpenWormholeScreenPayload::new
        );

        private S2COpenWormholeScreenPayload(RegistryFriendlyByteBuf buf) {
            this((HashMap<UUID, String>) buf.readMap(HashMap::new, RegistryFriendlyByteBuf::readUUID, FriendlyByteBuf::readUtf));
        }

        private void write(RegistryFriendlyByteBuf buf) {
            buf.writeMap(this.onlinePlayers, RegistryFriendlyByteBuf::writeUUID, FriendlyByteBuf::writeUtf);
        }

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record C2SSendTpRequest(UUID targetPlayer) implements CustomPacketPayload {
        public static final Type<C2SSendTpRequest> TYPE = new Type<>(SEND_TP_REQUEST);
        public static final StreamCodec<RegistryFriendlyByteBuf, C2SSendTpRequest> CODEC = StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, C2SSendTpRequest::targetPlayer,
                C2SSendTpRequest::new
        );

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record S2COpenRequestWidget(UUID requestingPlayer, String playerName) implements CustomPacketPayload {
        public static final Type<S2COpenRequestWidget> TYPE = new Type<>(OPEN_REQUEST_WIDGET);
        public static final StreamCodec<RegistryFriendlyByteBuf, S2COpenRequestWidget> CODEC = StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, S2COpenRequestWidget::requestingPlayer,
                ByteBufCodecs.STRING_UTF8, S2COpenRequestWidget::playerName,
                S2COpenRequestWidget::new
        );

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record C2SSendTpResponse(UUID requestingPlayer, boolean shouldTp) implements CustomPacketPayload {
        public static final Type<C2SSendTpResponse> TYPE = new Type<>(SEND_TP_RESPONSE);
        public static final StreamCodec<RegistryFriendlyByteBuf, C2SSendTpResponse> CODEC = StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, C2SSendTpResponse::requestingPlayer,
                ByteBufCodecs.BOOL, C2SSendTpResponse::shouldTp,
                C2SSendTpResponse::new
        );

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    private static void refundPotion(Player requestingPlayer) {
        Inventory inventory = requestingPlayer.getInventory();
        int slot = inventory.findSlotMatchingItem(new ItemStack(Items.GLASS_BOTTLE));
        if (slot != -1) {
            inventory.getItem(slot).consume(1, requestingPlayer);
            if (!requestingPlayer.hasInfiniteMaterials()) {
                inventory.add(new ItemStack(ModItems.POTION_OF_WORMHOLE));
            }
        }
    }

    public static void sendMessage(Player player, Component playerName, String key) {
        player.displayClientMessage(Component.translatable("gui.ender_potions." + key, playerName.copy().withStyle(ChatFormatting.RED)), true);
    }

    public static void init() {
        PayloadTypeRegistry.playS2C().register(S2COpenWormholeScreenPayload.TYPE, S2COpenWormholeScreenPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(C2SSendTpRequest.TYPE, C2SSendTpRequest.CODEC);
        PayloadTypeRegistry.playS2C().register(S2COpenRequestWidget.TYPE, S2COpenRequestWidget.CODEC);
        PayloadTypeRegistry.playC2S().register(C2SSendTpResponse.TYPE, C2SSendTpResponse.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(C2SSendTpRequest.TYPE, (payload, context) ->
                context.server().execute(() -> {
                    Player targetPlayer = context.player().level().getPlayerByUUID(payload.targetPlayer);
                    ServerPlayNetworking.send(
                            (ServerPlayer) targetPlayer,
                            new S2COpenRequestWidget(
                                    context.player().getUUID(),
                                    context.player().getDisplayName().getString()
                            )
                    );
                })
        );

        ServerPlayNetworking.registerGlobalReceiver(C2SSendTpResponse.TYPE, (payload, context) ->
                context.server().execute(() -> {
                    Player requestingPlayer = context.player().level().getPlayerByUUID(payload.requestingPlayer);
                    Player targetPlayer = context.player();
                    Component playerName = context.player().getDisplayName();
                    if (payload.shouldTp) {
                        if (targetPlayer != null) {
                            if (requestingPlayer != null) {
                                sendMessage(requestingPlayer, playerName, "tp_accepted");
                                sendMessage(targetPlayer, playerName, "tp_incoming");
                                requestingPlayer.teleportTo(targetPlayer.getX(), targetPlayer.getY(), targetPlayer.getZ());
                            } else {
                                sendMessage(targetPlayer, playerName, "tp_requester_died");
                            }
                        } else if (requestingPlayer != null) {
                            sendMessage(requestingPlayer, playerName, "tp_target_died");
                            refundPotion(requestingPlayer);
                        }
                    } else if (requestingPlayer != null) {
                        sendMessage(requestingPlayer, playerName, "tp_denied");
                        refundPotion(requestingPlayer);
                    }
                })
        );
    }

    public static void initClient() {
        ClientPlayNetworking.registerGlobalReceiver(S2COpenWormholeScreenPayload.TYPE, (payload, context) ->
                ClientMain.openWormholeScreen(payload.onlinePlayers)
        );

        ClientPlayNetworking.registerGlobalReceiver(S2COpenRequestWidget.TYPE, (payload, context) ->
                RequestWidget.requests.enqueue(new ObjectObjectImmutablePair<>(payload.requestingPlayer, payload.playerName))
        );
    }
}
