package me.pajic.ender_potions.network;

import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
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
    }
}
