package com.darkxwn.tbstackable.mixin;

import com.darkxwn.tbstackable.util.StackPacker;
import com.tiviacz.travelersbackpack.util.ContainerContentsHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Единственные места, где содержимое рюкзака превращается в ItemContainerContents и обратно. */
@Mixin(ContainerContentsHelper.class)
public abstract class ContainerContentsHelperMixin {
    /** Запись: большой стак упаковывается (count не больше обычного лимита + поле extra_count). */
    @ModifyVariable(method = "updateStack", at = @At("HEAD"), argsOnly = true)
    private static ItemStack tbstackable$pack(ItemStack stack) {
        return StackPacker.pack(stack);
    }

    /** Чтение: упакованные стаки возвращаются к настоящему размеру. */
    @Inject(method = "getItems", at = @At("RETURN"))
    private static void tbstackable$unpack(ItemContainerContents contents, int size,
                                           CallbackInfoReturnable<NonNullList<ItemStack>> cir) {
        cir.getReturnValue().replaceAll(StackPacker::unpack);
    }
}
