package com.darkxwn.tbstackable.mixin.client;

import com.darkxwn.tbstackable.util.OverflowHighlight;
import com.darkxwn.tbstackable.util.StackLimits;
import com.tiviacz.travelersbackpack.inventory.menu.slot.BackpackSlotItemHandler;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Красная подсветка слотов, стаки в которых не поместятся без улучшения (на 3 секунды). */
@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {
    @Inject(method = "extractSlot", at = @At("RETURN"))
    private void tbstackable$highlight(GuiGraphicsExtractor g, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {
        if (!OverflowHighlight.isActive() || !(slot instanceof BackpackSlotItemHandler)) return;
        ItemStack stack = slot.getItem();
        if (stack.isEmpty() || stack.getCount() <= StackLimits.limitFor(stack, OverflowHighlight.replacement())) return;
        g.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, OverflowHighlight.color());
    }
}
