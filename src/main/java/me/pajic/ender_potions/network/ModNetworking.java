package me.pajic.ender_potions.network;

import me.pajic.ender_potions.ClientMain;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class ModNetworking {

    public static final ResourceLocation WORMHOLE_TELEPORT = ResourceLocation.fromNamespaceAndPath("ender_potions", "wormhole_teleport");
    public static final ResourceLocation OPEN_WORMHOLE_SCREEN = ResourceLocation.fromNamespaceAndPath("ender_potions", "open_wormhole_screen");

    public record C2SWormholeTeleportPayload(UUID playerToTpTo) implements CustomPacketPayload {
        public static final Type<C2SWormholeTeleportPayload> TYPE = new Type<>(WORMHOLE_TELEPORT);
        public static final StreamCodec<RegistryFriendlyByteBuf, C2SWormholeTeleportPayload> CODEC = StreamCodec.composite(
                UUIDUtil.STREAM_CODEC, C2SWormholeTeleportPayload::playerToTpTo,
                C2SWormholeTeleportPayload::new
        );

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record S2COpenWormholeScreenPayload(HashMap<UUID, String> onlinePlayers) implements CustomPacketPayload {
        public static final Type<S2COpenWormholeScreenPayload> TYPE = new Type<>(OPEN_WORMHOLE_SCREEN);
        public static final StreamCodec<RegistryFriendlyByteBuf, S2COpenWormholeScreenPayload> CODEC = CustomPacketPayload.codec(
                S2COpenWormholeScreenPayload::write, S2COpenWormholeScreenPayload::new
        );

        private S2COpenWormholeScreenPayload(RegistryFriendlyByteBuf buf) {
            this((HashMap<UUID, String>) buf.readMap(HashMap::new, RegistryFriendlyByteBuf::readUUID, FriendlyByteBuf::readUtf));
        }

        private void write(RegistryFriendlyByteBuf buf) {
            buf.writeMap(this.onlinePlayers, (buffer, value) -> buffer.writeUUID(value), (buffer, value) -> buffer.writeUtf(value));
        }

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    @SubscribeEvent
    public static void init(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
                C2SWormholeTeleportPayload.TYPE,
                C2SWormholeTeleportPayload.CODEC,
                (payload, context) -> {
                    Player playerToTpTo = context.player().level().getPlayerByUUID(payload.playerToTpTo);
                    context.player().teleportTo(playerToTpTo.getX(), playerToTpTo.getY(), playerToTpTo.getZ());
                }
        );
        registrar.playToClient(
                ModNetworking.S2COpenWormholeScreenPayload.TYPE,
                ModNetworking.S2COpenWormholeScreenPayload.CODEC,
                (payload, context) ->
                        ClientMain.openWormholeScreen(payload.onlinePlayers)
        );
    }
}
