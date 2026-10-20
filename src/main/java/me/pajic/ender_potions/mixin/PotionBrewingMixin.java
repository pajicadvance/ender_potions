package me.pajic.ender_potions.mixin;

//? fabric && <26.3 {

/*import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.pajic.ender_potions.item.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PotionBrewing.class)
public class PotionBrewingMixin {

    @WrapMethod(method = "isIngredient")
    private boolean acceptEnderPotionReagents(ItemStack ingredient, Operation<Boolean> original) {
        return ender_potions$getOutput(ingredient) != null || original.call(ingredient);
    }

    @WrapMethod(method = "hasMix")
    private boolean hasEnderPotionMix(ItemStack source, ItemStack ingredient, Operation<Boolean> original) {
        return ender_potions$isAwkward(source) && ender_potions$getOutput(ingredient) != null || original.call(source, ingredient);
    }

    @WrapMethod(method = "mix")
    private ItemStack mixEnderPotion(ItemStack ingredient, ItemStack source, Operation<ItemStack> original) {
        Item output = ender_potions$getOutput(ingredient);
        return output != null && ender_potions$isAwkward(source) ? new ItemStack(output) : original.call(ingredient, source);
    }

    @Unique @Nullable
    private static Item ender_potions$getOutput(ItemStack ingredient) {
        if (ingredient.is(Items.ENDER_EYE)) return ModItems.POTION_OF_WORMHOLE;
        if (ingredient.is(Items.ENDER_PEARL)) return ModItems.POTION_OF_TELEPORTATION;
        return null;
    }

    @Unique private static boolean ender_potions$isAwkward(ItemStack stack) {
        return stack.is(Items.POTION) && stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY).is(Potions.AWKWARD);
    }
}
*///?}
