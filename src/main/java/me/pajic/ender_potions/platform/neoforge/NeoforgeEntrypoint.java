package me.pajic.ender_potions.platform.neoforge;

//? neoforge {

/*import me.pajic.ender_potions.EnderPotions;
import me.pajic.ender_potions.gamerule.ModGameRules;
import me.pajic.ender_potions.item.ModItems;
import me.pajic.ender_potions.networking.ServerNetworkEvents;
import me.pajic.ender_potions.networking.payload.C2SWormholeCancel;
import me.pajic.ender_potions.networking.payload.C2SWormholeRequest;
import me.pajic.ender_potions.networking.payload.S2COpenWormholeScreen;
import me.pajic.ender_potions.wormhole.WormholeRequests;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.RegisterEvent;

//? <26.1 {
/^import me.pajic.ender_potions.networking.ClientNetworkEvents;
^///?}

//? <26.3 {
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
//?}

@Mod(EnderPotions.MOD_ID)
@EventBusSubscriber(modid = EnderPotions.MOD_ID)
public class NeoforgeEntrypoint {

    public NeoforgeEntrypoint() {
        ModGameRules.init();
        NeoForge.EVENT_BUS.addListener((RegisterCommandsEvent e) -> WormholeRequests.registerCommands(e.getDispatcher()));
        NeoForge.EVENT_BUS.addListener((ServerTickEvent.Post e) -> WormholeRequests.tick(e.getServer()));
        NeoForge.EVENT_BUS.addListener((ServerStoppingEvent e) -> WormholeRequests.onServerStopping(e.getServer()));
    }

    @SubscribeEvent
    private static void register(RegisterEvent event) {
        //? >=26.1 {
        event.register(Registries.GAME_RULE, registry -> {
            registry.register(EnderPotions.id("teleport_radius"), ModGameRules.TELEPORT_RADIUS);
            registry.register(EnderPotions.id("teleport_max_height"), ModGameRules.TELEPORT_MAX_HEIGHT);
            registry.register(EnderPotions.id("teleport_underground_chance"), ModGameRules.TELEPORT_UNDERGROUND_CHANCE);
            registry.register(EnderPotions.id("wormhole_request_timeout"), ModGameRules.WORMHOLE_REQUEST_TIMEOUT);
        });
        //?}
        event.register(Registries.ITEM, registry -> {
            registry.register(ModItems.POTION_OF_TELEPORTATION_KEY, ModItems.POTION_OF_TELEPORTATION);
            registry.register(ModItems.POTION_OF_WORMHOLE_KEY, ModItems.POTION_OF_WORMHOLE);
        });
    }

    @SubscribeEvent
    private static void initNetworking(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(S2COpenWormholeScreen.TYPE, S2COpenWormholeScreen.CODEC
                /^? <26.1 {^//^, (p, c) -> ClientNetworkEvents.openWormholeScreen()^//^?}^/
        );
        registrar.playToServer(C2SWormholeRequest.TYPE, C2SWormholeRequest.CODEC, (payload, context) ->
                ServerNetworkEvents.requestWormhole(payload, context.player())
        );
        registrar.playToServer(C2SWormholeCancel.TYPE, C2SWormholeCancel.CODEC, (payload, context) ->
                ServerNetworkEvents.cancelWormhole(payload, context.player())
        );
    }

    @SubscribeEvent
    private static void initCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(ModItems.POTION_OF_TELEPORTATION);
            event.accept(ModItems.POTION_OF_WORMHOLE);
        }
    }

    //? <26.3 {
    @SubscribeEvent
    private static void initBrewing(RegisterBrewingRecipesEvent event) {
        Ingredient awkwardPotion = DataComponentIngredient.of(
                false, DataComponents.POTION_CONTENTS, new PotionContents(Potions.AWKWARD), Items.POTION
        );
        event.getBuilder().addRecipe(
                awkwardPotion,
                Ingredient.of(Items.ENDER_EYE),
                new ItemStack(ModItems.POTION_OF_WORMHOLE)
        );
        event.getBuilder().addRecipe(
                awkwardPotion,
                Ingredient.of(Items.ENDER_PEARL),
                new ItemStack(ModItems.POTION_OF_TELEPORTATION)
        );
    }
    //?}
}
*///?}
