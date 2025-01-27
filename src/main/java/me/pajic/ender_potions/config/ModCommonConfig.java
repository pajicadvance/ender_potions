package me.pajic.ender_potions.config;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = "ender_potions", bus = EventBusSubscriber.Bus.MOD)
public class ModCommonConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue ENABLE_POTION_OF_WORMHOLE = BUILDER
            .translation("text.config.ender_potions.option.enablePotionOfWormhole")
            .gameRestart()
            .define("enablePotionOfWormhole", true);
    private static final ModConfigSpec.BooleanValue ENABLE_POTION_OF_TELEPORTATION = BUILDER
            .translation("text.config.ender_potions.option.enablePotionOfTeleportation")
            .gameRestart()
            .define("enablePotionOfTeleportation", true);

    public static final ModConfigSpec COMMON_SPEC = BUILDER.build();

    public static boolean enablePotionOfWormhole;
    public static boolean enablePotionOfTeleportation;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent.Loading event) {
        updateConfig(event);
    }

    @SubscribeEvent
    static void onChange(final ModConfigEvent.Reloading event) {
        updateConfig(event);
    }

    private static void updateConfig(ModConfigEvent event) {
        if (event.getConfig().getSpec() == COMMON_SPEC) {
            enablePotionOfWormhole = ENABLE_POTION_OF_WORMHOLE.get();
            enablePotionOfTeleportation = ENABLE_POTION_OF_TELEPORTATION.get();
        }
    }
}
