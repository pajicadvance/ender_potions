package me.pajic.ender_potions.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
//? if > 1.21.1 {
/*import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Consumables;
*///?}

public class ModItems {

    public static final Item POTION_OF_TELEPORTATION = new PotionOfTeleportationItem(
            new Item.Properties().stacksTo(1)/*? if > 1.21.1 {*//*.setId(ResourceKey.create(Registries.ITEM, ResourceLocation.parse("ender_potions:potion_of_teleportation"))).component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK).usingConvertsTo(Items.GLASS_BOTTLE)*//*?}*/,
            Component.translatable("item.ender_potions.potion_of_teleportation.tooltip").withStyle(ChatFormatting.BLUE)
    );

    public static final Item POTION_OF_WORMHOLE = new PotionOfWormholeItem(
            new Item.Properties().stacksTo(1)/*? if > 1.21.1 {*//*.setId(ResourceKey.create(Registries.ITEM, ResourceLocation.parse("ender_potions:potion_of_wormhole"))).component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK).usingConvertsTo(Items.GLASS_BOTTLE)*//*?}*/,
            Component.translatable("item.ender_potions.potion_of_wormhole.tooltip").withStyle(ChatFormatting.BLUE)
    );
}
