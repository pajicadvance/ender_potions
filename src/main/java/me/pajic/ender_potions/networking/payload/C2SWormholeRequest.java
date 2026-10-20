package me.pajic.ender_potions.networking.payload;

import me.pajic.ender_potions.EnderPotions;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record C2SWormholeRequest(UUID target) implements CustomPacketPayload {

    public static final Type<C2SWormholeRequest> TYPE = new Type<>(EnderPotions.id("wormhole_request"));
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SWormholeRequest> CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, C2SWormholeRequest::target,
            C2SWormholeRequest::new
    );

    @Override @NotNull
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
