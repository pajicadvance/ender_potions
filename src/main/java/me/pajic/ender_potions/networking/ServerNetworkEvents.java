package me.pajic.ender_potions.networking;

import me.pajic.ender_potions.networking.payload.C2SWormholeCancel;
import me.pajic.ender_potions.networking.payload.C2SWormholeRequest;
import me.pajic.ender_potions.wormhole.WormholeRequests;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class ServerNetworkEvents {

    public static void requestWormhole(C2SWormholeRequest payload, Player player) {
        if (player instanceof ServerPlayer serverPlayer) WormholeRequests.request(serverPlayer, payload.target());
    }

    public static void cancelWormhole(C2SWormholeCancel payload, Player player) {
        if (player instanceof ServerPlayer serverPlayer) WormholeRequests.cancel(serverPlayer);
    }
}
