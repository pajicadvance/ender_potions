package me.pajic.ender_potions.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

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

    public static void init() {
        PayloadTypeRegistry.playC2S().register(C2SWormholeTeleportPayload.TYPE, C2SWormholeTeleportPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ModClientNetworking.S2COpenWormholeScreenPayload.TYPE, ModClientNetworking.S2COpenWormholeScreenPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(C2SWormholeTeleportPayload.TYPE, (payload, context) -> context.server().execute(() -> {
            Player playerToTpTo = context.player().level().getPlayerByUUID(payload.playerToTpTo);
            context.player().teleportTo(playerToTpTo.getX(), playerToTpTo.getY(), playerToTpTo.getZ());
        }));
    }

}
