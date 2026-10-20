package me.pajic.ender_potions.gamerule;

import net.minecraft.server.level.ServerLevel;

//? >=26.1 {
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.serialization.Codec;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
//?} else {
/*import net.minecraft.world.level.GameRules;
*///?}
//? <26.1 && fabric {
/*import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
*///?}

public class ModGameRules {

    public static final int MAX_TELEPORT_RADIUS = 30_000_000;
    public static final int MAX_REQUEST_TIMEOUT = 3600;

    //? >=26.1 {
    public static final GameRule<Integer> TELEPORT_RADIUS = intRule(1000, 1, MAX_TELEPORT_RADIUS);
    public static final GameRule<Integer> TELEPORT_MAX_HEIGHT = intRule(320, Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final GameRule<Integer> TELEPORT_UNDERGROUND_CHANCE = intRule(50, 0, 100);
    public static final GameRule<Integer> WORMHOLE_REQUEST_TIMEOUT = intRule(60, 1, MAX_REQUEST_TIMEOUT);

    private static GameRule<Integer> intRule(int defaultValue, int min, int max) {
        return new GameRule<>(
                GameRuleCategory.PLAYER,
                GameRuleType.INT,
                IntegerArgumentType.integer(min, max),
                GameRuleTypeVisitor::visitInteger,
                Codec.intRange(min, max),
                i -> i,
                defaultValue,
                FeatureFlagSet.of()
        );
    }
    //?} else if fabric {
    /*public static final GameRules.Key<GameRules.IntegerValue> TELEPORT_RADIUS = GameRuleRegistry.register(
            "enderPotionsTeleportRadius", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(1000, 1, MAX_TELEPORT_RADIUS)
    );
    public static final GameRules.Key<GameRules.IntegerValue> TELEPORT_MAX_HEIGHT = GameRuleRegistry.register(
            "enderPotionsTeleportMaxHeight", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(320)
    );
    public static final GameRules.Key<GameRules.IntegerValue> TELEPORT_UNDERGROUND_CHANCE = GameRuleRegistry.register(
            "enderPotionsTeleportUndergroundChance", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(50, 0, 100)
    );
    public static final GameRules.Key<GameRules.IntegerValue> WORMHOLE_REQUEST_TIMEOUT = GameRuleRegistry.register(
            "enderPotionsWormholeRequestTimeout", GameRules.Category.PLAYER, GameRuleFactory.createIntRule(60, 1, MAX_REQUEST_TIMEOUT)
    );
    *///?} else {
    /*public static final GameRules.Key<GameRules.IntegerValue> TELEPORT_RADIUS = GameRules.register(
            "enderPotionsTeleportRadius", GameRules.Category.PLAYER, GameRules.IntegerValue.create(1000)
    );
    public static final GameRules.Key<GameRules.IntegerValue> TELEPORT_MAX_HEIGHT = GameRules.register(
            "enderPotionsTeleportMaxHeight", GameRules.Category.PLAYER, GameRules.IntegerValue.create(320)
    );
    public static final GameRules.Key<GameRules.IntegerValue> TELEPORT_UNDERGROUND_CHANCE = GameRules.register(
            "enderPotionsTeleportUndergroundChance", GameRules.Category.PLAYER, GameRules.IntegerValue.create(50)
    );
    public static final GameRules.Key<GameRules.IntegerValue> WORMHOLE_REQUEST_TIMEOUT = GameRules.register(
            "enderPotionsWormholeRequestTimeout", GameRules.Category.PLAYER, GameRules.IntegerValue.create(60)
    );
    *///?}

    public static void init() {}

    public static int teleportRadius(ServerLevel level) {
        //? >=26.1 {
        int radius = level.getGameRules().get(TELEPORT_RADIUS);
        //?} else {
        /*int radius = level.getGameRules().getInt(TELEPORT_RADIUS);
        *///?}
        return Math.clamp(radius, 1, MAX_TELEPORT_RADIUS);
    }

    public static int teleportMaxHeight(ServerLevel level) {
        //? >=26.1 {
        return level.getGameRules().get(TELEPORT_MAX_HEIGHT);
        //?} else {
        /*return level.getGameRules().getInt(TELEPORT_MAX_HEIGHT);
        *///?}
    }

    public static int teleportUndergroundChance(ServerLevel level) {
        //? >=26.1 {
        int chance = level.getGameRules().get(TELEPORT_UNDERGROUND_CHANCE);
        //?} else {
        /*int chance = level.getGameRules().getInt(TELEPORT_UNDERGROUND_CHANCE);
        *///?}
        return Math.clamp(chance, 0, 100);
    }

    // Seconds a wormhole teleport request waits for the target to answer
    public static int wormholeRequestTimeout(ServerLevel level) {
        //? >=26.1 {
        int timeout = level.getGameRules().get(WORMHOLE_REQUEST_TIMEOUT);
        //?} else {
        /*int timeout = level.getGameRules().getInt(WORMHOLE_REQUEST_TIMEOUT);
        *///?}
        return Math.clamp(timeout, 1, MAX_REQUEST_TIMEOUT);
    }
}
