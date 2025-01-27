package me.pajic.ender_potions.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.pajic.ender_potions.item.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @ModifyExpressionValue(
            method = "randomTeleport",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;hasChunkAt(Lnet/minecraft/core/BlockPos;)Z"
            )
    )
    private boolean removeChunkCheckForPotion(boolean original) {
        if (
                (LivingEntity) (Object) this instanceof Player p &&
                (p.getMainHandItem().is(ModItems.POTION_OF_TELEPORTATION) || p.getOffhandItem().is(ModItems.POTION_OF_TELEPORTATION)))
        {
            return true;
        }
        return original;
    }
}
