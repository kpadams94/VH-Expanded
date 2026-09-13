package dev.kpada.vhexpanded;

import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RunAwardScopeTest {
    private final UUID player = UUID.randomUUID();
    @Test void ordinaryCallsAreUnchanged() { assertEquals(1000, RunAwardScope.apply(player, 1000)); }
    @Test void onlyIntendedRecipientAndOnlyOnce() {
        try (var scope = new RunAwardScope(player, 5)) {
            assertEquals(1000, RunAwardScope.apply(UUID.randomUUID(), 1000));
            assertEquals(1500, RunAwardScope.apply(player, 1000));
            assertEquals(1000, scope.beforeBonus());
            assertEquals(1500, scope.awarded());
            assertEquals(1000, RunAwardScope.apply(player, 1000));
        }
        assertEquals(1000, RunAwardScope.apply(player, 1000));
    }
    @Test void exceptionCannotLeakRunScope() {
        assertThrows(IllegalStateException.class, () -> {
            try (var scope = new RunAwardScope(player, 5)) { throw new IllegalStateException("test"); }
        });
        assertEquals(1000, RunAwardScope.apply(player, 1000));
    }
    @Test void nestedScopesRestoreOuterRecipient() {
        try (var outer = new RunAwardScope(player, 1)) {
            try (var inner = new RunAwardScope(UUID.randomUUID(), 5)) {
                assertEquals(1000, RunAwardScope.apply(player, 1000));
            }
            assertEquals(1100, RunAwardScope.apply(player, 1000));
        }
    }
    @Test void zeroAwardDoesNotBecomePositiveAndResetHasImmediateEffect() {
        try (var scope = new RunAwardScope(player, 5)) { assertEquals(0, RunAwardScope.apply(player, 0)); }
        try (var scope = new RunAwardScope(player, 0)) { assertEquals(1000, RunAwardScope.apply(player, 1000)); }
    }
}
