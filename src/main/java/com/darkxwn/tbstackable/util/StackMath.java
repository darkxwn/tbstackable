package com.darkxwn.tbstackable.util;

/** Чистая арифметика (без классов Minecraft), чтобы её можно было тестировать отдельно. */
public final class StackMath {
    private StackMath() {}

    /** Сколько единиц остаётся в самом стаке при сохранении (не больше обычного лимита предмета). */
    public static int storedCount(int count, int vanillaMax) {
        return Math.min(count, vanillaMax);
    }

    /** Сколько единиц уходит в отдельное поле "extra_count" при сохранении. */
    public static int extraCount(int count, int vanillaMax) {
        return Math.max(0, count - vanillaMax);
    }

    /** Обратная операция при загрузке. */
    public static int restore(int stored, int extra) {
        return stored + extra;
    }
}
