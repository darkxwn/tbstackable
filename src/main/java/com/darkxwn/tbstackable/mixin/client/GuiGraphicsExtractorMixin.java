package com.darkxwn.tbstackable.mixin.client;

import com.darkxwn.tbstackable.util.CountFormatter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.locale.Language;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Рисует счётчик стаков больше 99: "1.03k" уменьшенным шрифтом, чтобы влезало в слот. */
@Mixin(GuiGraphicsExtractor.class)
public abstract class GuiGraphicsExtractorMixin {
    @Inject(method = "itemCount", at = @At("HEAD"), cancellable = true)
    private void tbstackable$count(Font font, ItemStack stack, int x, int y, String text, CallbackInfo ci) {
        if (text != null || stack.getCount() <= 99) return;

        Language lang = Language.getInstance();
        String s = CountFormatter.format(stack.getCount(),
                lang.getOrDefault("tbstackable.count.thousands", "k"),
                lang.getOrDefault("tbstackable.count.millions", "M"));
        int width = font.width(s);
        float scale = width > 16 ? 16f / width : 1f;

        GuiGraphicsExtractor g = (GuiGraphicsExtractor) (Object) this;
        // как у ванильного текста: правый край x + 17, верх y + 9; при уменьшении прижимаем к низу слота
        float left = x + 17 - width * scale;
        float top = y + 9 + 8 * (1f - scale);
        g.pose().pushMatrix();
        g.pose().translate(left, top);
        g.pose().scale(scale, scale);
        g.text(font, s, 0, 0, 0xFFFFFFFF, true);
        g.pose().popMatrix();
        ci.cancel();
    }
}
