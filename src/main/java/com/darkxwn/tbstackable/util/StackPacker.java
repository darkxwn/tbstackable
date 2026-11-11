package com.darkxwn.tbstackable.util;

import com.darkxwn.tbstackable.init.ModComponents;
import net.minecraft.world.item.ItemStack;

/** Превращает стак любого размера в форму, которую можно сохранить стандартными кодеками, и обратно. */
public final class StackPacker {
    private StackPacker() {}

    public static ItemStack pack(ItemStack stack) {
        if (stack.isEmpty()) return stack;
        int max = stack.getMaxStackSize();
        int extra = StackMath.extraCount(stack.getCount(), max);
        if (extra == 0) return stack;
        ItemStack packed = stack.copyWithCount(StackMath.storedCount(stack.getCount(), max));
        packed.set(ModComponents.EXTRA_COUNT, extra);
        return packed;
    }

    public static ItemStack unpack(ItemStack stack) {
        Integer extra = stack.get(ModComponents.EXTRA_COUNT);
        if (extra == null) return stack;
        ItemStack unpacked = stack.copy();
        unpacked.remove(ModComponents.EXTRA_COUNT);
        unpacked.setCount(StackMath.restore(stack.getCount(), extra));
        return unpacked;
    }
}
