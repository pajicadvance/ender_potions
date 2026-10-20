package me.pajic.ender_potions.networking.payload;

import me.pajic.ender_potions.EnderPotions;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record C2SWormholeCancel() implements CustomPacketPayload {

    public static final Type<C2SWormholeCancel> TYPE = new Type<>(EnderPotions.id("wormhole_cancel"));
    public static final StreamCodec<RegistryFriendlyByteBuf, C2SWormholeCancel> CODEC = StreamCodec.unit(new C2SWormholeCancel());

    @Override @NotNull
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
