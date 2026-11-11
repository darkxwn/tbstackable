package com.darkxwn.tbstackable.mixin.client;

import com.darkxwn.tbstackable.StackTier;
import com.darkxwn.tbstackable.item.StackUpgradeItem;
import com.darkxwn.tbstackable.util.OverflowHighlight;
import com.darkxwn.tbstackable.util.StackLimits;
import com.tiviacz.travelersbackpack.inventory.menu.BackpackBaseMenu;
import com.tiviacz.travelersbackpack.inventory.menu.slot.BackpackSlotItemHandler;
import com.tiviacz.travelersbackpack.inventory.menu.slot.UpgradeLockableSlotItemHandler;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Красная подсветка слотов, стаки в которых не поместятся без улучшения (на 3 секунды). */
@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin<T extends AbstractContainerMenu> {
    @Shadow
    protected T menu;

    @Inject(method = "extractSlot", at = @At("RETURN"))
    private void tbstackable$highlight(GuiGraphicsExtractor g, Slot slot, int mouseX, int mouseY, CallbackInfo ci) {
        if (!OverflowHighlight.isActive() || !(slot instanceof BackpackSlotItemHandler)) return;
        ItemStack stack = slot.getItem();
        if (stack.isEmpty() || stack.getCount() <= StackLimits.limitFor(stack, OverflowHighlight.replacement())) return;
        g.fill(slot.x, slot.y, slot.x + 16, slot.y + 16, OverflowHighlight.color());
    }

    @Inject(method = "slotClicked", at = @At("HEAD"))
    private void tbstackable$onSlotClicked(Slot slot, int slotId, int mouseButton, ContainerInput clickType, CallbackInfo ci) {
        if (!(slot instanceof UpgradeLockableSlotItemHandler upgradeSlot)) return;
        ItemStack inSlot = upgradeSlot.getItem();
        if (!(inSlot.getItem() instanceof StackUpgradeItem)) return;
        if (!(this.menu instanceof BackpackBaseMenu backpackMenu)) return;

        ItemStack carried = this.menu.getCarried();
        StackTier replacement = null;

        if (clickType == ContainerInput.SWAP && mouseButton >= 0 && mouseButton < 9) {
            ItemStack hotbarStack = backpackMenu.player.getInventory().getItem(mouseButton);
            if (hotbarStack.getItem() instanceof StackUpgradeItem hotbarUpgrade) {
                replacement = hotbarUpgrade.getTier();
            }
        } else if (carried.getItem() instanceof StackUpgradeItem carriedUpgrade) {
            replacement = carriedUpgrade.getTier();
        }

        if (StackLimits.wouldOverflow(backpackMenu.getWrapper(), replacement)) {
            OverflowHighlight.trigger(replacement);
        }
    }
}
