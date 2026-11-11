package com.darkxwn.tbstackable.upgrade;

import com.darkxwn.tbstackable.StackTier;
import com.darkxwn.tbstackable.client.StackUpgradeWidget;
import com.mojang.datafixers.util.Pair;
import com.tiviacz.travelersbackpack.client.screens.BackpackScreen;
import com.tiviacz.travelersbackpack.client.screens.widgets.WidgetBase;
import com.tiviacz.travelersbackpack.inventory.BackpackWrapper;
import com.tiviacz.travelersbackpack.inventory.UpgradeManager;
import com.tiviacz.travelersbackpack.inventory.menu.BackpackBaseMenu;
import com.tiviacz.travelersbackpack.inventory.upgrades.Point;
import com.tiviacz.travelersbackpack.inventory.upgrades.UpgradeBase;
import net.minecraft.world.inventory.Slot;

import java.util.List;

/** Пассивное улучшение без вкладки, слотов и тумблера. Сам множитель применяется миксинами (этап 2). */
public class StackUpgrade extends UpgradeBase<StackUpgrade> {
    private final StackTier tier;

    public StackUpgrade(UpgradeManager manager, int dataHolderSlot, StackTier tier) {
        super(manager, dataHolderSlot, new Point(0, 0));
        this.tier = tier;
    }

    public StackTier getTier() {
        return tier;
    }

    @Override
    public WidgetBase<?> createWidget(BackpackScreen screen, int x, int y) {
        // Экран вызывает createWidget для каждого улучшения, поэтому null возвращать нельзя.
        return new StackUpgradeWidget(screen, this,
                new Point(screen.getGuiLeft() + x, screen.getGuiTop() + y),
                new Point(0, 0),
                "screen.tbstackable.stack_upgrade");
    }

    @Override
    public boolean hasTab() {
        return false;
    }

    @Override
    public List<Pair<Integer, Integer>> getUpgradeSlotsPosition(int x, int y) {
        return List.of();
    }

    @Override
    public List<? extends Slot> getUpgradeSlots(BackpackBaseMenu menu, BackpackWrapper wrapper, int x, int y) {
        return List.of();
    }
}
