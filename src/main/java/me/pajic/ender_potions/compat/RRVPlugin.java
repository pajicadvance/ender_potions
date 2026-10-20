package me.pajic.ender_potions.compat;

//? >=26.1 && <26.3 {

/*import cc.cassian.rrv.api.ReliableRecipeViewerClientPlugin;
import cc.cassian.rrv.api.recipe.ItemView;
import cc.cassian.rrv.common.builtin.brewing.BrewingClientRecipe;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

@Entrypoint("rrv_client")
public class RRVPlugin implements ReliableRecipeViewerClientPlugin {

    @Override
    public void onIntegrationInitialize() {
        ItemView.addClientRecipeProvider(recipeList -> CompatBrewingRecipes.mixes().forEach(mix ->
                recipeList.add(new BrewingClientRecipe(
                        mix.id(), new ItemStack(mix.output()), Ingredient.of(mix.reagent()), mix.input()
                ))
        ));
    }
}
*///?}
