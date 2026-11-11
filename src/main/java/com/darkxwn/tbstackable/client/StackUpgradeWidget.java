package com.darkxwn.tbstackable.client;

import com.darkxwn.tbstackable.upgrade.StackUpgrade;
import com.tiviacz.travelersbackpack.client.screens.BackpackScreen;
import com.tiviacz.travelersbackpack.client.screens.widgets.UpgradeWidgetBase;
import com.tiviacz.travelersbackpack.inventory.upgrades.Point;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;

/**
 * "Пустой" виджет: у улучшения нет вкладки, кнопок и тумблера, но экран рюкзака требует виджет.
 * TODO: проверить в игре, что ничего не рисуется и клики не перехватываются.
 */
public class StackUpgradeWidget extends UpgradeWidgetBase<StackUpgrade> {
    public StackUpgradeWidget(BackpackScreen screen, StackUpgrade upgrade, Point pos, Point uv, String tooltipKey) {
        super(screen, upgrade, pos, uv, tooltipKey);
    }

    @Override
    public boolean hasButtons() {
        return false;
    }

    @Override
    public void renderBg(GuiGraphicsExtractor g, int x, int y, int mouseX, int mouseY) {
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float partialTick) {
    }

    @Override
    public void renderTooltip(GuiGraphicsExtractor g, int mouseX, int mouseY) {
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return false;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        return false;
    }
}
