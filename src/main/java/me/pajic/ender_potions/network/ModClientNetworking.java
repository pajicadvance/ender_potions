package me.pajic.ender_potions.network;

import me.pajic.ender_potions.gui.WormholeScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class ModClientNetworking {

    public record S2COpenWormholeScreenPayload(HashMap<UUID, String> onlinePlayers) implements CustomPacketPayload {
        public static final Type<S2COpenWormholeScreenPayload> TYPE = new Type<>(ModNetworking.OPEN_WORMHOLE_SCREEN);
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

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(S2COpenWormholeScreenPayload.TYPE, (payload, context) ->
                Minecraft.getInstance().setScreen(new WormholeScreen(payload.onlinePlayers))
        );
    }
}
