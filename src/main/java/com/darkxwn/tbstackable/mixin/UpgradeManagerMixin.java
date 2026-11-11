package com.darkxwn.tbstackable.mixin;

import com.google.common.collect.BiMap;
import com.tiviacz.travelersbackpack.inventory.UpgradeManager;
import com.tiviacz.travelersbackpack.inventory.handler.ItemStackHandler;
import com.tiviacz.travelersbackpack.inventory.upgrades.UpgradeBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(UpgradeManager.class)
public abstract class UpgradeManagerMixin {
    @Shadow
    public abstract boolean invalidateUpgrade(int slot);

    @Shadow
    public BiMap<Integer, Optional<UpgradeBase<?>>> mappedUpgrades;

    @Inject(method = "detectedChange", at = @At("HEAD"))
    private void tbstackable$onUpgradeChange(ItemStackHandler handler, int slot, CallbackInfo ci) {
        if (this.mappedUpgrades != null && this.mappedUpgrades.containsKey(slot)) {
            this.invalidateUpgrade(slot);
        }
    }
}
