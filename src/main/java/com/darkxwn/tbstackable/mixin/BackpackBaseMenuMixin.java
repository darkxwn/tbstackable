package com.darkxwn.tbstackable.mixin;

import com.tiviacz.travelersbackpack.inventory.menu.BackpackBaseMenu;
import net.minecraft.util.Prediction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BackpackBaseMenu.class)
public abstract class BackpackBaseMenuMixin extends AbstractContainerMenu {

    protected BackpackBaseMenuMixin(@Nullable MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    /**
     * При Shift-клике по слоту рюкзака перемещает в инвентарь игрока не более 1 стандартного стака
     * (например, до 64 блоков), а не все 512+ предметов сразу.
     */
    @Inject(method = "quickMoveStack", at = @At("HEAD"), cancellable = true)
    private void tbstackable$quickMoveSingleStack(Player player, int slotIndex, CallbackInfoReturnable<ItemStack> cir) {
        BackpackBaseMenu menu = (BackpackBaseMenu) (Object) this;
        if (slotIndex >= menu.BACKPACK_INV_START && slotIndex < menu.BACKPACK_INV_END) {
            Slot slot = this.getSlot(slotIndex);
            if (slot != null && slot.hasItem()) {
                ItemStack inSlot = slot.getItem();
                int maxToMove = Math.min(inSlot.getCount(), inSlot.getMaxStackSize());
                if (maxToMove > 0) {
                    ItemStack toMove = inSlot.split(maxToMove);
                    this.moveItemStackTo(toMove, menu.PLAYER_INV_START, menu.PLAYER_HOT_END, true);
                    if (toMove.getCount() > 0) {
                        inSlot.grow(toMove.getCount());
                    }
                    int actuallyMoved = maxToMove - toMove.getCount();
                    if (actuallyMoved > 0) {
                        if (inSlot.isEmpty()) {
                            slot.set(ItemStack.EMPTY);
                        } else {
                            slot.set(inSlot);
                        }
                        slot.onTake(player, inSlot);
                    }
                    cir.setReturnValue(ItemStack.EMPTY);
                }
            }
        }
    }

    /**
     * При Shift+Q (buttonNum == 2) выбрасывает ровно 1 стандартный стак (до 64 шт.) или остаток,
     * а обычный Q (buttonNum == 0) и Ctrl+Q (buttonNum == 1) продолжают работать стандартно.
     */
    @Inject(method = "doClick", at = @At("HEAD"), cancellable = true)
    private void tbstackable$handleThrow(int slotId, int buttonNum, ContainerInput clickType, Player player, CallbackInfo ci) {
        if (clickType == ContainerInput.THROW && buttonNum == 2 && this.getCarried().isEmpty() && slotId >= 0 && slotId < this.slots.size()) {
            BackpackBaseMenu menu = (BackpackBaseMenu) (Object) this;
            if (slotId >= menu.BACKPACK_INV_START && slotId < menu.BACKPACK_INV_END) {
                Slot slot = this.getSlot(slotId);
                if (slot != null && slot.hasItem() && player.canDropItems()) {
                    ItemStack inSlot = slot.getItem();
                    int maxToDrop = Math.min(inSlot.getCount(), inSlot.getMaxStackSize());
                    if (maxToDrop > 0) {
                        ItemStack dropped = slot.safeTake(maxToDrop, Integer.MAX_VALUE, player);
                        player.drop(dropped, true, Prediction.PREDICTED);
                        player.handleCreativeModeItemDrop(dropped);
                        ci.cancel();
                    }
                }
            }
        }
    }
}
