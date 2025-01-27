package me.pajic.ender_potions;

import me.pajic.ender_potions.config.ModCommonConfig;
import me.pajic.ender_potions.config.ModServerConfig;
import me.pajic.ender_potions.item.ModItems;
import me.pajic.ender_potions.network.ModNetworking;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod("ender_potions")
public class Main {

    public Main(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, ModCommonConfig.COMMON_SPEC);
        modContainer.registerConfig(ModConfig.Type.SERVER, ModServerConfig.SERVER_SPEC);
        modEventBus.addListener(this::registerItems);
        NeoForge.EVENT_BUS.addListener(this::registerBrewingRecipes);
        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(ModNetworking::init);
    }

    private void registerItems(RegisterEvent event) {
        event.register(Registries.ITEM, registry -> {
            registry.register(
                    ResourceLocation.parse("ender_potions:potion_of_wormhole"),
                    ModItems.POTION_OF_WORMHOLE
            );
            registry.register(
                    ResourceLocation.parse("ender_potions:potion_of_teleportation"),
                    ModItems.POTION_OF_TELEPORTATION
            );
        });
    }

    private void registerBrewingRecipes(RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();
        ItemStack awkwardPotion = new ItemStack(Items.POTION);
        awkwardPotion.update(DataComponents.POTION_CONTENTS, PotionContents.EMPTY, Potions.AWKWARD, PotionContents::withPotion);
        builder.addRecipe(
                DataComponentIngredient.of(true, awkwardPotion),
                Ingredient.of(Items.ENDER_EYE),
                new ItemStack(ModItems.POTION_OF_WORMHOLE)
        );
        builder.addRecipe(
                DataComponentIngredient.of(true, awkwardPotion),
                Ingredient.of(Items.CHORUS_FRUIT),
                new ItemStack(ModItems.POTION_OF_TELEPORTATION)
        );
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.insertAfter(Items.POTION.getDefaultInstance(), ModItems.POTION_OF_TELEPORTATION.getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            event.insertAfter(ModItems.POTION_OF_TELEPORTATION.getDefaultInstance(), ModItems.POTION_OF_WORMHOLE.getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }
}
