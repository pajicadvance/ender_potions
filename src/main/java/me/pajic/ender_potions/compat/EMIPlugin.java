package me.pajic.ender_potions.compat;

//? fabric && <26.1 {

/*import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;

@Entrypoint("emi")
public class EMIPlugin implements EmiPlugin {

    @Override
    public void register(EmiRegistry registry) {
        CompatBrewingRecipes.mixes().forEach(mix -> registry.addRecipe(new BrewingRecipe(mix)));
    }

    private static class BrewingRecipe extends BasicEmiRecipe {

        private static final Identifier BACKGROUND = Identifier.withDefaultNamespace("textures/gui/container/brewing_stand.png");
        private static final EmiStack BLAZE_POWDER = EmiStack.of(Items.BLAZE_POWDER);

        private final EmiStack input;
        private final EmiStack reagent;
        private final EmiStack output;

        BrewingRecipe(CompatBrewingRecipes.Mix mix) {
            super(VanillaEmiRecipeCategories.BREWING, mix.id(), 120, 61);
            input = EmiStack.of(mix.input());
            reagent = EmiStack.of(mix.reagent());
            output = EmiStack.of(mix.output());
            inputs.add(input.copy().setAmount(3));
            inputs.add(reagent);
            outputs.add(output.copy().setAmount(3));
        }

        @Override
        public void addWidgets(WidgetHolder widgets) {
            widgets.addTexture(BACKGROUND, 0, 0, 103, 61, 16, 14);
            widgets.addAnimatedTexture(BACKGROUND, 81, 2, 9, 28, 176, 0, 1000 * 20, false, false, false);
            widgets.addAnimatedTexture(BACKGROUND, 47, 0, 12, 29, 185, 0, 700, false, true, false);
            widgets.addTexture(BACKGROUND, 44, 30, 18, 4, 176, 29);
            widgets.addSlot(BLAZE_POWDER, 0, 2).drawBack(false);
            widgets.addSlot(input, 39, 36).drawBack(false);
            widgets.addSlot(reagent, 62, 2).drawBack(false);
            widgets.addSlot(output, 85, 36).drawBack(false).recipeContext(this);
        }
    }
}
*///?}
