package com.darkxwn.tbstackable.mixin;

import com.darkxwn.tbstackable.StackTier;
import com.darkxwn.tbstackable.item.StackUpgradeItem;
import com.darkxwn.tbstackable.util.StackLimits;
import com.tiviacz.travelersbackpack.inventory.menu.BackpackBaseMenu;
import com.tiviacz.travelersbackpack.inventory.menu.slot.SlotItemHandler;
import com.tiviacz.travelersbackpack.inventory.menu.slot.UpgradeLockableSlotItemHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Запрещает вынимать улучшение стаков (или менять на более слабое), пока стаки не помещаются без него. */
@Mixin(UpgradeLockableSlotItemHandler.class)
public abstract class UpgradeLockableSlotItemHandlerMixin {
    @Shadow
    public BackpackBaseMenu menu;

    @Shadow
    public boolean isHidden;

    @Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true)
    private void tbstackable$lockRemoval(Player player, CallbackInfoReturnable<Boolean> cir) {
        ItemStack inSlot = ((SlotItemHandler) (Object) this).getItem();
        if (!(inSlot.getItem() instanceof StackUpgradeItem current)) return;

        // Замена прямо кликом: на курсоре другое улучшение стаков
        ItemStack carried = player.containerMenu.getCarried();
        StackTier replacement = carried.getItem() instanceof StackUpgradeItem other ? other.getTier() : null;

        if (StackLimits.wouldOverflow(menu.getWrapper(), replacement)) {
            cir.setReturnValue(false);
        } else {
            cir.setReturnValue(!this.isHidden);
        }
    }

    @Inject(method = "isActive", at = @At("HEAD"), cancellable = true)
    private void tbstackable$isActive(CallbackInfoReturnable<Boolean> cir) {
        ItemStack inSlot = ((SlotItemHandler) (Object) this).getItem();
        if (inSlot.getItem() instanceof StackUpgradeItem) {
            cir.setReturnValue(!this.isHidden);
        }
    }
}
