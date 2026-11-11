package com.darkxwn.tbstackable.mixin;

import com.darkxwn.tbstackable.util.StackLimitHolder;
import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackpackWrapper.class)
public abstract class BackpackWrapperMixin {
    /** После загрузки хранилища связываем его с рюкзаком, чтобы лимит стака знал про улучшение. */
    @Inject(method = "loadStorage", at = @At("RETURN"))
    private void tbstackable$attach(CallbackInfo ci) {
        BackpackWrapper self = (BackpackWrapper) (Object) this;
        ((StackLimitHolder) self.getStorage()).tbstackable$setWrapper(self);
    }
}
