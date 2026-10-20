package me.pajic.ender_potions.platform.neoforge;

//? neoforge && >=26.1 {

/*import me.pajic.ender_potions.EnderPotions;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import me.pajic.ender_potions.networking.ClientNetworkEvents;
import me.pajic.ender_potions.networking.payload.S2COpenWormholeScreen;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;

@EventBusSubscriber(modid = EnderPotions.MOD_ID, value = Dist.CLIENT)
public class NeoforgeClientEventSubscriber {


    @SubscribeEvent
    private static void initNetworking(RegisterClientPayloadHandlersEvent event) {
        event.register(S2COpenWormholeScreen.TYPE, (p, c) ->
                ClientNetworkEvents.openWormholeScreen()
        );
    }
}
*///?}
