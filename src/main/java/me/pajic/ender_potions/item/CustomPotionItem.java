package me.pajic.ender_potions.item;

//? >=26.2 {
import net.minecraft.advancements.triggers.CriteriaTriggers;
//?} else {
/*import net.minecraft.advancements.CriteriaTriggers;
*///?}
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.TooltipFlag;
//? >=26.1
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

//? >=26.1 {
import java.util.function.Consumer;
//?} else {
/*import java.util.List;
*///?}

public class CustomPotionItem extends PotionItem {

    private final Component tooltip;

    public CustomPotionItem(Properties properties, Component tooltip) {
        super(properties);
        this.tooltip = tooltip;
    }

    public boolean runCustomBehavior(ItemStack stack, Level level, Player player) {
        return true;
    }

    protected boolean leavesBottle() {
        return true;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity livingEntity) {
        Player player = livingEntity instanceof Player ? (Player) livingEntity : null;
        if (player != null) {
            if (!runCustomBehavior(stack, level, player)) {
                if (player instanceof ServerPlayer sp) sp.inventoryMenu.sendAllDataToRemote();
                return stack;
            }
            if (player instanceof ServerPlayer sp) CriteriaTriggers.CONSUME_ITEM.trigger(sp, stack);
            player.awardStat(Stats.ITEM_USED.get(this));
            stack.consume(1, player);
            if (leavesBottle() && !player.hasInfiniteMaterials()) {
                if (stack.isEmpty()) return new ItemStack(Items.GLASS_BOTTLE);
                player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
            }
            player.gameEvent(GameEvent.DRINK);
        }
        return stack;
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        return InteractionResult.PASS;
    }

    //? <26.1 {
    /*@Override
    public @NotNull String getDescriptionId(@NotNull ItemStack stack) {
        return getDescriptionId();
    }
    *///?}

    @SuppressWarnings("deprecation")
    @Override
    public void appendHoverText(
            @NotNull ItemStack itemStack,
            @NotNull TooltipContext context,
            //? >=26.1
            @NotNull TooltipDisplay display,
            //~ if <26.1 'Consumer' -> 'List'
            Consumer<Component> components,
            @NotNull TooltipFlag tooltipFlag
    ) {
        //~ if <26.1 'accept' -> 'add'
        components.accept(tooltip);
    }
}
