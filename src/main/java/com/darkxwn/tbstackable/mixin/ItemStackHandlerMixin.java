package com.darkxwn.tbstackable.mixin;

import com.darkxwn.tbstackable.StackTier;
import com.darkxwn.tbstackable.util.StackLimitHolder;
import com.darkxwn.tbstackable.util.StackLimits;
import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;
import com.tiviacz.travelersbackpack.inventory.handler.ItemStackHandler;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(ItemStackHandler.class)
public abstract class ItemStackHandlerMixin implements StackLimitHolder {
    @Unique
    private BackpackWrapper tbstackable$wrapper;

    @Shadow
    public abstract int getSlotLimit(int slot);

    @Override
    public void tbstackable$setWrapper(BackpackWrapper wrapper) {
        this.tbstackable$wrapper = wrapper;
    }

    @Inject(method = "getStackLimit", at = @At("HEAD"), cancellable = true)
    private void tbstackable$stackLimit(int slot, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (tbstackable$wrapper == null) return;
        Optional<StackTier> tier = StackLimits.getTier(tbstackable$wrapper);
        if (tier.isEmpty()) return;
        cir.setReturnValue(Math.min(getSlotLimit(slot), StackLimits.limitFor(stack, tier.get())));
    }
}
