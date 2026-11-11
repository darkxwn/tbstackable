package com.darkxwn.tbstackable.item;

import com.darkxwn.tbstackable.StackTier;
import com.darkxwn.tbstackable.upgrade.StackUpgrade;
import com.tiviacz.travelersbackpack.inventory.UpgradeManager;
import com.tiviacz.travelersbackpack.inventory.upgrades.UpgradeBase;
import com.tiviacz.travelersbackpack.item.upgrade.UpgradeItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.apache.commons.lang3.function.TriFunction;

import java.util.Optional;
import java.util.function.Consumer;

public class StackUpgradeItem extends UpgradeItem {
    private final StackTier tier;

    public StackUpgradeItem(Item.Properties properties, StackTier tier) {
        super(properties, null);
        this.tier = tier;
    }

    public StackTier getTier() {
        return tier;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display,
                                Consumer<Component> consumer, TooltipFlag flag) {
        consumer.accept(Component.translatable("item.tbstackable.stack_upgrade_tooltip", tier.multiplierText())
                .withStyle(ChatFormatting.BLUE));
        consumer.accept(Component.translatable("item.travelersbackpack.upgrade_apply_tooltip"));
    }

    @Override
    public boolean isTickingUpgrade() {
        return false;
    }

    /** Лимит стака должен работать и у надетого, и у поставленного рюкзака. */
    @Override
    public boolean requiresEquippedBackpack() {
        return false;
    }

    // TODO: проверить в игре, что значит этот флаг (у Lantern он переопределён)
    @Override
    public boolean hasBlockFunctionality() {
        return true;
    }

    @Override
    public Class<? extends UpgradeBase<?>> getUpgradeClass() {
        return StackUpgrade.class;
    }

    @Override
    public TriFunction<UpgradeManager, Integer, ItemStack, Optional<? extends UpgradeBase<?>>> getUpgrade() {
        return (manager, slot, stack) -> Optional.of(new StackUpgrade(manager, slot, tier));
    }
}
