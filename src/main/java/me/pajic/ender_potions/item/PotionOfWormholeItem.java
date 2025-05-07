package me.pajic.ender_potions.item;

import me.pajic.ender_potions.network.ModNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.UUID;

public class PotionOfWormholeItem extends CustomPotionItem {

    public PotionOfWormholeItem(Properties properties, Component tooltip, boolean enabledFlag) {
        super(properties, tooltip, enabledFlag);
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
                ServerPlayNetworking.send(
                        level.getServer().getPlayerList().getPlayer(player.getUUID()),
                        new ModNetworking.S2COpenWormholeScreenPayload(onlinePlayers)
                );
            }
        }
    }
}