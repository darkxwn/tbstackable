package com.darkxwn.tbstackable.util;

import com.darkxwn.tbstackable.StackTier;

/**
 * Состояние красной подсветки слотов. Только клиент, но без клиентских классов Minecraft,
 * поэтому безопасно вызывается из общего кода.
 */
public final class OverflowHighlight {
    private static final long DURATION_MS = 3000;
    private static final long FADE_MS = 700;

    private static volatile long until = 0;
    private static volatile StackTier replacement = null;

    private OverflowHighlight() {}

    /** replacementTier: на что собираются заменить улучшение (null = просто вынуть). */
    public static void trigger(StackTier replacementTier) {
        replacement = replacementTier;
        until = System.currentTimeMillis() + DURATION_MS;
    }

    public static boolean isActive() {
        return System.currentTimeMillis() < until;
    }

    public static StackTier replacement() {
        return replacement;
    }

    /** ARGB: красный, в последние 0.7 с плавно гаснет. */
    public static int color() {
        long left = until - System.currentTimeMillis();
        float k = left >= FADE_MS ? 1f : Math.max(0f, left / (float) FADE_MS);
        int alpha = (int) (0x90 * k);
        return (alpha << 24) | 0xFF2020;
    }
}
