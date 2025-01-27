package me.pajic.ender_potions.plugin;

import dev.emi.emi.EmiPort;
import dev.emi.emi.EmiUtil;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.recipe.EmiBrewingRecipe;
import me.pajic.ender_potions.item.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;

public class EMIPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        ItemStack awkwardPotion = new ItemStack(Items.POTION);
        awkwardPotion.set(DataComponents.POTION_CONTENTS, new PotionContents(Potions.AWKWARD));

        registry.addRecipe(new EmiBrewingRecipe(
                EmiStack.of(awkwardPotion),
                EmiStack.of(Items.CHORUS_FRUIT),
                EmiStack.of(ModItems.POTION_OF_TELEPORTATION),
                EmiPort.id(
                        "emi",
                        "/" + "brewing/item" +
                                "/" + EmiUtil.subId(awkwardPotion.getItem()) +
                                "/" + EmiUtil.subId(Items.CHORUS_FRUIT) +
                                "/" + EmiUtil.subId(ModItems.POTION_OF_TELEPORTATION)
                )
        ));

        registry.addRecipe(new EmiBrewingRecipe(
                EmiStack.of(awkwardPotion),
                EmiStack.of(Items.ENDER_EYE),
                EmiStack.of(ModItems.POTION_OF_WORMHOLE),
                EmiPort.id(
                        "emi",
                        "/" + "brewing/item" +
                                "/" + EmiUtil.subId(awkwardPotion.getItem()) +
                                "/" + EmiUtil.subId(Items.ENDER_EYE) +
                                "/" + EmiUtil.subId(ModItems.POTION_OF_WORMHOLE)
                )
        ));
    }
}
