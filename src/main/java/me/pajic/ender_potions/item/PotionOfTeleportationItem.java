package me.pajic.ender_potions.item;

import me.pajic.ender_potions.gamerule.ModGameRules;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.ArrayList;
import java.util.List;

//? >=26.3
import net.minecraft.tags.BlockTags;

public class PotionOfTeleportationItem extends CustomPotionItem {

    private static final int MAX_ATTEMPTS = 16;

    public PotionOfTeleportationItem(Properties properties, Component tooltip) {
        super(properties, tooltip);
    }

    @Override
    public boolean runCustomBehavior(ItemStack stack, Level level, Player player) {
        if (!(level instanceof ServerLevel serverLevel)) return true;

        int radius = ModGameRules.teleportRadius(serverLevel);
        int undergroundChance = ModGameRules.teleportUndergroundChance(serverLevel);
        //~ if <26.1 'getMinY()' -> 'getMinBuildHeight()'
        int minY = serverLevel.getMinY();
        int maxY = minY + serverLevel.getHeight() - 1;
        int topY = Math.min(maxY, ModGameRules.teleportMaxHeight(serverLevel));
        RandomSource random = player.getRandom();

        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            int x = random.nextIntBetweenInclusive(player.getBlockX() - radius, player.getBlockX() + radius);
            int z = random.nextIntBetweenInclusive(player.getBlockZ() - radius, player.getBlockZ() + radius);
            if (!serverLevel.getWorldBorder().isWithinBounds(x + 0.5, z + 0.5)) continue;

            List<BlockPos> surface = new ArrayList<>();
            List<BlockPos> underground = new ArrayList<>();
            findStandingSpots(serverLevel, x, z, minY, maxY, topY, surface, underground);

            boolean preferUnderground = random.nextInt(100) < undergroundChance;
            List<BlockPos> preferred = preferUnderground ? underground : surface;
            List<BlockPos> fallback = preferUnderground ? surface : underground;
            if (tryTeleport(player, preferred, random) || tryTeleport(player, fallback, random)) return true;
        }

        Component message = Component.translatable("gui.ender_potions.teleport.failed");
        //? >=26.1 {
        player.sendOverlayMessage(message);
        //?} else {
        /*player.displayClientMessage(message, true);
        *///?}
        return false;
    }

    private static void findStandingSpots(
            ServerLevel level, int x, int z, int minY, int maxY, int topY,
            List<BlockPos> surface, List<BlockPos> underground
    ) {
        int surfaceY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        boolean hasCeiling = level.dimensionType().hasCeiling();
        boolean blockAbove = false;

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, maxY, z);
        BlockState head = level.getBlockState(pos);
        boolean headOpen = isOpen(level, pos, head);
        BlockState feet = level.getBlockState(pos.setY(maxY - 1));
        boolean feetOpen = isOpen(level, pos, feet);

        for (int y = maxY - 2; y >= minY; y--) {
            BlockState floor = level.getBlockState(pos.setY(y));
            int feetY = y + 1;
            if (feetY <= topY && (!hasCeiling || blockAbove) && feetOpen && headOpen && isFloor(floor)) {
                BlockPos spot = new BlockPos(x, feetY, z);
                if (feetY >= surfaceY) surface.add(spot);
                else underground.add(spot);
            }
            if (!head.isAir()) blockAbove = true;
            head = feet;
            headOpen = feetOpen;
            feet = floor;
            feetOpen = isOpen(level, pos, floor);
        }
    }

    @SuppressWarnings("deprecation")
    private static boolean isFloor(BlockState state) {
        //? >=26.3 {
        return state.is(BlockTags.ENTITIES_CAN_TELEPORT_TO) && !state.is(BlockTags.CONSUMABLE_DOES_NOT_TELEPORT_TO);
        //?} else {
        /*return state.blocksMotion();
        *///?}
    }

    private static boolean isOpen(ServerLevel level, BlockPos pos, BlockState state) {
        return state.getCollisionShape(level, pos).isEmpty() && state.getFluidState().isEmpty();
    }

    private static boolean tryTeleport(Player player, List<BlockPos> spots, RandomSource random) {
        while (!spots.isEmpty()) {
            BlockPos spot = spots.remove(random.nextInt(spots.size()));
            double x = spot.getX() + 0.5;
            double z = spot.getZ() + 0.5;
            //? >=26.3 {
            if (player.randomTeleport(x, spot.getY(), z, false, BlockTags.CONSUMABLE_DOES_NOT_TELEPORT_TO)) return true;
            //?} else {
            /*if (player.randomTeleport(x, spot.getY(), z, false)) return true;
            *///?}
        }
        return false;
    }
}
