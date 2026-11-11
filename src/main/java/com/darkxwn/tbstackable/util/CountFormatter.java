package com.darkxwn.tbstackable.util;

/** "1.03k", "27.6k", "123k", "1.05M". Округление вниз, чтобы не завышать количество. */
public final class CountFormatter {
    private CountFormatter() {}

    public static String format(long n) {
        return format(n, "k", "M");
    }

    public static String format(long n, String thousands, String millions) {
        if (n < 1000) return Long.toString(n);
        if (n < 1_000_000) return scaled(n, 1000, thousands);
        return scaled(n, 1_000_000, millions);
    }

    private static String scaled(long n, long unit, String suffix) {
        long whole = n / unit;
        long rest = n % unit;
        String frac = "";
        if (whole < 10) {
            frac = String.format("%02d", rest * 100 / unit);
        } else if (whole < 100) {
            frac = Long.toString(rest * 10 / unit);
        }
        frac = frac.replaceAll("0+$", "");
        return whole + (frac.isEmpty() ? "" : "." + frac) + suffix;
    }
}
