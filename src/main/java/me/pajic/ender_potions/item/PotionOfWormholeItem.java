package me.pajic.ender_potions.item;

import me.pajic.ender_potions.wormhole.WormholeRequests;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PotionOfWormholeItem extends CustomPotionItem {

    public PotionOfWormholeItem(Properties properties, Component tooltip) {
        super(properties, tooltip);
    }

    @Override
    public boolean runCustomBehavior(ItemStack stack, Level level, Player player) {
        return !(player instanceof ServerPlayer serverPlayer) || WormholeRequests.open(serverPlayer);
    }

    @Override
    protected boolean leavesBottle() {
        return false;
    }
}
