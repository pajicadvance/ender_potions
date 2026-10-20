package me.pajic.ender_potions.compat;

//? <26.3 {

/*import me.pajic.ender_potions.EnderPotions;
import me.pajic.ender_potions.item.ModItems;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;

import java.util.List;

public final class CompatBrewingRecipes {

    public record Mix(Identifier id, Item reagent, Item output) {
        public ItemStack input() {
            return PotionContents.createItemStack(Items.POTION, Potions.AWKWARD);
        }
    }

    public static List<Mix> mixes() {
        return List.of(
                new Mix(EnderPotions.id("brewing/potion_of_wormhole"), Items.ENDER_EYE, ModItems.POTION_OF_WORMHOLE),
                new Mix(EnderPotions.id("brewing/potion_of_teleportation"), Items.ENDER_PEARL, ModItems.POTION_OF_TELEPORTATION)
        );
    }
}
*///?}
