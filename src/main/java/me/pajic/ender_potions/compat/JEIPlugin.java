package me.pajic.ender_potions.compat;

//? fabric && <26.3 {

/*import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import me.pajic.ender_potions.EnderPotions;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Entrypoint("jei_mod_plugin")
public class JEIPlugin implements IModPlugin {

    @Override
    public @NotNull Identifier getPluginUid() {
        return EnderPotions.id("jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IVanillaRecipeFactory factory = registration.getVanillaRecipeFactory();
        List<IJeiBrewingRecipe> recipes = CompatBrewingRecipes.mixes().stream()
                .map(mix -> factory.createBrewingRecipe(
                        List.of(new ItemStack(mix.reagent())), mix.input(), new ItemStack(mix.output()), mix.id()
                ))
                .toList();
        registration.addRecipes(RecipeTypes.BREWING, recipes);
    }
}
*///?}
