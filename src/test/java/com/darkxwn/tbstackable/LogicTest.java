package com.darkxwn.tbstackable;

import com.darkxwn.tbstackable.util.CountFormatter;
import com.darkxwn.tbstackable.util.StackMath;

/** Простые проверки без JUnit: java LogicTest (завершается с ошибкой при провале). */
public class LogicTest {
    static int failed = 0;

    static void eq(Object expected, Object actual, String what) {
        if (!expected.equals(actual)) {
            failed++;
            System.out.println("FAIL " + what + ": expected " + expected + " but got " + actual);
        }
    }

    public static void main(String[] args) {
        // лимиты стака по тирам
        eq(96, StackTier.WOOD.applyTo(64), "wood 64");
        eq(128, StackTier.COPPER.applyTo(64), "copper 64");
        eq(256, StackTier.IRON.applyTo(64), "iron 64");
        eq(384, StackTier.GOLD.applyTo(64), "gold 64");
        eq(512, StackTier.DIAMOND.applyTo(64), "diamond 64");
        eq(1024, StackTier.NETHERITE.applyTo(64), "netherite 64");
        eq(24, StackTier.WOOD.applyTo(16), "wood 16");
        eq(256, StackTier.NETHERITE.applyTo(16), "netherite 16");
        for (StackTier t : StackTier.values()) eq(1, t.applyTo(1), "unstackable " + t);
        eq("1.5", StackTier.WOOD.multiplierText(), "text 1.5");
        eq("16", StackTier.NETHERITE.multiplierText(), "text 16");

        // упаковка счётчика
        eq(64, StackMath.storedCount(1024, 64), "stored 1024");
        eq(960, StackMath.extraCount(1024, 64), "extra 1024");
        eq(50, StackMath.storedCount(50, 64), "stored 50");
        eq(0, StackMath.extraCount(50, 64), "extra 50");
        for (int max : new int[]{1, 16, 64, 99}) {
            for (int count = 1; count <= 1024; count++) {
                int s = StackMath.storedCount(count, max), e = StackMath.extraCount(count, max);
                eq(count, StackMath.restore(s, e), "roundtrip " + count + "/" + max);
                if (s > 99 || s < 1) { failed++; System.out.println("FAIL stored out of codec range: " + s); }
            }
        }

        // форматирование
        eq("0", CountFormatter.format(0), "fmt 0");
        eq("999", CountFormatter.format(999), "fmt 999");
        eq("1k", CountFormatter.format(1000), "fmt 1000");
        eq("1.02k", CountFormatter.format(1024), "fmt 1024");
        eq("1.03k", CountFormatter.format(1030), "fmt 1030");
        eq("1.5k", CountFormatter.format(1500), "fmt 1500");
        eq("9.99k", CountFormatter.format(9999), "fmt 9999");
        eq("10k", CountFormatter.format(10000), "fmt 10000");
        eq("27.6k", CountFormatter.format(27648), "fmt 27648");
        eq("123k", CountFormatter.format(123456), "fmt 123456");
        eq("999k", CountFormatter.format(999999), "fmt 999999");
        eq("1M", CountFormatter.format(1_000_000), "fmt 1M");
        eq("1.05M", CountFormatter.format(1_050_000), "fmt 1.05M");
        eq("1.03к", CountFormatter.format(1030, "к", "М"), "fmt ru");

        if (failed > 0) { System.out.println(failed + " FAILED"); System.exit(1); }
        System.out.println("ALL TESTS PASSED");
    }
}
