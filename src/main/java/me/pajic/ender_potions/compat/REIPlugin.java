package me.pajic.ender_potions.compat;

//? fabric && <26.3 {

/*import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.plugin.client.BuiltinClientPlugin;
import net.minecraft.world.item.ItemStack;

//? <26.1 {
/^import net.minecraft.world.item.crafting.Ingredient;
^///?} else {
import me.shedaniel.rei.api.common.util.EntryIngredients;

import java.util.List;
//?}

@Entrypoint("rei_client")
public class REIPlugin implements REIClientPlugin {

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        BuiltinClientPlugin plugin = BuiltinClientPlugin.getInstance();
        CompatBrewingRecipes.mixes().forEach(mix -> {
            //? <26.1 {
            /^plugin.registerBrewingRecipe(mix.input(), Ingredient.of(mix.reagent()), new ItemStack(mix.output()));
            ^///?} else {
            plugin.registerBrewingRecipe(
                    EntryIngredients.ofItemStacks(List.of(mix.input())),
                    EntryIngredients.ofItemStacks(List.of(new ItemStack(mix.reagent()))),
                    EntryIngredients.ofItemStacks(List.of(new ItemStack(mix.output())))
            );
            //?}
        });
    }
}
*///?}
