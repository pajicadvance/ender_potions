package me.pajic.ender_potions.item;

import me.pajic.ender_potions.EnderPotions;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

//? >=26.1 {
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.Consumables;
//?}

public class ModItems {

    public static final ResourceKey<Item> POTION_OF_TELEPORTATION_KEY = ResourceKey.create(Registries.ITEM, EnderPotions.id("potion_of_teleportation"));
    public static final ResourceKey<Item> POTION_OF_WORMHOLE_KEY = ResourceKey.create(Registries.ITEM, EnderPotions.id("potion_of_wormhole"));

    public static final Item POTION_OF_TELEPORTATION = new PotionOfTeleportationItem(
            new Item.Properties()
                    //? >=26.1 {
                    .setId(POTION_OF_TELEPORTATION_KEY)
                    .component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK)
                    //?}
                    .stacksTo(1),
            Component.translatable("item.ender_potions.potion_of_teleportation.tooltip").withStyle(ChatFormatting.BLUE)
    );

    public static final Item POTION_OF_WORMHOLE = new PotionOfWormholeItem(
            new Item.Properties()
                    //? >=26.1 {
                    .setId(POTION_OF_WORMHOLE_KEY)
                    .component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK)
                    //?}
                    .stacksTo(1),
            Component.translatable("item.ender_potions.potion_of_wormhole.tooltip").withStyle(ChatFormatting.BLUE)
    );
}
