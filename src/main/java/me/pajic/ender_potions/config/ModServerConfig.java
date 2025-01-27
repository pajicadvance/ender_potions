package me.pajic.ender_potions.config;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = "ender_potions", bus = EventBusSubscriber.Bus.MOD)
public class ModServerConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue TELEPORT_RADIUS = BUILDER
            .translation("text.config.ender_potions.option.teleportRadius")
            .defineInRange("teleportRadius", 1000, 1, Integer.MAX_VALUE);
    private static final ModConfigSpec.IntValue TELEPORT_MAX_HEIGHT = BUILDER
            .translation("text.config.ender_potions.option.teleportMaxHeight")
            .defineInRange("teleportMaxHeight", 384, -64, Integer.MAX_VALUE);

    public static final ModConfigSpec SERVER_SPEC = BUILDER.build();

    public static int teleportRadius;
    public static int teleportMaxHeight;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent.Loading event) {
        updateConfig(event);
    }

    @SubscribeEvent
    static void onChange(final ModConfigEvent.Reloading event) {
        updateConfig(event);
    }

    private static void updateConfig(ModConfigEvent event) {
        if (event.getConfig().getSpec() == SERVER_SPEC) {
            teleportRadius = TELEPORT_RADIUS.get();
            teleportMaxHeight = TELEPORT_MAX_HEIGHT.get();
        }
    }
}
