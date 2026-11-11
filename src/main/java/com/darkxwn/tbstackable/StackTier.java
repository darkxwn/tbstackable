package com.darkxwn.tbstackable;

/** Тиры улучшения стаков. Порядок = порядок крафта (каждый следующий делается из предыдущего). */
public enum StackTier {
    WOOD("wood", 1.5),
    COPPER("copper", 2.0),
    IRON("iron", 4.0),
    GOLD("gold", 6.0),
    DIAMOND("diamond", 8.0),
    NETHERITE("netherite", 16.0);

    private final String id;
    private final double multiplier;

    StackTier(String id, double multiplier) {
        this.id = id;
        this.multiplier = multiplier;
    }

    public String itemName() {
        return id + "_stack_upgrade";
    }

    public double multiplier() {
        return multiplier;
    }

    /** "1.5" / "2" / "16" для тултипа. */
    public String multiplierText() {
        return multiplier == Math.floor(multiplier) ? String.valueOf((int) multiplier) : String.valueOf(multiplier);
    }

    /** Новый лимит стака для предмета с базовым лимитом baseMax. Нестакуемые (1) остаются 1. */
    public int applyTo(int baseMax) {
        if (baseMax <= 1) return baseMax;
        return (int) Math.floor(baseMax * multiplier);
    }
}
