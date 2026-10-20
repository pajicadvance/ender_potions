package me.pajic.ender_potions.networking.payload;

import me.pajic.ender_potions.EnderPotions;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record S2COpenWormholeScreen() implements CustomPacketPayload {

    public static final Type<S2COpenWormholeScreen> TYPE = new Type<>(EnderPotions.id("open_wormhole_screen"));
    public static final StreamCodec<RegistryFriendlyByteBuf, S2COpenWormholeScreen> CODEC = StreamCodec.unit(new S2COpenWormholeScreen());

    @Override @NotNull
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
