package me.pajic.ender_potions.item;

import me.emafire003.dev.custombrewrecipes.CustomBrewRecipeRegister;
import me.pajic.ender_potions.Main;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
//? if > 1.21.1 {
/*import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.component.Consumables;
*///?}

public class ModItems {

    public static final Item POTION_OF_TELEPORTATION = new PotionOfTeleportationItem(
            new Item.Properties().stacksTo(1)/*? if > 1.21.1 {*//*.setId(ResourceKey.create(Registries.ITEM, ResourceLocation.parse("ender_potions:potion_of_teleportation"))).component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK).usingConvertsTo(Items.GLASS_BOTTLE)*//*?}*/,
            Component.translatable("item.ender_potions.potion_of_teleportation.tooltip").withStyle(ChatFormatting.BLUE),
            Main.CONFIG.enablePotionOfTeleportation()
    );

    public static final Item POTION_OF_WORMHOLE = new PotionOfWormholeItem(
            new Item.Properties().stacksTo(1)/*? if > 1.21.1 {*//*.setId(ResourceKey.create(Registries.ITEM, ResourceLocation.parse("ender_potions:potion_of_wormhole"))).component(DataComponents.CONSUMABLE, Consumables.DEFAULT_DRINK).usingConvertsTo(Items.GLASS_BOTTLE)*//*?}*/,
            Component.translatable("item.ender_potions.potion_of_wormhole.tooltip").withStyle(ChatFormatting.BLUE),
            Main.CONFIG.enablePotionOfWormhole()
    );

    public static void init() {
        Registry.register(
                BuiltInRegistries.ITEM,
                ResourceLocation.parse("ender_potions:potion_of_teleportation"),
                POTION_OF_TELEPORTATION
        );
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(contents -> contents.addAfter(
                Items.POTION,
                POTION_OF_TELEPORTATION
        ));
        CustomBrewRecipeRegister.registerCustomRecipeWithComponents(
                Items.POTION,
                Items.CHORUS_FRUIT,
                POTION_OF_TELEPORTATION,
                DataComponentMap.builder().set(
                        DataComponents.POTION_CONTENTS,
                        new PotionContents(Potions.AWKWARD)
                ).build(),
                null,
                null
        );

        Registry.register(
                BuiltInRegistries.ITEM,
                ResourceLocation.parse("ender_potions:potion_of_wormhole"),
                POTION_OF_WORMHOLE
        );
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS).register(contents -> contents.addAfter(
                POTION_OF_TELEPORTATION,
                POTION_OF_WORMHOLE
        ));
        CustomBrewRecipeRegister.registerCustomRecipeWithComponents(
                Items.POTION,
                Items.ENDER_EYE,
                POTION_OF_WORMHOLE,
                DataComponentMap.builder().set(
                        DataComponents.POTION_CONTENTS,
                        new PotionContents(Potions.AWKWARD)
                ).build(),
                null,
                null
        );
    }
}
