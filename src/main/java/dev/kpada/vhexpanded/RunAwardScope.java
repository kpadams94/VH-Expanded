package dev.kpada.vhexpanded;

import java.util.UUID;

/** A one-use recipient token, present only around the personal run-award call. */
public final class RunAwardScope implements AutoCloseable {
    private static final ThreadLocal<RunAwardScope> ACTIVE = new ThreadLocal<>();
    private final RunAwardScope previous;
    private final UUID recipient;
    private final int rank;
    private boolean used;
    private int beforeBonus;
    private int awarded;

    public RunAwardScope(UUID recipient, int rank) {
        this.previous = ACTIVE.get();
        this.recipient = recipient;
        this.rank = rank;
        ACTIVE.set(this);
    }

    public static int apply(UUID recipient, int nativeAward) {
        RunAwardScope scope = ACTIVE.get();
        if (scope == null || scope.used || !scope.recipient.equals(recipient)) return nativeAward;
        scope.used = true;
        scope.beforeBonus = nativeAward;
        scope.awarded = ExperiencedXp.award(nativeAward, scope.rank);
        return scope.awarded;
    }

    public int beforeBonus() { return beforeBonus; }
    public int awarded() { return awarded; }
    public boolean used() { return used; }

    @Override public void close() {
        if (previous == null) ACTIVE.remove(); else ACTIVE.set(previous);
    }
}
