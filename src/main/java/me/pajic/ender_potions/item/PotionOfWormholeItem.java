package me.pajic.ender_potions.item;

import me.pajic.ender_potions.config.ModCommonConfig;
import me.pajic.ender_potions.network.ModNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class PotionOfWormholeItem extends CustomPotionItem {

    public PotionOfWormholeItem(Properties properties, Component tooltip) {
        super(properties, tooltip);
    }

    @Override
    public void runCustomBehavior(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide()) {
            HashMap<UUID, String> onlinePlayers = new HashMap<>();
            level.players().forEach(p -> {
                if (!p.getUUID().equals(player.getUUID())) {
                    onlinePlayers.put(p.getUUID(), p.getDisplayName().getString());
                }
            });
            if (!onlinePlayers.isEmpty()) {
                PacketDistributor.sendToPlayer(
                        level.getServer().getPlayerList().getPlayer(player.getUUID()),
                        new ModNetworking.S2COpenWormholeScreenPayload(onlinePlayers)
                );
            }
        }
    }

    @Override
    public boolean isEnabled(@NotNull FeatureFlagSet enabledFeatures) {
        return ModCommonConfig.enablePotionOfWormhole;
    }
}