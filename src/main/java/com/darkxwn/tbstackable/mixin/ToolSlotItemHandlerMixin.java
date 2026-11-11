package com.darkxwn.tbstackable.mixin;

import com.tiviacz.travelersbackpack.inventory.menu.slot.ToolSlotItemHandler;
import com.tiviacz.travelersbackpack.item.upgrade.UpgradeItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ToolSlotItemHandler.class)
public abstract class ToolSlotItemHandlerMixin {
    @Inject(method = "isValid", at = @At("HEAD"), cancellable = true)
    private static void tbstackable$noUpgradesInToolSlots(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!stack.isEmpty() && stack.getItem() instanceof UpgradeItem) {
            cir.setReturnValue(false);
        }
    }
}
