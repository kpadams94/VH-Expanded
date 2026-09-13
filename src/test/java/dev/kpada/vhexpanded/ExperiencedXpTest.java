package dev.kpada.vhexpanded;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ExperiencedXpTest {
    @Test void everyApprovedRank() {
        int[] expected = {1000, 1100, 1200, 1300, 1400, 1500};
        for (int rank = 0; rank <= 5; rank++) assertEquals(expected[rank], ExperiencedXp.award(1000, rank));
    }
    @Test void multiplicativeStackingAfterOtherBonuses() { assertEquals(3000, ExperiencedXp.award(2000, 5)); }
    @Test void neverManufacturesEligibility() {
        for (int rank = 0; rank <= 5; rank++) {
            assertEquals(0, ExperiencedXp.award(0, rank));
            assertEquals(-1, ExperiencedXp.award(-1, rank));
        }
    }
    @Test void floorsOnlyFinalBonusUsingExactIntegerArithmetic() {
        assertEquals(16, ExperiencedXp.award(13, 3));
        assertEquals(151, ExperiencedXp.award(101, 5));
        assertEquals(1, ExperiencedXp.award(1, 5));
        assertEquals(110, ExperiencedXp.award(100, 1));
    }
    @Test void cannotWrapPositiveAwardOrAcceptInvalidRanks() {
        assertEquals(Integer.MAX_VALUE, ExperiencedXp.award(Integer.MAX_VALUE, 5));
        assertThrows(IllegalArgumentException.class, () -> ExperiencedXp.award(1, 6));
        assertThrows(IllegalArgumentException.class, () -> ExperiencedXp.award(1, -1));
    }
}
