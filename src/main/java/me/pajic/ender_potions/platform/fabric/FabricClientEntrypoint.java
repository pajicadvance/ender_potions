package me.pajic.ender_potions.platform.fabric;

//? fabric {

import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import me.pajic.ender_potions.networking.ClientNetworkEvents;
import me.pajic.ender_potions.networking.payload.S2COpenWormholeScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

@Entrypoint("client")
public class FabricClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(S2COpenWormholeScreen.TYPE, (p, c) ->
                ClientNetworkEvents.openWormholeScreen()
        );
	}
}
//?}
