package dev.kpada.vhexpanded.repulsor;

/** Raw values; native modifiers are applied by the ability/payment/UI pipeline. */
public final class RepulsorBalance {
    public record Tuning(float radiusBase, float radiusPerLevel, float barrierRadiusBase,
                         float barrierRadiusStep, int barrierEveryLevels, int durationBaseTicks,
                         int durationStepTicks, int durationEveryLevels, int cooldownInterceptTicks,
                         int cooldownPerLevelTicks, int cooldownFloorTicks, float manaBase, float manaPerLevel) {
        public Tuning {
            if(!Float.isFinite(radiusBase)||!Float.isFinite(radiusPerLevel)||!Float.isFinite(manaBase)||!Float.isFinite(manaPerLevel)
                    ||!Float.isFinite(barrierRadiusBase)||!Float.isFinite(barrierRadiusStep)
                    ||barrierRadiusBase<=0||barrierRadiusStep<0||barrierEveryLevels<1
                    ||radiusBase<=0||radiusPerLevel<0||durationBaseTicks<=0||durationStepTicks<0||durationEveryLevels<1
                    ||cooldownInterceptTicks<0||cooldownPerLevelTicks<0||cooldownFloorTicks<0||manaBase<0||manaPerLevel<0)
                throw new IllegalArgumentException("Invalid Repulsor tuning values");
        }
    }
    private static Tuning tuning=new Tuning(1,.5f,1,.5f,2,10,10,4,420,20,80,15,2);
    private RepulsorBalance() {}
    static void configure(Tuning values) { tuning=java.util.Objects.requireNonNull(values); }
    public static float radius(int level) { return tuning.radiusBase()+tuning.radiusPerLevel()*(level-1); }
    public static float barrierRadius(int level) { return tuning.barrierRadiusBase()+tuning.barrierRadiusStep()*((level-1)/tuning.barrierEveryLevels()); }
    public static int duration(int level) { return tuning.durationBaseTicks()+tuning.durationStepTicks()*(level/tuning.durationEveryLevels()); }
    public static int cooldown(int level) { return Math.max(tuning.cooldownFloorTicks(),tuning.cooldownInterceptTicks()-tuning.cooldownPerLevelTicks()*level); }
    public static float mana(int level) { return tuning.manaBase()+tuning.manaPerLevel()*(level-1); }
}
