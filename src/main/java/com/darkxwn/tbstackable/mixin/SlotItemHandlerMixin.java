package com.darkxwn.tbstackable.mixin;

import com.darkxwn.tbstackable.StackTier;
import com.darkxwn.tbstackable.util.StackLimitHolder;
import com.darkxwn.tbstackable.util.StackLimits;
import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;
import com.tiviacz.travelersbackpack.inventory.handler.ItemStackHandler;
import com.tiviacz.travelersbackpack.inventory.menu.slot.SlotItemHandler;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(SlotItemHandler.class)
public abstract class SlotItemHandlerMixin {
    @Shadow
    public abstract ItemStackHandler getItemHandler();

    @Inject(method = "getMaxStackSize(Lnet/minecraft/world/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
    private void tbstackable$slotMaxStackSize(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (stack.isEmpty()) return;
        if (this.getItemHandler() instanceof StackLimitHolder holder) {
            BackpackWrapper wrapper = holder.tbstackable$getWrapper();
            if (wrapper != null) {
                Optional<StackTier> tier = StackLimits.getTier(wrapper);
                if (tier.isPresent()) {
                    cir.setReturnValue(StackLimits.limitFor(stack, tier.get()));
                }
            }
        }
    }
}
