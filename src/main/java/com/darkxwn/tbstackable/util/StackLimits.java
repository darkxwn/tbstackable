package com.darkxwn.tbstackable.util;

import com.darkxwn.tbstackable.StackTier;
import com.darkxwn.tbstackable.upgrade.StackUpgrade;
import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;
import com.tiviacz.travelersbackpack.inventory.handler.ItemStackHandler;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public final class StackLimits {
    private StackLimits() {}

    /** Тир улучшения стаков в рюкзаке (если оно вставлено). */
    public static Optional<StackTier> getTier(BackpackWrapper wrapper) {
        if (wrapper == null || wrapper.getUpgradeManager() == null) return Optional.empty();
        return wrapper.getUpgrade(StackUpgrade.class).map(StackUpgrade::getTier);
    }

    /** Лимит стака в хранилище при заданном тире (null = без улучшения). */
    public static int limitFor(ItemStack stack, StackTier tier) {
        int base = stack.getMaxStackSize();
        return tier == null ? base : tier.applyTo(base);
    }

    /**
     * Есть ли в хранилище стаки, которые не поместятся в лимит при заданном тире.
     * Используется для запрета снятия/понижения улучшения.
     */
    public static boolean wouldOverflow(BackpackWrapper wrapper, StackTier tier) {
        ItemStackHandler storage = wrapper.getStorage();
        for (int i = 0; i < storage.getSlots(); i++) {
            ItemStack s = storage.getStackInSlot(i);
            if (!s.isEmpty() && s.getCount() > limitFor(s, tier)) return true;
        }
        return false;
    }
}
