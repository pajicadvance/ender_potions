package me.pajic.ender_potions.wormhole;

import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import me.pajic.ender_potions.EnderPotions;
import me.pajic.ender_potions.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.LevelResource;
import org.jetbrains.annotations.Nullable;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

//? >26.2
import net.minecraft.util.Prediction;

public final class WormholeRefunds {

    private static final Codec<Integer> CODEC = Codec.INT;

    public static boolean refund(MinecraftServer server, UUID playerId) {
        ServerPlayer player = server.getPlayerList().getPlayer(playerId);
        if (player != null && player.isAlive()) {
            givePotion(player);
            return true;
        }
        Path path = file(server, playerId);
        Integer refunds = read(path);
        if (refunds == null) {
            backupBroken(path);
            refunds = 0;
        }
        write(path, refunds + 1);
        return false;
    }

    public static void deliver(ServerPlayer player) {
        Path path = file(player.level().getServer(), player.getUUID());
        if (!Files.exists(path)) return;
        Integer refunds = read(path);
        if (refunds == null) return;
        try {
            Files.deleteIfExists(path);
        } catch (Exception e) {
            EnderPotions.LOGGER.warn("Couldn't delete {}", path, e);
            return;
        }
        if (refunds <= 0) return;
        for (int i = 0; i < refunds; i++) givePotion(player);
        player.sendSystemMessage(Component.translatable(
                "gui.ender_potions.wormhole.refunds_delivered", refunds
        ).withStyle(ChatFormatting.GRAY));
    }

    private static void givePotion(ServerPlayer player) {
        give(player, new ItemStack(ModItems.POTION_OF_WORMHOLE));
    }

    public static void giveBottle(ServerPlayer player) {
        give(player, new ItemStack(Items.GLASS_BOTTLE));
    }

    private static void give(ServerPlayer player, ItemStack stack) {
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false
                    //? >26.2
                    , Prediction.SERVER_ONLY
            );
        }
    }

    private static Path file(MinecraftServer server, UUID player) {
        return server.getWorldPath(LevelResource.ROOT)
                .resolve("data").resolve(EnderPotions.MOD_ID).resolve("wormhole_refunds")
                .resolve(player + ".json");
    }

    private static @Nullable Integer read(Path path) {
        if (!Files.exists(path)) return 0;
        try (Reader reader = Files.newBufferedReader(path)) {
            return CODEC.parse(JsonOps.INSTANCE, JsonParser.parseReader(reader))
                    .resultOrPartial(EnderPotions.LOGGER::warn).orElse(null);
        } catch (Exception e) {
            EnderPotions.LOGGER.warn("Couldn't read wormhole refunds from {}", path, e);
            return null;
        }
    }

    private static void write(Path path, int refunds) {
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path)) {
                writer.write(CODEC.encodeStart(JsonOps.INSTANCE, refunds).getOrThrow().toString());
            }
        } catch (Exception e) {
            EnderPotions.LOGGER.warn("Couldn't save wormhole refunds to {}", path, e);
        }
    }

    private static void backupBroken(Path path) {
        try {
            Files.move(path, path.resolveSibling(path.getFileName() + ".broken"), StandardCopyOption.REPLACE_EXISTING);
            EnderPotions.LOGGER.warn("Moved unreadable wormhole refund file to {}.broken", path);
        } catch (Exception e) {
            EnderPotions.LOGGER.warn("Couldn't back up unreadable wormhole refund file {}", path, e);
        }
    }
}
