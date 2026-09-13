package dev.kpada.vhexpanded;
import dev.kpada.vhexpanded.repulsor.RepulsorBalance;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class RepulsorBalanceTest {
    @Test void approvedRowsAndDurationBoundaries() {
        int[] levels={1,3,4,7,8,12,15,16,17,20};
        float[] radii={1,2,2.5f,4,4.5f,6.5f,8,8.5f,9,10.5f};
        int[] duration={10,10,20,20,30,40,40,50,50,60};
        int[] cooldown={400,360,340,280,260,180,120,100,80,80};
        float[] barriers={1,1.5f,1.5f,2.5f,2.5f,3.5f,4.5f,4.5f,5,5.5f};
        int[] mana={15,19,21,27,29,37,43,45,47,53};
        for(int i=0;i<levels.length;i++) {
            assertEquals(radii[i],RepulsorBalance.radius(levels[i]));
            assertEquals(barriers[i],RepulsorBalance.barrierRadius(levels[i]));
            assertEquals(duration[i],RepulsorBalance.duration(levels[i]));
            assertEquals(cooldown[i],RepulsorBalance.cooldown(levels[i]));
            assertEquals(mana[i],RepulsorBalance.mana(levels[i]));
        }
    }
    @Test void overlevelsKeepScalingAfterCooldownFloor() {
        for(int l=18;l<=128;l++) {
            assertEquals(80,RepulsorBalance.cooldown(l));
            assertEquals(.5f,RepulsorBalance.radius(l)-RepulsorBalance.radius(l-1));
            assertEquals(l%2==1?.5f:0,RepulsorBalance.barrierRadius(l)-RepulsorBalance.barrierRadius(l-1));
            assertEquals(2,RepulsorBalance.mana(l)-RepulsorBalance.mana(l-1));
            assertEquals(l%4==0?10:0,RepulsorBalance.duration(l)-RepulsorBalance.duration(l-1));
        }
    }
    @Test void barrierStepsOnlyOnOddLevelsWhileRepelAlwaysGrows() {
        assertEquals(1,RepulsorBalance.barrierRadius(1));
        assertEquals(1,RepulsorBalance.barrierRadius(2));
        assertEquals(1.5f,RepulsorBalance.barrierRadius(3));
        assertEquals(1.5f,RepulsorBalance.barrierRadius(4));
        assertEquals(2,RepulsorBalance.barrierRadius(5));
        assertEquals(3,RepulsorBalance.radius(5));
    }
}
