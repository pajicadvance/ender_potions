package me.pajic.ender_potions.platform.fabric;

//? fabric {

import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import me.pajic.ender_potions.EnderPotions;
import me.pajic.ender_potions.gamerule.ModGameRules;
import me.pajic.ender_potions.item.ModItems;
import me.pajic.ender_potions.networking.ServerNetworkEvents;
import me.pajic.ender_potions.networking.payload.C2SWormholeCancel;
import me.pajic.ender_potions.networking.payload.C2SWormholeRequest;
import me.pajic.ender_potions.networking.payload.S2COpenWormholeScreen;
import me.pajic.ender_potions.wormhole.WormholeRequests;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTabs;

@Entrypoint("main")
public class FabricEntrypoint implements ModInitializer {

	@Override
	public void onInitialize() {
        ModGameRules.init();
        //? >=26.1 {
        Registry.register(BuiltInRegistries.GAME_RULE, EnderPotions.id("teleport_radius"), ModGameRules.TELEPORT_RADIUS);
        Registry.register(BuiltInRegistries.GAME_RULE, EnderPotions.id("teleport_max_height"), ModGameRules.TELEPORT_MAX_HEIGHT);
        Registry.register(BuiltInRegistries.GAME_RULE, EnderPotions.id("teleport_underground_chance"), ModGameRules.TELEPORT_UNDERGROUND_CHANCE);
        Registry.register(BuiltInRegistries.GAME_RULE, EnderPotions.id("wormhole_request_timeout"), ModGameRules.WORMHOLE_REQUEST_TIMEOUT);
        //?}
        Registry.register(BuiltInRegistries.ITEM, ModItems.POTION_OF_TELEPORTATION_KEY, ModItems.POTION_OF_TELEPORTATION);
        Registry.register(BuiltInRegistries.ITEM, ModItems.POTION_OF_WORMHOLE_KEY, ModItems.POTION_OF_WORMHOLE);
        PayloadTypeRegistry.clientboundPlay().register(S2COpenWormholeScreen.TYPE, S2COpenWormholeScreen.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(C2SWormholeRequest.TYPE, C2SWormholeRequest.CODEC);
        PayloadTypeRegistry.serverboundPlay().register(C2SWormholeCancel.TYPE, C2SWormholeCancel.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(C2SWormholeRequest.TYPE, (payload, context) ->
                ServerNetworkEvents.requestWormhole(payload, context.player())
        );
        ServerPlayNetworking.registerGlobalReceiver(C2SWormholeCancel.TYPE, (payload, context) ->
                ServerNetworkEvents.cancelWormhole(payload, context.player())
        );
        CommandRegistrationCallback.EVENT.register((dispatcher, r, e) ->
                WormholeRequests.registerCommands(dispatcher)
        );
        ServerTickEvents.END_SERVER_TICK.register(WormholeRequests::tick);
        ServerLifecycleEvents.SERVER_STOPPING.register(WormholeRequests::onServerStopping);
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(output -> {
            output.accept(ModItems.POTION_OF_TELEPORTATION);
            output.accept(ModItems.POTION_OF_WORMHOLE);
        });
	}
}
//?}
