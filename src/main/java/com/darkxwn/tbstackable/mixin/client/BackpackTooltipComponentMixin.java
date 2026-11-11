package com.darkxwn.tbstackable.mixin.client;

import com.darkxwn.tbstackable.util.StackPacker;
import com.tiviacz.travelersbackpack.client.screens.tooltip.BackpackTooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.List;

/** Превью по Ctrl: суммируем настоящие количества и не режем стак на куски по 999. */
@Mixin(BackpackTooltipComponent.class)
public abstract class BackpackTooltipComponentMixin {
    @ModifyVariable(method = "mergeStacks", at = @At("HEAD"), argsOnly = true)
    private List<ItemStack> tbstackable$unpack(List<ItemStack> stacks) {
        List<ItemStack> out = new ArrayList<>(stacks.size());
        for (ItemStack s : stacks) out.add(StackPacker.unpack(s));
        return out;
    }

    @ModifyConstant(method = "mergeStacks", constant = @Constant(intValue = 999))
    private int tbstackable$noSplit(int original) {
        return Integer.MAX_VALUE;
    }
}
