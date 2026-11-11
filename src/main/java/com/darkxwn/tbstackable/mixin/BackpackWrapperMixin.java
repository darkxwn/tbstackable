package com.darkxwn.tbstackable.mixin;

import com.darkxwn.tbstackable.util.StackLimitHolder;
import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;
import com.tiviacz.travelersbackpack.inventory.upgrades.UpgradeBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mixin(BackpackWrapper.class)
public abstract class BackpackWrapperMixin {
    /** После загрузки хранилища связываем его с рюкзаком, чтобы лимит стака знал про улучшение. */
    @Inject(method = "loadStorage", at = @At("RETURN"))
    private void tbstackable$attach(CallbackInfo ci) {
        BackpackWrapper self = (BackpackWrapper) (Object) this;
        ((StackLimitHolder) self.getStorage()).tbstackable$setWrapper(self);
    }

    /** Защита от ConcurrentModificationException при тике улучшений рюкзака-блока */
    @Redirect(
        method = "tickForBlockEntity",
        at = @At(value = "INVOKE", target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V")
    )
    private static void tbstackable$safeTickForBlockEntity(List<UpgradeBase<?>> list, Consumer<UpgradeBase<?>> consumer) {
        new ArrayList<>(list).forEach(consumer);
    }

    /** Защита от ConcurrentModificationException при тике улучшений надетого рюкзака */
    @Redirect(
        method = "tick",
        at = @At(value = "INVOKE", target = "Ljava/util/List;forEach(Ljava/util/function/Consumer;)V")
    )
    private static void tbstackable$safeTick(List<UpgradeBase<?>> list, Consumer<UpgradeBase<?>> consumer) {
        new ArrayList<>(list).forEach(consumer);
    }
}
