package me.pajic.ender_potions.item;

import me.pajic.ender_potions.config.ModCommonConfig;
import me.pajic.ender_potions.config.ModServerConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PotionOfTeleportationItem extends CustomPotionItem {

    public PotionOfTeleportationItem(Properties properties, Component tooltip) {
        super(properties, tooltip);
    }

    @Override
    public void runCustomBehavior(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide) {
            int radius = ModServerConfig.teleportRadius;
            int maxHeight = ModServerConfig.teleportMaxHeight;
            int levelHeight = player.level().getHeight();
            //? if <= 1.21.1
            /*int levelMinY = player.level().getMinBuildHeight();*/
            //? if > 1.21.1
            int levelMinY = player.level().getMinY();
            if (levelHeight > maxHeight) levelHeight = maxHeight;
            RandomSource random = player.getRandom();
            int x, y, z;

            do {
                x = random.nextIntBetweenInclusive((int) (player.getX() - radius), (int) (player.getX() + radius));
                y = random.nextIntBetweenInclusive(levelMinY, levelHeight);
                z = random.nextIntBetweenInclusive((int) (player.getZ() - radius), (int) (player.getZ() + radius));
            } while (!player.randomTeleport(x, y, z, false));
        }
    }

    @Override
    public boolean isEnabled(@NotNull FeatureFlagSet enabledFeatures) {
        return ModCommonConfig.enablePotionOfTeleportation;
    }
}