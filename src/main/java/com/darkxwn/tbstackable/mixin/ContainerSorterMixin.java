package com.darkxwn.tbstackable.mixin;

import com.darkxwn.tbstackable.StackTier;
import com.darkxwn.tbstackable.util.StackLimits;
import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;
import com.tiviacz.travelersbackpack.inventory.handler.ItemStackHandler;
import com.tiviacz.travelersbackpack.inventory.sorter.ContainerSorter;
import com.tiviacz.travelersbackpack.inventory.sorter.SortSelector;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(ContainerSorter.class)
public abstract class ContainerSorterMixin {

    @Inject(method = "sortBackpack", at = @At("HEAD"), cancellable = true)
    private static void tbstackable$sortBackpack(BackpackWrapper wrapper, Player player, SortSelector.SortType sortType, boolean shift, CallbackInfo ci) {
        if (wrapper == null) return;
        Optional<StackTier> tierOpt = StackLimits.getTier(wrapper);
        if (tierOpt.isEmpty()) return; // Без улучшения стаков работает оригинальная сортировка

        ci.cancel();

        if (shift) {
            wrapper.setNextSortType();
            return;
        }

        StackTier tier = tierOpt.get();
        List<ItemStack> list = new ArrayList<>();
        ItemStackHandler storage = wrapper.getStorage();
        List<Integer> unsortable = wrapper.getUnsortableSlots();

        for (int i = 0; i < storage.getSlots(); i++) {
            if (unsortable != null && unsortable.contains(i)) {
                continue;
            }
            ItemStack stack = storage.getStackInSlot(i);
            if (!stack.isEmpty()) {
                tbstackable$addStackWithMerge(list, stack.copy(), tier);
            }
        }

        if (!list.isEmpty()) {
            list.sort(SortSelector.getSortTypeComparator(list, sortType));
        }

        int listIndex = 0;
        for (int i = 0; i < storage.getSlots(); i++) {
            if (unsortable != null && unsortable.contains(i)) {
                continue;
            }
            if (listIndex < list.size()) {
                storage.setStackInSlot(i, list.get(listIndex++));
            } else {
                storage.setStackInSlot(i, ItemStack.EMPTY);
            }
        }
    }

    @Unique
    private static void tbstackable$addStackWithMerge(List<ItemStack> list, ItemStack toAdd, StackTier tier) {
        if (toAdd.isEmpty()) return;
        int maxLimit = StackLimits.limitFor(toAdd, tier);

        if (toAdd.isStackable()) {
            for (ItemStack existing : list) {
                if (existing.getCount() < maxLimit && ItemStack.isSameItemSameComponents(existing, toAdd)) {
                    int space = maxLimit - existing.getCount();
                    int canAdd = Math.min(toAdd.getCount(), space);
                    existing.grow(canAdd);
                    toAdd.shrink(canAdd);
                    if (toAdd.isEmpty()) return;
                }
            }
        }

        while (!toAdd.isEmpty()) {
            if (toAdd.getCount() <= maxLimit) {
                list.add(toAdd);
                break;
            } else {
                list.add(toAdd.split(maxLimit));
            }
        }
    }
}
