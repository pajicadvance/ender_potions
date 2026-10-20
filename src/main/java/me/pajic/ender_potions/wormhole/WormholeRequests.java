package me.pajic.ender_potions.wormhole;

import com.mojang.brigadier.CommandDispatcher;
import me.pajic.ender_potions.EnderPotions;
import me.pajic.ender_potions.gamerule.ModGameRules;
import me.pajic.ender_potions.networking.payload.S2COpenWormholeScreen;
import me.pajic.ender_potions.platform.MultiLoaderUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import java.util.*;

public class WormholeRequests {

    private static final int SELECTION_TIMEOUT_SECONDS = 60;

    private record PendingSelection(boolean refundable, long expiresAt) {}

    private record PendingRequest(
            UUID id, UUID requester, Component requesterName, UUID target,
            Component targetName, boolean refundable, long expiresAt
    ) {}

    private static final Map<UUID, PendingSelection> SELECTING = new HashMap<>();
    private static final Map<UUID, PendingRequest> PENDING = new LinkedHashMap<>();

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(EnderPotions.MOD_ID)
                .then(Commands.literal("acceptwormhole")
                        .then(Commands.argument("id", UuidArgument.uuid())
                                .executes(ctx -> accept(
                                        ctx.getSource().getPlayerOrException(),
                                        UuidArgument.getUuid(ctx, "id")
                                ))))
                .then(Commands.literal("declinewormhole")
                        .then(Commands.argument("id", UuidArgument.uuid())
                                .executes(ctx -> decline(
                                        ctx.getSource().getPlayerOrException(),
                                        UuidArgument.getUuid(ctx, "id")
                                )))));
    }

    public static boolean open(ServerPlayer player) {
        UUID id = player.getUUID();
        if (SELECTING.containsKey(id) || PENDING.values().stream().anyMatch(r -> r.requester().equals(id))) {
            player.sendSystemMessage(error("gui.ender_potions.wormhole.busy"));
            return false;
        }
        MinecraftServer server = player.level().getServer();
        if (server.getPlayerList().getPlayers().stream().noneMatch(p -> canTarget(player, p))) {
            player.sendSystemMessage(error("gui.ender_potions.wormhole.no_players"));
            return false;
        }
        SELECTING.put(id, new PendingSelection(
                !player.hasInfiniteMaterials(),
                System.currentTimeMillis() + SELECTION_TIMEOUT_SECONDS * 1000L
        ));
        MultiLoaderUtil.INSTANCE.s2c(player, new S2COpenWormholeScreen());
        return true;
    }

    public static void request(ServerPlayer requester, UUID targetId) {
        PendingSelection selection = SELECTING.remove(requester.getUUID());
        if (selection == null) return;
        MinecraftServer server = requester.level().getServer();

        ServerPlayer target = server.getPlayerList().getPlayer(targetId);
        if (target == null || !canTarget(requester, target)) {
            requester.sendSystemMessage(error("gui.ender_potions.wormhole.offline"));
            refund(server, requester.getUUID(), selection.refundable());
            return;
        }

        int timeout = ModGameRules.wormholeRequestTimeout(server.overworld());
        PendingRequest request = new PendingRequest(
                UUID.randomUUID(), requester.getUUID(), requester.getDisplayName(),
                targetId, target.getDisplayName(), selection.refundable(),
                System.currentTimeMillis() + timeout * 1000L
        );
        PENDING.put(request.id(), request);

        target.sendSystemMessage(Component.translatable(
                "gui.ender_potions.wormhole.request", request.requesterName(),
                acceptLink(request.id(), timeout), declineLink(request.id())
        ));
        requester.sendSystemMessage(Component.translatable(
                "gui.ender_potions.request_sent", request.targetName()
        ));
    }

    public static void cancel(ServerPlayer player) {
        PendingSelection selection = SELECTING.remove(player.getUUID());
        if (selection != null) refund(player.level().getServer(), player.getUUID(), selection.refundable());
    }

    private static int accept(ServerPlayer target, UUID requestId) {
        PendingRequest request = take(target, requestId);
        if (request == null) return 0;
        MinecraftServer server = target.level().getServer();

        ServerPlayer requester = server.getPlayerList().getPlayer(request.requester());
        if (requester == null || !requester.isAlive()) {
            target.sendSystemMessage(error(requester == null
                    ? "gui.ender_potions.wormhole.requester_offline"
                    : "gui.ender_potions.tp_requester_died"
            ));
            refund(server, request.requester(), request.refundable());
            return 0;
        }

        requester.level().playSound(null, requester.getX(), requester.getY(), requester.getZ(),
                SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
        teleport(requester, target);
        requester.resetFallDistance();
        if (request.refundable()) WormholeRefunds.giveBottle(requester);
        target.level().playSound(null, target.getX(), target.getY(), target.getZ(),
                SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);

        requester.sendSystemMessage(Component.translatable("gui.ender_potions.tp_accepted", request.targetName()));
        target.sendSystemMessage(Component.translatable("gui.ender_potions.tp_incoming", request.requesterName()));
        return 1;
    }

    private static int decline(ServerPlayer target, UUID requestId) {
        PendingRequest request = take(target, requestId);
        if (request == null) return 0;
        MinecraftServer server = target.level().getServer();

        target.sendSystemMessage(Component.translatable(
                "gui.ender_potions.wormhole.declined", request.requesterName()
        ).withStyle(ChatFormatting.GRAY));
        ServerPlayer requester = server.getPlayerList().getPlayer(request.requester());
        if (requester != null) requester.sendSystemMessage(Component.translatable(
                "gui.ender_potions.tp_denied", request.targetName()
        ).withStyle(ChatFormatting.GRAY));
        refund(server, request.requester(), request.refundable());
        return 1;
    }

    public static void tick(MinecraftServer server) {
        if (SELECTING.isEmpty() && PENDING.isEmpty()) return;
        long now = System.currentTimeMillis();

        Iterator<Map.Entry<UUID, PendingSelection>> selections = SELECTING.entrySet().iterator();
        while (selections.hasNext()) {
            Map.Entry<UUID, PendingSelection> entry = selections.next();
            if (now < entry.getValue().expiresAt()) continue;
            selections.remove();
            refund(server, entry.getKey(), entry.getValue().refundable());
        }

        Iterator<PendingRequest> requests = PENDING.values().iterator();
        while (requests.hasNext()) {
            PendingRequest request = requests.next();
            if (now < request.expiresAt()) continue;
            requests.remove();
            ServerPlayer target = server.getPlayerList().getPlayer(request.target());
            if (target != null) target.sendSystemMessage(Component.translatable(
                    "gui.ender_potions.wormhole.expired", request.requesterName()
            ).withStyle(ChatFormatting.GRAY));
            ServerPlayer requester = server.getPlayerList().getPlayer(request.requester());
            if (requester != null) requester.sendSystemMessage(Component.translatable(
                    "gui.ender_potions.wormhole.expired_requester", request.targetName()
            ).withStyle(ChatFormatting.GRAY));
            refund(server, request.requester(), request.refundable());
        }
    }

    public static void onServerStopping(MinecraftServer server) {
        SELECTING.forEach((id, selection) -> refund(server, id, selection.refundable()));
        PENDING.values().forEach(request -> refund(server, request.requester(), request.refundable()));
        SELECTING.clear();
        PENDING.clear();
    }

    private static PendingRequest take(ServerPlayer target, UUID requestId) {
        PendingRequest request = PENDING.get(requestId);
        if (request == null || !request.target().equals(target.getUUID())
                || System.currentTimeMillis() >= request.expiresAt()) {
            target.sendSystemMessage(error("gui.ender_potions.wormhole.invalid"));
            return null;
        }
        PENDING.remove(requestId);
        return request;
    }

    private static boolean canTarget(ServerPlayer requester, ServerPlayer target) {
        if (target.getUUID().equals(requester.getUUID())) return false;
        return requester.getTeam() == null || target.isAlliedTo(requester);
    }

    private static void refund(MinecraftServer server, UUID playerId, boolean refundable) {
        if (!refundable) return;
        boolean delivered = WormholeRefunds.refund(server, playerId);
        ServerPlayer player = server.getPlayerList().getPlayer(playerId);
        if (player != null) player.sendSystemMessage(Component.translatable(delivered
                ? "gui.ender_potions.wormhole.refunded"
                : "gui.ender_potions.wormhole.refund_pending"
        ).withStyle(ChatFormatting.GRAY));
    }

    private static void teleport(ServerPlayer player, ServerPlayer destination) {
        //? >=26.1 {
        player.teleportTo(
                destination.level(), destination.getX(), destination.getY(), destination.getZ(),
                Set.of(), destination.getYRot(), destination.getXRot(), true
        );
        //?} else {
        /*player.teleportTo(
                destination.serverLevel(), destination.getX(), destination.getY(), destination.getZ(),
                destination.getYRot(), destination.getXRot()
        );
        *///?}
    }

    private static Component acceptLink(UUID requestId, int timeout) {
        return link(
                "gui.ender_potions.wormhole.accept", ChatFormatting.GREEN,
                "/" + EnderPotions.MOD_ID + " acceptwormhole " + requestId,
                Component.translatable("gui.ender_potions.wormhole.accept.hover", timeout)
        );
    }

    private static Component declineLink(UUID requestId) {
        return link(
                "gui.ender_potions.wormhole.decline", ChatFormatting.RED,
                "/" + EnderPotions.MOD_ID + " declinewormhole " + requestId,
                Component.translatable("gui.ender_potions.wormhole.decline.hover")
        );
    }

    private static Component link(String key, ChatFormatting color, String command, Component hover) {
        return Component.translatable(key).withStyle(style -> style
                .withColor(color)
                .withUnderlined(true)
                .withClickEvent(clickEvent(command))
                .withHoverEvent(hoverEvent(hover)));
    }

    private static ClickEvent clickEvent(String command) {
        //? >=1.21.5 {
        return new ClickEvent.RunCommand(command);
        //?} else {
        /*return new ClickEvent(ClickEvent.Action.RUN_COMMAND, command);
        *///?}
    }

    private static HoverEvent hoverEvent(Component text) {
        //? >=1.21.5 {
        return new HoverEvent.ShowText(text);
        //?} else {
        /*return new HoverEvent(HoverEvent.Action.SHOW_TEXT, text);
        *///?}
    }

    private static Component error(String key, Object... args) {
        return Component.translatable(key, args).withStyle(ChatFormatting.RED);
    }
}
