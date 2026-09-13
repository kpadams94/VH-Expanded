package dev.kpada.vhexpanded;

/** Pure calculation, after the native pipeline has produced its integer award. */
public final class ExperiencedXp {
    private ExperiencedXp() {}

    public static int award(int otherwiseEarned, int rank) {
        if (rank < 0 || rank > 5) throw new IllegalArgumentException("Experienced rank must be 0–5");
        if (otherwiseEarned <= 0) return otherwiseEarned;
        return (int) Math.min(Integer.MAX_VALUE, (long) otherwiseEarned * (10 + rank) / 10);
    }
}
