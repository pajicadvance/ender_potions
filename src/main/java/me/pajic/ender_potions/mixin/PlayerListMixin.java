package me.pajic.ender_potions.mixin;

import me.pajic.ender_potions.wormhole.WormholeRefunds;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerList.class)
public class PlayerListMixin {

    @Inject(
            method = "placeNewPlayer",
            at = @At("RETURN")
    )
    private void deliverWormholeRefundsOnJoin(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo ci) {
        WormholeRefunds.deliver(player);
    }

    @Inject(
            method = "respawn",
            at = @At("RETURN")
    )
    private void deliverWormholeRefundsOnRespawn(CallbackInfoReturnable<ServerPlayer> cir) {
        WormholeRefunds.deliver(cir.getReturnValue());
    }
}
